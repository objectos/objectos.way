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
import static org.testng.Assert.assertSame;

import objectos.way.Http.HeaderName;
import org.testng.annotations.Test;

public class HttpHeaderNameTest {

  @Test
  public void of01() {
    for (int i = 0; i < HttpHeaderName.standardNamesSize(); i++) {
      HttpHeaderName std;
      std = HttpHeaderName.standardName(i);

      String name;
      name = std.capitalized();

      HeaderName result;
      result = Http.HeaderName.of(name);

      assertSame(result, std);
    }
  }

  @Test
  public void of02() {
    Http.HeaderName res;
    res = Http.HeaderName.of("Foo-Bar");

    assertEquals(res instanceof HttpHeaderNameUnknown, true);
    assertEquals(res.index(), -1);
    assertEquals(res.capitalized(), "Foo-Bar");
  }

  @SuppressWarnings("unlikely-arg-type")
  @Test(description = "equals() should work fine")
  public void testCase01() {
    Http.HeaderName foo1 = new HttpHeaderNameUnknown("Foo");
    Http.HeaderName foo2 = new HttpHeaderNameUnknown("Foo");
    Http.HeaderName bar = new HttpHeaderNameUnknown("Bar");

    assertEquals(foo1.equals(foo2), true);
    assertEquals(foo2.equals(foo1), true);
    assertEquals(foo2.equals(bar), false);
    assertEquals(bar.equals(foo2), false);
    assertEquals(bar.equals(null), false);
    assertEquals(bar.equals("Bar"), false);
  }

}