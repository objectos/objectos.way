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
package objectos.way;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.util.Set;
import org.testng.annotations.Test;

public class HttpExchangeTest1RequestLine {

  @Test(description = """
  GET / HTTP/1.1
  Host: www.example.com
  Connection: close
  """)
  public void testCase001() throws IOException {
    HttpExchange line;
    line = regularInput("""
    GET / HTTP/1.1\r
    Host: www.example.com\r
    Connection: close\r
    \r
    """);

    line.parseRequestLine();

    // method
    byte method;
    method = line.method;

    assertEquals(method, Http.GET);

    // path
    assertEquals(line.path(), "/");

    // query
    assertEquals(line.rawQuery(), null);
    assertEquals(line.queryParamNames(), Set.of());

    // version
    assertEquals(line.versionMajor, 1);
    assertEquals(line.versionMinor, 1);

    // not bad request
    assertEquals(line.parseStatus.isError(), false);
  }

  @Test(description = """
  GET /endpoint?foo=bar HTTP/1.1
  Host: www.example.com
  """)
  public void testCase007() throws IOException {
    HttpExchange line;
    line = regularInput("""
    GET /endpoint?foo=bar HTTP/1.1\r
    Host: www.example.com\r
    \r
    """);

    line.parseRequestLine();

    // method
    byte method;
    method = line.method;

    assertEquals(method, Http.GET);

    // path
    assertEquals(line.path(), "/endpoint");

    // query
    assertEquals(line.rawQuery(), "foo=bar");
    assertEquals(line.queryParam("foo"), "bar");
    assertEquals(line.queryParam("x"), null);
    assertEquals(line.queryParamNames(), Set.of("foo"));

    // version
    assertEquals(line.versionMajor, 1);
    assertEquals(line.versionMinor, 1);

    // not bad request
    assertEquals(line.parseStatus.isError(), false);
  }

  @Test(description = """
  It should properly handle EOF on subsequent request line
  """)
  public void testCase020() throws IOException {
    HttpExchange line;
    line = regularInput("");

    line.parseRequestLine();

    assertEquals(line.method, 0);
    assertEquals(line.parseStatus.isError(), true);
  }

  private HttpExchange regularInput(Object... data) throws IOException {
    TestableSocket socket;
    socket = TestableSocket.of(data);

    return new HttpExchange(socket, 64, 128, null, TestingNoteSink.INSTANCE);
  }

}