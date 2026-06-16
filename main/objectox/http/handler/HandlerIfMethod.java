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

import objectos.http.Handler;
import objectos.http.Request;
import objectos.http.RequestMethod;
import objectos.http.Result;

record HandlerIfMethod(RequestMethod method, Handler handler) implements Handler {

  @Override
  public final Result handle(Request request) {
    final RequestMethod actual;
    actual = request.method();

    if (actual.equals(method)) {
      return handler.handle(request);
    }

    if (actual.equals(RequestMethod.HEAD) && method.equals(RequestMethod.GET)) {
      return handler.handle(request);
    }

    return request;
  }

}
