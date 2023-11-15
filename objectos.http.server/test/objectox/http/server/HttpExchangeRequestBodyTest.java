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
package objectox.http.server;

import static org.testng.Assert.assertEquals;

import java.util.Map;
import objectos.http.Http;
import objectox.http.StandardHeaderName;
import org.testng.annotations.Test;

public class HttpExchangeRequestBodyTest {

  @Test
  public void http006() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    Http006.INPUT.accept(exchange);

    while (exchange.state < HttpExchange._HANDLE) {
      exchange.stepOne();
    }

    // buffer should have been exhausted
    assertEquals(exchange.bufferIndex, Http006.INPUT.requestLength());
    assertEquals(exchange.bufferLimit, Http006.INPUT.requestLength());
    assertEquals(exchange.error, null);
    assertEquals(exchange.keepAlive, true);
    assertEquals(exchange.method, Http.Method.POST);
    // request body should have been created
    assertEquals(exchange.requestBody.toString(), "email=user%40example.com");
    assertEquals(exchange.requestHeaders, Map.of(
      StandardHeaderName.HOST, TestingInput.hv("www.example.com"),
      StandardHeaderName.CONTENT_LENGTH, TestingInput.hv("24"),
      StandardHeaderName.CONTENT_TYPE, TestingInput.hv("application/x-www-form-urlencoded")
    ));
    assertEquals(exchange.requestHeaderName, null);
    assertEquals(exchange.requestPath.toString(), "/login");
    assertEquals(exchange.responseBody, null);
    assertEquals(exchange.responseHeaders, null);
    assertEquals(exchange.responseHeadersIndex, -1);
    assertEquals(exchange.socket.isClosed(), false);
    assertEquals(exchange.state, HttpExchange._HANDLE);
    assertEquals(exchange.status, null);
    assertEquals(exchange.versionMajor, 1);
    assertEquals(exchange.versionMinor, 1);
  }

}