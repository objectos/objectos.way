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

import static objectos.way.Http.Method.DELETE;
import static objectos.way.Http.Method.GET;
import static org.testng.Assert.assertEquals;

import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;
import org.testng.annotations.Test;

public class HttpRoutingTest1OfPath {

  private static final Http.Handler OK = http -> http.ok(Media.Bytes.textPlain("OK\n"));

  private static final class ThisFilter implements Http.Filter {
    int count;

    @Override
    public final void filter(Http.Exchange http, Http.Handler handler) {
      count++;

      handler.handle(http);
    }
  }

  @Test
  public void allow01() {
    test(
        routing -> {
          routing.path("/allow01", allow01 -> {
            allow01.allow(GET, ok("GET"));
          });
        },

        http -> http.path("/allow01"),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 3\r
        \r
        GET\
        """,

        http -> {
          http.method(Http.Method.DELETE);

          http.path("/allow01");
        },

        """
        HTTP/1.1 405 Method Not Allowed\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Length: 0\r
        Allow: GET, HEAD\r
        \r
        """
    );
  }

  @Test
  public void allow02() {
    test(
        routing -> {
          routing.path("/allow", allow -> {
            allow.allow(GET, ok("GET"));

            allow.handler(http -> {
              final String foo;
              foo = http.queryParam("foo");

              if (foo != null) {
                http.ok(Media.Bytes.textPlain("foo"));
              }
            });

            allow.allow(DELETE, ok("DEL"));
          });
        },

        round(
            http -> http.path("/allow"),

            """
            HTTP/1.1 200 OK\r
            Date: Wed, 28 Jun 2023 12:08:43 GMT\r
            Content-Type: text/plain; charset=utf-8\r
            Content-Length: 3\r
            \r
            GET\
            """
        ),

        round(
            http -> {
              http.path("/allow");

              http.queryParam("foo", "bar");
            },

            """
            HTTP/1.1 200 OK\r
            Date: Wed, 28 Jun 2023 12:08:43 GMT\r
            Content-Type: text/plain; charset=utf-8\r
            Content-Length: 3\r
            \r
            GET\
            """
        ),

        round(
            http -> {
              http.method(Http.Method.POST);

              http.path("/allow");

              http.queryParam("foo", "bar");
            },

            """
            HTTP/1.1 200 OK\r
            Date: Wed, 28 Jun 2023 12:08:43 GMT\r
            Content-Type: text/plain; charset=utf-8\r
            Content-Length: 3\r
            \r
            foo\
            """
        ),

        round(
            http -> {
              http.method(DELETE);

              http.path("/allow");
            },

            """
            HTTP/1.1 200 OK\r
            Date: Wed, 28 Jun 2023 12:08:43 GMT\r
            Content-Type: text/plain; charset=utf-8\r
            Content-Length: 3\r
            \r
            DEL\
            """
        )
    );
  }

  @Test
  public void allow03() {
    test(
        routing -> {
          routing.path("/allow", allow -> {
            allow.allow(GET, http -> {
              final String foo;
              foo = http.queryParam("foo");

              if (foo != null) {
                http.ok(Media.Bytes.textPlain("foo"));
              }
            }, ok("GET"));
          });
        },

        http -> {
          http.path("/allow");

          http.queryParam("foo", "bar");
        },

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 3\r
        \r
        foo\
        """,

        http -> http.path("/allow"),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 3\r
        \r
        GET\
        """
    );
  }

  @Test(description = "If the handler produces no response, send 204: single handler case")
  public void allow04() {
    test(
        routing -> {
          routing.path("/noop", allow -> {
            allow.allow(GET, http -> {});
          });
        },

        http -> http.path("/noop"),

        """
        HTTP/1.1 204 No Content\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Length: 0\r
        \r
        """
    );
  }

  @Test(description = "If the handler produces no response, send 204: many handlers case")
  public void allow05() {
    test(
        routing -> {
          routing.path("/noop", allow -> {
            allow.allow(GET, http -> {}, http -> {});
          });
        },

        http -> http.path("/noop"),

        """
        HTTP/1.1 204 No Content\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Length: 0\r
        \r
        """
    );
  }

  @Test
  public void subpath01() {
    final HttpRequestMatcher matcher;
    matcher = HttpRequestMatcher.pathWildcard("/app/");

    final HttpRouting.OfPath routing;
    routing = new HttpRouting.OfPath(matcher);

    final Media.Bytes object;
    object = Media.Bytes.textPlain("LOGIN", StandardCharsets.UTF_8);

    routing.subpath("login", path -> {
      path.handler(ok(object));
    });

    routing.handler(Http.Handler.notFound());

    final Http.Handler handler;
    handler = routing.build();

    final Http.Exchange http;
    http = http(config -> {
      config.path("/app/login");
    });

    handler.handle(http);

    assertEquals(
        http.toString(),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 5\r
        \r
        LOGIN\
        """
    );
  }

