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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import objectos.http.HttpRequestParser.InvalidRequestLine;
import objectos.way.Y;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@SuppressWarnings("exports")
public class HttpRequestTest1ParsePath {

  private static final boolean[] VALID_BYTES;

  static {
    final boolean[] valid;
    valid = new boolean[256];

    final String validString;
    validString = Http.unreserved() + Http.subDelims() + ":@";

    for (int idx = 0, len = validString.length(); idx < len; idx++) {
      final char c;
      c = validString.charAt(idx);

      valid[c] = true;
    }

    valid['/'] = true;

    VALID_BYTES = valid;
  }

  @DataProvider
  public Object[][] pathValidProvider() {
    final List<Object[]> l;
    l = new ArrayList<>();

    l.add(valid("/", "/", "root"));
    l.add(valid("/index.html", "/index.html", "with segment"));

    for (int value = 0; value < VALID_BYTES.length; value++) {
      switch (value) {
        case '\n', '\r' -> {/* will trigger 505 not 400 */}

        case '?' -> {/* valid, but tested on parse query */}

        case '%' -> {/* tested on percent-encoded */}

        default -> {
          if (VALID_BYTES[value]) {
            l.add(valid(value));
          }
        }
      }
    }

    return l.toArray(Object[][]::new);
  }

  private Object[] valid(int value) {
    final String raw;
    raw = "/path" + (char) value;

    final String path;
    path = raw;

    final String description;
    description = "path contains the " + Integer.toHexString(value) + " valid byte";

    return valid(raw, path, description);
  }

  private Object[] valid(String raw, String path, String description) {
    return new Object[] {raw, path, description};
  }

  @Test(dataProvider = "pathValidProvider")
  public void pathValid(String raw, String path, String description) throws IOException {
    final HttpRequest req;
    req = HttpRequestParserY.parse(
        test -> test.bufferSize(256, 512),

        iso8859("""
        GET %s HTTP/1.1\r
        Host: test\r
        \r
        """.formatted(raw))
    );

    assertEquals(req.path(), path);
  }

  @DataProvider
  public Object[][] pathInvalidProvider() {
    List<Object[]> l = new ArrayList<>();

    l.add(invalid("", InvalidRequestLine.PATH_FIRST_CHAR, "empty path"));
    l.add(invalid("index.html", InvalidRequestLine.PATH_FIRST_CHAR, "path does not begin with '/'"));
    l.add(invalid("//index.html", InvalidRequestLine.PATH_SEGMENT_NZ, "path begins with empty segment"));
    l.add(invalid("/%2Findex.html", InvalidRequestLine.PATH_SEGMENT_NZ, "path begins with empty segment"));
    l.add(invalid("%2F/index.html", InvalidRequestLine.PATH_SEGMENT_NZ, "path begins with empty segment"));
    l.add(invalid("%2F%2Findex.html", InvalidRequestLine.PATH_SEGMENT_NZ, "path begins with empty segment"));

    for (int value = 0; value < VALID_BYTES.length; value++) {
      switch (value) {
        case ' ', '\n', '\r' -> {/* will trigger 505 not 400 */}

        case '?' -> {/* valid, but tested on parse query */}

        case '%' -> {/* tested on percent-encoded */}

        default -> {
          if (!VALID_BYTES[value]) {
            l.add(invalid(value));
          }
        }
      }
    }

    return l.toArray(Object[][]::new);
  }

  private Object[] invalid(int value) {
    final String path;
    path = "/pa" + (char) value + "th";

    final String description;
    description = "path contains the " + Integer.toHexString(value) + " invalid byte";

    return invalid(path, InvalidRequestLine.PATH_NEXT_CHAR, description);
  }

  private Object[] invalid(
      String path,
      HttpClientException.Kind kind,
      String description) {
    if (!path.isEmpty()) {
      path = " " + path;
    }

    String req = """
    GET%s HTTP/1.1\r
    \r
    """.formatted(path);

    return new Object[] {req, kind, description};
  }

  @Test(dataProvider = "pathInvalidProvider")
  public void pathInvalid(
      String request,
      HttpClientException.Kind kind,
      String description) throws IOException {
    try {
      HttpRequestParserY.parse(
          test -> test.bufferSize(256, 512),

          iso8859(request)
      );

      Assert.fail("It should have thrown");
    } catch (HttpClientException expected) {
      assertEquals(expected.kind, kind);
    }
  }

