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

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Stream;
import objectos.y.SocketY;
import objectox.http.RequestMethodEnum;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@SuppressWarnings("exports")
public class HttpRoutesTest {

  private final HttpHandler ok = http -> { http.status(HttpStatus.OK); http.send(); };
  private final HttpHandler notFound = http -> { http.status(HttpStatus.NOT_FOUND); http.send(); };

  @Test(description = "Handlers should be applied in declaration order")
  public void handler02() {
    final AtomicInteger counter;
    counter = new AtomicInteger(1);

    class ThisHandler implements HttpHandler {
      int id;

      @Override
      public final void handle(HttpExchange http) {
        id = counter.getAndIncrement();
      }
    }

    var h1 = new ThisHandler();
    var h2 = new ThisHandler();
    var h3 = new ThisHandler();

    test(
        r -> {
          r.at("/test", h1, h2, h3);
        },

        """
        GET /test HTTP/1.1\r
        Host: www.example.com\r
        \r
        """,

        """
        HTTP/1.1 404 Not Found\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Connection: close\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 14\r
        \r
        404 Not Found
        """
    );

    assertEquals(h1.id, 1);
    assertEquals(h2.id, 2);
    assertEquals(h3.id, 3);
  }

  @Test(description = "Handler should not be applied on a processed exchange")
  public void handler03() {
    final AtomicInteger counter;
    counter = new AtomicInteger(1);

    class ThisHandler implements HttpHandler {
      int id;

      @Override
      public final void handle(HttpExchange http) {
        id = counter.getAndIncrement();

        if (id == 2) {
          http.status(HttpStatus.OK);
          http.send();
        }
      }
    }

    var h1 = new ThisHandler();
    var h2 = new ThisHandler();
    var h3 = new ThisHandler();

    test(
        r -> {
          r.at("/test", h1, h2, h3);
        },

        """
        GET /test HTTP/1.1\r
        Host: www.example.com\r
        \r
        """,

        """
        HTTP/1.1 200 OK\r
        \r
        """
    );

    assertEquals(h1.id, 1);
    assertEquals(h2.id, 2);
    assertEquals(h3.id, 0);
  }

  @DataProvider
  public Iterator<RequestMethodEnum> methodProvider() {
    return Stream.of(RequestMethodEnum.VALUES).filter(m -> m.implemented).iterator();
  }

  @Test(
      description = "Method HttpHandler should reject other methods",
      dataProvider = "methodProvider"
  )
  public void method01(RequestMethodEnum method) {
    test(
        r -> {
          r.at("/test", Http.POST, ok);
        },

        """
        %s /test HTTP/1.1\r
        Host: www.example.com\r
        \r
        """.formatted(method.name()),

        switch (method) {
          case POST -> """
              HTTP/1.1 200 OK\r
              \r
              """;

          default -> """
              HTTP/1.1 405 Method Not Allowed\r
              Date: Wed, 28 Jun 2023 12:08:43 GMT\r
              Connection: close\r
              Content-Length: 0\r
              Allow: POST\r
              \r
              """;
        }
    );
  }

  private static final HttpHeaderName TEST = HttpHeaderName.of("Way-Test");

  @Test(
      description = "Method HttpHandler: allow declaring multiple methods",
      dataProvider = "methodProvider"
  )
  public void method02(RequestMethodEnum method) {
    record ThisHandler(String value) implements HttpHandler {
      @Override
      public void handle(HttpExchange http) {
        http.status(HttpStatus.OK);
        http.header(TEST, value);
        http.send();
      }
    }

    var get = new ThisHandler("get");
    var post = new ThisHandler("post");

    test(
        r -> {
          r.at("/test", Http.GET, get, Http.POST, post);
        },

        """
        %s /test HTTP/1.1\r
        Host: www.example.com\r
        \r
        """.formatted(method.name()),

        switch (method) {
          case GET, HEAD -> """
              HTTP/1.1 200 OK\r
              Way-Test: get\r
              \r
              """;

          case POST -> """
              HTTP/1.1 200 OK\r
              Way-Test: post\r
              \r
              """;

          default -> """
              HTTP/1.1 405 Method Not Allowed\r
              Date: Wed, 28 Jun 2023 12:08:43 GMT\r
              Connection: close\r
              Content-Length: 0\r
              Allow: GET, HEAD, POST\r
              \r
              """;
        }
    );
  }

