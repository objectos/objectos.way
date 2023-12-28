/*
 * Copyright (C) 2023 Objectos Software LTDA.
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
package objectox.http.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

final class Input {

  private final byte[] buffer;

  private final InputStream inputStream;

  private int index;

  private int limit;

  Input(int bufferSize, InputStream inputStream) {
    buffer = new byte[bufferSize];

    this.inputStream = inputStream;
  }

  public final int index() {
    return index;
  }

  public final boolean hasNext() throws IOException {
    ensureBuffer(index);

    return index < limit;
  }

  public final byte next() {
    return buffer[index++];
  }

  public final byte peek() {
    return buffer[index];
  }

  public final boolean test(byte[] bytes) throws IOException {
    int requiredIndex;
    requiredIndex = index + bytes.length;

    ensureBuffer(requiredIndex);

    return Arrays.equals(
        buffer, index, requiredIndex,
        bytes, 0, bytes.length);
  }

  public final void skip(int count) {
    index += count;
  }

  private void ensureBuffer(int requiredIndex) throws IOException {
    if (requiredIndex >= buffer.length) {
      throw new IllegalArgumentException("""
      Buffer is not large enough to hold the requested index.

      buffer.length=%d
      requiredIndex=%d

      Please increase the internal buffer size
      """.formatted(buffer.length, requiredIndex));
    }

    while (requiredIndex >= limit) {
      int writableLength;
      writableLength = buffer.length - limit;

      int bytesRead;
      bytesRead = inputStream.read(buffer, limit, writableLength);

      if (bytesRead < 0) {
        break;
      }

      limit += bytesRead;
    }
  }

  final HttpRequestPath createPath() {
    return new HttpRequestPath(buffer, index);
  }

}