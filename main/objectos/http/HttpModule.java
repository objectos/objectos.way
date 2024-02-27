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
package objectos.http;

import java.util.Arrays;
import objectos.lang.object.Check;
import objectos.util.array.ObjectArrays;

public abstract class HttpModule {

  sealed static abstract class Matcher {

    Matcher() {}

    abstract boolean test(ServerExchange http);

  }

  private static final class Compiler implements Handler {

    private Route[] routes;

    private int routesIndex;

    public final Handler compile() {
      routes = Arrays.copyOf(routes, routesIndex);

      return this;
    }

    @Override
    public final void handle(ServerExchange http) {
      for (int index = 0, length = routes.length; index < length; index++) {
        Route route;
        route = routes[index];

        if (route.execute(http)) {
          return;
        }
      }

      http.notFound();
    }

    final void route(Matcher matcher, Handler handler) {
      int requiredIndex;
      requiredIndex = routesIndex++;

      if (routes == null) {
        routes = new Route[10];
      } else {
        routes = ObjectArrays.growIfNecessary(routes, requiredIndex);
      }

      routes[requiredIndex] = new Route(matcher, handler);
    }

  }

  private record Route(Matcher matcher, Handler handler) {

    final boolean execute(ServerExchange http) {
      boolean result;
      result = false;

      if (matcher.test(http)) {
        handler.handle(http);

        result = http.processed();
      }

      return result;
    }

  }

  private Compiler compiler;

  protected HttpModule() {}

  public final Handler compile() {
    Check.state(compiler == null, "Another compilation is already in progress");

    try {
      compiler = new Compiler();

      configure();

      return compiler.compile();
    } finally {
      compiler = null;
    }
  }

  protected abstract void configure();

  protected final void route(Matcher matcher, Handler handler) {
    Check.notNull(matcher, "matcher == null");
    Check.notNull(handler, "handler == null");

    compiler.route(matcher, handler);
  }

  // matchers

  private static final class PathIs extends Matcher {

    private final String value;

    PathIs(String value) {
      this.value = value;
    }

    @Override
    final boolean test(ServerExchange http) {
      UriPath path;
      path = http.path();

      return path.is(value);
    }

  }

  protected final Matcher path(String value) {
    Check.notNull(value, "value == null");

    return new PathIs(value);
  }

  // actions

  protected final Handler matrix(Method method, Handler handler) {
    Check.notNull(method, "method == null");
    Check.notNull(handler, "handler == null");

    return http -> http.methodMatrix(method, handler);
  }

  // pre-made actions

  protected final Handler movedPermanently(String location) {
    Check.notNull(location, "location == null");

    return http -> http.movedPermanently(location);
  }

}