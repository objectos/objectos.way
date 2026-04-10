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

import static objectos.http.HttpY.arr;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import objectos.http.HttpRequestParserException.Kind;
import objectos.way.Y;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@SuppressWarnings("exports")
public class HttpRequestParser3PathTest {

  private final String validString = Http.unreserved() + Http.subDelims() + ":@" + "/";

  private String parse(Object... data) throws IOException {
    final Socket socket;
    socket = Y.socket(data);

    final HttpRequestParser0Input input;
    input = HttpRequestParser0Input.of(512, socket);

    final HttpRequestParser3Path parser;
    parser = new HttpRequestParser3Path(input);

    return parser.parse();
  }

  @DataProvider
  public Object[][] pathValidProvider() {
    final List<Object[]> l;
    l = new ArrayList<>();

    l.add(arr("/", "/", "root"));
    l.add(arr("/index.html", "/index.html", "with segment"));

    final byte[] validBytes;
    validBytes = validString.getBytes(StandardCharsets.US_ASCII);

    for (byte b : validBytes) {
      final String raw;
      raw = "/path" + (char) b;

      final String path;
      path = raw;

      final String description;
      description = "path contains the 0x%02x valid byte".formatted(b);

      l.add(arr(raw, path, description));
    }

    return l.toArray(Object[][]::new);
  }

  @Test(dataProvider = "pathValidProvider")
  public void pathValid(String raw, String path, String description) throws IOException {
    assertEquals(
        parse(
            iso8859("%s HTTP/1.1".formatted(raw))
        ),

        path
    );
  }

  @DataProvider
  public Object[][] pathInvalidProvider() {
    final List<Object[]> l;
    l = new ArrayList<>();

    l.add(arr("", "Unexpected byte 0x20 while parsing path: path must start with '/'"));
    l.add(arr("index.html", "Unexpected byte 0x69 while parsing path: path must start with '/'"));
    l.add(arr("//index.html", "First path segment must not be empty"));
    l.add(arr("/%2Findex.html", "First path segment must not be empty"));
    l.add(arr("%2F/index.html", "First path segment must not be empty"));
    l.add(arr("%2F%2Findex.html", "First path segment must not be empty"));

    final byte[] validBytes;
    validBytes = validString.getBytes();

    Arrays.sort(validBytes);

    for (int ascii = 0; ascii < 128; ascii++) {
      if (ascii == ' ' || ascii == '\r' || ascii == '\n') {
        continue;
      }

      if (ascii == '%' || ascii == '?') {
        continue;
      }

      final int idx;
      idx = Arrays.binarySearch(validBytes, (byte) ascii);

      if (idx >= 0) {
        continue;
      }

      final String path;
      path = "/pa" + (char) ascii + "th";

      final String msg;
      msg = "Unexpected byte 0x%02X while parsing path".formatted(ascii);

      l.add(arr(path, msg));
    }

    for (int iso = 128; iso < 256; iso++) {
      final String path;
      path = "/pa" + (char) iso + "th";

      final String msg;
      msg = "Unexpected byte 0x%02X while reading from input: ASCII value expected".formatted(iso);

      l.add(arr(path, msg));
    }

    return l.toArray(Object[][]::new);
  }

