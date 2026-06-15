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
import static org.testng.Assert.assertSame;

import java.time.InstantSource;
import java.util.Map;
import objectos.way.Y;
import objectox.http.HttpToken;
import org.testng.annotations.Test;

public class SessionFinderTest {

  private final InstantSource instantSource = Y.clockFixed();

  @Test(description = "ignore unparsable")
  public void find01() {
    final SessionFinder finder;
    finder = SessionFinderY.create(opts -> {
      opts.instantSource = instantSource;
    });

    final String cookie;
    cookie = "not parsable";

    final Session res;
    res = finder.find(cookie);

    assertEquals(res, null);
  }

  @Test(description = "ignore non-existing token")
  public void find02() {
    final HttpToken id;
    id = HttpToken.of32(5, 6, 7, 8);

    final SessionFinder finder;
    finder = SessionFinderY.create(opts -> {
      opts.instantSource = instantSource;

      opts.sessions = Map.of();
    });

    final String cookie;
    cookie = id.toString();

    final Session res;
    res = finder.find(cookie);

    assertEquals(res, null);
  }

  @Test(description = "ignore existing invalid")
  public void find03() {
    final HttpToken id;
    id = HttpToken.of32(1, 2, 3, 4);

    final SessionPojo pojo;
    pojo = SessionPojoY.of();

    pojo.invalidate();

    final SessionFinder finder;
    finder = SessionFinderY.create(opts -> {
      opts.instantSource = instantSource;

      opts.session(id, pojo);
    });

    final String cookie;
    cookie = id.toString();

    final Session res;
    res = finder.find(cookie);

    assertEquals(res, null);
  }

  @Test(description = "return existing")
  public void find04() {
    final HttpToken id;
    id = HttpToken.of32(1, 2, 3, 4);

    final SessionPojo pojo;
    pojo = SessionPojoY.of();

    final SessionFinder finder;
    finder = SessionFinderY.create(opts -> {
      opts.instantSource = instantSource;

      opts.session(id, pojo);
    });

    final String cookie;
    cookie = id.toString();

    final Session res;
    res = finder.find(cookie);

    assertSame(res, pojo);

    assertEquals(pojo.accessTime(), instantSource.instant());
  }

}
