/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
package objectox.http;

import objectos.http.Http.Method;
import objectos.http.HttpExchange;

public final class TestingHandler {

  public static final TestingHandler INSTANCE = new TestingHandler();

  private TestingHandler() {}

  public final void acceptHttpExchange(HttpExchange exchange) {
    if (exchange.hasResponse()) {
      throw new UnsupportedOperationException("Implement me");
    }

    switch (exchange.path()) {
      case "/" -> { Http001.response(exchange); return; }

      case "/chunked.txt" -> { Http002.response(exchange); return; }
    }

    Method method;
    method = exchange.method();

    if (method == Method.GET) {
      Http004.response(exchange);
    }

    else if (method == Method.POST) {
      Http006.response(exchange);
    }

    if (!exchange.hasResponse()) {
      throw new UnsupportedOperationException("Implement me");
    }
  }

}