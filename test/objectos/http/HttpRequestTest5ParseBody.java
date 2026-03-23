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
import static org.testng.Assert.assertSame;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import objectos.http.HttpRequestParser.InvalidRequestBody;
import objectos.internal.Util;
import objectos.way.Y;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HttpRequestTest5ParseBody {

  @Test(description = "empty: no content-length")
  public void empty01() throws IOException {
    final HttpRequest req;
    req = HttpRequestTester.parse(
        test -> test.bufferSize(256, 512),

        iso8859("""
        GET / HTTP/1.1\r
        Host: www.example.com\r
        Connection: close\r
        \r
        """)
    );

    assertEquals(toByteArray(req), Util.EMPTY_BYTE_ARRAY);
  }

  @Test(description = "empty: content-length=0")
  public void empty02() throws IOException {
    final HttpRequest req;
    req = HttpRequestTester.parse(
        test -> test.bufferSize(256, 512),

        iso8859("""
        GET / HTTP/1.1\r
        Host: www.example.com\r
        Content-Length: 0\r
        \r
        """)
    );

    assertEquals(toByteArray(req), Util.EMPTY_BYTE_ARRAY);
  }

  @Test(description = "buffer: no read")
  public void buffer01() throws IOException {
    final HttpRequest req;
    req = HttpRequestTester.parse(
        test -> test.bufferSize(256, 512),

        iso8859("""
        POST / HTTP/1.1\r
        Host: www.example.com\r
        Content-Length: 24\r
        Content-Type: application/x-www-form-urlencoded\r
        \r
        email=user%40example.com\
        """)
    );

    assertEquals(toByteArray(req), iso8859("email=user%40example.com"));
  }

  @Test(description = "buffer: read")
  public void buffer02() throws IOException {
    final HttpRequest req;
    req = HttpRequestTester.parse(
        test -> test.bufferSize(256, 512),

        iso8859("""
        POST / HTTP/1.1\r
        Host: www.example.com\r
        Content-Length: 24\r
        Content-Type: application/x-www-form-urlencoded\r
        \r
        email=\
        """),

        iso8859("user%40example.com")
    );

    assertEquals(toByteArray(req), iso8859("email=user%40example.com"));
  }

  @Test(description = "buffer: read + resize")
  public void buffer03() throws IOException {
    final String frag;
    frag = ".".repeat(100);

    final HttpRequest req;
    req = HttpRequestTester.parse(
        test -> {
          test.bufferSize(2, 512);

          test.requestBodyMaxSize(400);
        },

        """
        POST / HTTP/1.1\r
        Host: www.example.com\r
        Content-Type: text/plain\r
        Content-Length: 400\r
        \r
        """,

        frag, frag, frag, frag
    );

    assertEquals(toByteArray(req), iso8859(frag + frag + frag + frag));
  }

  @Test(description = "buffer: read + IOException")
  public void buffer04() {
    final String frag;
    frag = ".".repeat(16);

    final IOException ioe;
    ioe = Y.trimStackTrace(new IOException(), 1);

    try {
      HttpRequestTester.parse(
          test -> test.bufferSize(256, 512),

          """
          POST / HTTP/1.1\r
          Host: www.example.com\r
          Content-Length: 32\r
          Content-Type: text/plain\r
          \r
          """,

          frag, ioe
      );

      Assert.fail("It should have thrown");
    } catch (IOException expected) {
      assertSame(expected, ioe);
    }
  }

  @Test(description = "buffer: read + resize + IOException")
  public void buffer05() {
    final String frag;
    frag = ".".repeat(32);

    final IOException ioe;
    ioe = Y.trimStackTrace(new IOException(), 1);

    try {
      HttpRequestTester.parse(
          test -> test.bufferSize(2, 512),

          """
          POST / HTTP/1.1\r
          Host: www.example.com\r
          Content-Length: 256\r
          Content-Type: text/plain\r
          \r
          """,

          frag, ioe
      );

      Assert.fail("It should have thrown");
    } catch (IOException expected) {
      assertSame(expected, ioe);
    }
  }

  @Test(description = "buffer: read + EOF")
  public void buffer06() throws IOException {
    final String frag;
    frag = ".".repeat(32);

    try {
      HttpRequestTester.parse(
          test -> test.bufferSize(256, 512),

          """
          POST / HTTP/1.1\r
          Host: www.example.com\r
          Content-Length: 256\r
          Content-Type: text/plain\r
          \r
          """,

          frag
      );

      Assert.fail("It should have thrown");
    } catch (HttpClientException expected) {
      assertSame(expected.kind, InvalidRequestBody.EOF);
    }
  }

  @Test(description = "buffer: slow client")
  public void buffer07() throws IOException {
    final String frag;
    frag = ".".repeat(100);

    final HttpRequest req;
    req = HttpRequestTester.parse(
        test -> {
          test.bufferSize(2, 512);

          test.requestBodyMaxSize(400);
        },

        Y.slowStream(1, """
        POST / HTTP/1.1\r
        Host: www.example.com\r
        Content-Type: text/plain\r
        Content-Length: 100\r
        \r
        %s""".formatted(frag))
    );

    assertEquals(toByteArray(req), iso8859(frag));
  }

  private byte[] toByteArray(HttpRequest req) {
    final ByteArrayOutputStream out;
    out = new ByteArrayOutputStream();

    try (InputStream in = req.bodyInputStream()) {
      in.transferTo(out);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }

    return out.toByteArray();
  }

  private byte[] iso8859(String s) {
    return s.getBytes(StandardCharsets.ISO_8859_1);
  }

}
