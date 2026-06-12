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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import objectox.http.route.RouteMatcher;
import objectox.http.route.RouteParser;

final class HttpRoutes0 implements HttpRoutes {

  private final List<HttpHandler> handlers = new ArrayList<>();

  private Set<String> paths = Set.of();

  public final HttpHandler build() {
    return switch (handlers.size()) {
      case 0 -> _ -> {};

      case 1 -> handlers.get(0);

      default -> new HttpHandler5List(List.copyOf(handlers));
    };
  }

  @Override
  public final void at(String path, Option first, Option... rest) {
    Objects.requireNonNull(path, "path == null");

    if (paths.isEmpty()) {
      paths = new HashSet<>();
    }

    if (!paths.add(path)) {
      final String msg;
      msg = "Path already registered: " + path;

      throw new IllegalArgumentException(msg);
    }

    final RouteParser parser;
    parser = new RouteParser(path);

    final RouteMatcher matcher;
    matcher = parser.parse();

    final Set<String> paramNames;
    paramNames = parser.paramNames();

    final HttpRoutes1Path builder;
    builder = new HttpRoutes1Path(matcher, paramNames);

    builder.add(first, "first");

    for (int idx = 0; idx < rest.length; idx++) {
      final Option o;
      o = rest[idx];

      builder.add(o, "rest", idx);
    }

    final HttpHandler handler;
    handler = builder.build();

    handlers.add(handler);
  }

}
