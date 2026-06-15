/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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
package objectox.http.session;

import static org.testng.Assert.assertEquals;

import objectos.lang.Key;
import org.testng.annotations.Test;

public class SessionLazyTest {

  @Test
  public void testCase01() {
    final SessionLazy lazy;
    lazy = new SessionLazy();

    assertEquals(lazy.isPresent(), false);
    assertEquals(lazy.attr(String.class), null);

    lazy.attr(String.class, "foo");

    assertEquals(lazy.isPresent(), true);
    assertEquals(lazy.attr(String.class), "foo");
  }

  private final Key<String> test = Key.of("TEST");

  @Test
  public void testCase02() {
    final SessionLazy lazy;
    lazy = new SessionLazy();

    assertEquals(lazy.isPresent(), false);
    assertEquals(lazy.attr(test), null);

    lazy.attr(test, "foo");

    assertEquals(lazy.isPresent(), true);
    assertEquals(lazy.attr(test), "foo");
  }

  @Test
  public void testCase03() {
    final SessionLazy lazy;
    lazy = new SessionLazy();

    assertEquals(lazy.isPresent(), false);
    assertEquals(lazy.attr(test), null);

    lazy.attr(test, "foo");

    assertEquals(lazy.isPresent(), true);
    assertEquals(lazy.attr(test), "foo");

    lazy.invalidate();

    assertEquals(lazy.isPresent(), false);
    assertEquals(lazy.attr(test), null);
  }

}
