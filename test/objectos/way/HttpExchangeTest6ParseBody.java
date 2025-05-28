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
import static org.testng.Assert.assertFalse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HttpExchangeTest6ParseBody extends HttpExchangeTest {

  @Test(description = "empty: no content-length")
  public void empty01() {
    test(
        """
        GET / HTTP/1.1\r
        Host: www.example.com\r
        Connection: close\r
        \r
        """,

        Util.EMPTY_BYTE_ARRAY
    );
  }

  @Test(description = "empty: content-length=0")
  public void empty02() {
    test(
        """
        POST / HTTP/1.1\r
        Host: www.example.com\r
        Content-Length: 0\r
        \r
        """,

        Util.EMPTY_BYTE_ARRAY
    );
  }

  @Test(description = "buffer: no read")
  public void buffer01() {
    test(
        """
        POST / HTTP/1.1\r
        Host: www.example.com\r
        Content-Length: 24\r
        Content-Type: application/x-www-form-urlencoded\r
        \r
        email=user%40example.com\
        """,

        ascii("email=user%40example.com")
    );
  }

  @Test(description = "buffer: read")
  public void buffer02() {
    test(
        """
        POST / HTTP/1.1\r
        Host: www.example.com\r
        Content-Length: 24\r
        Content-Type: application/x-www-form-urlencoded\r
        \r
        email=\
        """,

        "user%40example.com",

        ascii("email=user%40example.com")
    );
  }

  @Test(description = "buffer: read + resize")
  public void buffer03() {
    final String frag;
    frag = ".".repeat(100);

    test(
        2, 512,

        arr(
            """
            POST / HTTP/1.1\r
            Host: www.example.com\r
            Content-Type: text/plain\r
            Content-Length: 400\r
            \r
            """,

            frag, frag, frag, frag
        ),

        ascii(frag + frag + frag + frag)
    );
  }

  @Test(description = "buffer: read + IOException")
  public void buffer04() {
    final String frag;
    frag = ".".repeat(16);

    final IOException ioe;
    ioe = Y.trimStackTrace(new IOException(), 1);

    testError(
        256, 512,

        arr(
            """
            POST / HTTP/1.1\r
            Host: www.example.com\r
            Content-Length: 32\r
            Content-Type: text/plain\r
            \r
            """,

            frag, ioe
        ),

        ""
    );
  }

  @Test(description = "buffer: read + resize + IOException")
  public void buffer05() {
    final String frag;
    frag = ".".repeat(128);

    final IOException ioe;
    ioe = Y.trimStackTrace(new IOException(), 1);

    testError(
        2, 512,

        arr(
            """
            POST / HTTP/1.1\r
            Host: www.example.com\r
            Content-Length: 256\r
            Content-Type: text/plain\r
            \r
            """,

            frag, ioe
        ),

        ""
    );
  }

  @Test(description = "buffer: read + EOF")
  public void buffer06() {
    final String frag;
    frag = ".".repeat(32);

    testError(
        256, 512,

        arr(
            """
            POST / HTTP/1.1\r
            Host: www.example.com\r
            Content-Length: 256\r
            Content-Type: text/plain\r
            \r
            """,

            frag
        ),

        ""
    );
  }

  private static class Tester extends HttpExchangeBodyFiles {

    private final Path directory = Y.nextTempDir();

    private Path file;

    @Override
    public final Path file(long id) throws IOException {
      return file = super.file(id);
    }

    @Override
    final Path directory() {
      return directory;
    }

    public final boolean fileExists() {
      return Files.exists(file);
    }

  }

  @Test(description = "file: happy-path")
  public void file01() {
    final Tester tester;
    tester = new Tester();

    final String content;
    content = ".o".repeat(512);

    exec(test -> {
      test.bodyFiles(tester);

      test.bufferSize(2, 256);

      test.xch(xch -> {
        xch.req("""
        POST / HTTP/1.1\r
        Host: www.example.com\r
        Content-Type: text/plain\r
        Content-Length: 1024\r
        \r
        """);

        xch.req(content);

        xch.handler(http -> {
          try (InputStream in = http.bodyInputStream()) {
            assertEquals(in.readAllBytes(), ascii(content));
          } catch (IOException e) {
            throw new UncheckedIOException(e);
          }

          http.ok(OK);
        });

        xch.resp(OK_RESP);
      });
    });

    assertFalse(tester.fileExists());
  }

  @Test(description = "file: client read IOException")
  public void file02() {
    final String frag;
    frag = ".".repeat(512);

    final IOException ioe;
    ioe = Y.trimStackTrace(new IOException(), 1);

    testError(
        128, 256,

        arr(
            """
            POST / HTTP/1.1\r
            Host: www.example.com\r
            Content-Type: text/plain\r
            Content-Length: 1024\r
            \r
            """,

            frag, ioe
        ),

        ""
    );
  }

  @Test(description = "file: ISE on getOutputStream")
  public void file03() {
    final Tester tester;
    tester = new Tester() {
      @Override
      public final OutputStream newOutputStream(Path file) throws IOException {
        throw Y.trimStackTrace(new IOException(), 1);
      }
    };

    final String contents;
    contents = ".".repeat(1024);

    exec(test -> {
      test.bodyFiles(tester);

      test.bufferSize(128, 256);

      test.xch(xch -> {
        xch.req("""
        POST / HTTP/1.1\r
        Host: www.example.com\r
        Content-Type: text/plain\r
        Content-Length: 1024\r
        \r
        """);

        xch.req(contents);

        xch.shouldHandle(false);

        xch.resp("""
        HTTP/1.1 500 Internal Server Error\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 82\r
        Connection: close\r
        \r
        The server encountered an internal error and was unable to complete your request.
        """);
      });
    });

    assertFalse(tester.fileExists());
  }

  @Test(description = "file: ISE on OutputStream.write")
  public void file04() {
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
            throw Y.trimStackTrace(new IOException(), 1);
          }
        };
      }
    };

    final String contents;
    contents = ".".repeat(1024);

    exec(test -> {
      test.bodyFiles(tester);

      test.bufferSize(128, 256);

      test.xch(xch -> {
        xch.req("""
        POST / HTTP/1.1\r
        Host: www.example.com\r
        Content-Type: text/plain\r
        Content-Length: 1024\r
        \r
        """);

        xch.req(contents);

        xch.shouldHandle(false);

        xch.resp("""
        HTTP/1.1 500 Internal Server Error\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 82\r
        Connection: close\r
        \r
        The server encountered an internal error and was unable to complete your request.
        """);
      });
    });

    assertFalse(tester.fileExists());
  }

  @Test(description = "file: ISE on OutputStream.close")
  public void file05() {
    final Tester tester;
    tester = new Tester() {
      @Override
      public final OutputStream newOutputStream(Path file) throws IOException {
        return new OutputStream() {
          @Override
          public final void close() throws IOException {
            throw Y.trimStackTrace(new IOException(), 1);
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

    exec(test -> {
      test.bodyFiles(tester);

      test.bufferSize(128, 256);

      test.xch(xch -> {
        xch.req("""
        POST / HTTP/1.1\r
        Host: www.example.com\r
        Content-Type: text/plain\r
        Content-Length: 1024\r
        \r
        """);

        xch.req(contents);

        xch.shouldHandle(false);

        xch.resp("""
        HTTP/1.1 500 Internal Server Error\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 82\r
        Connection: close\r
        \r
        The server encountered an internal error and was unable to complete your request.
        """);
      });
    });

    assertFalse(tester.fileExists());
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

  @DataProvider
  public Object[][] contentTooLargeProvider() {
    return new Object[][] {
        {"""
        POST / HTTP/1.1\r
        Host: host\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 9223372036854775808\r
        \r
        """, "Content-Length unsigned long overflow"}
    };
  }

  @Test(dataProvider = "contentTooLargeProvider")
  public void contentTooLarge(String request, String description) {
    exec(test -> {
      test.bufferSize(128, 128);

      test.xch(xch -> {
        xch.req(request);

        xch.shouldHandle(false);

        xch.resp("""
        HTTP/1.1 413 Content Too Large\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 69\r
        Connection: close\r
        \r
        The request message body exceeds the server's maximum allowed limit.
        """);
      });
    });
  }

  private byte[] ascii(String s) {
    return s.getBytes(StandardCharsets.US_ASCII);
  }

  private void test(Object o1, byte[] body) {
    test(256, 512, arr(o1), body);
  }

  private void test(Object o1, Object o2, byte[] body) {
    test(256, 512, arr(o1, o2), body);
  }

  private void test(int initial, int max, Object[] data, byte[] body) {
    exec(test -> {
      test.bufferSize(initial, max);

      test.xch(xch -> {
        for (Object o : data) {
          xch.req(o);
        }

        xch.shouldHandle(true);

        xch.handler(http -> {
          final ByteArrayOutputStream out;
          out = new ByteArrayOutputStream();

          try (InputStream in = http.bodyInputStream()) {
            in.transferTo(out);
          } catch (IOException e) {
            throw new UncheckedIOException(e);
          }

          final byte[] result;
          result = out.toByteArray();

          assertEquals(result, body);

          http.ok(OK);
        });

        xch.resp(OK_RESP);
      });
    });
  }

  private void testError(int initial, int max, Object[] data, String expected) {
    exec(test -> {
      test.bufferSize(initial, max);
      test.xch(xch -> {
        for (Object o : data) {
          xch.req(o);
        }
        xch.shouldHandle(false);
        xch.resp(expected);
      });
    });
  }

}