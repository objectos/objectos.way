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

  private ByteArrayOutputStream outputStream;

  private final InputStream inputStream;

  private TestableSocket(byte[] requestBytes) {
    this.inputStream = new InputStream() {
      private final byte[] bytes = requestBytes;

      private int index;

      @Override
      public int read() throws IOException {
        if (index < bytes.length) {
          return bytes[index++];
        } else {
          return -1;
        }
      }
    };
  }

  public TestableSocket(InputStream inputStream) {
    this.inputStream = inputStream;
  }

  public static TestableSocket empty() {
    return new TestableSocket(
      TestableInputStream.EMPTY
    );
  }

  public static TestableSocket of(Object... data) {
    return new TestableSocket(
      TestableInputStream.of(data)
    );
  }

  public static TestableSocket parse(String string) {
    byte[] bytes;
    bytes = string.getBytes(StandardCharsets.UTF_8);

    return new TestableSocket(bytes);
  }

  @Override
  public InputStream getInputStream() throws IOException {
    return inputStream;
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