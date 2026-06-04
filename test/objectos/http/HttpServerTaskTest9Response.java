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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;
import objectos.lang.Throwables;
import objectos.way.Media;
import objectos.way.Y;
import objectos.y.InputStreamY;
import objectos.y.OutputStreamY;
import objectos.y.PathY;
import objectos.y.SocketY;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HttpServerTaskTest9Response {

  @SuppressWarnings("exports")
  @DataProvider
  public Iterator<Y.MediaKind> mediaKindProvider() {
    return EnumSet.allOf(Y.MediaKind.class).iterator();
  }

  // 4xx responses

  @Test(description = "error(status)")
  public void error01() {
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = SocketY.of("""
          POST /1 HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """);

          opts.handler = http -> http.error(HttpStatus.BAD_REQUEST);
        }),

        """
        HTTP/1.1 400 Bad Request\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Connection: close\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 16\r
        \r
        400 Bad Request
        """
    );
  }

  @Test(description = "erorr(status, msg)")
  public void error02() {
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = SocketY.of("""
          POST /1 HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """);

          opts.handler = http -> http.error(HttpStatus.FORBIDDEN, "Invalid credentials");
        }),

        """
        HTTP/1.1 403 Forbidden\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Connection: close\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 35\r
        \r
        403 Forbidden

        Invalid credentials
        """
    );
  }

  @Test(description = "error(status, e)")
  public void error03() {
    final IOException e;
    e = Throwables.trimStackTrace(new IOException(), 1);

    final StackTraceElement[] copy;
    copy = new StackTraceElement[1];

    copy[0] = new StackTraceElement("objectos.way.Test", "error", "Test.java", 123);

    e.setStackTrace(copy);

    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = SocketY.of("""
          POST /1 HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """);

          opts.handler = http -> http.error(HttpStatus.INTERNAL_SERVER_ERROR, e);
        }),

        """
        HTTP/1.1 500 Internal Server Error\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Connection: close\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 90\r
        \r
        500 Internal Server Error

        java.io.IOException
        	at objectos.way.Test.error(Test.java:123)
        """
    );
  }

  // response builder

  @Test
  public void respond01() {
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = SocketY.of("""
          GET /1 HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """);

          opts.handler = http -> {
            http.status(HttpStatus.NOT_FOUND);

            http.header(HttpHeaderName.DATE, http.now());

            http.header(HttpHeaderName.CONTENT_LENGTH, 0L);

            http.send();
          };
        }),

        """
        HTTP/1.1 404 Not Found\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Length: 0\r
        \r
        """
    );
  }

  @Test
  public void respond02() {
    final byte[] body;
    body = "FOO\n".getBytes(StandardCharsets.US_ASCII);

    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = SocketY.of("""
          GET /1 HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """);

          opts.handler = http -> {
            http.status(HttpStatus.OK);

            http.header(HttpHeaderName.DATE, http.now());

            http.header(HttpHeaderName.CONTENT_TYPE, "text/plain");

            http.header(HttpHeaderName.CONTENT_LENGTH, body.length);

            http.send(body, 0, body.length);
          };
        }),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain\r
        Content-Length: 4\r
        \r
        FOO
        """
    );
  }

  @Test
  public void respond03() {
    final Media.Bytes media;
    media = Media.Bytes.textPlain("FOO\n");

    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = SocketY.of("""
          GET /1 HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """);

          opts.handler = http -> {
            http.status(HttpStatus.OK);

            http.header(HttpHeaderName.CONNECTION, "close");

            http.header(HttpHeaderName.DATE, http.now());

            http.send(media);
          };
        }),

        """
        HTTP/1.1 200 OK\r
        Connection: close\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 4\r
        \r
        FOO
        """
    );
  }

  @SuppressWarnings("exports")
  @Test(dataProvider = "mediaKindProvider")
  public void respond04(Y.MediaKind kind) {
    final Media media;
    media = Y.mediaOf(kind, 4);

    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.bufferSize = 256;

          opts.socket = SocketY.of("""
          GET /1 HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """);

          opts.handler = http -> {
            http.status(HttpStatus.OK);

            http.header(HttpHeaderName.CONNECTION, "close");

            http.header(HttpHeaderName.DATE, http.now());

            http.send(media);
          };
        }),

        """
        HTTP/1.1 200 OK\r
        Connection: close\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Transfer-Encoding: chunked\r
        \r
        04\r
        1234\r
        0\r
        \r
        """
    );
  }

  @DataProvider
  public Iterator<HttpStatus> respondStatusProvider() {
    final HttpStatus0[] values;
    values = HttpStatus0.values();

    return Stream.of(values).map(HttpStatus.class::cast).iterator();
  }

  @Test(dataProvider = "respondStatusProvider")
  public void respondStatus(HttpStatus status) {
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.bufferSize = 256;

          opts.socket = SocketY.of("""
          GET /1 HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """);

          opts.handler = http -> {
            http.status(status);

            http.header(HttpHeaderName.DATE, http.now());

            http.header(HttpHeaderName.CONNECTION, "close");

            http.send();
          };
        }),

        """
        HTTP/1.1 %d %s\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Connection: close\r
        \r
        """.formatted(status.code(), status.reasonPhrase())
    );
  }

  public record HeaderValueData(String value, String expected, String description) {}

  @DataProvider
  public Iterator<HeaderValueData> respondHeaderValidProvider() {
    final List<HeaderValueData> list;
    list = new ArrayList<>();

    list.add(new HeaderValueData("", "ETag:", "Empty string is valid"));

    final String validChars;
    validChars = Http.vchar();

    for (int idx = 0, len = validChars.length(); idx < len; idx++) {
      final char c;
      c = validChars.charAt(idx);

      final String s;
      s = Character.toString(c);

      list.add(new HeaderValueData(s, "ETag: " + s, "Character: " + c));
    }

    list.add(new HeaderValueData("x y", "ETag: x y", "SPACE allowed if not at the beginning/end"));
    list.add(new HeaderValueData("x\ty", "ETag: x\ty", "TAB allowed if not at the beginning/end"));

    return list.iterator();
  }

  @Test(description = "http.header(HeaderName, String) w/ a valid string", dataProvider = "respondHeaderValidProvider")
  public void respondHeaderValid(HeaderValueData data) {
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = SocketY.of("""
          GET /1 HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """);

          opts.handler = http -> {
            http.status(HttpStatus.OK);

            http.header(HttpHeaderName.DATE, http.now());

            http.header(HttpHeaderName.ETAG, data.value);

            http.send();
          };
        }),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        %s\r
        \r
        """.formatted(data.expected)
    );
  }

  @DataProvider
  public Iterator<HeaderValueData> respondHeaderInvalidProvider() {
    final List<HeaderValueData> list;
    list = new ArrayList<>();

    final boolean[] valid;
    valid = new boolean[256];

    final String validChars;
    validChars = Http.vchar();

    for (int idx = 0, len = validChars.length(); idx < len; idx++) {
      final char validChar;
      validChar = validChars.charAt(idx);

      valid[validChar] = true;
    }

    // we'll handle SPACE and HTAB later
    valid[' '] = true;
    valid['\t'] = true;

    for (int c = 0; c < 0xFF; c++) {
      if (!valid[c]) {
        final String value;
        value = Character.toString(c);

        final String expected;
        expected = "Invalid character at index 0: " + value;

        final String description;
        description = "Invalid character " + value;

        final HeaderValueData data;
        data = new HeaderValueData(value, expected, description);

        list.add(data);
      }
    }

    list.add(new HeaderValueData(" abc", "Leading SPACE or HTAB characters are not allowed", ""));
    list.add(new HeaderValueData("\tabc", "Leading SPACE or HTAB characters are not allowed", ""));
    list.add(new HeaderValueData("abc ", "Trailing SPACE or HTAB characters are not allowed", ""));
    list.add(new HeaderValueData("abc\t", "Trailing SPACE or HTAB characters are not allowed", ""));

    return list.iterator();
  }

  @Test(description = "http.header(HeaderName, String) w/ an invalid string", dataProvider = "respondHeaderInvalidProvider")
  public void respondHeaderInvalid(HeaderValueData data) {
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = SocketY.of("""
          GET /1 HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """);

          opts.handler = http -> {
            http.status(HttpStatus.OK);

            http.header(HttpHeaderName.DATE, http.now());

            try {
              http.header(HttpHeaderName.ETAG, data.value);

              Assert.fail();
            } catch (IllegalArgumentException expected) {
              final String message;
              message = expected.getMessage();

              assertEquals(message, data.expected);

              http.send();
            }
          };
        }),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        \r
        """
    );
  }

  @DataProvider
  public Object[][] headerValueBuilderValidProvider() {
    return new Object[][] {
        {
            builder(b -> {
              b.value("inline");
            }),
            "Content-Disposition: inline"
        },
        {
            builder(b -> {
              b.value("attachment");
              b.param("filename", "document.pdf");
            }),
            "Content-Disposition: attachment; filename=document.pdf"
        },
        {
            builder(b -> {
              b.value("attachment");
              b.param("filename", "[foo].txt");
            }),
            "Content-Disposition: attachment; filename=\"[foo].txt\""
        },
        {
            builder(b -> {
              b.value("attachment");
              b.param("filename", "");
            }),
            "Content-Disposition: attachment; filename=\"\""
        },
        {
            builder(b -> {
              b.value("foo");
              b.value("bar");
            }),
            "Content-Disposition: foo, bar"
        },
        {
            builder(b -> {
              b.value("foo");
              b.param("q", "0.9");
              b.value("bar");
            }),
            "Content-Disposition: foo; q=0.9, bar"
        },
        {
            builder(b -> {
              b.value("attachment");
              b.param("filename*", StandardCharsets.UTF_8, "document.pdf");
            }),
            "Content-Disposition: attachment; filename*=UTF-8''document.pdf"
        },
        {
            builder(b -> {
              b.value("attachment");
              b.param("filename*", StandardCharsets.UTF_8, "ação.pdf");
            }),
            "Content-Disposition: attachment; filename*=UTF-8''a%C3%A7%C3%A3o.pdf"
        },
        {
            builder(b -> {
              b.value("attachment");
              b.param("filename*", StandardCharsets.UTF_8, "");
            }),
            "Content-Disposition: attachment; filename*=UTF-8''"
        },
        {
            builder(b -> {
              b.param("filename*", StandardCharsets.UTF_8, "");
            }),
            "Content-Disposition: ; filename*=UTF-8''"
        }
    };
  }

  private Consumer<HttpHeaderValueBuilder> builder(Consumer<HttpHeaderValueBuilder> builder) {
    return builder;
  }

  @Test(dataProvider = "headerValueBuilderValidProvider")
  public void headerValueBuilderValid(Consumer<? super HttpHeaderValueBuilder> builder, String expected) {
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = SocketY.of("""
          GET /1 HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """);

          opts.handler = http -> {
            http.status(HttpStatus.OK);

            http.header(HttpHeaderName.DATE, http.now());

            http.header(HttpHeaderName.CONTENT_DISPOSITION, builder);

            http.send();
          };
        }),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        %s\r
        \r
        """.formatted(expected)
    );
  }

  @DataProvider
  public Object[][] headerValueBuilderInvalidProvider() {
    return new Object[][] {
        {
            builder(builder -> {
              builder.param("inva lid", "foo.txt");
            }),
            "Parameter name contains an invalid character at index 4: ' '"
        },
        {
            builder(builder -> {
              builder.param("[]", StandardCharsets.UTF_8, "foo.txt");
            }),
            "Parameter name contains an invalid character at index 0: '['"
        }
    };
  }

  @Test(dataProvider = "headerValueBuilderInvalidProvider")
  public void headerValueBuilderInvalid(Consumer<? super HttpHeaderValueBuilder> builder, String expectedMessage) {
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = SocketY.of("""
          GET /1 HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """);

          opts.handler = http -> {
            http.status(HttpStatus.OK);

            http.header(HttpHeaderName.DATE, http.now());

            try {
              http.header(HttpHeaderName.ETAG, builder);

              Assert.fail();
            } catch (RuntimeException runtime) {
              final String message;
              message = runtime.getMessage();

              assertEquals(message, expectedMessage);

              http.send();
            }
          };
        }),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        \r
        """
    );
  }

  @Test(description = "Empty response body: support Web.Resources")
  public void empty01() {
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = SocketY.of("""
          GET /1 HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """);

          opts.handler = http -> {
            http.status(HttpStatus.NOT_MODIFIED);

            http.header(HttpHeaderName.DATE, http.now());

            http.header(HttpHeaderName.ETAG, "some%hash");

            http.send();
          };
        }),

        """
        HTTP/1.1 304 Not Modified\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        ETag: some%hash\r
        \r
        """
    );
  }

  @Test(description = "file: GET")
  public void file01() {
    final String contents;
    contents = "x".repeat(1024);

    final Path file01;
    file01 = PathY.nextFile(contents, StandardCharsets.UTF_8);

    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = SocketY.of("""
          GET /1 HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """);

          opts.handler = http -> {
            http.status(HttpStatus.OK);

            http.header(HttpHeaderName.DATE, http.now());

            http.header(HttpHeaderName.CONTENT_TYPE, "text/plain; charset=utf-8");

            http.header(HttpHeaderName.CONTENT_LENGTH, 1024);

            http.send(file01);
          };
        }),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 1024\r
        \r
        %s\
        """.formatted(contents)
    );
  }

  @Test(description = "file: HEAD")
  public void file02() {
    final String contents;
    contents = "x".repeat(1024);

    final Path file02;
    file02 = PathY.nextFile(contents, StandardCharsets.UTF_8);

    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = SocketY.of("""
          HEAD /1 HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """);

          opts.handler = http -> {
            http.status(HttpStatus.OK);

            http.header(HttpHeaderName.DATE, http.now());

            http.header(HttpHeaderName.CONTENT_TYPE, "text/plain; charset=utf-8");

            http.header(HttpHeaderName.CONTENT_LENGTH, 1024);

            http.send(file02);
          };
        }),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 1024\r
        \r
        """
    );
  }

  @Test(description = "file: GET (chunked)")
  public void file03() {
    final String contents;
    contents = "x".repeat(64);

    final Path file01;
    file01 = PathY.nextFile(contents, StandardCharsets.UTF_8);

    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = SocketY.of("""
          GET /1 HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """);

          opts.handler = http -> {
            http.status(HttpStatus.OK);

            http.header(HttpHeaderName.DATE, http.now());

            http.header(HttpHeaderName.CONTENT_TYPE, "text/plain; charset=utf-8");

            http.header(HttpHeaderName.CONTENT_LENGTH, 64);

            http.header(HttpHeaderName.TRANSFER_ENCODING, "chunked");

            http.send(file01);
          };
        }),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 64\r
        Transfer-Encoding: chunked\r
        \r
        040\r
        xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx\r
        0\r
        \r
        """.formatted(contents)
    );
  }

  // ##################################################################
  // # BEGIN: IOException while writing
  // ##################################################################

  @Test(description = "throws on COMMIT")
  public void ioException01() {
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = SocketY.create(config -> {
            config.inputStream = InputStreamY.of("""
                GET /1 HTTP/1.1\r
                Host: www.example.com\r
                Connection: close\r
                \r
                """);

            config.outputStream = OutputStreamY.create(os -> {
              os.throwOnWrite = Throwables.trimStackTrace(new IOException(), 1);
            });
          });

          opts.handler = http -> http.ok(Media.Bytes.textPlain("OK"));
        }),

        ""
    );
  }

  @Test(description = "throws on headerUnchecked")
  public void ioException02() {
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = SocketY.create(config -> {
            config.inputStream = InputStreamY.of("""
                GET /1 HTTP/1.1\r
                Host: www.example.com\r
                Connection: close\r
                \r
                """);

            config.outputStream = OutputStreamY.create(os -> {
              os.throwOnWrite = Throwables.trimStackTrace(new IOException(), 1);
            });
          });

          final String veryLargeHex;
          veryLargeHex = "f756cd80".repeat(256);

          final String location;
          location = "/foo/" + veryLargeHex;

          opts.handler = http -> http.found(location);
        }),

        ""
    );
  }

  // ##################################################################
  // # END: IOException while writing
  // ##################################################################

  @Test(description = "Disallow response methods after response is sent")
  public void state01() {
    state(http -> http.status(HttpStatus.NOT_FOUND));
    state(http -> http.header(HttpHeaderName.REFERER, 123L));
    state(http -> http.header(HttpHeaderName.REFERER, "foo"));
    state(http -> http.send());
    state(http -> http.send(new byte[0]));
    state(http -> http.send(new byte[] {1, 2, 3}, 0, 1));
  }

  private void state(HttpHandler handler) {
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = SocketY.of("""
          GET /1 HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """);

          opts.handler = http -> {
            http.ok(Media.Bytes.textPlain("1"));

            try {
              handler.handle(http);

              Assert.fail("It should have thrown");
            } catch (IllegalStateException expected) {
              assertEquals(expected.getMessage(), "A response has already been written out");
            }
          };
        }),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 1\r
        \r
        1\
        """
    );
  }

}