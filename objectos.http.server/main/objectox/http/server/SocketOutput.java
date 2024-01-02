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
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

final class SocketOutput {

  private final byte[] buffer;

  private final OutputStream outputStream;

  private int bufferIndex;

  public SocketOutput(byte[] buffer, OutputStream outputStream) {
    this.buffer = buffer;

    this.outputStream = outputStream;
  }

  public final void writeBytes(byte[] bytes) throws IOException {
    if (bytes.length > buffer.length) {
      flush();

      outputStream.write(bytes, 0, bytes.length);

      return;
    }

    int remaining;
    remaining = remaining();

    if (bytes.length > remaining) {
      flush();
    }

    append(bytes);
  }

  public final void writeUtf8(String s) throws IOException {
    byte[] bytes;
    bytes = s.getBytes(StandardCharsets.UTF_8);

    writeBytes(bytes);
  }

  public final void flush() throws IOException {
    if (bufferIndex >= 0) {
      outputStream.write(buffer, 0, bufferIndex);

      bufferIndex = 0;
    }
  }

  private void append(byte[] bytes) {
    int length;
    length = bytes.length;

    System.arraycopy(bytes, 0, buffer, bufferIndex, length);

    bufferIndex += length;
  }

  private int remaining() {
    return buffer.length - bufferIndex;
  }

}