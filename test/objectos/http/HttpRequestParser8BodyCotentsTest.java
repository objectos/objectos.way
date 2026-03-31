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
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import objectos.internal.Util;
import objectos.way.Y;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HttpRequestParser8BodyCotentsTest {

  @Test(enabled = false, description = "empty: no content-length")
  public void empty01() throws IOException {
    final HttpRequest req;
    req = HttpRequestParserY.parse(
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

  @Test(enabled = false, description = "empty: content-length=0")
  public void empty02() throws IOException {
    final HttpRequest req;
    req = HttpRequestParserY.parse(
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

  @Test(enabled = false, description = "buffer: no read")
  public void buffer01() throws IOException {
    final HttpRequest req;
    req = HttpRequestParserY.parse(
        test -> test.bufferSize(256, 512),

        iso8859("""
        POST / HTTP/1.1\r
        Host: www.example.com\r
        Content-Length: 24\r
        \r
        email=user%40example.com\
        """)
    );

    assertEquals(toByteArray(req), iso8859("email=user%40example.com"));
  }

  @Test(enabled = false, description = "buffer: read")
  public void buffer02() throws IOException {
    final HttpRequest req;
    req = HttpRequestParserY.parse(
        test -> test.bufferSize(256, 512),

        iso8859("""
        POST / HTTP/1.1\r
        Host: www.example.com\r
        Content-Length: 24\r
        \r
        email=\
        """),

        iso8859("user%40example.com")
    );

    assertEquals(toByteArray(req), iso8859("email=user%40example.com"));
  }

  @Test(enabled = false, description = "buffer: read + resize")
  public void buffer03() throws IOException {
    final String frag;
    frag = ".".repeat(100);

    final HttpRequest req;
    req = HttpRequestParserY.parse(
        test -> {
          test.bufferSize(2, 512);

          test.bodySizeMax(400);
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

  @Test(enabled = false, description = "buffer: read + IOException")
  public void buffer04() {
    final String frag;
    frag = ".".repeat(16);

    final IOException ioe;
    ioe = Y.trimStackTrace(new IOException(), 1);

    try {
      HttpRequestParserY.parse(
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

  @Test(enabled = false, description = "buffer: read + resize + IOException")
  public void buffer05() {
    final String frag;
    frag = ".".repeat(32);

    final IOException ioe;
    ioe = Y.trimStackTrace(new IOException(), 1);

    try {
      HttpRequestParserY.parse(
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

  @Test(enabled = false, description = "buffer: read + EOF")
  public void buffer06() throws IOException {
    final String frag;
    frag = ".".repeat(32);

    try {
      HttpRequestParserY.parse(
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
      //assertSame(expected.kind, InvalidRequestBody.EOF);
    }
  }

  @Test(enabled = false, description = "buffer: slow client")
  public void buffer07() throws IOException {
    final String frag;
    frag = ".".repeat(100);

    final HttpRequest req;
    req = HttpRequestParserY.parse(
        test -> {
          test.bufferSize(2, 512);

          test.bodySizeMax(400);
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

  private static class Tester extends HttpExchangeBodyFiles {

    private final Path directory = Y.nextTempDir();

    @Override
    final Path directory() {
      return directory;
    }

  }

  @Test(enabled = false, description = "file: happy-path")
  public void file01() throws IOException {
    final Tester tester;
    tester = new Tester();

    final String content;
    content = ".o".repeat(512);

    final HttpRequest req;
    req = HttpRequestParserY.parse(
        test -> {
          test.bodyFiles(tester);

          test.bufferSize(2, 256);
        },

        """
        POST / HTTP/1.1\r
        Host: www.example.com\r
        Content-Type: text/plain\r
        Content-Length: 1024\r
        \r
        """,

        content
    );

    assertEquals(toByteArray(req), iso8859(content));
  }

  @Test(enabled = false, description = "file: client read IOException")
  public void file02() {
    final Tester tester;
    tester = new Tester();

    final String frag;
    frag = ".".repeat(512);

    final IOException ioe;
    ioe = Y.trimStackTrace(new IOException(), 1);

    try {
      HttpRequestParserY.parse(
          test -> {
            test.bodyFiles(tester);

            test.bufferSize(128, 256);
          },

          """
          POST / HTTP/1.1\r
          Host: www.example.com\r
          Content-Type: text/plain\r
          Content-Length: 1024\r
          \r
          """,

          frag, ioe
      );

      Assert.fail("It should have thrown");
    } catch (IOException expected) {
      assertSame(expected, ioe);
    }
  }

  @Test(enabled = false, description = "file: ISE on getOutputStream")
  public void file03() {
    final IOException ioe;
    ioe = Y.trimStackTrace(new IOException(), 1);

    final Tester tester;
    tester = new Tester() {
      @Override
      public final OutputStream newOutputStream(Path file) throws IOException {
        throw ioe;
      }
    };

    final String contents;
    contents = ".".repeat(1024);

    try {
      HttpRequestParserY.parse(
          test -> {
            test.bodyFiles(tester);

            test.bufferSize(128, 256);
          },

          """
          POST / HTTP/1.1\r
          Host: www.example.com\r
          Content-Type: text/plain\r
          Content-Length: 1024\r
          \r
          """,

          contents
      );

      Assert.fail("It should have thrown");
    } catch (IOException expected) {
      assertSame(expected, ioe);
    }
  }

  @Test(enabled = false, description = "file: ISE on OutputStream.write")
  public void file04() {
    final IOException ioe;
    ioe = Y.trimStackTrace(new IOException(), 1);

    final Tester tester;
    tester = new Tester() {
      @Override
      public final OutputStream newOutputStream(Path file) throws IOException {
        return new OutputStream() {
          @Override
          public final void write(int b) throws IOException {
            throw new UnsupportedOperationException("Implement me");
          }

          @Override
          public final void write(byte[] b, int off, int len) throws IOException {
            throw ioe;
          }
        };
      }
    };

    final String contents;
    contents = ".".repeat(1024);

    try {
      HttpRequestParserY.parse(
          test -> {
            test.bodyFiles(tester);

            test.bufferSize(128, 256);
          },

          """
          POST / HTTP/1.1\r
          Host: www.example.com\r
          Content-Type: text/plain\r
          Content-Length: 1024\r
          \r
          """,

          contents
      );

      Assert.fail("It should have thrown");
    } catch (IOException expected) {
      assertSame(expected, ioe);
    }
  }

  @Test(enabled = false, description = "file: ISE on OutputStream.close")
  public void file05() {
    final IOException ioe;
    ioe = Y.trimStackTrace(new IOException(), 1);

    final Tester tester;
    tester = new Tester() {
      @Override
      public final OutputStream newOutputStream(Path file) throws IOException {
        return new OutputStream() {
          @Override
          public final void close() throws IOException {
            throw ioe;
          }

          @Override
          public final void write(int b) throws IOException {}

          @Override
          public final void write(byte[] b, int off, int len) throws IOException {}
        };
      }
    };

    final String contents;
    contents = ".".repeat(1024);

    try {
      HttpRequestParserY.parse(
          test -> {
            test.bodyFiles(tester);

            test.bufferSize(128, 256);
          },

          """
          POST / HTTP/1.1\r
          Host: www.example.com\r
          Content-Type: text/plain\r
          Content-Length: 1024\r
          \r
          """,

          contents
      );

      Assert.fail("It should have thrown");
    } catch (IOException expected) {
      assertSame(expected, ioe);
    }
  }

  @Test(enabled = false, description = "file: slow client")
  public void file06() throws IOException {
    final Tester tester;
    tester = new Tester();

    final String content;
    content = ".o".repeat(512);

    final HttpRequest req;
    req = HttpRequestParserY.parse(
        test -> {
          test.bodyFiles(tester);

          test.bufferSize(2, 256);
        },

        """
        POST / HTTP/1.1\r
        Host: www.example.com\r
        Content-Type: text/plain\r
        Content-Length: 1024\r
        \r
        """,

        content
    );

    assertEquals(toByteArray(req), iso8859(content));
  }

  @Test
  public void longMaxValue() {
    long parsed1 = Long.parseLong("9223372036854775807");
    assertEquals(parsed1, 9_223_372_036_854_775_807L);

    try {
      Long.parseLong("9223372036854775808");
      Assert.fail();
    } catch (NumberFormatException expected) {

    }
  }

  /*
  
  @DataProvider
  public Object[][] badRequestProvider() {
    return new Object[][] {
        {
            """
            POST / HTTP/1.1\r
            Host: www.example.com\r
            Content-Type: text/plain\r
            Content-Length: 5\r
            Transfer-Encoding: chunked\r
            \r
            Uh-Oh
            """,
  
            InvalidRequestHeaders.BOTH_CL_TE,
  
            "request contains both transfer-encoding and content-length"
        },
  
        {
            """
            POST / HTTP/1.1\r
            Host: www.example.com\r
            Content-Type: text/plain\r
            Content-Length: five bytes\r
            \r
            Uh-Oh
            """,
  
            InvalidRequestHeaders.INVALID_CONTENT_LENGTH,
  
            "request contains an invalid content-length"
        },
  
        {
            """
            POST / HTTP/1.1\r
            Host: www.example.com\r
            Content-Type: text/plain\r
            \r
            Uh-Oh
            """,
  
            InvalidRequestHeaders.LENGTH_REQUIRED,
  
            "request requires content-length"
        },
  
        {
            """
            POST / HTTP/1.1\r
            Host: host\r
            Content-Type: text/plain; charset=utf-8\r
            Content-Length: 9223372036854775808\r
            \r
            """,
  
            InvalidRequestHeaders.CONTENT_TOO_LARGE,
  
            "Content-Length unsigned long overflow"
        },
  
        {
            """
            POST / HTTP/1.1\r
            Host: host\r
            Content-Type: text/plain; charset=utf-8\r
            Content-Length: 65\r
            \r
            """,
  
            InvalidRequestHeaders.CONTENT_TOO_LARGE,
  
            "Content-Length exceeds configured limit"
        }
  
    };
  }

  */

  @SuppressWarnings("exports")
  @Test(enabled = false, dataProvider = "badRequestProvider")
  public void badRequest(String request, HttpClientException.Kind kind, String description) throws IOException {
    try {
      HttpRequestParserY.parse(
          test -> {
            test.bufferSize(128, 128);

            test.bodySizeMax(64);
          },

          request
      );

      Assert.fail("It should have thrown");
    } catch (HttpClientException expected) {
      assertEquals(expected.kind, kind);
    }
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
