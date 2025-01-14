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

import java.util.Set;
import org.testng.annotations.Test;

public class HttpRequestTargetQueryTest {

  @Test(description = """
  UriQuery: empty
  """)
  public void testCase01() {
    Http.RequestTarget q;
    q = queryOf("");

    assertEquals(q.queryParam("foo"), null);
    assertEquals(q.queryParam(""), null);
    assertEquals(q.queryParamNames(), Set.of());
  }

  @Test(description = """
  UriQuery: single value
  """)
  public void testCase02() {
    Http.RequestTarget q;
    q = queryOf("foo=bar");

    assertEquals(q.queryParam("foo"), "bar");
    assertEquals(q.queryParam("x"), null);
    assertEquals(q.queryParamNames(), Set.of("foo"));
  }

  @Test(description = """
  UriQuery: name only
  """)
  public void testCase03() {
    Http.RequestTarget q;
    q = queryOf("foo");

    assertEquals(q.queryParam("foo"), "");
    assertEquals(q.queryParam("x"), null);
    assertEquals(q.queryParamNames(), Set.of("foo"));
  }

  @Test(description = """
  UriQuery: empty value
  """)
  public void testCase04() {
    Http.RequestTarget q;
    q = queryOf("foo=");

    assertEquals(q.queryParam("foo"), "");
    assertEquals(q.queryParam("x"), null);
    assertEquals(q.queryParamNames(), Set.of("foo"));
  }

  @Test(description = """
  UriQuery: corner cases
  """)
  public void testCase05() {
    Http.RequestTarget q;
    q = queryOf("a&foo=");

    assertEquals(q.queryParam("a"), "");
    assertEquals(q.queryParam("foo"), "");
    assertEquals(q.queryParam("x"), null);
    assertEquals(q.queryParamNames(), Set.of("a", "foo"));
  }

  @Test(description = """
  UriQuery: corner cases
  """)
  public void testCase06() {
    Http.RequestTarget q;
    q = queryOf("a=1+2+3&b=foo");

    assertEquals(q.queryParam("a"), "1 2 3");
    assertEquals(q.queryParam("b"), "foo");
    assertEquals(q.queryParam("x"), null);
    assertEquals(q.queryParamNames(), Set.of("a", "b"));
  }

  @Test(description = """
  UriQuery: getAsInt()
  """)
  public void testCase07() {
    Http.RequestTarget q;
    q = queryOf("a=123&b=-456&c=foo&d=&e&f=123.45");

    assertEquals(q.queryParamAsInt("a", -1), 123);
    assertEquals(q.queryParamAsInt("b", -1), -456);
    assertEquals(q.queryParamAsInt("c", -1), -1);
    assertEquals(q.queryParamAsInt("d", -1), -1);
    assertEquals(q.queryParamAsInt("e", -1), -1);
    assertEquals(q.queryParamAsInt("f", -1), -1);
    assertEquals(q.queryParamNames(), Set.of("a", "b", "c", "d", "e", "f"));
  }

  @Test(description = """
  UriQuery: set
  """)
  public void testCase08() {
    Http.RequestTarget q;
    q = queryOf("a=1&b=2&c=3&d");

    assertEquals(q.queryParam("a"), "1");
    assertEquals(q.queryParam("b"), "2");
    assertEquals(q.queryParam("c"), "3");
    assertEquals(q.queryParam("d"), "");
    assertEquals(q.queryParamNames(), Set.of("a", "b", "c", "d"));
  }

  @Test(description = """
  UriQuery: duplicate name should return first value
  """)
  public void testCase09() {
    Http.RequestTarget q;
    q = queryOf("a=123&b=xpto&c&b=");

    assertEquals(q.queryParam("a"), "123");
    assertEquals(q.queryParam("b"), "xpto");
    assertEquals(q.queryParam("c"), "");
    assertEquals(q.queryParamNames(), Set.of("a", "b", "c"));
  }

  @Test
  public void testCase10() {
    Http.RequestTarget q;
    q = queryOf("%26=the%20%26%20char&foo=bar");

    assertEquals(q.queryParam("&"), "the & char");
    assertEquals(q.queryParam("foo"), "bar");
    assertEquals(q.queryParam("%26"), null);
    assertEquals(q.queryParamNames(), Set.of("&", "foo"));
  }

  private Http.RequestTarget queryOf(String q) {
    return HttpExchange.parseRequestTarget("/test?" + q);
  }

}