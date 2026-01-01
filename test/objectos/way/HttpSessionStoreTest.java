/*
 * Copyright (C) 2026 Objectos Software LTDA.
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
import static org.testng.Assert.assertTrue;

import java.util.Map;
import java.util.function.Consumer;
import java.util.random.RandomGenerator;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HttpSessionStoreTest {

  @DataProvider
  public Object[][] createCookieNameProvider() {
    return new Object[][] {
        {true, "all valid characters",
            Http.tchar(), ""},

        {false, "empty",
            "", "Cookie name must not be blank"},
        {false, "blank",
            " \t ", "Cookie name must not be blank"},
        {false, "Single invalid char",
            "COOKIE{ID", """
                    Cookie name must only contain the following characters:
                    \t"!" / "#" / "$" / "%" / "&" / "'" / "*" / "+" / "-" / "." / "^" / "_" / "`" / "|" / "~"
                    \tDIGIT (US-ASCII) / ALPHA (US-ASCII)
                    """},
        {false, "Multiple invalid chars",
            "{COOKIE}", """
                    Cookie name must only contain the following characters:
                    \t"!" / "#" / "$" / "%" / "&" / "'" / "*" / "+" / "-" / "." / "^" / "_" / "`" / "|" / "~"
                    \tDIGIT (US-ASCII) / ALPHA (US-ASCII)
                    """},
    };
  }

  @Test(dataProvider = "createCookieNameProvider")
  public void createCookieName(boolean valid, String description, String cookieName, String expectedMessage) {
    try {
      final HttpSessionStore store;
      store = create(opts -> {
        opts.cookieName(cookieName);
      });

      if (!valid) {
        Assert.fail("It should have thrown");
      } else {
        assertNotNull(store);
      }
    } catch (IllegalArgumentException expected) {
      if (valid) {
        Assert.fail("Unexpected exception", expected);
      } else {
        final String message;
        message = expected.getMessage();

        assertEquals(message, expectedMessage);
      }
    }
  }

  @DataProvider
  public Object[][] loadSessionProvider() {
    return new Object[][] {
        // valid
        {true, "1 value",
            "WAY=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ="},
        {true, "2 values, valid first",
            "WAY=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=; other=foo"},
        {true, "2 values, valid second",
            "other=foo; WAY=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ="},
        {true, "2 values, valid second, first value empty",
            "other=; WAY=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ="},
        {true, "2 values, valid second, first empty",
            "=; WAY=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ="},
        {true, "2 values, same name, valid first",
            "WAY=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=; WAY=foo"},
        {true, "2 values, same name, valid second",
            "WAY=foo; WAY=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ="},
        {true, "valid value surrounded by spaces",
            "  WAY=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=  "},
        {true, "2 values, valid second, no space after semicolon",
            "other=foo;WAY=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ="},
        {true, "valid cookie with trailing semicolon",
            "WAY=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=;"},
        {true, "valid cookie with leading semicolon",
            ";WAY=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ="},
        {true, "3 values, valid one in the middle",
            "foo=bar; WAY=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=; baz=qux"},

        // invalid
        {false, "empty header value",
            ""},
        {false, "1 value, empty cookie value",
            "WAY="},
        {false, "1 value, length = length - 1",
            "WAY=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ"},
        {false, "1 value, valid length, invalid value",
            "WAY=XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX="},
        {false, "header is null",
            null},
        {false, "1 value, wrong cookie name",
            "SESSIONID=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ="},
        {false, "1 value, name correct but mixed case",
            "way=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ="},
        {false, "1 value, name correct but extra whitespace",
            "WAY = AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ="},
        {false, "1 value, name correct but tab character between name and equals",
            "WAY\t=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ="},
        {false, "cookie name is substring of correct name",
            "WA=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ="},
        {false, "cookie name starts with correct name",
            "WAY_TOO_LONG=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ="}
    };
  }

  @Test(dataProvider = "loadSessionProvider")
  public void loadSession(boolean present, String description, String headerValue) {
    final HttpToken id;
    id = HttpToken.of32(1, 2, 3, 4);

    final HttpSession session;
    session = new HttpSession(id, Map.of());

    final HttpSessionStore store;
    store = create(opts -> {
      opts.cookieName("WAY");

      opts.session(session);
    });

    final HttpExchange http;
    http = HttpExchange.create0(opts -> {
      if (headerValue != null) {
        opts.header(Http.HeaderName.COOKIE, headerValue);
      }
    });

    assertEquals(http.sessionPresent(), false);

    assertEquals(store.loadSession(http), present);

    assertEquals(http.sessionPresent(), present);
  }

  @DataProvider
  public Object[] loadSessionInvalidProvider() {
    final HttpToken id;
    id = HttpToken.of32(1, 2, 3, 4);

    return new Object[][] {
        {id, "WAY=" + id, "1 value"},
        {id, "WAY=" + id + "; other=foo", "2 values, valid first"},
        {id, "other=foo; WAY=" + id, "2 values, valid second"},
        {id, "other=; WAY=" + id, "2 values, valid second, first value empty"},
        {id, "=; WAY=" + id, "2 values, valid second, first empty"},
        {id, "WAY=" + id + "; WAY=foo", "2 values, same name, valid first"},
        {id, "WAY=foo; WAY=" + id, "2 values, same name, valid second"}
    };
  }

  @Test
  public void ensureSession01() {
    final HttpSessionStore store;
    store = create(options -> {
      options.sessionGenerator(generator(1L, 2L, 3L, 4L));
    });

    final HttpExchange http;
    http = HttpExchange.create0(_ -> {});

    assertEquals(http.sessionPresent(), false);

    store.ensureSession(http);

    assertEquals(http.sessionPresent(), true);
  }

  @Test
  public void ensureSession02() {
    final HttpSessionStore store;
    store = create(options -> {
      options.sessionGenerator(generator(1L, 2L, 3L, 4L));
    });

    final HttpExchange http;
    http = HttpExchange.create0(_ -> {});

    assertEquals(http.sessionPresent(), false);

    store.ensureSession(http);

    assertEquals(http.sessionPresent(), true);

    http.sessionAttr(String.class, "MARKER");

    store.ensureSession(http);

    assertEquals(http.sessionPresent(), true);

    assertEquals(http.sessionAttr(String.class), "MARKER");
  }

  @DataProvider
  public Object[][] safeMethodsProvider() {
    return new Object[][] {
        {Http.Method.GET},
        {Http.Method.HEAD}
    };
  }

  @DataProvider
  public Object[][] unsafeMethodsProvider() {
    return new Object[][] {
        {Http.Method.POST},
        {Http.Method.PUT},
        {Http.Method.PATCH},
        {Http.Method.DELETE}
    };
  }

  @Test(dataProvider = "unsafeMethodsProvider")
  public void requireCsrfToken01(Http.Method method) {
    final HttpSessionStore store;
    store = create(options -> {
      options.csrfGenerator(generator(5L, 6L, 7L, 8L));

      options.sessionGenerator(generator(1L, 2L, 3L, 4L));
    });

    store.createSession();

    final HttpExchange http;
    http = HttpExchange.create0(config -> {
      config.method(method);

      config.header(Http.HeaderName.COOKIE, cookie("OBJECTOSWAY", 1L, 2L, 3L, 4L));

      final HttpToken token;
      token = HttpToken.of32(5L, 6L, 7L, 8L);

      config.header(Http.HeaderName.WAY_CSRF_TOKEN, token.toString());
    });

    store.loadSession(http);

    store.requireCsrfToken(http);

    assertFalse(http.processed());
  }

  @Test(dataProvider = "unsafeMethodsProvider")
  public void requireCsrfToken02(Http.Method method) {
    final HttpSessionStore store;
    store = create(options -> {
      options.csrfGenerator(generator(5L, 6L, 7L, 8L));

      options.sessionGenerator(generator(1L, 2L, 3L, 4L));
    });

    store.createSession();

    final HttpExchange http;
    http = HttpExchange.create0(config -> {
      config.clock(Y.clockFixed());

      config.method(method);

      // valid session
      config.header(Http.HeaderName.COOKIE, cookie("OBJECTOSWAY", 1L, 2L, 3L, 4L));

      // no csrf token
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

  @Test(dataProvider = "unsafeMethodsProvider")
  public void requireCsrfToken03(Http.Method method) {
    final HttpSessionStore store;
    store = create(options -> {
      options.csrfGenerator(generator(5L, 6L, 7L, 8L));

      options.sessionGenerator(generator(1L, 2L, 3L, 4L));
    });

    store.createSession();

    final HttpExchange http;
    http = HttpExchange.create0(config -> {
      config.clock(Y.clockFixed());

      config.method(method);

      // valid session
      config.header(Http.HeaderName.COOKIE, cookie("OBJECTOSWAY", 1L, 2L, 3L, 4L));

      // invalid csrf token
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

  @Test(dataProvider = "unsafeMethodsProvider")
  public void requireCsrfToken04(Http.Method method) {
    final HttpSessionStore store;
    store = create(options -> {
      options.csrfGenerator(generator(5L, 6L, 7L, 8L));

      options.sessionGenerator(generator(1L, 2L, 3L, 4L));
    });

    store.createSession();

    final HttpExchange http;
    http = HttpExchange.create0(config -> {
      config.clock(Y.clockFixed());

      config.method(method);

      // invalid session
      config.header(Http.HeaderName.COOKIE, cookie("OBJECTOSWAY", 1L, 1L, 1L, 1L));

      // invalid csrf token
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

  @Test(dataProvider = "safeMethodsProvider")
  public void requireCsrfToken05(Http.Method method) {
    final HttpSessionStore store;
    store = create(options -> {
      options.csrfGenerator(generator(5L, 6L, 7L, 8L));

      options.sessionGenerator(generator(1L, 2L, 3L, 4L));
    });

    store.createSession();

    final HttpExchange http;
    http = HttpExchange.create0(config -> {
      config.clock(Y.clockFixed());

      config.method(method);

      // valid session
      config.header(Http.HeaderName.COOKIE, cookie("OBJECTOSWAY", 1L, 2L, 3L, 4L));

      // no csrf token
    });

    store.loadSession(http);

    store.requireCsrfToken(http);

    assertFalse(http.processed());
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