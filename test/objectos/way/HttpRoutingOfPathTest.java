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

import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;
import org.testng.annotations.Test;

public class HttpRoutingOfPathTest {

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

  @Test(description = "filter: path(exact)")
  public void filter01() {
    final ThisFilter filter;
    filter = new ThisFilter();

    final HttpRouting.Of routing;
    routing = new HttpRouting.Of();

    routing.path("/a/exact", exact -> {
      exact.filter(filter);

      exact.handler(OK);
    });

    final Http.Handler handler;
    handler = routing.build();

    final Http.Exchange http;
    http = http(config -> {
      config.path("/a/exact");
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

  @Test(description = "filter: path(wildcard)")
  public void filter02() {
    final ThisFilter filter;
    filter = new ThisFilter();

    final HttpRouting.Of routing;
    routing = new HttpRouting.Of();

    routing.path("/a/*", a -> {
      a.filter(filter);

      a.handler(OK);
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
        b.filter(filter);

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
      a.filter(filter);

      a.subpath("b", b -> {
        b.handler(OK);
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
      a.filter(filter);

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

  private Http.Exchange http(Consumer<? super Http.Exchange.Options> outer) {
    return Http.Exchange.create(config -> {
      config.clock(Y.clockFixed());

      outer.accept(config);
    });
  }

  private Http.Handler ok(Media.Bytes object) {
    return http -> http.ok(object);
  }

  private Http.Handler notFound(String msg) {
    return http -> http.notFound(Media.Bytes.textPlain(msg));
  }

}