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

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import objectos.way.Y;
import objectos.y.HttpClientY;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ServerTest {

  private Server server;

  @BeforeClass
  public void beforeClass() throws Exception {
    server = Server.create(opts -> {
      opts.host(host -> {
        host.name("server.test.localhost");

        final ServerTestHost1 host1;
        host1 = new ServerTestHost1();

        final Handler handler;
        handler = Handler.create(host1);

        host.handler(handler);
      });

      opts.noteSink(Y.noteSink());
    });

    Y.shutdownHook(server);
  }

  @Test(description = """
  Unmatched host
  """)
  public void general01() throws IOException {
    final URI uri;
    uri = HttpClientY.uri(server, "/");

    final HttpRequest req;
    req = HttpRequest.newBuilder(uri).header("Host", "server.test.localhost").build();

    final HttpResponse<String> resp;
    resp = HttpClientY.send(req, BodyHandlers.ofString());

    assertEquals(resp.statusCode(), 400);
    assertEquals(resp.body(), "Invalid host: server.test.localhost");
  }

  /*
  
  @SuppressWarnings("unused")
  private void testCase02(HttpExchange http) {
    var method = http.method();

    var impl = (RequestMethodEnum) method;

    switch (impl) {
      case GET, HEAD -> testCase02Get(http);

      default -> http.error(Status.METHOD_NOT_ALLOWED);
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

      default -> http.error(Status.METHOD_NOT_ALLOWED);
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

      default -> http.error(Status.METHOD_NOT_ALLOWED);
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
        http.error(Status.NOT_FOUND);
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

  */
}