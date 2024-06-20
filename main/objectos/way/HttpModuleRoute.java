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

import java.util.function.Function;
import objectos.way.HttpModule.Matcher;

sealed abstract class HttpModuleRoute implements HttpModuleAction {

  static final class RouteHandler extends HttpModuleRoute {

    private final Http.Handler handler;

    public RouteHandler(Matcher matcher, Http.Handler handler) {
      super(matcher);
      this.handler = handler;
    }

    @Override
    final Http.Handler handler() { return handler; }

  }

  static final class RouteFactory1<T> extends HttpModuleRoute {

    private final Function<T, Http.Handler> factory;
    private final T value;

    public RouteFactory1(Matcher matcher, Function<T, Http.Handler> factory, T value) {
      super(matcher);
      this.factory = factory;
      this.value = value;
    }

    @Override
    final Http.Handler handler() {
      return factory.apply(value);
    }

  }

  private final Matcher matcher;

  public HttpModuleRoute(Matcher matcher) {
    this.matcher = matcher;
  }

  @Override
  public final boolean execute(Http.Exchange http) {
    boolean result;
    result = false;

    if (matcher.test(http)) {
      Http.Handler handler;
      handler = handler();

      handler.handle(http);

      result = http.processed();
    }

    return result;
  }

  abstract Http.Handler handler();
  
}