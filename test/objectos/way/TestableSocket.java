/*
 * Copyright (C) 2016-2025 Objectos Software LTDA.
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
package objectos.way;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TestableSocket extends Socket {

  private static class ThrowsOnGetOutput extends TestableSocket {
    @Override
    public final OutputStream getOutputStream() throws IOException {
      throw thrown = new IOException();
    }
  }

  private static class ThrowsOnWrite extends TestableSocket {
    @Override
    public final OutputStream getOutputStream() throws IOException {
      return new OutputStream() {
        @Override
        public void write(int b) throws IOException {
          throw thrown = new IOException();
        }
      };
    }
  }

  private ByteArrayOutputStream outputStream;

  private final InputStream inputStream;

  IOException thrown;

  public TestableSocket(InputStream inputStream) {
    this.inputStream = inputStream;
  }

  TestableSocket() {
    inputStream = null;
  }

  public static TestableSocket of(Object... data) {
    return new TestableSocket(
        Y.inputStream(data)
    );
  }

  public static TestableSocket throwsOnGetOutput() {
    return new ThrowsOnGetOutput();
  }

  public static TestableSocket throwsOnWrite() {
    return new ThrowsOnWrite();
  }

  @Override
  public InputStream getInputStream() throws IOException {
    return inputStream;
  }

  @Override
  public OutputStream getOutputStream() throws IOException {
    if (outputStream == null) {
      outputStream = new ByteArrayOutputStream();
    }

    return outputStream;
  }

  public final String outputAsString() {
    return outputStream.toString(StandardCharsets.UTF_8);
  }

  public final void outputReset() {
    if (outputStream != null) {
      outputStream.reset();
    }
  }

}