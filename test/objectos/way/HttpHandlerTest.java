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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Consumer;
import org.testng.annotations.Test;

public class HttpHandlerTest {

  private final Media.Bytes pass = Media.Bytes.textPlain("PASS", StandardCharsets.UTF_8);

  private final Media.Bytes skip = Media.Bytes.textPlain("SKIP", StandardCharsets.UTF_8);

  @Test
  public void of01() {
    final Http.Handler handler;
    handler = HttpHandler.of(null, null);

    assertSame(handler, HttpHandler.NOOP);
  }

  @Test
  public void of02() {
    final Http.Handler handler;
    handler = HttpHandler.of(null, List.of());

    assertSame(handler, HttpHandler.NOOP);
  }

  @Test
  public void of03() {
    final Http.Handler single;
    single = ok(pass);

    final Http.Handler handler;
    handler = HttpHandler.of(null, List.of(single));

    assertSame(handler, single);
  }

  @Test
  public void of04() {
    final Http.Handler handler;
    handler = HttpHandler.of(
        HttpRequestMatcher.pathExact("/of04"),

        List.of(ok(pass))
    );

    test(
        handler,

        http(config -> {
          config.path("/of04");
        }),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 4\r
        \r
        PASS\
        """
    );

    test(
        handler,

        http(config -> {
          config.path("/foo");
        }),

        ""
    );
  }

  @Test
  public void of05() {
    final Http.Handler handler;
    handler = HttpHandler.of(
        HttpRequestMatcher.pathExact("/of05"),

        List.of(
            decorate("OF-05"),

            ok(pass)
        )
    );

    test(
        handler,

        http(config -> {
          config.path("/of05");
        }),

        http -> {
          assertEquals(http.get(String.class), "OF-05");
        },

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 4\r
        \r
        PASS\
        """
    );

    test(
        handler,

        http(config -> {
          config.path("/foo");
        }),

        http -> {
          assertEquals(http.get(String.class), null);
        },

        ""
    );
  }

  @Test
  public void of06() {
    final Http.Handler handler;
    handler = HttpHandler.of(
        null,

        List.of(
            decorate("OF-06"),

            ok(pass)
        )
    );

    test(
        handler,

        http(config -> {
          config.path("/of06");
        }),

        http -> {
          assertEquals(http.get(String.class), "OF-06");
        },

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 4\r
        \r
        PASS\
        """
    );

    test(
        handler,

        http(config -> {
          config.path("/foo");
        }),

        http -> {
          assertEquals(http.get(String.class), "OF-06");
        },

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 4\r
        \r
        PASS\
        """
    );
  }

  @Test
  public void filter01() {
    final Http.Handler handler;
    handler = HttpHandler.filter(
        HttpRequestMatcher.pathWildcard("/filter/"),

        (http, chain) -> {
          http.set(String.class, "TC01");

          chain.handle(http);

          if (!http.processed()) {
            http.set(Integer.class, 1);
          }
        },

        HttpHandler.ofSubpath(
            HttpRequestMatcher.pathExact("test01"),

            List.of(
                ok(pass)
            )
        )
    );

    test(
        handler,

        http(config -> {
          config.path("/filter/test01");
        }),

        http -> {
          assertEquals(http.get(Integer.class), null);
          assertEquals(http.get(String.class), "TC01");
        },

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 4\r
        \r
        PASS\
        """
    );

    test(
        handler,

        http(config -> {
          config.path("/filter/not");
        }),

        http -> {
          assertEquals(http.get(Integer.class), 1);
          assertEquals(http.get(String.class), "TC01");
        },

        ""
    );
  }

  @Test
  public void movedPermanently01() {
    final Http.Handler handler;
    handler = Http.Handler.movedPermanently("/foo");

    test(
        handler,

        http(config -> {
          config.path("/test");
        }),

        """
        HTTP/1.1 301 Moved Permanently\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Length: 0\r
        Location: /foo\r
        \r
        """
    );
  }

  @Test
  public void ok01() {
    final Http.Handler handler;
    handler = Http.Handler.ok(Media.Bytes.textPlain("1\n"));

    test(
        handler,

        http(config -> {
          config.path("/test");
        }),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 2\r
        \r
        1
        """
    );
  }

