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
import java.util.function.Function;
import objectos.lang.object.Check;
import objectos.util.array.ObjectArrays;
import objectos.way.HttpModule.Matcher;

final class HttpModuleCompiler implements Http.Handler {

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

  final void route(Matcher matcher, Http.Handler handler) {
    handler = decorate(handler);
    
    int index;
    index = nextSlot();

    actions[index] = new HttpModuleRoute.RouteHandler(matcher, handler);
  }

  final <T> void route(Matcher matcher, Function<T, Http.Handler> factory, T value) {
    Function<T, Http.Handler> function;
    function = interceptor == null ? factory : (T t) -> interceptor.intercept(factory.apply(t));

    int index;
    index = nextSlot();

    actions[index] = new HttpModuleRoute.RouteFactory1<>(matcher, function, value);
  }

  final void sessionStore(SessionStore sessionStore) {
    Check.state(this.sessionStore == null, "A session store has already been configured");

    this.sessionStore = sessionStore;
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