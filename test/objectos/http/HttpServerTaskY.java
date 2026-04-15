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

import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;
import java.util.function.Consumer;
import objectos.way.Note;
import objectos.way.Y;

final class HttpServerTaskY {

  Path bodyDirectory;

  int bodyMemoryMax = 1024;

  long bodySizeMax = 2048;

  int bufferSize = 512;

  Clock clock = Y.clockFixed();

  HttpHandler handler = _ -> {};

  long id = Long.MAX_VALUE;

  Note.Sink noteSink = Y.noteSink();

  HttpSessionLoader sessionLoader = (_, _) -> null;

  Socket socket;

  public static String resp(Consumer<? super HttpServerTaskY> opts) {
    final HttpServerTaskY y;
    y = new HttpServerTaskY();

    opts.accept(y);

    final HttpServerTask task;
    task = y.build();

    task.run();

    return y.response();
  }

  public static HttpServerTaskY run(Consumer<? super HttpServerTaskY> opts) {
    final HttpServerTaskY y;
    y = new HttpServerTaskY();

    opts.accept(y);

    final HttpServerTask task;
    task = y.build();

    task.run();

    return y;
  }

  public final String response() {
    return Y.toString(socket);
  }

  private record ThisBodyOptions(Path directory, int memoryMax, long sizeMax)
      implements HttpRequestBodyOptions {

    @Override
    public final HttpRequestBodySupport supportOf(long id) {
      return new HttpRequestBodySupport() {

        private Path file;

        @Override
        public final void close() throws IOException {
          if (file != null) {
            final Path deleteMe;
            deleteMe = file;

            file = null;

            Files.delete(deleteMe);
          }
        }

        @Override
        final Path file() {
          if (file == null) {
            final String format;

            if (id < 0) {
              format = "%019d.neg";
            } else {
              format = "%019d";
            }

            final String name;
            name = String.format(format, id);

            file = directory.resolve(name);
          }

          return file;
        }

        @Override
        final int memoryMax() {
          return memoryMax;
        }

        @Override
        final long sizeMax() {
          return sizeMax;
        }

      };
    }

  }

  private HttpServerTask build() {
    return new HttpServerTask(
        new ThisBodyOptions(bodyDirectory, bodyMemoryMax, bodySizeMax),

        new byte[bufferSize],

        clock,

        handler,

        id,

        noteSink,

        sessionLoader,

        socket
    );
  }

}
