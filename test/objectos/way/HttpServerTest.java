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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Consumer;
import objectos.way.Http.Exchange;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class HttpServerTest implements Consumer<Http.Routing> {

  @BeforeClass
  public void beforeClass() throws Exception {
    TestingHttpServer.bindHttpServerTest(this);
  }

  @Override
  public final void accept(Http.Routing routing) {
    routing.path("/test/:name", path -> {
      path.handler(this::handle1);
    });
  }

  private void handle1(Http.Exchange http) {
    String methodName;
    methodName = http.pathParam("name");

    Throwable rethrow = null;

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
      throw new Http.InternalServerException(rethrow);
    }
  }

  @SuppressWarnings("unused")
  private void testCase01(Http.Exchange http) {
    switch (http.method()) {
      case GET, HEAD -> testCase01Get(http);

      default -> http.respond(Http.ResponseMessage.methodNotAllowed(Http.Method.GET, Http.Method.HEAD));
    }
  }

  private void testCase01Get(Http.Exchange http) {
    final Media.Bytes object;
    object = Media.Bytes.textPlain("TC01\n", StandardCharsets.UTF_8);

    http.respond(Http.Status.OK, object);
  }

  @Test(description = """
  methodMatrix with 1 method:
  - accept declared method
  - reject other methods
  """)
  public void testCase01() throws IOException {
    Testing.test(
        Testing.httpClient(
            "/test/testCase01",

            builder -> builder.headers(
                "Host", "http.server.test"
            )
        ),

        """
        HTTP/1.1 200
        content-length: 5
        content-type: text/plain; charset=utf-8
        date: Wed, 28 Jun 2023 12:08:43 GMT

        TC01
        """
    );

    Testing.test(
        Testing.httpClient(
            "/test/testCase01",

            builder -> builder.POST(BodyPublishers.noBody()).headers(
                "Host", "http.server.test"
            )
        ),

        """
        HTTP/1.1 405
        allow: GET, HEAD
        content-length: 0
        date: Wed, 28 Jun 2023 12:08:43 GMT

        """
    );
  }

  @SuppressWarnings("unused")
  private void testCase02(Http.Exchange http) {
    switch (http.method()) {
      case GET, HEAD -> testCase02Get(http);

      default -> http.respond(Http.ResponseMessage.methodNotAllowed(Http.Method.GET, Http.Method.HEAD));
    }
  }

  private void testCase02Get(Http.Exchange http) {
    final Media.Bytes object;
    object = Media.Bytes.textPlain("TC02\n", StandardCharsets.UTF_8);

    http.respond(Http.Status.OK, object);
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
          Content-Type: text/plain; charset=utf-8\r
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
          Content-Type: text/plain; charset=utf-8\r
          Content-Length: 5\r
          \r
          TC02
          """
      );
    }
  }

  @SuppressWarnings("unused")
  private void testCase03(Http.Exchange http) {
    switch (http.method()) {
      case GET, HEAD -> testCase03Get(http);

      case POST -> testCase03Post(http);

      default -> http.respond(Http.ResponseMessage.methodNotAllowed(Http.Method.GET, Http.Method.HEAD, Http.Method.POST));
    }
  }

  private void testCase03Get(Http.Exchange http) {
    http.respond(Http.Status.OK, new TestingSingleParagraph("TC03 GET"));
  }

  private void testCase03Post(Http.Exchange http) {
    http.respond(Http.Status.OK, new TestingSingleParagraph("TC03 POST"));
  }

  @Test(description = """
  It should be possible to send pre-made 200 OK responses
  """)
  public void testCase03() throws IOException, InterruptedException {
    HttpResponse<String> response;
    response = Testing.httpClient(
        "/test/testCase03",

        builder -> builder.HEAD().headers(
            "Host", "http.server.test"
        )
    );

    assertEquals(response.statusCode(), 200);
    assertEquals(response.headers().allValues("Content-Type"), List.of("text/html; charset=utf-8"));
    assertEquals(response.headers().allValues("Date"), List.of("Wed, 28 Jun 2023 12:08:43 GMT"));
    assertEquals(response.body(), "");

    response = Testing.httpClient(
        "/test/testCase03",

        builder -> builder.headers(
            "Host", "http.server.test"
        )
    );

    assertEquals(response.statusCode(), 200);
    assertEquals(response.headers().allValues("Content-Type"), List.of("text/html; charset=utf-8"));
    assertEquals(response.headers().allValues("Date"), List.of("Wed, 28 Jun 2023 12:08:43 GMT"));
    assertEquals(response.body(), """
    <html>
    <p>TC03 GET</p>
    </html>
    """);

    response = Testing.httpClient(
        "/test/testCase03",

        builder -> builder.POST(BodyPublishers.noBody()).headers(
            "Host", "http.server.test"
        )
    );

    assertEquals(response.statusCode(), 200);
    assertEquals(response.headers().allValues("Content-Type"), List.of("text/html; charset=utf-8"));
    assertEquals(response.headers().allValues("Date"), List.of("Wed, 28 Jun 2023 12:08:43 GMT"));
    assertEquals(response.body(), """
    <html>
    <p>TC03 POST</p>
    </html>
    """);
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
    switch (http.method()) {
      case GET, HEAD -> testCase04Get(http);

      case POST -> testCase04Post(http);

      default -> http.respond(Http.ResponseMessage.methodNotAllowed(Http.Method.GET, Http.Method.HEAD, Http.Method.POST));
    }
  }

  private void testCase04Get(Http.Exchange http) {
    http.set(String.class, "TC04 GET");

    http.respond(Http.Status.OK, new AttributeTester(http, String.class));
  }

  private void testCase04Post(Http.Exchange http) {
    http.respond(Http.Status.OK, new AttributeTester(http, String.class));
  }

  @Test(description = """
  Request attributes should be reset between requests
  """)
  public void testCase04() throws IOException, InterruptedException {
    Testing.test(
        Testing.httpClient(
            "/test/testCase04",

            builder -> builder.headers(
                "Host", "http.server.test"
            )
        ),

        """
        HTTP/1.1 200
        content-length: 16
        content-type: text/html; charset=utf-8
        date: Wed, 28 Jun 2023 12:08:43 GMT

        <p>TC04 GET</p>
        """
    );

    Testing.test(
        Testing.httpClient(
            "/test/testCase04",

            builder -> builder.POST(BodyPublishers.noBody()).headers(
                "Host", "http.server.test"
            )
        ),

        """
        HTTP/1.1 200
        content-length: 12
        content-type: text/html; charset=utf-8
        date: Wed, 28 Jun 2023 12:08:43 GMT

        <p>null</p>
        """
    );
  }

  @SuppressWarnings("unused")
  private void testCase05(Http.Exchange http) {
    @SuppressWarnings("serial")
    class NotFoundException extends Http.AbstractHandlerException {
      @Override
      public void handle(Http.Exchange http) {
        http.respond(Http.Status.NOT_FOUND, new TestingSingleParagraph("NOT FOUND"));
      }
    }

    throw new NotFoundException();
  }

  @Test(description = """
  An Http.AbstractHandlerException caught by the loop should call its handle method
  """)
  public void testCase05() {
    Testing.test(
        Testing.httpClient(
            "/test/testCase05",

            builder -> builder.headers(
                "Host", "http.server.test"
            )
        ),

        """
        HTTP/1.1 404
        content-length: 32
        content-type: text/html; charset=utf-8
        date: Wed, 28 Jun 2023 12:08:43 GMT

        <html>
        <p>NOT FOUND</p>
        </html>
        """
    );
  }

  private Socket newSocket() throws IOException {
    return TestingHttpServer.newSocket();
  }

  private void test(Socket socket, String request, String expectedResponse) throws IOException {
    TestingHttpServer.test(socket, request, expectedResponse);
  }

}