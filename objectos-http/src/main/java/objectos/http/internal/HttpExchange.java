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
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import objectos.http.Http;
import objectos.http.HttpProcessor;
import objectos.http.Response;
import objectos.http.Status;
import objectos.http.internal.HttpExchange.Result.StatusResult;
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

  sealed interface Result {
    record StatusResult(Status status) implements Result {}
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

  static final byte _STOP = 0,

      _BAD_REQUEST = 1,

      _CLOSE = 2,

      _FINALLY = 3,

      _IO_READ = 4,

      _PARSE_METHOD = 9,

      _PROCESS = 5,

      _REQUEST_HEADER = 6,

      _REQUEST_HEADER_NAME = 7,

      _REQUEST_HEADER_VALUE = 8,

      _REQUEST_TARGET = 10,

      _REQUEST_VERSION = 11,

      _RESPONSE_BODY = 12,

      _RESPONSE_HEADER_BUFFER = 13,

      _RESPONSE_HEADER_WRITE_FULL = 14,

      _RESPONSE_HEADER_WRITE_PARTIAL = 15,

      _SOCKET_WRITE = 16,

      _START = 17;

  private final byte[] buffer;

  int bufferIndex;

  int bufferLimit;

  @SuppressWarnings("unused")
  private Throwable error;

  Method method;

  byte nextAction;

  @SuppressWarnings("unused")
  private final NoteSink noteSink;

  private final HttpProcessor processor;

  HeaderName requestHeaderName;

  Map<HeaderName, HeaderValue> requestHeaders;

  RequestTarget requestTarget;

  private ThisResponse response;

  byte[] responseBytes;

  List<ResponseHeader> responseHeaders;

  int responseHeadersIndex;

  private Result result;

  private final Socket socket;

  byte state;

  Version version;

  public HttpExchange(int bufferSize,
                      NoteSink noteSink,
                      HttpProcessor processor,
                      Socket socket) {
    this.buffer = new byte[bufferSize];

    // we set the buffer values to -1
    // to check if the START state works correctly

    bufferIndex = -1;

    bufferLimit = -1;

    this.noteSink = noteSink;

    this.processor = processor;

    this.socket = socket;

    state = _START;
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
      case _IO_READ -> executeIoRead();

      case _PROCESS -> executeProcess();

      case _REQUEST_HEADER -> executeRequestHeader();

      case _REQUEST_HEADER_NAME -> executeRequestHeaderName();

      case _REQUEST_HEADER_VALUE -> executeRequestHeaderValue();

      case _PARSE_METHOD -> executeRequestMethod();

      case _REQUEST_TARGET -> executeRequestTarget();

      case _REQUEST_VERSION -> executeRequestVersion();

      case _RESPONSE_HEADER_BUFFER -> executeResponseHeaderBuffer();

      case _RESPONSE_HEADER_WRITE_FULL -> executeResponseHeaderWriteFull();

      case _RESPONSE_HEADER_WRITE_PARTIAL -> executeResponseHeaderWritePartial();

      case _START -> executeStart();

      default -> throw new UnsupportedOperationException(
        "Implement me :: state=" + state
      );
    };
  }

  private void bufferCompact() {
    int redableLength;
    redableLength = bufferLimit - bufferIndex;

    System.arraycopy(buffer, bufferIndex, buffer, 0, redableLength);

    bufferIndex = 0;

    bufferLimit = redableLength;
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

  private byte executeIoRead() {
    if (bufferIndex > 0) {
      bufferCompact();
    }

    InputStream inputStream;

    try {
      inputStream = socket.getInputStream();
    } catch (IOException e) {
      return toClose(e);
    }

    int bufferWritable;
    bufferWritable = buffer.length - bufferLimit;

    int bytesRead;

    try {
      bytesRead = inputStream.read(buffer, bufferLimit, bufferWritable);
    } catch (IOException e) {
      return toClose(e);
    }

    if (bytesRead < 0) {
      throw new UnsupportedOperationException("Implement me");
    }

    bufferLimit += bytesRead;

    return nextAction;
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

  private byte executeRequestHeader() {
    int index;
    index = bufferIndex;

    if (!bufferHasIndex(index)) {
      return toIoRead(state);
    }

    // we'll check if the current char is CR

    final byte first;
    first = bufferGet(index);

    if (first != Bytes.CR) {
      // not CR, process as field name

      return _REQUEST_HEADER_NAME;
    }

    index++;

    if (!bufferHasIndex(index)) {
      return toIoRead(state);
    }

    // we'll check if CR is followed by LF

    final byte second;
    second = bufferGet(index);

    if (second != Bytes.LF) {
      // not LF, reject as a bad request

      return toResult(Status.BAD_REQUEST);
    }

    // ok, we are at the end of header
    // bufferIndex will resume immediately after the LF

    bufferIndex = index + 1;

    // next action depends on the method

    return switch (method) {
      case GET -> _PROCESS;

      default -> throw new UnsupportedOperationException("Implement me");
    };
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

      return toIoRead(state);
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

      return _REQUEST_HEADER_VALUE;
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

      return toIoRead(state);
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

    return _REQUEST_HEADER;
  }

  private byte executeRequestMethod() {
    int start = bufferIndex;

    if (!bufferHasIndex(start)) {
      return toIoRead(state);
    }

    byte first;
    first = bufferGet(start);

    Method maybeMethod;
    maybeMethod = switch (first) {
      case 'G' -> Method.GET;

      default -> null;
    };

    if (maybeMethod == null) {
      return toResult(Status.BAD_REQUEST);
    }

    byte[] methodBytes;
    methodBytes = maybeMethod.bytes;

    if (!bufferEquals(methodBytes, start)) {
      return toResult(Status.NOT_IMPLEMENTED);
    }

    int spaceIndex;
    spaceIndex = start + methodBytes.length;

    if (!bufferHasIndex(spaceIndex)) {
      return toIoRead(state);
    }

    byte maybeSpace;
    maybeSpace = bufferGet(spaceIndex);

    if (maybeSpace != Bytes.SP) {
      return toResult(Status.BAD_REQUEST);
    }

    bufferIndex = spaceIndex + 1;

    method = maybeMethod;

    return _REQUEST_TARGET;
  }

  private byte executeRequestTarget() {
    int index;
    index = bufferIndex;

    boolean found;
    found = false;

    for (; bufferHasIndex(index); index++) {
      byte b;
      b = bufferGet(index);

      if (b == Bytes.SP) {
        found = true;

        break;
      }
    }

    if (!found) {
      return toIoRead(state);
    }

    requestTarget = new RequestTarget(bufferIndex, index);

    // after SP
    bufferIndex = index + 1;

    return _REQUEST_VERSION;
  }

  private byte executeRequestVersion() {
    int index;
    index = bufferIndex;

    int versionLength;
    versionLength = Version.V1_1.bytes.length;

    int versionEnd;
    versionEnd = index + versionLength;

    int lineEnd;
    lineEnd = versionEnd + 1;

    if (!bufferHasIndex(lineEnd)) {
      return toIoRead(state);
    }

    int minorIndex;
    minorIndex = versionEnd - 1;

    byte minor;
    minor = bufferGet(minorIndex);

    Version maybe;
    maybe = switch (minor) {
      case '0' -> Version.V1_0;

      case '1' -> Version.V1_1;

      default -> null;
    };

    if (maybe == null || !bufferEquals(maybe.bytes, index)) {
      return toResult(Status.HTTP_VERSION_NOT_SUPPORTED);
    }

    if (bufferGet(versionEnd) != Bytes.CR || bufferGet(lineEnd) != Bytes.LF) {
      return toResult(Status.BAD_REQUEST);
    }

    bufferIndex = lineEnd + 1;

    version = maybe;

    // we create the map to store the eventual request headers

    requestHeaders = new EnumMap<>(HeaderName.class);

    return _REQUEST_HEADER;
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

  private byte executeStart() {
    // TODO set timeout

    // we ensure the buffer is reset

    bufferIndex = bufferLimit = 0;

    return toIoRead(_PARSE_METHOD);
  }

  private byte toClose(IOException e) {
    error = e;

    return _CLOSE;
  }

  private byte toIoRead(byte onRead) {
    nextAction = onRead;

    return _IO_READ;
  }

  private byte toResponseHeaderBuffer() {
    // we reset our buffer

    bufferIndex = bufferLimit = 0;

    return _RESPONSE_HEADER_BUFFER;
  }

  private byte toResult(Status status) {
    if (result != null) {
      throw new IllegalStateException("Result was already set");
    }

    result = new StatusResult(status);

    return _CLOSE;
  }

}