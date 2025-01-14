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
import org.testng.annotations.Test;

public class HttpExchangeTest2RequestHeaders {

  @Test(description = """
  GET / HTTP/1.1
  Host: www.example.com
  Connection: close
  """)
  public void testCase001() throws IOException {
    HttpExchange headers;
    headers = regularInput("""
    Host: www.example.com\r
    Connection: close\r
    \r
    """);

    headers.parseHeaders();

    assertEquals(headers.size(), 2);
    assertEquals(headers.header(Http.HeaderName.HOST), "www.example.com");
    assertEquals(headers.header(Http.HeaderName.CONNECTION), "close");
  }

  @Test
  public void testCase003() throws IOException {
    HttpExchange headers;
    headers = regularInput("""
    Host: www.example.com\r
    Connection: close\r
    Foo: bar\r
    \r
    """);

    headers.parseHeaders();

    assertEquals(headers.size(), 3);
    assertEquals(headers.header(Http.HeaderName.HOST), "www.example.com");
    assertEquals(headers.header(Http.HeaderName.CONNECTION), "close");
    assertEquals(headers.header(Http.HeaderName.create("Foo")), "bar");
  }

  @Test(description = """
  Minimal POST request
  - happy path
  """)
  public void testCase008() throws IOException {
    HttpExchange headers;
    headers = regularInput("""
    Host: www.example.com\r
    Content-Length: 24\r
    Content-Type: application/x-www-form-urlencoded\r
    \r
    email=user%40example.com""");

    headers.parseHeaders();

    assertEquals(headers.size(), 3);
    assertEquals(headers.header(Http.HeaderName.HOST), "www.example.com");
    assertEquals(headers.header(Http.HeaderName.CONTENT_LENGTH), "24");
    assertEquals(headers.header(Http.HeaderName.CONTENT_TYPE), "application/x-www-form-urlencoded");
  }

  @Test(description = """
  Properly handle empty From: header
  """)
  public void testCase020() throws IOException {
    HttpExchange headers;
    headers = regularInput("""
    Host: www.example.com\r
    From: \r
    Accept-Encoding: gzip, deflate, br\r
    \r
    """);

    headers.parseHeaders();

    assertEquals(headers.size(), 3);
    assertEquals(headers.header(Http.HeaderName.HOST), "www.example.com");
    assertEquals(headers.header(Http.HeaderName.FROM), "");
    assertEquals(headers.header(Http.HeaderName.ACCEPT_ENCODING), "gzip, deflate, br");
  }

  @Test
  public void edge001() throws IOException {
    HttpExchange headers;
    headers = regularInput("""
    no-leading-ows:foo\r
    empty-value:\r
    trailing-ows1: foo \r
    trailing-ows2: foo\040
    \r
    """);

    headers.parseHeaders();

    assertEquals(headers.size(), 4);
    assertEquals(headers.header(Http.HeaderName.create("no-leading-ows")), "foo");
    assertEquals(headers.header(Http.HeaderName.create("empty-value")), "");
    assertEquals(headers.header(Http.HeaderName.create("trailing-ows1")), "foo");
    assertEquals(headers.header(Http.HeaderName.create("trailing-ows2")), "foo");
  }

  private HttpExchange regularInput(Object... data) throws IOException {
    TestableSocket socket;
    socket = TestableSocket.of(data);

    return new HttpExchange(socket, 64, 128, null, TestingNoteSink.INSTANCE);
  }

}