  @Test(description = "Method HttpHandler: allow multiple handlers for each method")
  public void method03() {
    final AtomicInteger counter;
    counter = new AtomicInteger(1);

    class ThisHandler implements HttpHandler {
      int id;
      final String value;

      ThisHandler(String value) {
        this.value = value;
      }

      @Override
      public void handle(HttpExchange http) {
        id = counter.getAndIncrement();

        if (id == 3) {
          http.status(HttpStatus.OK);
          http.header(TEST, value);
          http.send();
        }
      }
    }

    var get1 = new ThisHandler("get");
    var get2 = new ThisHandler("get");
    var get3 = new ThisHandler("get");
    var post = new ThisHandler("post");

    test(
        r -> {
          r.at("/test", Http.GET, get1, get2, get3, Http.POST, post);
        },

        """
        GET /test HTTP/1.1\r
        Host: www.example.com\r
        \r
        """,

        """
        HTTP/1.1 200 OK\r
        Way-Test: get\r
        \r
        """
    );

    assertEquals(get1.id, 1);
    assertEquals(get2.id, 2);
    assertEquals(get3.id, 3);
  }

  @DataProvider
  public Object[][] pathExactProvider() {
    return new Object[][] {
        {"/test", true},
        {"/t%65st", true},

        {"/tes", false},
        {"/test/", false},
        {"/testt", false}
    };
  }

  @Test(dataProvider = "pathExactProvider")
  public void path01(String path, boolean resp200) {
    test(
        r -> {
          r.at("/test", Http.GET, ok);
        },

        """
        GET %s HTTP/1.1\r
        Host: www.example.com\r
        \r
        """.formatted(path),

        resp200
            ? """
              HTTP/1.1 200 OK\r
              \r
              """
            : """
              HTTP/1.1 404 Not Found\r
              Date: Wed, 28 Jun 2023 12:08:43 GMT\r
              Connection: close\r
              Content-Type: text/plain; charset=utf-8\r
              Content-Length: 14\r
              \r
              404 Not Found
              """
    );
  }

  @DataProvider
  public Object[][] pathParamProvider() {
    return new Object[][] {
        {
            "/test/{p1}", "/test/",
            """
            Way-Test: p1=\r
            Way-Test: p2=null\r
            """
        },
        {
            "/test/{p1}", "/test/foo",
            """
            Way-Test: p1=foo\r
            Way-Test: p2=null\r
            """
        },
        {
            "/test/{p1}", "/test/foo/bar",
            """
            Way-Test: p1=foo/bar\r
            Way-Test: p2=null\r
            """
        },
        {
            "/test/{p1}/", "/test/foo/",
            """
            Way-Test: p1=foo\r
            Way-Test: p2=null\r
            """
        },
        {
            "/test/{p1}/more/{p2}", "/test/foo/more/bar",
            """
            Way-Test: p1=foo\r
            Way-Test: p2=bar\r
            """
        }
    };
  }

  @Test(dataProvider = "pathParamProvider")
  public void pathParam01(String expression, String actual, String expected) {
    test(
        r -> {
          r.at(expression, Http.GET, Http.handler(http -> {
            http.status(HttpStatus.OK);

            http.header(TEST, "p1=" + http.pathParam("p1"));
            http.header(TEST, "p2=" + http.pathParam("p2"));

            http.send();
          }));
        },

        """
        GET %s HTTP/1.1\r
        Host: www.example.com\r
        \r
        """.formatted(actual),

        """
        HTTP/1.1 200 OK\r
        %s\
        \r
        """.formatted(expected)
    );
  }

  @DataProvider
  public Object[][] pathParam02Provider() {
    return new Object[][] {
        {"/test/{id}", opts(Http.pathParam("id", PathParams.digits()), ok), "/test/123", true},
        {"/test/{id}", opts(Http.pathParam("id", PathParams.digits()), ok), "/test/", false},
        {"/test/{id}", opts(Http.pathParam("id", PathParams.digits()), ok), "/test/abc", false}
    };
  }

  private HttpRoutes.Option[] opts(HttpRoutes.Option... arr) {
    return arr;
  }

  @Test(dataProvider = "pathParam02Provider")
  public void pathParam02(String expression, HttpRoutes.Option[] options, String path, boolean resp200) {
    test(
        r -> {
          r.at(expression, Http.GET, options);
          r.at("/test/{}", Http.GET, notFound);
        },

        """
        GET %s HTTP/1.1\r
        Host: www.example.com\r
        \r
        """.formatted(path),

        resp200 ? """
        HTTP/1.1 200 OK\r
        \r
        """ : """
        HTTP/1.1 404 Not Found\r
        \r
        """
    );
  }

  private void test(Consumer<? super HttpRoutes> routes, String req, String resp) {
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = SocketY.of(req);

          opts.handler = HttpHandler.create(routes);
        }),

        resp
    );
  }

}
