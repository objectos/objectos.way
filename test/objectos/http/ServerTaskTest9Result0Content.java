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

import org.testng.annotations.Test;

public class ServerTaskTest9Result0Content {

  @Test
  public void fixedLength01() {
    assertEquals(
        ServerTaskY.resp(opts -> {
          opts.host("www.example.com", _ -> Content.of(MediaType.TEXT_PLAIN, "1"));

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
        Content-Length: 1\r
        \r
        1\
        """
    );
  }

  @Test(description = "does not fit in buffer")
  public void fixedLength02() {
    final String veryLong;
    veryLong = ".".repeat(2048);

    assertEquals(
        ServerTaskY.resp(opts -> {
          opts.host("www.example.com", _ -> Content.of(MediaType.TEXT_PLAIN, veryLong));

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
        Content-Length: 2048\r
        \r
        %s\
        """.formatted(veryLong)
    );
  }

  @Test(description = "HEAD")
  public void fixedLength03() {
    final String veryLong;
    veryLong = ".".repeat(2048);

    assertEquals(
        ServerTaskY.resp(opts -> {
          opts.host("www.example.com", _ -> Content.of(MediaType.TEXT_PLAIN, veryLong));

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
        Content-Length: 2048\r
        \r
        """
    );
  }

  @Test(description = "1 chunk")
  public void chunked01() {
    final Content media;
    media = ContentY.chunked(MediaType.TEXT_PLAIN, 64);

    assertEquals(
        ServerTaskY.resp(opts -> {
          opts.bufferSize = 256;

          opts.host("www.example.com", _ -> media);

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

  @Test(description = "1 chunk + zero-pad")
  public void chunked02() {
    final Content media;
    media = ContentY.chunked(MediaType.TEXT_PLAIN, 15);

    assertEquals(
        ServerTaskY.resp(opts -> {
          opts.bufferSize = 256;

          opts.host("www.example.com", _ -> media);

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
        Transfer-Encoding: chunked\r
        \r
        0F\r
        123456789012345\r
        0\r
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

  @Test(description = "1 chunk, headers + body fit in buffer exactly")
  public void chunked03() {
    // 4 = chunk-size + CR + LF
    // 2 = CR + LF (after data)
    // 5 = 0 + CR + LF + CR + LF
    final Content media;
    media = ContentY.chunked(MediaType.TEXT_PLAIN, 256 - TEXT_RESP_LEN - 4 - 2 - 5);

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

    assertEquals(expectedResponse.length(), 256);

    assertEquals(
        ServerTaskY.resp(opts -> {
          opts.bufferSize = 256;

          opts.host("www.example.com", _ -> media);

          opts.socket("""
          GET /1 HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """);
        }),

        expectedResponse
    );
  }

  @Test(description = "2 chunks")
  public void chunked04() {
    int mediaLength;
    mediaLength = 0;

    // buffer length
    mediaLength += 256;
    // 4 = chunk-size + CR + LF
    mediaLength -= 4;
    // 2 = CR + LF (after data)
    mediaLength -= 2;
    // 5 = 0 + CR + LF + CR + LF
    mediaLength -= 5;
    // 1byte + trailer will cause a flush
    mediaLength += 1 + 5;

    final Content media;
    media = ContentY.chunked(MediaType.TEXT_PLAIN, mediaLength);

    final String expectedResponse;
    expectedResponse = """
    HTTP/1.1 200 OK\r
    Date: Wed, 28 Jun 2023 12:08:43 GMT\r
    Content-Type: text/plain; charset=utf-8\r
    Transfer-Encoding: chunked\r
    \r
    FA\r
    .................................................
    .................................................
    .................................................
    .................................................
    .................................................
    \r
    01\r
    1\r
    0\r
    \r
    """;

    assertEquals(
        ServerTaskY.resp(opts -> {
          opts.bufferSize = 256;

          opts.host("www.example.com", _ -> media);

          opts.socket("""
          GET /1 HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """);
        }),

        expectedResponse
    );
  }

  @Test(description = "response headers fill up the buffer")
  public void chunked05() {
    final String contentType;
    contentType = contentTypeToFillResponseHeaders(256);

    final MediaType type;
    type = new MediaTypePojo(contentType);

    final Content media;
    media = ContentY.chunked(type, 240);

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

    assertEquals(
        ServerTaskY.resp(opts -> {
          opts.bufferSize = 256;

          opts.host("www.example.com", _ -> media);

          opts.socket("""
          GET /1 HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """);
        }),

        expectedResponse
    );
  }

  @Test(description = "trailer chunk does not fit in buffer")
  public void chunked06() {
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

    final Content media;
    media = ContentY.chunked(MediaType.TEXT_PLAIN, mediaLength);

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

    assertEquals(
        ServerTaskY.resp(opts -> {
          opts.bufferSize = 256;

          opts.host("www.example.com", _ -> media);

          opts.socket("""
          GET /1 HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """);
        }),

        expectedResponse
    );
  }

  @Test
  public void provider01() {
    final ContentProvider entity;
    entity = () -> Content.of(MediaType.TEXT_PLAIN, "foo");

    assertEquals(
        ServerTaskY.resp(opts -> {
          opts.bufferSize = 256;

          opts.host("www.example.com", _ -> entity);

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
        Content-Length: 3\r
        \r
        foo\
        """
    );
  }

  @Test
  public void provider02() {
    final ContentProvider entity;
    entity = () -> Content.of(MediaType.TEXT_PLAIN, out -> out.write("foo".getBytes()));

    assertEquals(
        ServerTaskY.resp(opts -> {
          opts.bufferSize = 256;

          opts.host("www.example.com", _ -> entity);

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
        Transfer-Encoding: chunked\r
        \r
        03\r
        foo\r
        0\r
        \r
        """
    );
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

}