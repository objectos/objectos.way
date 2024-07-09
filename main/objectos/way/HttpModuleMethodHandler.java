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

import objectos.way.Http.Handler;
import objectos.way.Http.Request.Method;
import objectos.way.HttpModule.MethodHandler;

sealed abstract class HttpModuleMethodHandler extends HttpModule.MethodHandler {

  private static final class OfHandler extends HttpModuleMethodHandler implements Http.Handler {

    private final Http.Handler handler;

    OfHandler(Method method, Handler handler) {
      super(method);
      this.handler = handler;
    }

    @Override
    public final void handle(Http.Exchange http) {
      if (handles(http)) {
        handler.handle(http);
      } else {
        http.methodNotAllowed();
      }
    }

    @Override
    final Http.Handler compile() {
      return this;
    }

  }

  private final Http.Request.Method method;

  HttpModuleMethodHandler(Method method) {
    this.method = method;
  }

  public static MethodHandler ofHandler(Http.Request.Method method, Http.Handler handler) {
    return new OfHandler(method, handler);
  }

  final boolean handles(Http.Exchange http) {
    Http.Request.Method actual;
    actual = http.method();

    if (method.is(Http.GET)) {
      return actual.is(Http.GET, Http.HEAD);
    } else {
      return actual.is(method);
    }
  }

}