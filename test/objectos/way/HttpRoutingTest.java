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
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import java.io.IOException;
import java.net.Socket;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.UnaryOperator;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class HttpRoutingTest implements Http.Routing.Module {

  private record Box(String value) {}

  private record User(String login) {}

  @SuppressWarnings("serial")
  private static class TestException extends RuntimeException {}

  private String cookie;

  private Http.SessionStore sessionStore;

  @BeforeClass
  public void beforeClass() {
    this.sessionStore = Http.SessionStore.create(config -> {
      config.cookieName("HTTPMODULETEST");

      config.sessionGenerator(Y.randomGeneratorOfLongs(1L, 2L, 3L, 4L));
    });

    cookie = Y.cookie("HTTPMODULETEST", 1L, 2L, 3L, 4L);

    final Http.Exchange http;
    http = Http.Exchange.create(opts -> {});

    sessionStore.ensureSession(http);

    http.sessionAttr(User.class, new User("test"));

    TestingHttpServer.bindHttpRoutingTest(this);
  }

  @Override
  public final void configure(Http.Routing routing) {
    routing.when(this::notAuthenticated, matched -> {
      // matches: /testCase01/foo
      // but not: /testCase01, /testCase01/, /testCase01/foo/bar
      matched.path("/testCase01/{text}", path -> {
        path.paramNotEmpty("text");

        path.handler(this::$testCase01);
      });

      matched.path("/testCase13", path -> {
        path.allow(Http.Method.GET, this::$testCase13);

        path.allow(Http.Method.POST, this::$testCase13);
      });

      matched.path("/testCase14/{}", tc14 -> {
        tc14.subpath("a", path -> {
          path.allow(Http.Method.GET, this::$testCase14);
        });

        tc14.handler(Http.Handler.notFound());
      });

      matched.path("/testCase15/{}", tc15 -> {
        tc15.filter(this::$testCase15Filter, filtered -> {
          filtered.subpath("a", a -> {
            a.allow(Http.Method.GET, this::$testCase15);
          });
        });
      });

      // redirect non-authenticated requests
      matched.handler(this::testCase02);
    });

    routing.when(this::authenticated, matched -> {
      // matches: /testCase03, /testCase03/foo, /testCase03/foo/bar
      matched.path("/testCase03", path -> {
        path.handler(this::testCase03);
      });
      matched.path("/testCase03/{}", path -> {
        path.handler(this::testCase03);
      });

      // matches: /testCase04, /testCase04/foo, /testCase04/foo/bar
      matched.install(new TestCase04());

      // matches: /testCase05/img, /testCase05/img/, /testCase05/img/a, /testCase05/img/b
      matched.path("/testCase05/img", path -> {
        path.handler(this::testCase05);
      });
      matched.path("/testCase05/img/{}", path -> {
        path.handler(this::testCase05);
      });

      // matches: /testCase06/, /testCase06/foo, /testCase06/foo/bar
      // but not: /testCase06
      matched.path("/testCase06/{}", path -> {
        path.handler(this::$testCase06);
      });

      // tc07: interceptMatched
      matched.path("/testCase07/before", path -> {
        path.handler(this::$testCase07);
      });

      matched.path("/testCase07/after", path -> {
        final Http.Handler testCase07;
        testCase07 = testCase07(this::$testCase07);

        path.handler(testCase07);
      });

      // tc08: install
      matched.install(new TestCase08());

      // tc09: notEmpty, digits
      matched.path("/testCase09/{notEmpty}/{digits}", path -> {
        path.paramNotEmpty("notEmpty");

        path.paramDigits("digits");

        path.handler(this::$testCase09);
      });

      // tc10: regex
      matched.path("/testCase10/{regex}", path -> {
        path.paramRegex("regex", "[0-9a-z]+");

        path.handler(this::$testCase10);
      });

      // tc11: multiple handlers
      matched.path("/testCase11", path -> {
        path.handler(Http.Handler.noop());

        path.handler(this::$testCase11);

        path.handler(http -> http.ok(Media.Bytes.textPlain("nonono\n")));
      });

      // tc12: interceptor
      matched.path("/testCase12", path -> {
        path.handler(http -> {
          assertNull(http.get(String.class));

          http.set(Integer.class, 123);
        });

        UnaryOperator<Http.Handler> tc12X = handler -> {
          return http -> {
            http.set(String.class, "ABC");

            handler.handle(http);
          };
        };

        Http.Handler tc12C = http -> {
          String s = http.get(String.class);
          Integer i = http.get(Integer.class);
          Media.Bytes object = Media.Bytes.textPlain("tc12=" + s + "-" + i.toString(), StandardCharsets.UTF_8);
          http.ok(object);
        };

        path.handler(tc12X.apply(tc12C));
      });
    });

    routing.handler(Http.Handler.notFound());
  }

  private boolean notAuthenticated(Http.Exchange http) {
    sessionStore.loadSession(http);

    return !authenticated(http);
  }

  private boolean authenticated(Http.Exchange http) {
    if (!http.sessionPresent()) {
      return false;
    }

    final User user;
    user = http.sessionAttr(User.class);

    return user != null;
  }

  private void $testCase01(Http.Exchange http) {
    String text;
    text = http.pathParam("text");

    TestingSingleParagraph html;
    html = new TestingSingleParagraph(text);

    http.ok(html);
  }

  @Test
  public void testCase01() throws IOException, InterruptedException {
    HttpResponse<String> response;
    response = Y.httpClient(
        "/testCase01/foo",

        builder -> builder.headers(
            "Host", "http.module.test"
        )
    );

    assertEquals(response.statusCode(), 200);
    assertEquals(response.headers().allValues("Content-Type"), List.of("text/html; charset=utf-8"));
    assertEquals(response.headers().allValues("Date"), List.of("Wed, 28 Jun 2023 12:08:43 GMT"));
    assertEquals(response.body(), """
    <html>
    <p>foo</p>
    </html>
    """);

    response = Y.httpClient(
        "/testCase01",

        builder -> builder.headers(
            "Host", "http.module.test",
            "Cookie", cookie
        )
    );

    assertEquals(response.statusCode(), 404);
    assertEquals(response.headers().allValues("Connection"), List.of("close"));
    assertEquals(response.headers().allValues("Date"), List.of("Wed, 28 Jun 2023 12:08:43 GMT"));

    response = Y.httpClient(
        "/testCase01/",

        builder -> builder.headers(
            "Host", "http.module.test",
            "Cookie", cookie
        )
    );

    assertEquals(response.statusCode(), 404);
    assertEquals(response.headers().allValues("Connection"), List.of("close"));
    assertEquals(response.headers().allValues("Date"), List.of("Wed, 28 Jun 2023 12:08:43 GMT"));

    response = Y.httpClient(
        "/testCase01/foo/bar",

        builder -> builder.headers(
            "Host", "http.module.test",
            "Cookie", cookie
        )
    );

    assertEquals(response.statusCode(), 404);
    assertEquals(response.headers().allValues("Connection"), List.of("close"));
    assertEquals(response.headers().allValues("Date"), List.of("Wed, 28 Jun 2023 12:08:43 GMT"));
  }

  private void testCase02(Http.Exchange http) {
    User user;
    user = null;

    if (http.sessionPresent()) {
      user = http.sessionAttr(User.class);
    }

    if (user == null) {
      http.found("/login");
    }
  }

  @Test
  public void testCase02() throws IOException {
    try (Socket socket = newSocket()) {
      test(socket,
          """
          GET /testCase02/foo HTTP/1.1\r
          Host: http.module.test\r
          \r
          """,

          """
          HTTP/1.1 302 Found\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Content-Length: 0\r
          Location: /login\r
          \r
          """
      );

      test(socket,
          """
          GET /testCase02/foo HTTP/1.1\r
          Host: http.module.test\r
          Cookie: HTTPMODULETEST=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=\r
          \r
          """,

          """
          HTTP/1.1 404 Not Found\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Content-Length: 0\r
          Connection: close\r
          \r
          """
      );
    }
  }

  private void testCase03(Http.Exchange http) {
    String value;
    value = http.path();

    String text;
    text = value.substring("/testCase03".length());

    Media.Bytes plain;
    plain = Media.Bytes.textPlain(text, StandardCharsets.UTF_8);

    http.ok(plain);
  }

  @Test
  public void testCase03() throws IOException {
    try (Socket socket = newSocket()) {
      test(socket,
          """
          GET /testCase03 HTTP/1.1\r
          Host: http.module.test\r
          Cookie: HTTPMODULETEST=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=\r
          \r
          """,

          """
          HTTP/1.1 200 OK\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Content-Type: text/plain; charset=utf-8\r
          Content-Length: 0\r
          \r
          """
      );

      test(socket,
          """
          GET /testCase03/ HTTP/1.1\r
          Host: http.module.test\r
          Cookie: HTTPMODULETEST=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=\r
          \r
          """,

          """
          HTTP/1.1 200 OK\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Content-Type: text/plain; charset=utf-8\r
          Content-Length: 1\r
          \r
          /"""
      );

      test(socket,
          """
          GET /testCase03/foo HTTP/1.1\r
          Host: http.module.test\r
          Cookie: HTTPMODULETEST=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=\r
          \r
          """,

          """
          HTTP/1.1 200 OK\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Content-Type: text/plain; charset=utf-8\r
          Content-Length: 4\r
          \r
          /foo"""
      );

      test(socket,
          """
          GET /testCase03/foo/bar HTTP/1.1\r
          Host: http.module.test\r
          Cookie: HTTPMODULETEST=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=\r
          \r
          """,

          """
          HTTP/1.1 200 OK\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Content-Type: text/plain; charset=utf-8\r
          Content-Length: 8\r
          \r
          /foo/bar"""
      );
    }
  }

  private static class TestCase04 implements Http.Routing.Module {
    @Override
    public final void configure(Http.Routing routing) {
      routing.path("/testCase04", path -> {
        path.handler(http -> http.ok(Media.Bytes.textPlain("ROOT")));
      });

      routing.path("/testCase04/", path -> {
        path.handler(http -> http.movedPermanently("/testCase04"));
      });

      routing.path("/testCase04/foo", path -> {
        path.handler(http -> http.ok(Media.Bytes.textPlain("foo")));
      });
    }
  }

  @Test
  public void testCase04() throws IOException {
    try (Socket socket = newSocket()) {
      test(socket,
          """
          GET /testCase04 HTTP/1.1\r
          Host: http.module.test\r
          Cookie: HTTPMODULETEST=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=\r
          \r
          """,

          """
          HTTP/1.1 200 OK\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Content-Type: text/plain; charset=utf-8\r
          Content-Length: 4\r
          \r
          ROOT"""
      );

      test(socket,
          """
          GET /testCase04/ HTTP/1.1\r
          Host: http.module.test\r
          Cookie: HTTPMODULETEST=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=\r
          \r
          """,

          """
          HTTP/1.1 301 Moved Permanently\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Content-Length: 0\r
          Location: /testCase04\r
          \r
          """
      );

      test(socket,
          """
          GET /testCase04/foo HTTP/1.1\r
          Host: http.module.test\r
          Cookie: HTTPMODULETEST=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=\r
          \r
          """,

          """
          HTTP/1.1 200 OK\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Content-Type: text/plain; charset=utf-8\r
          Content-Length: 3\r
          \r
          foo"""
      );

      test(socket,
          """
          GET /testCase04/bar HTTP/1.1\r
          Host: http.module.test\r
          Cookie: HTTPMODULETEST=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=\r
          \r
          """,

          """
          HTTP/1.1 404 Not Found\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Content-Length: 0\r
          Connection: close\r
          \r
          """
      );
    }
  }

  private void testCase05(Http.Exchange http) {
    String value;
    value = http.path();

    String text;
    text = value.substring("/testCase05/img".length());

    Media.Bytes plain;
    plain = Media.Bytes.textPlain(text, StandardCharsets.UTF_8);

    http.ok(plain);
  }

  @Test
  public void testCase05() throws IOException {
    try (Socket socket = newSocket()) {
      test(socket,
          """
          GET /testCase05/img/a HTTP/1.1\r
          Host: http.module.test\r
          Cookie: HTTPMODULETEST=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=\r
          \r
          """,

          """
          HTTP/1.1 200 OK\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Content-Type: text/plain; charset=utf-8\r
          Content-Length: 2\r
          \r
          /a"""
      );

      test(socket,
          """
          GET /testCase05/img HTTP/1.1\r
          Host: http.module.test\r
          Cookie: HTTPMODULETEST=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=\r
          \r
          """,

          """
          HTTP/1.1 200 OK\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Content-Type: text/plain; charset=utf-8\r
          Content-Length: 0\r
          \r
          """
      );

      test(socket,
          """
          GET /testCase05/img/ HTTP/1.1\r
          Host: http.module.test\r
          Cookie: HTTPMODULETEST=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=\r
          \r
          """,

          """
          HTTP/1.1 200 OK\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Content-Type: text/plain; charset=utf-8\r
          Content-Length: 1\r
          \r
          /"""
      );

      test(socket,
          """
          GET /testCase05/img/a/b HTTP/1.1\r
          Host: http.module.test\r
          Cookie: HTTPMODULETEST=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=\r
          \r
          """,

          """
          HTTP/1.1 200 OK\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Content-Type: text/plain; charset=utf-8\r
          Content-Length: 4\r
          \r
          /a/b"""
      );

      test(socket,
          """
          GET /testCase05/img/a/b/c HTTP/1.1\r
          Host: http.module.test\r
          Cookie: HTTPMODULETEST=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=\r
          \r
          """,

          """
          HTTP/1.1 200 OK\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Content-Type: text/plain; charset=utf-8\r
          Content-Length: 6\r
          \r
          /a/b/c"""
      );

      test(socket,
          """
          GET /testCase05/imx/a/b/c HTTP/1.1\r
          Host: http.module.test\r
          Cookie: HTTPMODULETEST=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=\r
          \r
          """,

          """
          HTTP/1.1 404 Not Found\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Content-Length: 0\r
          Connection: close\r
          \r
          """
      );
    }
  }

  private void $testCase06(Http.Exchange http) {
    String value;
    value = http.path();

    String text;
    text = value.substring("/testCase06".length());

    Media.Bytes plain;
    plain = Media.Bytes.textPlain(text, StandardCharsets.UTF_8);

    http.ok(plain);
  }

  @Test
  public void testCase06() throws IOException {
    try (Socket socket = newSocket()) {
      test(socket,
          """
          GET /testCase06/ HTTP/1.1\r
          Host: http.module.test\r
          Cookie: HTTPMODULETEST=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=\r
          \r
          """,

          """
          HTTP/1.1 200 OK\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Content-Type: text/plain; charset=utf-8\r
          Content-Length: 1\r
          \r
          /"""
      );

      test(socket,
          """
          GET /testCase06/foo HTTP/1.1\r
          Host: http.module.test\r
          Cookie: HTTPMODULETEST=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=\r
          \r
          """,

          """
          HTTP/1.1 200 OK\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Content-Type: text/plain; charset=utf-8\r
          Content-Length: 4\r
          \r
          /foo"""
      );

      test(socket,
          """
          GET /testCase06/foo/bar HTTP/1.1\r
          Host: http.module.test\r
          Cookie: HTTPMODULETEST=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=\r
          \r
          """,

          """
          HTTP/1.1 200 OK\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Content-Type: text/plain; charset=utf-8\r
          Content-Length: 8\r
          \r
          /foo/bar"""
      );

      test(socket,
          """
          GET /testCase06 HTTP/1.1\r
          Host: http.module.test\r
          Cookie: HTTPMODULETEST=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=\r
          \r
          """,

          """
          HTTP/1.1 404 Not Found\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Content-Length: 0\r
          Connection: close\r
          \r
          """
      );
    }
  }

  private void $testCase07(Http.Exchange http) {
    Box box;
    box = http.get(Box.class);

    String value;
    value = box != null ? box.value : "null";

    if ("throw".equals(value)) {
      throw new TestException();
    }

    Media.Bytes plain;
    plain = Media.Bytes.textPlain("VALUE=" + value, StandardCharsets.UTF_8);

    http.ok(plain);
  }

  private Http.Handler testCase07(Http.Handler handler) {
    return http -> {
      String value;
      value = http.queryParam("value");

      if (value == null) {
        value = "";
      }

      Box box;
      box = new Box(value);

      http.set(Box.class, box);

      try {
        handler.handle(http);
      } catch (TestException e) {
        Media.Bytes plain;
        plain = Media.Bytes.textPlain("TestException", StandardCharsets.UTF_8);

        http.ok(plain);
      }
    };
  }

  @Test
  public void testCase07() throws IOException {
    try (Socket socket = newSocket()) {
      test(socket,
          """
          GET /testCase07/before?value=foo HTTP/1.1\r
          Host: http.module.test\r
          Cookie: HTTPMODULETEST=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=\r
          \r
          """,

          """
          HTTP/1.1 200 OK\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Content-Type: text/plain; charset=utf-8\r
          Content-Length: 10\r
          \r
          VALUE=null"""
      );

      test(socket,
          """
          GET /testCase07/after?value=foo HTTP/1.1\r
          Host: http.module.test\r
          Cookie: HTTPMODULETEST=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=\r
          \r
          """,

          """
          HTTP/1.1 200 OK\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Content-Type: text/plain; charset=utf-8\r
          Content-Length: 9\r
          \r
          VALUE=foo"""
      );

      test(socket,
          """
          GET /testCase07/after?value=throw HTTP/1.1\r
          Host: http.module.test\r
          Cookie: HTTPMODULETEST=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=\r
          \r
          """,

          """
          HTTP/1.1 200 OK\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Content-Type: text/plain; charset=utf-8\r
          Content-Length: 13\r
          \r
          TestException"""
      );
    }
  }

  private static class TestCase08 implements Http.Routing.Module {
    @Override
    public final void configure(Http.Routing routing) {
      routing.path("/testCase08", path -> {
        path.handler(http -> http.ok(Media.Bytes.textPlain("TC08")));
      });
    }
  }

  @Test
  public void testCase08() throws IOException {
    try (Socket socket = newSocket()) {
      test(socket,
          """
          GET /testCase08 HTTP/1.1\r
          Host: http.module.test\r
          Cookie: HTTPMODULETEST=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=\r
          Connection: close\r
          \r
          """,

          """
          HTTP/1.1 200 OK\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Content-Type: text/plain; charset=utf-8\r
          Content-Length: 4\r
          \r
          TC08"""
      );
    }
  }

  private void $testCase09(Http.Exchange http) {
    String notEmpty;
    notEmpty = http.pathParam("notEmpty");

    String digits;
    digits = http.pathParam("digits");

    Media.Bytes plain;
    plain = Media.Bytes.textPlain(notEmpty + ":" + digits, StandardCharsets.UTF_8);

    http.ok(plain);
  }

  @Test
  public void testCase09() throws IOException {
    try (Socket socket = newSocket()) {
      test(socket,
          """
          GET /testCase09/abc/123 HTTP/1.1\r
          Host: http.module.test\r
          Cookie: HTTPMODULETEST=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=\r
          \r
          """,

          """
          HTTP/1.1 200 OK\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Content-Type: text/plain; charset=utf-8\r
          Content-Length: 7\r
          \r
          abc:123"""
      );

      test(socket,
          """
          GET /testCase09//123 HTTP/1.1\r
          Host: http.module.test\r
          Cookie: HTTPMODULETEST=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=\r
          \r
          """,

          """
          HTTP/1.1 404 Not Found\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Content-Length: 0\r
          Connection: close\r
          \r
          """
      );
    }

    try (Socket socket = newSocket()) {
      test(socket,
          """
          GET /testCase09/abc/x HTTP/1.1\r
          Host: http.module.test\r
          Cookie: HTTPMODULETEST=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=\r
          Connection: close\r
          \r
          """,

          """
          HTTP/1.1 404 Not Found\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Content-Length: 0\r
          Connection: close\r
          \r
          """
      );
    }
  }

  private void $testCase10(Http.Exchange http) {
    String regex;
    regex = http.pathParam("regex");

    Media.Bytes plain;
    plain = Media.Bytes.textPlain(regex, StandardCharsets.UTF_8);

    http.ok(plain);
  }

  @Test
  public void testCase10() throws IOException {
    try (Socket socket = newSocket()) {
      test(socket,
          """
          GET /testCase10/abc123 HTTP/1.1\r
          Host: http.module.test\r
          Cookie: HTTPMODULETEST=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=\r
          \r
          """,

          """
          HTTP/1.1 200 OK\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Content-Type: text/plain; charset=utf-8\r
          Content-Length: 6\r
          \r
          abc123"""
      );

      test(socket,
          """
          GET /testCase09/abc_123 HTTP/1.1\r
          Host: http.module.test\r
          Cookie: HTTPMODULETEST=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=\r
          \r
          """,

          """
          HTTP/1.1 404 Not Found\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Content-Length: 0\r
          Connection: close\r
          \r
          """
      );
    }
  }

  private void $testCase11(Http.Exchange http) {
    Media.Bytes plain;
    plain = Media.Bytes.textPlain("SECOND", StandardCharsets.UTF_8);

    http.ok(plain);
  }

  @Test
  public void testCase11() throws IOException, InterruptedException {
    HttpResponse<String> response;
    response = Y.httpClient(
        "/testCase11",

        builder -> builder.headers(
            "Host", "http.module.test",
            "Connection", "close",
            "Cookie", "HTTPMODULETEST=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ="
        )
    );

    assertEquals(response.statusCode(), 200);
    assertEquals(response.body(), "SECOND");
  }

  @Test
  public void testCase12() throws IOException, InterruptedException {
    HttpResponse<String> response;
    response = Y.httpClient(
        "/testCase12",

        builder -> builder.headers(
            "Host", "http.module.test",
            "Connection", "close",
            "Cookie", cookie
        )
    );

    assertEquals(response.statusCode(), 200);
    assertEquals(response.body(), "tc12=ABC-123");
  }

  private void $testCase13(Http.Exchange http) {
    Http.Method method;
    method = http.method();

    Http.HeaderName name;
    name = Http.HeaderName.of("X-Test-Case-13");

    String value;
    value = http.header(name);

    String text;
    text = method.name() + "=" + value;

    Media.Bytes plain;
    plain = Media.Bytes.textPlain(text, StandardCharsets.UTF_8);

    http.ok(plain);
  }

  @Test
  public void testCase13() throws IOException, InterruptedException {
    Y.test(
        Y.httpClient(
            "/testCase13",

            builder -> builder.GET().headers(
                "Host", "http.module.test",
                "X-Test-Case-13", "one"
            )
        ),

        """
        HTTP/1.1 200
        content-length: 7
        content-type: text/plain; charset=utf-8
        date: Wed, 28 Jun 2023 12:08:43 GMT

        GET=one\
        """
    );

    Y.test(
        Y.httpClient(
            "/testCase13",

            builder -> builder.HEAD().headers(
                "Host", "http.module.test",
                "X-Test-Case-13", "two"
            )
        ),

        """
        HTTP/1.1 200
        content-length: 8
        content-type: text/plain; charset=utf-8
        date: Wed, 28 Jun 2023 12:08:43 GMT

        """
    );

    Y.test(
        Y.httpClient(
            "/testCase13",

            builder -> builder.POST(HttpRequest.BodyPublishers.noBody()).headers(
                "Host", "http.module.test",
                "X-Test-Case-13", "three"
            )
        ),

        """
        HTTP/1.1 200
        content-length: 10
        content-type: text/plain; charset=utf-8
        date: Wed, 28 Jun 2023 12:08:43 GMT

        POST=three\
        """
    );

    Y.test(
        Y.httpClient(
            "/testCase13",

            builder -> builder.DELETE().headers(
                "Host", "http.module.test",
                "X-Test-Case-13", "four"
            )
        ),

        """
        HTTP/1.1 405
        allow: GET, HEAD, POST
        content-length: 0
        date: Wed, 28 Jun 2023 12:08:43 GMT

        """
    );
  }

  @SuppressWarnings("unused")
  private void $testCase14(Http.Exchange http) {
    final Http.Method method;
    method = http.method();

    final String path;
    path = http.path();

    final String sub;
    sub = path.substring("/testCase14".length());

    http.ok(Media.Bytes.textPlain(method + "=" + sub));
  }

  @Test
  public void testCase14() {
    Y.test(
        Y.httpClient(
            "/testCase14/a",

            builder -> builder.GET().headers(
                "Host", "http.module.test"
            )
        ),

        """
        HTTP/1.1 200
        content-length: 6
        content-type: text/plain; charset=utf-8
        date: Wed, 28 Jun 2023 12:08:43 GMT

        GET=/a\
        """
    );

    Y.test(
        Y.httpClient(
            "/testCase14/x",

            builder -> builder.GET().headers(
                "Host", "http.module.test"
            )
        ),

        """
        HTTP/1.1 404
        connection: close
        content-length: 0
        date: Wed, 28 Jun 2023 12:08:43 GMT

        """
    );
  }

  @SuppressWarnings("unused")
  private void $testCase15Filter(Http.Exchange http, Http.Handler handler) {
    final Http.HeaderName name;
    name = Http.HeaderName.of("X-Test-Case-15");

    final String value;
    value = http.header(name);

    http.set(String.class, value);

    handler.handle(http);

    if (!http.processed()) {
      final Media.Bytes object;
      object = Media.Bytes.textPlain("Not Found", StandardCharsets.UTF_8);

      http.notFound(object);
    }
  }

  @SuppressWarnings("unused")
  private void $testCase15(Http.Exchange http) {
    final String maybe;
    maybe = http.get(String.class);

    final String value;
    value = maybe != null ? maybe : "null";

    final Media.Bytes media;
    media = Media.Bytes.textPlain(value);

    http.ok(media);
  }

  @Test
  public void testCase15() {
    Y.test(
        Y.httpClient(
            "/testCase15/a",

            builder -> builder.GET().headers(
                "Host", "http.module.test",
                "X-Test-Case-15", "matched"
            )
        ),

        """
        HTTP/1.1 200
        content-length: 7
        content-type: text/plain; charset=utf-8
        date: Wed, 28 Jun 2023 12:08:43 GMT

        matched\
        """
    );

    Y.test(
        Y.httpClient(
            "/testCase15/x",

            builder -> builder.GET().headers(
                "Host", "http.module.test",
                "X-Test-Case-15", "not-matched"
            )
        ),

        """
        HTTP/1.1 404
        content-length: 9
        content-type: text/plain; charset=utf-8
        date: Wed, 28 Jun 2023 12:08:43 GMT

        Not Found\
        """
    );
  }

  @Test
  public void edge01() {
    Http.Routing.Module empty = routing -> {};

    Http.Handler handler = Http.Handler.of(empty);

    assertNotNull(handler);
  }

  private Socket newSocket() throws IOException {
    return TestingHttpServer.newSocket();
  }

  private void test(Socket socket, String request, String expectedResponse) throws IOException {
    TestingHttpServer.test(socket, request, expectedResponse);
  }

}