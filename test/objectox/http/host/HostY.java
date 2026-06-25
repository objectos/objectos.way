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
import java.io.UncheckedIOException;
import java.util.function.Consumer;
import objectos.http.Handler;
import objectos.http.SessionOptions;

public final class HostY implements HostGlobals {

  public Handler handler;

  public String name;

  public Consumer<? super SessionOptions> session;

  public static Host create(Consumer<? super HostY> opts) {
    final HostY y;
    y = new HostY();

    opts.accept(y);

    return y.build();
  }

  private Host build() {
    try {
      final HostStageBuilder builder;
      builder = new HostStageBuilder();

      builder.handler(handler);

      builder.name(name);

      if (session != null) {
        builder.session(session);
      }

      final HostStage stage;
      stage = builder.build();

      return stage.toHost(this);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  @Override
  public final int port() {
    return 80;
  }

}
