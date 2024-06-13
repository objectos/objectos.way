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
package objectos.http;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import objectos.way.Http;
import org.testng.annotations.Test;

public class WayServerRequestBodyTest {

  @Test(description = """
  GET / HTTP/1.1
  Host: www.example.com
  Connection: close
  """)
  public void testCase001() throws IOException {
    WayServerRequestBody body;
    body = regularInput("""
    Host: www.example.com\r
    Connection: close\r
    \r
    """);

    body.parseHeaders();
    body.parseRequestBody();

    assertEquals(body.parseStatus.isError(), false);
    assertEquals(asString(body), "");
  }

  @Test(description = """
  Minimal POST request
  - happy path
  """)
  public void testCase008() throws IOException {
    WayServerRequestBody body;
    body = regularInput("""
    Host: www.example.com\r
    Content-Length: 24\r
    Content-Type: application/x-www-form-urlencoded\r
    \r
    email=user%40example.com""");

    body.parseHeaders();
    body.parseRequestBody();

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

    WayServerRequestBody body;
    body = regularInput("""
    Host: www.example.com\r
    Content-Length: 256\r
    Content-Type: text/plain\r
    \r
    %s""".formatted(chunk256));

    try {
      body.parseHeaders();
      body.parseRequestBody();

      assertEquals(body.parseStatus.isError(), false);
      assertEquals(asString(body), chunk256);
    } finally {
      body.close();
    }
  }

  private String asString(Http.Request.Body body) throws IOException {
    return ObjectosHttp.readString(body);
  }

  private WayServerRequestBody regularInput(Object... data) {
    WayServerRequestBody body;
    body = new WayServerRequestBody();

    body.bufferSize(64, 128);

    TestingInputStream inputStream;
    inputStream = TestingInputStream.of(data);

    body.initSocketInput(inputStream);

    return body;
  }

}