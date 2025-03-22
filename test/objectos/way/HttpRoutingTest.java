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
import java.util.function.Consumer;
import objectos.way.Http.HeaderName;
import objectos.way.Http.Method;
import objectos.way.Http.Routing;
import objectos.way.TestingRandom.SequentialRandom;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class HttpRoutingTest implements Consumer<Http.Routing> {

  private record Box(String value) {}

  private record User(String login) {}

  @SuppressWarnings("serial")
  private static class TestException extends RuntimeException {}

  private Web.Store sessionStore;

  private SequentialRandom random;

  @BeforeClass
  public void beforeClass() {
    random = new SequentialRandom();

    this.sessionStore = Web.Store.create(config -> {
      config.cookieName("HTTPMODULETEST");

      config.random(random);
    });

    TestingHttpServer.bindHttpRoutingTest(this);
  }

  @BeforeMethod
  public void beforeMethod() {
    Web.Session session;
    session = Web.Session.create("TEST_COOKIE");

    session.put(User.class, new User("test"));

    sessionStore.store(session);

    random.reset();
  }

  @Override
  public final void accept(Http.Routing routing) {
    routing.when(this::notAuthenticated, matched -> {
      // matches: /testCase01/foo
      // but not: /testCase01, /testCase01/, /testCase01/foo/bar
      matched.path("/testCase01/:text", path -> {
        path.paramNotEmpty("text");

        path.handler(this::$testCase01);
      });

      matched.path("/testCase13", path -> {
        path.allow(Http.Method.GET, this::$testCase13);

        path.allow(Http.Method.POST, this::$testCase13);
      });

      // redirect non-authenticated requests
      matched.handler(this::testCase02);
    });

    routing.when(this::authenticated, matched -> {
      // matches: /testCase03, /testCase03/foo, /testCase03/foo/bar
      matched.path("/testCase03", path -> {
        path.handler(this::testCase03);
      });
      matched.path("/testCase03/*", path -> {
        path.handler(this::testCase03);
      });

      // matches: /testCase04, /testCase04/foo, /testCase04/foo/bar
      matched.install(new TestCase04());

      // matches: /testCase05/img, /testCase05/img/, /testCase05/img/a, /testCase05/img/b
      matched.path("/testCase05/img", path -> {
        path.handler(this::testCase05);
      });
      matched.path("/testCase05/img/*", path -> {
        path.handler(this::testCase05);
      });

      // matches: /testCase06/, /testCase06/foo, /testCase06/foo/bar
      // but not: /testCase06
      matched.path("/testCase06/*", path -> {
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
      matched.path("/testCase09/:notEmpty/:digits", path -> {
        path.paramNotEmpty("notEmpty");

        path.paramDigits("digits");

        path.handler(this::$testCase09);
      });

      // tc10: regex
      matched.path("/testCase10/:regex", path -> {
        path.paramRegex("regex", "[0-9a-z]+");

        path.handler(this::$testCase10);
      });

      // tc11: multiple handlers
      matched.path("/testCase11", path -> {
        path.handler(Http.Handler.firstOf(
            Http.Handler.noop(),

            this::$testCase11,

            Http.Handler.ofText("nonono\n", StandardCharsets.UTF_8))
        );
      });

      // tc12: interceptor
      matched.path("/testCase12", path -> {
        Http.Handler tc12A = http -> {
          assertNull(http.get(String.class));

          http.set(Integer.class, 123);
        };

        Http.Handler.Interceptor tc12X = handler -> {
          return http -> {
            http.set(String.class, "ABC");

            handler.handle(http);
          };
        };

        Http.Handler tc12C = http -> {
          String s = http.get(String.class);
          Integer i = http.get(Integer.class);
          http.okText("tc12=" + s + "-" + i.toString(), StandardCharsets.UTF_8);
        };

        path.handler(Http.Handler.firstOf(tc12A, tc12X.intercept(tc12C)));
      });
    });

    routing.handler(Http.Handler.notFound());
  }

  private boolean notAuthenticated(Http.Request req) {
    return !authenticated(req);
  }

  private boolean authenticated(Http.Request req) {
    final Web.Session session;
    session = sessionStore.get(req);

    if (session == null) {
      return false;
    }

    final User user;
    user = session.get(User.class);

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
    response = Testing.httpClient(
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

    response = Testing.httpClient(
        "/testCase01",

        builder -> builder.headers(
            "Host", "http.module.test",
            "Cookie", "HTTPMODULETEST=TEST_COOKIE"
        )
    );

    assertEquals(response.statusCode(), 404);
    assertEquals(response.headers().allValues("Connection"), List.of("close"));
    assertEquals(response.headers().allValues("Date"), List.of("Wed, 28 Jun 2023 12:08:43 GMT"));

    response = Testing.httpClient(
        "/testCase01/",

        builder -> builder.headers(
            "Host", "http.module.test",
            "Cookie", "HTTPMODULETEST=TEST_COOKIE"
        )
    );

    assertEquals(response.statusCode(), 404);
    assertEquals(response.headers().allValues("Connection"), List.of("close"));
    assertEquals(response.headers().allValues("Date"), List.of("Wed, 28 Jun 2023 12:08:43 GMT"));

    response = Testing.httpClient(
        "/testCase01/foo/bar",

        builder -> builder.headers(
            "Host", "http.module.test",
            "Cookie", "HTTPMODULETEST=TEST_COOKIE"
        )
    );

    assertEquals(response.statusCode(), 404);
    assertEquals(response.headers().allValues("Connection"), List.of("close"));
    assertEquals(response.headers().allValues("Date"), List.of("Wed, 28 Jun 2023 12:08:43 GMT"));
  }

  private void testCase02(Http.Exchange http) {
    User user;
    user = null;

    Web.Session session;
    session = http.get(Web.Session.class);

    if (session != null) {
      user = session.get(User.class);
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
          HTTP/1.1 302 FOUND\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Location: /login\r
          \r
          """
      );

      test(socket,
          """
          GET /testCase02/foo HTTP/1.1\r
          Host: http.module.test\r
          Cookie: HTTPMODULETEST=TEST_COOKIE\r
          \r
          """,

          """
          HTTP/1.1 404 NOT FOUND\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
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

    http.okText(text, StandardCharsets.UTF_8);
  }

  @Test
  public void testCase03() throws IOException {
    try (Socket socket = newSocket()) {
      test(socket,
          """
          GET /testCase03 HTTP/1.1\r
          Host: http.module.test\r
          Cookie: HTTPMODULETEST=TEST_COOKIE\r
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
          Cookie: HTTPMODULETEST=TEST_COOKIE\r
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
          Cookie: HTTPMODULETEST=TEST_COOKIE\r
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
          Cookie: HTTPMODULETEST=TEST_COOKIE\r
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

  private static class TestCase04 implements Consumer<Http.Routing> {
    @Override
    public final void accept(Http.Routing routing) {
      routing.path("/testCase04", path -> {
        path.handler(Http.Handler.ofText("ROOT", StandardCharsets.UTF_8));
      });

      routing.path("/testCase04/", path -> {
        path.handler(Http.Handler.movedPermanently("/testCase04"));
      });

      routing.path("/testCase04/foo", path -> {
        path.handler(Http.Handler.ofText("foo", StandardCharsets.UTF_8));
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
          Cookie: HTTPMODULETEST=TEST_COOKIE\r
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
          Cookie: HTTPMODULETEST=TEST_COOKIE\r
          \r
          """,

          """
          HTTP/1.1 301 MOVED PERMANENTLY\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Location: /testCase04\r
          \r
          """
      );

      test(socket,
          """
          GET /testCase04/foo HTTP/1.1\r
          Host: http.module.test\r
          Cookie: HTTPMODULETEST=TEST_COOKIE\r
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
          Cookie: HTTPMODULETEST=TEST_COOKIE\r
          \r
          """,

          """
          HTTP/1.1 404 NOT FOUND\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
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

    http.okText(text, StandardCharsets.UTF_8);
  }

  @Test
  public void testCase05() throws IOException {
    try (Socket socket = newSocket()) {
      test(socket,
          """
          GET /testCase05/img/a HTTP/1.1\r
          Host: http.module.test\r
          Cookie: HTTPMODULETEST=TEST_COOKIE\r
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
          Cookie: HTTPMODULETEST=TEST_COOKIE\r
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
          Cookie: HTTPMODULETEST=TEST_COOKIE\r
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
          Cookie: HTTPMODULETEST=TEST_COOKIE\r
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
          Cookie: HTTPMODULETEST=TEST_COOKIE\r
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
          Cookie: HTTPMODULETEST=TEST_COOKIE\r
          \r
          """,

          """
          HTTP/1.1 404 NOT FOUND\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
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

    http.okText(text, StandardCharsets.UTF_8);
  }

  @Test
  public void testCase06() throws IOException {
    try (Socket socket = newSocket()) {
      test(socket,
          """
          GET /testCase06/ HTTP/1.1\r
          Host: http.module.test\r
          Cookie: HTTPMODULETEST=TEST_COOKIE\r
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
          Cookie: HTTPMODULETEST=TEST_COOKIE\r
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
          Cookie: HTTPMODULETEST=TEST_COOKIE\r
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
          Cookie: HTTPMODULETEST=TEST_COOKIE\r
          \r
          """,

          """
          HTTP/1.1 404 NOT FOUND\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
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

    http.okText("VALUE=" + value, StandardCharsets.UTF_8);
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
        http.okText("TestException", StandardCharsets.UTF_8);
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
          Cookie: HTTPMODULETEST=TEST_COOKIE\r
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
          Cookie: HTTPMODULETEST=TEST_COOKIE\r
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
          Cookie: HTTPMODULETEST=TEST_COOKIE\r
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

  private static class TestCase08 implements Consumer<Http.Routing> {
    @Override
    public final void accept(Routing routing) {
      routing.path("/testCase08", path -> {
        path.handler(Http.Handler.ofText("TC08", StandardCharsets.UTF_8));
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
          Cookie: HTTPMODULETEST=TEST_COOKIE\r
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

    http.okText(notEmpty + ":" + digits, StandardCharsets.UTF_8);
  }

  @Test
  public void testCase09() throws IOException {
    try (Socket socket = newSocket()) {
      test(socket,
          """
          GET /testCase09/abc/123 HTTP/1.1\r
          Host: http.module.test\r
          Cookie: HTTPMODULETEST=TEST_COOKIE\r
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
          Cookie: HTTPMODULETEST=TEST_COOKIE\r
          \r
          """,

          """
          HTTP/1.1 404 NOT FOUND\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
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
          Cookie: HTTPMODULETEST=TEST_COOKIE\r
          Connection: close\r
          \r
          """,

          """
          HTTP/1.1 404 NOT FOUND\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Connection: close\r
          \r
          """
      );
    }
  }

  private void $testCase10(Http.Exchange http) {
    String regex;
    regex = http.pathParam("regex");

    http.okText(regex, StandardCharsets.UTF_8);
  }

  @Test
  public void testCase10() throws IOException {
    try (Socket socket = newSocket()) {
      test(socket,
          """
          GET /testCase10/abc123 HTTP/1.1\r
          Host: http.module.test\r
          Cookie: HTTPMODULETEST=TEST_COOKIE\r
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
          Cookie: HTTPMODULETEST=TEST_COOKIE\r
          \r
          """,

          """
          HTTP/1.1 404 NOT FOUND\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Connection: close\r
          \r
          """
      );
    }
  }

  private void $testCase11(Http.Exchange http) {
    http.okText("SECOND", StandardCharsets.UTF_8);
  }

  @Test
  public void testCase11() throws IOException, InterruptedException {
    HttpResponse<String> response;
    response = Testing.httpClient(
        "/testCase11",

        builder -> builder.headers(
            "Host", "http.module.test",
            "Connection", "close",
            "Cookie", "HTTPMODULETEST=TEST_COOKIE"
        )
    );

    assertEquals(response.statusCode(), 200);
    assertEquals(response.body(), "SECOND");
  }

  @Test
  public void testCase12() throws IOException, InterruptedException {
    HttpResponse<String> response;
    response = Testing.httpClient(
        "/testCase12",

        builder -> builder.headers(
            "Host", "http.module.test",
            "Connection", "close",
            "Cookie", "HTTPMODULETEST=TEST_COOKIE"
        )
    );

    assertEquals(response.statusCode(), 200);
    assertEquals(response.body(), "tc12=ABC-123");
  }

  private void $testCase13(Http.Exchange http) {
    Method method = http.method();

    HeaderName name = Http.HeaderName.of("X-Test-Case-13");

    String value = http.header(name);

    http.okText(method.name() + "=" + value, StandardCharsets.UTF_8);
  }

  @Test
  public void testCase13() throws IOException, InterruptedException {
    HttpResponse<String> response;
    response = Testing.httpClient(
        "/testCase13",

        builder -> builder.GET().headers(
            "Host", "http.module.test",
            "X-Test-Case-13", "one"
        )
    );

    assertEquals(response.statusCode(), 200);
    assertEquals(response.body(), "GET=one");

    response = Testing.httpClient(
        "/testCase13",

        builder -> builder.HEAD().headers(
            "Host", "http.module.test",
            "X-Test-Case-13", "two"
        )
    );

    assertEquals(response.statusCode(), 200);
    assertEquals(response.body(), "");

    response = Testing.httpClient(
        "/testCase13",

        builder -> builder.POST(HttpRequest.BodyPublishers.noBody()).headers(
            "Host", "http.module.test",
            "X-Test-Case-13", "three"
        )
    );

    assertEquals(response.statusCode(), 200);
    assertEquals(response.body(), "POST=three");

    response = Testing.httpClient(
        "/testCase13",

        builder -> builder.DELETE().headers(
            "Host", "http.module.test",
            "X-Test-Case-13", "four"
        )
    );

    assertEquals(response.statusCode(), 405);
  }

  @Test
  public void edge01() {
    Consumer<Http.Routing> empty = routing -> {};

    Http.Handler handler = Http.Handler.create(empty);

    assertNotNull(handler);
  }

  private Socket newSocket() throws IOException {
    return TestingHttpServer.newSocket();
  }

  private void test(Socket socket, String request, String expectedResponse) throws IOException {
    TestingHttpServer.test(socket, request, expectedResponse);
  }

}