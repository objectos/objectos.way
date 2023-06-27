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
import java.util.Map;
import objectos.http.HttpProcessor;
import objectos.http.Status;
import objectos.http.internal.HttpExchange.Result.StatusResult;
import objectos.lang.NoteSink;

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

  static final byte _STOP = 0,

      _BAD_REQUEST = 1,

      _CLOSE = 2,

      _FINALLY = 3,

      _PROCESS = 4,

      _REQUEST_HEADER = 5,

      _REQUEST_HEADER_NAME = 6,

      _REQUEST_HEADER_VALUE = 7,

      _REQUEST_METHOD = 8,

      _REQUEST_TARGET = 9,

      _REQUEST_VERSION = 10,

      _SOCKET_READ = 11,

      _START = 12;

  private final byte[] buffer;

  int bufferIndex;

  int bufferLimit;

  @SuppressWarnings("unused")
  private Throwable error;

  HeaderName headerName;

  final Map<HeaderName, HeaderValue> headers;

  Method method;

  @SuppressWarnings("unused")
  private final NoteSink noteSink;

  @SuppressWarnings("unused")
  private final HttpProcessor processor;

  RequestTarget requestTarget;

  private Result result;

  private final Socket socket;

  byte socketReadAction;

  private byte socketReadEofAction;

  byte state;

  Version version;

  public HttpExchange(int bufferSize,
                      NoteSink noteSink,
                      HttpProcessor processor,
                      Socket socket) {
    this.buffer = new byte[bufferSize];

    headers = new EnumMap<>(HeaderName.class);

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
      case _PROCESS -> executeProcess();

      case _REQUEST_HEADER -> executeRequestHeader();

      case _REQUEST_HEADER_NAME -> executeRequestHeaderName();

      case _REQUEST_HEADER_VALUE -> executeRequestHeaderValue();

      case _REQUEST_METHOD -> executeRequestMethod();

      case _REQUEST_TARGET -> executeRequestTarget();

      case _REQUEST_VERSION -> executeRequestVersion();

      case _SOCKET_READ -> executeSocketRead();

      case _START -> executeStart();

      default -> throw new UnsupportedOperationException(
        "Implement me :: state=" + state
      );
    };
  }

  private byte executeProcess() {
    throw new UnsupportedOperationException("Implement me");
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

  private byte executeRequestHeader() {
    int index;
    index = bufferIndex;

    if (!bufferHasIndex(index)) {
      return toSocketRead(state, _CLOSE);
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
      return toSocketRead(state, _CLOSE);
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

      return toSocketRead(state, _CLOSE);
    }

    byte first;
    first = bufferGet(nameStart);

    HeaderName maybe;
    maybe = switch (first) {
      case 'H' -> HeaderName.HOST;

      default -> null;
    };

    if (maybe != null && bufferEquals(maybe.bytes, nameStart)) {
      headerName = maybe;

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

      return toSocketRead(state, _CLOSE);
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
    previousValue = headers.put(headerName, headerValue);

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
      return toSocketRead(state, _CLOSE);
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
      return toSocketRead(state, _CLOSE);
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
      return toSocketRead(state, _CLOSE);
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
      return toSocketRead(state, _CLOSE);
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

    return _REQUEST_HEADER;
  }

  private byte executeSocketRead() {
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
      return socketReadEofAction;
    }

    bufferLimit += bytesRead;

    return socketReadAction;
  }

  private byte executeStart() {
    // processor.requestStart(this);

    // TODO set timeout

    return toSocketRead(_REQUEST_METHOD, _CLOSE);
  }

  private byte toClose(IOException e) {
    error = e;

    return _CLOSE;
  }

  private byte toResult(Status status) {
    if (result != null) {
      throw new IllegalStateException("Result was already set");
    }

    result = new StatusResult(status);

    return _CLOSE;
  }

  private byte toSocketRead(byte onRead, byte onEof) {
    socketReadAction = onRead;

    socketReadEofAction = onEof;

    return _SOCKET_READ;
  }

}