  @Test
  public void ok02() {
    final Media.Text text;
    text = Y.mediaTextOfLength(64);

    final Http.Handler handler;
    handler = Http.Handler.ok(text);

    test(
        handler,

        http(config -> {
          config.path("/test");
        }),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Transfer-Encoding: chunked\r
        \r
        .................................................
        12345678901234\
        """
    );
  }

  @Test
  public void subpath01() {
    final Http.Handler handler;
    handler = HttpHandler.single(
        HttpRequestMatcher.pathWildcard("/subpath/"),

        HttpHandler.ofSubpath(
            HttpRequestMatcher.pathExact("test01"),

            List.of(
                ok(pass)
            )
        )
    );

    test(
        handler,

        http(config -> {
          config.path("/subpath/test01");
        }),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 4\r
        \r
        PASS\
        """
    );
  }

  @Test
  public void subpath02() {
    final Http.Handler handler;
    handler = HttpHandler.single(
        HttpRequestMatcher.pathWildcard("/subpath/"),

        HttpHandler.ofSubpath(
            HttpRequestMatcher.pathWildcard("test02/"),

            List.of(
                decorate("SUB-02"),

                HttpHandler.ofSubpath(
                    HttpRequestMatcher.pathExact("more"),

                    List.of(ok(pass))
                )
            )
        )
    );

    test(
        handler,

        http(config -> {
          config.path("/subpath/test02/more");
        }),

        http -> {
          assertEquals(http.get(String.class), "SUB-02");
        },

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 4\r
        \r
        PASS\
        """
    );

    test(
        handler,

        http(config -> {
          config.path("/subpath/test02/noop");
        }),

        http -> {
          assertEquals(http.get(String.class), "SUB-02");
        },

        ""
    );
  }

  @Test
  public void subpath03() {
    final Http.Handler handler;
    handler = HttpHandler.of(
        HttpRequestMatcher.pathWildcard("/subpath/"),

        List.of(
            HttpHandler.ofSubpath(
                HttpRequestMatcher.pathExact("skip-me"),

                List.of(
                    ok(skip)
                )
            ),

            HttpHandler.ofSubpath(
                HttpRequestMatcher.pathExact("test-03"),

                List.of(
                    ok(pass)
                )
            )
        )
    );

    test(
        handler,

        http(config -> {
          config.path("/subpath/test-03");
        }),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 4\r
        \r
        PASS\
        """
    );

    test(
        handler,

        http(config -> {
          config.path("/subpath/skip-me");
        }),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 4\r
        \r
        SKIP\
        """
    );
  }

  @Test
  public void subpath04() {
    final Http.Handler handler;
    handler = HttpHandler.single(
        HttpRequestMatcher.pathWildcard("/subpath/"),

        HttpHandler.ofSubpath(
            HttpRequestMatcher.pathWildcard("test04/"),

            List.of(
                HttpHandler.ofSubpath(
                    HttpRequestMatcher.pathExact("skip"),

                    List.of(ok(skip))
                ),

                HttpHandler.ofSubpath(
                    HttpRequestMatcher.pathExact("more"),

                    List.of(ok(pass))
                )
            )
        )
    );

    test(
        handler,

        http(config -> {
          config.path("/subpath/test04/more");
        }),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 4\r
        \r
        PASS\
        """
    );

    test(
        handler,

        http(config -> {
          config.path("/subpath/test04/skip");
        }),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 4\r
        \r
        SKIP\
        """
    );
  }

  private Http.Exchange http(Consumer<? super Http.Exchange.Options> outer) {
    return Http.Exchange.create(config -> {
      config.clock(Y.clockFixed());

      outer.accept(config);
    });
  }

  private Http.Handler decorate(String value) {
    return http -> http.set(String.class, value);
  }

  private Http.Handler ok(Media.Bytes object) {
    return http -> http.ok(object);
  }

  private void test(Http.Handler handler, Http.Exchange http, String expected) {
    handler.handle(http);

    if (expected.isEmpty()) {
      assertEquals(http.processed(), false);
    } else {
      assertEquals(http.processed(), true);

      assertEquals(http.toString(), expected);
    }
  }

  private void test(Http.Handler handler,
      Http.Exchange http,
      Consumer<Http.Exchange> afterListener,
      String expected) {
    handler.handle(http);

    afterListener.accept(http);

    if (expected.isEmpty()) {
      assertEquals(http.processed(), false);
    } else {
      assertEquals(http.processed(), true);

      assertEquals(http.toString(), expected);
    }
  }

}