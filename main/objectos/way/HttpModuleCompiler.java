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
import java.util.function.Supplier;
import objectos.lang.object.Check;
import objectos.util.array.ObjectArrays;

final class HttpModuleCompiler extends HttpModuleMatcherParser implements Http.Handler {

  private HttpModuleAction[] actions;

  private int actionsIndex;

  private Http.Handler.Interceptor interceptor;

  private SessionStore sessionStore;

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
    if (sessionStore != null) {
      http.acceptSessionStore(sessionStore);
    }

    for (int index = 0, length = actions.length; index < length; index++) {
      HttpModuleAction action;
      action = actions[index];

      if (action.execute(http)) {
        return;
      }
    }

    http.notFound();
  }

  final void filter(Http.Handler handler) {
    handler = decorate(handler);

    int index;
    index = nextSlot();

    actions[index] = new HttpModuleFilter(handler);
  }

  final void interceptor(Http.Handler.Interceptor next) {
    if (interceptor == null) {
      interceptor = next;
    } else {
      interceptor = handler -> interceptor.intercept(next.intercept(handler));
    }
  }

  final void route(String pathExpression, Http.Handler handler, HttpModuleRouteOptions options) {
    HttpModuleMatcher matcher;
    matcher = parseAndDecorate(pathExpression, options);

    Http.Handler actualHandler;
    actualHandler = decorate(handler);

    int index;
    index = nextSlot();

    actions[index] = new HttpModuleRoute.RouteHandler(matcher, actualHandler);
  }

  final void route(String pathExpression, Supplier<Http.Handler> supplier, HttpModuleRouteOptions options) {
    HttpModuleMatcher matcher;
    matcher = parseAndDecorate(pathExpression, options);

    Supplier<Http.Handler> actualSupplier;
    actualSupplier = interceptor == null ? supplier : () -> interceptor.intercept(supplier.get());

    int index;
    index = nextSlot();

    actions[index] = new HttpModuleRoute.RouteSupplier(matcher, actualSupplier);
  }

  final void sessionStore(SessionStore sessionStore) {
    Check.state(this.sessionStore == null, "A session store has already been configured");

    this.sessionStore = sessionStore;
  }

  private HttpModuleMatcher parseAndDecorate(String pathExpression, HttpModuleRouteOptions options) {
    HttpModuleMatcher matcher;
    matcher = matcher(pathExpression);

    return options.decorate(matcher);
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
      actions = ObjectArrays.growIfNecessary(actions, requiredIndex);
    }

    return requiredIndex;
  }

}