/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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

import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertSame;

import java.util.random.RandomGenerator;
import org.testng.annotations.Test;

public class WebSessionTest {

  private final RandomGenerator random = new TestingRandom.SequentialRandom();

  @Test
  public void create01() {
    final Web.Session session;
    session = Web.Session.create();

    final Object user;
    user = "foo";

    session.put("user", user);

    assertSame(session.get("user"), user);
  }

  @Test
  public void testCase01() {
    final WebToken id;
    id = WebToken.of32(random);

    final Web.Session session;
    session = new WebSession(id);

    final Object user;
    user = "foo";

    session.put("user", user);

    assertSame(session.get("user"), user);

    final Object newUser;
    newUser = "bar";

    session.put("user", newUser);

    assertSame(session.get("user"), newUser);

    session.remove("user");

    assertNull(session.get("user"));
  }

}