  @DataProvider
  public Object[][] percentValidProvider() {
    return new Object[][] {
        // Existing 1-byte cases
        {"/utf8/%40", "/utf8/@", "1-byte (ASCII @)"},
        {"/utf8/%00", "/utf8/\u0000", "1-byte (min value, null character)"},
        {"/utf8/%7F", "/utf8/\u007F", "1-byte (max value, DEL)"},
        // Additional 1-byte cases
        {"/utf8/%2F", "/utf8//", "1-byte (reserved character /)"},
        {"/utf8/%3A", "/utf8/:", "1-byte (reserved character :)"},

        // Existing 2-byte cases
        {"/utf8/%C3%A1", "/utf8/á", "2-bytes (Latin small letter a with acute)"},
        {"/utf8/%C2%80", "/utf8/\u0080", "2-bytes (min value)"},
        {"/utf8/%DF%BF", "/utf8/\u07FF", "2-bytes (max value)"},
        // Additional 2-byte cases
        {"/utf8/%C3%80", "/utf8/À", "2-bytes (Latin capital letter A with grave)"},
        {"/utf8/%D7%90", "/utf8/א", "2-bytes (Hebrew letter Alef)"},

        // 3-byte cases
        {"/utf8/%E0%A0%80", "/utf8/\u0800", "3-bytes (min value)"},
        {"/utf8/%EF%BF%BF", "/utf8/\uFFFF", "3-bytes (max value)"},
        {"/utf8/%E2%82%AC", "/utf8/€", "3-bytes (Euro sign)"},
        {"/utf8/%E2%9C%93", "/utf8/✓", "3-bytes (Check mark)"},
        {"/utf8/%ED%9F%BF", "/utf8/\uD7FF", "3-bytes (max before surrogate range)"},
        {"/utf8/%EE%80%80", "/utf8/\uE000", "3-bytes (min after surrogate range)"},

        // 4-byte cases
        {"/utf8/%F0%90%80%80", "/utf8/\uD800\uDC00", "4-bytes (min value, U+10000)"},
        {"/utf8/%F4%8F%BF%BF", "/utf8/\uDBFF\uDFFF", "4-bytes (max value, U+10FFFF)"},
        {"/utf8/%F0%9F%98%80", "/utf8/😀", "4-bytes (grinning face emoji)"},
        {"/utf8/%F0%9F%8C%8D", "/utf8/🌍", "4-bytes (Earth globe Europe-Africa)"},

        // Mixed and complex cases
        {"/utf8/%C3%A1%E2%82%AC%F0%9F%98%80", "/utf8/á€😀", "Mixed (2-byte, 3-byte, 4-byte)"},
        {"/utf8/hello%2Fworld%40%23", "/utf8/hello/world@#", "Multiple 1-byte reserved characters"},
        {"/utf8/%25", "/utf8/%", "1-byte (percent sign itself encoded)"},
        {"/utf8/%E2%80%8B", "/utf8/\u200B", "3-bytes (zero-width space)"},
        {"/utf8/%E2%81%B0", "/utf8/⁰", "3-bytes (superscript zero)"}
    };
  }

  @Test(dataProvider = "percentValidProvider")
  public void percentValid(String raw, String path, String description) throws IOException {
    final HttpRequest req;
    req = HttpRequestParserY.parse(
        test -> test.bufferSize(256, 512),

        iso8859("""
        GET %s HTTP/1.1\r
        Host: test\r
        \r
        """.formatted(raw))
    );

    assertEquals(req.path(), path);
  }

  @DataProvider
  public Object[][] percentInvalidProvider() {
    return new Object[][] {
        // Existing 1-byte invalid cases
        {"/pct/%xd", "1-byte + invalid hex digit (first)"},
        {"/pct/%ax", "1-byte + invalid hex digit (second)"},
        {"/pct/%FF", "1-byte + invalid range"},

        // Invalid 2-byte cases
        {"/pct/%C0%80", "2-bytes + overlong encoding (U+0000)"},
        {"/pct/%C2%FF", "2-bytes + invalid continuation byte (out of range)"},
        {"/pct/%C2", "2-bytes + incomplete sequence (missing continuation byte)"},
        {"/pct/%DF%C0", "2-bytes + invalid continuation byte (not 10xxxxxx)"},

        // Invalid 3-byte cases
        {"/pct/%E0%80%80", "3-bytes + overlong encoding (U+0000)"},
        {"/pct/%E0%A0%FF", "3-bytes + invalid continuation byte (third byte)"},
        {"/pct/%ED%A0%80", "3-bytes + surrogate code point (U+D800)"},
        {"/pct/%E0%A0", "3-bytes + incomplete sequence (missing last byte)"},
        {"/pct/%EF%FF%80", "3-bytes + invalid continuation byte (second byte)"},

        // Invalid 4-byte cases
        {"/pct/%F0%80%80%80", "4-bytes + overlong encoding (U+0000)"},
        {"/pct/%F5%80%80%80", "4-bytes + lead byte exceeds Unicode range (0xF5)"},
        {"/pct/%F0%FF%80%80", "4-bytes + invalid continuation byte (second byte)"},
        {"/pct/%F0%80%FF%80", "4-bytes + invalid continuation byte (third byte)"},
        {"/pct/%F0%80%80%FF", "4-bytes + invalid continuation byte (fourth byte)"},
        {"/pct/%F0%80%80", "4-bytes + incomplete sequence (missing last byte)"},

        // Other edge cases
        {"/pct/%", "Incomplete percent-encoding (lone %)"},
        {"/pct/%G0%80", "Invalid hex digit in percent-encoding (G)"},
        {"/pct/%C3%A1%FF", "Valid 2-byte followed by invalid 1-byte"},
        {"/pct/%E0%A0%80%F5%80%80%80", "Valid 3-byte followed by invalid 4-byte"},
        {"/pct/%%80", "Double percent sign with invalid sequence"},
        {"/pct/%80%80%80%80", "Invalid lead byte (0x80, continuation byte)"}
    };
  }

