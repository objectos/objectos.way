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

import java.util.Set;
import org.testng.annotations.Test;

public class HttpRequestTargetQueryTest {
  
  @Test
  public void encodedValue() {
    assertEquals(queryOf("").encodedValue(), "");
    assertEquals(queryOf("foo=bar").encodedValue(), "foo=bar");
    assertEquals(queryOf("a=1&b=2").encodedValue(), "a=1&b=2");
    assertEquals(queryOf("email=user@example.com").encodedValue(), "email=user%40example.com");
    assertEquals(queryOf("").set("a", "1 2 3").encodedValue(), "a=1+2+3");
  }

  @Test(description = """
  UriQuery: empty
  """)
  public void testCase01() {
    HttpUriQuery q;
    q = queryOf("");

    assertEquals(q.get("foo"), null);
    assertEquals(q.get(""), null);
    assertEquals(q.isEmpty(), true);
  }

  @Test(description = """
  UriQuery: single value
  """)
  public void testCase02() {
    HttpUriQuery q;
    q = queryOf("foo=bar");

    assertEquals(q.get("foo"), "bar");
    assertEquals(q.get("x"), null);
    assertEquals(q.isEmpty(), false);
  }

  @Test(description = """
  UriQuery: name only
  """)
  public void testCase03() {
    HttpUriQuery q;
    q = queryOf("foo");

    assertEquals(q.get("foo"), "");
    assertEquals(q.get("x"), null);
    assertEquals(q.isEmpty(), false);
  }

  @Test(description = """
  UriQuery: empty value
  """)
  public void testCase04() {
    HttpUriQuery q;
    q = queryOf("foo=");

    assertEquals(q.get("foo"), "");
    assertEquals(q.get("x"), null);
    assertEquals(q.isEmpty(), false);
  }

  @Test(description = """
  UriQuery: corner cases
  """)
  public void testCase05() {
    HttpUriQuery q;
    q = queryOf("a&foo=");

    assertEquals(q.get("a"), "");
    assertEquals(q.get("foo"), "");
    assertEquals(q.get("x"), null);
    assertEquals(q.isEmpty(), false);
  }

  @Test(description = """
  UriQuery: names()
  """)
  public void testCase06() {
    assertEquals(
        queryOf("").names(),
        Set.of()
    );
    assertEquals(
        queryOf("a&foo=").names(),
        Set.of("a", "foo")
    );
    assertEquals(
        queryOf("a=b&c=d").names(),
        Set.of("a", "c")
    );
  }

  @Test(description = """
  UriQuery: getAsInt()
  """)
  public void testCase07() {
    HttpUriQuery q;
    q = queryOf("a=123&b=-456&c=foo&d=&e&f=123.45");

    assertEquals(q.getAsInt("a", -1), 123);
    assertEquals(q.getAsInt("b", -1), -456);
    assertEquals(q.getAsInt("c", -1), -1);
    assertEquals(q.getAsInt("d", -1), -1);
    assertEquals(q.getAsInt("e", -1), -1);
    assertEquals(q.getAsInt("f", -1), -1);
  }
  
  @Test(description = """
  UriQuery: set
  """)
  public void testCase08() {
    HttpUriQuery q;
    q = queryOf("a=1&b=2&c=3&d");

    Http.Request.Target.Query res;
    res = q.set("a", "x");

    assertEquals(res.get("a"), "x");
    assertEquals(res.get("b"), "2");
    assertEquals(res.get("c"), "3");
    assertEquals(res.get("d"), "");
  }

  @Test(description = """
  UriQuery: duplicate name should return first value
  """)
  public void testCase09() {
    HttpUriQuery q;
    q = queryOf("a=123&b=xpto&c&b=");

    assertEquals(q.get("a"), "123");
    assertEquals(q.get("b"), "xpto");
    assertEquals(q.get("c"), "");
  }

  private HttpUriQuery queryOf(String q) {
    HttpUriQuery query;
    query = new HttpUriQuery();

    query.set(q);

    return query;
  }

}