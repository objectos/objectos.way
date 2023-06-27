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

import java.net.Socket;
import objectos.http.AbstractHttpTest;
import objectos.http.HttpProcessor;
import objectos.http.internal.HttpExchange.RequestTarget;
import org.testng.annotations.Test;

public class HttpExchangeTest extends AbstractHttpTest {

  @Test(description = """
  HttpExchange TC0001

  - GET / 1.1
  - Host
  """)
  public void testCase0001() throws Throwable {
    Socket socket;
    socket = TestableSocket.parse("""
    GET / HTTP/1.1
    Host: localhost:7001

    """);

    HttpProcessor processor;
    processor = new HttpProcessor() {
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
    assertEquals(exchange.headerName, HeaderName.HOST);
    assertEquals(exchange.state, HttpExchange._REQUEST_HEADER_VALUE);

    exchange.stepOne();

    // SP 'l' 'o' 'c' 'a' 'l' 'h' 'o' 's' 't' = 10
    // ':' '7' '0' '0' '1' CR LF = 7
    assertEquals(exchange.bufferIndex, 38);
    //assertEquals(processor.host, "localhost:7001");
    assertEquals(exchange.state, HttpExchange._REQUEST_HEADER);

    exchange.stepOne();

    assertEquals(exchange.bufferIndex, 40);
    assertEquals(exchange.state, HttpExchange._PROCESS);
  }

}