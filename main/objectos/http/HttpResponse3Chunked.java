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

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import objectos.internal.Bytes;

final class HttpResponse3Chunked extends OutputStream {

  private static final byte[] CHUNKED_TRAILER = "0\r\n\r\n".getBytes(StandardCharsets.UTF_8);

  private final byte[] buffer;

  private int bufferIndex;

  private int chunkIndex;

  private boolean closed;

  private int dataIndex;

  private final OutputStream outputStream;

  HttpResponse3Chunked(byte[] buffer, int bufferIndex, OutputStream outputStream) throws IOException {
    this.buffer = buffer;

    this.bufferIndex = bufferIndex;

    this.outputStream = outputStream;

    writeChunkBegin();
  }

  @Override
  public final void close() throws IOException {
    if (!closed) {
      writeChunkEnd();

      final int available;
      available = buffer.length - bufferIndex;

      final int trailerLength;
      trailerLength = CHUNKED_TRAILER.length;

      if (available < trailerLength) {
        // trailer won't fit in buffer
        // => flush

        writeChunkFlush();
      }

      System.arraycopy(CHUNKED_TRAILER, 0, buffer, bufferIndex, trailerLength);

      bufferIndex += trailerLength;

      writeChunkFlush();

      closed = true;
    }
  }

  @Override
  public final void flush() {
    // noop
  }

  @Override
  public final void write(int b) throws IOException {
    int available;
    available = writeChunkAvailable();

    if (available <= 0) {
      writeChunkEnd();

      writeChunkFlush();

      writeChunkBegin();
    }

    buffer[bufferIndex++] = (byte) b;
  }

  @Override
  public final void write(byte[] bytes) throws IOException {
    write(bytes, 0, bytes.length);
  }

  @Override
  public final void write(byte[] bytes, int offset, int length) throws IOException {
    int bytesIndex;
    bytesIndex = offset;

    int remaining;
    remaining = length;

    while (remaining > 0) {
      int available;
      available = writeChunkAvailable();

      if (available <= 0) {
        writeChunkEnd();

        writeChunkFlush();

        writeChunkBegin();

        available = writeChunkAvailable();
      }

      final int bytesToCopy;
      bytesToCopy = Math.min(remaining, available);

      System.arraycopy(bytes, bytesIndex, buffer, bufferIndex, bytesToCopy);

      bufferIndex += bytesToCopy;

      bytesIndex += bytesToCopy;

      remaining -= bytesToCopy;
    }
  }

  private int writeChunkAvailable() {
    //return buffer.length - (bufferIndex + CHUNKED_TRAILER.length + 2);

    // it should be large enough to hold the CRLF
    return buffer.length - (bufferIndex + 2);
  }

  private void writeChunkBegin() throws IOException {
    // chunkSizeLength = bytes required to store the chunk-size + CRLF
    int chunkSizeLength;
    chunkSizeLength = 0;

    // save space for (max) chunk-size
    chunkSizeLength += writeChunkMaxHexDigits();

    // save space for CRLF
    chunkSizeLength += 2;

    final int available;
    available = buffer.length - bufferIndex;

    if (available <= chunkSizeLength) {
      // remaining buffer is not enough for the chunk size
      // => flush

      writeChunkFlush();
    }

    // where the chunk begins
    // -> the index of the first digit of the chunk size
    chunkIndex = bufferIndex;

    // where the chunk data begins
    dataIndex = chunkIndex + chunkSizeLength;

    bufferIndex = dataIndex;
  }

  private int writeChunkMaxHexDigits() {
    // buffer will not increase its size during writes
    int maxDataLength;
    maxDataLength = buffer.length;

    // buffer must hold the last zero chunk
    // maxDataLength -= CHUNKED_TRAILER.length;

    // must hold the CRLF after data
    maxDataLength -= 2;

    // must hold the CRLF after chunk-size
    maxDataLength -= 2;

    // must hold at least 1 digit of the chunk-size
    maxDataLength -= 1;

    return Http.requiredHexDigits(maxDataLength);
  }

  private void writeChunkEnd() {
    // writes out the chunk size
    int sizeIndex;
    sizeIndex = dataIndex - 1;

    buffer[sizeIndex--] = Bytes.LF;
    buffer[sizeIndex--] = Bytes.CR;

    int chunkLength;
    chunkLength = bufferIndex - dataIndex;

    final int hexDigits;
    hexDigits = Http.requiredHexDigits(chunkLength);

    for (int i = 0; i < hexDigits; i++) {
      final int nibble;
      nibble = chunkLength & 0xF;

      final byte digit;
      digit = Http.hexDigit(nibble);

      buffer[sizeIndex--] = digit;

      chunkLength >>= 4;
    }

    // left pad the size with '0' digits

    while (sizeIndex >= chunkIndex) {
      buffer[sizeIndex--] = '0';
    }

    // buffer the data CRLF

    buffer[bufferIndex++] = Bytes.CR;
    buffer[bufferIndex++] = Bytes.LF;
  }

  private void writeChunkFlush() throws IOException {
    outputStream.write(buffer, 0, bufferIndex);

    chunkIndex = dataIndex = bufferIndex = 0;
  }

}
