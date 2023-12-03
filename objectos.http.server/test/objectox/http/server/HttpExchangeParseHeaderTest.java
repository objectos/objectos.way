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

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import objectos.http.Http;
import objectox.http.HeaderType;
import objectox.http.HttpStatus;
import objectox.http.StandardHeaderName;
import org.testng.annotations.Test;

@SuppressWarnings("resource")
public class HttpExchangeParseHeaderTest {

  @Test
  public void http001() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    Http001.INPUT.accept(exchange);

    while (exchange.state < HttpExchange._REQUEST_BODY) {
      exchange.stepOne();
    }

    // buffer should have been exhausted
    assertEquals(exchange.bufferIndex, Http001.INPUT.requestLength());
    assertEquals(exchange.bufferLimit, Http001.INPUT.requestLength());
    assertEquals(exchange.error, null);
    assertEquals(exchange.keepAlive, true);
    assertEquals(exchange.method, Http.Method.GET);
    assertEquals(exchange.requestBody, null);
    // request headers parsed
    assertEquals(exchange.requestHeaders, Map.of(
        StandardHeaderName.HOST, TestingInput.hv("www.example.com"),
        StandardHeaderName.CONNECTION, TestingInput.hv("close")
    ));
    assertEquals(exchange.requestHeaderName, null);
    assertEquals(exchange.requestPath.toString(), "/");
    assertEquals(exchange.responseBody, null);
    assertEquals(exchange.responseHeaders, null);
    assertEquals(exchange.responseHeadersIndex, -1);
    assertEquals(exchange.socket.isClosed(), false);
    assertEquals(exchange.state, HttpExchange._HANDLE);
    assertEquals(exchange.status, null);
    assertEquals(exchange.versionMajor, 1);
    assertEquals(exchange.versionMinor, 1);
  }

  @Test
  public void http006() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    Http006.INPUT.accept(exchange);

    while (exchange.state < HttpExchange._REQUEST_BODY) {
      exchange.stepOne();
    }

    // buffer should have been consumed up to the payload
    assertEquals(exchange.bufferIndex, Http006.INPUT.requestLength() - 24);
    assertEquals(exchange.bufferLimit, Http006.INPUT.requestLength());
    assertEquals(exchange.error, null);
    assertEquals(exchange.keepAlive, true);
    assertEquals(exchange.method, Http.Method.POST);
    assertEquals(exchange.requestBody, null);
    // request headers parsed
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
    assertEquals(exchange.state, HttpExchange._REQUEST_BODY);
    assertEquals(exchange.status, null);
    assertEquals(exchange.versionMajor, 1);
    assertEquals(exchange.versionMinor, 1);
  }

  // PARSE_HEADER

  @Test(description = """
  [#445] HTTP 001: PARSE_HEADER --> PARSE_HEADER_NAME

  - resets header name
  """)
  public void parseHeader() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    byte[] bytes;
    bytes = Bytes.utf8("FOO");

    exchange.buffer = bytes;
    exchange.bufferIndex = 0;
    exchange.bufferLimit = bytes.length;
    exchange.state = HttpExchange._PARSE_HEADER;

    exchange.stepOne();

    assertEquals(exchange.bufferIndex, 0);
    assertEquals(exchange.bufferLimit, bytes.length);
    assertEquals(exchange.state, HttpExchange._PARSE_HEADER_NAME);
  }

  @Test(description = """
  [#446] HTTP 001: PARSE_HEADER --> INPUT_READ
  """)
  public void parseHeaderToInputRead() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    // input resembles an empty line
    // client is very slow...

    byte[] bytes;
    bytes = Bytes.utf8("\r");

    exchange.buffer = Arrays.copyOf(bytes, 20); /* buffer is not full */
    exchange.bufferIndex = 0;
    exchange.bufferLimit = bytes.length; /* buffer is not full */
    exchange.state = HttpExchange._PARSE_HEADER;

    exchange.stepOne();

    assertEquals(exchange.bufferIndex, 0);
    assertEquals(exchange.bufferLimit, bytes.length);
    assertEquals(exchange.nextAction, HttpExchange._PARSE_HEADER);
    assertEquals(exchange.state, HttpExchange._INPUT_READ);
  }

  @Test(description = """
  [#446] HTTP 001: PARSE_HEADER --> REQUEST_ERROR::BAD_REQUEST
  """)
  public void parseHeaderToRequestErrorBadRequest() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    byte[] bytes;
    bytes = Bytes.utf8("\r");

    exchange.buffer = bytes; /* buffer is full */
    exchange.bufferIndex = 0;
    exchange.bufferLimit = 1; /* buffer is full */
    exchange.state = HttpExchange._PARSE_HEADER;

    exchange.stepOne();

    assertEquals(exchange.bufferIndex, 0);
    assertEquals(exchange.bufferLimit, 1);
    assertEquals(exchange.state, HttpExchange._REQUEST_ERROR);
    assertEquals(exchange.status, HttpStatus.BAD_REQUEST);
  }

  @Test(description = """
  [#444] HTTP 001: PARSE_HEADER --> HANDLE
  """)
  public void parseHeaderToHandle() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    record Test(String request, int bufferIndex) {}

    List<Test> tests = List.of(
        new Test("\r\nbody", 2),
        new Test("\nbody", 1)
    );

    for (var test : tests) {
      byte[] bytes;
      bytes = Bytes.utf8(test.request);

      exchange.buffer = bytes;
      exchange.bufferIndex = 0;
      exchange.bufferLimit = bytes.length;
      exchange.state = HttpExchange._PARSE_HEADER;

      exchange.stepOne();

      assertEquals(exchange.bufferIndex, test.bufferIndex);
      assertEquals(exchange.bufferLimit, bytes.length);
      assertEquals(exchange.state, HttpExchange._HANDLE);
    }
  }

  // PARSE_HEADER_NAME

  @Test(description = """
  [#444] HTTP 001: PARSE_HEADER_NAME --> PARSE_HEADER_VALUE

  - bufferIndex ends after colon
  """)
  public void parseHeaderName() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    StringBuilder sb;
    sb = new StringBuilder();

    for (var headerName : StandardHeaderName.values()) {
      if (headerName.type == HeaderType.RESPONSE) {
        continue;
      }

      sb.setLength(0);

      sb.append(headerName.name);
      sb.append(':');
      sb.append(" some value\r\n");

      byte[] bytes;
      bytes = Bytes.utf8(sb.toString());

      exchange.buffer = bytes;
      exchange.bufferIndex = 0;
      exchange.bufferLimit = bytes.length;
      exchange.state = HttpExchange._PARSE_HEADER_NAME;

      exchange.stepOne();

      byte[] nameBytes;
      nameBytes = headerName.name.getBytes(StandardCharsets.UTF_8);

      assertEquals(exchange.bufferIndex, nameBytes.length + 1, headerName.toString());
      assertEquals(exchange.requestHeaderName, headerName);
      assertEquals(exchange.state, HttpExchange._PARSE_HEADER_VALUE);
    }
  }

  @Test(description = """
  [#444] HTTP 001: PARSE_HEADER_NAME --> INPUT_READ

  - buffer index remains unchanged
  - not header name set
  """)
  public void parseHeaderNameToInputRead() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    byte[] bytes;
    bytes = Bytes.utf8("Connectio");

    exchange.buffer = Arrays.copyOf(bytes, 20); /* buffer is not full */
    exchange.bufferIndex = 0;
    exchange.bufferLimit = bytes.length;
    exchange.requestHeaderName = StandardHeaderName.ACCEPT_ENCODING;
    exchange.state = HttpExchange._PARSE_HEADER_NAME;

    exchange.stepOne();

    assertEquals(exchange.bufferIndex, 0);
    assertEquals(exchange.nextAction, HttpExchange._PARSE_HEADER_NAME);
    assertEquals(exchange.requestHeaderName, null);
    assertEquals(exchange.state, HttpExchange._INPUT_READ);
  }

  @Test(description = """
  [#444] HTTP 001: PARSE_HEADER_NAME --> REQUEST_ERROR::BAD_REQUEST

  - buffer index remains unchanged
  - not header name set
  """)
  public void parseHeaderNameToRequestErrorBadRequest() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    byte[] bytes;
    bytes = Bytes.utf8("Connectio");

    exchange.buffer = bytes; /* buffer is full */
    exchange.bufferIndex = 0;
    exchange.bufferLimit = bytes.length;
    exchange.requestHeaderName = StandardHeaderName.ACCEPT_ENCODING;
    exchange.state = HttpExchange._PARSE_HEADER_NAME;

    exchange.stepOne();

    assertEquals(exchange.bufferIndex, 0);
    assertEquals(exchange.requestHeaderName, null);
    assertEquals(exchange.state, HttpExchange._REQUEST_ERROR);
    assertEquals(exchange.status, HttpStatus.BAD_REQUEST);
  }

  @Test(description = """
  [#452] HTTP 001: PARSE_HEADER_NAME --> PARSE_HEADER_VALUE (not found)

  - header name is unknown
  """)
  public void parseHeaderNameToParseHeaderValue() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    byte[] bytes;
    bytes = Bytes.utf8("Foo: Bar\r\n");

    exchange.buffer = bytes;
    exchange.bufferIndex = 0;
    exchange.bufferLimit = bytes.length;
    exchange.state = HttpExchange._PARSE_HEADER_NAME;

    exchange.stepOne();

    assertEquals(exchange.bufferIndex, 4);
    assertEquals(exchange.requestHeaderName, "Foo");
    assertEquals(exchange.state, HttpExchange._PARSE_HEADER_VALUE);
  }

  // PARSE_HEADER_VALUE

  @Test(description = """
  [#444] HTTP 001: PARSE_HEADER_VALUE --> PARSE_HEADER

  - happy path
  """)
  public void parseHeaderValue() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    List<String> requests = List.of(
        "Host: foobar\r\n",
        "Host: foobar\n",
        "Host:   foobar \r\n",
        "Host:   foobar \n",
        "Host: \t  foobar \t\r\n",
        "Host: \t  foobar \t\n",
        "Host:foobar\r\n",
        "Host:foobar\n"
    );

    for (var request : requests) {
      byte[] bytes;
      bytes = Bytes.utf8(request);

      exchange.buffer = bytes;
      exchange.bufferIndex = 5;
      exchange.bufferLimit = bytes.length;
      exchange.requestHeaders = null;
      exchange.requestHeaderName = StandardHeaderName.HOST;
      exchange.state = HttpExchange._PARSE_HEADER_VALUE;

      exchange.stepOne();

      assertEquals(exchange.bufferIndex, bytes.length);
      assertEquals(exchange.requestHeaders.size(), 1);
      assertEquals(exchange.requestHeaders.get(StandardHeaderName.HOST).toString(), "foobar");
      assertEquals(exchange.state, HttpExchange._PARSE_HEADER);
    }
  }

  @Test(description = """
  [#444] HTTP 001: PARSE_HEADER_VALUE --> PARSE_HEADER

  - semi-happy path: empty value
  """)
  public void parseHeaderValueEmptyValue() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    List<String> requests = List.of(
        "Host:\r\n",
        "Host:\n",
        "Host: \r\n",
        "Host: \n",
        "Host: \t\r\n",
        "Host: \t\n",
        "Host: \t \r\n",
        "Host: \t \n"
    );

    for (var request : requests) {
      byte[] bytes;
      bytes = Bytes.utf8(request);

      exchange.buffer = bytes;
      exchange.bufferIndex = 5;
      exchange.bufferLimit = bytes.length;
      exchange.requestHeaders = null;
      exchange.requestHeaderName = StandardHeaderName.HOST;
      exchange.state = HttpExchange._PARSE_HEADER_VALUE;

      exchange.stepOne();

      assertEquals(exchange.bufferIndex, bytes.length);
      assertEquals(exchange.requestHeaders.size(), 1);
      assertEquals(exchange.requestHeaders.get(StandardHeaderName.HOST).toString(), "");
      assertEquals(exchange.state, HttpExchange._PARSE_HEADER);
    }
  }

  @Test(description = """
  [#452] HTTP 001: PARSE_HEADER_VALUE --> PARSE_HEADER

  - semi-happy path: header name is unknown
  """)
  public void parseHeaderValueHeaderNameIsUnknown() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    List<String> requests = List.of(
        "Foo: foobar\r\n",
        "Foo: foobar\n",
        "Foo:   foobar \r\n",
        "Foo:   foobar \n",
        "Foo: \t  foobar \t\r\n",
        "Foo: \t  foobar \t\n",
        "Foo:foobar\r\n",
        "Foo:foobar\n"
    );

    for (var request : requests) {
      byte[] bytes;
      bytes = Bytes.utf8(request);

      exchange.buffer = bytes;
      exchange.bufferIndex = 4;
      exchange.bufferLimit = bytes.length;
      exchange.requestHeaders = null;
      exchange.requestHeaderName = null;
      exchange.state = HttpExchange._PARSE_HEADER_VALUE;

      exchange.stepOne();

      assertEquals(exchange.bufferIndex, bytes.length);
      assertEquals(exchange.requestHeaders, null);
      assertEquals(exchange.state, HttpExchange._PARSE_HEADER);
    }
  }

  @Test(description = """
  [#444] HTTP 001: PARSE_HEADER_VALUE --> INPUT_READ
  """)
  public void parseHeaderValueToInputRead() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    byte[] bytes;
    bytes = Bytes.utf8("Host: localh");

    exchange.buffer = Arrays.copyOf(bytes, 20);
    exchange.bufferIndex = 5;
    exchange.bufferLimit = bytes.length;
    exchange.requestHeaders = null;
    exchange.requestHeaderName = StandardHeaderName.HOST;
    exchange.state = HttpExchange._PARSE_HEADER_VALUE;

    exchange.stepOne();

    assertEquals(exchange.bufferIndex, 5);
    assertEquals(exchange.nextAction, HttpExchange._PARSE_HEADER_VALUE);
    assertEquals(exchange.requestHeaders, null);
    assertEquals(exchange.state, HttpExchange._INPUT_READ);
  }

  @Test(description = """
  [#444] HTTP 001: PARSE_HEADER_VALUE --> REQUEST_ERROR::BAD_REQUEST
  """)
  public void parseHeaderValueToRequestErrorBadRequest() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    byte[] bytes;
    bytes = Bytes.utf8("Host: localh");

    exchange.buffer = bytes;
    exchange.bufferIndex = 5;
    exchange.bufferLimit = bytes.length;
    exchange.requestHeaders = null;
    exchange.requestHeaderName = StandardHeaderName.HOST;
    exchange.state = HttpExchange._PARSE_HEADER_VALUE;

    exchange.stepOne();

    assertEquals(exchange.bufferIndex, 5);
    assertEquals(exchange.requestHeaders, null);
    assertEquals(exchange.state, HttpExchange._REQUEST_ERROR);
    assertEquals(exchange.status, HttpStatus.BAD_REQUEST);
  }

}