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

final class ServerCoreY {

  Handler handler;

  Note.Sink noteSink = Y.noteSink();

  Socket socket;

  Thread.Builder threadBuilder;

  private ServerCoreY() {}

  public static String resp(Consumer<? super ServerCoreY> opts) {
    final ServerCoreY y;
    y = run(opts);

    final Socket socket;
    socket = y.socket;

    return socket.toString();
  }

  public static ServerCoreY run(Consumer<? super ServerCoreY> opts) {
    final ServerCoreY y;
    y = new ServerCoreY();

    opts.accept(y);

    final Runnable task;
    task = y.build();

    task.run();

    return y;
  }

  private Runnable build() {
    final ServerCore core;
    core = new ServerCore(noteSink, threadBuilder);

    return core.createTask(socket);
  }

}
