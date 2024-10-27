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

import java.util.Arrays;
import objectos.way.HttpModule.Condition;

final class HttpModuleCompiler extends HttpModuleMatcherParser implements Http.Handler {

  private record FallbackHandler(Http.Handler main, Http.Handler fallback) implements Http.Handler {
    @Override
    public final void handle(Http.Exchange http) {
      main.handle(http);

      if (http.processed()) {
        return;
      }

      fallback.handle(http);
    }
  }

  private HttpModuleAction[] actions;

  private int actionsIndex;

  private Http.Handler.Interceptor interceptor;

  private HttpModuleMatcher routeMatcher;

  private Http.Handler routeHandler;

  private Http.Handler.Interceptor routeInterceptor;

  public final Http.Handler compile() {
    if (actions != null) {
      actions = Arrays.copyOf(actions, actionsIndex);
    } else {
      actions = new HttpModuleAction[0];
    }

    return this;
  }

  @Override
  public final void handle(Http.Exchange http) {
    for (int index = 0, length = actions.length; index < length; index++) {
      HttpModuleAction action;
      action = actions[index];

      if (action.execute(http)) {
        return;
      }
    }

    http.notFound();
  }

  public final void routeStart(String pathExpression) {
    routeMatcher = matcher(pathExpression);
  }

  public final void handleWith(Http.Handler handler) {
    Http.Handler result;
    result = handler;

    if (routeInterceptor != null) {
      result = routeInterceptor.intercept(result);
    }

    if (routeHandler != null) {
      result = new FallbackHandler(routeHandler, result);
    }

    routeHandler = result;
  }

  public final void interceptWith(Interceptor interceptor) {
    if (routeInterceptor == null) {
      routeInterceptor = interceptor;
    } else {
      routeInterceptor = handler -> routeInterceptor.intercept(interceptor.intercept(handler));
    }
  }

  public final void pathParams(Condition[] conditions) {
    routeMatcher = routeMatcher.withConditions(conditions);
  }

  public final void routeEnd() {
    if (routeHandler == null) {
      throw new IllegalArgumentException("Route without handler");
    }

    Http.Handler actualHandler;
    actualHandler = decorate(routeHandler);

    int index;
    index = nextSlot();

    actions[index] = new HttpModuleRoute(routeMatcher, actualHandler);

    routeMatcher = null;

    routeHandler = null;

    routeInterceptor = null;
  }

  private record HttpModuleFilter(Http.Handler handler) implements HttpModuleAction {
    @Override
    public final boolean execute(Http.Exchange http) {
      handler.handle(http);

      return http.processed();
    }
  }

  public final void filter(Http.Handler handler) {
    handler = decorate(handler);

    int index;
    index = nextSlot();

    actions[index] = new HttpModuleFilter(handler);
  }

  private record HttpModuleHost(String name, Http.Handler handler) implements HttpModuleAction {
    @Override
    public final boolean execute(Http.Exchange http) {
      Http.Request.Headers headers;
      headers = http.headers();

      String hostName;
      hostName = headers.first(Http.HeaderName.HOST);

      if (name.equals(hostName)) {
        handler.handle(http);
      }

      return http.processed();
    }
  }

  public final void host(String name, Http.Handler handler) {
    handler = decorate(handler);

    int index;
    index = nextSlot();

    actions[index] = new HttpModuleHost(name, handler);
  }

  public final void interceptor(Http.Handler.Interceptor next) {
    if (interceptor == null) {
      interceptor = next;
    } else {
      interceptor = handler -> interceptor.intercept(next.intercept(handler));
    }
  }

  private Http.Handler decorate(Http.Handler handler) {
    if (interceptor == null) {
      return handler;
    } else {
      return interceptor.intercept(handler);
    }
  }

  private int nextSlot() {
    int requiredIndex;
    requiredIndex = actionsIndex++;

    if (actions == null) {
      actions = new HttpModuleAction[10];
    } else {
      actions = Util.growIfNecessary(actions, requiredIndex);
    }

    return requiredIndex;
  }

}