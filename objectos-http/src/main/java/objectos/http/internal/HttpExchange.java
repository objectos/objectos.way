/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package objectos.http.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import objectos.http.Http;
import objectos.http.HttpProcessor;
import objectos.http.Response;
import objectos.http.Status;
import objectos.lang.Note1;
import objectos.lang.NoteSink;
import objectos.util.GrowableList;

public final class HttpExchange implements Runnable {

  class HeaderValue {

    final int start;

    final int end;

    HeaderValue(int start, int end) {
      this.start = start;
      this.end = end;
    }

    @Override
    public final String toString() {
      return new String(buffer, start, end - start, StandardCharsets.UTF_8);
    }

  }

  record RequestTarget(int start, int end) {}

  private record ResponseHeader(HeaderName name, String value) {
    public final byte[] bytes() {
      String text;
      text = name.name + ": " + value + "\r\n";

      return text.getBytes(StandardCharsets.UTF_8);
    }
  }

  private class ThisResponse implements Response {

    @Override
    public final void contentLength(long length) {
      if (length < 0) {
        throw new IllegalArgumentException("Length must be >= 0");
      }

      responseHeaders.add(
        new ResponseHeader(HeaderName.CONTENT_LENGTH, Long.toString(length))
      );
    }

    @Override
    public final void contentType(String value) {
      Objects.requireNonNull(value, "value == null");

      responseHeaders.add(
        new ResponseHeader(HeaderName.CONTENT_TYPE, value)
      );
    }

    @Override
    public final void date(ZonedDateTime date) {
      Objects.requireNonNull(date, "date == null");

      String value;
      value = Http.formatDate(date);

      responseHeaders.add(
        new ResponseHeader(HeaderName.DATE, value)
      );
    }

    @Override
    public final void send(byte[] bytes) {
      Objects.requireNonNull(bytes, "bytes == null");

      responseBytes = bytes;
    }

  }

  public static final Note1<IOException> EIO_READ_ERROR = Note1.error();

  static final byte _STOP = 0,

      // Setup phase

      _SETUP = 1,

      // Input phase

      _INPUT = 2,
      _INPUT_READ = 3,

      // Input / Request Line phase

      _REQUEST_LINE = 4,
      _REQUEST_LINE_METHOD = 5,
      _REQUEST_LINE_METHOD_P = 6,
      _REQUEST_LINE_TARGET = 7,
      _REQUEST_LINE_VERSION = 8,

      // Input / Parse header phase

      _PARSE_HEADER = 9,

      // Output phase

      _CLIENT_ERROR = 10,

      //

      _CLOSE = 2,

      _FINALLY = 3,

      _PARSE_HEADER_NAME = 12,
      _PARSE_HEADER_VALUE = 13,

      _PROCESS = 10,

      _RESPONSE_BODY = 14,
      _RESPONSE_HEADER_BUFFER = 15,
      _RESPONSE_HEADER_WRITE_FULL = 16,
      _RESPONSE_HEADER_WRITE_PARTIAL = 17,

      _SOCKET_WRITE = 18;

  byte[] buffer;

  int bufferIndex;

  int bufferLimit;

  Throwable error;

  Method method;

  byte nextAction;

  NoteSink noteSink;

  HttpProcessor processor;

  HeaderName requestHeaderName;

  Map<HeaderName, HeaderValue> requestHeaders;

  RequestTarget requestTarget;

  private ThisResponse response;

  byte[] responseBytes;

  List<ResponseHeader> responseHeaders;

  int responseHeadersIndex;

  Socket socket;

  byte state;

  Status status;

  byte versionMajor;

  byte versionMinor;

  public HttpExchange(int bufferSize,
                      NoteSink noteSink,
                      HttpProcessor processor,
                      Socket socket) {
    this.buffer = new byte[bufferSize];

    this.noteSink = noteSink;

    this.processor = processor;

    this.socket = socket;

    state = _SETUP;
  }

  HttpExchange() {}

  @Override
  public final void run() {
    while (isActive()) {
      stepOne();
    }
  }

  final boolean isActive() {
    return state != _STOP;
  }

