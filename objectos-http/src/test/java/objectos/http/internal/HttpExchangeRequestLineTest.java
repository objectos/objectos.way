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
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import java.util.Arrays;
import java.util.List;
import objectos.http.Status;
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

  @Test(description = """
  [#430] REQUEST_LINE --> CLIENT_ERROR::BAD_REQUEST

  - buffer should remain untouched
  """)
  public void requestLineToClientErrorBadRequest() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    byte[] bytes;
    bytes = "(GET) /".getBytes();

    exchange.buffer = bytes;
    exchange.bufferIndex = 0;
    exchange.bufferLimit = bytes.length;
    exchange.state = HttpExchange._REQUEST_LINE;

    exchange.stepOne();

    assertEquals(exchange.bufferIndex, 0);
    assertEquals(exchange.state, HttpExchange._CLIENT_ERROR);
    assertEquals(exchange.status, Status.BAD_REQUEST);
  }

  @Test(description = """
  [#431] HTTP 001: REQUEST_LINE_METHOD --> REQUEST_LINE_TARGET

  - check if bufferIndex is updated
  """)
  public void requestLineMethod() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    byte[] bytes;
    bytes = "GET /".getBytes();

    exchange.buffer = bytes;
    exchange.bufferIndex = 0;
    exchange.bufferLimit = bytes.length;
    exchange.method = Method.GET;
    exchange.state = HttpExchange._REQUEST_LINE_METHOD;

    exchange.stepOne();

    assertEquals(exchange.bufferIndex, 4);
    assertEquals(exchange.state, HttpExchange._REQUEST_LINE_TARGET);
  }

  @Test(description = """
  [#432] HTTP 001: REQUEST_LINE_METHOD --> CLIENT_ERROR::BAD_REQUEST

  - bufferIndex is NOT updated
  """)
  public void requestLineMethodToClientErrorBadRequest() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    byte[] bytes;
    bytes = "GOT /".getBytes();

    exchange.buffer = bytes;
    exchange.bufferIndex = 0;
    exchange.bufferLimit = bytes.length;
    exchange.method = Method.GET;
    exchange.state = HttpExchange._REQUEST_LINE_METHOD;

    exchange.stepOne();

    assertEquals(exchange.bufferIndex, 0);
    assertEquals(exchange.state, HttpExchange._CLIENT_ERROR);
    assertEquals(exchange.status, Status.BAD_REQUEST);
  }

  @Test(description = """
  [#433] HTTP 001: REQUEST_LINE_METHOD --> INPUT_READ
  """)
  public void requestLineMethodToInputRead() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    byte[] bytes;
    bytes = "GE".getBytes();

    exchange.buffer = bytes;
    exchange.bufferIndex = 0;
    exchange.bufferLimit = bytes.length;
    exchange.method = Method.GET;
    exchange.state = HttpExchange._REQUEST_LINE_METHOD;

    exchange.stepOne();

    assertEquals(exchange.bufferIndex, 0);
    assertEquals(exchange.nextAction, HttpExchange._REQUEST_LINE_METHOD);
    assertEquals(exchange.state, HttpExchange._INPUT_READ);
  }

  @Test(description = """
  [#434] HTTP 001: REQUEST_LINE_TARGET --> REQUEST_LINE_VERSION

  - bufferIndex should be after SP
  - requestTarget should contain the correct indices
  """)
  public void requestLineTarget() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    byte[] bytes;
    bytes = "GET / HTTP/1.1".getBytes();

    exchange.buffer = bytes;
    exchange.bufferIndex = 4;
    exchange.bufferLimit = bytes.length;
    exchange.state = HttpExchange._REQUEST_LINE_TARGET;

    exchange.stepOne();

    assertEquals(exchange.bufferIndex, 6);
    assertNotNull(exchange.requestTarget);
    assertEquals(exchange.requestTarget.start(), 4);
    assertEquals(exchange.requestTarget.end(), 5);
    assertEquals(exchange.state, HttpExchange._REQUEST_LINE_VERSION);
  }

  @Test(description = """
  [#435] HTTP 001: REQUEST_LINE_TARGET --> INPUT_READ

  - bufferIndex should not have been updated
  """)
  public void requestLineTargetToInputRead() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    byte[] bytes;
    bytes = "GET /".getBytes();

    // buffer is not full
    exchange.buffer = Arrays.copyOf(bytes, bytes.length + 1);
    exchange.bufferIndex = 4;
    exchange.bufferLimit = bytes.length;
    exchange.state = HttpExchange._REQUEST_LINE_TARGET;

    exchange.stepOne();

    assertEquals(exchange.bufferIndex, 4);
    assertNull(exchange.requestTarget);
    assertEquals(exchange.state, HttpExchange._INPUT_READ);
  }

  @Test(description = """
  [#436] HTTP 001: REQUEST_LINE_TARGET --> CLIENT_ERROR::URI_TOO_LONG
  """)
  public void executeParseRequestTarget03UriTooLong() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    byte[] bytes;
    bytes = "GET /attack!".getBytes();

    // buffer is full
    exchange.buffer = bytes;
    exchange.bufferIndex = 4;
    exchange.bufferLimit = bytes.length;
    exchange.state = HttpExchange._REQUEST_LINE_TARGET;

    exchange.stepOne();

    assertEquals(exchange.bufferIndex, 4);
    assertNull(exchange.requestTarget);
    assertEquals(exchange.state, HttpExchange._CLIENT_ERROR);
    assertEquals(exchange.status, Status.URI_TOO_LONG);
  }

}