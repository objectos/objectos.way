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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import objectos.way.Http.Exchange;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class HttpServerTest extends Http.Module {

  @BeforeClass
  public void beforeClass() throws Exception {
    TestingHttpServer.bindHttpServerTest(this);
  }

  @Override
  protected final void configure() {
    route("/test/:name", this::handle1);
  }

  private void handle1(Http.Exchange http) {
    Http.Request.Target.Path path;
    path = http.path();

    String methodName;
    methodName = path.get("name");

    try {
      Class<? extends HttpServerTest> testClass;
      testClass = getClass();

      Method handlingMethod;
      handlingMethod = testClass.getDeclaredMethod(methodName, Http.Exchange.class);

      handlingMethod.invoke(this, http);
    } catch (InvocationTargetException e) {
      Throwable cause;
      cause = e.getCause();

      if (cause instanceof RuntimeException re) {
        throw re;
      }

      http.internalServerError(e);
    } catch (Exception e) {
      http.internalServerError(e);
    }
  }

  @SuppressWarnings("unused")
  private void testCase01(Http.Exchange http) {
    http.methodMatrix(Http.GET, this::testCase01Get);
  }

  private void testCase01Get(Http.Exchange http) {
    http.status(Http.OK);
    http.dateNow();
    http.header(Http.CONTENT_TYPE, "text/plain");
    http.header(Http.CONTENT_LENGTH, 5);
    http.send("TC01\n".getBytes(StandardCharsets.UTF_8));
  }

  @Test(description = """
  methodMatrix with 1 method:
  - accept declared method
  - reject other methods
  """)
  public void testCase01() throws IOException {
    try (Socket socket = newSocket()) {
      test(socket,
          """
          GET /test/testCase01 HTTP/1.1\r
          Host: http.server.test\r
          \r
          """,

          """
          HTTP/1.1 200 OK\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Content-Type: text/plain\r
          Content-Length: 5\r
          \r
          TC01
          """
      );

      test(socket,
          """
          POST /test/testCase01 HTTP/1.1\r
          Host: http.server.test\r
          Connection: close\r
          \r
          """,

          """
          HTTP/1.1 405 METHOD NOT ALLOWED\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Connection: close\r
          \r
          """
      );
    }
  }

  @SuppressWarnings("unused")
  private void testCase02(Http.Exchange http) {
    http.methodMatrix(Http.GET, this::testCase02Get);
  }

  private void testCase02Get(Http.Exchange http) {
    http.status(Http.OK);
    http.dateNow();
    http.header(Http.CONTENT_TYPE, "text/plain");
    http.header(Http.CONTENT_LENGTH, 5);
    http.send("TC02\n".getBytes(StandardCharsets.UTF_8));
  }

  @Test(description = """
  GET handler should be used for HEAD requests as well
  """)
  public void testCase02() throws IOException {
    try (Socket socket = newSocket()) {
      test(socket,
          """
          HEAD /test/testCase02 HTTP/1.1\r
          Host: http.server.test\r
          \r
          """,

          """
          HTTP/1.1 200 OK\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Content-Type: text/plain\r
          Content-Length: 5\r
          \r
          """
      );

      test(socket,
          """
          GET /test/testCase02 HTTP/1.1\r
          Host: http.server.test\r
          \r
          """,

          """
          HTTP/1.1 200 OK\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Content-Type: text/plain\r
          Content-Length: 5\r
          \r
          TC02
          """
      );
    }
  }

  @SuppressWarnings("unused")
  private void testCase03(Http.Exchange http) {
    http.methodMatrix(
        Http.GET, this::testCase03Get,
        Http.POST, this::testCase03Post
    );
  }

  private void testCase03Get(Http.Exchange http) {
    http.ok(new TestingSingleParagraph("TC03 GET"));
  }

  private void testCase03Post(Http.Exchange http) {
    http.ok(new TestingSingleParagraph("TC03 POST"));
  }

  @Test(description = """
  It should be possible to send pre-made 200 OK responses
  """)
  public void testCase03() throws IOException {
    try (Socket socket = newSocket()) {
      test(socket,
          """
          HEAD /test/testCase03 HTTP/1.1\r
          Host: http.server.test\r
          \r
          """,

          """
          HTTP/1.1 200 OK\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Content-Type: text/html; charset=utf-8\r
          Transfer-Encoding: chunked\r
          \r
          """
      );

      test(socket,
          """
          GET /test/testCase03 HTTP/1.1\r
          Host: http.server.test\r
          \r
          """,

          """
          HTTP/1.1 200 OK\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Content-Type: text/html; charset=utf-8\r
          Transfer-Encoding: chunked\r
          \r
          1f\r
          <html>
          <p>TC03 GET</p>
          </html>
          \r
          0\r
          \r
          """
      );

      test(socket,
          """
          POST /test/testCase03 HTTP/1.1\r
          Host: http.server.test\r
          \r
          """,

          """
          HTTP/1.1 200 OK\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Content-Type: text/html; charset=utf-8\r
          Transfer-Encoding: chunked\r
          \r
          20\r
          <html>
          <p>TC03 POST</p>
          </html>
          \r
          0\r
          \r
          """
      );
    }
  }

  private static final class AttributeTester extends Html.Template {
    private final Exchange http;
    private final Class<?> key;

    public AttributeTester(Exchange http, Class<?> key) {
      this.http = http;
      this.key = key;
    }

    @Override
    protected final void render() {
      Object o;
      o = http.get(key);

      String text;
      text = String.valueOf(o);

      p(text);
    }
  }

  @SuppressWarnings("unused")
  private void testCase04(Http.Exchange http) {
    http.methodMatrix(
        Http.GET, this::testCase04Get,
        Http.POST, this::testCase04Post
    );
  }

  private void testCase04Get(Http.Exchange http) {
    http.set(String.class, "TC04 GET");

    http.ok(new AttributeTester(http, String.class));
  }

  private void testCase04Post(Http.Exchange http) {
    http.ok(new AttributeTester(http, String.class));
  }

  @Test(description = """
  Request attributes should be reset between requests
  """)
  public void testCase04() throws IOException {
    try (Socket socket = newSocket()) {
      test(socket,
          """
          GET /test/testCase04 HTTP/1.1\r
          Host: http.server.test\r
          \r
          """,

          """
          HTTP/1.1 200 OK\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Content-Type: text/html; charset=utf-8\r
          Transfer-Encoding: chunked\r
          \r
          10\r
          <p>TC04 GET</p>
          \r
          0\r
          \r
          """
      );

      test(socket,
          """
          POST /test/testCase04 HTTP/1.1\r
          Host: http.server.test\r
          \r
          """,

          """
          HTTP/1.1 200 OK\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Content-Type: text/html; charset=utf-8\r
          Transfer-Encoding: chunked\r
          \r
          c\r
          <p>null</p>
          \r
          0\r
          \r
          """
      );
    }
  }

  @SuppressWarnings("unused")
  private void testCase05(Http.Exchange http) {
    @SuppressWarnings("serial")
    class NotFoundException extends Http.AbstractHandlerException {
      @Override
      public void handle(Http.Exchange http) {
        http.notFound();
      }
    }

    throw new NotFoundException();
  }

  @Test(description = """
  An Http.AbstractHandlerException caught by the loop should call its handle method
  """)
  public void testCase05() throws IOException {
    try (Socket socket = newSocket()) {
      test(socket,
          """
          GET /test/testCase05 HTTP/1.1\r
          Host: http.server.test\r
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