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
package objectos.http.internal;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

final class TestableSocket extends Socket {

  IOException getInputStream;

  private final byte[] inputStreamBytes;

  private int inputStreamIndex;

  IOException inputStreamRead;

  private ByteArrayOutputStream outputStream;

  private TestableSocket(byte[] requestBytes) {
    this.inputStreamBytes = requestBytes;
  }

  public static TestableSocket parse(String string) {
    String normalized;
    normalized = string.replaceAll("\n", "\r\n");

    byte[] bytes;
    bytes = normalized.getBytes(StandardCharsets.UTF_8);

    return new TestableSocket(bytes);
  }

  @Override
  public final InputStream getInputStream() throws IOException {
    if (getInputStream != null) {
      throw getInputStream;
    }

    return new InputStream() {
      @Override
      public int read() throws IOException {
        if (inputStreamRead != null) {
          throw inputStreamRead;
        }

        if (inputStreamIndex < inputStreamBytes.length) {
          return inputStreamBytes[inputStreamIndex++];
        } else {
          return -1;
        }
      }
    };
  }

  @Override
  public final OutputStream getOutputStream() throws IOException {
    if (outputStream == null) {
      outputStream = new ByteArrayOutputStream();
    }

    return outputStream;
  }

  public final String outputAsString() {
    byte[] bytes;
    bytes = outputStream.toByteArray();

    return new String(bytes, StandardCharsets.UTF_8);
  }

}