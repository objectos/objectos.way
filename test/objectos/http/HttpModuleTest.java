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
package objectos.http;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import objectos.http.UriPath.Segment;
import objectos.way.TestingRandom.SequentialRandom;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class HttpModuleTest extends HttpModule {

  private record User(String login) {}

  private WaySessionStore sessionStore;

  private SequentialRandom random;

  @BeforeClass
  public void beforeClass() {
    WaySessionStore sessionStore;
    sessionStore = new WaySessionStore();

    sessionStore.cookieName("HTTPMODULETEST");

    random = new SequentialRandom();

    sessionStore.random(random);

    this.sessionStore = sessionStore;

    TestingHttpServer.bindHttpModuleTest(this);
  }

  @BeforeMethod
  public void beforeMethod() {
    sessionStore.clear();

    WaySession session;
    session = new WaySession("TEST_COOKIE");

    session.put(User.class, new User("test"));

    sessionStore.add(session);

    random.reset();
  }

  @Override
  protected final void configure() {
    sessionStore(sessionStore);

    // matches: /testCase01/foo
    // but not: /testCase01, /testCase01/, /testCase01/foo/bar
    route(segments(eq("testCase01"), nonEmpty()), this::testCase01);

    // redirect non-authenticated requests
    filter(this::testCase02);

    // matches: /testCase03, /testCase03/foo, /testCase03/foo/bar
    route(segments(eq("testCase03"), zeroOrMore()), this::testCase03);

    // matches: /testCase04, /testCase04/foo, /testCase04/foo/bar
    route(segments(eq("testCase04"), zeroOrMore()), new TestCase04());
  }

  private void testCase01(ServerExchange http) {
    UriPath path;
    path = http.path();

    List<Segment> segments;
    segments = path.segments();

    Segment second;
    second = segments.get(1);

    String text;
    text = second.value();

    SingleParagraph html;
    html = new SingleParagraph(text);

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
          Content-Length: 26\r
          Set-Cookie: HTTPMODULETEST=00000000000000000000000000000001; Path=/\r
          \r
          <html>
          <p>foo</p>
          </html>
          """
      );

      test(socket,
          """
          GET /testCase01 HTTP/1.1\r
          Host: http.module.test\r
          Cookie:
          \r
          """,

          """
          HTTP/1.1 302 FOUND\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Location: /login\r
          Set-Cookie: HTTPMODULETEST=00000000000000000000000000000003; Path=/\r
          \r
          """
      );
    }

    try (Socket socket = newSocket()) {
      test(socket,
          """
          GET /testCase01/ HTTP/1.1\r
          Host: http.module.test\r
          \r
          """,

          """
          HTTP/1.1 302 FOUND\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Location: /login\r
          Set-Cookie: HTTPMODULETEST=00000000000000000000000000000004; Path=/\r
          \r
          """
      );
    }

    try (Socket socket = newSocket()) {
      test(socket,
          """
          GET /testCase01/foo/bar HTTP/1.1\r
          Host: http.module.test\r
          \r
          """,

          """
          HTTP/1.1 302 FOUND\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Location: /login\r
          Set-Cookie: HTTPMODULETEST=00000000000000000000000000000005; Path=/\r
          \r
          """
      );
    }
  }

  private void testCase02(ServerExchange http) {
    Session session;
    session = http.session();

    User user;
    user = session.get(User.class);

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
          Set-Cookie: HTTPMODULETEST=00000000000000000000000000000001; Path=/\r
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

  private void testCase03(ServerExchange http) {
    UriPath path;
    path = http.path();

    List<Segment> segments;
    segments = path.segments();

    String text = segments.stream()
        .skip(1)
        .map(Segment::value)
        .collect(Collectors.joining("/", "", "\n"));

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
          Content-Length: 1\r
          \r
          \n"""
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
          \n"""
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
          foo
          """
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
          foo/bar
          """
      );
    }
  }

  private static class TestCase04 extends HttpModule {
    @Override
    protected final void configure() {
      route(segments(present()), http -> http.okText("ROOT", StandardCharsets.UTF_8));

      route(segments(present(), eq("")), http -> http.movedPermanently("/testCase04"));

      route(segments(present(), eq("foo")), http -> http.okText("foo", StandardCharsets.UTF_8));
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

  private Socket newSocket() throws IOException {
    return TestingHttpServer.newSocket();
  }

  private void test(Socket socket, String request, String expectedResponse) throws IOException {
    TestingHttpServer.test(socket, request, expectedResponse);
  }

}