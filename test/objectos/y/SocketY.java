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
package objectos.y;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.function.Consumer;

public final class SocketY extends Socket {

  public static final class Options {

    public InputStream inputStream = InputStream.nullInputStream();

    public OutputStream outputStream = OutputStreamY.create();

  }

  private final InputStream inputStream;

  private final OutputStream outputStream;

  private SocketY(InputStream inputStream, OutputStream outputStream) {
    this.inputStream = inputStream;

    this.outputStream = outputStream;
  }

  public static Socket create(Consumer<? super Options> opts) {
    final Options options;
    options = new Options();

    opts.accept(options);

    return new SocketY(
        options.inputStream,

        options.outputStream
    );
  }

  public static Socket of(Object... data) {
    return create(s -> {
      s.inputStream = InputStreamY.of(data);
    });
  }

  @Override
  public final InputStream getInputStream() throws IOException {
    return inputStream;
  }

  @Override
  public final OutputStream getOutputStream() throws IOException {
    return outputStream;
  }

  @Override
  public final String toString() {
    return outputStream.toString();
  }

}
