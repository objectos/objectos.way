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
package objectos.http.session;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertSame;

import org.testng.annotations.Test;

public class SessionStoreTest {

  @Test(description = """
  Create a new session and confirm it can be found in the store
  """)
  public void testCase01() {
    SessionStore store;
    store = SessionStore.create();

    Session session;
    session = store.nextSession();

    assertNotNull(session);

    String id;
    id = session.id();

    assertNotNull(id);

    Session maybe;
    maybe = store.get(id);

    assertSame(maybe, session);
  }

  @Test(description = """
  It should be possible to store values to and retrieve values from the session.
  """)
  public void testCase02() {
    SessionStore store;
    store = SessionStore.create();

    Session session;
    session = store.nextSession();

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
    SessionStore store;
    store = SessionStore.create();

    Session session;
    session = store.nextSession();

    assertNotNull(session);

    String id;
    id = session.id();

    assertSame(store.get(id), session);

    session.invalidate();

    assertNull(store.get(id));
  }

}