  @Test
  public void subpath03() {
    final HttpRouting.Of routing;
    routing = new HttpRouting.Of();

    routing.path("/a/*", a -> {
      a.subpath("b", b -> {
        b.handler(OK);
      });

      a.handler(notFound("a\n"));
    });

    final Http.Handler handler;
    handler = routing.build();

    final Http.Exchange http;
    http = http(config -> {
      config.path("/a/b");
    });

    handler.handle(http);

    assertEquals(
        http.toString(),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 3\r
        \r
        OK
        """
    );
  }

  @Test
  public void subpath04() {
    final HttpRouting.Of routing;
    routing = new HttpRouting.Of();

    routing.path("/a/*", a -> {
      a.subpath("b/:id", b -> {
        b.paramDigits("id");

        b.handler(http -> {
          String id = http.pathParam("id");

          http.ok(Media.Bytes.textPlain(id));
        });
      });

      a.handler(notFound("a\n"));
    });

    final Http.Handler handler;
    handler = routing.build();

    final Http.Exchange http;
    http = http(config -> {
      config.path("/a/b/123");
    });

    handler.handle(http);

    assertEquals(
        http.toString(),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 3\r
        \r
        123\
        """
    );
  }

  @Test(description = "filter: path(exact)")
  public void filter01() {
    final ThisFilter filter;
    filter = new ThisFilter();

    test(
        routing -> routing.path("/a/exact", exact -> {
          exact.filter(filter, filtered -> {
            filtered.handler(OK);
          });
        }),

        config -> {
          config.path("/a/exact");
        },

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 3\r
        \r
        OK
        """
    );

    assertEquals(filter.count, 1);
  }

  @Test(description = "filter: path(wildcard)")
  public void filter02() {
    final ThisFilter filter;
    filter = new ThisFilter();

    final HttpRouting.Of routing;
    routing = new HttpRouting.Of();

    routing.path("/a/*", a -> {
      a.filter(filter, filtered -> {
        filtered.handler(OK);
      });
    });

    final Http.Handler handler;
    handler = routing.build();

    final Http.Exchange http1;
    http1 = http(config -> {
      config.path("/a/");
    });

    handler.handle(http1);

    assertEquals(filter.count, 1);

    assertEquals(
        http1.toString(),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 3\r
        \r
        OK
        """
    );

    final Http.Exchange http2;
    http2 = http(config -> {
      config.path("/a/b");
    });

    handler.handle(http2);

    assertEquals(filter.count, 2);

    assertEquals(
        http2.toString(),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 3\r
        \r
        OK
        """
    );
  }

  @Test(description = "filter: path(subpath(exact)")
  public void filter03() {
    final ThisFilter filter;
    filter = new ThisFilter();

    final HttpRouting.Of routing;
    routing = new HttpRouting.Of();

    routing.path("/a/*", a -> {
      a.subpath("b", b -> {
        b.filter(filter, filtered -> {
          filtered.handler(OK);
        });
      });

      a.handler(notFound("a\n"));
    });

    final Http.Handler handler;
    handler = routing.build();

    final Http.Exchange http;
    http = http(config -> {
      config.path("/a/b");
    });

    handler.handle(http);

    assertEquals(filter.count, 1);

    assertEquals(
        http.toString(),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 3\r
        \r
        OK
        """
    );
  }

