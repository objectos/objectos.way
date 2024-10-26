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

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import org.testng.annotations.Test;

public class WebStoreTest {

  @Test(description = """
  Create a new session and confirm it can be found in the repo
  """)
  public void testCase01() {
    Web.Store repo;
    repo = Web.Store.create(config -> {});

    Web.Session session;
    session = repo.createNext();

    assertNotNull(session);

    String id;
    id = session.id();

    assertNotNull(id);

    Web.Session maybe;
    maybe = repo.get(id);

    assertSame(maybe, session);
  }

  @Test(description = """
  It should be possible to repo values to and retrieve values from the session.
  """)
  public void testCase02() {
    Web.Store repo;
    repo = Web.Store.create(config -> {});

    Web.Session session;
    session = repo.createNext();

    Object user;
    user = "foo";

    assertNull(session.put("user", user));
    assertSame(session.get("user"), user);

    Object newUser;
    newUser = "bar";

    assertSame(session.put("user", newUser), user);

    assertSame(session.remove("user"), newUser);
    assertNull(session.get("user"));
  }

  @Test(description = """
  It should not be possible to retrieve a session after it has been invalidated
  """)
  public void testCase03() {
    Web.Store repo;
    repo = Web.Store.create(config -> {});

    Web.Session session;
    session = repo.createNext();

    assertNotNull(session);

    String id;
    id = session.id();

    assertSame(repo.get(id), session);

    session.invalidate();

    assertNull(repo.get(id));
  }

  @Test(description = """
  It should update last access time on each get
  """)
  public void testCase04() {
    IncrementingClock clock;
    clock = new IncrementingClock(2024, 4, 29);

    Web.Store repo;
    repo = Web.Store.create(config -> {
      config.clock(clock);
    });

    String id;
    id = "foo";

    WebSession session;
    session = new WebSession(id);

    Instant start;
    start = clock.instant();

    session.accessTime = start;

    repo.store(session);

    Web.Session res;
    res = repo.get(id);

    assertSame(res, session);

    assertTrue(session.accessTime.isAfter(start));
  }

  @Test(description = """
  It should remove all invalid sessions
  """)
  public void testCase05() {
    IncrementingClock clock;
    clock = new IncrementingClock(2024, 4, 29);

    Web.Store repo;
    repo = Web.Store.create(config -> {
      config.clock(clock);
    });

    WebSession a = new WebSession("a");
    WebSession b = new WebSession("b");
    WebSession c = new WebSession("c");

    repo.store(a);
    repo.store(b);
    repo.store(c);

    assertSame(repo.get("a"), a);
    assertSame(repo.get("b"), b);
    assertSame(repo.get("c"), c);

    b.invalidate();

    repo.cleanUp();

    assertSame(repo.get("a"), a);
    assertNull(repo.get("b"));
    assertSame(repo.get("c"), c);
  }

  @Test(description = """
  It should remove all empty sessions older than max age
  """)
  public void testCase06() {
    Clock clock;
    clock = TestingClock.FIXED;

    Web.Store repo;
    repo = Web.Store.create(config -> {
      config.clock(clock);

      config.emptyMaxAge(Duration.ofMinutes(1));
    });

    WebSession a = new WebSession("a");
    WebSession b = new WebSession("b");
    WebSession c = new WebSession("c");

    repo.store(a);
    repo.store(b);
    repo.store(c);

    assertSame(repo.get("a"), a);
    assertSame(repo.get("b"), b);
    assertSame(repo.get("c"), c);

    a.accessTime = clock.instant().minusSeconds(59);
    b.accessTime = clock.instant().minusSeconds(60);
    c.accessTime = clock.instant().minusSeconds(61);

    repo.cleanUp();

    assertSame(repo.get("a"), a);
    assertSame(repo.get("b"), b);
    assertNull(repo.get("c"));
  }

}