  final void stepOne() {
    state = switch (state) {
      // Setup phase

      case _SETUP -> setup();

      // Input phase

      case _INPUT -> input();
      case _INPUT_READ -> inputRead();

      // Input / Request Line phase

      case _REQUEST_LINE -> requestLine();
      case _REQUEST_LINE_METHOD -> requestLineMethod();
      case _REQUEST_LINE_TARGET -> requestLineTarget();
      case _REQUEST_LINE_VERSION -> requestLineVersion();

      // Input / Parse Header phase

      case _PARSE_HEADER -> parseHeader();

      //

      case _PROCESS -> executeProcess();

      case _PARSE_HEADER_NAME -> executeRequestHeaderName();

      case _PARSE_HEADER_VALUE -> executeRequestHeaderValue();

      case _RESPONSE_HEADER_BUFFER -> executeResponseHeaderBuffer();

      case _RESPONSE_HEADER_WRITE_FULL -> executeResponseHeaderWriteFull();

      case _RESPONSE_HEADER_WRITE_PARTIAL -> executeResponseHeaderWritePartial();

      default -> throw new UnsupportedOperationException(
        "Implement me :: state=" + state
      );
    };
  }

  private boolean bufferEquals(byte[] target, int start) {
    return Arrays.equals(
      buffer, start, start + target.length,
      target, 0, target.length
    );
  }

  private byte bufferGet(int index) {
    return buffer[index];
  }

  private boolean bufferHasIndex(int index) {
    return index < bufferLimit;
  }

  private byte executeProcess() {
    response = new ThisResponse();

    responseHeaders = new GrowableList<>();

    try {
      processor.process(null, response);
    } catch (Exception e) {
      error = e;

      return toResult(Status.INTERNAL_SERVER_ERROR);
    }

    if (responseHeaders.isEmpty()) {
      throw new UnsupportedOperationException("Implement me");
    }

    // we'll start at the first response header

    responseHeadersIndex = 0;

    return toResponseHeaderBuffer();
  }

  private byte executeRequestHeaderName() {
    final int nameStart;
    nameStart = bufferIndex;

    int index;
    index = nameStart;

    boolean found;
    found = false;

    for (; bufferHasIndex(index); index++) {
      byte b;
      b = bufferGet(index);

      if (b == Bytes.COLON) {
        found = true;

        break;
      }
    }

    if (!found) {
      // TODO header name limit

      return toInputRead(state);
    }

    byte first;
    first = bufferGet(nameStart);

    HeaderName maybe;
    maybe = switch (first) {
      case 'H' -> HeaderName.HOST;

      default -> null;
    };

    if (maybe != null && bufferEquals(maybe.bytes, nameStart)) {
      requestHeaderName = maybe;

      bufferIndex = index + 1;

      return _PARSE_HEADER_VALUE;
    }

    throw new UnsupportedOperationException("Implement me");
  }

  private byte executeRequestHeaderValue() {
    int maybeStart;
    maybeStart = bufferIndex;

    // we search for the LF char that marks the end-of-line

    int index;
    index = maybeStart;

    boolean found;
    found = false;

    for (; bufferHasIndex(index); index++) {
      byte b;
      b = bufferGet(index);

      if (b == Bytes.LF) {
        found = true;

        break;
      }
    }

    if (!found) {
      // LF was not found
      // TODO field length limit

      return toInputRead(state);
    }

    // we check if CR is found before the LF

    int lineFeedIndex;
    lineFeedIndex = index;

    int carriageIndex;
    carriageIndex = lineFeedIndex - 1;

    if (bufferGet(carriageIndex) != Bytes.CR) {
      return toResult(Status.BAD_REQUEST);
    }

    // we trim the OWS, if found, from the start of the value

    final int valueStart;

    byte maybeOws;
    maybeOws = bufferGet(maybeStart);

    if (Bytes.isOptionalWhitespace(maybeOws)) {
      valueStart = maybeStart + 1;
    } else {
      valueStart = maybeStart;
    }

    // the value string ends immediately before the CR char

    final int valueEnd;
    valueEnd = carriageIndex;

    HeaderValue headerValue;
    headerValue = new HeaderValue(valueStart, valueEnd);

    HeaderValue previousValue;
    previousValue = requestHeaders.put(requestHeaderName, headerValue);

    if (previousValue != null) {
      throw new UnsupportedOperationException("Implement me");
    }

    // we have found the value.
    // bufferIndex should point to the position immediately after the LF char

    bufferIndex = lineFeedIndex + 1;

    return _PARSE_HEADER;
  }

  private byte executeResponseHeaderBuffer() {
    // we reset out buffer

    bufferIndex = bufferLimit = 0;

    // assume buffer will be large enough for headers

    byte nextStep;
    nextStep = _RESPONSE_HEADER_WRITE_FULL;

    int responseHeadersSize;
    responseHeadersSize = responseHeaders.size();

    while (responseHeadersIndex < responseHeadersSize) {
      ResponseHeader header;
      header = responseHeaders.get(responseHeadersIndex);

      byte[] bytes;
      bytes = header.bytes();

      if (bytes.length > buffer.length) {
        // TODO handle response header too large

        throw new UnsupportedOperationException(
          "Implement me :: buffer not large enough"
        );
      }

      int newLimit;
      newLimit = bufferLimit + bytes.length;

      if (newLimit > buffer.length) {
        // buffer is full so we write out the current contents

        nextStep = _RESPONSE_HEADER_WRITE_PARTIAL;

        break;
      }

      System.arraycopy(bytes, 0, buffer, bufferLimit, bytes.length);

      bufferLimit = newLimit;

      responseHeadersIndex++;
    }

    return nextStep;
  }

