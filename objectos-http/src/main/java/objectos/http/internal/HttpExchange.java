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
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import objectos.http.Http;
import objectos.http.Http.Handler;
import objectos.http.Http.Response;
import objectos.lang.Note1;
import objectos.lang.NoteSink;
import objectos.util.GrowableList;

public final class HttpExchange implements Http.Exchange, Runnable {

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
      _PARSE_HEADER_NAME = 10,
      _PARSE_HEADER_NAME_CASE_INSENSITIVE = 11,
      _PARSE_HEADER_VALUE = 12,

      // Handle phase

      _HANDLE = 13,

      // Output phase

      _CLIENT_ERROR = 14,
      _OUTPUT = 15,
      _OUTPUT_BODY = 16,
      _OUTPUT_BUFFER = 17,
      _OUTPUT_HEADER = 18,

      //

      _CLOSE = 2,

      _FINALLY = 3;

  byte[] buffer;

  int bufferIndex;

  int bufferLimit;

  Throwable error;

  Handler handler;

  Method method;

  byte nextAction;

  NoteSink noteSink;

  HeaderName requestHeaderName;

  Map<HeaderName, HeaderValue> requestHeaders;

  HttpRequestTarget requestTarget;

  private HttpResponse response;

  byte[] responseBytes;

  List<HttpResponseHeader> responseHeaders;

  int responseHeadersIndex;

  Socket socket;

  byte state;

  Http.Status status;

  byte versionMajor;

  byte versionMinor;

  public HttpExchange(int bufferSize,
                      Handler handler,
                      NoteSink noteSink,
                      Socket socket) {
    this.buffer = new byte[bufferSize];

    this.handler = handler;

    this.noteSink = noteSink;

    this.socket = socket;

    state = _SETUP;
  }

  HttpExchange() {}

  @Override
  public final Response response() {
    // TODO check state

    if (response == null) {
      response = new HttpResponse(this);
    }

    return response;
  }

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
      case _PARSE_HEADER_NAME -> parseHeaderName();
      case _PARSE_HEADER_VALUE -> parseHeaderValue();

      // Handle phase

      case _HANDLE -> handle();

      // Output phase

      case _OUTPUT -> output();
      case _OUTPUT_HEADER -> outputHeader();

