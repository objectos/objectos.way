/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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
package objectox.http.req;

import module java.base;
import objectox.http.HttpClientException;

public final class RequestInputStream extends InputStream {

  @SuppressWarnings("serial")
  static final class Eof extends IOException {}

  @SuppressWarnings("serial")
  static final class Overflow extends IOException {}

  private final byte[] buffer;

  private int bufferIndex;

  private int bufferLimit;

  private final InputStream inputStream;

  public RequestInputStream(byte[] buffer, InputStream inputStream) {
    this.buffer = buffer;

    this.inputStream = inputStream;
  }

  static RequestInputStream of(int bufferSize, Socket socket) throws IOException {
    return new RequestInputStream(
        new byte[bufferSize],

        socket.getInputStream()
    );
  }

  public final int bufferIndex() {
    return bufferIndex;
  }

  public final void bufferTo(OutputStream out, int len) throws IOException {
    final int nextIndex;
    nextIndex = bufferIndex + len;

    if (nextIndex > bufferLimit) {
      throw new IllegalArgumentException("length will overflow bufferIndex");
    }

    out.write(buffer, bufferIndex, len);

    bufferIndex = nextIndex;
  }

  public final String makeStr(int startIndex) {
    return makeStr(startIndex, bufferIndex);
  }

  public final String makeStr(int startIndex, int endIndex) {
    final int length;
    length = endIndex - startIndex - 1;

    return new String(buffer, startIndex, length, StandardCharsets.US_ASCII);
  }

  public final StringBuilder makeStrBuilder(int startIndex) {
    final String prefix;
    prefix = makeStr(startIndex);

    return new StringBuilder(prefix);
  }

  public final byte peekByte() throws IOException {
    if (ensureBuffer() != -1) {
      return buffer[bufferIndex];
    } else {
      throw new Eof();
    }
  }

  public final byte peekPrev() {
    return buffer[bufferIndex - 1];
  }

  @Override
  public final int read() throws IOException {
    if (ensureBuffer() != -1) {
      final byte b;
      b = buffer[bufferIndex++];

      return Byte.toUnsignedInt(b);
    } else {
      return -1;
    }
  }

  public final byte readByte() throws IOException {
    if (ensureBuffer() != -1) {
      return buffer[bufferIndex++];
    } else {
      throw new Eof();
    }
  }

  public final byte readByte(HttpClientException.Kind kind) throws IOException {
    final byte b;
    b = readByte();

    if (b < 0) {
      final String msg;
      msg = "Unexpected byte 0x%02X while reading from input: ASCII value expected".formatted(b);

      throw new HttpClientException(msg, kind);
    }

    return b;
  }

  public final int readForBody() throws IOException {
    final int buffered;
    buffered = bufferLimit - bufferIndex;

    if (buffered > 0) {
      return buffered;
    }

    if (buffered == 0) {
      final int read;
      read = inputStream.read(buffer, 0, buffer.length);

      if (read < 0) {
        throw new Eof();
      }

      bufferIndex = 0;

      return bufferLimit = read;
    }

    throw new IllegalStateException("buffered bytes < 0");
  }

  public final int readToBuffer() throws IOException {
    final int writableLength;
    writableLength = buffer.length - bufferLimit;

    if (writableLength == 0) {
      throw new Overflow();
    }

    final int bytesRead;
    bytesRead = inputStream.read(buffer, bufferLimit, writableLength);

    assert bytesRead != 0 : "InputStream.read should not return 0 when writableLength != 0";

    if (bytesRead > 0) {
      bufferLimit += bytesRead;
    }

    return bytesRead;
  }

  public final void skipByte() {
    bufferIndex += 1;
  }

  public final boolean start() throws IOException {
    bufferIndex = bufferLimit = 0;

    final int bytesRead;
    bytesRead = readToBuffer();

    return bytesRead != -1;
  }

  private int ensureBuffer() throws IOException {
    final int readable;
    readable = bufferLimit - bufferIndex;

    if (readable > 0) {
      return 0;
    }

    if (readable == 0) {
      return readToBuffer();
    }

    throw new IllegalStateException("readable bytes < 0");
  }

}
