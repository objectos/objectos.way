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
import static org.testng.Assert.assertSame;

import java.io.IOException;
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
import objectos.lang.Note1;
import objectos.lang.NoteSink;
import org.testng.annotations.Test;

public class HttpExchangeTest {

  private static final NoteSink NOOP_NOTE_SINK = NoOpNoteSink.getInstance();

  private static final HttpProcessor NOOP_PROCESSOR = new HttpProcessor() {
    @Override
    public void process(Request request, Response response) throws Exception {
      // no-op
    }
  };

  @Test(description = """
  [#427] HTTP 001: IO_READ --> PARSE_METHOD
  """)
  public void executeIoRead01() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    exchange.buffer = new byte[64];
    exchange.bufferIndex = 0;
    exchange.bufferLimit = 0;
    exchange.nextAction = HttpExchange._PARSE_METHOD;
    exchange.socket = socket("FOO\r\n");
    exchange.state = HttpExchange._IO_READ;

    exchange.stepOne();

    assertEquals(exchange.bufferIndex, 0);
    assertEquals(exchange.bufferLimit, 5);
    assertEquals(exchange.state, HttpExchange._PARSE_METHOD);
  }

  @Test(description = """
  [#428] HTTP 001: IO_READ --> IO_EXCEPTION

  - Socket::getInputStream throws
  """)
  public void executeIoRead02() {
    TestableSocket socket;
    socket = socket("FOO");

    IOException error;
    error = new IOException();

    socket.getInputStream = error;

    var noteSink = new TestableNoteSink() {
      Note1<?> note1;
      Object value1;

      @Override
      public <T1> void send(Note1<T1> note, T1 v1) {
        note1 = note;
        value1 = v1;
      }
    };

    HttpExchange exchange;
    exchange = new HttpExchange(64, noteSink, NOOP_PROCESSOR, socket);

    exchange.bufferIndex = 0;
    exchange.bufferLimit = 0;
    exchange.nextAction = HttpExchange._PARSE_METHOD;
    exchange.state = HttpExchange._IO_READ;

    exchange.stepOne();

    assertEquals(exchange.bufferIndex, 0);
    assertEquals(exchange.bufferLimit, 0);
    assertSame(exchange.error, error);
    assertEquals(exchange.state, HttpExchange._CLOSE);

    assertSame(noteSink.note1, HttpExchange.EIO_READ_ERROR);
    assertSame(noteSink.value1, error);
  }

  @Test(description = """
  [#428] HTTP 001: IO_READ --> IO_EXCEPTION

  - InputStream::read throws
  """)
  public void executeIoRead03() {
    TestableSocket socket;
    socket = socket("FOO");

    IOException error;
    error = new IOException();

    socket.inputStreamRead = error;

    var noteSink = new TestableNoteSink() {
      Note1<?> note1;
      Object value1;

      @Override
      public <T1> void send(Note1<T1> note, T1 v1) {
        note1 = note;
        value1 = v1;
      }
    };

    HttpExchange exchange;
    exchange = new HttpExchange(64, noteSink, NOOP_PROCESSOR, socket);

    exchange.bufferIndex = 0;
    exchange.bufferLimit = 0;
    exchange.nextAction = HttpExchange._PARSE_METHOD;
    exchange.state = HttpExchange._IO_READ;

    exchange.stepOne();

    assertEquals(exchange.bufferIndex, 0);
    assertEquals(exchange.bufferLimit, 0);
    assertSame(exchange.error, error);
    assertEquals(exchange.state, HttpExchange._CLOSE);

    assertSame(noteSink.note1, HttpExchange.EIO_READ_ERROR);
    assertSame(noteSink.value1, error);
  }

  @Test(description = """
  [#429] HTTP 001: PARSE_METHOD -> PARSE_METHOD_CANDIDATE
  """)
  public void executeParseMethod01() {
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
      exchange.state = HttpExchange._PARSE_METHOD;

      exchange.stepOne();

      assertEquals(exchange.bufferIndex, 0);
      assertEquals(exchange.bufferLimit, bytes.length);
      assertEquals(exchange.method, pair.method);
      assertEquals(exchange.state, HttpExchange._PARSE_METHOD_CANDIDATE);
    }
  }

  @Test(description = """
  [#430] HTTP 001: PARSE_METHOD -> BAD_REQUEST
  """)
  public void executeParseMethod02() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    byte[] bytes;
    bytes = "(GET) /".getBytes();

    exchange.buffer = bytes;
    exchange.bufferIndex = 0;
    exchange.bufferLimit = bytes.length;
    exchange.state = HttpExchange._PARSE_METHOD;

    exchange.stepOne();

    assertEquals(exchange.bufferIndex, 0);
    assertEquals(exchange.state, HttpExchange._CLIENT_ERROR);
    assertEquals(exchange.status, Status.BAD_REQUEST);
  }

  @Test(description = """
  [#431] HTTP 001: PARSE_METHOD_CANDIDATE --> PARSE_REQUEST_TARGET

  - check if bufferIndex is updated
  """)
  public void executeParseMethodCandidate01() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    byte[] bytes;
    bytes = "GET /".getBytes();

    exchange.buffer = bytes;
    exchange.bufferIndex = 0;
    exchange.bufferLimit = bytes.length;
    exchange.method = Method.GET;
    exchange.state = HttpExchange._PARSE_METHOD_CANDIDATE;

    exchange.stepOne();

    assertEquals(exchange.bufferIndex, 4);
    assertEquals(exchange.state, HttpExchange._PARSE_REQUEST_TARGET);
  }

  @Test(description = """
  [#432] HTTP 001: PARSE_METHOD_CANDIDATE --> BAD_REQUEST

  - bufferIndex is NOT updated
  """)
  public void executeParseMethodCandidate02BadRequest() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    byte[] bytes;
    bytes = "GOT /".getBytes();

    exchange.buffer = bytes;
    exchange.bufferIndex = 0;
    exchange.bufferLimit = bytes.length;
    exchange.method = Method.GET;
    exchange.state = HttpExchange._PARSE_METHOD_CANDIDATE;

    exchange.stepOne();

    assertEquals(exchange.bufferIndex, 0);
    assertEquals(exchange.state, HttpExchange._CLIENT_ERROR);
    assertEquals(exchange.status, Status.BAD_REQUEST);
  }

  @Test(description = """
  [#433] HTTP 001: PARSE_METHOD_CANDIDATE --> IO_READ
  """)
  public void executeParseMethodCandidate03IoRead() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    byte[] bytes;
    bytes = "GE".getBytes();

    exchange.buffer = bytes;
    exchange.bufferIndex = 0;
    exchange.bufferLimit = bytes.length;
    exchange.method = Method.GET;
    exchange.state = HttpExchange._PARSE_METHOD_CANDIDATE;

    exchange.stepOne();

    assertEquals(exchange.bufferIndex, 0);
    assertEquals(exchange.nextAction, HttpExchange._PARSE_METHOD_CANDIDATE);
    assertEquals(exchange.state, HttpExchange._IO_READ);
  }

  @Test(description = """
  [#434] HTTP 001: PARSE_REQUEST_TARGET --> PARSE_VERSION

  - bufferIndex should be after SP
  - requestTarget should contain the correct indices
  """)
  public void executeParseRequestTarget01() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    byte[] bytes;
    bytes = "GET / HTTP/1.1".getBytes();

    exchange.buffer = bytes;
    exchange.bufferIndex = 4;
    exchange.bufferLimit = bytes.length;
    exchange.state = HttpExchange._PARSE_REQUEST_TARGET;

    exchange.stepOne();

    assertEquals(exchange.bufferIndex, 6);
    assertNotNull(exchange.requestTarget);
    assertEquals(exchange.requestTarget.start(), 4);
    assertEquals(exchange.requestTarget.end(), 5);
    assertEquals(exchange.state, HttpExchange._PARSE_VERSION);
  }

  @Test(description = """
  [#435] HTTP 001: PARSE_REQUEST_TARGET --> IO_READ

  - bufferIndex should not have been updated
  """)
  public void executeParseRequestTarget02IoRead() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    byte[] bytes;
    bytes = "GET /".getBytes();

    // buffer is not full
    exchange.buffer = Arrays.copyOf(bytes, bytes.length + 1);
    exchange.bufferIndex = 4;
    exchange.bufferLimit = bytes.length;
    exchange.state = HttpExchange._PARSE_REQUEST_TARGET;

    exchange.stepOne();

    assertEquals(exchange.bufferIndex, 4);
    assertNull(exchange.requestTarget);
    assertEquals(exchange.state, HttpExchange._IO_READ);
  }

  @Test(description = """
  [#436] HTTP 001: PARSE_REQUEST_TARGET --> URI_TOO_LONG
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
    exchange.state = HttpExchange._PARSE_REQUEST_TARGET;

    exchange.stepOne();

    assertEquals(exchange.bufferIndex, 4);
    assertNull(exchange.requestTarget);
    assertEquals(exchange.state, HttpExchange._CLIENT_ERROR);
    assertEquals(exchange.status, Status.URI_TOO_LONG);
  }

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
  [#438] HTTP 001: PARSE_VERSION --> IO_READ
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
    assertEquals(exchange.state, HttpExchange._IO_READ);
  }

  @Test(description = """
  [#426] HTTP 001: START --> IO_READ

  - buffer must be reset
  - next action -> PARSE_METHOD
  """)
  public void executeStart01() {
    TestableSocket socket;
    socket = socket("FOO");

    HttpExchange exchange;
    exchange = new HttpExchange(64, NOOP_NOTE_SINK, NOOP_PROCESSOR, socket);

    assertEquals(exchange.state, HttpExchange._START);

    exchange.stepOne();

    assertEquals(exchange.bufferIndex, 0);
    assertEquals(exchange.bufferLimit, 0);
    assertEquals(exchange.nextAction, HttpExchange._PARSE_METHOD);
    assertEquals(exchange.state, HttpExchange._IO_READ);
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
    assertEquals(exchange.state, HttpExchange._START);

    exchange.stepOne();

    assertEquals(exchange.state, HttpExchange._IO_READ);
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

  private TestableSocket socket(String s) {
    return TestableSocket.parse(s);
  }

}