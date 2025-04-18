/*
 * Copyright (C) 2025 Objectos Software LTDA.
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
package objectos.way;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.net.Socket;
import org.testng.annotations.Test;

public class HttpExchangeTest3RequestBody {

  @Test(description = """
  GET / HTTP/1.1
  Host: www.example.com
  Connection: close
  """)
  public void testCase001() throws IOException {
    HttpExchange body;
    body = regularInput("""
    Host: www.example.com\r
    Connection: close\r
    \r
    """);

    assertEquals(body.parseStatus.isError(), false);
    assertEquals(asString(body), "");
  }

  @Test(description = """
  Minimal POST request
  - happy path
  """)
  public void testCase008() throws IOException {
    HttpExchange body;
    body = regularInput("""
    Host: www.example.com\r
    Content-Length: 24\r
    Content-Type: application/x-www-form-urlencoded\r
    \r
    email=user%40example.com""");

    assertEquals(body.parseStatus.isError(), false);
    assertEquals(asString(body), "email=user%40example.com");
  }

  @Test(description = """
  Request body is larger than buffer
  """)
  public void testCase019() throws IOException {
    String chunk256 = """
    .................................................
    .................................................
    .................................................
    .................................................
    .................................................
    123456""";

    HttpExchange body;
    body = regularInput("""
    Host: www.example.com\r
    Content-Length: 256\r
    Content-Type: text/plain\r
    \r
    %s""".formatted(chunk256));

    try {
      assertEquals(body.parseStatus.isError(), false);
      assertEquals(asString(body), chunk256);
    } finally {
      body.close();
    }
  }

  private String asString(Http.RequestBody body) throws IOException {
    return ObjectosHttp.readString(body);
  }

  private HttpExchange regularInput(Object... data) throws IOException {
    final Socket socket;
    socket = Y.socket(data);

    HttpExchange http;
    http = new HttpExchange(socket, 64, 128, null, TestingNoteSink.INSTANCE);

    http.parseHeaders();

    http.parseRequestBody();

    http.parseRequestEnd();

    return http;
  }

}