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

import java.util.function.Supplier;
import objectos.http.Http.Method;
import objectos.http.HttpExchange;
import objectos.http.server.Exchange;
import objectos.http.server.Handler;
import objectos.http.server.Request;

public final class TestingHandler implements Handler, Supplier<Handler> {

  public static final TestingHandler INSTANCE = new TestingHandler();

  private TestingHandler() {}

  @Override
  public final Handler get() { return this; }

  @Override
  public final void handle(Exchange exchange) {
    Request request;
    request = exchange.request();

    switch (request.path()) {
      case "/" -> Http001.INSTANCE.handle(exchange);

      case "/chunked.txt" -> Http002.INSTANCE.handle(exchange);

      case "/login" -> login(exchange);

      case "/login.css" -> Http004.INSTANCE.handle(exchange);
    }
  }

  private void login(Exchange exchange) {
    Request request;
    request = exchange.request();

    Method method;
    method = request.method();

    if (method == Method.GET) {
      Http004.INSTANCE.handle(exchange);
    }

    else if (method == Method.POST) {
      Http006.INSTANCE.handle(exchange);
    }
  }

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