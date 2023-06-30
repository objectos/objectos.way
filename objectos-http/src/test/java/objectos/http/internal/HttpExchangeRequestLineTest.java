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
import org.testng.annotations.Test;

public class HttpExchangeRequestLineTest {

  @Test(description = """
  [#429] HTTP 001: REQUEST_LINE --> REQUEST_LINE_METHOD
  """)
  public void requestLine() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    record Pair(String request, Method method) {}

    List<Pair> pairs = List.of(
      new Pair("CONNECT /", Method.CONNECT),
      new Pair("DELETE /", Method.DELETE),
      new Pair("HEAD /", Method.HEAD),
      new Pair("GET /", Method.GET),
      new Pair("OPTIONS /", Method.OPTIONS),
      new Pair("TRACE /", Method.TRACE)
    );

    for (var pair : pairs) {
      String request;
      request = pair.request;

      byte[] bytes;
      bytes = request.getBytes();

      exchange.buffer = bytes;
      exchange.bufferIndex = 0;
      exchange.bufferLimit = bytes.length;
      exchange.state = HttpExchange._REQUEST_LINE;

      exchange.stepOne();

      assertEquals(exchange.bufferIndex, 0);
      assertEquals(exchange.bufferLimit, bytes.length);
      assertEquals(exchange.method, pair.method);
      assertEquals(exchange.state, HttpExchange._REQUEST_LINE_METHOD);
    }
  }

}