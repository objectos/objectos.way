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
import objectos.http.Status;
import org.testng.annotations.Test;

public class HttpExchangeParseHeaderTest {

  // PARSE_HEADER

  @Test(description = """
  [#445] HTTP 001: PARSE_HEADER --> PARSE_HEADER_NAME
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
  [#446] HTTP 001: PARSE_HEADER --> CLIENT_ERROR::BAD_REQUEST
  """)
  public void parseHeaderToClientErrorBadRequest() {
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
    assertEquals(exchange.state, HttpExchange._CLIENT_ERROR);
    assertEquals(exchange.status, Status.BAD_REQUEST);
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

    for (var headerName : HeaderName.values()) {
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

      assertEquals(exchange.bufferIndex, headerName.bytes.length + 1);
      assertEquals(exchange.requestHeaderName, headerName);
      assertEquals(exchange.state, HttpExchange._PARSE_HEADER_VALUE);
    }
  }

}