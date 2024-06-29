/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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
package objectos.way;

import java.util.function.Supplier;
import objectos.way.Http.Handler;

sealed abstract class HttpModuleRoute implements HttpModuleAction {

  static final class RouteHandler extends HttpModuleRoute {

    private final Http.Handler handler;

    public RouteHandler(HttpModuleMatcher matcher, Http.Handler handler) {
      super(matcher);
      this.handler = handler;
    }

    @Override
    final Http.Handler handler() { return handler; }

  }

  static final class RouteSupplier extends HttpModuleRoute {

    private final Supplier<Http.Handler> supplier;

    public RouteSupplier(HttpModuleMatcher matcher, Supplier<Handler> supplier) {
      super(matcher);
      this.supplier = supplier;
    }

    @Override
    final Http.Handler handler() {
      return supplier.get();
    }

  }

  private final HttpModuleMatcher matcher;

  public HttpModuleRoute(HttpModuleMatcher matcher) {
    this.matcher = matcher;
  }

  @Override
  public final boolean execute(Http.Exchange http) {
    boolean result;
    result = false;

    Http.Request.Target target;
    target = http.target();

    HttpRequestLine t;
    t = (HttpRequestLine) target;

    t.matcherReset();

    if (matcher.test(t)) {
      Http.Handler handler;
      handler = handler();

      handler.handle(http);

      result = http.processed();
    }

    return result;
  }

  abstract Http.Handler handler();

}