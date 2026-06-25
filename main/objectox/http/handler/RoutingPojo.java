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
package objectox.http.handler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import objectos.http.Handler;
import objectos.http.Routing;
import objectos.http.RoutingOption;

public final class RoutingPojo implements Routing {

  private final List<Handler> handlers = new ArrayList<>();

  private Set<String> paths = Set.of();

  public static Handler create0(Consumer<? super RoutingPojo> routes) {
    final RoutingPojo pojo;
    pojo = new RoutingPojo();

    routes.accept(pojo);

    return pojo.build();
  }

  private Handler build() {
    return switch (handlers.size()) {
      case 0 -> HandlerNoop.INSTANCE;

      case 1 -> handlers.get(0);

      default -> HandlerList.copyOf(handlers);
    };
  }

  @Override
  public final void at(String pathExpression, RoutingOption first, RoutingOption... rest) {
    Objects.requireNonNull(pathExpression, "pathExpression == null");

    if (paths.isEmpty()) {
      paths = new HashSet<>();
    }

    if (!paths.add(pathExpression)) {
      final String msg;
      msg = "Path already registered: " + pathExpression;

      throw new IllegalArgumentException(msg);
    }

    final RoutingAt at;
    at = RoutingAt.of(pathExpression);

    at.option(
        Objects.requireNonNull(first, "first == null")
    );

    for (int idx = 0; idx < rest.length; idx++) {
      final RoutingOption option;
      option = rest[idx];

      if (option == null) {
        final String msg;
        msg = "rest[%d] == null".formatted(idx);

        throw new NullPointerException(msg);
      }

      at.option(option);
    }

    final Handler handler;
    handler = at.build();

    handlers.add(handler);
  }

}
