/*
 * Copyright (C) 2023 Objectos Software LTDA.
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
package objectos.http.server;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import objectox.http.server.TestableSocket;
import org.testng.annotations.Test;

public class ServerRequestTest {

  @Test(enabled = false, description = """
  GET / HTTP/1.1
  Host: www.example.com
  Connection: close
  """)
  public void testCase001() {
    ServerRequest request;
    request = regularInput("""
      GET / HTTP/1.1
      Host: www.example.com
      Connection: close

      """.replace("\n", "\r\n"));

    UriPath path;
    path = request.path();

    assertEquals(path.is("/"), true);
  }

  private ServerRequest regularInput(String input) {
    // we do not care about closing the Socket/Exchange as these are all in-memory instances
    // i.e. no real I/O
    TestableSocket socket;
    socket = TestableSocket.of(input);

    HttpExchange exchange;
    exchange = HttpExchange.create(socket);

    exchange.bufferSize(128);

    exchange.noteSink(TestingNoteSink.INSTANCE);

    ServerExchangeResult result;

    try {
      result = exchange.get();
    } catch (IOException e) {
      throw new AssertionError("Unexpected IOException", e);
    }

    return (ServerRequest) result;
  }

}