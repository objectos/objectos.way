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
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import objectos.script.JsLibrary;
import objectos.way.Y;
import objectos.y.HttpClientY;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ServerTest {

  private Server server;

  private String mainHost;

  @BeforeClass
  public void beforeClass() throws IOException {
    server = Server.create(opts -> {
      opts.host(new ServerTestHostMain());

      opts.noteSink(Y.noteSink());
    });

    mainHost = "main.server.test:" + server.port();

    Y.shutdownHook(server);
  }

  private URI uri(String path) {
    return HttpClientY.uri(server, path);
  }

  @Test(description = """
  it should 400 on unmatched host
  """)
  public void main01() {
    ServerY.resp(
        req -> {
          req.GET();
          req.uri(uri("/"));
          req.header("Host", "unknown");
        },

        BodyHandlers.ofString(),

        resp -> {
          assertEquals(resp.statusCode(), 400);
          assertEquals(resp.body(), "Invalid host: unknown");
        }
    );
  }

  @Test(description = """
  it should redirect '/' to '/index.html'
  """)
  public void main02() {
    ServerY.resp(
        req -> {
          req.GET();
          req.uri(uri("/"));
          req.header("Host", mainHost);
        },

        BodyHandlers.ofString(),

        resp -> {
          assertEquals(resp.statusCode(), 301);
          assertEquals(resp.headers().firstValue("Location").get(), "/index.html");
        }
    );
  }

  @Test(description = """
  GET /index.html should return 200 OK
  """)
  public void main03() {
    ServerY.resp(
        req -> {
          req.GET();
          req.uri(uri("/index.html"));
          req.header("Host", mainHost);
        },

        BodyHandlers.ofString(),

        resp -> {
          assertEquals(resp.statusCode(), 200);
          assertEquals(resp.body(), """
          <!DOCTYPE html>
          <h1>home</h1>
          """);
        }
    );
  }

  @Test(description = """
  HEAD /index.html should return 200 OK
  """)
  public void main04() {
    ServerY.resp(
        req -> {
          req.HEAD();
          req.uri(uri("/index.html"));
          req.header("Host", mainHost);
        },

        BodyHandlers.ofString(),

        resp -> {
          assertEquals(resp.statusCode(), 200);
          assertEquals(resp.body(), "");
        }
    );
  }

  @Test(description = """
  Other methods to /index.html should return 404
  """)
  public void main05() {
    ServerY.resp(
        req -> {
          req.POST(BodyPublishers.noBody());
          req.uri(uri("/index.html"));
          req.header("Host", mainHost);
        },

        BodyHandlers.ofString(),

        resp -> {
          assertEquals(resp.statusCode(), 404);
        }
    );
  }

  @Test(description = """
  GET /i-do-not-exist should return 404
  """)
  public void main06() {
    ServerY.resp(
        req -> {
          req.POST(BodyPublishers.noBody());
          req.uri(uri("/i-do-not-exist"));
          req.header("Host", mainHost);
        },

        BodyHandlers.ofString(),

        resp -> {
          assertEquals(resp.statusCode(), 404);
        }
    );
  }

  private String etag;

  @Test(description = """
  StaticFile:
  - first request: 200
  - second request: 304
  """)
  public void main07() {
    ServerY.resp(
        req -> {
          req.GET();
          req.uri(uri("/script.js"));
          req.header("Host", mainHost);
        },

        BodyHandlers.ofString(),

        resp -> {
          assertEquals(resp.statusCode(), 200);
          etag = resp.headers().firstValue("ETag").get();
          assertEquals(resp.body(), JsLibrary.of().toString());
        }
    );

    ServerY.resp(
        req -> {
          req.GET();
          req.uri(uri("/script.js"));
          req.header("Host", mainHost);
          req.header("If-None-Match", etag);
        },

        BodyHandlers.ofString(),

        resp -> {
          assertEquals(resp.statusCode(), 304);
          assertEquals(resp.body(), "");
        }
    );
  }

  /*

  @SuppressWarnings("unused")
  private void main03(HttpExchange http) {
    objectos.http.RequestMethod method = http.method();
  
    RequestMethodEnum impl = (RequestMethodEnum) method;
  
    switch (impl) {
      case GET, HEAD -> main03Get(http);
  
      case POST -> main03Post(http);
  
      default -> http.error(Status.METHOD_NOT_ALLOWED);
    }
  }
  
  private void main03Get(HttpExchange http) {
    http.ok(new TestingSingleParagraph("TC03 GET"));
  }
  
  private void main03Post(HttpExchange http) {
    http.ok(new TestingSingleParagraph("TC03 POST"));
  }
  
  @Test(description = """
  It should be possible to send pre-made 200 OK responses
  """)
  public void main03() throws IOException, InterruptedException {
    test(
        """
        HEAD /test/main03 HTTP/1.1\r
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
        GET /test/main03 HTTP/1.1\r
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
        POST /test/main03 HTTP/1.1\r
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
  private void main04(HttpExchange http) {
    var method = http.method();
  
    var impl = (RequestMethodEnum) method;
  
    switch (impl) {
      case GET, HEAD -> main04Get(http);
  
      case POST -> main04Post(http);
  
      default -> http.error(Status.METHOD_NOT_ALLOWED);
    }
  }
  
  private void main04Get(HttpExchange http) {
    http.req(String.class, "TC04 GET");
  
    http.ok(new AttributeTester(http, String.class));
  }
  
  private void main04Post(HttpExchange http) {
    http.ok(new AttributeTester(http, String.class));
  }
  
  @Test(description = """
  Request attributes should be reset between requests
  """)
  public void main04() throws IOException, InterruptedException {
    test(
        """
        GET /test/main04 HTTP/1.1\r
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
        POST /test/main04 HTTP/1.1\r
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
  private void main05(HttpExchange http) {
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
  public void main05() throws IOException {
    test(
        """
        GET /test/main05 HTTP/1.1\r
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
  private void main06(HttpExchange http) {
    throw Throwables.trimStackTrace(new RuntimeException("Uh-Oh"), 1);
  }
  
  @Test
  public void main06() throws IOException {
    test(
        """
        GET /test/main06 HTTP/1.1\r
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