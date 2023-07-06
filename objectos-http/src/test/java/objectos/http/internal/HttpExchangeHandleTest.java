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

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import objectos.http.Http;
import objectos.http.server.Exchange;
import objectos.http.server.Handler;
import objectos.http.server.Response;
import objectos.util.GrowableList;
import org.testng.annotations.Test;

public class HttpExchangeHandleTest {

  @Test
  public void http001() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    Http001.INPUT.accept(exchange);

    while (exchange.state < HttpExchange._OUTPUT) {
      exchange.stepOne();
    }

    assertEquals(exchange.bufferIndex, Http001.INPUT.requestLength());
    assertEquals(exchange.bufferLimit, Http001.INPUT.requestLength());
    assertEquals(exchange.error, null);
    // "Connection: close" should set the property
    assertEquals(exchange.keepAlive, false);
    // method won't be used from this point forward
    assertEquals(exchange.method, null);
    // request headers won't be used from this point forward
    assertEquals(exchange.requestHeaders, Map.of());
    assertEquals(exchange.requestHeaderName, null);
    // request target won't be used from this point forward
    assertEquals(exchange.requestTarget, null);
    // response body set
    assertEquals(exchange.responseBody, Bytes.utf8("Hello World!\n"));
    // response headers set
    assertEquals(exchange.responseHeaders, List.of(
      new HttpResponseHeader(HeaderName.CONTENT_TYPE, "text/plain; charset=utf-8"),
      new HttpResponseHeader(HeaderName.CONTENT_LENGTH, "13"),
      new HttpResponseHeader(HeaderName.DATE, "Wed, 28 Jun 2023 12:08:43 GMT")
    ));
    assertEquals(exchange.responseHeadersIndex, -1);
    assertEquals(exchange.socket.isClosed(), false);
    assertEquals(exchange.state, HttpExchange._OUTPUT);
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
    HttpExchange exchange;
    exchange = new HttpExchange();

    exchange.keepAlive = false;
    exchange.requestHeaders = Map.of(HeaderName.CONNECTION, hv("Close"));
    exchange.responseBody = Bytes.utf8("body");
    exchange.responseHeaders = null;
    exchange.state = HttpExchange._HANDLE;

    exchange.stepOne();

    assertEquals(exchange.keepAlive, false);
    assertEquals(exchange.responseBody, null);
    assertNotNull(exchange.responseHeaders);
    assertEquals(exchange.state, HttpExchange._HANDLE_INVOKE);
  }

  @Test
  public void handleInvoke() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    final byte[] bytes;
    bytes = Bytes.utf8("Hello world!\n");

    ZonedDateTime date;
    date = ZonedDateTime.of(
      LocalDate.of(2023, 6, 28),
      LocalTime.of(9, 8, 43),
      ZoneId.of("GMT-3")
    );

    exchange.handler = new Handler() {
      @Override
      public void handle(Exchange exchange) {
        Response response;
        response = exchange.response();

        response.status(Http.Status.OK_200);

        response.header(Http.Header.CONTENT_TYPE, "text/plain; charset=utf-8");

        response.header(Http.Header.CONTENT_LENGTH, Long.toString(bytes.length));

        response.header(Http.Header.DATE, Http.formatDate(date));

        response.send(bytes);
      }
    };
    exchange.responseBody = new byte[0];
    exchange.responseHeaders = new GrowableList<>();
    exchange.state = HttpExchange._HANDLE_INVOKE;

    exchange.stepOne();

    assertEquals(exchange.responseBody, bytes);
    assertEquals(
      exchange.responseHeaders.stream()
          .map(Object::toString)
          .collect(Collectors.joining("\n", "", "\n")),

      """
      Content-Type: text/plain; charset=utf-8
      Content-Length: 13
      Date: Wed, 28 Jun 2023 12:08:43 GMT
      """
    );
    assertEquals(exchange.state, HttpExchange._OUTPUT);
    assertEquals(exchange.status, HttpStatus.OK);
  }

  private HeaderValue hv(String string) {
    byte[] bytes;
    bytes = string.getBytes(StandardCharsets.UTF_8);

    return new HeaderValue(bytes, 0, bytes.length);
  }

}