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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;
import objectos.lang.Stage;
import objectos.way.Html;
import objectos.way.Media;
import objectos.way.TestingSingleParagraph;
import objectos.way.Y;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class HttpServerTest implements Consumer<HttpRouting> {

  private HttpServer server;

  @BeforeClass
  public void beforeClass() throws Exception {
    server = HttpServer.create(opts -> {
      opts.clock(Y.clockFixed());

      opts.noteSink(Y.noteSink());

      opts.stage(Stage.PROD);

      opts.host(host -> {
        host.name("server.test.localhost");

        final HttpHandler handler;
        handler = HttpHandler.create(this);

        host.handler(handler);
      });
    });
  }

  @Override
  public final void accept(HttpRouting routing) {
    routing.path("/test/{name}", path -> {
      path.handler(this::handle1);
    });
  }

  private void handle1(HttpExchange http) {
    final String methodName;
    methodName = http.pathParam("name");

    Throwable rethrow = null;

    try {
      final Class<? extends HttpServerTest> testClass;
      testClass = getClass();

      final Method handlingMethod;
      handlingMethod = testClass.getDeclaredMethod(methodName, HttpExchange.class);

      handlingMethod.invoke(this, http);
    } catch (InvocationTargetException e) {
      Throwable cause;
      cause = e.getCause();

      if (cause instanceof RuntimeException re) {
        throw re;
      }

      rethrow = e;
    } catch (Exception e) {
      rethrow = e;
    }

    if (rethrow instanceof Error err) {
      throw err;
    }

    if (rethrow instanceof RuntimeException re) {
      throw re;
    }

    if (rethrow != null) {
      throw new RuntimeException(rethrow);
    }
  }

  @SuppressWarnings("unused")
  private void testCase01(HttpExchange http) {
    switch (http.method()) {
      case GET, HEAD -> testCase01Get(http);

      default -> http.error(HttpStatus.METHOD_NOT_ALLOWED);
    }
  }

  private void testCase01Get(HttpExchange http) {
    final Media.Bytes object;
    object = Media.Bytes.textPlain("TC01\n", StandardCharsets.UTF_8);

    http.ok(object);
  }

  @Test(description = """
  methodMatrix with 1 method:
  - accept declared method
  - reject other methods
  """)
  public void testCase01() throws IOException {
    try (Socket socket = HttpY.socket(server)) {
      HttpY.test(
          socket,

          """
          GET /test/testCase01 HTTP/1.1\r
          Host: server.test.localhost:%d\r
          \r
          """.formatted(server.port()),

          """
          HTTP/1.1 200 OK\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Content-Type: text/plain; charset=utf-8\r
          Content-Length: 5\r
          \r
          TC01
          """
      );

      HttpY.test(
          socket,

          """
          POST /test/testCase01 HTTP/1.1\r
          Host: server.test.localhost:%d\r
          \r
          """.formatted(server.port()),

          """
          HTTP/1.1 405 Method Not Allowed\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Connection: close\r
          Content-Type: text/plain; charset=utf-8\r
          Content-Length: 23\r
          \r
          405 Method Not Allowed
          """
      );
    }
  }

  @SuppressWarnings("unused")
  private void testCase02(HttpExchange http) {
    switch (http.method()) {
      case GET, HEAD -> testCase02Get(http);

      default -> http.error(HttpStatus.METHOD_NOT_ALLOWED);
    }
  }

  private void testCase02Get(HttpExchange http) {
    final Media.Bytes object;
    object = Media.Bytes.textPlain("TC02\n", StandardCharsets.UTF_8);

    http.ok(object);
  }

  @Test(description = """
  GET handler should be used for HEAD requests as well
  """)
  public void testCase02() throws IOException {
    try (Socket socket = HttpY.socket(server)) {
      HttpY.test(
          socket,

          """
          HEAD /test/testCase02 HTTP/1.1\r
          Host: server.test.localhost:%d\r
          \r
          """.formatted(server.port()),

          """
          HTTP/1.1 200 OK\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Content-Type: text/plain; charset=utf-8\r
          Content-Length: 5\r
          \r
          """
      );

      HttpY.test(
          socket,

          """
          GET /test/testCase02 HTTP/1.1\r
          Host: server.test.localhost:%d\r
          Connection: close\r
          \r
          """.formatted(server.port()),

          """
          HTTP/1.1 200 OK\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Content-Type: text/plain; charset=utf-8\r
          Content-Length: 5\r
          \r
          TC02
          """
      );
    }
  }

  @SuppressWarnings("unused")
  private void testCase03(HttpExchange http) {
    switch (http.method()) {
      case GET, HEAD -> testCase03Get(http);

      case POST -> testCase03Post(http);

      default -> http.error(HttpStatus.METHOD_NOT_ALLOWED);
    }
  }

  private void testCase03Get(HttpExchange http) {
    http.ok(new TestingSingleParagraph("TC03 GET"));
  }

  private void testCase03Post(HttpExchange http) {
    http.ok(new TestingSingleParagraph("TC03 POST"));
  }

  @Test(description = """
  It should be possible to send pre-made 200 OK responses
  """)
  public void testCase03() throws IOException, InterruptedException {
    try (Socket socket = HttpY.socket(server)) {
      HttpY.test(
          socket,

          """
          HEAD /test/testCase03 HTTP/1.1\r
          Host: server.test.localhost:%d\r
          \r
          """.formatted(server.port()),

          """
          HTTP/1.1 200 OK\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Content-Type: text/html; charset=utf-8\r
          Transfer-Encoding: chunked\r
          \r
          """
      );

      HttpY.test(
          socket,

          """
          GET /test/testCase03 HTTP/1.1\r
          Host: server.test.localhost:%d\r
          \r
          """.formatted(server.port()),

          """
          HTTP/1.1 200 OK\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Content-Type: text/html; charset=utf-8\r
          Transfer-Encoding: chunked\r
          \r
          <html>
          <p>TC03 GET</p>
          </html>
          """
      );

      HttpY.test(
          socket,

          """
          POST /test/testCase03 HTTP/1.1\r
          Host: server.test.localhost:%d\r
          Connection: close\r
          \r
          """.formatted(server.port()),

          """
          HTTP/1.1 200 OK\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Content-Type: text/html; charset=utf-8\r
          Transfer-Encoding: chunked\r
          \r
          <html>
          <p>TC03 POST</p>
          </html>
          """
      );
    }
  }

  private static final class AttributeTester extends Html.Template {
    private final HttpExchange http;
    private final Class<?> key;

    public AttributeTester(HttpExchange http, Class<?> key) {
      this.http = http;
      this.key = key;
    }

    @Override
    protected final void render() {
      Object o;
      o = http.req(key);

      String text;
      text = String.valueOf(o);

      p(text);
    }
  }

  @SuppressWarnings("unused")
  private void testCase04(HttpExchange http) {
    switch (http.method()) {
      case GET, HEAD -> testCase04Get(http);

      case POST -> testCase04Post(http);

      default -> http.error(HttpStatus.METHOD_NOT_ALLOWED);
    }
  }

  private void testCase04Get(HttpExchange http) {
    http.req(String.class, "TC04 GET");

    http.ok(new AttributeTester(http, String.class));
  }

  private void testCase04Post(HttpExchange http) {
    http.ok(new AttributeTester(http, String.class));
  }

  @Test(description = """
  Request attributes should be reset between requests
  """)
  public void testCase04() throws IOException, InterruptedException {
    try (Socket socket = HttpY.socket(server)) {
      HttpY.test(
          socket,

          """
          GET /test/testCase04 HTTP/1.1\r
          Host: server.test.localhost:%d\r
          \r
          """.formatted(server.port()),

          """
          HTTP/1.1 200 OK\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Content-Type: text/html; charset=utf-8\r
          Transfer-Encoding: chunked\r
          \r
          <p>TC04 GET</p>
          """
      );

      HttpY.test(
          socket,

          """
          POST /test/testCase04 HTTP/1.1\r
          Host: server.test.localhost:%d\r
          Connection: close\r
          \r
          """.formatted(server.port()),

          """
          HTTP/1.1 200 OK\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Content-Type: text/html; charset=utf-8\r
          Transfer-Encoding: chunked\r
          \r
          <p>null</p>
          """
      );
    }
  }

  @SuppressWarnings("unused")
  private void testCase05(HttpExchange http) {
    @SuppressWarnings("serial")
    class NotFoundException extends Http.AbstractHandlerException {
      @Override
      public void handle(HttpExchange http) {
        http.error(HttpStatus.NOT_FOUND);
      }
    }

    throw new NotFoundException();
  }

  @Test(description = """
  An Http.AbstractHandlerException caught by the loop should call its handle method
  """)
  public void testCase05() throws IOException {
    try (Socket socket = HttpY.socket(server)) {
      HttpY.test(
          socket,

          """
          GET /test/testCase05 HTTP/1.1\r
          Host: server.test.localhost:%d\r
          Connection: close\r
          \r
          """.formatted(server.port()),

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
    }
  }

  @SuppressWarnings("unused")
  private void testCase06(HttpExchange http) {
    throw Y.trimStackTrace(new RuntimeException("Uh-Oh"), 1);
  }

  @Test
  public void testCase06() throws IOException {
    try (Socket socket = HttpY.socket(server)) {
      HttpY.test(
          socket,

          """
          GET /test/testCase06 HTTP/1.1\r
          Host: server.test.localhost:%d\r
          Connection: close\r
          \r
          """.formatted(server.port()),

          """
          HTTP/1.1 500 Internal Server Error\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Connection: close\r
          Content-Type: text/plain; charset=utf-8\r
          Content-Length: 82\r
          \r
          The server encountered an internal error and was unable to complete your request.
          """
      );
    }
  }

}