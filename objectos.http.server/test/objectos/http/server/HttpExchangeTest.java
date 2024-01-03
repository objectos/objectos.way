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
import java.time.ZonedDateTime;
import objectos.http.Http;
import objectos.http.Method;
import objectos.http.server.HttpExchange.Processed;
import objectos.notes.NoOpNoteSink;
import objectos.notes.Note;
import objectos.notes.Note1;
import objectos.notes.NoteSink;
import objectos.util.array.ByteArrays;
import objectox.http.StandardHeaderName;
import objectox.http.server.Bytes;
import objectox.http.server.Http001;
import objectox.http.server.ObjectoxHttpServer;
import objectox.http.server.TestableSocket;
import objectox.http.server.TestingClock;
import objectox.http.server.TestingInput.RegularInput;
import org.testng.annotations.Test;

public class HttpExchangeTest {

  @Test(description = """
  1. GET request with explicity close
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
      assertEquals(headers.first(StandardHeaderName.HOST), "www.example.com");
      assertEquals(headers.first(StandardHeaderName.CONNECTION), "close");

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

      assertSame(result, resp);

      http.commit();

      assertEquals(socket.outputAsString(), resp01);

      assertEquals(http.keepAlive(), false);
    } catch (IOException e) {
      throw new AssertionError("Failed with IOException", e);
    }
  }

  @Test(description = """
  GET request with keep-alive. Second request with explicit close.
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
      assertEquals(headers.first(StandardHeaderName.HOST), "www.example.com");

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

      assertSame(result, resp);

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

      assertEquals(headers.size(), 1);
      assertEquals(headers.first(StandardHeaderName.HOST), "www.example.com");

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

      assertSame(result, resp);

      socket.outputReset();
      http.commit();

      assertEquals(socket.outputAsString(), resp02);

      assertEquals(http.keepAlive(), true);
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