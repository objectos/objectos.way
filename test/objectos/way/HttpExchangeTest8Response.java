/*
 * Copyright (C) 2025 Objectos Software LTDA.
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
import java.io.UncheckedIOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.function.Consumer;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HttpExchangeTest8Response {

  // 2xx responses

  @Test(description = "ok(Media.Bytes): fits in buffer")
  public void okMediaBytes01() {
    get(
        http -> http.ok(Media.Bytes.textPlain("1\n")),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 2\r
        \r
        1
        """
    );
  }

  @Test(description = "ok(Media.Bytes): does not fit in buffer")
  public void okMediaBytes02() {
    final String veryLong;
    veryLong = ".".repeat(2048);

    get(
        http -> http.ok(Media.Bytes.textPlain(veryLong)),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 2048\r
        \r
        %s\
        """.formatted(veryLong)
    );
  }

  @Test(description = "ok(Media.Bytes): HEAD")
  public void okMediaBytes03() {
    final String veryLong;
    veryLong = ".".repeat(2048);

    head(
        http -> http.ok(Media.Bytes.textPlain(veryLong)),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 2048\r
        \r
        """
    );
  }

  // 3xx responses

  @Test
  public void found01() {
    get(
        http -> http.found("/login"),

        """
        HTTP/1.1 302 Found\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Length: 0\r
        Location: /login\r
        \r
        """
    );
  }

  @Test
  public void found02() {
    get(
        http -> http.found("/product/café/😀"),

        """
        HTTP/1.1 302 Found\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Length: 0\r
        Location: /product/caf%C3%A9/%F0%9F%98%80\r
        \r
        """
    );
  }

  @Test
  public void seeOther01() {
    get(
        http -> http.seeOther("/page"),

        """
        HTTP/1.1 303 See Other\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Length: 0\r
        Location: /page\r
        \r
        """
    );
  }

  @Test
  public void seeOther02() {
    get(
        http -> http.seeOther("/product/café/😀"),

        """
        HTTP/1.1 303 See Other\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Length: 0\r
        Location: /product/caf%C3%A9/%F0%9F%98%80\r
        \r
        """
    );
  }

  // 4xx responses

  @Test(description = "badRequest(Media.Bytes)")
  public void badRequest01() {
    post(
        http -> http.badRequest(Media.Bytes.textPlain("BAD\n")),

        """
        HTTP/1.1 400 Bad Request\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 4\r
        \r
        BAD
        """
    );
  }

  @Test(description = "notFound(Media.Bytes)")
  public void notFound01() {
    post(
        http -> http.notFound(Media.Bytes.textPlain("NOT\n")),

        """
        HTTP/1.1 404 Not Found\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 4\r
        \r
        NOT
        """
    );
  }

  @Test(description = "allow(Http.Method...)")
  public void allow01() {
    post(
        http -> http.allow(Http.Method.GET, Http.Method.HEAD),

        """
        HTTP/1.1 405 Method Not Allowed\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Allow: GET, HEAD\r
        Content-Length: 0\r
        \r
        """
    );
  }

  private void empty01(HttpExchange http) {
    http.status(Http.Status.NOT_MODIFIED);
    http.header(Http.HeaderName.DATE, http.now());
    http.header(Http.HeaderName.ETAG, "some%hash");
    http.send();
  }

  @Test(description = "Empty response body: support Web.Resources")
  public void empty01() {
    test(
        """
        GET /atom.xml HTTP/1.1\r
        Host: www.example.com\r
        If-None-Match: some%hash\r
        Connection: close\r
        \r
        """,

        this::empty01,

        """
        HTTP/1.1 304 Not Modified\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        ETag: some%hash\r
        \r
        """
    );
  }

  private Path file01;

  private void file01(HttpExchange http) {
    http.status(Http.Status.OK);
    http.header(Http.HeaderName.DATE, http.now());
    http.header(Http.HeaderName.CONTENT_TYPE, "text/plain; charset=utf-8");
    http.header(Http.HeaderName.CONTENT_LENGTH, 1024);
    http.send(file01);
  }

  @Test(description = "file: GET")
  public void file01() {
    final String contents;
    contents = "x".repeat(1024);

    file01 = Y.nextTempFile(contents, StandardCharsets.UTF_8);

    get(
        this::file01,

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 1024\r
        \r
        %s\
        """.formatted(contents)
    );
  }

  private Path file02;

  private void file02(HttpExchange http) {
    http.status(Http.Status.OK);
    http.header(Http.HeaderName.DATE, http.now());
    http.header(Http.HeaderName.CONTENT_TYPE, "text/plain; charset=utf-8");
    http.header(Http.HeaderName.CONTENT_LENGTH, 1024);
    http.send(file02);
  }

  @Test(description = "file: HEAD")
  public void file02() {
    final String contents;
    contents = "x".repeat(1024);

    file02 = Y.nextTempFile(contents, StandardCharsets.UTF_8);

    head(
        this::file02,

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 1024\r
        \r
        """
    );
  }

  @Test(description = "Disallow request methods after response is sent")
  public void state01() {
    state(Http.Exchange::method);
    state(Http.Exchange::path);
    state(http -> http.queryParam("x"));
    state(http -> http.queryParamAll("x"));
    state(http -> http.queryParamNames());
    state(Http.Exchange::version);

    state(http -> http.header(Http.HeaderName.ACCEPT_ENCODING));

    state(Http.Exchange::bodyInputStream);
  }

  private void get(Consumer<HttpExchange> handler, String expectedResponse) {
    test("""
    GET /test HTTP/1.1\r
    Host: www.objectos.com.br\r
    Connection: close\r
    \r
    """, handler, expectedResponse);
  }

  private void head(Consumer<HttpExchange> handler, String expectedResponse) {
    test("""
    HEAD /test HTTP/1.1\r
    Host: www.objectos.com.br\r
    Connection: close\r
    \r
    """, handler, expectedResponse);
  }

  private void post(Consumer<HttpExchange> handler, String expectedResponse) {
    test("""
    POST /test HTTP/1.1\r
    Host: www.objectos.com.br\r
    Content-Length: 24\r
    Content-Type: application/x-www-form-urlencoded\r
    Connection: close\r
    \r
    email=user%40example.com\
    """, handler, expectedResponse);
  }

  @FunctionalInterface
  private interface ThrowingConsumer {
    void accept(HttpExchange http) throws IOException;
  }

  private void state(ThrowingConsumer handler) {
    final Socket socket;
    socket = Y.socket("""
    GET /test HTTP/1.1\r
    Host: www.objectos.com.br\r
    Connection: close\r
    \r
    """);

    try (HttpExchange http = new HttpExchange(socket, 256, 512, TestingClock.FIXED, TestingNoteSink.INSTANCE)) {
      assertEquals(http.shouldHandle(), true);

      http.ok(Media.Bytes.textPlain("OK"));

      handler.accept(http);

      Assert.fail("It should have thrown");
    } catch (IllegalStateException expected) {
      final String message;
      message = expected.getMessage();

      assertEquals(message, """
      This request method can only be invoked:
      - after a successful shouldHandle() operation; and
      - before a response has been sent.
      """);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private void test(String request, Consumer<HttpExchange> handler, String expectedResponse) {
    final Socket socket;
    socket = Y.socket(request);

    try (HttpExchange http = new HttpExchange(socket, 256, 512, TestingClock.FIXED, TestingNoteSink.INSTANCE)) {
      assertEquals(http.shouldHandle(), true);

      handler.accept(http);

      assertEquals(http.shouldHandle(), false);

      assertEquals(Y.toString(socket), expectedResponse);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

}