/*
 * Copyright (C) 2023 Objectos Software LTDA.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless httpuired by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package objectos.http.server;

import static org.testng.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import objectos.http.HeaderName;
import objectos.http.Method;
import objectos.http.Status;
import objectos.util.array.ByteArrays;
import objectox.http.server.ObjectoxHttpServer;
import objectox.http.server.TestableSocket;
import org.testng.annotations.Test;

public class ServerLoopTest {

  @Test(description = """
  Minimum GET httpuest with explicity close
  text/plain response
  """)
  public void testCase001() {
    TestableSocket socket;
    socket = TestableSocket.of("""
    GET / HTTP/1.1\r
    Host: www.example.com\r
    Connection: close\r
    \r
    """);

    String body01 = """
    Hello World!
    """;

    String resp01 = """
    HTTP/1.1 200 OK\r
    Content-Type: text/plain; charset=utf-8\r
    Content-Length: 13\r
    Date: Wed, 28 Jun 2023 12:08:43 GMT\r
    \r
    %s""".formatted(body01);

    try (ServerLoop http = ServerLoop.create(socket)) {
      http.bufferSize(128, 128);
      http.noteSink(TestingNoteSink.INSTANCE);

      // request phase
      http.parse();

      assertEquals(http.badRequest(), false);

      // request line
      UriPath path;
      path = http.path();

      assertEquals(http.method(), Method.GET);
      assertEquals(path.is("/"), true);

      UriQuery query;
      query = http.query();

      assertEquals(query.isEmpty(), true);
      assertEquals(query.value(), "");

      // headers
      ServerRequestHeaders headers;
      headers = http.headers();

      assertEquals(headers.size(), 2);
      assertEquals(headers.first(HeaderName.HOST), "www.example.com");
      assertEquals(headers.first(HeaderName.CONNECTION), "close");

      // body
      Body body;
      body = http.body();

      assertEquals(ObjectoxHttpServer.readAllBytes(body), ByteArrays.empty());

      // response phase
      byte[] msg;
      msg = body01.getBytes(StandardCharsets.UTF_8);

      http.status(Status.OK);
      http.header(HeaderName.CONTENT_TYPE, "text/plain; charset=utf-8");
      http.header(HeaderName.CONTENT_LENGTH, msg.length);
      http.header(HeaderName.DATE, "Wed, 28 Jun 2023 12:08:43 GMT");
      http.send(msg);

      http.commit();

      assertEquals(socket.outputAsString(), resp01);

      assertEquals(http.keepAlive(), false);
    } catch (IOException e) {
      throw new AssertionError("Failed with IOException", e);
    }
  }

  @Test(description = """
  1. GET httpuest with implied keep-alive
  2. GET httpuest with explicit close
  """)
  public void testCase002() {
    TestableSocket socket;
    socket = TestableSocket.of("""
    GET /login HTTP/1.1\r
    Host: www.example.com\r
    \r
    """, """
    GET /login.css HTTP/1.1\r
    Host: www.example.com\r
    Connection: close\r
    \r
    """);

    String body01 = """
    <!doctype html>
    <html>
    <head>
    <title>Login page</title>
    <link rel="stylesheet" type="text/css" href="login.css" />
    </head>
    <body>
    <!-- the actual body -->
    </body>
    </html>
    """;

    String resp01 = """
    HTTP/1.1 200 OK\r
    Content-Type: text/html; charset=utf-8\r
    Content-Length: 171\r
    Date: Wed, 28 Jun 2023 12:08:43 GMT\r
    \r
    %s""".formatted(body01);

    String body02 = """
    * {
      box-sizing: border-box;
    }
    """;

    String resp02 = """
    HTTP/1.1 200 OK\r
    Content-Type: text/css; charset=utf-8\r
    Content-Length: 32\r
    Date: Wed, 28 Jun 2023 12:08:43 GMT\r
    \r
    %s""".formatted(body02);

    try (ServerLoop http = ServerLoop.create(socket)) {
      http.bufferSize(128, 128);
      http.noteSink(TestingNoteSink.INSTANCE);

      // request 01
      http.parse();

      assertEquals(http.badRequest(), false);

      // request line
      UriPath path;
      path = http.path();

      assertEquals(http.method(), Method.GET);
      assertEquals(path.is("/login"), true);

      UriQuery query;
      query = http.query();

      assertEquals(query.isEmpty(), true);
      assertEquals(query.value(), "");

      // headers
      ServerRequestHeaders headers;
      headers = http.headers();

      assertEquals(headers.size(), 1);
      assertEquals(headers.first(HeaderName.HOST), "www.example.com");

      // body
      Body body;
      body = http.body();

      assertEquals(ObjectoxHttpServer.readAllBytes(body), ByteArrays.empty());

      // response phase
      byte[] msg;
      msg = body01.getBytes(StandardCharsets.UTF_8);

      http.status(Status.OK);
      http.header(HeaderName.CONTENT_TYPE, "text/html; charset=utf-8");
      http.header(HeaderName.CONTENT_LENGTH, msg.length);
      http.header(HeaderName.DATE, "Wed, 28 Jun 2023 12:08:43 GMT");
      http.send(msg);

      http.commit();

      assertEquals(socket.outputAsString(), resp01);

      assertEquals(http.keepAlive(), true);

      // request 02
      http.parse();

      assertEquals(http.badRequest(), false);

      // request line
      path = http.path();

      assertEquals(http.method(), Method.GET);
      assertEquals(path.is("/login.css"), true);

      // headers
      headers = http.headers();

      assertEquals(headers.size(), 2);
      assertEquals(headers.first(HeaderName.HOST), "www.example.com");
      assertEquals(headers.first(HeaderName.CONNECTION), "close");

      // body
      body = http.body();

      assertEquals(ObjectoxHttpServer.readAllBytes(body), ByteArrays.empty());

      // response phase
      msg = body02.getBytes(StandardCharsets.UTF_8);

      http.status(Status.OK);
      http.header(HeaderName.CONTENT_TYPE, "text/css; charset=utf-8");
      http.header(HeaderName.CONTENT_LENGTH, msg.length);
      http.header(HeaderName.DATE, "Wed, 28 Jun 2023 12:08:43 GMT");
      http.send(msg);

      socket.outputReset();
      http.commit();

      assertEquals(socket.outputAsString(), resp02);

      assertEquals(http.keepAlive(), false);
    } catch (IOException e) {
      throw new AssertionError("Failed with IOException", e);
    }
  }

  @Test(description = """
  It should handle unkonwn httpuest headers
  """)
  public void testCase003() {
    TestableSocket socket;
    socket = TestableSocket.of("""
    GET / HTTP/1.1\r
    Host: www.example.com\r
    Connection: close\r
    Foo: bar\r
    \r
    """);

    String body01 = """
    Hello World!
    """;

    String resp01 = """
    HTTP/1.1 200 OK\r
    Content-Type: text/plain; charset=utf-8\r
    Content-Length: 13\r
    Date: Wed, 28 Jun 2023 12:08:43 GMT\r
    \r
    %s""".formatted(body01);

    try (ServerLoop http = ServerLoop.create(socket)) {
      http.bufferSize(128, 128);
      http.noteSink(TestingNoteSink.INSTANCE);

      // request phase
      http.parse();

      assertEquals(http.badRequest(), false);

      // headers
      ServerRequestHeaders headers;
      headers = http.headers();

      assertEquals(headers.size(), 3);
      assertEquals(headers.first(HeaderName.HOST), "www.example.com");
      assertEquals(headers.first(HeaderName.CONNECTION), "close");
      assertEquals(headers.first(HeaderName.create("Foo")), "bar");

      // response phase
      byte[] msg;
      msg = body01.getBytes(StandardCharsets.UTF_8);

      http.status(Status.OK);
      http.header(HeaderName.CONTENT_TYPE, "text/plain; charset=utf-8");
      http.header(HeaderName.CONTENT_LENGTH, msg.length);
      http.header(HeaderName.DATE, "Wed, 28 Jun 2023 12:08:43 GMT");
      http.send(msg);

      http.commit();

      assertEquals(socket.outputAsString(), resp01);

      assertEquals(http.keepAlive(), false);
    } catch (IOException e) {
      throw new AssertionError("Failed with IOException", e);
    }
  }

  @Test(description = """
  It should be possible to send regular files as a response
  """)
  public void testCase004() {
    TestableSocket socket;
    socket = TestableSocket.of("""
    GET /index.html HTTP/1.1\r
    Host: www.example.com\r
    Connection: close\r
    Foo: bar\r
    \r
    """);

    String body01 = """
    <html></html>
    """;

    String resp01 = """
    HTTP/1.1 200 OK\r
    Content-Type: text/html; charset=utf-8\r
    Content-Length: 14\r
    Date: Wed, 28 Jun 2023 12:08:43 GMT\r
    \r
    %s""".formatted(body01);

    Path dir;
    dir = null;

    try (ServerLoop http = ServerLoop.create(socket)) {
      http.bufferSize(128, 128);
      http.noteSink(TestingNoteSink.INSTANCE);

      // request phase
      http.parse();

      assertEquals(http.badRequest(), false);

      // request line
      UriPath path;
      path = http.path();

      assertEquals(http.method(), Method.GET);
      assertEquals(path.is("/index.html"), true);

      UriQuery query;
      query = http.query();

      assertEquals(query.isEmpty(), true);
      assertEquals(query.value(), "");

      // headers
      ServerRequestHeaders headers;
      headers = http.headers();

      assertEquals(headers.size(), 3);
      assertEquals(headers.first(HeaderName.HOST), "www.example.com");
      assertEquals(headers.first(HeaderName.CONNECTION), "close");
      assertEquals(headers.first(HeaderName.create("Foo")), "bar");

      // response phase
      dir = ObjectoxHttpServer.createTempDir();

      Path index;
      index = dir.resolve("index.html");

      Files.writeString(index, body01, StandardCharsets.UTF_8);

      http.status(Status.OK);
      http.header(HeaderName.CONTENT_TYPE, "text/html; charset=utf-8");
      http.header(HeaderName.CONTENT_LENGTH, Files.size(index));
      http.header(HeaderName.DATE, "Wed, 28 Jun 2023 12:08:43 GMT");
      http.send(index);

      http.commit();

      assertEquals(socket.outputAsString(), resp01);

      assertEquals(http.keepAlive(), false);
    } catch (IOException e) {
      throw new AssertionError("Failed with IOException", e);
    } finally {
      ObjectoxHttpServer.deleteRecursively(dir);
    }
  }

  @Test(description = """
  Support for the If-None-Match httpuest header and ETAG Response header
  """)
  public void testCase005() {
    TestableSocket socket;
    socket = TestableSocket.of("""
    GET /atom.xml HTTP/1.1\r
    Host: www.example.com\r
    If-None-Match: some%hash\r
    Connection: close\r
    \r
    """);

    String resp01 = """
    HTTP/1.1 304 NOT MODIFIED\r
    Date: Wed, 28 Jun 2023 12:08:43 GMT\r
    ETag: some%hash\r
    \r
    """;

    try (ServerLoop http = ServerLoop.create(socket)) {
      http.bufferSize(128, 128);
      http.noteSink(TestingNoteSink.INSTANCE);

      // request phase
      http.parse();

      assertEquals(http.badRequest(), false);

      // request line
      UriPath path;
      path = http.path();

      assertEquals(http.method(), Method.GET);
      assertEquals(path.is("/atom.xml"), true);

      UriQuery query;
      query = http.query();

      assertEquals(query.isEmpty(), true);
      assertEquals(query.value(), "");

      // headers
      ServerRequestHeaders headers;
      headers = http.headers();

      assertEquals(headers.size(), 3);
      assertEquals(headers.first(HeaderName.HOST), "www.example.com");
      assertEquals(headers.first(HeaderName.IF_NONE_MATCH), "some%hash");
      assertEquals(headers.first(HeaderName.CONNECTION), "close");

      // response phase

      http.status(Status.NOT_MODIFIED);
      http.header(HeaderName.DATE, "Wed, 28 Jun 2023 12:08:43 GMT");
      http.header(HeaderName.ETAG, "some%hash");
      http.send();

      http.commit();

      assertEquals(socket.outputAsString(), resp01);

      assertEquals(http.keepAlive(), false);
    } catch (IOException e) {
      throw new AssertionError("Failed with IOException", e);
    }
  }

  @Test(description = """
  Allow for NOT FOUND responses
  - it should set keep alive to false
  """)
  public void testCase006() {
    TestableSocket socket;
    socket = TestableSocket.of("""
    GET /xml-rpc.php HTTP/1.1\r
    Host: www.example.com\r
    \r
    """);

    String resp01 = """
    HTTP/1.1 404 NOT FOUND\r
    Connection: close\r
    Date: Wed, 28 Jun 2023 12:08:43 GMT\r
    \r
    """;

    try (ServerLoop http = ServerLoop.create(socket)) {
      http.bufferSize(128, 128);
      http.noteSink(TestingNoteSink.INSTANCE);

      // request phase
      http.parse();

      assertEquals(http.badRequest(), false);

      // response phase

      http.status(Status.NOT_FOUND);
      http.header(HeaderName.CONNECTION, "close");
      http.header(HeaderName.DATE, "Wed, 28 Jun 2023 12:08:43 GMT");
      http.send();

      http.commit();

      assertEquals(socket.outputAsString(), resp01);

      assertEquals(http.keepAlive(), false);
    } catch (IOException e) {
      throw new AssertionError("Failed with IOException", e);
    }
  }

  @Test(description = """
  Allow for query parameters
  - happy path
  """)
  public void testCase007() {
    TestableSocket socket;
    socket = TestableSocket.of("""
    GET /endpoint?foo=bar HTTP/1.1\r
    Host: www.example.com\r
    \r
    """);

    String resp01 = """
    HTTP/1.1 404 NOT FOUND\r
    Connection: close\r
    Date: Wed, 28 Jun 2023 12:08:43 GMT\r
    \r
    """;

    try (ServerLoop http = ServerLoop.create(socket)) {
      http.bufferSize(128, 128);
      http.noteSink(TestingNoteSink.INSTANCE);

      // request phase
      http.parse();

      assertEquals(http.badRequest(), false);

      UriQuery query;
      query = http.query();

      assertEquals(query.value(), "foo=bar");
      assertEquals(query.get("foo"), "bar");

      // response phase

      http.status(Status.NOT_FOUND);
      http.header(HeaderName.CONNECTION, "close");
      http.header(HeaderName.DATE, "Wed, 28 Jun 2023 12:08:43 GMT");
      http.send();

      http.commit();

      assertEquals(socket.outputAsString(), resp01);

      assertEquals(http.keepAlive(), false);
    } catch (IOException e) {
      throw new AssertionError("Failed with IOException", e);
    }
  }

  @Test(description = """
  Minimal POST request
  - happy path
  """)
  public void testCase008() {
    TestableSocket socket;
    socket = TestableSocket.of("""
    POST /login HTTP/1.1\r
    Host: www.example.com\r
    Content-Length: 24\r
    Content-Type: application/x-www-form-urlencoded\r
    \r
    email=user%40example.com""");

    String body01 = """
    Hello user@example.com. Please enter your password.
    """;

    String resp01 = """
    HTTP/1.1 303 SEE OTHER\r
    Location: /app\r
    Content-Type: text/plain; charset=utf-8\r
    Content-Length: 52\r
    Date: Wed, 28 Jun 2023 12:08:43 GMT\r
    \r
    %s""".formatted(body01);

    try (ServerLoop http = ServerLoop.create(socket)) {
      http.bufferSize(128, 256);
      http.noteSink(TestingNoteSink.INSTANCE);

      // request phase
      http.parse();

      assertEquals(http.badRequest(), false);

      Body requestBody;
      requestBody = http.body();

      assertEquals(asString(requestBody), "email=user%40example.com");

      // response phase

      http.status(Status.SEE_OTHER);
      http.header(HeaderName.LOCATION, "/app");
      http.header(HeaderName.CONTENT_TYPE, "text/plain; charset=utf-8");
      http.header(HeaderName.CONTENT_LENGTH, "52");
      http.header(HeaderName.DATE, "Wed, 28 Jun 2023 12:08:43 GMT");
      http.send(body01.getBytes(StandardCharsets.UTF_8));

      http.commit();

      assertEquals(socket.outputAsString(), resp01);

      assertEquals(http.keepAlive(), true);
    } catch (IOException e) {
      throw new AssertionError("Failed with IOException", e);
    }
  }

  private String asString(Body body) throws IOException {
    try (InputStream in = body.openStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream()) {
      in.transferTo(out);

      byte[] bytes;
      bytes = out.toByteArray();

      return new String(bytes, StandardCharsets.UTF_8);
    }
  }

}