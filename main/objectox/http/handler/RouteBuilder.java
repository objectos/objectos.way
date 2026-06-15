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
import java.util.List;
import java.util.Objects;
import objectos.http.Handler;
import objectos.http.Redirection;
import objectos.http.Response;
import objectos.http.Result;

final class RouteBuilder {

  private List<Handler> handlers = List.of();

  private final RouteMatcher matcher;

  private boolean result;

  RouteBuilder(RouteMatcher matcher) {
    this.matcher = Objects.requireNonNull(matcher, "matcher == null");
  }

  public final Route build() {
    final Handler handler;
    handler = buildHandler();

    return new Route(matcher, handler);
  }

  private Handler buildHandler() {
    return switch (handlers.size()) {
      case 0 -> HandlerNoop.INSTANCE;

      case 1 -> handlers.get(0);

      default -> HandlerList.copyOf(handlers);
    };
  }

  public final void addRedirect(Redirection value) {
    addResult(value);
  }

  public final void addResponse(Response value) {
    addResult(value);
  }

  private void add(Handler handler) {
    if (handlers.isEmpty()) {
      handlers = new ArrayList<>();
    }

    handlers.add(handler);
  }

  private void addResult(Result value) {
    if (result) {
      final String msg;
      msg = "A result has already been set";

      throw new IllegalStateException(msg);
    }

    final Handler handler;
    handler = new HandlerResult(value);

    add(handler);

    result = true;
  }

}
