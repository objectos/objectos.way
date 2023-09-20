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

public class HttpExchangeTest {

  @Test
  public void http003() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    Http003.INPUT.accept(exchange);

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
    assertEquals(exchange.socket.isClosed(), true);
    assertEquals(exchange.state, HttpExchange._STOP);
    assertEquals(exchange.status, null);
    assertEquals(exchange.versionMajor, -1);
    assertEquals(exchange.versionMinor, -1);

    TestableSocket socket;
    socket = (TestableSocket) exchange.socket;

    assertEquals(socket.outputAsString(), Http003.OUTPUT);
  }

  @Test
  public void http004() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    Http004.INPUT.accept(exchange);

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
    assertEquals(exchange.socket.isClosed(), true);
    assertEquals(exchange.state, HttpExchange._STOP);
    assertEquals(exchange.status, null);
    assertEquals(exchange.versionMajor, -1);
    assertEquals(exchange.versionMinor, -1);

    TestableSocket socket;
    socket = (TestableSocket) exchange.socket;

    assertEquals(socket.outputAsString(), Http004.OUTPUT);
  }

}