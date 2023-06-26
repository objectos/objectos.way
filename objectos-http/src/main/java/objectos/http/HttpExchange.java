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
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Arrays;
import java.util.Date;
import objectos.http.HttpExchange.Result.StatusResult;
import objectos.lang.NoteSink;

final class HttpExchange implements HttpResponseHandle, Runnable {

  sealed interface Result {
    record StatusResult(Status status) implements Result {}
  }

  static final byte _STOP = 0,

      _BAD_REQUEST = -1,

      _CLOSE = -2,

      _FINALLY = -3,

      _REQUEST_METHOD = -4,

      _REQUEST_TARGET = -5,

      _REQUEST_VERSION = -6,

      _SOCKET_READ = -7,

      _START = -8;

  private final byte[] buffer;

  int bufferIndex;

  int bufferLimit;

  @SuppressWarnings("unused")
  private Throwable error;

  Method method;

  @SuppressWarnings("unused")
  private final NoteSink noteSink;

  private final HttpProcessor processor;

  private Result result;

  private final Socket socket;

  byte socketReadAction;

  private byte socketReadEofAction;

  byte state;

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
  public final String formatDate(Date date) {
    throw new UnsupportedOperationException();
  }

  @Override
  public final String formatDate(long millis) {
    throw new UnsupportedOperationException();
  }

  @Override
  public final ByteBuffer getByteBuffer() {
    throw new UnsupportedOperationException();
  }

  @Override
  public final WritableByteChannel getChannel() {
    throw new UnsupportedOperationException();
  }

  @Override
  public final CharBuffer getCharBuffer() {
    throw new UnsupportedOperationException();
  }

  @Override
  public final CharsetEncoder getEncoder(Charset charset) {
    throw new UnsupportedOperationException();
  }

  @Override
  public final StringBuilder getStringBuilder() {
    throw new UnsupportedOperationException();
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
      case _REQUEST_METHOD -> executeRequestMethod();

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

  private byte bufferRead(int index) {
    return buffer[index];
  }

  private boolean bufferReadable(int index) {
    return index < bufferLimit;
  }

  private byte executeRequestMethod() {
    int start = bufferIndex;

    if (!bufferReadable(start)) {
      return toSocketRead(state, _CLOSE);
    }

    byte first;
    first = bufferRead(start);

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

    if (!bufferReadable(spaceIndex)) {
      return toSocketRead(state, _CLOSE);
    }

    byte maybeSpace;
    maybeSpace = bufferRead(spaceIndex);

    if (maybeSpace != Http.SP_BYTE) {
      return toResult(Status.BAD_REQUEST);
    }

    bufferIndex = spaceIndex + 1;

    method = maybeMethod;

    return _REQUEST_TARGET;
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
    processor.requestStart(this);

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