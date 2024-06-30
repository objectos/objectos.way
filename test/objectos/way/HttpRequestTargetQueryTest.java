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

public class HttpRequestTargetQueryTest {

  @Test(description = """
  UriQuery: empty
  """)
  public void testCase01() {
    Http.Request.Target q;
    q = queryOf("");

    assertEquals(q.queryParam("foo"), null);
    assertEquals(q.queryParam(""), null);
    assertEquals(q.rawQuery(), "");
  }

  @Test(description = """
  UriQuery: single value
  """)
  public void testCase02() {
    Http.Request.Target q;
    q = queryOf("foo=bar");

    assertEquals(q.queryParam("foo"), "bar");
    assertEquals(q.queryParam("x"), null);
    assertEquals(q.rawQuery(), "foo=bar");
  }

  @Test(description = """
  UriQuery: name only
  """)
  public void testCase03() {
    Http.Request.Target q;
    q = queryOf("foo");

    assertEquals(q.queryParam("foo"), "");
    assertEquals(q.queryParam("x"), null);
    assertEquals(q.rawQuery(), "foo");
  }

  @Test(description = """
  UriQuery: empty value
  """)
  public void testCase04() {
    Http.Request.Target q;
    q = queryOf("foo=");

    assertEquals(q.queryParam("foo"), "");
    assertEquals(q.queryParam("x"), null);
    assertEquals(q.rawQuery(), "foo=");
  }

  @Test(description = """
  UriQuery: corner cases
  """)
  public void testCase05() {
    Http.Request.Target q;
    q = queryOf("a&foo=");

    assertEquals(q.queryParam("a"), "");
    assertEquals(q.queryParam("foo"), "");
    assertEquals(q.queryParam("x"), null);
    assertEquals(q.rawQuery(), "a&foo=");
  }

  @Test(description = """
  UriQuery: corner cases
  """)
  public void testCase06() {
    Http.Request.Target q;
    q = queryOf("a=1+2+3&b=foo");

    assertEquals(q.queryParam("a"), "1 2 3");
    assertEquals(q.queryParam("b"), "foo");
    assertEquals(q.queryParam("x"), null);
    assertEquals(q.rawQuery(), "a=1+2+3&b=foo");
  }

  @Test(description = """
  UriQuery: getAsInt()
  """)
  public void testCase07() {
    Http.Request.Target q;
    q = queryOf("a=123&b=-456&c=foo&d=&e&f=123.45");

    assertEquals(q.queryParamAsInt("a", -1), 123);
    assertEquals(q.queryParamAsInt("b", -1), -456);
    assertEquals(q.queryParamAsInt("c", -1), -1);
    assertEquals(q.queryParamAsInt("d", -1), -1);
    assertEquals(q.queryParamAsInt("e", -1), -1);
    assertEquals(q.queryParamAsInt("f", -1), -1);
  }

  @Test(description = """
  UriQuery: set
  """)
  public void testCase08() {
    Http.Request.Target q;
    q = queryOf("a=1&b=2&c=3&d");

    assertEquals(q.queryParam("a"), "1");
    assertEquals(q.queryParam("b"), "2");
    assertEquals(q.queryParam("c"), "3");
    assertEquals(q.queryParam("d"), "");
  }

  @Test(description = """
  UriQuery: duplicate name should return first value
  """)
  public void testCase09() {
    Http.Request.Target q;
    q = queryOf("a=123&b=xpto&c&b=");

    assertEquals(q.queryParam("a"), "123");
    assertEquals(q.queryParam("b"), "xpto");
    assertEquals(q.queryParam("c"), "");
  }

  @Test
  public void testCase10() {
    Http.Request.Target q;
    q = queryOf("%26=the%20%26%20char&foo=bar");

    assertEquals(q.queryParam("&"), "the & char");
    assertEquals(q.queryParam("foo"), "bar");
    assertEquals(q.queryParam("%26"), null);
  }

  private Http.Request.Target queryOf(String q) {
    return Http.parseRequestTarget("/test?" + q);
  }

}