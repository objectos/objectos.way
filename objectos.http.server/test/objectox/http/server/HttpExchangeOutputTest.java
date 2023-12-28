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
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import objectos.http.HeaderName;
import objectos.http.Http;
import objectox.http.HttpStatus;
import org.testng.annotations.Test;

@SuppressWarnings("resource")
public class HttpExchangeOutputTest {

  @Test
  public void http001() throws IOException {
    ObjectoxHttpExchange exchange;
    exchange = new ObjectoxHttpExchange();

    Http001.INPUT.accept(exchange);

    assertTrue(exchange.active());

    Http001.response(exchange);

    assertFalse(exchange.active());

    assertEquals(exchange.bufferIndex, -1);
    assertEquals(exchange.bufferLimit, -1);
    assertEquals(exchange.error, null);
    assertEquals(exchange.keepAlive, false);
    assertEquals(exchange.method, null);
    assertEquals(exchange.requestHeaders, Map.of());
    assertEquals(exchange.requestHeaderName, null);
    assertEquals(exchange.requestPath, null);
    assertEquals(exchange.responseBody, null);
    assertEquals(exchange.responseHeaders, List.of());
    assertEquals(exchange.responseHeadersIndex, -1);
    assertEquals(exchange.socket.isClosed(), false);
    assertEquals(exchange.state, ObjectoxHttpExchange._STOP);
    // status won't be used from this point forward
    assertEquals(exchange.status, null);
    // version won't be used from this point forward
    assertEquals(exchange.versionMajor, -1);
    assertEquals(exchange.versionMinor, -1);

    TestableSocket socket;
    socket = (TestableSocket) exchange.socket;

    assertEquals(socket.outputAsString(), Http001.OUTPUT);
  }

  @Test
  public void http002() throws IOException {
    ObjectoxHttpExchange exchange;
    exchange = new ObjectoxHttpExchange();

    Http002.INPUT.accept(exchange);

    assertTrue(exchange.active());

    Http002.response(exchange);

    assertFalse(exchange.active());

    // buffer exhausted and reset
    assertEquals(exchange.bufferIndex, -1);
    assertEquals(exchange.bufferLimit, -1);
    assertEquals(exchange.error, null);
    assertEquals(exchange.keepAlive, false);
    assertEquals(exchange.method, null);
    assertEquals(exchange.requestHeaders, Map.of());
    assertEquals(exchange.requestHeaderName, null);
    assertEquals(exchange.requestPath, null);
    assertEquals(exchange.responseBody, null);
    assertEquals(exchange.responseHeaders, List.of());
    assertEquals(exchange.responseHeadersIndex, -1);
    assertEquals(exchange.socket.isClosed(), false);
    assertEquals(exchange.state, ObjectoxHttpExchange._STOP);
    // status won't be used from this point forward
    assertEquals(exchange.status, null);
    // version won't be used from this point forward
    assertEquals(exchange.versionMajor, -1);
    assertEquals(exchange.versionMinor, -1);

    TestableSocket socket;
    socket = (TestableSocket) exchange.socket;

    assertEquals(socket.outputAsString(), Http002.OUTPUT);
  }

  // OUTPUT

  @Test(description = """
  [#449] OUTPUT --> OUTPUT_STATUS

  - buffer reset
  - responseHeaderIndex reset
  """)
  public void output() {
    ObjectoxHttpExchange exchange;
    exchange = new ObjectoxHttpExchange();

    exchange.bufferIndex = 123;
    exchange.bufferLimit = 123;
    exchange.responseHeadersIndex = 123;
    exchange.state = ObjectoxHttpExchange._OUTPUT;

    exchange.stepOne();

    assertEquals(exchange.bufferIndex, 0);
    assertEquals(exchange.bufferLimit, 0);
    assertEquals(exchange.responseHeadersIndex, 0);
    assertEquals(exchange.state, ObjectoxHttpExchange._OUTPUT_STATUS);
  }

  // OUTPUT_STATUS

  @Test(description = """
  [#449] OUTPUT_STATUS --> OUTPUT_HEADER

  - buffer reset
  - responseHeaderIndex reset
  """)
  public void outputStatus() {
    ObjectoxHttpExchange exchange;
    exchange = new ObjectoxHttpExchange();

    exchange.buffer = new byte[64];
    exchange.bufferIndex = 0;
    exchange.bufferLimit = 0;
    exchange.state = ObjectoxHttpExchange._OUTPUT_STATUS;
    exchange.status = HttpStatus.OK;
    exchange.versionMajor = 1;
    exchange.versionMinor = 1;

    exchange.stepOne();

    assertEquals(Arrays.copyOf(exchange.buffer, 17), Bytes.utf8("HTTP/1.1 200 OK\r\n"));
    assertEquals(exchange.bufferIndex, 0);
    assertEquals(exchange.bufferLimit, 17);
    assertEquals(exchange.state, ObjectoxHttpExchange._OUTPUT_HEADER);
  }

