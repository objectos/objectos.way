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
package objectox.http.srv;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;
import objectos.http.Content;
import objectos.http.ContentY;
import objectos.http.HeaderName;
import objectos.http.HeaderValueOptions;
import objectos.http.Status;
import objectos.http.MediaType;
import objectos.http.Redirection;
import objectos.http.Response;
import objectos.lang.Throwables;
import objectos.y.InputStreamY;
import objectos.y.OutputStreamY;
import objectos.y.PathY;
import objectos.y.SocketY;
import objectox.http.Rfc;
import objectox.http.resp.StatusEnum;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ServerTaskTest9ResultResponse {

  @Test
  public void create01() {
    assertEquals(
        ServerTaskY.resp(opts -> {
          opts.host("www.example.com", _ -> Response.create(resp -> {
            resp.status(Status.NOT_FOUND);

            resp.date();

            resp.header(HeaderName.CONTENT_LENGTH, 0L);
          }));

          opts.socket("""
          GET /1 HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """);
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
  public void sendContentBytes() {
    assertEquals(
        ServerTaskY.resp(opts -> {
          opts.host("www.example.com", _ -> Response.create(resp -> {
            resp.status(Status.OK);

            resp.date();

            resp.send(Content.of(MediaType.TEXT_PLAIN, "FOO\n"));
          }));

          opts.socket("""
          GET /1 HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """);
        }),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 4\r
        \r
        FOO
        """
    );
  }

  @Test
  public void sendContentBinaryObject() {
    assertEquals(
        ServerTaskY.resp(opts -> {
          opts.host("www.example.com", _ -> Response.create(resp -> {
            resp.status(Status.OK);

            resp.header(HeaderName.CONNECTION, "close");

            resp.date();

            resp.send(ContentY.chunked(MediaType.TEXT_PLAIN, 4));
          }));

          opts.socket("""
          GET /1 HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """);
        }),

        """
        HTTP/1.1 200 OK\r
        Connection: close\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Transfer-Encoding: chunked\r
        \r
        004\r
        1234\r
        0\r
        \r
        """
    );
  }

  @DataProvider
  public Iterator<Status> statusProvider() {
    final StatusEnum[] values;
    values = StatusEnum.values();

    return Stream.of(values).map(Status.class::cast).iterator();
  }

  @Test(dataProvider = "statusProvider")
  public void status(Status status) {
    assertEquals(
        ServerTaskY.resp(opts -> {
          opts.bufferSize = 256;

          opts.host("www.example.com", _ -> Response.create(http -> {
            http.status(status);

            http.date();

            http.header(HeaderName.CONNECTION, "close");
          }));

          opts.socket("""
          GET /1 HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """);
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
    validChars = Rfc.vchar();

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
        ServerTaskY.resp(opts -> {
          opts.host("www.example.com", _ -> Response.create(http -> {
            http.status(Status.OK);

            http.date();

            http.header(HeaderName.ETAG, data.value);
          }));

          opts.socket("""
          GET /1 HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """);
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
    validChars = Rfc.vchar();

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
        expected = "Invalid header value: character '%s' at index 0 is not allowed".formatted(value);

        final String description;
        description = "Invalid character " + value;

        final HeaderValueData data;
        data = new HeaderValueData(value, expected, description);

        list.add(data);
      }
    }

    list.add(new HeaderValueData(" abc", "Invalid header value: leading SPACE or HTAB characters are not allowed", ""));
    list.add(new HeaderValueData("\tabc", "Invalid header value: leading SPACE or HTAB characters are not allowed", ""));
    list.add(new HeaderValueData("abc ", "Invalid header value: trailing SPACE or HTAB characters are not allowed", ""));
    list.add(new HeaderValueData("abc\t", "Invalid header value: trailing SPACE or HTAB characters are not allowed", ""));

    return list.iterator();
  }

  @Test(description = "http.header(HeaderName, String) w/ an invalid string", dataProvider = "respondHeaderInvalidProvider")
  public void respondHeaderInvalid(HeaderValueData data) {
    assertEquals(
        ServerTaskY.resp(opts -> {
          opts.socket("""
          GET /1 HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """);

          opts.host("www.example.com", _ -> Response.create(http -> {
            http.status(Status.OK);

            http.date();

            try {
              http.header(HeaderName.ETAG, data.value);

              Assert.fail();
            } catch (IllegalArgumentException expected) {
              final String message;
              message = expected.getMessage();

              assertEquals(message, data.expected);
            }
          }));
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

  private Consumer<HeaderValueOptions> builder(Consumer<HeaderValueOptions> builder) {
    return builder;
  }

  @Test(dataProvider = "headerValueBuilderValidProvider")
  public void headerValueBuilderValid(Consumer<? super HeaderValueOptions> builder, String expected) {
    assertEquals(
        ServerTaskY.resp(opts -> {
          opts.host("www.example.com", _ -> Response.create(http -> {
            http.status(Status.OK);

            http.date();

            http.header(HeaderName.CONTENT_DISPOSITION, builder);
          }));

          opts.socket("""
          GET /1 HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """);
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
            "Invalid parameter name: character ' ' at index 4 is not allowed"
        },
        {
            builder(builder -> {
              builder.param("[]", StandardCharsets.UTF_8, "foo.txt");
            }),
            "Invalid parameter name: character '[' at index 0 is not allowed"
        }
    };
  }

  @Test(dataProvider = "headerValueBuilderInvalidProvider")
  public void headerValueBuilderInvalid(Consumer<? super HeaderValueOptions> builder, String expectedMessage) {
    assertEquals(
        ServerTaskY.resp(opts -> {
          opts.host("www.example.com", _ -> Response.create(http -> {
            http.status(Status.OK);

            http.date();

            try {
              http.header(HeaderName.ETAG, builder);

              Assert.fail();
            } catch (RuntimeException runtime) {
              final String message;
              message = runtime.getMessage();

              assertEquals(message, expectedMessage);
            }
          }));

          opts.socket("""
          GET /1 HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """);
        }),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
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
        ServerTaskY.resp(opts -> {
          opts.host("www.example.com", _ -> Response.create(http -> {
            http.status(Status.OK);

            http.date();

            http.header(HeaderName.CONTENT_TYPE, "text/plain; charset=utf-8");

            http.send(file01);
          }));

          opts.socket("""
          GET /1 HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """);
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
        ServerTaskY.resp(opts -> {
          opts.host("www.example.com", _ -> Response.create(http -> {
            http.status(Status.OK);

            http.date();

            http.header(HeaderName.CONTENT_TYPE, "text/plain; charset=utf-8");

            http.send(file02);
          }));

          opts.socket("""
          HEAD /1 HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """);
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

  @Test(description = "throws on COMMIT")
  public void ioException01() {
    assertEquals(
        ServerTaskY.resp(opts -> {
          opts.host("www.example.com", _ -> Content.of(MediaType.TEXT_PLAIN, "OK"));

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
        }),

        ""
    );
  }

  @Test(description = "throws on headerUnchecked")
  public void ioException02() {
    assertEquals(
        ServerTaskY.resp(opts -> {
          final String veryLargeHex;
          veryLargeHex = "f756cd80".repeat(256);

          final String location;
          location = "/foo/" + veryLargeHex;

          opts.host("www.example.com", _ -> Redirection.found(location));

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
        }),

        ""
    );
  }

}