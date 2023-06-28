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

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;
import objectos.http.AbstractHttpTest;
import objectos.http.HttpProcessor;
import objectos.http.Request;
import objectos.http.Response;
import objectos.http.internal.HttpExchange.HeaderValue;
import objectos.http.internal.HttpExchange.RequestTarget;
import org.testng.annotations.Test;

public class HttpExchangeTest extends AbstractHttpTest {

  @Test(description = """
  HttpExchange TC0001

  - GET / 1.1
  - Host
  """)
  public void testCase0001() throws Throwable {
    TestableSocket socket;
    socket = TestableSocket.parse("""
    GET / HTTP/1.1
    Host: localhost:7001

    """);

    HttpProcessor processor;
    processor = new HttpProcessor() {
      ZonedDateTime date = ZonedDateTime.of(
        LocalDate.of(2023, 6, 28),
        LocalTime.of(9, 8, 43),
        ZoneId.of("GMT-3")
      );

      @Override
      public final void process(Request request, Response response) {
        Charset charset;
        charset = StandardCharsets.UTF_8;

        String text;
        text = "Hello world!\n";

        byte[] bytes;
        bytes = text.getBytes(charset);

        response.contentType("text/plain; charset=utf-8");

        response.contentLength(bytes.length);

        response.date(date);

        response.send(bytes);
      }
    };

    HttpExchange exchange;
    exchange = new HttpExchange(64, noteSink, processor, socket);

    assertEquals(socket.isClosed(), false);
    assertEquals(exchange.state, HttpExchange._START);

    exchange.stepOne();

    assertEquals(exchange.state, HttpExchange._SOCKET_READ);
    assertEquals(exchange.socketReadAction, HttpExchange._REQUEST_METHOD);

    exchange.stepOne();

    assertEquals(exchange.bufferIndex, 0);
    assertEquals(exchange.bufferLimit, 40);
    assertEquals(exchange.state, HttpExchange._REQUEST_METHOD);

    exchange.stepOne();

    // 'G' 'E' 'T' SP = 4
    assertEquals(exchange.bufferIndex, 4);
    assertEquals(exchange.method, Method.GET);
    assertEquals(exchange.state, HttpExchange._REQUEST_TARGET);

    exchange.stepOne();

    // '/' SP = 2
    assertEquals(exchange.bufferIndex, 6);
    assertEquals(exchange.requestTarget, new RequestTarget(4, 5));
    assertEquals(exchange.state, HttpExchange._REQUEST_VERSION);

    exchange.stepOne();

    // 'H' 'T' 'T' 'P' '/' '1' '.' '1' CR LF = 10
    assertEquals(exchange.bufferIndex, 16);
    assertEquals(exchange.state, HttpExchange._REQUEST_HEADER);
    assertEquals(exchange.version, Version.V1_1);

    exchange.stepOne();

    assertEquals(exchange.bufferIndex, 16);
    assertEquals(exchange.state, HttpExchange._REQUEST_HEADER_NAME);

    exchange.stepOne();

    // 'H' 'o' 's' 't' ':' = 5
    assertEquals(exchange.bufferIndex, 21);
    assertEquals(exchange.requestHeaderName, HeaderName.HOST);
    assertEquals(exchange.state, HttpExchange._REQUEST_HEADER_VALUE);

    exchange.stepOne();

    // SP 'l' 'o' 'c' 'a' 'l' 'h' 'o' 's' 't' = 10
    // ':' '7' '0' '0' '1' CR LF = 7
    assertEquals(exchange.bufferIndex, 38);
    assertHeaderValue(exchange, HeaderName.HOST, "localhost:7001");
    assertEquals(exchange.state, HttpExchange._REQUEST_HEADER);

    exchange.stepOne();

    assertEquals(exchange.bufferIndex, 40);
    assertEquals(exchange.state, HttpExchange._PROCESS);

    exchange.stepOne();

    assertEquals(exchange.bufferIndex, 0);
    assertEquals(exchange.bufferLimit, 0);
    assertEquals(exchange.responseHeaders.size(), 3);
    assertEquals(exchange.responseHeadersIndex, 0);
    assertNotNull(exchange.responseBytes);
    assertEquals(exchange.state, HttpExchange._RESPONSE_HEADER_BUFFER);

    exchange.stepOne();

    assertEquals(exchange.bufferIndex, 0);
    assertEquals(exchange.bufferLimit, 61);
    assertEquals(exchange.responseHeadersIndex, 2);
    assertEquals(exchange.state, HttpExchange._RESPONSE_HEADER_WRITE_PARTIAL);

    exchange.stepOne();

    assertEquals(exchange.bufferIndex, 0);
    assertEquals(exchange.bufferLimit, 0);
    assertEquals(exchange.responseHeadersIndex, 2);
    assertEquals(exchange.state, HttpExchange._RESPONSE_HEADER_BUFFER);

    exchange.stepOne();

    assertEquals(exchange.bufferIndex, 0);
    assertEquals(exchange.bufferLimit, 39);
    assertEquals(exchange.responseHeadersIndex, 3);
    assertEquals(exchange.state, HttpExchange._RESPONSE_HEADER_WRITE_FULL);

    /*
    exchange.stepOne();
    
    assertEquals(
      socket.outputAsString(),
    
      """
      HTTP/1.1 200 OK<CRLF>
      Content-Type: text/plain; charset=utf-8<CRLF>
      Content-Length: 13<CRLF>
      Date: Wed, 28 Jun 2023 12:08:43 GMT<CRLF>
      <CRLF>
      Hello World!
      """.replace("<CRLF>\n", "\r\n")
    );
    */
  }

  private void assertHeaderValue(HttpExchange exchange, HeaderName name, String expected) {
    Map<HeaderName, HeaderValue> headers;
    headers = exchange.requestHeaders;

    HeaderValue value;
    value = headers.get(name);

    assertNotNull(value, "Header " + name + " was not found");

    assertEquals(value.toString(), expected);
  }

}