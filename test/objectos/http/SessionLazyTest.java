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
package objectos.http;

import static org.testng.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import objectos.lang.Key;
import objectos.y.RandomGeneratorY;
import org.testng.annotations.Test;

public class SessionLazyTest {

  @Test
  public void testCase01() {
    final Map<HttpToken, SessionPojo> sessions;
    sessions = new HashMap<>();

    final SessionLazy lazy;
    lazy = SessionLazyY.create(opts -> {
      opts.randomGenerator = RandomGeneratorY.ofLongs(3, 4, 5, 6);

      opts.sessions = sessions;
    });

    assertEquals(lazy.isPresent(), false);
    assertEquals(lazy.attr(String.class), null);
    assertEquals(sessions.size(), 0);

    lazy.attr(String.class, "foo");

    assertEquals(lazy.isPresent(), true);
    assertEquals(lazy.attr(String.class), "foo");
    assertEquals(sessions.size(), 1);

    final HttpToken id;
    id = HttpToken.of32(3, 4, 5, 6);

    final SessionPojo pojo;
    pojo = sessions.get(id);

    assertEquals(pojo.id(), id);
  }

  private final Key<String> test = Key.of("TEST");

  @Test
  public void testCase02() {
    final Map<HttpToken, SessionPojo> sessions;
    sessions = new HashMap<>();

    final SessionLazy lazy;
    lazy = SessionLazyY.create(opts -> {
      opts.randomGenerator = RandomGeneratorY.ofLongs(1, 2, 3, 4);

      opts.sessions = sessions;
    });

    assertEquals(lazy.isPresent(), false);
    assertEquals(lazy.attr(test), null);
    assertEquals(sessions.size(), 0);

    lazy.attr(test, "foo");

    assertEquals(lazy.isPresent(), true);
    assertEquals(lazy.attr(test), "foo");
    assertEquals(sessions.size(), 1);

    final HttpToken id;
    id = HttpToken.of32(1, 2, 3, 4);

    final SessionPojo pojo;
    pojo = sessions.get(id);

    assertEquals(pojo.id(), id);
  }

}
