/*
 * Copyright (C) 2025-2026 Objectos Software LTDA.
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
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import objectos.way.Media;
import objectos.way.Y;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HttpServerTaskTest6Body {

  private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

  @DataProvider
  public Object[][] validProvider() {
    final String frag;
    frag = ".".repeat(256);

    return new Object[][] {
        {
            """
            \r
            """,

            EMPTY_BYTE_ARRAY,
            "empty: no content-length"
        },
        {
            """
            Content-Length: 0\r
            \r
            """,

            EMPTY_BYTE_ARRAY,
            "empty: content-length=0"
        },
        {
            """
            Content-Length: 256\r
            \r
            %s\
            """.formatted(frag),

            ascii(frag),
            "memory: content-length < buffer size"
        },
        {
            """
            Content-Length: 768\r
            \r
            %s\
            """.formatted(frag + frag + frag),

            ascii(frag + frag + frag),
            "memory: content-length > buffer size"
        },
        {
            """
            Content-Length: 1024\r
            \r
            %s\
            """.formatted(frag + frag + frag + frag),

            ascii(frag + frag + frag + frag),
            "memory: content-length == limit"
        }
    };
  }

  @Test(dataProvider = "validProvider")
  public void valid(String payload, byte[] result, String description) {
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = Y.socket("""
          POST / HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          %s\
          """.formatted(payload));

          opts.handler = http -> {
            assertEquals(bytes(http), result);

            http.ok(Media.Bytes.textPlain("OK\n"));
          };
        }),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 3\r
        \r
        OK
        """
    );
  }

  @Test(dataProvider = "validProvider")
  public void validSlowClient(String payload, byte[] result, String description) {
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = Y.socket(Y.slowStream(1, """
          POST / HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          %s\
          """.formatted(payload)));

          opts.handler = http -> {
            assertEquals(bytes(http), result);

            http.ok(Media.Bytes.textPlain("OK\n"));
          };
        }),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 3\r
        \r
        OK
        """
    );
  }

  @Test
  public void file() {
    final Path directory;
    directory = Y.nextTempDir();

    final String filename;
    filename = "%019d".formatted(123L);

    final Path file;
    file = directory.resolve(filename);

    final String content;
    content = ".".repeat(1536);

    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = Y.socket("""
          GET / HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          Content-Length: 1536\r
          \r
          %s\
          """.formatted(content));

          opts.bodyDirectory = directory;

          opts.id = 123L;

          opts.handler = http -> {
            assertEquals(bytes(http), ascii(content));

            assertTrue(Files.exists(file));

            http.ok(Media.Bytes.textPlain("OK\n"));
          };
        }),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 3\r
        \r
        OK
        """
    );

    assertFalse(Files.exists(file));
  }

  @Test
  public void fileSlowClient() {
    final Path directory;
    directory = Y.nextTempDir();

    final String filename;
    filename = "%019d".formatted(123L);

    final Path file;
    file = directory.resolve(filename);

    final String content;
    content = ".".repeat(1536);

    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = Y.socket(Y.slowStream(1, """
          GET / HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          Content-Length: 1536\r
          \r
          %s\
          """.formatted(content)));

          opts.bodyDirectory = directory;

          opts.id = 123L;

          opts.handler = http -> {
            assertEquals(bytes(http), ascii(content));

            assertTrue(Files.exists(file));

            http.ok(Media.Bytes.textPlain("OK\n"));
          };
        }),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 3\r
        \r
        OK
        """
    );

    assertFalse(Files.exists(file));
  }

  @DataProvider

  public Object[][] ioExceptionProvider() {
    return new Object[][] {
        {
            """
            Content-Length: 256\r
            \r
            %s\
            """.formatted(".".repeat(128)),

            "memory: content-length < buffer size"
        },
        {
            """
            Content-Length: 768\r
            \r
            %s\
            """.formatted(".".repeat(600)),

            "memory: content-length > buffer size"
        },
        {
            """
            Content-Length: 1024\r
            \r
            %s\
            """.formatted(".".repeat(768)),

            "memory: content-length == limit"
        }
    };
  }

  @Test(dataProvider = "ioExceptionProvider")
  public void ioException(String payload, String description) {
    var noteSink = new HttpServerTaskYNoteSink();

    final IOException ioe;
    ioe = Y.trimStackTrace(new IOException(), 1);

    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.id = 123L;

          opts.noteSink = noteSink;

          opts.socket = Y.socket(
              """
              POST / HTTP/1.1\r
              Host: www.example.com\r
              Connection: close\r
              %s\
              """.formatted(payload),

              ioe
          );
        }),

        ""
    );

    assertEquals(noteSink.id, 123L);
    assertEquals(noteSink.event, "socket.read");
    assertEquals(noteSink.thrown, ioe);
  }

  @Test(dataProvider = "ioExceptionProvider")
  public void eof(String payload, String description) {
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = Y.socket(
              """
              POST / HTTP/1.1\r
              Host: www.example.com\r
              Connection: close\r
              %s\
              """.formatted(payload)
          );
        }),

        """
        HTTP/1.1 400 Bad Request\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Connection: close\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 25\r
        \r
        Incomplete request body.
        """
    );
  }

  private byte[] bytes(HttpExchange http) {
    try (
        InputStream is = http.bodyInputStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
    ) {
      is.transferTo(out);

      return out.toByteArray();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  @DataProvider
  public Object[][] badRequestProvider() {
    return new Object[][] {
        {
            """
            POST / HTTP/1.1\r
            Host: www.example.com\r
            Connection: close\r
            Content-Type: text/plain\r
            Content-Length: 5\r
            Transfer-Encoding: chunked\r
            \r
            Uh-Oh
            """,
            "request contains both transfer-encoding and content-length"
        },
        {
            """
            POST / HTTP/1.1\r
            Host: www.example.com\r
            Connection: close\r
            Content-Type: text/plain\r
            Content-Length: five bytes\r
            \r
            Uh-Oh
            """,
            "request contains an invalid content-length"
        }
    };
  }

  @Test(dataProvider = "badRequestProvider")
  public void badRequest(String request, String description) {
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = Y.socket(request);
        }),

        """
        HTTP/1.1 400 Bad Request\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Connection: close\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 25\r
        \r
        Invalid request headers.
        """
    );
  }

  @Test
  public void lengthRequired() {
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = Y.socket("""
          POST / HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          Content-Type: text/plain\r
          \r
          Uh-Oh
          """);
        }),

        """
        HTTP/1.1 411 Length Required\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Connection: close\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 25\r
        \r
        Invalid request headers.
        """
    );
  }

  @DataProvider
  public Object[][] contentTooLargeProvider() {
    return new Object[][] {
        {
            """
            POST / HTTP/1.1\r
            Host: www.example.com\r
            Connection: close\r
            Content-Type: text/plain; charset=utf-8\r
            Content-Length: 9223372036854775808\r
            \r
            """,

            "Content-Length unsigned long overflow"
        },
        {
            """
            POST / HTTP/1.1\r
            Host: www.example.com\r
            Connection: close\r
            Content-Type: text/plain; charset=utf-8\r
            Content-Length: 65000\r
            \r
            """,

            "Content-Length exceeds configured limit"
        }
    };
  }

  @Test(dataProvider = "contentTooLargeProvider")
  public void contentTooLarge(String request, String description) {
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = Y.socket(request);
        }),

        """
        HTTP/1.1 413 Content Too Large\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Connection: close\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 69\r
        \r
        The request message body exceeds the server's maximum allowed limit.
        """
    );
  }

  private byte[] ascii(String s) {
    return s.getBytes(StandardCharsets.US_ASCII);
  }

}