  @Test(description = """
  [#522] OUTPUT_STATUS --> RESULT

  - buffer not large enough for status line
  """)
  public void outputStatusToResult() {
    ObjectoxHttpExchange exchange;
    exchange = new ObjectoxHttpExchange();

    exchange.buffer = new byte[3]; // too small
    exchange.bufferIndex = 0;
    exchange.bufferLimit = 0;
    exchange.state = ObjectoxHttpExchange._OUTPUT_STATUS;
    exchange.status = HttpStatus.OK;
    exchange.versionMajor = 1;
    exchange.versionMinor = 1;

    exchange.stepOne();

    assertEquals(exchange.bufferIndex, 0);
    assertEquals(exchange.bufferLimit, 0);
    assertNotNull(exchange.error);
    assertEquals(exchange.state, ObjectoxHttpExchange._RESULT);
  }

  // OUTPUT_HEADER

  @Test(description = """
  [#449] OUTPUT_HEADER --> OUTPUT_HEADER

  - buffer should contain data
  - bufferLimit should be updated
  - responseHeaderIndex should be updated
  """)
  public void outputHeader() {
    ObjectoxHttpExchange exchange;
    exchange = new ObjectoxHttpExchange();

    exchange.buffer = new byte[64];
    exchange.bufferIndex = 0;
    exchange.bufferLimit = 0;
    exchange.responseHeaders = List.of(
      hrh(Http.Header.CONNECTION, "close"),
      hrh(Http.Header.CONTENT_TYPE, "text/plain")
    );
    exchange.responseHeadersIndex = 0;
    exchange.state = ObjectoxHttpExchange._OUTPUT_HEADER;

    exchange.stepOne();

    assertEquals(Arrays.copyOf(exchange.buffer, 19), Bytes.utf8("Connection: close\r\n"));
    assertEquals(exchange.bufferIndex, 0);
    assertEquals(exchange.bufferLimit, 19);
    assertEquals(exchange.responseHeadersIndex, 1);
    assertEquals(exchange.state, ObjectoxHttpExchange._OUTPUT_HEADER);
  }

  @Test(description = """
  [#449] OUTPUT_HEADER --> OUTPUT_BUFFER (buffer full)
  """)
  public void outputHeaderToOutputBufferBufferFull() {
    ObjectoxHttpExchange exchange;
    exchange = new ObjectoxHttpExchange();

    exchange.buffer = new byte[20]; /* large enough for Connection header */
    exchange.bufferLimit = 10; /* but not enough space left... */
    exchange.responseHeaders = List.of(
      hrh(Http.Header.CONNECTION, "close"),
      hrh(Http.Header.CONTENT_TYPE, "text/plain")
    );
    exchange.responseHeadersIndex = 0;
    exchange.state = ObjectoxHttpExchange._OUTPUT_HEADER;

    exchange.stepOne();

    assertEquals(exchange.buffer, new byte[20]);
    assertEquals(exchange.bufferLimit, 10);
    assertEquals(exchange.nextAction, ObjectoxHttpExchange._OUTPUT_HEADER);
    assertEquals(exchange.responseHeadersIndex, 0);
    assertEquals(exchange.state, ObjectoxHttpExchange._OUTPUT_BUFFER);
  }

  @Test(description = """
  [#449] OUTPUT_HEADER --> OUTPUT_TERMINATOR
  """)
  public void outputHeaderToOutputTerminator() {
    ObjectoxHttpExchange exchange;
    exchange = new ObjectoxHttpExchange();

    exchange.responseHeaders = List.of(
      hrh(Http.Header.CONNECTION, "close"),
      hrh(Http.Header.CONTENT_TYPE, "text/plain")
    );
    exchange.responseHeadersIndex = 2;
    exchange.state = ObjectoxHttpExchange._OUTPUT_HEADER;

    exchange.stepOne();

    assertEquals(exchange.responseHeadersIndex, 2);
    assertEquals(exchange.state, ObjectoxHttpExchange._OUTPUT_TERMINATOR);
  }

  // OUTPUT_BUFFER

  @Test(description = """
  [#449] OUTPUT_BUFFER --> next action
  """)
  public void outputBuffer() {
    ObjectoxHttpExchange exchange;
    exchange = new ObjectoxHttpExchange();

    TestableSocket socket;
    socket = TestableSocket.empty();

    exchange.buffer = Bytes.utf8("12345xxx");
    exchange.bufferLimit = 5;
    exchange.nextAction = ObjectoxHttpExchange._STOP;
    exchange.socket = socket;
    exchange.state = ObjectoxHttpExchange._OUTPUT_BUFFER;

    exchange.stepOne();

    assertEquals(socket.outputAsString(), "12345");
    assertEquals(exchange.bufferLimit, 0);
    assertEquals(exchange.state, ObjectoxHttpExchange._STOP);
  }

