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

public class WayRequestLineTest {

  @Test(description = """
  GET / HTTP/1.1
  Host: www.example.com
  Connection: close
  """)
  public void testCase001() throws IOException {
    WayRequestLine line;
    line = regularInput("""
    GET / HTTP/1.1\r
    Host: www.example.com\r
    Connection: close\r
    \r
    """);

    line.parseRequestLine();

    // method
    Method method;
    method = line.method;

    assertEquals(method, Method.GET);

    // path
    WayUriPath path;
    path = line.path;

    assertEquals(path.toString(), "/");

    // query
    UriQuery query;
    query = line.query;

    assertEquals(query.isEmpty(), true);

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
    WayRequestLine line;
    line = regularInput("""
    GET /endpoint?foo=bar HTTP/1.1\r
    Host: www.example.com\r
    \r
    """);

    line.parseRequestLine();

    // method
    Method method;
    method = line.method;

    assertEquals(method, Method.GET);

    // path
    WayUriPath path;
    path = line.path;

    assertEquals(path.toString(), "/endpoint");

    // query
    UriQuery query;
    query = line.query;

    assertEquals(query.isEmpty(), false);
    assertEquals(query.get("foo"), "bar");
    assertEquals(query.get("x"), null);
    assertEquals(query.value(), "foo=bar");

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
    WayRequestLine line;
    line = regularInput("");

    line.parseRequestLine();

    assertEquals(line.method, null);
    assertEquals(line.parseStatus.isError(), true);
  }

  private WayRequestLine regularInput(Object... data) throws IOException {
    WayRequestLine requestLine;
    requestLine = new WayRequestLine();

    requestLine.bufferSize(64, 128);

    TestingInputStream inputStream;
    inputStream = TestingInputStream.of(data);

    requestLine.initSocketInput(inputStream);

    return requestLine;
  }

}