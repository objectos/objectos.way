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

  private final byte[] buffer;

  private int bufferIndex;

  private int bufferLimit;

  private final InputStream inputStream;

  private int mark;

  private HttpRequestParser0Input(
      byte[] buffer,
      InputStream inputStream) {
    this.buffer = buffer;

    this.inputStream = inputStream;
  }

  static HttpRequestParser0Input of(int bufferSize, Socket socket) throws IOException {
    if (bufferSize < 128) {
      throw new IllegalArgumentException("Buffer size is too small");
    }

    return new HttpRequestParser0Input(
        new byte[bufferSize],

        socket.getInputStream()
    );
  }

  public final int bufferIndex() {
    return bufferIndex;
  }

  public final void ensureBuffer() throws IOException {
    final int readable;
    readable = bufferLimit - bufferIndex;

    if (readable > 0) {
      return;
    }

    if (readable == 0) {
      int writableLength;
      writableLength = buffer.length - bufferLimit;

      if (writableLength == 0) {
        throw new Overflow();
      }

      final int bytesRead;
      bytesRead = inputStream.read(buffer, bufferLimit, writableLength);

      if (bytesRead < 0) {
        throw new Eof();
      }

      assert bytesRead != 0 : "InputStream.read should not return 0 when writableLength != 0";

      bufferLimit += bytesRead;

      return;
    }

    throw new IllegalStateException("readable bytes < 0");
  }

  public final String makeStr() {
    return makeStr(bufferIndex);
  }

  public final String makeStr(int endIndex) {
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

  public final byte peekByte() throws IOException {
    ensureBuffer();

    return buffer[bufferIndex];
  }

  public final byte peekPrev() {
    return buffer[bufferIndex - 1];
  }

  public final byte readByte() throws IOException {
    ensureBuffer();

    return buffer[bufferIndex++];
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

  final String bufferToAscii() {
    return new String(buffer, StandardCharsets.US_ASCII);
  }

  final String bufferToAscii(int startIndex, int endIndex) {
    final int length;
    length = endIndex - startIndex;

    return new String(buffer, startIndex, length, StandardCharsets.US_ASCII);
  }

}
