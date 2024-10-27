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
package objectos.way;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import objectos.way.HttpExchangeLoop.ParseStatus;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HttpExchangeLoopTest {

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

    try (HttpExchangeLoop http = new HttpExchangeLoop(socket)) {
      http.bufferSize(128, 128);
      http.noteSink(TestingNoteSink.INSTANCE);

      // request phase
      ParseStatus parse;
      parse = http.parse();

      assertEquals(parse.isError(), false);

      // request line
      assertEquals(http.method(), Http.GET);
      assertEquals(http.path(), "/");
      assertEquals(http.rawQuery(), null);
      assertEquals(http.queryParam("x"), null);
      assertEquals(http.queryParamNames(), Set.of());

      // headers
      Http.Request.Headers headers;
      headers = http.headers();

      assertEquals(headers.size(), 2);
      assertEquals(headers.first(Http.HeaderName.HOST), "www.example.com");
      assertEquals(headers.first(Http.HeaderName.CONNECTION), "close");

      // body
      Http.Request.Body body;
      body = http.body();

      assertEquals(ObjectosHttp.readAllBytes(body), Util.EMPTY_BYTE_ARRAY);

      // response phase
      byte[] msg;
      msg = body01.getBytes(StandardCharsets.UTF_8);

      http.status(Http.Status.OK);
      http.header(Http.HeaderName.CONTENT_TYPE, "text/plain; charset=utf-8");
      http.header(Http.HeaderName.CONTENT_LENGTH, msg.length);
      http.header(Http.HeaderName.DATE, "Wed, 28 Jun 2023 12:08:43 GMT");
      http.send(msg);

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

    try (HttpExchangeLoop http = new HttpExchangeLoop(socket)) {
      http.bufferSize(128, 128);
      http.noteSink(TestingNoteSink.INSTANCE);

      // request 01
      ParseStatus parse;
      parse = http.parse();

      assertEquals(parse.isError(), false);

      // request line
      assertEquals(http.method(), Http.GET);
      assertEquals(http.path(), "/login");
      assertEquals(http.rawQuery(), null);
      assertEquals(http.queryParamNames(), Set.of());

      // headers
      Http.Request.Headers headers;
      headers = http.headers();

      assertEquals(headers.size(), 1);
      assertEquals(headers.first(Http.HeaderName.HOST), "www.example.com");

      // body
      Http.Request.Body body;
      body = http.body();

      assertEquals(ObjectosHttp.readAllBytes(body), Util.EMPTY_BYTE_ARRAY);

      // response phase
      byte[] msg;
      msg = body01.getBytes(StandardCharsets.UTF_8);

      http.status(Http.Status.OK);
      http.header(Http.HeaderName.CONTENT_TYPE, "text/html; charset=utf-8");
      http.header(Http.HeaderName.CONTENT_LENGTH, msg.length);
      http.header(Http.HeaderName.DATE, "Wed, 28 Jun 2023 12:08:43 GMT");
      http.send(msg);

      http.commit();

      assertEquals(socket.outputAsString(), resp01);

      assertEquals(http.keepAlive(), true);

      // request 02
      parse = http.parse();

      assertEquals(parse.isError(), false);

      // request line
      assertEquals(http.method(), Http.GET);
      assertEquals(http.path(), "/login.css");

      // headers
      headers = http.headers();

      assertEquals(headers.size(), 2);
      assertEquals(headers.first(Http.HeaderName.HOST), "www.example.com");
      assertEquals(headers.first(Http.HeaderName.CONNECTION), "close");

      // body
      body = http.body();

      assertEquals(ObjectosHttp.readAllBytes(body), Util.EMPTY_BYTE_ARRAY);

      // response phase
      msg = body02.getBytes(StandardCharsets.UTF_8);

      http.status(Http.Status.OK);
      http.header(Http.HeaderName.CONTENT_TYPE, "text/css; charset=utf-8");
      http.header(Http.HeaderName.CONTENT_LENGTH, msg.length);
      http.header(Http.HeaderName.DATE, "Wed, 28 Jun 2023 12:08:43 GMT");
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

    try (HttpExchangeLoop http = new HttpExchangeLoop(socket)) {
      http.bufferSize(128, 128);
      http.noteSink(TestingNoteSink.INSTANCE);

      // request phase
      ParseStatus parse;
      parse = http.parse();

      assertEquals(parse.isError(), false);

      // headers
      Http.Request.Headers headers;
      headers = http.headers();

      assertEquals(headers.size(), 3);
      assertEquals(headers.first(Http.HeaderName.HOST), "www.example.com");
      assertEquals(headers.first(Http.HeaderName.CONNECTION), "close");
      assertEquals(headers.first(Http.createHeaderName("Foo")), "bar");

      // response phase
      byte[] msg;
      msg = body01.getBytes(StandardCharsets.UTF_8);

      http.status(Http.Status.OK);
      http.header(Http.HeaderName.CONTENT_TYPE, "text/plain; charset=utf-8");
      http.header(Http.HeaderName.CONTENT_LENGTH, msg.length);
      http.header(Http.HeaderName.DATE, "Wed, 28 Jun 2023 12:08:43 GMT");
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

    try (HttpExchangeLoop http = new HttpExchangeLoop(socket)) {
      http.bufferSize(128, 128);
      http.noteSink(TestingNoteSink.INSTANCE);

      // request phase
      ParseStatus parse;
      parse = http.parse();

      assertEquals(parse.isError(), false);

      // request line
      assertEquals(http.method(), Http.GET);
      assertEquals(http.path(), "/index.html");
      assertEquals(http.rawQuery(), null);
      assertEquals(http.queryParamNames(), Set.of());

      // headers
      Http.Request.Headers headers;
      headers = http.headers();

      assertEquals(headers.size(), 3);
      assertEquals(headers.first(Http.HeaderName.HOST), "www.example.com");
      assertEquals(headers.first(Http.HeaderName.CONNECTION), "close");
      assertEquals(headers.first(Http.createHeaderName("Foo")), "bar");

      // response phase
      dir = ObjectosHttp.createTempDir();

      Path index;
      index = dir.resolve("index.html");

      Files.writeString(index, body01, StandardCharsets.UTF_8);

      http.status(Http.Status.OK);
      http.header(Http.HeaderName.CONTENT_TYPE, "text/html; charset=utf-8");
      http.header(Http.HeaderName.CONTENT_LENGTH, Files.size(index));
      http.header(Http.HeaderName.DATE, "Wed, 28 Jun 2023 12:08:43 GMT");
      http.send(index);

      http.commit();

      assertEquals(socket.outputAsString(), resp01);

      assertEquals(http.keepAlive(), false);
    } catch (IOException e) {
      throw new AssertionError("Failed with IOException", e);
    } finally {
      ObjectosHttp.deleteRecursively(dir);
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

    try (HttpExchangeLoop http = new HttpExchangeLoop(socket)) {
      http.bufferSize(128, 128);
      http.noteSink(TestingNoteSink.INSTANCE);

      // request phase
      ParseStatus parse;
      parse = http.parse();

      assertEquals(parse.isError(), false);

      // request line
      assertEquals(http.method(), Http.GET);
      assertEquals(http.path(), "/atom.xml");
      assertEquals(http.rawQuery(), null);
      assertEquals(http.queryParamNames(), Set.of());

      // headers
      Http.Request.Headers headers;
      headers = http.headers();

      assertEquals(headers.size(), 3);
      assertEquals(headers.first(Http.HeaderName.HOST), "www.example.com");
      assertEquals(headers.first(Http.HeaderName.IF_NONE_MATCH), "some%hash");
      assertEquals(headers.first(Http.HeaderName.CONNECTION), "close");

      // response phase

      http.status(Http.Status.NOT_MODIFIED);
      http.header(Http.HeaderName.DATE, "Wed, 28 Jun 2023 12:08:43 GMT");
      http.header(Http.HeaderName.ETAG, "some%hash");
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

    try (HttpExchangeLoop http = new HttpExchangeLoop(socket)) {
      http.bufferSize(128, 128);
      http.noteSink(TestingNoteSink.INSTANCE);

      // request phase
      ParseStatus parse;
      parse = http.parse();

      assertEquals(parse.isError(), false);

      // response phase

      http.status(Http.Status.NOT_FOUND);
      http.header(Http.HeaderName.CONNECTION, "close");
      http.header(Http.HeaderName.DATE, "Wed, 28 Jun 2023 12:08:43 GMT");
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

    try (HttpExchangeLoop http = new HttpExchangeLoop(socket)) {
      http.bufferSize(128, 128);
      http.noteSink(TestingNoteSink.INSTANCE);

      // request phase
      ParseStatus parse;
      parse = http.parse();

      assertEquals(parse.isError(), false);

      assertEquals(http.rawQuery(), "foo=bar");
      assertEquals(http.queryParam("foo"), "bar");
      assertEquals(http.queryParamNames(), Set.of("foo"));

      // response phase

      http.status(Http.Status.NOT_FOUND);
      http.header(Http.HeaderName.CONNECTION, "close");
      http.header(Http.HeaderName.DATE, "Wed, 28 Jun 2023 12:08:43 GMT");
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

    try (HttpExchangeLoop http = new HttpExchangeLoop(socket)) {
      http.bufferSize(128, 256);
      http.noteSink(TestingNoteSink.INSTANCE);

      // request phase
      ParseStatus parse;
      parse = http.parse();

      assertEquals(parse.isError(), false);

      Http.Request.Body requestBody;
      requestBody = http.body();

      assertEquals(ObjectosHttp.readString(requestBody), "email=user%40example.com");

      // response phase

      http.status(Http.Status.SEE_OTHER);
      http.header(Http.HeaderName.LOCATION, "/app");
      http.header(Http.HeaderName.CONTENT_TYPE, "text/plain; charset=utf-8");
      http.header(Http.HeaderName.CONTENT_LENGTH, "52");
      http.header(Http.HeaderName.DATE, "Wed, 28 Jun 2023 12:08:43 GMT");
      http.send(body01.getBytes(StandardCharsets.UTF_8));

      http.commit();

      assertEquals(socket.outputAsString(), resp01);

      assertEquals(http.keepAlive(), true);
    } catch (IOException e) {
      throw new AssertionError("Failed with IOException", e);
    }
  }

  @Test(description = """
  It should resize buffer if necessary when writing out the response headers
  """)
  public void testCase009() {
    TestableSocket socket;
    socket = TestableSocket.of("""
    GET / HTTP/1.1\r
    Host: www.example.com\r
    Connection: close\r
    \r
    """);

    String etag;
    etag = "AVeryLongEtagValueToTriggerTheBufferResizeWhenWritingOutTheHeaders";

    String resp01 = """
    HTTP/1.1 200 OK\r
    Content-Type: text/plain; charset=utf-8\r
    Content-Length: 5\r
    Date: Wed, 28 Jun 2023 12:08:43 GMT\r
    ETag: %s\r
    \r
    AAAA
    """.formatted(etag);

    try (HttpExchangeLoop http = new HttpExchangeLoop(socket)) {
      http.bufferSize(128, 256);
      http.clock(TestingClock.FIXED);
      http.noteSink(TestingNoteSink.INSTANCE);

      ParseStatus parse;
      parse = http.parse();

      assertEquals(parse.isError(), false);

      http.status(Http.Status.OK);
      http.header(Http.HeaderName.CONTENT_TYPE, "text/plain; charset=utf-8");
      http.header(Http.HeaderName.CONTENT_LENGTH, 5);
      http.header(Http.HeaderName.DATE, "Wed, 28 Jun 2023 12:08:43 GMT");
      http.header(Http.HeaderName.ETAG, etag);
      http.send("AAAA\n".getBytes(StandardCharsets.UTF_8));

      http.commit();

      assertEquals(socket.outputAsString(), resp01);

      assertEquals(http.keepAlive(), false);
    } catch (IOException e) {
      throw new AssertionError("Failed with IOException", e);
    }
  }

  @Test(description = """
  The unsupported media type pre-made response
  """)
  public void testCase010() {
    TestableSocket socket;
    socket = TestableSocket.of("""
    POST /login HTTP/1.1\r
    Host: www.example.com\r
    Content-Length: 24\r
    Content-Type: multipart/form-data\r
    \r
    email=user%40example.com""");

    String resp01 = """
    HTTP/1.1 415 UNSUPPORTED MEDIA TYPE\r
    Date: Wed, 28 Jun 2023 12:08:43 GMT\r
    Connection: close\r
    \r
    """;

    try (HttpExchangeLoop http = new HttpExchangeLoop(socket)) {
      http.bufferSize(128, 256);
      http.clock(TestingClock.FIXED);
      http.noteSink(TestingNoteSink.INSTANCE);

      ParseStatus parse;
      parse = http.parse();

      assertEquals(parse.isError(), false);

      http.unsupportedMediaType();

      http.commit();

      assertEquals(socket.outputAsString(), resp01);

      assertEquals(http.keepAlive(), false);
    } catch (IOException e) {
      throw new AssertionError("Failed with IOException", e);
    }
  }

  @Test(description = """
  Fail if:
  - content-length is set
  - body instanceof CharWritable
  """)
  public void testCase013() {
    TestableSocket socket;
    socket = TestableSocket.of("""
    GET /login HTTP/1.1\r
    Host: www.example.com\r
    \r
    """);

    TestingCharWritable writable;
    writable = TestingCharWritable.ofLength(64);

    try (HttpExchangeLoop http = new HttpExchangeLoop(socket)) {
      http.bufferSize(128, 256);
      http.clock(TestingClock.FIXED);
      http.noteSink(TestingNoteSink.INSTANCE);

      ParseStatus parse;
      parse = http.parse();

      assertEquals(parse.isError(), false);

      http.dateNow();
      http.header(Http.HeaderName.CONTENT_TYPE, "text/plain");
      http.header(Http.HeaderName.CONTENT_LENGTH, 64);
      http.send(writable, StandardCharsets.UTF_8);

      http.commit();

      Assert.fail("http.commit should have thrown");
    } catch (IllegalStateException expected) {
      String message;
      message = expected.getMessage();

      assertTrue(message.contains("Content-Length"));
      assertTrue(message.contains("CharWritable"));
    } catch (IOException e) {
      throw new AssertionError("Failed with IOException", e);
    }
  }

  @Test(description = """
  Fail if:
  - Transfer-Encoding: chunked is not set
  - body instanceof CharWritable
  """)
  public void testCase014() {
    TestableSocket socket;
    socket = TestableSocket.of("""
    GET /login HTTP/1.1\r
    Host: www.example.com\r
    \r
    """);

    TestingCharWritable writable;
    writable = TestingCharWritable.ofLength(64);

    try (HttpExchangeLoop http = new HttpExchangeLoop(socket)) {
      http.bufferSize(128, 256);
      http.clock(TestingClock.FIXED);
      http.noteSink(TestingNoteSink.INSTANCE);

      ParseStatus parse;
      parse = http.parse();

      assertEquals(parse.isError(), false);

      http.dateNow();
      http.header(Http.HeaderName.CONTENT_TYPE, "text/plain");
      http.send(writable, StandardCharsets.UTF_8);

      http.commit();

      Assert.fail("http.commit should have thrown");
    } catch (IllegalStateException expected) {
      String message;
      message = expected.getMessage();

      assertTrue(message.contains("chunked"));
      assertTrue(message.contains("CharWritable"));
    } catch (IOException e) {
      throw new AssertionError("Failed with IOException", e);
    }
  }

  @Test(description = """
  CharWritable: single chunk
  """)
  public void testCase015() {
    TestableSocket socket;
    socket = TestableSocket.of("""
    GET / HTTP/1.1\r
    Host: www.example.com\r
    \r
    """);

    // 80\r\n
    // [contents]\r\n
    // 0\r\n
    // \r\n

    TestingCharWritable writable;
    writable = TestingCharWritable.ofLength(128);

    String resp01 = """
    Date: Wed, 28 Jun 2023 12:08:43 GMT\r
    Content-Type: text/plain\r
    Transfer-Encoding: chunked\r
    \r
    80\r
    .................................................
    .................................................
    1234567890123456789012345678\r
    0\r
    \r
    """;

    try (HttpExchangeLoop http = new HttpExchangeLoop(socket)) {
      http.bufferSize(128, 128);
      http.clock(TestingClock.FIXED);
      http.noteSink(TestingNoteSink.INSTANCE);

      ParseStatus parse;
      parse = http.parse();

      assertEquals(parse.isError(), false);

      http.dateNow();
      http.header(Http.HeaderName.CONTENT_TYPE, "text/plain");
      http.header(Http.HeaderName.TRANSFER_ENCODING, "chunked");
      http.send(writable, StandardCharsets.UTF_8);

      http.commit();

      assertEquals(socket.outputAsString(), resp01);

      assertEquals(http.keepAlive(), true);
    } catch (IOException e) {
      throw new AssertionError("Failed with IOException", e);
    }
  }

  @Test(description = """
  CharWritable: multiple chunks
  """)
  public void testCase016() {
    TestableSocket socket;
    socket = TestableSocket.of("""
    GET / HTTP/1.1\r
    Host: www.example.com\r
    \r
    """);

    TestingCharWritable writable;
    writable = TestingCharWritable.ofLength(256);

    String resp01 = """
    Date: Wed, 28 Jun 2023 12:08:43 GMT\r
    Content-Type: text/plain\r
    Transfer-Encoding: chunked\r
    \r
    80\r
    .................................................
    .................................................
    ............................\r
    80\r
    .....................
    .................................................
    .................................................
    123456\r
    0\r
    \r
    """;

    try (HttpExchangeLoop http = new HttpExchangeLoop(socket)) {
      http.bufferSize(128, 128);
      http.clock(TestingClock.FIXED);
      http.noteSink(TestingNoteSink.INSTANCE);

      ParseStatus parse;
      parse = http.parse();

      assertEquals(parse.isError(), false);

      http.dateNow();
      http.header(Http.HeaderName.CONTENT_TYPE, "text/plain");
      http.header(Http.HeaderName.TRANSFER_ENCODING, "chunked");
      http.send(writable, StandardCharsets.UTF_8);

      http.commit();

      assertEquals(socket.outputAsString(), resp01);

      assertEquals(http.keepAlive(), true);
    } catch (IOException e) {
      throw new AssertionError("Failed with IOException", e);
    }
  }

  @Test(description = """
  CharWritable: single chunk with buffer resize
  """)
  public void testCase017() {
    TestableSocket socket;
    socket = TestableSocket.of("""
    GET / HTTP/1.1\r
    Host: www.example.com\r
    \r
    """);

    TestingCharWritable writable;
    writable = TestingCharWritable.ofLength(256);

    String resp01 = """
    Date: Wed, 28 Jun 2023 12:08:43 GMT\r
    Content-Type: text/plain\r
    Transfer-Encoding: chunked\r
    \r
    100\r
    .................................................
    .................................................
    .................................................
    .................................................
    .................................................
    123456\r
    0\r
    \r
    """;

    try (HttpExchangeLoop http = new HttpExchangeLoop(socket)) {
      http.bufferSize(128, 256);
      http.clock(TestingClock.FIXED);
      http.noteSink(TestingNoteSink.INSTANCE);

      ParseStatus parse;
      parse = http.parse();

      assertEquals(parse.isError(), false);

      http.dateNow();
      http.header(Http.HeaderName.CONTENT_TYPE, "text/plain");
      http.header(Http.HeaderName.TRANSFER_ENCODING, "chunked");
      http.send(writable, StandardCharsets.UTF_8);

      http.commit();

      assertEquals(socket.outputAsString(), resp01);

      assertEquals(http.keepAlive(), true);
    } catch (IOException e) {
      throw new AssertionError("Failed with IOException", e);
    }
  }

  @Test(description = """
  CharWritable: chunk is larger than buffer
  """)
  public void testCase018() {
    TestableSocket socket;
    socket = TestableSocket.of("""
    GET / HTTP/1.1\r
    Host: www.example.com\r
    \r
    """);

    String chunk256 = """
    .................................................
    .................................................
    .................................................
    .................................................
    .................................................
    123456""";

    Lang.CharWritable writable;
    writable = out -> out.append(chunk256);

    String resp01 = """
    Date: Wed, 28 Jun 2023 12:08:43 GMT\r
    Content-Type: text/plain\r
    Transfer-Encoding: chunked\r
    \r
    80\r
    .................................................
    .................................................
    ............................\r
    80\r
    .....................
    .................................................
    .................................................
    123456\r
    0\r
    \r
    """;

    try (HttpExchangeLoop http = new HttpExchangeLoop(socket)) {
      http.bufferSize(128, 128);
      http.clock(TestingClock.FIXED);
      http.noteSink(TestingNoteSink.INSTANCE);

      ParseStatus parse;
      parse = http.parse();

      assertEquals(parse.isError(), false);

      http.dateNow();
      http.header(Http.HeaderName.CONTENT_TYPE, "text/plain");
      http.header(Http.HeaderName.TRANSFER_ENCODING, "chunked");
      http.send(writable, StandardCharsets.UTF_8);

      http.commit();

      assertEquals(socket.outputAsString(), resp01);

      assertEquals(http.keepAlive(), true);
    } catch (IOException e) {
      throw new AssertionError("Failed with IOException", e);
    }
  }

  @Test(description = """
  Request body is larger than buffer
  """)
  public void testCase019() {
    String chunk256 = """
    .................................................
    .................................................
    .................................................
    .................................................
    .................................................
    123456""";

    TestableSocket socket;
    socket = TestableSocket.of("""
    POST /upload HTTP/1.1\r
    Host: www.example.com\r
    Content-Length: 256\r
    Content-Type: text/plain\r
    \r
    %s""".formatted(chunk256));

    try (HttpExchangeLoop http = new HttpExchangeLoop(socket)) {
      http.bufferSize(128, 128);
      http.clock(TestingClock.FIXED);
      http.noteSink(TestingNoteSink.INSTANCE);

      ParseStatus parse;
      parse = http.parse();

      assertEquals(parse.isError(), false);

      Http.Request.Body requestBody;
      requestBody = http.body();

      assertEquals(ObjectosHttp.readString(requestBody), chunk256);
    } catch (IOException e) {
      throw new AssertionError("Failed with IOException", e);
    }
  }

  @Test(description = """
  Properly handle empty From: header
  """)
  public void testCase020() {
    TestableSocket socket;
    socket = TestableSocket.of("""
    GET / HTTP/1.0\r
    Host: www.example.com\r
    From: \r
    Accept-Encoding: gzip, deflate, br\r
    \r
    """);

    try (HttpExchangeLoop http = new HttpExchangeLoop(socket)) {
      http.clock(TestingClock.FIXED);
      http.noteSink(TestingNoteSink.INSTANCE);

      ParseStatus parse;
      parse = http.parse();

      assertEquals(parse.isError(), false);

      // request line
      assertEquals(http.method(), Http.GET);
      assertEquals(http.path(), "/");
      assertEquals(http.rawQuery(), null);
      assertEquals(http.queryParam(""), null);
      assertEquals(http.queryParamNames(), Set.of());

      // headers
      Http.Request.Headers headers;
      headers = http.headers();

      assertEquals(headers.size(), 3);
      assertEquals(headers.first(Http.HeaderName.HOST), "www.example.com");
      assertEquals(headers.first(Http.HeaderName.FROM), "");
      assertEquals(headers.first(Http.HeaderName.ACCEPT_ENCODING), "gzip, deflate, br");

      // body
      Http.Request.Body body;
      body = http.body();

      assertEquals(ObjectosHttp.readAllBytes(body), Util.EMPTY_BYTE_ARRAY);
    } catch (IOException e) {
      throw new AssertionError("Failed with IOException", e);
    }
  }

  @Test(description = """
  request-target path decoding
  """)
  public void testCase021() {
    TestableSocket socket;
    socket = TestableSocket.of("""
      GET /wiki/%E6%9D%B1%E4%BA%AC HTTP/1.0\r
      Host: www.example.com\r
      \r
      """);

    try (HttpExchangeLoop http = new HttpExchangeLoop(socket)) {
      http.clock(TestingClock.FIXED);
      http.noteSink(TestingNoteSink.INSTANCE);

      ParseStatus parse;
      parse = http.parse();

      assertEquals(parse.isError(), false);

      // request line
      assertEquals(http.method(), Http.GET);
      assertEquals(http.path(), "/wiki/東京");
      assertEquals(http.rawQuery(), null);
      assertEquals(http.queryParam("null"), null);
      assertEquals(http.queryParamNames(), Set.of());

      // headers
      Http.Request.Headers headers;
      headers = http.headers();

      assertEquals(headers.size(), 1);
      assertEquals(headers.first(Http.HeaderName.HOST), "www.example.com");

      // body
      Http.Request.Body body;
      body = http.body();

      assertEquals(ObjectosHttp.readAllBytes(body), Util.EMPTY_BYTE_ARRAY);
    } catch (IOException e) {
      throw new AssertionError("Failed with IOException", e);
    }
  }

  @Test(description = """
  Empty query string
  """)
  public void testCase022() {
    TestableSocket socket;
    socket = TestableSocket.of("""
      GET /empty? HTTP/1.1\r
      Host: www.example.com\r
      \r
      """);

    try (HttpExchangeLoop http = new HttpExchangeLoop(socket)) {
      http.clock(TestingClock.FIXED);
      http.noteSink(TestingNoteSink.INSTANCE);

      ParseStatus parse;
      parse = http.parse();

      assertEquals(parse.isError(), false);

      // request line
      assertEquals(http.method(), Http.GET);
      assertEquals(http.path(), "/empty");
      assertEquals(http.rawQuery(), "");
      assertEquals(http.queryParam("null"), null);
      assertEquals(http.queryParamNames(), Set.of());

      // headers
      Http.Request.Headers headers;
      headers = http.headers();

      assertEquals(headers.size(), 1);
      assertEquals(headers.first(Http.HeaderName.HOST), "www.example.com");

      // body
      Http.Request.Body body;
      body = http.body();

      assertEquals(ObjectosHttp.readAllBytes(body), Util.EMPTY_BYTE_ARRAY);
    } catch (IOException e) {
      throw new AssertionError("Failed with IOException", e);
    }
  }

}