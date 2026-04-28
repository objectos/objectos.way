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
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

final class HttpRouting0 implements HttpRouting {

  private final List<HttpHandler> handlers = new ArrayList<>();

  private Set<HttpMethod> methods = Set.of();

  private Set<String> paths = Set.of();

  public final HttpHandler build() {
    build0Methods();

    handlers.add(HttpHandler2NotFound.INSTANCE);

    return switch (handlers.size()) {
      case 1 -> handlers.get(0);

      default -> new HttpHandler4List(List.copyOf(handlers));
    };
  }

  private void build0Methods() {
    if (methods.isEmpty()) {
      return;
    }

    if (methods.contains(HttpMethod.GET)) {
      methods.add(HttpMethod.HEAD);
    }

    else if (methods.contains(HttpMethod.HEAD)) {
      methods.add(HttpMethod.GET);
    }

    final HttpHandler handler;
    handler = new HttpHandler1MethodNotAllowed(methods);

    handlers.add(handler);
  }

  @Override
  public final void handler(HttpHandler value) {
    final HttpHandler h;
    h = Objects.requireNonNull(value, "value == null");

    handlers.add(h);
  }

  @Override
  public final void GET(HttpHandler value) {
    method0(HttpMethod.GET, value);
  }

  @Override
  public final void POST(HttpHandler value) {
    method0(HttpMethod.POST, value);
  }

  private void method0(HttpMethod method, HttpHandler value) {
    final HttpHandler h;
    h = Objects.requireNonNull(value, "value == null");

    if (methods.isEmpty()) {
      methods = EnumSet.noneOf(HttpMethod.class);
    }

    if (!methods.add(method)) {
      final String msg;
      msg = "A handler for " + method + " was already registered";

      throw new IllegalStateException(msg);
    }

    final HttpHandler handler;
    handler = new HttpHandler0Method(method, h);

    handlers.add(handler);
  }

  @Override
  public final void path(String path, Consumer<? super HttpRouting> routing) {
    Objects.requireNonNull(path, "path == null");

    if (paths.isEmpty()) {
      paths = new HashSet<>();
    }

    if (!paths.add(path)) {
      final String msg;
      msg = "Duplicate path expression: " + path;

      throw new IllegalArgumentException(msg);
    }

    final HttpPathMatcherParser parser;
    parser = new HttpPathMatcherParser(path);

    final HttpPathMatcher matcher;
    matcher = parser.parse();

    final HttpHandler delegate;
    delegate = HttpHandler.create(routing);

    final HttpHandler handler;
    handler = new HttpHandler3Path(matcher, delegate);

    handlers.add(handler);
  }

}
