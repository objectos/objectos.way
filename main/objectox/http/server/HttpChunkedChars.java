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
package objectox.http.server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import objectos.http.server.CharWritable;

final class HttpChunkedChars implements Appendable {

  private final Socket socket;

  private final byte[] buffer;

  private int bufferLimit;

  private final CharWritable entity;

  private final Charset charset;

  public HttpChunkedChars(Socket socket, byte[] buffer, CharWritable entity, Charset charset) {
    this.socket = socket;

    this.buffer = buffer;

    this.entity = entity;

    this.charset = charset;
  }

  @Override
  public final Appendable append(char c) throws IOException {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public final Appendable append(CharSequence csq) throws IOException {
    String s;
    s = csq.toString();

    byte[] bytes;
    bytes = s.getBytes(charset);

    writeBytes(bytes);

    return this;
  }

  private void writeBytes(byte[] bytes) throws IOException {
    int bytesIndex;
    bytesIndex = 0;

    while (bytesIndex < bytes.length) {
      int bufferRemaining;
      bufferRemaining = buffer.length - bufferLimit;

      if (bufferRemaining > 0) {
        int bytesRemaining;
        bytesRemaining = bytes.length - bytesIndex;

        int length;
        length = Math.min(bytesRemaining, bufferRemaining);

        System.arraycopy(bytes, bytesIndex, buffer, bufferLimit, length);

        bytesIndex += length;

        bufferLimit += length;

        continue;
      }

      if (bufferRemaining == 0) {
        flush();

        continue;
      }

      throw new UnsupportedOperationException("Implement me");
    }
  }

  @Override
  public final Appendable append(CharSequence csq, int start, int end) throws IOException {
    throw new UnsupportedOperationException("Implement me");
  }

  public final void write() throws IOException {
    entity.writeTo(this);

    int bufferRemaining;
    bufferRemaining = buffer.length - bufferLimit;

    if (bufferRemaining > 0) {
      flush();
    }

    flush();
  }

  private void flush() throws IOException {
    OutputStream outputStream;
    outputStream = socket.getOutputStream();

    int chunkLength;
    chunkLength = bufferLimit;

    String lengthDigits;
    lengthDigits = Integer.toHexString(chunkLength);

    byte[] lengthBytes;
    lengthBytes = (lengthDigits + "\r\n").getBytes(StandardCharsets.UTF_8);

    outputStream.write(lengthBytes, 0, lengthBytes.length);

    int bufferRemaining;
    bufferRemaining = buffer.length - bufferLimit;

    if (bufferRemaining >= 2) {
      buffer[bufferLimit++] = Bytes.CR;
      buffer[bufferLimit++] = Bytes.LF;

      outputStream.write(buffer, 0, bufferLimit);
    } else {
      outputStream.write(buffer, 0, bufferLimit);

      outputStream.write(Bytes.CRLF);
    }

    bufferLimit = 0;
  }

}