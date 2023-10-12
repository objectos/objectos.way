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
package objectox.http;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import objectos.http.Http;
import org.testng.annotations.Test;

@SuppressWarnings("resource")
public class HttpExchangeRequestLineTest {

  @Test
  public void http001() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    Http001.INPUT.accept(exchange);

    while (exchange.state < HttpExchange._PARSE_HEADER) {
      exchange.stepOne();
    }

    // first line consumed
    assertEquals(exchange.bufferIndex, "GET / HTTP/1.1\r\n".length());
    assertEquals(exchange.bufferLimit, Http001.INPUT.requestLength());
    assertEquals(exchange.error, null);
    assertEquals(exchange.keepAlive, true);
    // expect correct method
    assertEquals(exchange.method, Http.Method.GET);
    assertEquals(exchange.requestHeaders, null);
    assertEquals(exchange.requestHeaderName, null);
    // expect correct parsed target
    assertEquals(exchange.requestPath.toString(), "/");
    assertEquals(exchange.responseBody, null);
    assertEquals(exchange.responseHeaders, null);
    assertEquals(exchange.responseHeadersIndex, -1);
    assertEquals(exchange.socket.isClosed(), false);
    assertEquals(exchange.state, HttpExchange._PARSE_HEADER);
    assertEquals(exchange.status, null);
    // expect correct parsed version major.minor
    assertEquals(exchange.versionMajor, 1);
    assertEquals(exchange.versionMinor, 1);
  }

  @Test
  public void http006() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    Http006.INPUT.accept(exchange);

    while (exchange.state < HttpExchange._PARSE_HEADER) {
      exchange.stepOne();
    }

    // first line consumed
    assertEquals(exchange.bufferIndex, "POST /login HTTP/1.1\r\n".length());
    assertEquals(exchange.bufferLimit, Http006.INPUT.requestLength());
    assertEquals(exchange.error, null);
    assertEquals(exchange.keepAlive, true);
    // expect correct method
    assertEquals(exchange.method, Http.Method.POST);
    assertEquals(exchange.requestHeaders, null);
    assertEquals(exchange.requestHeaderName, null);
    // expect correct parsed target
    assertEquals(exchange.requestPath.toString(), "/login");
    assertEquals(exchange.responseBody, null);
    assertEquals(exchange.responseHeaders, null);
    assertEquals(exchange.responseHeadersIndex, -1);
    assertEquals(exchange.socket.isClosed(), false);
    assertEquals(exchange.state, HttpExchange._PARSE_HEADER);
    assertEquals(exchange.status, null);
    // expect correct parsed version major.minor
    assertEquals(exchange.versionMajor, 1);
    assertEquals(exchange.versionMinor, 1);
  }

  @Test(description = """
  [#429] HTTP 001: REQUEST_LINE --> REQUEST_LINE_METHOD
  """)
  public void requestLine() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    record Pair(String request, Http.Method method) {}

    List<Pair> pairs = List.of(
      new Pair("CONNECT /", Http.Method.CONNECT),
      new Pair("DELETE /", Http.Method.DELETE),
      new Pair("HEAD /", Http.Method.HEAD),
      new Pair("GET /", Http.Method.GET),
      new Pair("OPTIONS /", Http.Method.OPTIONS),
      new Pair("TRACE /", Http.Method.TRACE)
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
  [#430] REQUEST_LINE --> REQUEST_ERROR::BAD_REQUEST

  - buffer should remain untouched
  """)
  public void requestLineToRequestErrorBadRequest() {
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
    assertEquals(exchange.state, HttpExchange._REQUEST_ERROR);
    assertEquals(exchange.status, HttpStatus.BAD_REQUEST);
  }

  @Test(description = """
  [#491] HTTP 006: REQUEST_LINE --> REQUEST_LINE_METHOD_P
  """)
  public void requestLineToRequestLineMethodP() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    List<String> requests;
    requests = List.of("PATCH /a", "POST /b", "PUT /c");

    for (var request : requests) {
      byte[] bytes;
      bytes = request.getBytes();

      exchange.buffer = bytes;
      exchange.bufferIndex = 0;
      exchange.bufferLimit = bytes.length;
      exchange.state = HttpExchange._REQUEST_LINE;

      exchange.stepOne();

      assertEquals(exchange.bufferIndex, 0);
      assertEquals(exchange.bufferLimit, bytes.length);
      assertEquals(exchange.state, HttpExchange._REQUEST_LINE_METHOD_P);
    }
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
    exchange.method = Http.Method.GET;
    exchange.state = HttpExchange._REQUEST_LINE_METHOD;

    exchange.stepOne();

    assertEquals(exchange.bufferIndex, 4);
    assertEquals(exchange.state, HttpExchange._REQUEST_LINE_TARGET);
  }

  @Test(description = """
  [#432] HTTP 001: REQUEST_LINE_METHOD --> REQUEST_ERROR::BAD_REQUEST

  - bufferIndex is NOT updated
  """)
  public void requestLineMethodToRequestErrorBadRequest() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    byte[] bytes;
    bytes = "GOT /".getBytes();

    exchange.buffer = bytes;
    exchange.bufferIndex = 0;
    exchange.bufferLimit = bytes.length;
    exchange.method = Http.Method.GET;
    exchange.state = HttpExchange._REQUEST_LINE_METHOD;

    exchange.stepOne();

    assertEquals(exchange.bufferIndex, 0);
    assertEquals(exchange.state, HttpExchange._REQUEST_ERROR);
    assertEquals(exchange.status, HttpStatus.BAD_REQUEST);
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
    exchange.method = Http.Method.GET;
    exchange.state = HttpExchange._REQUEST_LINE_METHOD;

    exchange.stepOne();

    assertEquals(exchange.bufferIndex, 0);
    assertEquals(exchange.nextAction, HttpExchange._REQUEST_LINE_METHOD);
    assertEquals(exchange.state, HttpExchange._INPUT_READ);
  }

  @Test(description = """
  [#491] HTTP 006: REQUEST_LINE_METHOD_P --> REQUEST_LINE_TARGET
  """)
  public void requestLineMethodP() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    record Pair(String request, Http.Method method) {}

    List<Pair> pairs = List.of(
      new Pair("PATCH /", Http.Method.PATCH),
      new Pair("POST /", Http.Method.POST),
      new Pair("PUT /", Http.Method.PUT)
    );

    for (var pair : pairs) {
      String request;
      request = pair.request;

      byte[] bytes;
      bytes = request.getBytes();

      exchange.buffer = bytes;
      exchange.bufferIndex = 0;
      exchange.bufferLimit = bytes.length;
      exchange.state = HttpExchange._REQUEST_LINE_METHOD_P;

      exchange.stepOne();

      assertEquals(exchange.bufferIndex, 0);
      assertEquals(exchange.bufferLimit, bytes.length);
      assertEquals(exchange.method, pair.method);
      assertEquals(exchange.state, HttpExchange._REQUEST_LINE_METHOD);
    }
  }

  @Test(description = """
  [#491] HTTP 006: REQUEST_LINE_METHOD_P --> INPUT_READ
  """)
  public void requestLineMethodPToInputRead() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    byte[] bytes;
    bytes = "P".getBytes();

    exchange.buffer = bytes;
    exchange.bufferIndex = 0;
    exchange.bufferLimit = bytes.length;
    exchange.state = HttpExchange._REQUEST_LINE_METHOD_P;

    exchange.stepOne();

    assertEquals(exchange.bufferIndex, 0);
    assertEquals(exchange.nextAction, HttpExchange._REQUEST_LINE_METHOD_P);
    assertEquals(exchange.state, HttpExchange._INPUT_READ);
  }

  @Test(description = """
  [#516] HTTP 001: REQUEST_LINE_TARGET --> REQUEST_LINE_PATH
  """)
  public void requestLineTarget() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    byte[] bytes;
    bytes = "GET / HTTP/1.1".getBytes();

    // buffer is full
    exchange.buffer = bytes;
    exchange.bufferIndex = 4;
    exchange.bufferLimit = bytes.length;
    exchange.state = HttpExchange._REQUEST_LINE_TARGET;

    exchange.stepOne();

    assertEquals(exchange.bufferIndex, 5);
    assertNotNull(exchange.requestPath);
    assertEquals(exchange.requestPath.slash[0], 4);
    assertEquals(exchange.state, HttpExchange._REQUEST_LINE_PATH);
  }

  @Test(description = """
  [#516] HTTP 001: REQUEST_LINE_TARGET --> INPUT_READ
  """)
  public void requestLineTargetToInputRead() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    byte[] bytes;
    bytes = "GET ".getBytes();

    // buffer is NOT full
    exchange.buffer = Arrays.copyOf(bytes, bytes.length + 1);
    exchange.bufferIndex = 4;
    exchange.bufferLimit = bytes.length;
    exchange.state = HttpExchange._REQUEST_LINE_TARGET;

    exchange.stepOne();

    assertEquals(exchange.bufferIndex, 4);
    assertNull(exchange.requestPath);
    assertEquals(exchange.state, HttpExchange._INPUT_READ);
  }

  @Test(description = """
  [#516] HTTP 001: REQUEST_LINE_TARGET --> REQUEST_ERROR::BAD_REQUEST
  """)
  public void requestLineTargetToRequestErrorBadRequest() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    byte[] bytes;
    bytes = "GET foo/bar HTTP/1.1".getBytes();

    // buffer is full
    exchange.buffer = bytes;
    exchange.bufferIndex = 4;
    exchange.bufferLimit = bytes.length;
    exchange.state = HttpExchange._REQUEST_LINE_TARGET;

    exchange.stepOne();

    assertEquals(exchange.bufferIndex, 4);
    assertNull(exchange.requestPath);
    assertEquals(exchange.state, HttpExchange._REQUEST_ERROR);
    assertEquals(exchange.status, HttpStatus.BAD_REQUEST);
  }

  @Test(description = """
  [#434] HTTP 001: REQUEST_LINE_PATH --> REQUEST_LINE_VERSION

  - bufferIndex should be after SP
  - requestTarget should contain the correct indices
  - segments should have been created correctly
  """)
  public void requestLinePath() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    record Data(String requestLine, String path, List<String> segments) {}

    List<Data> dataList = List.of(
      new Data("GET / HTTP/1.1", "/", List.of("")),
      new Data("GET /index.html HTTP/1.1", "/index.html", List.of("index.html")),
      new Data("GET /foo/bar HTTP/1.1", "/foo/bar", List.of("foo", "bar")),
      new Data("GET //bar HTTP/1.1", "//bar", List.of("", "bar")),
      new Data("GET /foo/ HTTP/1.1", "/foo/", List.of("foo", "")),
      new Data("GET /a/b/c HTTP/1.1", "/a/b/c", List.of("a", "b", "c")),
      new Data("GET /a/b/c/d/e/f HTTP/1.1", "/a/b/c/d/e/f",
        List.of("a", "b", "c", "d", "e", "f"))
    );

    for (var data : dataList) {
      String requestLine;
      requestLine = data.requestLine;

      byte[] bytes;
      bytes = requestLine.getBytes();

      HttpRequestPath path;
      path = new HttpRequestPath(bytes);

      exchange.buffer = bytes;
      exchange.bufferIndex = 5;
      exchange.bufferLimit = bytes.length;
      exchange.requestPath = path;
      path.slash(4);
      exchange.state = HttpExchange._REQUEST_LINE_PATH;

      exchange.stepOne();

      assertEquals(exchange.bufferIndex, 5 + data.path.length());

      assertEquals(path.toString(), data.path);

      List<String> segments;
      segments = new ArrayList<>(path.segmentCount());

      for (int i = 0; i < path.segmentCount(); i++) {
        String s;
        s = path.segment(i);

        segments.add(s);
      }

      assertEquals(segments, data.segments);
      assertEquals(exchange.state, HttpExchange._REQUEST_LINE_VERSION);
    }
  }

  @Test(description = """
  [#435] HTTP 001: REQUEST_LINE_PATH --> INPUT_READ

  - bufferIndex should not have been updated
  """)
  public void requestLinePathToInputRead() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    byte[] bytes;
    bytes = "GET /".getBytes();

    // buffer is not full
    exchange.buffer = Arrays.copyOf(bytes, bytes.length + 1);
    exchange.bufferIndex = 5;
    exchange.bufferLimit = bytes.length;
    exchange.state = HttpExchange._REQUEST_LINE_TARGET;

    exchange.stepOne();

    assertEquals(exchange.bufferIndex, 5);
    assertNull(exchange.requestPath);
    assertEquals(exchange.state, HttpExchange._INPUT_READ);
  }

  @Test(description = """
  [#436] HTTP 001: REQUEST_LINE_PATH --> REQUEST_ERROR::URI_TOO_LONG
  """)
  public void requestLinePathToRequestErrorUriTooLong() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    byte[] bytes;
    bytes = "GET /attack!".getBytes();

    // buffer is full
    exchange.buffer = bytes;
    exchange.bufferIndex = 5;
    exchange.bufferLimit = bytes.length;
    exchange.state = HttpExchange._REQUEST_LINE_PATH;

    exchange.stepOne();

    assertEquals(exchange.bufferIndex, 12);
    assertNull(exchange.requestPath);
    assertEquals(exchange.state, HttpExchange._REQUEST_ERROR);
    assertEquals(exchange.status, HttpStatus.URI_TOO_LONG);
  }

  @Test(description = """
  [#437] HTTP 001: REQUEST_LINE_VERSION --> PARSE_HEADER

  - bufferIndex is after CRLF
  - version has correct values
  """)
  public void requestLineVersion01() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    // CRLF line terminator

    byte[] bytes;
    bytes = "GET / HTTP/1.1\r\nHost:".getBytes();

    exchange.buffer = bytes;
    exchange.bufferIndex = 6;
    exchange.bufferLimit = bytes.length;
    exchange.state = HttpExchange._REQUEST_LINE_VERSION;

    exchange.stepOne();

    assertEquals(exchange.bufferIndex, 16);
    assertEquals(exchange.state, HttpExchange._PARSE_HEADER);
    assertEquals(exchange.versionMajor, 1);
    assertEquals(exchange.versionMinor, 1);
  }

  @Test(description = """
  [#437] HTTP 001: REQUEST_LINE_VERSION --> PARSE_HEADER

  - client line terminator is LF only
  """)
  public void requestLineVersion02LF() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    // LF line terminator

    byte[] bytes;
    bytes = "GET / HTTP/1.1\nHost:".getBytes();

    exchange.buffer = bytes;
    exchange.bufferIndex = 6;
    exchange.bufferLimit = bytes.length;
    exchange.state = HttpExchange._REQUEST_LINE_VERSION;

    exchange.stepOne();

    assertEquals(exchange.bufferIndex, 15);
    assertEquals(exchange.state, HttpExchange._PARSE_HEADER);
    assertEquals(exchange.versionMajor, 1);
    assertEquals(exchange.versionMinor, 1);
  }

  @Test(description = """
  [#438] HTTP 001: REQUEST_LINE_VERSION --> REQUEST_ERROR::BAD_REQUEST
  """)
  public void requestLineVersionToRequestErrorBadRequest() {
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
      exchange.state = HttpExchange._REQUEST_LINE_VERSION;
      exchange.status = null;

      exchange.stepOne();

      assertEquals(exchange.bufferIndex, 6);
      assertEquals(exchange.state, HttpExchange._REQUEST_ERROR);
      assertEquals(exchange.status, HttpStatus.BAD_REQUEST);
      assertEquals(exchange.versionMajor, 0);
      assertEquals(exchange.versionMinor, 0);
    }
  }

  @Test(description = """
  [#439] HTTP 001: REQUEST_LINE_VERSION --> INPUT_READ
  """)
  public void requestLineVersionToInputRead() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    byte[] bytes;
    bytes = "GET / HTTP/1.".getBytes();

    exchange.buffer = Arrays.copyOf(bytes, bytes.length + 1);
    exchange.bufferIndex = 6;
    exchange.bufferLimit = bytes.length;
    exchange.state = HttpExchange._REQUEST_LINE_VERSION;

    exchange.stepOne();

    assertEquals(exchange.bufferIndex, 6);
    assertEquals(exchange.nextAction, HttpExchange._REQUEST_LINE_VERSION);
    assertEquals(exchange.state, HttpExchange._INPUT_READ);
  }

  @Test(description = """
  [#440] HTTP 001: REQUEST_LINE_VERSION --> REQUEST_ERROR::URI_TOO_LONG
  """)
  public void requestLineVersionToRequestErrorUriTooLong() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    byte[] bytes;
    bytes = "GET / HTTP/1.".getBytes();

    exchange.buffer = bytes;
    exchange.bufferIndex = 6;
    exchange.bufferLimit = bytes.length;
    exchange.state = HttpExchange._REQUEST_LINE_VERSION;

    exchange.stepOne();

    assertEquals(exchange.bufferIndex, 6);
    assertEquals(exchange.state, HttpExchange._REQUEST_ERROR);
    assertEquals(exchange.status, HttpStatus.URI_TOO_LONG);
  }

}