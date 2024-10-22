/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import objectos.way.TestingRandom.SequentialRandom;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class HttpModuleTest extends Http.Module {

  private record Box(String value) {}

  private record User(String login) {}

  @SuppressWarnings("serial")
  private static class TestException extends RuntimeException {}

  private Web.Store sessionStore;

  private SequentialRandom random;

  @BeforeClass
  public void beforeClass() {
    random = new SequentialRandom();

    this.sessionStore = Web.createStore(
        Web.cookieName("HTTPMODULETEST"),

        Web.random(random)
    );

    TestingHttpServer.bindHttpModuleTest(this);
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
  protected final void configure() {
    filter(sessionStore::filter);

    // matches: /testCase01/foo
    // but not: /testCase01, /testCase01/, /testCase01/foo/bar
    route("/testCase01/:text",
        pathParams(notEmpty("text")),
        handler(this::$testCase01));

    // redirect non-authenticated requests
    filter(this::testCase02);

    // matches: /testCase03, /testCase03/foo, /testCase03/foo/bar
    Http.Handler testCase03 = this::testCase03;
    route("/testCase03", handler(testCase03));
    route("/testCase03/*", handler(testCase03));

    // matches: /testCase04, /testCase04/foo, /testCase04/foo/bar
    install(new TestCase04());

    // matches: /testCase05/img, /testCase05/img/, /testCase05/img/a, /testCase05/img/b
    Http.Handler testCase05 = this::testCase05;
    route("/testCase05/img", handler(testCase05));
    route("/testCase05/img/*", handler(testCase05));

    // matches: /testCase06/, /testCase06/foo, /testCase06/foo/bar
    // but not: /testCase06
    route("/testCase06/*", handler(this::$testCase06));

    // tc07: interceptMatched
    route("/testCase07/before", handler(this::$testCase07));

    interceptMatched(this::testCase07);

    route("/testCase07/after", handler(this::$testCase07));

    // tc08: install
    install(new TestCase08());

    // tc09: notEmpty, digits
    route("/testCase09/:notEmpty/:digits",
        pathParams(notEmpty("notEmpty"), digits("digits")),
        handler(this::$testCase09));

    // tc10: regex
    route("/testCase10/:regex",
        pathParams(regex("regex", "[0-9a-z]+")),
        handler(this::$testCase10));

    // tc11: multiple handlers
    Http.Handler noop = http -> {};
    Http.Handler nono = http -> http.okText("nonono\n", StandardCharsets.UTF_8);
    route("/testCase11", handler(noop), handler(this::$testCase11), handler(nono));

    // tc12: interceptor
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

    route("/testCase12", handler(tc12A), interceptor(tc12X), handler(tc12C));
  }

  private void $testCase01(Http.Exchange http) {
    String text;
    text = http.pathParam("text");

    TestingSingleParagraph html;
    html = new TestingSingleParagraph(text);

    http.ok(html);
  }

  @Test
  public void testCase01() throws IOException {
    try (Socket socket = newSocket()) {
      test(socket,
          """
          GET /testCase01/foo HTTP/1.1\r
          Host: http.module.test\r
          \r
          """,

          """
          HTTP/1.1 200 OK\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Content-Type: text/html; charset=utf-8\r
          Transfer-Encoding: chunked\r
          \r
          1a\r
          <html>
          <p>foo</p>
          </html>
          \r
          0\r
          \r
          """
      );

      test(socket,
          """
          GET /testCase01 HTTP/1.1\r
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
          GET /testCase01/ HTTP/1.1\r
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
          GET /testCase01/foo/bar HTTP/1.1\r
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

  private static class TestCase04 extends Http.Module {
    @Override
    protected final void configure() {
      route("/testCase04", handler(http -> http.okText("ROOT", StandardCharsets.UTF_8)));

      route("/testCase04/", handler(http -> http.movedPermanently("/testCase04")));

      route("/testCase04/foo", handler(http -> http.okText("foo", StandardCharsets.UTF_8)));
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

  private static class TestCase08 extends Http.Module {
    @Override
    protected final void configure() {
      route("/testCase08", handler(http -> http.okText("TC08", StandardCharsets.UTF_8)));
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

        Testing.headers(
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

        Testing.headers(
            "Host", "http.module.test",
            "Connection", "close",
            "Cookie", "HTTPMODULETEST=TEST_COOKIE"
        )
    );

    assertEquals(response.statusCode(), 200);
    assertEquals(response.body(), "tc12=ABC-123");
  }

  @Test
  public void edge01() {
    Http.Module empty = new Http.Module() {
      @Override
      protected void configure() {}
    };

    Http.Handler handler = empty.compile();

    assertNotNull(handler);
  }

  private Socket newSocket() throws IOException {
    return TestingHttpServer.newSocket();
  }

  private void test(Socket socket, String request, String expectedResponse) throws IOException {
    TestingHttpServer.test(socket, request, expectedResponse);
  }

}