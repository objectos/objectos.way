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

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;

final class ResponseBuffered implements Closeable {

  private final byte[] buffer;

  private int bufferIndex;

  private final OutputStream outputStream;

  ResponseBuffered(byte[] buffer, OutputStream outputStream) {
    this.buffer = buffer;

    this.outputStream = outputStream;
  }

  @Override
  public final void close() throws IOException {
    flush();
  }

  public final void write(byte[] bytes) throws IOException {
    write0(bytes, 0, bytes.length);
  }

  private void write0(byte[] bytes, int offset, int length) throws IOException {
    int idx;
    idx = offset;

    int remaining;
    remaining = length;

    while (remaining > 0) {
      int available;
      available = buffer.length - bufferIndex;

      if (available <= 0) {
        flush();

        available = buffer.length - bufferIndex;
      }

      final int bytesToCopy;
      bytesToCopy = Math.min(remaining, available);

      System.arraycopy(bytes, idx, buffer, bufferIndex, bytesToCopy);

      bufferIndex += bytesToCopy;

      idx += bytesToCopy;

      remaining -= bytesToCopy;
    }
  }

  private void flush() throws IOException {
    outputStream.write(buffer, 0, bufferIndex);

    bufferIndex = 0;
  }

}
