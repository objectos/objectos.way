/*
 * Copyright (C) 2025 Objectos Software LTDA.
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

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;
import java.util.Set;
import objectos.way.HttpExchange.ParseStatus;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HttpExchangeTest {

  @FunctionalInterface
  private interface ThrowingConsumer<T> { void accept(T t) throws IOException; }

  private record HttpRecord(int bufferSizeInitial, int bufferSizeMax, Clock clock, Note.Sink noteSink) {
    public HttpExchange build(TestableSocket socket) {
      try {
        return new HttpExchange(socket, bufferSizeInitial, bufferSizeMax, clock, noteSink);
      } catch (IOException e) {
        throw new AssertionError("Failed with IOException", e);
      }
    }
  }

  @Test(description = """
  Minimum GET request with explicity close
  text/plain response
  """)
  public void testCase001() {
    test(
        http(128, 128, TestingClock.FIXED, TestingNoteSink.INSTANCE),

        """
        GET / HTTP/1.1\r
        Host: www.example.com\r
        Connection: close\r
        \r
        """,

        http -> {
          // request line
          assertEquals(http.method(), Http.Method.GET);
          assertEquals(http.path(), "/");
          assertEquals(http.rawQuery(), null);
          assertEquals(http.queryParam("x"), null);
          assertEquals(http.queryParamNames(), Set.of());

          // headers
          assertEquals(http.size(), 2);
          assertEquals(http.header(Http.HeaderName.HOST), "www.example.com");
          assertEquals(http.header(Http.HeaderName.CONNECTION), "close");

          // body
          assertEquals(ObjectosHttp.readAllBytes(http), Util.EMPTY_BYTE_ARRAY);

          // response phase
          final Media.Bytes media;
          media = Media.Bytes.textPlain("Hello World!\n", StandardCharsets.UTF_8);

          http.ok(media);
        },

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 13\r
        \r
        Hello World!
        """,

        keepAlive(false)
    );
  }

  @Test(description = """
  1. GET request with implied keep-alive
  2. GET request with explicit close
  """)
  public void testCase002() {
    final String body01 = """
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

    final String body02 = """
    * {
      box-sizing: border-box;
    }
    """;

    test(
        http(128, 128, TestingClock.FIXED, TestingNoteSink.INSTANCE),

        """
        GET /login HTTP/1.1\r
        Host: www.example.com\r
        \r
        """,

        http -> {
          // request line
          assertEquals(http.method(), Http.Method.GET);
          assertEquals(http.path(), "/login");
          assertEquals(http.rawQuery(), null);
          assertEquals(http.queryParamNames(), Set.of());

          // headers
          assertEquals(http.size(), 1);
          assertEquals(http.header(Http.HeaderName.HOST), "www.example.com");

          // body
          assertEquals(ObjectosHttp.readAllBytes(http), Util.EMPTY_BYTE_ARRAY);

          // response phase
          final Media.Bytes object01;
          object01 = Media.Bytes.textPlain(body01, StandardCharsets.UTF_8);

          http.ok(object01);
        },

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 171\r
        \r
        %s""".formatted(body01),

        keepAlive(true),

        """
        GET /login.css HTTP/1.1\r
        Host: www.example.com\r
        Connection: close\r
        \r
        """,

        http -> {
          // request line
          assertEquals(http.method(), Http.Method.GET);
          assertEquals(http.path(), "/login.css");

          // headers
          assertEquals(http.size(), 2);
          assertEquals(http.header(Http.HeaderName.HOST), "www.example.com");
          assertEquals(http.header(Http.HeaderName.CONNECTION), "close");

          // body
          assertEquals(ObjectosHttp.readAllBytes(http), Util.EMPTY_BYTE_ARRAY);

          // response phase
          final Media.Bytes object02;
          object02 = Media.Bytes.textPlain(body02, StandardCharsets.UTF_8);

          http.ok(object02);
        },

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 32\r
        \r
        %s""".formatted(body02),

        keepAlive(false)
    );
  }

  @Test(description = """
  It should handle unkonwn request headers
  """)
  public void testCase003() {
    final String body01 = """
    Hello World!
    """;

    test(
        http(128, 128, TestingClock.FIXED, TestingNoteSink.INSTANCE),

        """
        GET / HTTP/1.1\r
        Host: www.example.com\r
        Connection: close\r
        Foo: bar\r
        \r
        """,

        http -> {
          // headers
          assertEquals(http.header(Http.HeaderName.HOST), "www.example.com");
          assertEquals(http.header(Http.HeaderName.CONNECTION), "close");
          assertEquals(http.header(Http.HeaderName.of("Foo")), "bar");

          // response phase
          final Media.Bytes object;
          object = Media.Bytes.textPlain(body01, StandardCharsets.UTF_8);

          http.ok(object);
        },

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 13\r
        \r
        %s""".formatted(body01),

        keepAlive(false)
    );
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

    try (HttpExchange http = new HttpExchange(socket, 128, 128, Clock.systemDefaultZone(), TestingNoteSink.INSTANCE)) {
      // request phase
      ParseStatus parse;
      parse = http.parse();

      assertEquals(parse.isError(), false);

      // request line
      assertEquals(http.method(), Http.Method.GET);
      assertEquals(http.path(), "/index.html");
      assertEquals(http.rawQuery(), null);
      assertEquals(http.queryParamNames(), Set.of());

      // headers
      assertEquals(http.header(Http.HeaderName.HOST), "www.example.com");
      assertEquals(http.header(Http.HeaderName.CONNECTION), "close");
      assertEquals(http.header(Http.HeaderName.of("Foo")), "bar");

      // response phase
      dir = ObjectosHttp.createTempDir();

      Path index;
      index = dir.resolve("index.html");

      Files.writeString(index, body01, StandardCharsets.UTF_8);

      http.status0(Http.Status.OK);
      http.header0(Http.HeaderName.CONTENT_TYPE, "text/html; charset=utf-8");
      http.header0(Http.HeaderName.CONTENT_LENGTH, Files.size(index));
      http.header0(Http.HeaderName.DATE, "Wed, 28 Jun 2023 12:08:43 GMT");
      http.send0(index);

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
    test(
        http(128, 128, Clock.systemDefaultZone(), TestingNoteSink.INSTANCE),

        """
        GET /atom.xml HTTP/1.1\r
        Host: www.example.com\r
        If-None-Match: some%hash\r
        Connection: close\r
        \r
        """,

        http -> {
          // request line
          assertEquals(http.method(), Http.Method.GET);
          assertEquals(http.path(), "/atom.xml");
          assertEquals(http.rawQuery(), null);
          assertEquals(http.queryParamNames(), Set.of());

          // headers
          assertEquals(http.size(), 3);
          assertEquals(http.header(Http.HeaderName.HOST), "www.example.com");
          assertEquals(http.header(Http.HeaderName.IF_NONE_MATCH), "some%hash");
          assertEquals(http.header(Http.HeaderName.CONNECTION), "close");

          // response phase
          http.status0(Http.Status.NOT_MODIFIED);
          http.header0(Http.HeaderName.DATE, "Wed, 28 Jun 2023 12:08:43 GMT");
          http.header0(Http.HeaderName.ETAG, "some%hash");
          http.send0();
        },

        """
        HTTP/1.1 304 NOT MODIFIED\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        ETag: some%hash\r
        \r
        """,

        keepAlive(false)
    );
  }

  @Test(description = """
  Allow for NOT FOUND responses
  - it should set keep alive to false
  """)
  public void testCase006() {
    test(
        http(128, 128, Clock.systemDefaultZone(), TestingNoteSink.INSTANCE),

        """
        GET /xml-rpc.php HTTP/1.1\r
        Host: www.example.com\r
        \r
        """,

        http -> {
          // response phase
          http.status0(Http.Status.NOT_FOUND);
          http.header0(Http.HeaderName.CONNECTION, "close");
          http.header0(Http.HeaderName.DATE, "Wed, 28 Jun 2023 12:08:43 GMT");
          http.send0();
        },

        """
        HTTP/1.1 404 Not Found\r
        Connection: close\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        \r
        """,

        keepAlive(false)
    );
  }

  @Test(description = """
  Allow for query parameters
  - happy path
  """)
  public void testCase007() {
    test(
        http(128, 128, Clock.systemDefaultZone(), TestingNoteSink.INSTANCE),

        """
        GET /endpoint?foo=bar HTTP/1.1\r
        Host: www.example.com\r
        \r
        """,

        http -> {
          assertEquals(http.rawQuery(), "foo=bar");
          assertEquals(http.queryParam("foo"), "bar");
          assertEquals(http.queryParamNames(), Set.of("foo"));

          // response phase
          http.status0(Http.Status.NOT_FOUND);
          http.header0(Http.HeaderName.CONNECTION, "close");
          http.header0(Http.HeaderName.DATE, "Wed, 28 Jun 2023 12:08:43 GMT");
          http.send0();
        },

        """
        HTTP/1.1 404 Not Found\r
        Connection: close\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        \r
        """,

        keepAlive(false)
    );
  }

  @Test(description = """
  Minimal POST request
  - happy path
  """)
  public void testCase008() {
    test(
        http(128, 256, Clock.systemDefaultZone(), TestingNoteSink.INSTANCE),

        """
        POST /login HTTP/1.1\r
        Host: www.example.com\r
        Content-Length: 24\r
        Content-Type: application/x-www-form-urlencoded\r
        \r
        email=user%40example.com""",

        http -> {
          assertEquals(ObjectosHttp.readString(http), "email=user%40example.com");

          // response phase
          final String body = "Hello user@example.com. Please enter your password.\n";
          http.status0(Http.Status.SEE_OTHER);
          http.header0(Http.HeaderName.LOCATION, "/app");
          http.header0(Http.HeaderName.CONTENT_TYPE, "text/plain; charset=utf-8");
          http.header0(Http.HeaderName.CONTENT_LENGTH, "52");
          http.header0(Http.HeaderName.DATE, "Wed, 28 Jun 2023 12:08:43 GMT");
          http.send0(body.getBytes(StandardCharsets.UTF_8));
        },

        """
        HTTP/1.1 303 SEE OTHER\r
        Location: /app\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 52\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        \r
        Hello user@example.com. Please enter your password.
        """,

        keepAlive(true)
    );
  }

  @Test(description = """
  It should resize buffer if necessary when writing out the response headers
  """)
  public void testCase009() {
    test(
        http(128, 256, TestingClock.FIXED, TestingNoteSink.INSTANCE),

        """
        GET / HTTP/1.1\r
        Host: www.example.com\r
        Connection: close\r
        \r
        """,

        http -> {
          final String etag = "AVeryLongEtagValueToTriggerTheBufferResizeWhenWritingOutTheHeaders";
          final String body = "AAAA\n";

          http.status0(Http.Status.OK);
          http.header0(Http.HeaderName.CONTENT_TYPE, "text/plain; charset=utf-8");
          http.header0(Http.HeaderName.CONTENT_LENGTH, 5);
          http.header0(Http.HeaderName.DATE, "Wed, 28 Jun 2023 12:08:43 GMT");
          http.header0(Http.HeaderName.ETAG, etag);
          http.send0(body.getBytes(StandardCharsets.UTF_8));
        },

        """
        HTTP/1.1 200 OK\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 5\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        ETag: AVeryLongEtagValueToTriggerTheBufferResizeWhenWritingOutTheHeaders\r
        \r
        AAAA
        """,

        keepAlive(false)
    );
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

    try (HttpExchange http = new HttpExchange(socket, 128, 256, TestingClock.FIXED, TestingNoteSink.INSTANCE)) {
      ParseStatus parse;
      parse = http.parse();

      assertEquals(parse.isError(), false);

      http.respond(Http.Status.OK, writable, resp -> {
        resp.header(Http.HeaderName.CONTENT_LENGTH, 64);
      });

      Assert.fail("http.commit should have thrown");
    } catch (IllegalStateException expected) {
      String message;
      message = expected.getMessage();

      assertEquals(message, "Content-Length must not be set with a Lang.MediaWriter response");
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

    try (HttpExchange http = new HttpExchange(socket, 128, 256, TestingClock.FIXED, TestingNoteSink.INSTANCE)) {
      ParseStatus parse;
      parse = http.parse();

      assertEquals(parse.isError(), false);

      http.respond(Http.Status.OK, writable, resp -> {
        resp.header(Http.HeaderName.TRANSFER_ENCODING, "foobar");
      });

      Assert.fail("http.commit should have thrown");
    } catch (IllegalStateException expected) {
      String message;
      message = expected.getMessage();

      assertEquals(message, "Transfer-Encoding: chunked must be set with a Lang.MediaWriter response");
    } catch (IOException e) {
      throw new AssertionError("Failed with IOException", e);
    }
  }

  @Test(description = """
  CharWritable: single chunk
  """)
  public void testCase015() {
    test(
        http(128, 128, TestingClock.FIXED, TestingNoteSink.INSTANCE),

        """
        GET / HTTP/1.1\r
        Host: www.example.com\r
        \r
        """,

        http -> {
          TestingCharWritable writable = TestingCharWritable.ofLength(128);

          http.respond(Http.Status.OK, writable);
        },

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Transfer-Encoding: chunked\r
        \r
        80\r
        .................................................
        .................................................
        1234567890123456789012345678\r
        0\r
        \r
        """,

        keepAlive(true)
    );
  }

  @Test(description = """
  CharWritable: multiple chunks
  """)
  public void testCase016() {
    test(
        http(128, 128, TestingClock.FIXED, TestingNoteSink.INSTANCE),

        """
        GET / HTTP/1.1\r
        Host: www.example.com\r
        \r
        """,

        http -> {
          TestingCharWritable writable = TestingCharWritable.ofLength(256);

          http.respond(Http.Status.OK, writable);
        },

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
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
        """,

        keepAlive(true)
    );
  }

  @Test(description = """
  CharWritable: single chunk with buffer resize
  """)
  public void testCase017() {
    test(
        http(128, 256, TestingClock.FIXED, TestingNoteSink.INSTANCE),

        """
        GET / HTTP/1.1\r
        Host: www.example.com\r
        \r
        """,

        http -> {
          TestingCharWritable writable = TestingCharWritable.ofLength(256);

          http.respond(Http.Status.OK, writable);
        },

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
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
        """,

        keepAlive(true)
    );
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

    Lang.MediaWriter writable;
    writable = new Lang.MediaWriter() {
      @Override
      public String contentType() { return "text/plain"; }

      @Override
      public Charset mediaCharset() { return StandardCharsets.UTF_8; }

      @Override
      public void mediaTo(Appendable dest) throws IOException { dest.append(chunk256); }
    };

    String resp01 = """
    HTTP/1.1 200 OK\r
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

    try (HttpExchange http = new HttpExchange(socket, 128, 128, TestingClock.FIXED, TestingNoteSink.INSTANCE)) {
      ParseStatus parse;
      parse = http.parse();

      assertEquals(parse.isError(), false);

      http.respond(Http.Status.OK, writable);

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

    try (HttpExchange http = new HttpExchange(socket, 128, 128, TestingClock.FIXED, TestingNoteSink.INSTANCE)) {
      ParseStatus parse;
      parse = http.parse();

      assertEquals(parse.isError(), false);

      assertEquals(ObjectosHttp.readString(http), chunk256);
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

    try (HttpExchange http = new HttpExchange(socket, 1024, 4096, TestingClock.FIXED, TestingNoteSink.INSTANCE)) {
      ParseStatus parse;
      parse = http.parse();

      assertEquals(parse.isError(), false);

      // request line
      assertEquals(http.method(), Http.Method.GET);
      assertEquals(http.path(), "/");
      assertEquals(http.rawQuery(), null);
      assertEquals(http.queryParam(""), null);
      assertEquals(http.queryParamNames(), Set.of());

      // headers
      assertEquals(http.size(), 3);
      assertEquals(http.header(Http.HeaderName.HOST), "www.example.com");
      assertEquals(http.header(Http.HeaderName.FROM), "");
      assertEquals(http.header(Http.HeaderName.ACCEPT_ENCODING), "gzip, deflate, br");

      // body
      assertEquals(ObjectosHttp.readAllBytes(http), Util.EMPTY_BYTE_ARRAY);
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

    try (HttpExchange http = new HttpExchange(socket, 1024, 4096, TestingClock.FIXED, TestingNoteSink.INSTANCE)) {
      ParseStatus parse;
      parse = http.parse();

      assertEquals(parse.isError(), false);

      // request line
      assertEquals(http.method(), Http.Method.GET);
      assertEquals(http.path(), "/wiki/東京");
      assertEquals(http.rawQuery(), null);
      assertEquals(http.queryParam("null"), null);
      assertEquals(http.queryParamNames(), Set.of());

      // headers
      assertEquals(http.size(), 1);
      assertEquals(http.header(Http.HeaderName.HOST), "www.example.com");

      // body
      assertEquals(ObjectosHttp.readAllBytes(http), Util.EMPTY_BYTE_ARRAY);
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

    try (HttpExchange http = new HttpExchange(socket, 1024, 4096, TestingClock.FIXED, TestingNoteSink.INSTANCE)) {
      ParseStatus parse;
      parse = http.parse();

      assertEquals(parse.isError(), false);

      // request line
      assertEquals(http.method(), Http.Method.GET);
      assertEquals(http.path(), "/empty");
      assertEquals(http.rawQuery(), "");
      assertEquals(http.queryParam("null"), null);
      assertEquals(http.queryParamNames(), Set.of());

      // headers
      assertEquals(http.size(), 1);
      assertEquals(http.header(Http.HeaderName.HOST), "www.example.com");

      // body
      assertEquals(ObjectosHttp.readAllBytes(http), Util.EMPTY_BYTE_ARRAY);
    } catch (IOException e) {
      throw new AssertionError("Failed with IOException", e);
    }
  }

  @Test(description = """
  Disallow access to request body if response phase has started
  """)
  public void testCase023() {
    TestableSocket socket;
    socket = TestableSocket.of("""
    POST /login HTTP/1.1\r
    Host: www.example.com\r
    Content-Length: 24\r
    Content-Type: application/x-www-form-urlencoded\r
    \r
    email=user%40example.com""");

    try (HttpExchange http = new HttpExchange(socket, 128, 256, Clock.systemDefaultZone(), TestingNoteSink.INSTANCE)) {
      // request phase
      ParseStatus parse;
      parse = http.parse();

      assertEquals(parse.isError(), false);

      // early response start...

      http.status0(Http.Status.SEE_OTHER);

      try {
        http.bodyInputStream();

        Assert.fail();
      } catch (IllegalStateException expected) {

      }
    } catch (IOException e) {
      throw new AssertionError("Failed with IOException", e);
    }
  }

  @Test(description = """
  Support the Way-Request request header
  """)
  public void testCase024() {
    TestableSocket socket;
    socket = TestableSocket.of("""
    POST /login HTTP/1.1\r
    Host: www.example.com\r
    Content-Length: 24\r
    Content-Type: application/x-www-form-urlencoded\r
    Way-Request: true\r
    \r
    email=user%40example.com""");

    try (HttpExchange http = new HttpExchange(socket, 128, 256, Clock.systemDefaultZone(), TestingNoteSink.INSTANCE)) {
      ParseStatus parse;
      parse = http.parse();

      assertEquals(parse.isError(), false);

      // headers
      assertEquals(http.size(), 4);
      assertEquals(http.header(Http.HeaderName.HOST), "www.example.com");
      assertEquals(http.header(Http.HeaderName.CONTENT_LENGTH), "24");
      assertEquals(http.header(Http.HeaderName.CONTENT_TYPE), "application/x-www-form-urlencoded");
      assertEquals(http.header(Http.HeaderName.WAY_REQUEST), "true");
    } catch (IOException e) {
      throw new AssertionError("Failed with IOException", e);
    }
  }

  @Test(description = """
  Test all standard header request names
  """)
  public void testCase025() {
    StringBuilder req;
    req = new StringBuilder();

    req.append("GET / HTTP/1.1\r\n");

    for (HttpHeaderName name : HttpHeaderName.VALUES) {
      if (!name.isResponseOnly()) {
        req.append(name.headerCase());
        req.append(": ");
        req.append(Integer.toString(name.index()));
        req.append("\r\n");
      }
    }

    req.append("\r\n");
    req.append("123");

    TestableSocket socket;
    socket = TestableSocket.of(req.toString());

    try (HttpExchange http = new HttpExchange(socket, 128, 256, Clock.systemDefaultZone(), TestingNoteSink.INSTANCE)) {
      ParseStatus parse;
      parse = http.parse();

      assertEquals(parse.isError(), false);

      // headers
      for (HttpHeaderName name : HttpHeaderName.VALUES) {
        if (!name.isResponseOnly()) {
          assertEquals(http.header(name), Integer.toString(name.index()));
        }
      }
    } catch (IOException e) {
      throw new AssertionError("Failed with IOException", e);
    }
  }

  @Test(description = """
  Http.Response.respond(status, object)
  """)
  public void testCase026() {
    test(
        http(128, 128, TestingClock.FIXED, TestingNoteSink.INSTANCE),

        """
        GET /index.html HTTP/1.1\r
        Host: www.example.com\r
        Connection: close\r
        \r
        """,

        http -> {
          // response phase
          final Media.Bytes media;
          media = Media.Bytes.textPlain("Hello World!\n");

          http.ok(media);
        },

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 13\r
        \r
        Hello World!
        """,

        keepAlive(false)
    );
  }

  private HttpRecord http(int bufferSizeInitial, int bufferSizeMax, Clock clock, Note.Sink noteSink) {
    return new HttpRecord(bufferSizeInitial, bufferSizeMax, clock, noteSink);
  }

  private boolean keepAlive(boolean value) {
    return value;
  }

  private void test(
      HttpRecord httpRecord, String input,
      ThrowingConsumer<HttpExchange> test, String response, boolean keepAlive) {
    final TestableSocket socket;
    socket = TestableSocket.of(input);

    try (HttpExchange http = httpRecord.build(socket)) {
      ParseStatus parse;
      parse = http.parse();

      assertEquals(parse.isError(), false);

      test.accept(http);

      assertEquals(socket.outputAsString(), response);

      assertEquals(http.keepAlive(), keepAlive);
    } catch (IOException e) {
      throw new AssertionError("Failed with IOException", e);
    }
  }

  private void test(
      HttpRecord httpRecord,
      String input1,
      ThrowingConsumer<HttpExchange> test1, String response1, boolean keepAlive1,
      String input2,
      ThrowingConsumer<HttpExchange> test2, String response2, boolean keepAlive2) {
    final TestableSocket socket;
    socket = TestableSocket.of(input1, input2);

    try (HttpExchange http = httpRecord.build(socket)) {
      final ParseStatus parse1;
      parse1 = http.parse();

      assertEquals(parse1.isError(), false);

      test1.accept(http);

      assertEquals(socket.outputAsString(), response1);

      assertEquals(http.keepAlive(), keepAlive1);

      socket.outputReset();

      final ParseStatus parse2;
      parse2 = http.parse();

      assertEquals(parse2.isError(), false);

      test2.accept(http);

      assertEquals(socket.outputAsString(), response2);

      assertEquals(http.keepAlive(), keepAlive2);
    } catch (IOException e) {
      throw new AssertionError("Failed with IOException", e);
    }
  }

}