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

public class HttpExchangeTest {

  /*
  
  @Test(description = """
  Fail if:
  - content-length is set
  - body instanceof CharWritable
  """)
  public void testCase013() {
    Socket socket;
    socket = Y.socket("""
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
    Socket socket;
    socket = Y.socket("""
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
    Socket socket;
    socket = Y.socket("""
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

      assertEquals(http.toString(), resp01);

      assertEquals(http.keepAlive(), true);
    } catch (IOException e) {
      throw new AssertionError("Failed with IOException", e);
    }
  }

  @Test(description = """
  Support the Way-Request request header
  """)
  public void testCase024() {
    Socket socket;
    socket = Y.socket("""
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
  
  */

}