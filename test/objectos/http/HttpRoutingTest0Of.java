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

import java.util.function.Consumer;
import objectos.way.Media;
import objectos.way.Y;
import org.testng.annotations.Test;

public class HttpRoutingTest0Of {

  private static final HttpHandler OK = http -> http.ok(Media.Bytes.textPlain("OK\n"));

  @Test
  public void handler01() {
    test(
        routing -> {
          routing.handler(OK);
        },

        http -> {
          http.path("/whatever");
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
  }

  @Test
  public void handler02() {
    test(
        routing -> {
          routing.handler(OK);
        },

        http -> {
          http.path("/whatever");
        },

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 3\r
        \r
        OK
        """,

        http -> {
          http.path("/xpto");
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
  }

  @Test
  public void handler03() {
    test(
        routing -> {
          routing.handler(http -> {
            if ("/foo".equals(http.path())) {
              http.ok(Media.Bytes.textPlain("foo"));
            }
          });

          routing.handler(OK);
        },

        http -> {
          http.path("/foo");
        },

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 3\r
        \r
        foo\
        """,

        http -> {
          http.path("/abc");
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
  }

  @Test
  public void path01() {
    test(
        routing -> {
          routing.path("/foo", foo -> {
            foo.handler(http -> http.ok(Media.Bytes.textPlain("foo")));
          });

          routing.handler(OK);
        },

        http -> {
          http.path("/foo");
        },

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 3\r
        \r
        foo\
        """,

        http -> {
          http.path("/abc");
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
  }

  @Test
  public void path02() {
    test(
        routing -> {
          routing.path("/foo", HttpMethod.GET, http -> http.ok(Media.Bytes.textPlain("foo")));
        },

        http -> {
          http.path("/foo");
        },

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 3\r
        \r
        foo\
        """,

        http -> {
          http.method(HttpMethod.POST);
          http.path("/foo");
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
  public void path03() {
    test(
        routing -> {
          routing.path("/foo", HttpMethod.GET, _ -> {});

          routing.handler(HttpHandler.notFound());
        },

        http -> {
          http.path("/foo");
        },

        """
        HTTP/1.1 404 Not Found\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Length: 0\r
        Connection: close\r
        \r
        """
    );
  }

  private void test(
      Consumer<HttpRoutingImpl> options,
      Consumer<HttpExchange.Options> req1, String resp1) {
    final HttpRoutingImpl routing;
    routing = new HttpRoutingImpl();

    options.accept(routing);

    final HttpHandler handler;
    handler = routing.build();

    final HttpExchange http1;
    http1 = HttpExchange.create(cfg -> {
      cfg.clock(Y.clockFixed());

      req1.accept(cfg);
    });

    handler.handle(http1);

    assertEquals(
        http1.toString(),

        resp1
    );
  }

  private void test(
      Consumer<HttpRoutingImpl> options,
      Consumer<HttpExchange.Options> req1, String resp1,
      Consumer<HttpExchange.Options> req2, String resp2) {
    final HttpRoutingImpl routing;
    routing = new HttpRoutingImpl();

    options.accept(routing);

    final HttpHandler handler;
    handler = routing.build();

    final HttpExchange http1;
    http1 = HttpExchange.create(cfg -> {
      cfg.clock(Y.clockFixed());

      req1.accept(cfg);
    });

    handler.handle(http1);

    assertEquals(
        http1.toString(),

        resp1
    );

    final HttpExchange http2;
    http2 = HttpExchange.create(cfg -> {
      cfg.clock(Y.clockFixed());

      req2.accept(cfg);
    });

    handler.handle(http2);

    assertEquals(
        http2.toString(),

        resp2
    );
  }

}