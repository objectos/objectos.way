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

import java.net.Socket;
import java.util.function.Consumer;
import objectos.way.Note;
import objectos.way.Y;

final class HttpServerTaskY {

  private HttpRequestBodyOptions bodyOptions;

  int bufferSize = 1024;

  private HttpHandler handler;

  long id = Long.MAX_VALUE;

  Note.Sink noteSink = Y.noteSink();

  private HttpSessionStoreImpl sessionStore;

  Socket socket;

  public static HttpServerTaskY run(Consumer<? super HttpServerTaskY> opts) {
    final HttpServerTaskY y;
    y = new HttpServerTaskY();

    opts.accept(y);

    final HttpServerTask task;
    task = y.build();

    task.run();

    return y;
  }

  private HttpServerTask build() {
    return new HttpServerTask(
        bodyOptions,

        new byte[bufferSize],

        handler,

        id,

        noteSink,

        sessionStore,

        socket
    );
  }

}
