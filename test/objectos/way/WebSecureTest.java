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
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import java.time.Instant;
import java.util.random.RandomGenerator;
import org.testng.annotations.Test;

public class WebSecureTest {

  @Test(description = """
  Create a new session and confirm it can be found in the repo
  """)
  public void createSession01() {
    final Web.Secure secure;
    secure = Web.Secure.create(options -> {
      options.randomGenerator(generator(1L, 2L, 3L, 4L));
    });

    final Web.Session session;
    session = secure.createSession();

    assertNotNull(session);

    final Http.Exchange http;
    http = Http.Exchange.create(config -> {
      config.header(Http.HeaderName.COOKIE, cookie("OBJECTOSWAY", 1L, 2L, 3L, 4L));
    });

    final Web.Session maybe;
    maybe = secure.getSession(http);

    assertSame(maybe, session);
  }

  @Test
  public void ensureSession01() {
    final Web.Secure secure;
    secure = Web.Secure.create(options -> {
      options.randomGenerator(generator(1L, 2L, 3L, 4L));
    });

    final Http.Exchange http1;
    http1 = Http.Exchange.create(config -> {});

    final Web.Session created;
    created = secure.ensureSession(http1);

    assertNotNull(created);

    final Http.Exchange http2;
    http2 = Http.Exchange.create(config -> {
      config.header(Http.HeaderName.COOKIE, cookie("OBJECTOSWAY", 1L, 2L, 3L, 4L));
    });

    final Web.Session maybe;
    maybe = secure.ensureSession(http2);

    assertSame(maybe, created);
  }

  @Test(description = """
  It should not be possible to retrieve a session after it has been invalidated
  """)
  public void getSession01() {
    final Web.Secure secure;
    secure = Web.Secure.create(options -> {
      options.randomGenerator(generator(5L, 6L, 7L, 8L));
    });

    final Web.Session session;
    session = secure.createSession();

    assertNotNull(session);

    final Http.Exchange http;
    http = Http.Exchange.create(config -> {
      config.header(Http.HeaderName.COOKIE, cookie("OBJECTOSWAY", 5L, 6L, 7L, 8L));
    });

    assertSame(secure.getSession(http), session);

    session.invalidate();

    assertNull(secure.getSession(http));
  }

  @Test(description = """
  It should update last access time on each get
  """)
  public void getSession02() {
    final IncrementingClock clock;
    clock = new IncrementingClock(2024, 4, 29);

    final Web.Secure secure;
    secure = Web.Secure.create(options -> {
      options.clock(clock);

      options.randomGenerator(generator(-1L, -2L, -3L, -4L));
    });

    final WebSession session;
    session = (WebSession) secure.createSession();

    final Instant start;
    start = session.accessTime;

    final Http.Exchange http;
    http = Http.Exchange.create(options -> {
      options.header(Http.HeaderName.COOKIE, cookie("OBJECTOSWAY", -1L, -2L, -3L, -4L));
    });

    final Web.Session res;
    res = secure.getSession(http);

    assertSame(res, session);

    assertTrue(session.accessTime.isAfter(start));
  }

  @Test
  public void setCookie01() {
    final Web.Secure secure;
    secure = Web.Secure.create(options -> {
      options.randomGenerator(generator(5L, 6L, 7L, 8L));
    });

    final Web.Session session;
    session = secure.createSession();

    final Http.SetCookie setCookie;
    setCookie = secure.setCookie(session);

    assertEquals(
        setCookie.toString(),

        "OBJECTOSWAY=AAAAAAAAAAUAAAAAAAAABgAAAAAAAAAHAAAAAAAAAAg=; HttpOnly; Path=/; Secure"
    );
  }

  private String cookie(String name, long l0, long l1, long l2, long l3) {
    return Y.cookie(name, l0, l1, l2, l3);
  }

  private RandomGenerator generator(long... values) {
    return Y.randomGeneratorOfLongs(values);
  }

}