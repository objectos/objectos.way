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

import java.util.Arrays;
import java.util.List;
import objectos.http.Http;
import org.testng.annotations.Test;

public class HttpExchangeOutputTest {

  @Test(description = """
  [#449] OUTPUT --> OUTPUT_HEADER

  - buffer reset
  - responseHeaderIndex reset
  """)
  public void output() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    exchange.bufferIndex = 123;
    exchange.bufferLimit = 123;
    exchange.responseHeadersIndex = 123;
    exchange.state = HttpExchange._OUTPUT;

    exchange.stepOne();

    assertEquals(exchange.bufferIndex, 0);
    assertEquals(exchange.bufferLimit, 0);
    assertEquals(exchange.responseHeadersIndex, 0);
    assertEquals(exchange.state, HttpExchange._OUTPUT_HEADER);
  }

  @Test(description = """
  [#449] OUTPUT_HEADER --> OUTPUT_HEADER

  - buffer should contain data
  - bufferLimit should be updated
  - responseHeaderIndex should be updated
  """)
  public void outputHeader() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    exchange.buffer = new byte[64];
    exchange.bufferIndex = 0;
    exchange.bufferLimit = 0;
    exchange.responseHeaders = List.of(
      hrh(Http.Header.CONNECTION, "close"),
      hrh(Http.Header.CONTENT_TYPE, "text/plain")
    );
    exchange.responseHeadersIndex = 0;
    exchange.state = HttpExchange._OUTPUT_HEADER;

    exchange.stepOne();

    assertEquals(Arrays.copyOf(exchange.buffer, 19), "Connection: close\r\n".getBytes());
    assertEquals(exchange.bufferIndex, 0);
    assertEquals(exchange.bufferLimit, 19);
    assertEquals(exchange.responseHeadersIndex, 1);
    assertEquals(exchange.state, HttpExchange._OUTPUT_HEADER);
  }

  @Test(description = """
  [#449] OUTPUT_HEADER --> OUTPUT_BUFFER (buffer full)
  """)
  public void outputHeaderToOutputBufferBufferFull() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    exchange.buffer = new byte[20]; /* large enough for Connection header */
    exchange.bufferLimit = 10; /* but not enough space left... */
    exchange.responseHeaders = List.of(
      hrh(Http.Header.CONNECTION, "close"),
      hrh(Http.Header.CONTENT_TYPE, "text/plain")
    );
    exchange.responseHeadersIndex = 0;
    exchange.state = HttpExchange._OUTPUT_HEADER;

    exchange.stepOne();

    assertEquals(exchange.buffer, new byte[20]);
    assertEquals(exchange.bufferLimit, 10);
    assertEquals(exchange.nextAction, HttpExchange._OUTPUT_HEADER);
    assertEquals(exchange.responseHeadersIndex, 0);
    assertEquals(exchange.state, HttpExchange._OUTPUT_BUFFER);
  }

  @Test(description = """
  [#449] OUTPUT_HEADER --> OUTPUT_BUFFER (no more headers)
  """)
  public void outputHeaderToOutputBufferNoMoreHeader() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    exchange.responseHeaders = List.of(
      hrh(Http.Header.CONNECTION, "close"),
      hrh(Http.Header.CONTENT_TYPE, "text/plain")
    );
    exchange.responseHeadersIndex = 2;
    exchange.state = HttpExchange._OUTPUT_HEADER;

    exchange.stepOne();

    assertEquals(exchange.nextAction, HttpExchange._OUTPUT_BODY);
    assertEquals(exchange.responseHeadersIndex, 2);
    assertEquals(exchange.state, HttpExchange._OUTPUT_BUFFER);
  }

  private HttpResponseHeader hrh(Http.Header.Name name, String value) {
    return new HttpResponseHeader(name, value);
  }

}