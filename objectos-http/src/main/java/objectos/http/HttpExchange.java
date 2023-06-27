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
package objectos.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import objectos.http.HttpExchange.Result.StatusResult;
import objectos.lang.NoteSink;

final class HttpExchange implements Runnable {

  sealed interface Result {
    record StatusResult(Status status) implements Result {}
  }

  private static class ThisRequestTarget implements RequestTarget {

    private final byte[] bytes;

    public ThisRequestTarget(byte[] bytes) {
      this.bytes = bytes;
    }

    @Override
    public final boolean pathEquals(String string) {
      return new String(bytes).equals(string);
    }

  }

  static final byte _STOP = 0,

      _BAD_REQUEST = 1,

      _CLOSE = 2,

      _FINALLY = 3,

      _REQUEST_HEADER = 4,

      _REQUEST_HEADER_NAME = 5,

      _REQUEST_HEADER_VALUE = 6,

      _REQUEST_METHOD = 7,

      _REQUEST_TARGET = 8,

      _REQUEST_VERSION = 9,

      _SOCKET_READ = 10,

      _START = 11;

  private final byte[] buffer;

  int bufferIndex;

  int bufferLimit;

  @SuppressWarnings("unused")
  private Throwable error;

  HeaderName headerName;

  String headerValue;

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

  // public stuff

  HttpExchange(int bufferSize,
               NoteSink noteSink,
               HttpProcessor processor,
               Socket socket) {
    this.buffer = new byte[bufferSize];

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

  // non-public stuff

  final boolean isActive() {
    return state != _STOP;
  }

  final void stepOne() {
    state = switch (state) {
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
    final int firstIndex;
    firstIndex = bufferIndex;

    if (!bufferHasIndex(firstIndex)) {
      return toSocketRead(state, _CLOSE);
    }

    final byte first;
    first = bufferGet(firstIndex);

    if (first != Http.CR_BYTE) {
      return _REQUEST_HEADER_NAME;
    }

    throw new UnsupportedOperationException("Implement me");
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

      if (b == Http.COLON_BYTE) {
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

    ThisHeader maybe;
    maybe = switch (first) {
      case 'H' -> ThisHeader.HOST;

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

      if (b == Http.LF_BYTE) {
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

    if (bufferGet(carriageIndex) != Http.CR_BYTE) {
      return toResult(Status.BAD_REQUEST);
    }

    // we trim the OWS, if found, from the start of the value

    final int valueStart;

    byte maybeOws;
    maybeOws = bufferGet(maybeStart);

    if (Http.isOptionalWhitespace(maybeOws)) {
      valueStart = maybeStart + 1;
    } else {
      valueStart = maybeStart;
    }

    // the value string ends immediately before the CR char

    final int valueEnd;
    valueEnd = carriageIndex;

    headerValue = makeString(valueStart, valueEnd);

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

    if (maybeSpace != Http.SP_BYTE) {
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

      if (b == Http.SP_BYTE) {
        found = true;

        break;
      }
    }

    if (!found) {
      return toSocketRead(state, _CLOSE);
    }

    byte[] bytes;
    bytes = Arrays.copyOfRange(buffer, bufferIndex, index);

    // after SP
    bufferIndex = index + 1;

    requestTarget = new ThisRequestTarget(bytes);

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

    if (bufferGet(versionEnd) != Http.CR_BYTE || bufferGet(lineEnd) != Http.LF_BYTE) {
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

  private String makeString(int start, int end) {
    return new String(buffer, start, end - start, StandardCharsets.ISO_8859_1);
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