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

import java.time.InstantSource;
import java.util.HashMap;
import java.util.Map;
import objectos.way.Y;
import objectos.y.RandomGeneratorY;
import objectox.http.HttpToken;
import org.testng.annotations.Test;

public class SessionFactoryTest {

  private final InstantSource instantSource = Y.clockFixed();

  @Test
  public void next01() {
    final Map<HttpToken, SessionPojo> sessions;
    sessions = new HashMap<>();

    final SessionFactory factory;
    factory = SessionFactoryY.create(opts -> {
      opts.instantSource = instantSource;

      opts.randomGenerator = RandomGeneratorY.ofLongs(1, 2, 3, 4);

      opts.sessions = sessions;
    });

    final HttpToken id;
    id = factory.next(lazy());

    assertEquals(id, HttpToken.of32(1, 2, 3, 4));

    assertEquals(sessions.size(), 1);

    final SessionPojo pojo;
    pojo = sessions.get(id);

    assertEquals(pojo.accessTime(), instantSource.instant());
    assertEquals(pojo.attr(String.class), "foo");
    assertEquals(pojo.isPresent(), true);
  }

  @Test
  public void next02() {
    final HttpToken existing;
    existing = HttpToken.of32(1, 2, 3, 4);

    final Map<HttpToken, SessionPojo> sessions;
    sessions = new HashMap<>();

    sessions.put(existing, new SessionPojo(Map.of()));

    final SessionFactory factory;
    factory = SessionFactoryY.create(opts -> {
      opts.instantSource = instantSource;

      opts.randomGenerator = RandomGeneratorY.ofLongs(1, 2, 3, 4, 5, 6, 7, 8);

      opts.sessions = sessions;
    });

    final HttpToken id;
    id = factory.next(lazy());

    assertEquals(id, HttpToken.of32(5, 6, 7, 8));

    assertEquals(sessions.size(), 2);

    final SessionPojo pojo;
    pojo = sessions.get(id);

    assertEquals(pojo.accessTime(), instantSource.instant());
    assertEquals(pojo.attr(String.class), "foo");
    assertEquals(pojo.isPresent(), true);
  }

  private SessionLazy lazy() {
    final SessionLazy lazy;
    lazy = new SessionLazy();

    lazy.attr(String.class, "foo");

    return lazy;
  }

}