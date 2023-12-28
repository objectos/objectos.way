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
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import objectos.http.Http;
import objectox.http.HttpStatus;
import objectox.http.StandardHeaderName;
import org.testng.annotations.Test;

@SuppressWarnings("resource")
public class HttpExchangeHandleTest {

  @Test
  public void http001() {
    ObjectoxHttpExchange exchange;
    exchange = new ObjectoxHttpExchange();

    Http001.INPUT.accept(exchange);

    assertTrue(exchange.active());

    Http001.response(exchange);

    assertEquals(exchange.bufferIndex, Http001.INPUT.requestLength());
    assertEquals(exchange.bufferLimit, Http001.INPUT.requestLength());
    assertEquals(exchange.error, null);
    // "Connection: close" should set the property
    assertEquals(exchange.keepAlive, false);
    assertEquals(exchange.method, Http.Method.GET);
    assertEquals(exchange.requestHeaders, Map.of(
      StandardHeaderName.HOST, TestingInput.hv("www.example.com"),
      StandardHeaderName.CONNECTION, TestingInput.hv("close")
    ));
    assertEquals(exchange.requestHeaderName, null);
    assertEquals(exchange.requestPath.toString(), "/");
    // response body set
    assertEquals(exchange.responseBody, Bytes.utf8("Hello World!\n"));
    // response headers set
    assertEquals(exchange.responseHeaders, List.of(
      new HttpResponseHeader(StandardHeaderName.CONTENT_TYPE, "text/plain; charset=utf-8"),
      new HttpResponseHeader(StandardHeaderName.CONTENT_LENGTH, "13"),
      new HttpResponseHeader(StandardHeaderName.DATE, "Wed, 28 Jun 2023 12:08:43 GMT")
    ));
    assertEquals(exchange.responseHeadersIndex, -1);
    assertEquals(exchange.socket.isClosed(), false);
    assertEquals(exchange.state, ObjectoxHttpExchange._HANDLE_INVOKE);
    // response status set
    assertEquals(exchange.status, HttpStatus.OK);
    assertEquals(exchange.versionMajor, 1);
    assertEquals(exchange.versionMinor, 1);
  }

  @Test
  public void http004() {
    ObjectoxHttpExchange exchange;
    exchange = new ObjectoxHttpExchange();

    Http004.INPUT.accept(exchange);

    assertTrue(exchange.active());

    Http004.response(exchange);

    assertEquals(exchange.bufferIndex, Http004.INPUT01.length());
    assertEquals(exchange.bufferLimit, Http004.INPUT01.length());
    assertEquals(exchange.error, null);
    // "Connection: close" should set the property
    assertEquals(exchange.keepAlive, true);
    assertEquals(exchange.method, Http.Method.GET);
    // request headers won't be used from this point forward
    assertEquals(exchange.requestHeaders, Map.of(
      StandardHeaderName.HOST, TestingInput.hv("www.example.com"),
      StandardHeaderName.CONNECTION, TestingInput.hv("keep-alive")
    ));
    assertEquals(exchange.requestHeaderName, null);
    assertEquals(exchange.requestPath.toString(), "/login");
    // response body set
    assertEquals(exchange.responseBody, Bytes.utf8(Http004.BODY01));
    // response headers set
    assertEquals(exchange.responseHeaders, List.of(
      new HttpResponseHeader(StandardHeaderName.CONTENT_TYPE, "text/html; charset=utf-8"),
      new HttpResponseHeader(StandardHeaderName.CONTENT_LENGTH, "171"),
      new HttpResponseHeader(StandardHeaderName.DATE, "Fri, 07 Jul 2023 14:11:45 GMT")
    ));
    assertEquals(exchange.responseHeadersIndex, -1);
    assertEquals(exchange.socket.isClosed(), false);
    assertEquals(exchange.state, ObjectoxHttpExchange._HANDLE_INVOKE);
    // response status set
    assertEquals(exchange.status, HttpStatus.OK);
    assertEquals(exchange.versionMajor, 1);
    assertEquals(exchange.versionMinor, 1);
  }

  @Test(description = """
  [#448] HANDLE --> HANDLE_INVOKE

  - sets closeConnection
  - resets responseBody
  - creates responseHeaders
  """)
  public void handle() {
    ObjectoxHttpExchange exchange;
    exchange = new ObjectoxHttpExchange();

    record Test(String connection, boolean keepAlive) {}

    List<Test> tests;
    tests = List.of(
      new Test("Close", false),
      new Test("close", false),
      new Test("Keep-Alive", true),
      new Test("keep-alive", true)
    );

    for (Test test : tests) {
      exchange.keepAlive = false;
      exchange.requestHeaders = Map.of(StandardHeaderName.CONNECTION, hv(test.connection));
      exchange.responseBody = Bytes.utf8("body");
      exchange.responseHeaders = null;
      exchange.state = ObjectoxHttpExchange._HANDLE;

      exchange.stepOne();

      assertEquals(exchange.keepAlive, test.keepAlive);
      assertEquals(exchange.responseBody, null);
      assertNotNull(exchange.responseHeaders);
      assertEquals(exchange.state, ObjectoxHttpExchange._HANDLE_INVOKE);
    }
  }

  private BufferHeaderValue hv(String string) {
    byte[] bytes;
    bytes = string.getBytes(StandardCharsets.UTF_8);

    return new BufferHeaderValue(bytes, 0, bytes.length);
  }

}