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
package objectox.http;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import objectos.http.Handler;
import objectos.http.HttpHandler;
import objectos.http.Routing;
import objectos.http.RoutingOption;

public final class RoutingPojo implements Routing {

  private final List<HttpHandler> handlers = new ArrayList<>();

  private Set<String> paths = Set.of();

  public static Handler create0(Consumer<? super RoutingPojo> opts) {
    final RoutingPojo pojo;
    pojo = new RoutingPojo();

    opts.accept(pojo);

    return pojo.build();
  }

  private Handler build() {
    return switch (handlers.size()) {
      default -> HandlerNoop.INSTANCE;
    };
  }

  @Override
  public final void at(String path, RoutingOption first, RoutingOption... rest) {
    Objects.requireNonNull(path, "path == null");

    if (paths.isEmpty()) {
      paths = new HashSet<>();
    }

    if (!paths.add(path)) {
      final String msg;
      msg = "Path already registered: " + path;

      throw new IllegalArgumentException(msg);
    }

    throw new UnsupportedOperationException("Implement me");
  }

}