  @Test(description = """
  [#449] OUTPUT_BUFFER --> ERROR_WRITE::fails to get output stream
  """)
  public void outputBufferToClose() {
    ObjectoxHttpExchange exchange;
    exchange = new ObjectoxHttpExchange();

    TestableSocket socket;
    socket = TestableSocket.throwsOnGetOutput();

    exchange.buffer = Bytes.utf8("12345xxx");
    exchange.bufferLimit = 5;
    exchange.error = null;
    exchange.socket = socket;
    exchange.state = ObjectoxHttpExchange._OUTPUT_BUFFER;

    exchange.stepOne();

    assertEquals(exchange.bufferLimit, 5);
    assertSame(exchange.error, socket.thrown);
    assertEquals(exchange.state, ObjectoxHttpExchange._RESULT);
  }

  @Test(description = """
  [#449] OUTPUT_BUFFER --> ERROR_WRITE::output stream throws on write
  """)
  public void outputBufferToCloseOnWrite() {
    ObjectoxHttpExchange exchange;
    exchange = new ObjectoxHttpExchange();

    TestableSocket socket;
    socket = TestableSocket.throwsOnWrite();

    exchange.buffer = Bytes.utf8("12345xxx");
    exchange.bufferLimit = 5;
    exchange.error = null;
    exchange.socket = socket;
    exchange.state = ObjectoxHttpExchange._OUTPUT_BUFFER;

    exchange.stepOne();

    assertEquals(exchange.bufferLimit, 5);
    assertSame(exchange.error, socket.thrown);
    assertEquals(exchange.state, ObjectoxHttpExchange._RESULT);
  }

  // OUTPUT_TERMINATOR

  @Test(description = """
  [#449] OUTPUT_TERMINATOR --> OUTPUT_BODY
  """)
  public void outputTerminator() {
    ObjectoxHttpExchange exchange;
    exchange = new ObjectoxHttpExchange();

    byte[] bytes;
    bytes = Bytes.utf8("last header\r\n");

    exchange.buffer = Arrays.copyOf(bytes, 20);
    exchange.bufferLimit = bytes.length;
    exchange.responseBody = new byte[0];
    exchange.state = ObjectoxHttpExchange._OUTPUT_TERMINATOR;

    exchange.stepOne();

    assertEquals(
      Arrays.copyOf(exchange.buffer, exchange.bufferLimit),
      Bytes.utf8("last header\r\n\r\n")
    );
    assertEquals(exchange.state, ObjectoxHttpExchange._OUTPUT_BODY);
  }

  @Test(description = """
  [#449] OUTPUT_TERMINATOR --> OUTPUT_BUFFER
  """)
  public void outputTerminatorToOutputBuffer() {
    ObjectoxHttpExchange exchange;
    exchange = new ObjectoxHttpExchange();

    byte[] bytes;
    bytes = Bytes.utf8("last header\r\n");

    exchange.buffer = bytes; // buffer is full
    exchange.bufferLimit = bytes.length;
    exchange.state = ObjectoxHttpExchange._OUTPUT_TERMINATOR;

    exchange.stepOne();

    assertEquals(exchange.nextAction, ObjectoxHttpExchange._OUTPUT_TERMINATOR);
    assertEquals(exchange.state, ObjectoxHttpExchange._OUTPUT_BUFFER);
  }

  // OUTPUT_BODY

  @Test(description = """
  [#449] OUTPUT_BODY --> RESULT
  """)
  public void outputBody() {
    ObjectoxHttpExchange exchange;
    exchange = new ObjectoxHttpExchange();

    TestableSocket socket;
    socket = TestableSocket.empty();

    byte[] headers;
    headers = Bytes.utf8("headers\r\n\r\n");

    exchange.buffer = headers;
    exchange.bufferLimit = headers.length;
    exchange.responseBody = Bytes.utf8("Hello world!\n");
    exchange.socket = socket;
    exchange.state = ObjectoxHttpExchange._OUTPUT_BODY;

    exchange.stepOne();

    assertEquals(socket.outputAsString(), "headers\r\n\r\nHello world!\n");
    assertEquals(exchange.state, ObjectoxHttpExchange._RESULT);
  }

  @Test(description = """
  [#449] OUTPUT_BODY --> ERROR_WRITE
  """)
  public void outputBodyToErrorWrite() {
    ObjectoxHttpExchange exchange;
    exchange = new ObjectoxHttpExchange();

    TestableSocket socket;
    socket = TestableSocket.throwsOnGetOutput();

    byte[] headers;
    headers = Bytes.utf8("headers\r\n\r\n");

    exchange.buffer = headers;
    exchange.bufferLimit = headers.length;
    exchange.responseBody = Bytes.utf8("Hello world!\n");
    exchange.socket = socket;
    exchange.state = ObjectoxHttpExchange._OUTPUT_BODY;

    exchange.stepOne();

    assertEquals(exchange.error, socket.thrown);
    assertEquals(exchange.state, ObjectoxHttpExchange._RESULT);
  }

  private HttpResponseHeader hrh(HeaderName name, String value) {
    return new HttpResponseHeader(name, value);
  }

}