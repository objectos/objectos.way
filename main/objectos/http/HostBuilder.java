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

import java.util.Objects;

final class HostBuilder implements HostOptions {

  private final HostGlobals globals;

  private Handler handler = HandlerNoop.INSTANCE;

  private String name = "localhost";

  HostBuilder(HostGlobals globals) {
    this.globals = globals;
  }

  @Override
  public final void name(String value) {
    name = Objects.requireNonNull(value, "value == null");
  }

  @Override
  public final void handler(Handler value) {
    handler = Objects.requireNonNull(value, "value == null");
  }

  public final Host build() {
    return new Host(
        $handler(),

        $name()
    );
  }

  private HostHandler $handler() {
    return new HostHandler(handler, HandlerNoop.INSTANCE);
  }

  private String $name() {
    final int port;
    port = globals.port();

    final HostName hostName;
    hostName = new HostName(name, port);

    return hostName.get();
  }

}
