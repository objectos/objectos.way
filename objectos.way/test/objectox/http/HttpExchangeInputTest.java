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
import static org.testng.Assert.assertSame;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import objectos.core.notes.Note1;
import org.testng.annotations.Test;

@SuppressWarnings("resource")
public class HttpExchangeInputTest {

  @Test
  public void http001() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    Http001.INPUT.accept(exchange);

    while (exchange.state < HttpExchange._REQUEST_LINE) {
      exchange.stepOne();
    }

    assertEquals(exchange.bufferIndex, 0);
    assertEquals(exchange.bufferLimit, Http001.INPUT.requestLength());
    assertEquals(exchange.error, null);
    assertEquals(exchange.keepAlive, true);
    assertEquals(exchange.method, null);
    assertEquals(exchange.requestHeaders, null);
    assertEquals(exchange.requestHeaderName, null);
    assertEquals(exchange.requestPath, null);
    assertEquals(exchange.responseBody, null);
    assertEquals(exchange.responseHeaders, null);
    assertEquals(exchange.responseHeadersIndex, -1);
    assertEquals(exchange.state, HttpExchange._REQUEST_LINE);
    assertEquals(exchange.status, null);
    assertEquals(exchange.versionMajor, -1);
    assertEquals(exchange.versionMinor, -1);
  }

  @Test(description = """
  [#427] HTTP 001: INPUT alias to INPUT_READ --> REQUEST_LINE
  """)
  public void input() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    exchange.buffer = new byte[64];
    exchange.bufferIndex = 0;
    exchange.bufferLimit = 0;
    exchange.socket = TestableSocket.of("FOO\r\n");
    exchange.state = HttpExchange._INPUT;

    exchange.stepOne();

    assertEquals(exchange.bufferIndex, 0);
    assertEquals(exchange.bufferLimit, 5);
    assertEquals(exchange.state, HttpExchange._REQUEST_LINE);
  }

  @Test(description = """
  [#428] HTTP 001: INPUT_READ --> INPUT_READ_EOF
  """)
  public void inputReadToInputReadEof() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    exchange.buffer = new byte[64];
    exchange.bufferIndex = 0;
    exchange.bufferLimit = 0;
    exchange.nextAction = HttpExchange._REQUEST_LINE;
    exchange.socket = TestableSocket.empty();
    exchange.state = HttpExchange._INPUT_READ;

    exchange.stepOne();

    assertEquals(exchange.bufferIndex, 0);
    assertEquals(exchange.bufferLimit, 0);
    assertEquals(exchange.state, HttpExchange._INPUT_READ_EOF);
  }

  @Test(description = """
  [#428] HTTP 001: INPUT_READ --> INPUT_READ_ERROR

  - Socket::getInputStream throws
  """)
  public void inputReadToInputReadError01() {
    IOException error;
    error = new IOException();

    HttpExchange exchange;
    exchange = new HttpExchange();

    exchange.bufferIndex = 0;
    exchange.bufferLimit = 0;
    exchange.nextAction = HttpExchange._REQUEST_LINE;
    exchange.socket = new Socket() {
      @Override
      public InputStream getInputStream() throws IOException {
        throw error;
      }
    };
    exchange.state = HttpExchange._INPUT_READ;

    exchange.stepOne();

    assertEquals(exchange.bufferIndex, 0);
    assertEquals(exchange.bufferLimit, 0);
    assertSame(exchange.error, error);
    assertEquals(exchange.state, HttpExchange._INPUT_READ_ERROR);
  }

  @Test(description = """
  [#428] HTTP 001: INPUT_READ --> INPUT_READ_ERROR

  - InputStream::read throws
  """)
  public void inputReadToInputReadError02() {
    IOException error;
    error = new IOException();

    HttpExchange exchange;
    exchange = new HttpExchange();

    exchange.buffer = new byte[64];
    exchange.bufferIndex = 0;
    exchange.bufferLimit = 0;
    exchange.nextAction = HttpExchange._REQUEST_LINE;
    exchange.socket = TestableSocket.of(error);
    exchange.state = HttpExchange._INPUT_READ;

    exchange.stepOne();

    assertEquals(exchange.bufferIndex, 0);
    assertEquals(exchange.bufferLimit, 0);
    assertSame(exchange.error, error);
    assertEquals(exchange.state, HttpExchange._INPUT_READ_ERROR);
  }

  @Test(description = """
  [#523] HTTP: INPUT_READ_ERROR -> STOP

  - state should be reset
  """)
  public void inputReadEof() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    exchange.bufferIndex = 1;
    exchange.bufferLimit = 2;
    exchange.state = HttpExchange._INPUT_READ_EOF;

    exchange.stepOne();

    assertEquals(exchange.bufferIndex, -1);
    assertEquals(exchange.bufferLimit, -1);
    assertEquals(exchange.state, HttpExchange._STOP);
  }

  @Test(description = """
  [#523] HTTP: INPUT_READ_ERROR -> STOP
  """)
  public void inputReadError() {
    IOException error;
    error = new IOException();

    HttpExchange exchange;
    exchange = new HttpExchange();

    var noteSink = new TestableNoteSink() {
      Note1<?> note1;
      Object value1;

      @Override
      public <T1> void send(Note1<T1> note, T1 v1) {
        note1 = note;
        value1 = v1;
      }
    };

    exchange.error = error;
    exchange.noteSink = noteSink;
    exchange.state = HttpExchange._INPUT_READ_ERROR;

    exchange.stepOne();

    assertEquals(exchange.error, null);
    assertEquals(exchange.state, HttpExchange._STOP);

    assertSame(noteSink.note1, HttpExchange.IO_READ_ERROR);
    assertSame(noteSink.value1, error);
  }

}