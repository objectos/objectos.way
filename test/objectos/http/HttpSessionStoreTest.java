/*
 * Copyright (C) 2025-2026 Objectos Software LTDA.
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
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.function.Consumer;
import java.util.random.RandomGenerator;
import objectos.way.Y;
import objectos.y.RandomGeneratorY;
import objectox.http.RequestMethodEnum;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@SuppressWarnings("exports")
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
      final HttpSessionStoreImpl store;
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
  public Object[][] safeMethodsProvider() {
    return new Object[][] {
        {RequestMethodEnum.GET},
        {RequestMethodEnum.HEAD}
    };
  }

  @DataProvider
  public Object[][] unsafeMethodsProvider() {
    return new Object[][] {
        {RequestMethodEnum.POST},
        {RequestMethodEnum.PUT},
        {RequestMethodEnum.PATCH},
        {RequestMethodEnum.DELETE}
    };
  }

  @Test(enabled = false, dataProvider = "unsafeMethodsProvider")
  public void requireCsrfToken01(RequestMethodEnum method) {
    final HttpSessionStoreImpl store;
    store = create(options -> {
      options.csrfGenerator(generator(5L, 6L, 7L, 8L));

      options.sessionGenerator(generator(1L, 2L, 3L, 4L));
    });

    store.createSession();

    final HttpExchange http;
    http = HttpExchange.create(config -> {
      config.method(method);

      config.header(HttpHeaderName.COOKIE, cookie("OBJECTOSWAY", 1L, 2L, 3L, 4L));

      final HttpToken token;
      token = HttpToken.of32(5L, 6L, 7L, 8L);

      config.header(HttpHeaderName.WAY_CSRF_TOKEN, token.toString());
    });

    // store.loadSession(http);

    store.requireCsrfToken(http);

    assertFalse(http.processed());
  }

  @Test(enabled = false, dataProvider = "unsafeMethodsProvider")
  public void requireCsrfToken02(RequestMethodEnum method) {
    final HttpSessionStoreImpl store;
    store = create(options -> {
      options.csrfGenerator(generator(5L, 6L, 7L, 8L));

      options.sessionGenerator(generator(1L, 2L, 3L, 4L));
    });

    store.createSession();

    final HttpExchange http;
    http = HttpExchange.create(config -> {
      config.clock(Y.clockFixed());

      config.method(method);

      // valid session
      config.header(HttpHeaderName.COOKIE, cookie("OBJECTOSWAY", 1L, 2L, 3L, 4L));

      // no csrf token
    });

    // store.loadSession(http);

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

  @Test(enabled = false, dataProvider = "unsafeMethodsProvider")
  public void requireCsrfToken03(RequestMethodEnum method) {
    final HttpSessionStoreImpl store;
    store = create(options -> {
      options.csrfGenerator(generator(5L, 6L, 7L, 8L));

      options.sessionGenerator(generator(1L, 2L, 3L, 4L));
    });

    store.createSession();

    final HttpExchange http;
    http = HttpExchange.create(config -> {
      config.clock(Y.clockFixed());

      config.method(method);

      // valid session
      config.header(HttpHeaderName.COOKIE, cookie("OBJECTOSWAY", 1L, 2L, 3L, 4L));

      // invalid csrf token
      final HttpToken token;
      token = HttpToken.of32(5L, 6L, 9L, 9L);

      config.header(HttpHeaderName.WAY_CSRF_TOKEN, token.toString());
    });

    // store.loadSession(http);

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

  @Test(enabled = false, dataProvider = "unsafeMethodsProvider")
  public void requireCsrfToken04(RequestMethodEnum method) {
    final HttpSessionStoreImpl store;
    store = create(options -> {
      options.csrfGenerator(generator(5L, 6L, 7L, 8L));

      options.sessionGenerator(generator(1L, 2L, 3L, 4L));
    });

    store.createSession();

    final HttpExchange http;
    http = HttpExchange.create(config -> {
      config.clock(Y.clockFixed());

      config.method(method);

      // invalid session
      config.header(HttpHeaderName.COOKIE, cookie("OBJECTOSWAY", 1L, 1L, 1L, 1L));

      // invalid csrf token
      final HttpToken token;
      token = HttpToken.of32(2L, 2L, 2L, 2L);

      config.header(HttpHeaderName.WAY_CSRF_TOKEN, token.toString());
    });

    // store.loadSession(http);

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

  @Test(enabled = false, dataProvider = "safeMethodsProvider")
  public void requireCsrfToken05(RequestMethodEnum method) {
    final HttpSessionStoreImpl store;
    store = create(options -> {
      options.csrfGenerator(generator(5L, 6L, 7L, 8L));

      options.sessionGenerator(generator(1L, 2L, 3L, 4L));
    });

    store.createSession();

    final HttpExchange http;
    http = HttpExchange.create(config -> {
      config.clock(Y.clockFixed());

      config.method(method);

      // valid session
      config.header(HttpHeaderName.COOKIE, cookie("OBJECTOSWAY", 1L, 2L, 3L, 4L));

      // no csrf token
    });

    // store.loadSession(http);

    store.requireCsrfToken(http);

    assertFalse(http.processed());
  }

  private HttpSessionStoreImpl create(Consumer<HttpSessionStoreBuilder> options) {
    final HttpSessionStoreBuilder builder;
    builder = new HttpSessionStoreBuilder();

    options.accept(builder);

    return (HttpSessionStoreImpl) builder.build();
  }

  private String cookie(String name, long l0, long l1, long l2, long l3) {
    return HttpY.cookie(name, l0, l1, l2, l3);
  }

  private RandomGenerator generator(long... values) {
    return RandomGeneratorY.ofLongs(values);
  }

}