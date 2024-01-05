/*
 * Copyright (C) 2023 Objectos Software LTDA.
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
package objectos.http.server;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZonedDateTime;
import objectos.http.HeaderName;
import objectos.http.Http;
import objectos.http.Method;
import objectos.http.server.HttpExchange.Processed;
import objectos.notes.NoOpNoteSink;
import objectos.notes.Note;
import objectos.notes.Note1;
import objectos.notes.NoteSink;
import objectos.util.array.ByteArrays;
import objectox.http.server.Bytes;
import objectox.http.server.Http001;
import objectox.http.server.ObjectoxHttpServer;
import objectox.http.server.ObjectoxServerResponseResult;
import objectox.http.server.TestableSocket;
import objectox.http.server.TestingClock;
import objectox.http.server.TestingInput.RegularInput;
import org.testng.annotations.Test;

public class HttpExchangeTest {

  @Test(description = """
  Minimum GET request with explicity close
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

    try (HttpExchange http = HttpExchange.create(socket)) {
      http.bufferSize(128);
      http.clock(TestingClock.FIXED);
      http.noteSink(TestingNoteSink.INSTANCE);

      // request phase
      http.parse();

      assertEquals(http.isBadRequest(), false);

      ServerRequest req;
      req = http.toRequest();

      // request line
      UriPath path;
      path = req.path();

      assertEquals(req.method(), Method.GET);
      assertEquals(path.is("/"), true);

      // headers
      ServerRequestHeaders headers;
      headers = req.headers();

      assertEquals(headers.size(), 2);
      assertEquals(headers.first(HeaderName.HOST), "www.example.com");
      assertEquals(headers.first(HeaderName.CONNECTION), "close");

      // body
      Body body;
      body = req.body();

      assertEquals(ObjectoxHttpServer.readAllBytes(body), ByteArrays.empty());

      // response phase
      byte[] msg;
      msg = body01.getBytes(StandardCharsets.UTF_8);

      ServerResponse resp;
      resp = req.toResponse();

      resp.ok();
      resp.contentType("text/plain; charset=utf-8");
      resp.contentLength(msg.length);
      resp.dateNow();

      ServerResponseResult result;
      result = resp.send(msg);

      assertSame(result, ObjectoxServerResponseResult.DEFAULT);

      http.commit();

      assertEquals(socket.outputAsString(), resp01);

      assertEquals(http.keepAlive(), false);
    } catch (IOException e) {
      throw new AssertionError("Failed with IOException", e);
    }
  }

  @Test(description = """
  1. GET request with implied keep-alive
  2. GET request with explicit close
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

    try (HttpExchange http = HttpExchange.create(socket)) {
      http.bufferSize(128);
      http.clock(TestingClock.FIXED);
      http.noteSink(TestingNoteSink.INSTANCE);

      // request 01
      http.parse();

      assertEquals(http.isBadRequest(), false);

      ServerRequest req;
      req = http.toRequest();

      // request line
      UriPath path;
      path = req.path();

      assertEquals(req.method(), Method.GET);
      assertEquals(path.is("/login"), true);

      // headers
      ServerRequestHeaders headers;
      headers = req.headers();

      assertEquals(headers.size(), 1);
      assertEquals(headers.first(HeaderName.HOST), "www.example.com");

      // body
      Body body;
      body = req.body();

      assertEquals(ObjectoxHttpServer.readAllBytes(body), ByteArrays.empty());

      // response phase
      byte[] msg;
      msg = body01.getBytes(StandardCharsets.UTF_8);

      ServerResponse resp;
      resp = req.toResponse();

      resp.ok();
      resp.contentType("text/html; charset=utf-8");
      resp.contentLength(msg.length);
      resp.dateNow();

      ServerResponseResult result;
      result = resp.send(msg);

      assertSame(result, ObjectoxServerResponseResult.DEFAULT);

      http.commit();

      assertEquals(socket.outputAsString(), resp01);

      assertEquals(http.keepAlive(), true);

      // request 02
      http.parse();

      assertEquals(http.isBadRequest(), false);

      req = http.toRequest();

      // request line
      path = req.path();

      assertEquals(req.method(), Method.GET);
      assertEquals(path.is("/login.css"), true);

      // headers
      headers = req.headers();

      assertEquals(headers.size(), 2);
      assertEquals(headers.first(HeaderName.HOST), "www.example.com");
      assertEquals(headers.first(HeaderName.CONNECTION), "close");

      // body
      body = req.body();

      assertEquals(ObjectoxHttpServer.readAllBytes(body), ByteArrays.empty());

      // response phase
      msg = body02.getBytes(StandardCharsets.UTF_8);

      resp = req.toResponse();

      resp.ok();
      resp.contentType("text/css; charset=utf-8");
      resp.contentLength(msg.length);
      resp.dateNow();

      result = resp.send(msg);

      assertSame(result, ObjectoxServerResponseResult.DEFAULT);

      socket.outputReset();
      http.commit();

      assertEquals(socket.outputAsString(), resp02);

      assertEquals(http.keepAlive(), false);
    } catch (IOException e) {
      throw new AssertionError("Failed with IOException", e);
    }
  }

  @Test(description = """
  It should handle unkonwn request headers
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

    try (HttpExchange http = HttpExchange.create(socket)) {
      http.bufferSize(128);
      http.clock(TestingClock.FIXED);
      http.noteSink(TestingNoteSink.INSTANCE);

      // request phase
      http.parse();

      assertEquals(http.isBadRequest(), false);

      ServerRequest req;
      req = http.toRequest();

      // headers
      ServerRequestHeaders headers;
      headers = req.headers();

      assertEquals(headers.size(), 3);
      assertEquals(headers.first(HeaderName.HOST), "www.example.com");
      assertEquals(headers.first(HeaderName.CONNECTION), "close");
      assertEquals(headers.first(HeaderName.create("Foo")), "bar");

      // response phase
      byte[] msg;
      msg = body01.getBytes(StandardCharsets.UTF_8);

      ServerResponse resp;
      resp = req.toResponse();

      resp.ok();
      resp.contentType("text/plain; charset=utf-8");
      resp.contentLength(msg.length);
      resp.dateNow();

      ServerResponseResult result;
      result = resp.send(msg);

      assertSame(result, ObjectoxServerResponseResult.DEFAULT);

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

    try (HttpExchange http = HttpExchange.create(socket)) {
      http.bufferSize(128);
      http.clock(TestingClock.FIXED);
      http.noteSink(TestingNoteSink.INSTANCE);

      // request phase
      http.parse();

      assertEquals(http.isBadRequest(), false);

      ServerRequest req;
      req = http.toRequest();

      // request line
      UriPath path;
      path = req.path();

      assertEquals(req.method(), Method.GET);
      assertEquals(path.is("/index.html"), true);

      // headers
      ServerRequestHeaders headers;
      headers = req.headers();

      assertEquals(headers.size(), 3);
      assertEquals(headers.first(HeaderName.HOST), "www.example.com");
      assertEquals(headers.first(HeaderName.CONNECTION), "close");
      assertEquals(headers.first(HeaderName.create("Foo")), "bar");

      // response phase
      dir = ObjectoxHttpServer.createTempDir();

      Path index;
      index = dir.resolve("index.html");

      Files.writeString(index, body01, StandardCharsets.UTF_8);

      ServerResponse resp;
      resp = req.toResponse();

      resp.ok();
      resp.contentType("text/html; charset=utf-8");
      resp.contentLength(Files.size(index));
      resp.dateNow();

      ServerResponseResult result;
      result = resp.send(index);

      assertSame(result, ObjectoxServerResponseResult.DEFAULT);

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
  Support for the If-None-Match request header and ETAG Response header
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

    try (HttpExchange http = HttpExchange.create(socket)) {
      http.bufferSize(128);
      http.clock(TestingClock.FIXED);
      http.noteSink(TestingNoteSink.INSTANCE);

      // request phase
      http.parse();

      assertEquals(http.isBadRequest(), false);

      ServerRequest req;
      req = http.toRequest();

      // request line
      UriPath path;
      path = req.path();

      assertEquals(req.method(), Method.GET);
      assertEquals(path.is("/atom.xml"), true);

      // headers
      ServerRequestHeaders headers;
      headers = req.headers();

      assertEquals(headers.size(), 3);
      assertEquals(headers.first(HeaderName.HOST), "www.example.com");
      assertEquals(headers.first(HeaderName.IF_NONE_MATCH), "some%hash");
      assertEquals(headers.first(HeaderName.CONNECTION), "close");

      // response phase
      ServerResponse resp;
      resp = req.toResponse();

      resp.notModified();
      resp.dateNow();
      resp.header(HeaderName.ETAG, "some%hash");

      ServerResponseResult result;
      result = resp.send();

      assertSame(result, ObjectoxServerResponseResult.DEFAULT);

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

    try (HttpExchange http = HttpExchange.create(socket)) {
      http.bufferSize(128);
      http.clock(TestingClock.FIXED);
      http.noteSink(TestingNoteSink.INSTANCE);

      // request phase
      http.parse();

      assertEquals(http.isBadRequest(), false);

      ServerRequest req;
      req = http.toRequest();

      // response phase
      ServerResponse resp;
      resp = req.toResponse();

      resp.notFound();
      resp.header(HeaderName.CONNECTION, "close");
      resp.dateNow();

      ServerResponseResult result;
      result = resp.send();

      assertSame(result, ObjectoxServerResponseResult.CLOSE);

      http.commit();

      assertEquals(socket.outputAsString(), resp01);

      assertEquals(http.keepAlive(), true);
      assertEquals(result.closeConnection(), true);
    } catch (IOException e) {
      throw new AssertionError("Failed with IOException", e);
    }
  }

  @Test
  public void http001() throws IOException {
    RegularInput input;
    input = Http001.INPUT;

    TestableSocket socket;
    socket = TestableSocket.of(input.request());

    var noteSink = new NoOpNoteSink() {
      Processed processed;

      @Override
      public boolean isEnabled(Note note) { return true; }

      @Override
      public <T1> void send(Note1<T1> note, T1 v1) {
        if (note == HttpExchange.PROCESSED) {
          processed = (Processed) v1;
        }
      }
    };

    HttpExchange exchange;
    exchange = HttpExchange.of(socket, HttpExchange.Option.noteSink(noteSink));

    try (exchange) {
      assertTrue(exchange.active());

      assertEquals(exchange.method(), Http.Method.GET);

      assertEquals(exchange.path(), "/");

      assertFalse(exchange.hasResponse());

      byte[] bytes;
      bytes = Bytes.utf8("Hello World!\n");

      exchange.status(Http.Status.OK_200);

      exchange.header(Http.Header.CONTENT_TYPE, "text/plain; charset=utf-8");

      exchange.header(Http.Header.CONTENT_LENGTH, Long.toString(bytes.length));

      ZonedDateTime date;
      date = Http001.DATE;

      exchange.header(Http.Header.DATE, Http.formatDate(date));

      exchange.body(bytes);

      assertTrue(exchange.hasResponse());

      assertFalse(exchange.active());

      Processed processed;
      processed = noteSink.processed;

      assertNotNull(processed);
      assertEquals(processed.method(), Http.Method.GET);
      assertEquals(processed.status(), Http.Status.OK_200);
    }

    assertEquals(socket.outputAsString(), Http001.OUTPUT);
  }

  @Test
  public void unknownRequestHeaders() throws IOException {
    TestableSocket socket;
    socket = TestableSocket.of("""
		GET / HTTP/1.1
		Host: www.example.com
		Connection: close
		Foo: bar

		""".replace("\n", "\r\n"));

    NoteSink noteSink;
    noteSink = TestingNoteSink.INSTANCE;

    HttpExchange exchange;
    exchange = HttpExchange.of(socket, HttpExchange.Option.noteSink(noteSink));

    try (exchange) {
      assertTrue(exchange.active());

      assertEquals(exchange.header(Http.Header.HOST).toString(), "www.example.com");
      assertEquals(exchange.header(Http.Header.CONNECTION).toString(), "close");
      assertEquals(exchange.method(), Http.Method.GET);
      assertEquals(exchange.path(), "/");
    }
  }

}