      default -> throw new UnsupportedOperationException(
        "Implement me :: state=" + state
      );
    };
  }

  private boolean bufferEquals(byte[] target, int start) {
    int requiredIndex;
    requiredIndex = start + target.length;

    if (!bufferHasIndex(requiredIndex)) {
      return false;
    }

    return Arrays.equals(
      buffer, start, requiredIndex,
      target, 0, target.length);
  }

  private byte bufferGet(int index) {
    return buffer[index];
  }

  private boolean bufferHasIndex(int index) {
    return index < bufferLimit;
  }

  private byte handle() {
    try {
      responseBytes = null;

      if (responseHeaders == null) {
        responseHeaders = new GrowableList<>();
      } else {
        responseHeaders.clear();
      }

      handler.handle(this);

      return _OUTPUT;
    } catch (Throwable t) {
      error = t;

      return toClientError(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  private byte input() {
    nextAction = _REQUEST_LINE;

    return inputRead();
  }

  private byte inputRead() {
    InputStream inputStream;

    try {
      inputStream = socket.getInputStream();
    } catch (IOException e) {
      return toInputReadError(e);
    }

    int writableLength;
    writableLength = buffer.length - bufferLimit;

    int bytesRead;

    try {
      bytesRead = inputStream.read(buffer, bufferLimit, writableLength);
    } catch (IOException e) {
      return toInputReadError(e);
    }

    if (bytesRead < 0) {
      throw new UnsupportedOperationException("Implement me");
    }

    bufferLimit += bytesRead;

    return nextAction;
  }

  private byte output() {
    bufferIndex = bufferLimit = responseHeadersIndex = 0;

    return _OUTPUT_HEADER;
  }

  private byte outputHeader() {
    if (responseHeadersIndex == responseHeaders.size()) {
      nextAction = _OUTPUT_BODY;

      return _OUTPUT_BUFFER;
    }

    HttpResponseHeader header;
    header = responseHeaders.get(responseHeadersIndex);

    byte[] headerBytes;
    headerBytes = header.bytes();

    int headerLength;
    headerLength = headerBytes.length;

    int bufferRequiredIndex;
    bufferRequiredIndex = bufferLimit + headerLength;

    if (bufferRequiredIndex >= buffer.length) {
      nextAction = _OUTPUT_HEADER;

      return _OUTPUT_BUFFER;
    }

    System.arraycopy(headerBytes, 0, buffer, bufferLimit, headerLength);

    bufferLimit += headerLength;

    responseHeadersIndex++;

    return _OUTPUT_HEADER;
  }

  private byte parseHeader() {
    // if the buffer matches CRLF or LF then header has ended
    // otherwise, we will try to parse the header field name

    int index;
    index = bufferIndex;

    if (!bufferHasIndex(index)) {
      // ask for more data

      return toInputReadIfPossible(state, HttpStatus.BAD_REQUEST);
    }

    // we'll check if buffer is CRLF

    final byte first;
    first = bufferGet(index++);

    if (first == Bytes.CR) {
      // ok, first is CR

      if (!bufferHasIndex(index)) {
        // ask for more data

        return toInputReadIfPossible(state, HttpStatus.BAD_REQUEST);
      }

      final byte second;
      second = bufferGet(index++);

      if (second == Bytes.LF) {
        bufferIndex = index;

        return _HANDLE;
      }

      // not sure the best way to handle this case

      return _PARSE_HEADER_NAME;
    }

    if (first == Bytes.LF) {
      bufferIndex = index;

      return _HANDLE;
    }

    return _PARSE_HEADER_NAME;
  }

  private byte parseHeaderName() {
    // we reset any previous found header name

    requestHeaderName = null;

    // we will search the buffer for a ':' char

    final int nameStart;
    nameStart = bufferIndex;

    int colonIndex;
    colonIndex = nameStart;

    boolean found;
    found = false;

    for (; bufferHasIndex(colonIndex); colonIndex++) {
      byte b;
      b = bufferGet(colonIndex);

      if (b == Bytes.COLON) {
        found = true;

        break;
      }
    }

    if (!found) {
      // ':' was not found
      // read more data if possible

      return toInputReadIfPossible(state, HttpStatus.BAD_REQUEST);
    }

    // we will use the first char as hash code

    final byte first;
    first = bufferGet(nameStart);

    // ad hoc hash map

    return switch (first) {
      case 'A' -> parseHeaderName0(colonIndex,
        HeaderName.ACCEPT_ENCODING
      );

      case 'C' -> parseHeaderName0(colonIndex,
        HeaderName.CONNECTION,
        HeaderName.CONTENT_LENGTH,
        HeaderName.CONTENT_TYPE
      );

      case 'D' -> parseHeaderName0(colonIndex,
        HeaderName.DATE
      );

      case 'H' -> parseHeaderName0(colonIndex,
        HeaderName.HOST
      );

      case 'U' -> parseHeaderName0(colonIndex,
        HeaderName.USER_AGENT
      );

      default -> _PARSE_HEADER_NAME_CASE_INSENSITIVE;
    };
  }

  private byte parseHeaderName0(int colonIndex, HeaderName candidate) {
    final byte[] candidateBytes;
    candidateBytes = candidate.bytes;

    if (bufferEquals(candidateBytes, bufferIndex)) {
      requestHeaderName = candidate;

      // bufferIndex will resume immediately after colon

      bufferIndex = colonIndex + 1;

      return _PARSE_HEADER_VALUE;
    }

    return _PARSE_HEADER_NAME_CASE_INSENSITIVE;
  }

  private byte parseHeaderName0(int colonIndex, HeaderName c0, HeaderName c1, HeaderName c2) {
    byte result;
    result = parseHeaderName0(colonIndex, c0);

    if (result == _PARSE_HEADER_VALUE) {
      return result;
    }

    result = parseHeaderName0(colonIndex, c1);

    if (result == _PARSE_HEADER_VALUE) {
      return result;
    }

    return parseHeaderName0(colonIndex, c2);
  }

  private byte parseHeaderValue() {
    int valueStart;
    valueStart = bufferIndex;

    // we search for the LF char that marks the end-of-line

    int lfIndex = valueStart;

    boolean found;
    found = false;

    for (; bufferHasIndex(lfIndex); lfIndex++) {
      byte b;
      b = bufferGet(lfIndex);

      if (b == Bytes.LF) {
        found = true;

        break;
      }
    }

    if (!found) {
      // LF was not found

      return toInputReadIfPossible(state, HttpStatus.BAD_REQUEST);
    }

    // we'll trim any SP, HTAB or CR from the end of the value

    int valueEnd;
    valueEnd = lfIndex;

    loop: for (; valueEnd > valueStart; valueEnd--) {
      byte b;
      b = bufferGet(valueEnd - 1);

      switch (b) {
        case Bytes.SP, Bytes.HTAB, Bytes.CR -> {
          continue loop;
        }

        default -> {
          break loop;
        }
      }
    }

    // we'll trim any OWS, if found, from the start of the value

    for (; valueStart < valueEnd; valueStart++) {
      byte b;
      b = bufferGet(valueStart);

      if (!Bytes.isOptionalWhitespace(b)) {
        break;
      }
    }

    HeaderValue headerValue;
    headerValue = new HeaderValue(valueStart, valueEnd);

    if (requestHeaders == null) {
      requestHeaders = new EnumMap<>(HeaderName.class);
    }

    HeaderValue previousValue;
    previousValue = requestHeaders.put(requestHeaderName, headerValue);

    if (previousValue != null) {
      throw new UnsupportedOperationException("Implement me");
    }

    // we have found the value.
    // bufferIndex should point to the position immediately after the LF char

    bufferIndex = lfIndex + 1;

    return _PARSE_HEADER;
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

      default -> toClientError(HttpStatus.BAD_REQUEST);
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

      return toClientError(HttpStatus.BAD_REQUEST);
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

        requestTarget = new HttpRequestTarget(targetStart, index);

        // bufferIndex immediately after the SP char

        bufferIndex = index + 1;

        return _REQUEST_LINE_VERSION;
      }
    }

    // SP char was not found.
    // Read more data if possible

    return toInputReadIfPossible(state, HttpStatus.URI_TOO_LONG);
  }

  private byte requestLineVersion() {
    int versionStart;
    versionStart = bufferIndex;

    // 'H' 'T' 'T' 'P' '/' '1' '.' '1' = 8 bytes

    int versionLength;
    versionLength = 8;

    int versionEnd;
    versionEnd = versionStart + versionLength - 1;

    // versionEnd is @ CR
    // lineEnd is @ LF

    int lineEnd;
    lineEnd = versionEnd + 2;

    if (!bufferHasIndex(lineEnd)) {
      return toInputReadIfPossible(state, HttpStatus.URI_TOO_LONG);
    }

    int index;
    index = versionStart;

    if (buffer[index++] != 'H' ||
        buffer[index++] != 'T' ||
        buffer[index++] != 'T' ||
        buffer[index++] != 'P' ||
        buffer[index++] != '/') {

      // buffer does not start with 'HTTP/' => bad request

      return toClientError(HttpStatus.BAD_REQUEST);
    }

    byte maybeMajor;
    maybeMajor = buffer[index++];

    if (!Bytes.isDigit(maybeMajor)) {
      // major version is not a digit => bad request

      return toClientError(HttpStatus.BAD_REQUEST);
    }

    if (buffer[index++] != '.') {
      // major version not followed by a DOT => bad request

      return toClientError(HttpStatus.BAD_REQUEST);
    }

    versionMajor = (byte) (maybeMajor - 0x30);

    byte maybeMinor;
    maybeMinor = buffer[index++];

    if (!Bytes.isDigit(maybeMinor)) {
      // minor version is not a digit => bad request

      return toClientError(HttpStatus.BAD_REQUEST);
    }

    versionMinor = (byte) (maybeMinor - 0x30);

    byte crOrLf;
    crOrLf = buffer[index++];

    if (crOrLf == Bytes.CR && buffer[index++] == Bytes.LF) {
      // bufferIndex resumes immediately after CRLF

      bufferIndex = index;

      return _PARSE_HEADER;
    }

    if (crOrLf == Bytes.LF) {
      // bufferIndex resumes immediately after LF

      bufferIndex = index;

      return _PARSE_HEADER;
    }

    // no line terminator after version => bad request

    return toClientError(HttpStatus.BAD_REQUEST);
  }

  private byte setup() {
    // TODO set timeout

    // we ensure the buffer is reset

    bufferIndex = bufferLimit = 0;

    return _INPUT;
  }

  private byte toClientError(Http.Status error) {
    status = error;

    return _CLIENT_ERROR;
  }

  private byte toInputRead(byte onRead) {
    nextAction = onRead;

    return _INPUT_READ;
  }

  private byte toInputReadError(IOException e) {
    error = e;

    noteSink.send(EIO_READ_ERROR, e);

    return _CLOSE;
  }

  private byte toInputReadIfPossible(byte onRead, Http.Status onBufferFull) {
    if (bufferLimit < buffer.length) {
      return toInputRead(onRead);
    }

    if (bufferLimit == buffer.length) {
      return toClientError(onBufferFull);
    }

    // buffer limit overflow!!!
    // programming error

    throw new UnsupportedOperationException(
      "Implement me :: Internal Server Error"
    );
  }

  private byte toRequestLineMethod(Method maybe) {
    method = maybe;

    return _REQUEST_LINE_METHOD;
  }

}