  @Test(dataProvider = "pathInvalidProvider")
  public void pathInvalid(String path, String msg) throws IOException {
    try {
      parse(
          iso8859("%s HTTP/1.1".formatted(path))
      );

      Assert.fail("It should have thrown");
    } catch (HttpRequestParserException expected) {
      assertEquals(expected.getMessage(), msg);

      assertEquals(expected.kind, Kind.INVALID_REQUEST_LINE);
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
    assertEquals(
        parse(
            iso8859("%s HTTP/1.1".formatted(raw))
        ),

        path
    );
  }

  @DataProvider
  public Object[][] percentInvalidProvider() {
    return new Object[][] {
        {"/pct/%xd", "Invalid percent-encoded value: 0x78 is not an US-ASCII digit char"},
        {"/pct/%ax", "Invalid percent-encoded value: 0x78 is not an US-ASCII digit char"},
        {"/pct/%FF", "Invalid percent-encoded value: got 0x20 instead of start of byte 2 of a 4-byte UTF-8 code point"},

        {"/pct/%C0%80", "Invalid percent-encoded value: 0xC0 0x80 is not a valid UTF-8 2-byte sequence"},
        {"/pct/%C2%FF", "Invalid percent-encoded value: 0xFF is not a valid UTF-8 byte"},
        {"/pct/%C2", "Invalid percent-encoded value: got 0x20 instead of start of byte 2 of a 2-byte UTF-8 code point"},

        {"/pct/%E0%80%80", "Invalid percent-encoded value: 0xE0 0x80 0x80 is not a valid UTF-8 3-byte sequence"},
        {"/pct/%E0%A0%FF", "Invalid percent-encoded value: 0xFF is not a valid UTF-8 byte"},
        {"/pct/%ED%A0%80", "Invalid percent-encoded value: 0xED 0xA0 0x80 is not a valid UTF-8 3-byte sequence"},
        {"/pct/%E0%A0", "Invalid percent-encoded value: got 0x20 instead of start of byte 3 of a 3-byte UTF-8 code point"},
        {"/pct/%EF%FF%80", "Invalid percent-encoded value: 0xFF is not a valid UTF-8 byte"},

        {"/pct/%F0%80%80%80", "Invalid percent-encoded value: 0xF0 0x80 0x80 0x80 is not a valid UTF-8 4-byte sequence"},
        {"/pct/%F5%80%80%80", "Invalid percent-encoded value: 0xF5 0x80 0x80 0x80 is not a valid UTF-8 4-byte sequence"},
        {"/pct/%F0%FF%80%80", "Invalid percent-encoded value: 0xFF is not a valid UTF-8 byte"},
        {"/pct/%F0%80%FF%80", "Invalid percent-encoded value: 0xFF is not a valid UTF-8 byte"},
        {"/pct/%F0%80%80%FF", "Invalid percent-encoded value: 0xFF is not a valid UTF-8 byte"},
        {"/pct/%F0%80%80", "Invalid percent-encoded value: got 0x20 instead of start of byte 4 of a 4-byte UTF-8 code point"},

        // Other edge cases
        {"/pct/%", "Invalid percent-encoded value: 0x20 is not an US-ASCII digit char"},
        {"/pct/%G0%80", "Invalid percent-encoded value: 0x47 is not an US-ASCII digit char"},
        {"/pct/%C3%A1%FF", "Invalid percent-encoded value: got 0x20 instead of start of byte 2 of a 4-byte UTF-8 code point"},
        {"/pct/%E0%A0%80%F5%80%80%80", "Invalid percent-encoded value: 0xF5 0x80 0x80 0x80 is not a valid UTF-8 4-byte sequence"},
        {"/pct/%%80", "Invalid percent-encoded value: 0x25 is not an US-ASCII digit char"},
        {"/pct/%80%80%80%80", "Invalid percent-encoded value: 0x80 is not a valid UTF-8 1-byte sequence"}
    };
  }

  @Test(dataProvider = "percentInvalidProvider")
  public void percentInvalid(String raw, String msg) throws IOException {
    try {
      parse(
          iso8859("%s HTTP/1.1".formatted(raw))
      );

      Assert.fail("It should have thrown");
    } catch (HttpRequestParserException expected) {
      assertEquals(expected.getMessage(), msg);

      assertEquals(expected.kind, Kind.INVALID_REQUEST_LINE);
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
    assertEquals(
        parse(
            Y.slowStream(1, iso8859("%s HTTP/1.1".formatted(raw)))
        ),

        expected
    );
  }

  @Test
  public void uriTooLong() throws IOException {
    final String veryLongId;
    veryLongId = "/12345/sub/abc7890".repeat(200);

    try {
      parse(
          iso8859("/entity/%s HTTP/1.1".formatted(veryLongId))
      );

      Assert.fail("It should have thrown");
    } catch (HttpRequestParserException expected) {
      assertEquals(expected.getMessage(), "Buffer overflow while parsing path");

      assertEquals(expected.kind, Kind.URI_TOO_LONG);
    }
  }

  @Test
  public void ioException() {
    final IOException ex;
    ex = Y.trimStackTrace(new IOException(), 1);

    try {
      parse(
          iso8859("/index.h"),

          ex
      );

      Assert.fail("It should have thrown");
    } catch (IOException expected) {
      assertSame(expected, ex);
    }
  }

  private byte[] iso8859(String s) {
    return s.getBytes(StandardCharsets.ISO_8859_1);
  }

}
