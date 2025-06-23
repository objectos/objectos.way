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

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HttpExchangeTest9Response extends HttpExchangeTest {

  // 2xx responses

  @Test(description = "ok(Media.Bytes): fits in buffer")
  public void okMediaBytes01() {
    get(
        http -> http.ok(Media.Bytes.textPlain("1\n")),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 2\r
        \r
        1
        """
    );
  }

  @Test(description = "ok(Media.Bytes): does not fit in buffer")
  public void okMediaBytes02() {
    final String veryLong;
    veryLong = ".".repeat(2048);

    get(
        http -> http.ok(Media.Bytes.textPlain(veryLong)),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 2048\r
        \r
        %s\
        """.formatted(veryLong)
    );
  }

  @Test(description = "ok(Media.Bytes): HEAD")
  public void okMediaBytes03() {
    final String veryLong;
    veryLong = ".".repeat(2048);

    head(
        http -> http.ok(Media.Bytes.textPlain(veryLong)),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 2048\r
        \r
        """
    );
  }

  // 125 bytes
  private static final int TEXT_RESP_LEN = """
  HTTP/1.1 200 OK\r
  Date: Wed, 28 Jun 2023 12:08:43 GMT\r
  Content-Type: text/plain; charset=utf-8\r
  Transfer-Encoding: chunked\r
  \r
  """.length();

  @SuppressWarnings("exports")
  @DataProvider
  public Iterator<Y.MediaKind> mediaKindProvider() {
    return EnumSet.allOf(Y.MediaKind.class).iterator();
  }

  @SuppressWarnings("exports")
  @Test(description = "ok(Media.Stream/Text): 1 chunk", dataProvider = "mediaKindProvider")
  public void okMediaStreamText01(Y.MediaKind kind) {
    final Media media;
    media = Y.mediaOf(kind, 64);

    assertEquals(
        media.toString(),

        """
        .................................................
        12345678901234\
        """
    );

    get(
        256, 256,

        ok(media),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Transfer-Encoding: chunked\r
        \r
        40\r
        .................................................
        12345678901234\r
        0\r
        \r
        """
    );
  }

  @SuppressWarnings("exports")
  @Test(description = "ok(Media.Stream/Text): 1 chunk + zero-pad", dataProvider = "mediaKindProvider")
  public void okMediaStreamText02(Y.MediaKind kind) {
    final Media media;
    media = Y.mediaOf(kind, 15);

    assertEquals(
        media.toString(),

        """
        123456789012345\
        """
    );

    get(
        256, 256,

        ok(media),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Transfer-Encoding: chunked\r
        \r
        0F\r
        123456789012345\r
        0\r
        \r
        """
    );
  }

  @SuppressWarnings("exports")
  @Test(description = "ok(Media.Stream/Text): 1 chunk, headers + body fit in buffer exactly", dataProvider = "mediaKindProvider")
  public void okMediaStreamText03(Y.MediaKind kind) {
    // 4 = chunk-size + CR + LF
    // 2 = CR + LF (after data)
    // 5 = 0 + CR + LF + CR + LF
    final Media media;
    media = Y.mediaOf(kind, 256 - TEXT_RESP_LEN - 4 - 2 - 5);

    final String body;
    body = media.toString();

    assertEquals(
        body,

        """
        .................................................
        .................................................
        12345678901234567890\
        """
    );

    assertEquals(Integer.toHexString(body.length()), "78");

    final String expectedResponse;
    expectedResponse = """
    HTTP/1.1 200 OK\r
    Date: Wed, 28 Jun 2023 12:08:43 GMT\r
    Content-Type: text/plain; charset=utf-8\r
    Transfer-Encoding: chunked\r
    \r
    78\r
    .................................................
    .................................................
    12345678901234567890\r
    0\r
    \r
    """;

    get(
        256, 256,

        ok(media),

        expectedResponse
    );

    assertEquals(expectedResponse.length(), 256);
  }

  @SuppressWarnings("exports")
  @Test(description = "ok(Media.Stream/Text): 2 chunks", dataProvider = "mediaKindProvider")
  public void okMediaStreamText04(Y.MediaKind kind) {
    int mediaLength;
    mediaLength = 0;

    // buffer length
    mediaLength += 256;
    // unencoded data + headers fit in buffer
    mediaLength -= TEXT_RESP_LEN;
    // 4 = chunk-size + CR + LF
    mediaLength -= 4;
    // 2 = CR + LF (after data)
    mediaLength -= 2;
    // 5 = 0 + CR + LF + CR + LF
    mediaLength -= 5;
    // 1byte + trailer will cause a flush
    mediaLength += 1 + 5;

    final Media media;
    media = Y.mediaOf(kind, mediaLength);

    final String expectedResponse;
    expectedResponse = """
    HTTP/1.1 200 OK\r
    Date: Wed, 28 Jun 2023 12:08:43 GMT\r
    Content-Type: text/plain; charset=utf-8\r
    Transfer-Encoding: chunked\r
    \r
    7D\r
    .................................................
    .................................................
    1234567890123456789012345\r
    01\r
    6\r
    0\r
    \r
    """;

    get(
        256, 256,

        ok(media),

        expectedResponse
    );

    final String body;
    body = media.toString();

    assertEquals(
        body,

        """
        .................................................
        .................................................
        12345678901234567890123456\
        """
    );

    assertEquals(Integer.toHexString(body.length()), "7e");
  }

  @SuppressWarnings("exports")
  @Test(description = "ok(Media.Stream/Text): response headers fill up the buffer", dataProvider = "mediaKindProvider")
  public void okMediaStreamText05(Y.MediaKind kind) {
    final String contentType;
    contentType = contentTypeToFillResponseHeaders(256);

    final Media media;
    media = Y.mediaOf(kind, 240, contentType);

    final String expectedResponse;
    expectedResponse = """
    HTTP/1.1 200 OK\r
    Date: Wed, 28 Jun 2023 12:08:43 GMT\r
    Content-Type: XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX\r
    Transfer-Encoding: chunked\r
    \r
    F0\r
    .................................................
    .................................................
    .................................................
    .................................................
    1234567890123456789012345678901234567890\r
    0\r
    \r
    """;

    get(
        256, 256,

        ok(media),

        expectedResponse
    );

    final String body;
    body = media.toString();

    assertEquals(
        body,

        """
        .................................................
        .................................................
        .................................................
        .................................................
        1234567890123456789012345678901234567890\
        """
    );

    assertEquals(Integer.toHexString(body.length()), "f0");
  }

  @SuppressWarnings("exports")
  @Test(description = "ok(Media.Stream/Text): trailer chunk does not fit in buffer", dataProvider = "mediaKindProvider")
  public void okMediaStreamText06(Y.MediaKind kind) {
    int mediaLength;
    mediaLength = 0;

    // buffer length
    mediaLength += 256;
    // unencoded data + headers fit in buffer
    mediaLength -= TEXT_RESP_LEN;
    // 4 = chunk-size + CR + LF
    mediaLength -= 4;
    // 2 = CR + LF (after data)
    mediaLength -= 2;
    // 5 = 0 + CR + LF + CR + LF
    mediaLength -= 5;
    // 1 extra byte so trailer won't fit in buffer
    mediaLength += 1;

    final Media media;
    media = Y.mediaOf(kind, mediaLength);

    final String expectedResponse;
    expectedResponse = """
    HTTP/1.1 200 OK\r
    Date: Wed, 28 Jun 2023 12:08:43 GMT\r
    Content-Type: text/plain; charset=utf-8\r
    Transfer-Encoding: chunked\r
    \r
    79\r
    .................................................
    .................................................
    123456789012345678901\r
    0\r
    \r
    """;

    get(
        256, 256,

        ok(media),

        expectedResponse
    );

    final String body;
    body = media.toString();

    assertEquals(
        body,

        """
        .................................................
        .................................................
        123456789012345678901\
        """
    );

    assertEquals(Integer.toHexString(body.length()), "79");
  }

  private String contentTypeToFillResponseHeaders(int buffer) {
    final String blankContentType;
    blankContentType = """
    HTTP/1.1 200 OK\r
    Date: Wed, 28 Jun 2023 12:08:43 GMT\r
    Content-Type: \r
    Transfer-Encoding: chunked\r
    \r
    """;

    final int contentTypeLength;
    contentTypeLength = buffer - blankContentType.length();

    if (contentTypeLength <= 0) {
      throw new IllegalArgumentException("Buffer is too small: buffer=" + buffer);
    }

    return "X".repeat(contentTypeLength);
  }

  private Consumer<HttpExchange> ok(Media media) {
    if (media instanceof Media.Stream stream) {
      return http -> http.ok(stream);
    } else if (media instanceof Media.Text text) {
      return http -> http.ok(text);
    } else {
      throw new UnsupportedOperationException();
    }
  }

  // 3xx responses

  @Test
  public void movedPermanently01() {
    get(
        http -> http.movedPermanently("/login"),

        """
        HTTP/1.1 301 Moved Permanently\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Length: 0\r
        Location: /login\r
        \r
        """
    );
  }

  @Test
  public void movedPermanently02() {
    get(
        http -> http.movedPermanently("/product/café/😀"),

        """
        HTTP/1.1 301 Moved Permanently\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Length: 0\r
        Location: /product/caf%C3%A9/%F0%9F%98%80\r
        \r
        """
    );
  }

  @Test
  public void movedPermanently03() {
    final String veryLargeHex;
    veryLargeHex = "f756cd80".repeat(256);

    final String location;
    location = "/foo/" + veryLargeHex;

    get(
        http -> http.movedPermanently(location),

        """
        HTTP/1.1 301 Moved Permanently\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Length: 0\r
        Location: %s\r
        \r
        """.formatted(location)
    );
  }

  @Test
  public void found01() {
    get(
        http -> http.found("/login"),

        """
        HTTP/1.1 302 Found\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Length: 0\r
        Location: /login\r
        \r
        """
    );
  }

  @Test
  public void found02() {
    get(
        http -> http.found("/product/café/😀"),

        """
        HTTP/1.1 302 Found\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Length: 0\r
        Location: /product/caf%C3%A9/%F0%9F%98%80\r
        \r
        """
    );
  }

  @Test
  public void found03() {
    final String veryLargeHex;
    veryLargeHex = "f756cd80".repeat(256);

    final String location;
    location = "/foo/" + veryLargeHex;

    get(
        http -> http.found(location),

        """
        HTTP/1.1 302 Found\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Length: 0\r
        Location: %s\r
        \r
        """.formatted(location)
    );
  }

  @Test
  public void seeOther01() {
    get(
        http -> http.seeOther("/page"),

        """
        HTTP/1.1 303 See Other\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Length: 0\r
        Location: /page\r
        \r
        """
    );
  }

  @Test
  public void seeOther02() {
    get(
        http -> http.seeOther("/product/café/😀"),

        """
        HTTP/1.1 303 See Other\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Length: 0\r
        Location: /product/caf%C3%A9/%F0%9F%98%80\r
        \r
        """
    );
  }

  @Test
  public void seeOther03() {
    final String veryLargeHex;
    veryLargeHex = "f756cd80".repeat(256);

    final String location;
    location = "/foo/" + veryLargeHex;

    get(
        http -> http.seeOther(location),

        """
        HTTP/1.1 303 See Other\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Length: 0\r
        Location: %s\r
        \r
        """.formatted(location)
    );
  }

  // 4xx responses

  @Test(description = "badRequest(Media.Bytes)")
  public void badRequest01() {
    post(
        http -> http.badRequest(Media.Bytes.textPlain("BAD\n")),

        """
        HTTP/1.1 400 Bad Request\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 4\r
        \r
        BAD
        """
    );
  }

  @Test(description = "forbidden(Media.Bytes)")
  public void forbidden01() {
    post(
        http -> http.forbidden(Media.Bytes.textPlain("403\n")),

        """
        HTTP/1.1 403 Forbidden\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 4\r
        \r
        403
        """
    );
  }

  @Test(description = "notFound(Media.Bytes)")
  public void notFound01() {
    post(
        http -> http.notFound(Media.Bytes.textPlain("NOT\n")),

        """
        HTTP/1.1 404 Not Found\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 4\r
        \r
        NOT
        """
    );
  }

  @Test(description = "allow(Http.Method...)")
  public void allow01() {
    post(
        http -> http.allow(Http.Method.GET, Http.Method.HEAD),

        """
        HTTP/1.1 405 Method Not Allowed\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Allow: GET, HEAD\r
        Content-Length: 0\r
        \r
        """
    );
  }

  // 5xx responses

  @Test(description = "internalServerError(Media.Bytes, Throwable)")
  public void internalServerError01() {
    post(
        http -> http.internalServerError(Media.Bytes.textPlain("ISE\n"), Y.trimStackTrace(new IOException(), 1)),

        """
        HTTP/1.1 500 Internal Server Error\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 4\r
        \r
        ISE
        """
    );
  }

  // response builder

  @Test
  public void respond01() {
    get(
        http -> http.respond(resp -> {
          resp.status(Http.Status.NOT_FOUND);

          resp.header(Http.HeaderName.DATE, resp.now());

          resp.header(Http.HeaderName.CONTENT_LENGTH, 0L);

          resp.body();
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

    get(
        http -> http.respond(resp -> {
          resp.status(Http.Status.OK);

          resp.header(Http.HeaderName.DATE, resp.now());

          resp.header(Http.HeaderName.CONTENT_TYPE, "text/plain");

          resp.header(Http.HeaderName.CONTENT_LENGTH, body.length);

          resp.body(body, 0, body.length);
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
    final Media media;
    media = Media.Bytes.textPlain("FOO\n");

    get(
        http -> http.respond(resp -> {
          resp.status(Http.Status.OK);

          resp.header(Http.HeaderName.CONNECTION, "close");

          resp.media(media);
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

    get(
        http -> http.respond(resp -> {
          resp.status(Http.Status.OK);

          resp.header(Http.HeaderName.CONNECTION, "close");

          resp.media(media);
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
  public Iterator<Http.Status> respondStatusProvider() {
    final HttpStatus[] values = HttpStatus.values();

    return Stream.of(values).map(Http.Status.class::cast).iterator();
  }

  @Test(dataProvider = "respondStatusProvider")
  public void respondStatus(Http.Status status) {
    exec(test -> {
      test.xch(xch -> {
        xch.req("""
        GET / HTTP/1.1\r
        Host: Host\r
        \r
        """);

        xch.handler(http -> http.respond(resp -> {
          resp.status(status);

          resp.media(OK);
        }));

        xch.resp(
            """
            HTTP/1.1 %d %s\r
            Date: Wed, 28 Jun 2023 12:08:43 GMT\r
            Content-Type: text/plain; charset=utf-8\r
            Content-Length: 3\r
            \r
            OK
            """.formatted(status.code(), status.reasonPhrase())
        );
      });
    });
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

  @Test(description = "resp.header(HeaderName, String) w/ a valid string", dataProvider = "respondHeaderValidProvider")
  public void respondHeaderValid(HeaderValueData data) {
    exec(test -> {
      test.bufferSize(256, 512);

      test.xch(xch -> {
        xch.req("""
        GET / HTTP/1.1\r
        Host: Host\r
        \r
        """);

        xch.handler(http -> http.respond(resp -> {
          resp.status(Http.Status.OK);

          resp.header(Http.HeaderName.ETAG, data.value);

          resp.media(OK);
        }));

        xch.resp(
            """
            HTTP/1.1 200 OK\r
            %s\r
            Date: Wed, 28 Jun 2023 12:08:43 GMT\r
            Content-Type: text/plain; charset=utf-8\r
            Content-Length: 3\r
            \r
            OK
            """.formatted(data.expected)
        );
      });
    });
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

  @Test(description = "resp.header(HeaderName, String) w/ an invalid string", dataProvider = "respondHeaderInvalidProvider")
  public void respondHeaderInvalid(HeaderValueData data) {
    exec(test -> {
      test.bufferSize(256, 512);

      test.xch(xch -> {
        xch.req("""
        GET / HTTP/1.1\r
        Host: Host\r
        \r
        """);

        xch.handler(http -> http.respond(resp -> {
          resp.status(Http.Status.OK);

          try {
            resp.header(Http.HeaderName.ETAG, data.value);

            Assert.fail();
          } catch (IllegalArgumentException expected) {
            final String message;
            message = expected.getMessage();

            assertEquals(message, data.expected);

            resp.media(OK);
          }
        }));

        xch.resp(OK_RESP);
      });
    });
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

  private Consumer<Http.HeaderValueBuilder> builder(Consumer<Http.HeaderValueBuilder> builder) {
    return builder;
  }

  @Test(dataProvider = "headerValueBuilderValidProvider")
  public void headerValueBuilderValid(Consumer<? super Http.HeaderValueBuilder> builder, String expected) {
    get(
        http -> http.respond(resp -> {
          resp.status(Http.Status.OK);
          resp.header(Http.HeaderName.CONTENT_DISPOSITION, builder);
          resp.media(OK);
        }),

        """
        HTTP/1.1 200 OK\r
        %s\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 3\r
        \r
        OK
        """.formatted(expected)
    );
  }

  @DataProvider
  public Object[][] headerValueBuilderInvalidProvider() {
    return new Object[][] {
        {builder(builder -> {
          builder.param("inva lid", "foo.txt");
        }), "Parameter name contains an invalid character at index 4: ' '"},

        {builder(builder -> {
          builder.param("[]", StandardCharsets.UTF_8, "foo.txt");
        }), "Parameter name contains an invalid character at index 0: '['"}
    };
  }

  @Test(dataProvider = "headerValueBuilderInvalidProvider")
  public void headerValueBuilderInvalid(Consumer<? super Http.HeaderValueBuilder> builder, String expectedMessage) {
    exec(test -> {
      test.bufferSize(256, 512);

      test.xch(xch -> {
        xch.req("""
        GET / HTTP/1.1\r
        Host: Host\r
        \r
        """);

        xch.handler(http -> http.respond(resp -> {
          resp.status(Http.Status.OK);

          try {
            resp.header(Http.HeaderName.ETAG, builder);

            Assert.fail();
          } catch (RuntimeException runtime) {
            final String message;
            message = runtime.getMessage();

            assertEquals(message, expectedMessage);

            resp.media(OK);
          }
        }));

        xch.resp(OK_RESP);
      });
    });
  }

  private void empty01(HttpExchange http) {
    http.status(Http.Status.NOT_MODIFIED);
    http.header(Http.HeaderName.DATE, http.now());
    http.header(Http.HeaderName.ETAG, "some%hash");
    http.send();
  }

  @Test(description = "Empty response body: support Web.Resources")
  public void empty01() {
    test(
        """
        GET /atom.xml HTTP/1.1\r
        Host: www.example.com\r
        If-None-Match: some%hash\r
        Connection: close\r
        \r
        """,

        this::empty01,

        """
        HTTP/1.1 304 Not Modified\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        ETag: some%hash\r
        \r
        """
    );
  }

  private Path file01;

  private void file01(HttpExchange http) {
    http.status(Http.Status.OK);
    http.header(Http.HeaderName.DATE, http.now());
    http.header(Http.HeaderName.CONTENT_TYPE, "text/plain; charset=utf-8");
    http.header(Http.HeaderName.CONTENT_LENGTH, 1024);
    http.send(file01);
  }

  @Test(description = "file: GET")
  public void file01() {
    final String contents;
    contents = "x".repeat(1024);

    file01 = Y.nextTempFile(contents, StandardCharsets.UTF_8);

    get(
        this::file01,

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

  private Path file02;

  private void file02(HttpExchange http) {
    http.status(Http.Status.OK);
    http.header(Http.HeaderName.DATE, http.now());
    http.header(Http.HeaderName.CONTENT_TYPE, "text/plain; charset=utf-8");
    http.header(Http.HeaderName.CONTENT_LENGTH, 1024);
    http.send(file02);
  }

  @Test(description = "file: HEAD")
  public void file02() {
    final String contents;
    contents = "x".repeat(1024);

    file02 = Y.nextTempFile(contents, StandardCharsets.UTF_8);

    head(
        this::file02,

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 1024\r
        \r
        """
    );
  }

  // ##################################################################
  // # BEGIN: IOException while writing
  // ##################################################################

  @Test(description = "throws on COMMIT")
  public void ioException01() {
    exec(test -> {
      test.xch(xch -> {
        xch.req("""
        GET / HTTP/1.1\r
        Host: test\r
        \r
        """);

        xch.handler(http -> http.ok(Media.Bytes.textPlain("OK")));

        xch.resp(new IOException(), 1);
      });
    });
  }

  @Test(description = "throws on headerUnchecked")
  public void ioException02() {
    exec(test -> {
      test.xch(xch -> {
        xch.req("""
        GET / HTTP/1.1\r
        Host: test\r
        \r
        """);

        final String veryLargeHex;
        veryLargeHex = "f756cd80".repeat(256);

        final String location;
        location = "/foo/" + veryLargeHex;

        xch.handler(http -> http.found(location));

        xch.resp(new IOException(), 1);
      });
    });
  }

  // ##################################################################
  // # END: IOException while writing
  // ##################################################################

  @Test(description = "Disallow request methods after response is sent")
  public void state01() {
    state(Http.Exchange::method);
    state(Http.Exchange::path);
    state(http -> http.queryParam("x"));
    state(http -> http.queryParamAll("x"));
    state(http -> http.queryParamNames());
    state(Http.Exchange::version);

    state(http -> http.header(Http.HeaderName.ACCEPT_ENCODING));

    state(Http.Exchange::bodyInputStream);
  }

  private void get(Consumer<HttpExchange> handler, String expectedResponse) {
    test("""
    GET /test HTTP/1.1\r
    Host: www.objectos.com.br\r
    Connection: close\r
    \r
    """, handler, expectedResponse);
  }

  private void get(int initial, int max, Consumer<HttpExchange> handler, String expectedResponse) {
    test(
        initial, max,

        """
        GET /test HTTP/1.1\r
        Host: www.objectos.com.br\r
        Connection: close\r
        \r
        """,

        handler, expectedResponse
    );
  }

  private void head(Consumer<HttpExchange> handler, String expectedResponse) {
    test("""
    HEAD /test HTTP/1.1\r
    Host: www.objectos.com.br\r
    Connection: close\r
    \r
    """, handler, expectedResponse);
  }

  private void post(Consumer<HttpExchange> handler, String expectedResponse) {
    test("""
    POST /test HTTP/1.1\r
    Host: www.objectos.com.br\r
    Content-Length: 24\r
    Content-Type: application/x-www-form-urlencoded\r
    Connection: close\r
    \r
    email=user%40example.com\
    """, handler, expectedResponse);
  }

  @FunctionalInterface
  private interface ThrowingConsumer {
    void accept(HttpExchange http) throws IOException;
  }

  private void state(ThrowingConsumer handler) {
    final Socket socket;
    socket = Y.socket("""
    GET /test HTTP/1.1\r
    Host: www.objectos.com.br\r
    Connection: close\r
    \r
    """);

    try (HttpExchange http = new HttpExchange(socket, 256, 512, Y.clockFixed(), Y.noteSink(), 0L)) {
      assertEquals(http.shouldHandle(), true);

      http.ok(Media.Bytes.textPlain("OK"));

      handler.accept(http);

      Assert.fail("It should have thrown");
    } catch (IllegalStateException expected) {
      final String message;
      message = expected.getMessage();

      assertEquals(message, """
      This request method can only be invoked:
      - after a successful shouldHandle() operation; and
      - before a response has been sent.
      """);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private void test(String request, Consumer<HttpExchange> handler, String expectedResponse) {
    test(256, 512, request, handler, expectedResponse);
  }

  private void test(int initial, int max, String request, Consumer<HttpExchange> handler, String expectedResponse) {
    final Socket socket;
    socket = Y.socket(request);

    try (HttpExchange http = new HttpExchange(socket, initial, max, Y.clockFixed(), Y.noteSink(), 1024)) {
      assertEquals(http.shouldHandle(), true);

      handler.accept(http);

      assertEquals(http.shouldHandle(), false);

      assertEquals(Y.toString(socket), expectedResponse);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

}