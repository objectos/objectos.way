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
package objectox.http.server;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class ObjectoxUriQueryTest {

  @Test(description = """
  UriQuery: empty
  """)
  public void testCase01() {
    ObjectoxUriQuery q;
    q = queryOf("");

    assertEquals(q.get("foo"), null);
    assertEquals(q.get(""), null);
    assertEquals(q.isEmpty(), true);
  }

  @Test(description = """
  UriQuery: single value
  """)
  public void testCase02() {
    ObjectoxUriQuery q;
    q = queryOf("foo=bar");

    assertEquals(q.get("foo"), "bar");
    assertEquals(q.get("x"), null);
    assertEquals(q.isEmpty(), false);
  }

  @Test(description = """
  UriQuery: name only
  """)
  public void testCase03() {
    ObjectoxUriQuery q;
    q = queryOf("foo");

    assertEquals(q.get("foo"), "");
    assertEquals(q.get("x"), null);
    assertEquals(q.isEmpty(), false);
  }

  @Test(description = """
  UriQuery: empty value
  """)
  public void testCase04() {
    ObjectoxUriQuery q;
    q = queryOf("foo=");

    assertEquals(q.get("foo"), "");
    assertEquals(q.get("x"), null);
    assertEquals(q.isEmpty(), false);
  }

  @Test(description = """
  UriQuery: corner cases
  """)
  public void testCase05() {
    ObjectoxUriQuery q;
    q = queryOf("a&foo=");

    assertEquals(q.get("a"), "");
    assertEquals(q.get("foo"), "");
    assertEquals(q.get("x"), null);
    assertEquals(q.isEmpty(), false);
  }

  private ObjectoxUriQuery queryOf(String q) {
    ObjectoxUriQuery query;
    query = new ObjectoxUriQuery();

    query.set(q);

    return query;
  }

}