  private byte executeResponseHeaderWriteFull() {
    boolean separatorWritten;

    int bufferRemaining;
    bufferRemaining = buffer.length - bufferLimit;

    if (bufferRemaining < 2) {
      separatorWritten = false;
    } else {
      buffer[bufferLimit++] = Bytes.CR;
      buffer[bufferLimit++] = Bytes.LF;

      separatorWritten = true;
    }

    try {
      OutputStream outputStream;
      outputStream = socket.getOutputStream();

      outputStream.write(buffer, 0, bufferLimit);

      if (!separatorWritten) {
        buffer[0] = Bytes.CR;
        buffer[1] = Bytes.LF;

        outputStream.write(buffer, 0, 2);
      }

      return toResponseHeaderBuffer();
    } catch (IOException e) {
      return toClose(e);
    }
  }

  private byte executeResponseHeaderWritePartial() {
    try {
      OutputStream outputStream;
      outputStream = socket.getOutputStream();

      outputStream.write(buffer, 0, bufferLimit);

      return toResponseHeaderBuffer();
    } catch (IOException e) {
      return toClose(e);
    }
  }

  //

  private byte input() {
    nextAction = _REQUEST_LINE;

    return inputRead();
  }

  private byte inputRead() {
    InputStream inputStream;

    try {
      inputStream = socket.getInputStream();
    } catch (IOException e) {
      return toIoReadError(e);
    }

    int writableLength;
    writableLength = buffer.length - bufferLimit;

    int bytesRead;

    try {
      bytesRead = inputStream.read(buffer, bufferLimit, writableLength);
    } catch (IOException e) {
      return toIoReadError(e);
    }

    if (bytesRead < 0) {
      throw new UnsupportedOperationException("Implement me");
    }

    bufferLimit += bytesRead;

    return nextAction;
  }

  private byte parseHeader() {
    // if the buffer matches CR + LF then header has ended
    // otherwise, we will try to parse the header field name

    int index;
    index = bufferIndex;

    int requiredIndex;
    requiredIndex = index + 1;

    if (!bufferHasIndex(requiredIndex)) {
      // ask for more data

      return toInputRead(state);
    }

    // we'll check if the current char is CR

    final byte first;
    first = bufferGet(index++);

    if (first != Bytes.CR) {
      // not CR, process as field name

      return _PARSE_HEADER_NAME;
    }

    // we'll check if CR is followed by LF

    final byte second;
    second = bufferGet(index++);

    if (second != Bytes.LF) {
      // not LF, reject as a bad request

      return toResult(Status.BAD_REQUEST);
    }

    // ok, we are at the end of header
    // bufferIndex will resume immediately after the LF

    bufferIndex = index;

    // next action depends on the method

    return switch (method) {
      case GET -> _PROCESS;

      default -> throw new UnsupportedOperationException("Implement me");
    };
  }

  private byte requestLine() {
    int methodStart;
    methodStart = bufferIndex;

    // the next call is safe.
    //
    // if we got here, InputStream::read returned at least 1 byte
    // InputStream::read only returns 0 when len == 0 in read(array, off, len)
    // otherwise it returns > 0, or -1 when EOF or throws IOException

    byte first;
    first = bufferGet(methodStart);

    // based on the first char, we select out method candidate

    return switch (first) {
      case 'C' -> toRequestLineMethod(Method.CONNECT);

      case 'D' -> toRequestLineMethod(Method.DELETE);

      case 'G' -> toRequestLineMethod(Method.GET);

      case 'H' -> toRequestLineMethod(Method.HEAD);

      case 'O' -> toRequestLineMethod(Method.OPTIONS);

      case 'P' -> _REQUEST_LINE_METHOD_P;

      case 'T' -> toRequestLineMethod(Method.TRACE);

      // first char does not match any candidate
      // we are sure this is a bad request

      default -> toClientError(Status.BAD_REQUEST);
    };
  }

