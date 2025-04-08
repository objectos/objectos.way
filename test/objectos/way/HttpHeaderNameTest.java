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

import java.nio.charset.StandardCharsets;
import org.testng.annotations.Test;

public class HttpHeaderNameTest {

  @Test
  public void map01() {
    final String tokenChars;
    tokenChars = "!#$%&'*+-.^`|~ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    final byte[] tokenBytes;
    tokenBytes = tokenChars.getBytes(StandardCharsets.US_ASCII);

    assertEquals(tokenBytes.length, tokenChars.length());

    for (byte b : tokenBytes) {
      assertEquals(HttpHeaderName.map(b) > 0, true);
    }
  }

  @Test
  public void of01() {
    for (HttpHeaderName name : HttpHeaderName.VALUES) {
      assertSame(Http.HeaderName.of(name.headerCase()), name);
      assertSame(Http.HeaderName.of(name.lowerCase()), name);
    }
  }

  @Test
  public void of02() {
    final Http.HeaderName res;
    res = Http.HeaderName.of("Foo-Bar");

    assertEquals(res.index(), -1);
    assertEquals(res.headerCase(), "Foo-Bar");
    assertEquals(res.lowerCase(), "foo-bar");
  }

  @SuppressWarnings("unlikely-arg-type")
  @Test(description = "equals() should work fine")
  public void testCase01() {
    Http.HeaderName foo1 = HttpHeaderName.of("Foo");
    Http.HeaderName foo2 = HttpHeaderName.of("Foo");
    Http.HeaderName bar = HttpHeaderName.of("Bar");

    assertEquals(foo1.equals(foo2), true);
    assertEquals(foo2.equals(foo1), true);
    assertEquals(foo2.equals(bar), false);
    assertEquals(bar.equals(foo2), false);
    assertEquals(bar.equals(null), false);
    assertEquals(bar.equals("Bar"), false);
  }

}