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
package objectos.http;

import module java.base;

final class HttpRequestParser0Input {

  @SuppressWarnings("serial")
  static final class Eof extends IOException {}

  @SuppressWarnings("serial")
  static final class Overflow extends IOException {}

  private static final int HARD_MAX_BUFFER_SIZE = 1 << 15; // 32k

  private byte[] buffer;

  private int bufferIndex;

  private int bufferLimit;

  private final int bufferSizeMax;

  private final InputStream inputStream;

  private int mark;

  private HttpRequestParser0Input(
      byte[] buffer,
      int bufferSizeMax,
      InputStream inputStream) {
    this.buffer = buffer;

    this.bufferSizeMax = bufferSizeMax;

    this.inputStream = inputStream;
  }

  static HttpRequestParser0Input of(
      int bufferSizeInitial,
      int bufferSizeMax,
      Socket socket) throws IOException {
    final int initialSize;
    initialSize = powerOfTwo(bufferSizeInitial);

    return new HttpRequestParser0Input(
        new byte[initialSize],

        bufferSizeMax,

        socket.getInputStream()
    );
  }

  static final int powerOfTwo(int size) {
    // maybe size is already power of 2
    final int x;
    x = size - 1;

    final int leading;
    leading = Integer.numberOfLeadingZeros(x);

    final int n;
    n = -1 >>> leading;

    if (n < 0) {
      // should not happen as minimal buffer size is 128
      throw new IllegalArgumentException("Buffer size is too small");
    }

    if (n >= HARD_MAX_BUFFER_SIZE) {
      return HARD_MAX_BUFFER_SIZE;
    }

    return n + 1;
  }

  public final int bufferIndex() {
    return bufferIndex;
  }

  public final boolean canBuffer(long contentLength) {
    int maxAvailable;
    maxAvailable = bufferSizeMax - bufferIndex;

    return maxAvailable >= contentLength;
  }

  public final String makeStr() {
    return makeStr(bufferIndex);
  }

  private String makeStr(int endIndex) {
    endIndex = endIndex - 1;

    return bufferToAscii(mark, endIndex);
  }

  public final StringBuilder makeStrBuilder() {
    final String prefix;
    prefix = makeStr();

    return new StringBuilder(prefix);
  }

  public final void mark() {
    mark = bufferIndex;
  }

  public final boolean matches(byte[] value, int offset) throws IOException {
    final byte[] b;
    b = value;

    final int bFromIndex;
    bFromIndex = offset;

    final int bToIndex;
    bToIndex = value.length;

    final int length;
    length = bToIndex - bFromIndex;

    ensureBuffer(length);

    final byte[] a;
    a = buffer;

    final int aFromIndex;
    aFromIndex = bufferIndex;

    final int aToIndex;
    aToIndex = bufferIndex + length;

    final boolean matches;
    matches = Arrays.equals(
        a, aFromIndex, aToIndex,
        b, bFromIndex, bToIndex
    );

    if (matches) {
      bufferIndex += length;

      return true;
    } else {
      return false;
    }
  }

  public final byte peekByte() throws IOException {
    ensureBuffer(1);

    return buffer[bufferIndex];
  }

  public final byte peekPrev() {
    return buffer[bufferIndex - 1];
  }

  public final byte readByte() throws IOException {
    ensureBuffer(1);

    return buffer[bufferIndex++];
  }

  public final InputStream readStream(int length) throws IOException {
    ensureBuffer(length);

    return new ByteArrayInputStream(buffer, bufferIndex, length);
  }

  public final byte readTable(byte[] table, HttpClientException.Kind kind) throws IOException {
    final byte next;
    next = readByte();

    if (next < 0) {
      throw HttpClientException.of(kind);
    }

    return table[next];
  }

  public final void skipByte() {
    bufferIndex += 1;
  }

  public final void transferTo(OutputStream outputStream, long length) throws IOException {
    long remaining;
    remaining = length;

    // part of the body might be in the buffer
    final int buffered;
    buffered = bufferLimit - bufferIndex;

    if (buffered > 0) {
      outputStream.write(buffer, bufferIndex, buffered);

      remaining -= buffered;
    }

    // work buffer
    final byte[] work;
    work = new byte[bufferSizeMax];

    // transfer from inputStream
    while (remaining > 0) {
      // this is guaranteed to be an int value
      final long iteration;
      iteration = Math.min(remaining, work.length);

      final int read;
      read = inputStream.read(work, 0, (int) iteration);

      if (read < 0) {
        throw new Eof();
      }

      outputStream.write(work, 0, read);

      remaining -= read;
    }
  }

  final String bufferToAscii() {
    return new String(buffer, StandardCharsets.US_ASCII);
  }

  final String bufferToAscii(int startIndex, int endIndex) {
    final int length;
    length = endIndex - startIndex;

    return new String(buffer, startIndex, length, StandardCharsets.US_ASCII);
  }

  private void ensureBuffer(int count) throws IOException {
    int readable;
    readable = bufferLimit - bufferIndex;

    while (count > readable) {
      int writableLength;
      writableLength = buffer.length - bufferLimit;

      if (writableLength == 0) {
        // buffer is full, try to increase

        if (buffer.length == bufferSizeMax) {
          throw new Overflow();
        }

        final int newLength;
        newLength = buffer.length << 1;

        buffer = Arrays.copyOf(buffer, newLength);

        writableLength = buffer.length - bufferLimit;
      }

      final int bytesRead;
      bytesRead = inputStream.read(buffer, bufferLimit, writableLength);

      if (bytesRead < 0) {
        throw new Eof();
      }

      assert bytesRead != 0 : "InputStream.read should not return 0 when writableLength != 0";

      bufferLimit += bytesRead;

      readable = bufferLimit - bufferIndex;
    }
  }

}
