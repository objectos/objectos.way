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

import static org.testng.Assert.assertNotNull;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import objectos.way.Http.Request.Target.Query;
import objectos.way.TestingRandom.SequentialRandom;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class HttpModuleTest extends Http.Module {

  private record Box(String value) {}

  private record User(String login) {}
  
  @SuppressWarnings("serial")
  private static class TestException extends RuntimeException {}

  private AppSessionStore sessionStore;

  private SequentialRandom random;

  @BeforeClass
  public void beforeClass() {
    AppSessionStore sessionStore;
    sessionStore = new AppSessionStore();

    sessionStore.cookieName("HTTPMODULETEST");

    random = new SequentialRandom();

    sessionStore.random(random);

    this.sessionStore = sessionStore;

    TestingHttpServer.bindHttpModuleTest(this);
  }

  @BeforeMethod
  public void beforeMethod() {
    sessionStore.clear();

    WebSession session;
    session = new WebSession("TEST_COOKIE");

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
    
    // matches: /testCase05/img, /testCase05/img/, /testCase05/img/a, /testCase05/img/b
    route(segments(eq("testCase05"), eq("img"), zeroOrMore()), this::testCase05);
    
    // matches: /testCase06/, /testCase06/foo, /testCase06/foo/bar
    // but not: /testCase06
    route(segments(eq("testCase06"), oneOrMore()), this::testCase06);
    
    route(path("/testCase07/before"), this::testCase07);
    
    intercept(this::testCase07);
    
    route(path("/testCase07/after"), this::testCase07);
  }

  private void testCase01(Http.Exchange http) {
    Http.Request.Target.Path path;
    path = http.path();

    List<Http.Request.Target.Path.Segment> segments;
    segments = path.segments();

    Http.Request.Target.Path.Segment second;
    second = segments.get(1);

    String text;
    text = second.value();

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
          Set-Cookie: HTTPMODULETEST=00000000000000000000000000000001; Path=/\r
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

  private void testCase02(Http.Exchange http) {
    Web.Session session;
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

  private void testCase03(Http.Exchange http) {
    Http.Request.Target.Path path;
    path = http.path();

    List<Http.Request.Target.Path.Segment> segments;
    segments = path.segments();

    String text = segments.stream()
        .skip(1)
        .map(Http.Request.Target.Path.Segment::value)
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

  private static class TestCase04 extends Http.Module {
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

  private void testCase05(Http.Exchange http) {
    Http.Request.Target.Path path;
    path = http.path();

    List<Http.Request.Target.Path.Segment> segments;
    segments = path.segments();

    String text = segments.stream()
        .skip(2)
        .map(Http.Request.Target.Path.Segment::value)
        .collect(Collectors.joining("/", "", "\n"));

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
          a
          """
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
          Content-Length: 1\r
          \r
          \n"""
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
          \n"""
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
          a/b
          """
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
          a/b/c
          """
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
  
  private void testCase06(Http.Exchange http) {
    Http.Request.Target.Path path;
    path = http.path();

    List<Http.Request.Target.Path.Segment> segments;
    segments = path.segments();

    String text = segments.stream()
        .skip(1)
        .map(Http.Request.Target.Path.Segment::value)
        .collect(Collectors.joining("/", "", "\n"));

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
          \n"""
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
          foo
          """
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
          foo/bar
          """
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
  
  private void testCase07(Http.Exchange http) {
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
      Query query = http.query();

      String value;
      value = query.get("value");

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