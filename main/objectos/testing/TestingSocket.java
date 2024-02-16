/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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
package objectos.testing;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

class TestingSocket extends Socket {

  private final InputStream inputStream;

  private ByteArrayOutputStream outputStream;

  public TestingSocket(InputStream inputStream) {
    this.inputStream = inputStream;
  }

  @Override
  public final InputStream getInputStream() throws IOException {
    return inputStream;
  }

  @Override
  public final OutputStream getOutputStream() throws IOException {
    if (outputStream == null) {
      outputStream = new ByteArrayOutputStream();
    }

    return outputStream;
  }

  @Override
  public final String toString() {
    byte[] bytes;
    bytes = outputStream.toByteArray();

    return new String(bytes, StandardCharsets.UTF_8);
  }

}