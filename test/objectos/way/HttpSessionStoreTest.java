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
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import java.util.function.Consumer;
import java.util.random.RandomGenerator;
import org.testng.annotations.Test;

public class HttpSessionStoreTest {

  @Test
  public void createSession01() {
    final HttpSessionStore store;
    store = create(options -> {
      options.sessionGenerator(generator(1L, 2L, 3L, 4L));
    });

    final HttpSession session;
    session = store.createSession();

    assertNotNull(session);

    final Http.Exchange http;
    http = Http.Exchange.create(config -> {
      config.header(Http.HeaderName.COOKIE, cookie("OBJECTOSWAY", 1L, 2L, 3L, 4L));
    });

    final HttpSession maybe;
    maybe = store.getSession(http);

    assertSame(maybe, session);
  }

  @Test
  public void loadSession01() {
    final HttpSessionStore store;
    store = create(options -> {
      options.sessionGenerator(generator(1L, 2L, 3L, 4L));
    });

    final HttpSession session;
    session = store.createSession();

    assertNotNull(session);

    final HttpExchange http;
    http = HttpExchange.create0(config -> {
      config.header(Http.HeaderName.COOKIE, cookie("OBJECTOSWAY", 1L, 2L, 3L, 4L));
    });

    assertEquals(http.sessionLoaded(), false);

    store.loadSession(http);

    assertEquals(http.sessionLoaded(), true);
  }

  @Test
  public void ensureSession01() {
    final HttpSessionStore store;
    store = create(options -> {
      options.sessionGenerator(generator(1L, 2L, 3L, 4L));
    });

    final HttpExchange http;
    http = HttpExchange.create0(config -> {});

    assertEquals(http.sessionLoaded(), false);

    store.ensureSession(http);

    assertEquals(http.sessionLoaded(), true);
  }

  @Test
  public void ensureSession02() {
    final HttpSessionStore store;
    store = create(options -> {
      options.sessionGenerator(generator(1L, 2L, 3L, 4L));
    });

    final HttpExchange http;
    http = HttpExchange.create0(config -> {});

    assertEquals(http.sessionLoaded(), false);

    store.ensureSession(http);

    assertEquals(http.sessionLoaded(), true);

    http.sessionAttr(String.class, () -> "MARKER");

    store.ensureSession(http);

    assertEquals(http.sessionLoaded(), true);

    assertEquals(http.sessionAttr(String.class), "MARKER");
  }

  @Test
  public void requireCsrfToken01() {
    final HttpSessionStore store;
    store = create(options -> {
      options.csrfGenerator(generator(5L, 6L, 7L, 8L));

      options.sessionGenerator(generator(1L, 2L, 3L, 4L));
    });

    store.createSession();

    final HttpExchange http;
    http = HttpExchange.create0(config -> {
      config.method(Http.Method.POST);

      config.header(Http.HeaderName.COOKIE, cookie("OBJECTOSWAY", 1L, 2L, 3L, 4L));

      final HttpToken token;
      token = HttpToken.of32(5L, 6L, 7L, 8L);

      config.header(Http.HeaderName.WAY_CSRF_TOKEN, token.toString());
    });

    store.loadSession(http);

    store.requireCsrfToken(http);

    assertFalse(http.processed());
  }

  @Test
  public void requireCsrfToken02() {
    final HttpSessionStore store;
    store = create(options -> {
      options.csrfGenerator(generator(5L, 6L, 7L, 8L));

      options.sessionGenerator(generator(1L, 2L, 3L, 4L));
    });

    store.createSession();

    final HttpExchange http;
    http = HttpExchange.create0(config -> {
      config.clock(Y.clockFixed());

      config.method(Http.Method.POST);

      config.header(Http.HeaderName.COOKIE, cookie("OBJECTOSWAY", 1L, 2L, 3L, 4L));
    });

    store.loadSession(http);

    store.requireCsrfToken(http);

    assertTrue(http.processed());
    assertEquals(http.toString(), """
    HTTP/1.1 403 Forbidden\r
    Date: Wed, 28 Jun 2023 12:08:43 GMT\r
    Content-Type: text/plain; charset=utf-8\r
    Content-Length: 30\r
    Set-Cookie: OBJECTOSWAY=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=; HttpOnly; Path=/; Secure\r
    \r
    Invalid or missing CSRF token
    """);
  }

  @Test
  public void requireCsrfToken03() {
    final HttpSessionStore store;
    store = create(options -> {
      options.csrfGenerator(generator(5L, 6L, 7L, 8L));

      options.sessionGenerator(generator(1L, 2L, 3L, 4L));
    });

    store.createSession();

    final HttpExchange http;
    http = HttpExchange.create0(config -> {
      config.clock(Y.clockFixed());

      config.method(Http.Method.POST);

      config.header(Http.HeaderName.COOKIE, cookie("OBJECTOSWAY", 1L, 2L, 3L, 4L));

      final HttpToken token;
      token = HttpToken.of32(5L, 6L, 9L, 9L);

      config.header(Http.HeaderName.WAY_CSRF_TOKEN, token.toString());
    });

    store.loadSession(http);

    store.requireCsrfToken(http);

    assertTrue(http.processed());
    assertEquals(http.toString(), """
    HTTP/1.1 403 Forbidden\r
    Date: Wed, 28 Jun 2023 12:08:43 GMT\r
    Content-Type: text/plain; charset=utf-8\r
    Content-Length: 30\r
    Set-Cookie: OBJECTOSWAY=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=; HttpOnly; Path=/; Secure\r
    \r
    Invalid or missing CSRF token
    """);
  }

  @Test
  public void requireCsrfToken04() {
    final HttpSessionStore store;
    store = create(options -> {
      options.csrfGenerator(generator(5L, 6L, 7L, 8L));

      options.sessionGenerator(generator(1L, 2L, 3L, 4L));
    });

    store.createSession();

    final HttpExchange http;
    http = HttpExchange.create0(config -> {
      config.clock(Y.clockFixed());

      config.method(Http.Method.POST);

      config.header(Http.HeaderName.COOKIE, cookie("OBJECTOSWAY", 1L, 1L, 1L, 1L));

      final HttpToken token;
      token = HttpToken.of32(2L, 2L, 2L, 2L);

      config.header(Http.HeaderName.WAY_CSRF_TOKEN, token.toString());
    });

    store.loadSession(http);

    store.requireCsrfToken(http);

    assertTrue(http.processed());
    assertEquals(http.toString(), """
    HTTP/1.1 403 Forbidden\r
    Date: Wed, 28 Jun 2023 12:08:43 GMT\r
    Content-Type: text/plain; charset=utf-8\r
    Content-Length: 30\r
    \r
    Invalid or missing CSRF token
    """);
  }

  private HttpSessionStore create(Consumer<HttpSessionStoreBuilder> options) {
    final HttpSessionStoreBuilder builder;
    builder = new HttpSessionStoreBuilder();

    options.accept(builder);

    return (HttpSessionStore) builder.build();
  }

  private String cookie(String name, long l0, long l1, long l2, long l3) {
    return Y.cookie(name, l0, l1, l2, l3);
  }

  private RandomGenerator generator(long... values) {
    return Y.randomGeneratorOfLongs(values);
  }

}