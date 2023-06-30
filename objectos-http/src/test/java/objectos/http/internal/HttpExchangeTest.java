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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import objectos.http.HttpProcessor;
import objectos.http.Request;
import objectos.http.Response;
import objectos.http.Status;
import objectos.http.internal.HttpExchange.HeaderValue;
import objectos.http.internal.HttpExchange.RequestTarget;
import objectos.lang.NoOpNoteSink;
import objectos.lang.NoteSink;
import org.testng.annotations.Test;

public class HttpExchangeTest {

  private static final NoteSink NOOP_NOTE_SINK = NoOpNoteSink.getInstance();

  @Test(description = """
  [#437] HTTP 001: PARSE_VERSION --> PARSE_HEADER

  - bufferIndex is after CRLF
  - version has correct values
  """)
  public void executeParseVersion01() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    byte[] bytes;
    bytes = "GET / HTTP/1.1\r\n".getBytes();

    exchange.buffer = bytes;
    exchange.bufferIndex = 6;
    exchange.bufferLimit = bytes.length;
    exchange.state = HttpExchange._PARSE_VERSION;

    exchange.stepOne();

    assertEquals(exchange.bufferIndex, bytes.length);
    assertEquals(exchange.state, HttpExchange._PARSE_HEADER);
    assertEquals(exchange.versionMajor, 1);
    assertEquals(exchange.versionMinor, 1);
  }

  @Test(description = """
  [#438] HTTP 001: PARSE_VERSION --> BAD_REQUEST
  """)
  public void executeParseVersion02BadRequest() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    List<String> requests;
    requests = List.of(
      "GET / Http/1.1\r\n", // does not start with HTTP/
      "GET / HTTP-1.1\r\n", // does not start with HTTP/
      "GET / HTTP-x.1\r\n", // major is not a digit
      "GET / HTTP-1.y\r\n", // minor is not a digit
      "GET / HTTP-1-1\r\n", // no dot
      "GET / HTTP-1-1\n\n" // does not end with CRLF
    );

    for (var request : requests) {
      byte[] bytes;
      bytes = request.getBytes();

      exchange.buffer = bytes;
      exchange.bufferIndex = 6;
      exchange.bufferLimit = bytes.length;
      exchange.state = HttpExchange._PARSE_VERSION;
      exchange.status = null;

      exchange.stepOne();

      assertEquals(exchange.bufferIndex, 6);
      assertEquals(exchange.state, HttpExchange._CLIENT_ERROR);
      assertEquals(exchange.status, Status.BAD_REQUEST);
      assertEquals(exchange.versionMajor, 0);
      assertEquals(exchange.versionMinor, 0);
    }
  }

  @Test(description = """
  [#439] HTTP 001: PARSE_VERSION --> IO_READ
  """)
  public void executeParseVersion03IoRead() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    byte[] bytes;
    bytes = "GET / HTTP/1.".getBytes();

    exchange.buffer = Arrays.copyOf(bytes, bytes.length + 1);
    exchange.bufferIndex = 6;
    exchange.bufferLimit = bytes.length;
    exchange.state = HttpExchange._PARSE_VERSION;

    exchange.stepOne();

    assertEquals(exchange.bufferIndex, 6);
    assertEquals(exchange.nextAction, HttpExchange._PARSE_VERSION);
    assertEquals(exchange.state, HttpExchange._INPUT_READ);
  }

  @Test(description = """
  [#440] HTTP 001: PARSE_VERSION --> URI_TOO_LONG
  """)
  public void executeParseVersion04UriTooLong() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    byte[] bytes;
    bytes = "GET / HTTP/1.".getBytes();

    exchange.buffer = bytes;
    exchange.bufferIndex = 6;
    exchange.bufferLimit = bytes.length;
    exchange.state = HttpExchange._PARSE_VERSION;

    exchange.stepOne();

    assertEquals(exchange.bufferIndex, 6);
    assertEquals(exchange.state, HttpExchange._CLIENT_ERROR);
    assertEquals(exchange.status, Status.URI_TOO_LONG);
  }

  @Test(enabled = false, description = """
  HttpExchange TC0001

  - GET / 1.1
  - Host
  """)
  public void testCase0001() throws Throwable {
    TestableSocket socket;
    socket = TestableSocket.parse("""
    GET / HTTP/1.1
    Host: localhost:7001

    """.replace("\n", "\r\n"));

    HttpProcessor processor;
    processor = new HttpProcessor() {
      static final ZonedDateTime DATE = ZonedDateTime.of(
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

        response.date(DATE);

        response.send(bytes);
      }
    };

    HttpExchange exchange;
    exchange = new HttpExchange(64, NOOP_NOTE_SINK, processor, socket);

    assertEquals(socket.isClosed(), false);
    assertEquals(exchange.state, HttpExchange._SETUP);

    exchange.stepOne();

    assertEquals(exchange.state, HttpExchange._INPUT_READ);
    assertEquals(exchange.nextAction, HttpExchange._PARSE_METHOD);

    exchange.stepOne();

    assertEquals(exchange.bufferIndex, 0);
    assertEquals(exchange.bufferLimit, 40);
    assertEquals(exchange.state, HttpExchange._PARSE_METHOD);

    exchange.stepOne();

    // 'G' 'E' 'T' SP = 4
    assertEquals(exchange.bufferIndex, 4);
    assertEquals(exchange.method, Method.GET);
    assertEquals(exchange.state, HttpExchange._PARSE_REQUEST_TARGET);

    exchange.stepOne();

    // '/' SP = 2
    assertEquals(exchange.bufferIndex, 6);
    assertEquals(exchange.requestTarget, new RequestTarget(4, 5));
    assertEquals(exchange.state, HttpExchange._PARSE_VERSION);

    exchange.stepOne();

    // 'H' 'T' 'T' 'P' '/' '1' '.' '1' CR LF = 10
    assertEquals(exchange.bufferIndex, 16);
    assertEquals(exchange.state, HttpExchange._PARSE_HEADER);

    exchange.stepOne();

    assertEquals(exchange.bufferIndex, 16);
    assertEquals(exchange.state, HttpExchange._PARSE_HEADER_NAME);

    exchange.stepOne();

    // 'H' 'o' 's' 't' ':' = 5
    assertEquals(exchange.bufferIndex, 21);
    assertEquals(exchange.requestHeaderName, HeaderName.HOST);
    assertEquals(exchange.state, HttpExchange._PARSE_HEADER_VALUE);

    exchange.stepOne();

    // SP 'l' 'o' 'c' 'a' 'l' 'h' 'o' 's' 't' = 10
    // ':' '7' '0' '0' '1' CR LF = 7
    assertEquals(exchange.bufferIndex, 38);
    assertHeaderValue(exchange, HeaderName.HOST, "localhost:7001");
    assertEquals(exchange.state, HttpExchange._PARSE_HEADER);

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