  @Test(description = "filter: path(wildcard) -> path(exact)")
  public void filter04() {
    final ThisFilter filter;
    filter = new ThisFilter();

    final HttpRouting.Of routing;
    routing = new HttpRouting.Of();

    routing.path("/a/*", a -> {
      a.filter(filter, filtered -> {
        filtered.subpath("b", b -> {
          b.handler(OK);
        });
      });
    });

    final Http.Handler handler;
    handler = routing.build();

    final Http.Exchange http;
    http = http(config -> {
      config.path("/a/b");
    });

    handler.handle(http);

    assertEquals(filter.count, 1);

    assertEquals(
        http.toString(),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 3\r
        \r
        OK
        """
    );
  }

  @Test(description = "filter: path(wildcard) -> path(exact)")
  public void filter05() {
    final ThisFilter filter;
    filter = new ThisFilter();

    final HttpRouting.Of routing;
    routing = new HttpRouting.Of();

    routing.path("/a/*", a -> {
      a.filter(filter, filtered -> {
        filtered.subpath("b", b -> {
          b.handler(OK);
        });

        filtered.handler(notFound("a\n"));
      });
    });

    final Http.Handler handler;
    handler = routing.build();

    final Http.Exchange http;
    http = http(config -> {
      config.path("/a/b");
    });

    handler.handle(http);

    assertEquals(filter.count, 1);

    assertEquals(
        http.toString(),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 3\r
        \r
        OK
        """
    );
  }

  @Test
  public void when01() {
    test(
        routing -> {
          routing.path("/prefix/*", prefix -> {
            prefix.when(req -> req.path().equals("/prefix/a"), matched -> {
              matched.handler(OK);
            });

            prefix.handler(notFound("x"));
          });
        },

        http -> http.path("/prefix/a"),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 3\r
        \r
        OK
        """,

        http -> http.path("/prefix/b"),

        """
        HTTP/1.1 404 Not Found\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 1\r
        \r
        x\
        """
    );
  }

  private Http.Exchange http(Consumer<? super Http.Exchange.Options> outer) {
    return Http.Exchange.create(config -> {
      config.clock(Y.clockFixed());

      outer.accept(config);
    });
  }

  private record Round(Consumer<Http.Exchange.Options> req, String resp) {}

  private void test(
      Consumer<HttpRouting.Of> options,
      Consumer<Http.Exchange.Options> req1, String resp1) {
    test(options, new Round(req1, resp1));
  }

  private void test(
      Consumer<HttpRouting.Of> options,
      Consumer<Http.Exchange.Options> req1, String resp1,
      Consumer<Http.Exchange.Options> req2, String resp2) {
    test(options, new Round(req1, resp1), new Round(req2, resp2));
  }

  private void test(Consumer<HttpRouting.Of> options, Round... rounds) {
    final HttpRouting.Of routing;
    routing = new HttpRouting.Of();

    options.accept(routing);

    final Http.Handler handler;
    handler = routing.build();

    for (Round round : rounds) {
      final Http.Exchange http1;
      http1 = Http.Exchange.create(cfg -> {
        cfg.clock(Y.clockFixed());

        round.req.accept(cfg);
      });

      handler.handle(http1);

      assertEquals(
          http1.toString(),

          round.resp
      );
    }
  }

  private Round round(Consumer<Http.Exchange.Options> req, String resp) {
    return new Round(req, resp);
  }

  private Http.Handler ok(Media.Bytes object) {
    return http -> http.ok(object);
  }

  private Http.Handler ok(String text) {
    return http -> http.ok(Media.Bytes.textPlain(text));
  }

  private Http.Handler notFound(String msg) {
    return http -> http.notFound(Media.Bytes.textPlain(msg));
  }

}