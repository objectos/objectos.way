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
package objectox.http.srv;

import java.net.Socket;
import java.nio.file.Path;
import java.time.Clock;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import objectos.http.Handler;
import objectos.way.Note;
import objectos.way.Y;
import objectos.y.PathY;
import objectos.y.SocketY;
import objectox.http.host.Host;
import objectox.http.host.HostMap;
import objectox.http.host.HostY;
import objectox.http.req.RequestBodyConfig;
import objectox.http.resp.ResponseDate;

final class ServerTaskY {

  int bufferSize = 1024;

  Clock clock = Y.clockFixed();

  private final List<Host> hosts = new ArrayList<>();

  Note.Sink noteSink = Y.noteSink();

  RequestBodyConfig requestBodyConfig;

  Socket socket;

  private ServerTaskY() {}

  public static ServerTask create(Consumer<? super ServerTaskY> opts) {
    final ServerTaskY y;
    y = new ServerTaskY();

    opts.accept(y);

    return y.build();
  }

  public static String resp(Consumer<? super ServerTaskY> opts) {
    final ServerTask task;
    task = run(opts);

    final Socket socket;
    socket = task.socket;

    return socket.toString();
  }

  public static String resp(ServerTask task) {
    task.run();

    final Socket socket;
    socket = task.socket;

    return socket.toString();
  }

  public static ServerTask run(Consumer<? super ServerTaskY> opts) {
    final ServerTaskY y;
    y = new ServerTaskY();

    opts.accept(y);

    final ServerTask task;
    task = create(opts);

    task.run();

    return task;
  }

  public final void host(Consumer<? super HostY> opts) {
    hosts.add(HostY.create(opts));
  }

  public final void host(String name, Handler handler) {
    hosts.add(HostY.create(opts -> {
      opts.name = name;

      opts.handler = handler;
    }));
  }

  public final void requestBodyConfig(Path file) {
    requestBodyConfig = new RequestBodyConfig(
        () -> file,

        1024,

        4096
    );
  }

  public final void requestBodyConfig(Path file, int memoryMax) {
    requestBodyConfig = new RequestBodyConfig(
        () -> file,

        memoryMax,

        4096
    );
  }

  public final void socket(Object... data) {
    socket = SocketY.of(data);
  }

  private ServerTask build() {
    return new ServerTask(
        bufferSize,

        HostMap.of(hosts),

        noteSink,

        requestBodyConfig != null
            ? requestBodyConfig
            : requestBodyConfig(),

        new ResponseDate(clock),

        socket
    );
  }

  private RequestBodyConfig requestBodyConfig() {
    return new RequestBodyConfig(
        () -> PathY.nextFile(),

        1024,

        4096
    );
  }

}
