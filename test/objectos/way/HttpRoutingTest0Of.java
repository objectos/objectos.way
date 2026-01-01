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
package objectos.way;

import static org.testng.Assert.assertEquals;

import java.util.function.Consumer;
import org.testng.annotations.Test;

public class HttpRoutingTest0Of {

  private static final Http.Handler OK = http -> http.ok(Media.Bytes.textPlain("OK\n"));

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
          routing.path("/foo", Http.Method.GET, http -> http.ok(Media.Bytes.textPlain("foo")));
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
          http.method(Http.Method.POST);
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
          routing.path("/{}", Http.Method.GET, _ -> {});
        },

        http -> {
          http.path("/foo");
        },

        """
        HTTP/1.1 204 No Content\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Length: 0\r
        \r
        """
    );
  }

  private void test(
      Consumer<HttpRouting> options,
      Consumer<Http.Exchange.Options> req1, String resp1) {
    final HttpRouting routing;
    routing = new HttpRouting();

    options.accept(routing);

    final Http.Handler handler;
    handler = routing.build();

    final Http.Exchange http1;
    http1 = Http.Exchange.create(cfg -> {
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
      Consumer<HttpRouting> options,
      Consumer<Http.Exchange.Options> req1, String resp1,
      Consumer<Http.Exchange.Options> req2, String resp2) {
    final HttpRouting routing;
    routing = new HttpRouting();

    options.accept(routing);

    final Http.Handler handler;
    handler = routing.build();

    final Http.Exchange http1;
    http1 = Http.Exchange.create(cfg -> {
      cfg.clock(Y.clockFixed());

      req1.accept(cfg);
    });

    handler.handle(http1);

    assertEquals(
        http1.toString(),

        resp1
    );

    final Http.Exchange http2;
    http2 = Http.Exchange.create(cfg -> {
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