  private byte requestLineMethod() {
    // method candidate @ start of the buffer

    int candidateStart;
    candidateStart = bufferIndex;

    // we'll check if the buffer contents matches 'METHOD SP'

    byte[] candidateBytes;
    candidateBytes = method.nameAndSpace;

    int requiredIndex;
    requiredIndex = candidateStart + candidateBytes.length;

    if (!bufferHasIndex(requiredIndex)) {
      // we don't have enough bytes in the buffer...
      // assuming the client is slow on sending data

      // clear method candidate just in case...
      method = null;

      return toInputRead(state);
    }

    if (!bufferEquals(candidateBytes, candidateStart)) {
      // we have enough bytes and they don't match our 'method name + SP'
      // respond with bad request

      // clear method candidate just in case...
      method = null;

      return toClientError(Status.BAD_REQUEST);
    }

    // request OK so far...
    // update the bufferIndex

    bufferIndex = requiredIndex;

    // continue to request target

    return _REQUEST_LINE_TARGET;
  }

  private byte requestLineTarget() {
    // we will look for the first SP char

    int targetStart;
    targetStart = bufferIndex;

    for (int index = targetStart; bufferHasIndex(index); index++) {
      byte b;
      b = bufferGet(index);

      if (b == Bytes.SP) {
        // SP found, store the indices

        requestTarget = new RequestTarget(targetStart, index);

        // bufferIndex immediately after the SP char

        bufferIndex = index + 1;

        return _REQUEST_LINE_VERSION;
      }
    }

    // SP char was not found...

    if (bufferLimit < buffer.length) {
      // buffer can still hold data
      // assume client is slow

      return toInputRead(state);
    }

    // buffer is full
    // URI is too long to process

    return toClientError(Status.URI_TOO_LONG);
  }

  private byte requestLineVersion() {
    int versionStart;
    versionStart = bufferIndex;

    // 'H' 'T' 'T' 'P' '/' '1' '.' '1' = 8 bytes

    int versionLength;
    versionLength = 8;

    int versionEnd;
    versionEnd = versionStart + versionLength;

    // versionEnd is @ CR
    // lineEnd is @ LF

    int lineEnd;
    lineEnd = versionEnd + 1;

    if (!bufferHasIndex(lineEnd)) {
      if (bufferLimit < buffer.length) {
        // buffer can still hold data
        // assume client is slow

        return toInputRead(state);
      }

      // buffer is full
      // assume URI was too long to process

      return toClientError(Status.URI_TOO_LONG);
    }

    int index;
    index = versionStart;

    if (buffer[index++] != 'H' ||
        buffer[index++] != 'T' ||
        buffer[index++] != 'T' ||
        buffer[index++] != 'P' ||
        buffer[index++] != '/') {

      // buffer does not start with 'HTTP/' => bad request

      return toClientError(Status.BAD_REQUEST);
    }

    byte maybeMajor;
    maybeMajor = buffer[index++];

    if (!Bytes.isDigit(maybeMajor)) {
      // major version is not a digit => bad request

      return toClientError(Status.BAD_REQUEST);
    }

    if (buffer[index++] != '.') {
      // major version not followed by a DOT => bad request

      return toClientError(Status.BAD_REQUEST);
    }

    versionMajor = (byte) (maybeMajor - 0x30);

    byte maybeMinor;
    maybeMinor = buffer[index++];

    if (!Bytes.isDigit(maybeMinor)) {
      // minor version is not a digit => bad request

      return toClientError(Status.BAD_REQUEST);
    }

    versionMinor = (byte) (maybeMinor - 0x30);

    if (buffer[index++] != Bytes.CR ||
        buffer[index++] != Bytes.LF) {
      // no line terminator after version => bad request

      return toClientError(Status.BAD_REQUEST);
    }

    // bufferIndex resumes immediately after CR LF

    bufferIndex = index;

    return _PARSE_HEADER;
  }
  private byte setup() {
    // TODO set timeout

    // we ensure the buffer is reset

    bufferIndex = bufferLimit = 0;

    return _INPUT;
  }

  //

  private byte toClientError(Status error) {
    status = error;

    return _CLIENT_ERROR;
  }

  private byte toInputRead(byte onRead) {
    nextAction = onRead;

    return _INPUT_READ;
  }

  private byte toRequestLineMethod(Method maybe) {
    method = maybe;

    return _REQUEST_LINE_METHOD;
  }

  //

  private byte toClose(IOException e) {
    error = e;

    return _CLOSE;
  }

  private byte toIoReadError(IOException e) {
    error = e;

    noteSink.send(EIO_READ_ERROR, e);

    return _CLOSE;
  }

  private byte toResponseHeaderBuffer() {
    // we reset our buffer

    bufferIndex = bufferLimit = 0;

    return _RESPONSE_HEADER_BUFFER;
  }

  private byte toResult(Status status) {
    return _CLOSE;
  }

}