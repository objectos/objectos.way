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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import objectos.http.Http;
import objectos.http.Http.Method;
import org.testng.annotations.Test;

public class HttpExchangeTest {

  @Test
  public void http003() throws IOException {
    HttpExchange exchange;
    exchange = new HttpExchange();

    Http003.INPUT.accept(exchange);

    assertTrue(exchange.active());

    Http003.response(exchange);

    assertFalse(exchange.active());

    TestableSocket socket;
    socket = (TestableSocket) exchange.socket;

    assertEquals(socket.outputAsString(), Http003.OUTPUT);
  }

  @Test
  public void http004() throws IOException {
    HttpExchange exchange;
    exchange = new HttpExchange();

    Http004.INPUT.accept(exchange);

    assertTrue(exchange.active());

    Http004.response(exchange);

    assertTrue(exchange.active());

    Http004.response(exchange);

    assertFalse(exchange.active());

    TestableSocket socket;
    socket = (TestableSocket) exchange.socket;

    assertEquals(socket.outputAsString(), Http004.OUTPUT);
  }

  @SuppressWarnings("resource")
  @Test
  public void methodIs() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    exchange.method = Http.Method.GET;
    exchange.state = HttpExchange._HANDLE_INVOKE;

    assertEquals(exchange.methodIs(Method.GET), true);
    assertEquals(exchange.methodIs(Method.GET, Method.POST), true);
    assertEquals(exchange.methodIs(Method.POST, Method.GET), true);
    assertEquals(exchange.methodIs(Method.POST), false);
    assertEquals(exchange.methodIs(Method.POST, Method.DELETE), false);
  }

}