  @Test(dataProvider = "percentInvalidProvider")
  public void percentInvalid(String raw, String description) throws IOException {
    try {
      HttpRequestParserY.parse(
          test -> test.bufferSize(256, 512),

          iso8859("""
          GET %s HTTP/1.1\r
          Host: test\r
          \r
          """.formatted(raw))
      );

      Assert.fail("It should have thrown");
    } catch (HttpClientException expected) {
      assertEquals(expected.kind, InvalidRequestLine.PATH_PERCENT);
    }
  }

  @DataProvider
  public Object[][] slowClientProvider() {
    final Object[][] a1;
    a1 = pathValidProvider();

    final Object[][] a2;
    a2 = percentValidProvider();

    final Object[][] concat;
    concat = new Object[a1.length + a2.length][];

    System.arraycopy(a1, 0, concat, 0, a1.length);
    System.arraycopy(a2, 0, concat, a1.length, a2.length);

    return concat;
  }

  @Test(dataProvider = "slowClientProvider")
  public void slowClient(String raw, String expected, String description) throws IOException {
    final HttpRequest req;
    req = HttpRequestParserY.parse(
        test -> test.bufferSize(256, 512),

        Y.slowStream(1, iso8859("""
        GET %s HTTP/1.1\r
        Host: test\r
        \r
        """.formatted(raw)))
    );

    assertEquals(req.path(), expected);
  }

  @Test
  public void uriTooLong() throws IOException {
    final String veryLongId;
    veryLongId = "/12345/sub/abc7890".repeat(200);

    try {
      HttpRequestParserY.parse(
          test -> test.bufferSize(256, 512),

          iso8859("""
          GET /entity/%s HTTP/1.1\r
          Host: test\r
          \r
          """.formatted(veryLongId))
      );

      Assert.fail("It should have thrown");
    } catch (HttpClientException expected) {
      assertEquals(expected.kind, InvalidRequestLine.URI_TOO_LONG);
    }
  }

  @Test
  public void ioException() {
    final IOException ex;
    ex = Y.trimStackTrace(new IOException(), 1);

    try {
      HttpRequestParserY.parse(
          test -> test.bufferSize(256, 512),

          iso8859("GET /index.h"),

          ex
      );

      Assert.fail("It should have thrown");
    } catch (IOException expected) {
      assertSame(expected, ex);
    }
  }

  @DataProvider
  public Object[][] rawPathProvider() {
    final List<Object[]> l;
    l = new ArrayList<>();

    for (int value = 0; value < 128; value++) {
      final String raw;
      raw = "/raw/%%%02X".formatted(value);

      if (VALID_BYTES[value]) {
        l.add(new Object[] {raw, "/raw/" + (char) value});
      } else {
        l.add(new Object[] {raw, raw});
      }
    }

    return l.toArray(Object[][]::new);
  }

  // TODO remove?
  @Test(enabled = false, dataProvider = "rawPathProvider")
  public void rawPath(String raw, String expected) throws IOException {
    final HttpRequest req;
    req = HttpRequestParserY.parse(
        test -> test.bufferSize(256, 512),

        iso8859("""
        GET %s HTTP/1.1\r
        Host: test\r
        \r
        """.formatted(raw))
    );

    assertEquals(req.rawPath(), expected);
  }

  private byte[] iso8859(String s) {
    return s.getBytes(StandardCharsets.ISO_8859_1);
  }

}
