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
import java.io.UncheckedIOException;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.Clock;
import java.time.Instant;
import java.util.function.Consumer;
import objectos.lang.Stage;
import objectos.lang.Throwables;
import objectos.way.Html;
import objectos.way.Media;
import objectos.way.TestingSingleParagraph;
import objectos.way.Y;
import objectos.y.PathY;
import objectox.http.RequestMethodEnum;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class HttpServerTest {

  private final Path root = PathY.nextDir();

  private HttpServer server;

  private static final class Host1 implements Consumer<HttpRoutes> {
    private final HttpServerTest instance;

    Host1(HttpServerTest instance) {
      this.instance = instance;
    }

    @Override
    public final void accept(HttpRoutes r) {
      r.at("/test/{name}", Http.handler(this::handle));
    }

    private void handle(HttpExchange http) {
      final String methodName;
      methodName = http.pathParam("name");

      Throwable rethrow = null;

      try {
        final Class<? extends HttpServerTest> testClass;
        testClass = HttpServerTest.class;

        final java.lang.reflect.Method handlingMethod;
        handlingMethod = testClass.getDeclaredMethod(methodName, HttpExchange.class);

        handlingMethod.invoke(instance, http);
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
  }

  private static final class Host2 implements Consumer<HttpRoutes> {
    @Override
    public void accept(HttpRoutes r) {
      r.at("/host01", Http.GET, Http.handler(this::host01));

      r.at("/staticFiles03", Http.GET, Http.handler(this::staticFiles03));
    }

    private void host01(HttpExchange http) {
      http.ok(Media.Bytes.textPlain("HOST01"));
    }

    private void staticFiles03(HttpExchange http) {
      http.staticFile(Media.Bytes.textPlain("SF03\n"));
    }
  }

  @BeforeClass
  public void beforeClass() throws Exception {
    final Path dir;
    dir = PathY.nextDir();

    final Path sub;
    sub = dir.resolve("files");

    Files.createDirectory(sub);

    Files.writeString(dir.resolve("staticFiles01"), "default\n");

    Files.writeString(sub.resolve("staticFiles01.txt"), "text\n");

    server = HttpServer.create(opts -> {
      opts.clock(Y.clockFixed());

      opts.noteSink(Y.noteSink());

      opts.stage(Stage.PROD);

      opts.host(host -> {
        host.name("server.test.localhost");

        final Host1 host1;
        host1 = new Host1(this);

        final HttpHandler handler;
        handler = HttpHandler.create(host1);

        host.handler(handler);

        host.staticFiles(files -> {
          files.addDirectory(dir);

          files.contentTypes("""
          .txt: text/plain; charset=utf8
          """);

          files.rootDirectory(root);
        });
      });

      opts.host(host -> {
        host.name("server2.test.localhost");

        final Host2 host2;
        host2 = new Host2();

        final HttpHandler handler;
        handler = HttpHandler.create(host2);

        host.handler(handler);

        host.stage(Stage.DEV);
      });
    });

    setLastModifiedTime(root, "staticFiles01");
    setLastModifiedTime(root, "files/staticFiles01.txt");

    Y.shutdownHook(server);
  }

  private void setLastModifiedTime(Path root, String path) throws IOException {
    final Path target;
    target = root.resolve(path);

    final Clock clock;
    clock = Y.clockFixed();

    final Instant instant;
    instant = clock.instant();

    final FileTime fileTime;
    fileTime = FileTime.from(instant);

    Files.setLastModifiedTime(target, fileTime);
  }

  private void test(String... xchs) throws IOException {
    try (Socket socket = HttpY.socket(server)) {
      for (int idx = 0, len = xchs.length; idx < len;) {
        String req;
        req = xchs[idx++];

        req = req.formatted(server.port());

        final String resp;
        resp = xchs[idx++];

        HttpY.test(socket, req, resp);
      }
    }
  }

  @SuppressWarnings("unused")
  private void testCase01(HttpExchange http) {
    var method = http.method();

    var impl = (RequestMethodEnum) method;

    switch (impl) {
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
    test(
        """
        GET /test/testCase01 HTTP/1.1\r
        Host: server.test.localhost:%d\r
        \r
        """,

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 5\r
        \r
        TC01
        """,

        """
        POST /test/testCase01 HTTP/1.1\r
        Host: server.test.localhost:%d\r
        \r
        """,

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

  @SuppressWarnings("unused")
  private void testCase02(HttpExchange http) {
    var method = http.method();

    var impl = (RequestMethodEnum) method;

    switch (impl) {
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
    test(
        """
        HEAD /test/testCase02 HTTP/1.1\r
        Host: server.test.localhost:%d\r
        \r
        """,

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 5\r
        \r
        """,

        """
        GET /test/testCase02 HTTP/1.1\r
        Host: server.test.localhost:%d\r
        Connection: close\r
        \r
        """,

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

  @SuppressWarnings("unused")
  private void testCase03(HttpExchange http) {
    objectos.http.RequestMethod method = http.method();

    RequestMethodEnum impl = (RequestMethodEnum) method;

    switch (impl) {
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
    test(
        """
        HEAD /test/testCase03 HTTP/1.1\r
        Host: server.test.localhost:%d\r
        \r
        """,

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/html; charset=utf-8\r
        Transfer-Encoding: chunked\r
        \r
        """,

        """
        GET /test/testCase03 HTTP/1.1\r
        Host: server.test.localhost:%d\r
        \r
        """,

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/html; charset=utf-8\r
        Transfer-Encoding: chunked\r
        \r
        <html>
        <p>TC03 GET</p>
        </html>
        """,

        """
        POST /test/testCase03 HTTP/1.1\r
        Host: server.test.localhost:%d\r
        Connection: close\r
        \r
        """,

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
    var method = http.method();

    var impl = (RequestMethodEnum) method;

    switch (impl) {
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
    test(
        """
        GET /test/testCase04 HTTP/1.1\r
        Host: server.test.localhost:%d\r
        \r
        """,

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/html; charset=utf-8\r
        Transfer-Encoding: chunked\r
        \r
        <p>TC04 GET</p>
        """,

        """
        POST /test/testCase04 HTTP/1.1\r
        Host: server.test.localhost:%d\r
        Connection: close\r
        \r
        """,

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
    test(
        """
        GET /test/testCase05 HTTP/1.1\r
        Host: server.test.localhost:%d\r
        Connection: close\r
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
  }

  @SuppressWarnings("unused")
  private void testCase06(HttpExchange http) {
    throw Throwables.trimStackTrace(new RuntimeException("Uh-Oh"), 1);
  }

  @Test
  public void testCase06() throws IOException {
    test(
        """
        GET /test/testCase06 HTTP/1.1\r
        Host: server.test.localhost:%d\r
        Connection: close\r
        \r
        """,

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

  @Test
  public void host01() throws IOException {
    test(
        """
        GET /host01 HTTP/1.1\r
        Host: server2.test.localhost:%d\r
        Connection: close\r
        \r
        """,

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 6\r
        \r
        HOST01\
        """
    );
  }

  @Test(description = "staticFiles::addDirectory")
  public void staticFiles01() throws IOException {
    test(
        """
        GET /staticFiles01 HTTP/1.1\r
        Host: server.test.localhost:%d\r
        \r
        """,

        """
        HTTP/1.1 200 OK\r
        Content-Type: application/octet-stream\r
        Content-Length: 8\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        ETag: 18901e7e8f8-8\r
        \r
        default
        """,

        """
        GET /files/staticFiles01.txt HTTP/1.1\r
        Host: server.test.localhost:%d\r
        \r
        """,

        """
        HTTP/1.1 200 OK\r
        Content-Type: text/plain; charset=utf8\r
        Content-Length: 5\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        ETag: 18901e7e8f8-5\r
        \r
        text
        """
    );
  }

  @SuppressWarnings("unused")
  private void staticFiles02(HttpExchange http) {
    http.staticFile(Media.Bytes.textPlain("TC08\n"));

    try {
      setLastModifiedTime(root, "test/staticFiles02");
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  @Test(description = "HttpExchange::staticFile")
  public void staticFiles02() throws IOException {
    test(
        """
        GET /test/staticFiles02 HTTP/1.1\r
        Host: server.test.localhost:%d\r
        \r
        """,

        """
        HTTP/1.1 200 OK\r
        Content-Type: application/octet-stream\r
        Content-Length: 5\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        ETag: 18901e7e8f8-5\r
        \r
        TC08
        """
    );
  }

  @Test(description = "static files + dev mode")
  public void staticFiles03() throws IOException {
    test(
        """
        GET /staticFiles03 HTTP/1.1\r
        Host: server2.test.localhost:%d\r
        \r
        """,

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 5\r
        \r
        SF03
        """
    );
  }

}