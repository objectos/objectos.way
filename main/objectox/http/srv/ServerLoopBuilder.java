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

import java.io.IOException;
import java.net.ServerSocket;
import java.time.Clock;
import java.util.Objects;
import java.util.function.Consumer;
import objectos.http.HostOptions;
import objectos.http.RequestBodyOptions;
import objectos.http.ServerOptions;
import objectos.internal.NoOpSinkSingleton;
import objectos.lang.Stage;
import objectos.way.Note;
import objectox.http.host.HostGlobals;
import objectox.http.host.HostMapBuilder;
import objectox.http.req.RequestBodyConfigBuilder;
import objectox.http.req.RequestBodyConfig;

public final class ServerLoopBuilder
    implements
    ServerOptions {

  private int bufferSize = 4096;

  private Clock clock;

  private final HostMapBuilder hostMapBuilder = new HostMapBuilder();

  private Note.Sink noteSink = NoOpSinkSingleton.INSTANCE;

  private RequestBodyConfig requestBodyConfig;

  private final ServerSocketBuilder serverSocketBuilder = new ServerSocketBuilder();

  @SuppressWarnings("unused")
  private Stage stage = Stage.PROD;

  @Override
  public final void bufferSize(int value) {
    if (value < 128) {
      final String msg;
      msg = "Invalid buffer size: buffers must hold at least 128 bytes";

      throw new IllegalArgumentException(msg);
    }

    bufferSize = value;
  }

  @Override
  public final void clock(Clock value) {
    clock = Objects.requireNonNull(value, "value == null");
  }

  @Override
  public final void host(Consumer<? super HostOptions> opts) {
    hostMapBuilder.add(opts);
  }

  @Override
  public final void noteSink(Note.Sink value) {
    noteSink = Objects.requireNonNull(value, "value == null");
  }

  @Override
  public final void port(int value) {
    serverSocketBuilder.port(value);
  }

  @Override
  public final void requestBody(Consumer<? super RequestBodyOptions> opts) {
    final RequestBodyConfigBuilder builder;
    builder = new RequestBodyConfigBuilder();

    opts.accept(builder);

    requestBodyConfig = builder.build();
  }

  @Override
  public final void stage(Stage value) {
    stage = Objects.requireNonNull(value, "value == null");
  }

  public final ServerLoop build() throws IOException {
    final ServerLoop loop;
    loop = unstarted();

    loop.start();

    return loop;
  }

  public final ServerLoop unstarted() throws IOException {
    final ServerSocket serverSocket;
    serverSocket = serverSocketBuilder.build();

    return new ServerLoop(
        bufferSize,

        clock != null ? clock : Clock.systemUTC(),

        hostMapBuilder.build(new HostGlobals() {
          @Override
          public final int port() {
            return serverSocket.getLocalPort();
          }
        }),

        noteSink,

        requestBodyConfig != null ? requestBodyConfig : RequestBodyConfigBuilder.standard(),

        serverSocket,

        Thread.ofVirtual().name("http-", 1).factory()
    );
  }

}
