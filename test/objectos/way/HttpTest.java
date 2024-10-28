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

import org.testng.annotations.Test;

public class HttpTest {

  @Test(description = """
  It should be possible to create a custom/unknown header name
  """)
  public void createHeaderName01() {
    Http.HeaderName res;
    res = Http.HeaderName.create("Foo");

    assertEquals(res.capitalized(), "Foo");
    assertEquals(res.index(), -1);
  }

  @Test(description = """
  It should return StandardHeaderName for known header names
  """)
  public void createHeaderName02() {
    Http.HeaderName res;
    res = Http.HeaderName.create("Connection");

    assertEquals(res.capitalized(), "Connection");
    assertEquals(res.index() >= 0, true);
  }

  @Test
  public void method() {
    assertEquals(Http.method(Http.CONNECT).name(), "CONNECT");
    assertEquals(Http.method(Http.DELETE).name(), "DELETE");
    assertEquals(Http.method(Http.GET).name(), "GET");
    assertEquals(Http.method(Http.HEAD).name(), "HEAD");
    assertEquals(Http.method(Http.OPTIONS).name(), "OPTIONS");
    assertEquals(Http.method(Http.PATCH).name(), "PATCH");
    assertEquals(Http.method(Http.POST).name(), "POST");
    assertEquals(Http.method(Http.PUT).name(), "PUT");
    assertEquals(Http.method(Http.TRACE).name(), "TRACE");
  }

  @Test(description = """
  It should parse a single name-value pair
  """)
  public void parseCookies01() {
    Http.Request.Cookies c;
    c = Http.parseCookies("foo=bar");

    assertEquals(c.get("foo"), "bar");
  }

  @Test
  public void parseRequestTarget01() {
    Http.RequestTarget target;
    target = HttpExchange.parseRequestTarget("/");

    assertEquals(target.path(), "/");
  }

  @Test
  public void parseRequestTarget02() {
    Http.RequestTarget target;
    target = HttpExchange.parseRequestTarget("/foo/bar?page=1&sort=asc");

    assertEquals(target.path(), "/foo/bar");
    assertEquals(target.queryParam("page"), "1");
    assertEquals(target.queryParamAsInt("page", 0), 1);
    assertEquals(target.queryParam("sort"), "asc");
  }

}