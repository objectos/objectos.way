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
package objectox.http.host;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Consumer;
import objectos.http.Handler;
import objectos.http.HostOptions;
import objectos.http.SessionOptions;
import objectos.http.StaticFilesOptions;
import objectox.http.handler.HandlerNoop;
import objectox.http.media.StaticFiles;
import objectox.http.media.StaticFilesBuilder;
import objectox.http.session.SessionSupport;
import objectox.http.session.SessionSupportBuilder;

public final class HostStageBuilder implements HostOptions {

  private Handler handler = HandlerNoop.INSTANCE;

  private String name = "localhost";

  private SessionSupport sessionSupport;

  private StaticFiles staticFiles;

  public final HostStage build() {
    return new HostStage(
        handler,

        name,

        sessionSupport,

        staticFiles
    );
  }

  @Override
  public final void name(String value) {
    name = Objects.requireNonNull(value, "value == null");

    final int colon;
    colon = name.indexOf(':');

    if (colon != -1) {
      final String msg;
      msg = "Invalid host name: names must not include the port number '%s'".formatted(name);

      throw new IllegalArgumentException(msg);
    }
  }

  @Override
  public final void handler(Handler value) {
    handler = Objects.requireNonNull(value, "value == null");
  }

  @Override
  public final void session(Consumer<? super SessionOptions> opts) {
    if (sessionSupport != null) {
      final String msg;
      msg = "Session support has already been configured";

      throw new IllegalStateException(msg);
    }

    final SessionSupportBuilder builder;
    builder = new SessionSupportBuilder();

    opts.accept(builder);

    sessionSupport = builder.build();
  }

  @Override
  public final void staticFiles(Consumer<? super StaticFilesOptions> opts) throws IOException {
    if (staticFiles != null) {
      final String msg;
      msg = "Static files support has already been configured";

      throw new IllegalStateException(msg);
    }

    final StaticFilesBuilder builder;
    builder = new StaticFilesBuilder();

    opts.accept(builder);

    staticFiles = builder.build();
  }

}
