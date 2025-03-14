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
import java.util.Set;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HttpExchangeTest1RequestLine {

  @Test(description = "rawQueryWith: add a new parameter to an empty query")
  public void rawQueryWith01() throws IOException {
    Http.RequestTarget target;
    target = rawQuery("/test");

    assertEquals(target.rawQuery(), null);
    assertEquals(target.rawQueryWith("page", "123"), "page=123");
  }

  @Test(description = "rawQueryWith: add a new parameter to an non-empty query")
  public void rawQueryWith02() {
    Http.RequestTarget target;
    target = rawQuery("/test?q=a");

    assertEquals(target.rawQuery(), "q=a");
    assertEquals(target.rawQueryWith("page", "123"), "q=a&page=123");
  }

  @Test(description = "rawQueryWith: replace existing parameter value")
  public void rawQueryWith03() {
    Http.RequestTarget target;
    target = rawQuery("/test?q=a&page=foo");

    assertEquals(target.rawQuery(), "q=a&page=foo");
    assertEquals(target.rawQueryWith("page", "123"), "q=a&page=123");
  }

  @Test(description = "rawQueryWith: name needs encoding")
  public void rawQueryWith04() {
    Http.RequestTarget target;
    target = rawQuery("/test");

    assertEquals(target.rawQuery(), null);
    assertEquals(target.rawQueryWith("@", "123"), "%40=123");
  }

  @Test(description = "rawQueryWith: value needs encoding")
  public void rawQueryWith05() {
    Http.RequestTarget target;
    target = rawQuery("/test");

    assertEquals(target.rawQuery(), null);
    assertEquals(target.rawQueryWith("page", "@"), "page=%40");
  }

  @Test(description = "rawQueryWith: name and value needs encoding")
  public void rawQueryWith06() {
    Http.RequestTarget target;
    target = rawQuery("/test");

    assertEquals(target.rawQuery(), null);
    assertEquals(target.rawQueryWith("@", "~"), "%40=%7E");
  }

  @Test(description = "rawQueryWith: reject null name")
  public void rawQueryWith07() {
    Http.RequestTarget target;
    target = rawQuery("/test");

    try {
      target.rawQueryWith(null, "foo");

      Assert.fail();
    } catch (NullPointerException expected) {

    }
  }

  @Test(description = "rawQueryWith: reject null value")
  public void rawQueryWith08() {
    Http.RequestTarget target;
    target = rawQuery("/test");

    try {
      target.rawQueryWith("foo", null);

      Assert.fail();
    } catch (NullPointerException expected) {

    }
  }

  @Test(description = "rawQueryWith: reject empty name")
  public void rawQueryWith09() {
    Http.RequestTarget target;
    target = rawQuery("/test");

    try {
      target.rawQueryWith("", "foo");

      Assert.fail();
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), "name must not be blank");
    }
  }

  @Test(description = "rawQueryWith: reject blank name")
  public void rawQueryWith10() {
    Http.RequestTarget target;
    target = rawQuery("/test");

    try {
      target.rawQueryWith("   ", "foo");

      Assert.fail();
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), "name must not be blank");
    }
  }

  @Test(description = "rawQueryWith: accept empty value")
  public void rawQueryWith11() {
    Http.RequestTarget target;
    target = rawQuery("/test");

    assertEquals(target.rawQuery(), null);
    assertEquals(target.rawQueryWith("page", ""), "page=");
  }

  private Http.RequestTarget rawQuery(String path) {
    try {
      String request = """
      GET %s HTTP/1.1\r
      Host: www.example.com\r
      \r
      """.formatted(path);

      HttpExchange line;
      line = regularInput(request);

      line.parseRequestLine();

      return line;
    } catch (IOException e) {
      throw new AssertionError(e);
    }
  }

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
    Http.Method method;
    method = line.method();

    assertEquals(method, Http.Method.GET);

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
    Http.Method method;
    method = line.method();

    assertEquals(method, Http.Method.GET);

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

    assertEquals(line.method(), null);
    assertEquals(line.parseStatus.isError(), true);
  }

  private HttpExchange regularInput(Object... data) throws IOException {
    TestableSocket socket;
    socket = TestableSocket.of(data);

    return new HttpExchange(socket, 64, 128, null, TestingNoteSink.INSTANCE);
  }

}