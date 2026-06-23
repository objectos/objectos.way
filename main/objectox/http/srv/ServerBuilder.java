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
import java.time.Clock;
import java.util.Objects;
import java.util.function.Consumer;
import objectos.http.HostOptions;
import objectos.http.ServerOptions;
import objectos.internal.NoOpSinkSingleton;
import objectos.way.Note;
import objectox.http.host.HostMapBuilder;
import objectox.http.req.RequestBodyOptionsPojo;

public final class ServerBuilder
    implements
    ServerOptions {

  @SuppressWarnings("unused")
  private final int bufferSize = 4096;

  @SuppressWarnings("unused")
  private Clock clock;

  private final HostMapBuilder hostMapBuilder = new HostMapBuilder();

  @SuppressWarnings("unused")
  private Note.Sink noteSink = NoOpSinkSingleton.INSTANCE;

  @SuppressWarnings("unused")
  private RequestBodyOptionsPojo requestBodyOptions;

  private final ServerSocketBuilder serverSocketBuilder = new ServerSocketBuilder();

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

  public final ServerLoop build() throws IOException {
    throw new UnsupportedOperationException("Implement me");
  }

}
