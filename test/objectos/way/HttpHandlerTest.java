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
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertSame;

import java.nio.charset.StandardCharsets;
import java.util.List;
import org.testng.annotations.Test;

public class HttpHandlerTest {

  private final Lang.MediaObject pass = Lang.MediaObject.textPlain("PASS", StandardCharsets.UTF_8);

  private final Lang.MediaObject skip = Lang.MediaObject.textPlain("SKIP", StandardCharsets.UTF_8);

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

    final Http.TestingExchange http1;
    http1 = Http.TestingExchange.create(config -> {
      config.path("/of04");
    });

    handler.handle(http1);

    assertSame(http1.responseBody(), pass);

    final Http.TestingExchange http2;
    http2 = Http.TestingExchange.create(config -> {
      config.path("/foo");
    });

    handler.handle(http2);

    assertNull(http2.responseBody());
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

    final Http.TestingExchange http1;
    http1 = Http.TestingExchange.create(config -> {
      config.path("/of05");
    });

    handler.handle(http1);

    assertEquals(http1.get(String.class), "OF-05");
    assertSame(http1.responseBody(), pass);

    final Http.TestingExchange http2;
    http2 = Http.TestingExchange.create(config -> {
      config.path("/foo");
    });

    handler.handle(http2);

    assertNull(http2.get(String.class));
    assertNull(http2.responseBody());
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

    final Http.TestingExchange http1;
    http1 = Http.TestingExchange.create(config -> {
      config.path("/of06");
    });

    handler.handle(http1);

    assertEquals(http1.get(String.class), "OF-06");
    assertSame(http1.responseBody(), pass);

    final Http.TestingExchange http2;
    http2 = Http.TestingExchange.create(config -> {
      config.path("/foo");
    });

    handler.handle(http2);

    assertEquals(http2.get(String.class), "OF-06");
    assertSame(http2.responseBody(), pass);
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

    final Http.TestingExchange http;
    http = Http.TestingExchange.create(config -> {
      config.path("/subpath/test01");
    });

    handler.handle(http);

    assertSame(http.responseBody(), pass);
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

    final Http.TestingExchange http1;
    http1 = Http.TestingExchange.create(config -> {
      config.path("/subpath/test02/more");
    });

    handler.handle(http1);

    assertEquals(http1.get(String.class), "SUB-02");
    assertSame(http1.responseBody(), pass);

    final Http.TestingExchange http2;
    http2 = Http.TestingExchange.create(config -> {
      config.path("/subpath/test02/noop");
    });

    handler.handle(http2);

    assertEquals(http2.get(String.class), "SUB-02");
    assertEquals(http2.responseBody(), null);
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

    final Http.TestingExchange http1;
    http1 = Http.TestingExchange.create(config -> {
      config.path("/subpath/test-03");
    });

    handler.handle(http1);

    assertSame(http1.responseBody(), pass);

    final Http.TestingExchange http2;
    http2 = Http.TestingExchange.create(config -> {
      config.path("/subpath/skip-me");
    });

    handler.handle(http2);

    assertEquals(http2.responseBody(), skip);
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

    final Http.TestingExchange http1;
    http1 = Http.TestingExchange.create(config -> {
      config.path("/subpath/test04/more");
    });

    handler.handle(http1);

    assertSame(http1.responseBody(), pass);

    final Http.TestingExchange http2;
    http2 = Http.TestingExchange.create(config -> {
      config.path("/subpath/test04/skip");
    });

    handler.handle(http2);

    assertEquals(http2.responseBody(), skip);
  }

  private Http.Handler decorate(String value) {
    return http -> http.set(String.class, value);
  }

  private Http.Handler ok(Lang.MediaObject object) {
    return http -> http.respond(object);
  }

}