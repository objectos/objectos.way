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
import org.testng.annotations.Test;

public class WayServerRequestHeadersTest {

  @Test(description = """
  GET / HTTP/1.1
  Host: www.example.com
  Connection: close
  """)
  public void testCase001() throws IOException {
    WayServerRequestHeaders headers;
    headers = regularInput("""
    Host: www.example.com\r
    Connection: close\r
    \r
    """);

    headers.parseHeaders();

    assertEquals(headers.size(), 2);
    assertEquals(headers.first(HeaderName.HOST), "www.example.com");
    assertEquals(headers.first(HeaderName.CONNECTION), "close");
  }

  @Test
  public void testCase003() throws IOException {
    WayServerRequestHeaders headers;
    headers = regularInput("""
    Host: www.example.com\r
    Connection: close\r
    Foo: bar\r
    \r
    """);

    headers.parseHeaders();

    assertEquals(headers.size(), 3);
    assertEquals(headers.first(HeaderName.HOST), "www.example.com");
    assertEquals(headers.first(HeaderName.CONNECTION), "close");
    assertEquals(headers.first(HeaderName.create("Foo")), "bar");
  }

  @Test(description = """
  Minimal POST request
  - happy path
  """)
  public void testCase008() throws IOException {
    WayServerRequestHeaders headers;
    headers = regularInput("""
    Host: www.example.com\r
    Content-Length: 24\r
    Content-Type: application/x-www-form-urlencoded\r
    \r
    email=user%40example.com""");

    headers.parseHeaders();

    assertEquals(headers.size(), 3);
    assertEquals(headers.first(HeaderName.HOST), "www.example.com");
    assertEquals(headers.first(HeaderName.CONTENT_LENGTH), "24");
    assertEquals(headers.first(HeaderName.CONTENT_TYPE), "application/x-www-form-urlencoded");
  }

  @Test
  public void edge001() throws IOException {
    WayServerRequestHeaders headers;
    headers = regularInput("""
    no-leading-ows:foo\r
    empty-value:\r
    trailing-ows1: foo \r
    trailing-ows2: foo\040
    \r
    """);

    headers.parseHeaders();

    assertEquals(headers.size(), 4);
    assertEquals(headers.first(HeaderName.create("no-leading-ows")), "foo");
    assertEquals(headers.first(HeaderName.create("empty-value")), "");
    assertEquals(headers.first(HeaderName.create("trailing-ows1")), "foo");
    assertEquals(headers.first(HeaderName.create("trailing-ows2")), "foo");
  }

  private WayServerRequestHeaders regularInput(Object... data) {
    WayServerRequestHeaders headers;
    headers = new WayServerRequestHeaders();

    headers.bufferSize(64, 128);

    TestingInputStream inputStream;
    inputStream = TestingInputStream.of(data);

    headers.initSocketInput(inputStream);

    return headers;
  }

}