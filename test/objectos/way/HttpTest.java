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

import org.testng.annotations.Test;

public class HttpTest {

  @Test(description = """
  It should parse a single name-value pair
  """)
  public void parseCookies01() {
    Http.Cookies c;
    c = Http.Cookies.parse("foo=bar");

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