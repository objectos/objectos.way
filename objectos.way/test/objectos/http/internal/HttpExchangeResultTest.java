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
package objectos.http.internal;

import static org.testng.Assert.assertEquals;

import java.util.List;
import java.util.Map;
import org.testng.annotations.Test;

@SuppressWarnings("resource")
public class HttpExchangeResultTest {

  @Test
  public void http001() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    Http001.INPUT.accept(exchange);

    while (exchange.isActive()) {
      exchange.stepOne();
    }

    assertEquals(exchange.bufferIndex, -1);
    assertEquals(exchange.bufferLimit, -1);
    assertEquals(exchange.error, null);
    assertEquals(exchange.keepAlive, false);
    assertEquals(exchange.method, null);
    assertEquals(exchange.requestHeaders, Map.of());
    assertEquals(exchange.requestHeaderName, null);
    assertEquals(exchange.requestTarget, null);
    assertEquals(exchange.responseBody, null);
    assertEquals(exchange.responseHeaders, List.of());
    assertEquals(exchange.responseHeadersIndex, -1);
    // socket closed
    assertEquals(exchange.socket.isClosed(), true);
    assertEquals(exchange.state, HttpExchange._STOP);
    assertEquals(exchange.status, null);
    assertEquals(exchange.versionMajor, -1);
    assertEquals(exchange.versionMinor, -1);
  }

  @Test(description = """
  [#450] RESULT --> STOP
  """)
  public void result() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    exchange.keepAlive = false;
    exchange.socket = TestableSocket.empty();
    exchange.state = HttpExchange._RESULT;

    exchange.stepOne();

    assertEquals(exchange.socket.isClosed(), true);
    assertEquals(exchange.state, HttpExchange._STOP);
  }

  @Test(description = """
  [#453] RESULT --> SETUP
  """)
  public void resultToSetup() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    exchange.keepAlive = true;
    exchange.socket = TestableSocket.empty();
    exchange.state = HttpExchange._RESULT;

    exchange.stepOne();

    assertEquals(exchange.socket.isClosed(), false);
    assertEquals(exchange.state, HttpExchange._SETUP);
  }

}