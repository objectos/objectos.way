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

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Objects;

final class ByteSource implements Closeable {

  private final InputStream inputStream;

  private final byte[] buffer;

  private int bufferIndex;

  private int bufferLimit;

  private boolean eof;

  private ByteSource(InputStream inputStream, byte[] buffer) {
    this.inputStream = inputStream;
    this.buffer = buffer;
  }

  public static ByteSource ofInputStream(int bufferSize, InputStream inputStream) {
    Objects.requireNonNull(inputStream, "inputStream == null");

    byte[] buffer;
    buffer = new byte[bufferSize];

    return new ByteSource(inputStream, buffer);
  }

  public static ByteSource ofSocket(Socket socket, int bufferSize) throws IOException {
    InputStream inputStream;
    inputStream = socket.getInputStream();

    byte[] buffer;
    buffer = new byte[bufferSize];

    return new ByteSource(inputStream, buffer);
  }

  @Override
  public final void close() throws IOException {
    try {
      inputStream.close();
    } finally {
      bufferIndex = bufferLimit = -1;

      eof = true;
    }
  }

  public final byte get() {
    return buffer[bufferIndex++];
  }

  public final boolean hasMore(int required) throws IOException {
    int bufferReadable = bufferLimit - bufferIndex;

    if (bufferReadable >= required) {
      return true;
    }

    int bytesToWrite = required - bufferReadable;

    int bufferWritable = buffer.length - bufferLimit;

    if (bufferWritable < bytesToWrite) {
      compact();
    }

    readIntoWritable(bytesToWrite);

    return bufferLimit - bufferIndex >= required;
  }

  public final boolean isClosed() {
    return bufferIndex == -1 && bufferLimit == -1 && eof;
  }

  public final boolean matches(byte[] expected) {
    return Arrays.equals(
      buffer, bufferIndex, bufferIndex = bufferIndex + expected.length,
      expected, 0, expected.length
    );
  }

  public final boolean readUntil(byte value, OutputStream to) throws IOException {
    while (hasMore(1)) {
      byte b;
      b = get();

      if (b == value) {
        return true;
      }

      to.write(b);
    }

    return false;
  }

  private void compact() {
    if (bufferIndex == bufferLimit) {
      bufferIndex = bufferLimit = 0;

      return;
    }

    int length = bufferLimit - bufferIndex;

    System.arraycopy(buffer, bufferIndex, buffer, 0, length);

    bufferIndex = 0;

    bufferLimit = length;
  }

  private void readIntoWritable(int bytesToWrite) throws IOException {
    int totalWritten = 0;

    while (!eof && totalWritten < bytesToWrite) {
      int bufferWritable = buffer.length - bufferLimit;

      int read = inputStream.read(buffer, bufferLimit, bufferWritable);

      if (read < 0) {
        eof = true;

        break;
      }

      totalWritten += read;

      bufferLimit += read;
    }
  }

}