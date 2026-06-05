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

import java.time.InstantSource;
import objectos.way.Y;
import org.testng.annotations.Test;

public class SessionFinderTest {

  private final InstantSource instantSource = Y.clockFixed();

  @Test(description = "return existing")
  public void find01() {
    final HttpToken id;
    id = HttpToken.of32(1, 2, 3, 4);

    final SessionFinder finder;
    finder = SessionFinderY.create(opts -> {
      opts.instantSource = instantSource;

      opts.sessionPut(id);
    });

    final String cookie;
    cookie = id.toString();

    final Session res;
    res = finder.find(cookie);

    assertEquals(res instanceof SessionPojo, true);

    final SessionPojo pojo;
    pojo = (SessionPojo) res;

    assertEquals(pojo.accessTime(), instantSource.instant());
    assertEquals(pojo.id(), id);
  }

  @Test(description = "return from supplier")
  public void find02() {
    final HttpToken id;
    id = HttpToken.of32(5, 6, 7, 8);

    final SessionFinder finder;
    finder = SessionFinderY.create(opts -> {
      opts.instantSource = instantSource;
    });

    final String cookie;
    cookie = id.toString();

    final Session res;
    res = finder.find(cookie);

    assertEquals(res, null);
  }

  @Test(description = "return from supplier")
  public void find03() {
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

}
