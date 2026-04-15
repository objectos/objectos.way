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

import static objectos.http.HttpY.arr;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;

import module java.base;
import objectos.http.HttpClientException.Kind;
import objectos.way.Y;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HttpRequestParser4QueryTest {

  private final String validString = Http.unreserved() + Http.subDelims() + ":@/?";

  private Map<String, Object> parse(Object... data) throws IOException {
    final Socket socket;
    socket = Y.socket(data);

    final HttpRequestParser0Input input;
    input = HttpRequestParser0Input.of(512, socket);

    input.readByte(); // '?' or other

    final HttpRequestParser4Query parser;
    parser = new HttpRequestParser4Query(input);

    return parser.parse();
  }

  @Test
  public void noQuery() throws IOException {
    var res = parse(
        " HTTP/1.1"
    );

    assertEquals(res, Map.of());
  }

  @DataProvider
  public Object[][] queryValidProvider() {
    return HttpY.queryValidProvider();
  }

  @Test(dataProvider = "queryValidProvider")
  public void queryValid(String raw, Map<String, Object> expected, String description) throws IOException {
    var res = parse(
        "?%s HTTP/1.1".formatted(raw)
    );

    assertEquals(res, expected);
  }

  @DataProvider
  public Object[][] queryInvalidProvider() {
    final List<Object[]> l;
    l = new ArrayList<>();

    final byte[] validBytes;
    validBytes = validString.getBytes();

    Arrays.sort(validBytes);

    for (int ascii = 0; ascii < 128; ascii++) {
      if (ascii == ' ' || ascii == '\r' || ascii == '\n') {
        continue;
      }

      if (ascii == '%') {
        continue;
      }

      final int idx;
      idx = Arrays.binarySearch(validBytes, (byte) ascii);

      if (idx >= 0) {
        continue;
      }

      final String key;
      key = "k" + (char) ascii + "y=value";

      final String val;
      val = "key=va" + (char) ascii + "ue";

      final String msg;
      msg = "Unexpected byte 0x%02X while parsing URI query".formatted(ascii);

      l.add(arr(key, msg));
      l.add(arr(val, msg));
    }

    for (int iso = 128; iso < 256; iso++) {
      final String key;
      key = "k" + (char) iso + "y=value";

      final String val;
      val = "key=va" + (char) iso + "ue";

      final String msg;
      msg = "Unexpected byte 0x%02X while reading from input: ASCII value expected".formatted(iso);

      l.add(arr(key, msg));
      l.add(arr(val, msg));
    }

    return l.toArray(Object[][]::new);
  }

  @Test(dataProvider = "queryInvalidProvider")
  public void queryInvalid(String raw, String msg) throws IOException {
    try {
      parse(
          "?%s HTTP/1.1".formatted(raw).getBytes(StandardCharsets.ISO_8859_1)
      );

      Assert.fail("It should have thrown");
    } catch (HttpClientException expected) {
      assertEquals(expected.getMessage(), msg);

      assertEquals(expected.kind, Kind.INVALID_REQUEST_LINE);
    }
  }

  @DataProvider
  public Object[][] percentValidProvider() {
    return new Object[][] {
        {"k%7D=value", Map.of("k}", "value"), "percent: 1-byte + key"},
        {"key=va%7Dl", Map.of("key", "va}l"), "percent: 1-byte + value"},
        {"%5E=%3D", Map.of("^", "="), "percent: 1-byte + key + value"},
        {"k%C2%A0=value", Map.of("k\u00A0", "value"), "percent: 2-byte + key"},
        {"key=va%C3%91l", Map.of("key", "vaÑl"), "percent: 2-byte + value"},
        {"%C2%BF=%C3%80", Map.of("¿", "À"), "percent: 2-byte + key + value"},
        {"k%E2%80%8B=value", Map.of("k\u200B", "value"), "percent: 3-byte + key"},
        {"key=va%E2%82%ACl", Map.of("key", "va€l"), "percent: 3-byte + value"},
        {"%E2%98%83=%E2%9C%93", Map.of("☃", "✓"), "percent: 3-byte + key + value"},
        {"k%F0%9F%98%80=value", Map.of("k😀", "value"), "percent: 4-byte + key"},
        {"key=va%F0%9F%8C%8Al", Map.of("key", "va🌊l"), "percent: 4-byte + value"},
        {"%F0%9F%90%8C=%F0%9F%8D%8F", Map.of("🐌", "🍏"), "percent: 4-byte + key + value"},

        // test stringbuilder code path
        {"k%7Dy%7D=value", Map.of("k}y}", "value"), "percent: 1-byte + key"},
        {"key=val%7De%7D", Map.of("key", "val}e}"), "percent: 1-byte + value"},
    };
  }

  @Test(dataProvider = "percentValidProvider")
  public void percentValid(String raw, Map<String, Object> expected, String description) throws IOException {
    var res = parse(
        "?%s HTTP/1.1".formatted(raw)
    );

    assertEquals(res, expected);
  }

  @DataProvider
  public Object[][] percentInvalidProvider() {
    return new Object[][] {
        {"key%xz=value", "key + invalid percent sequence"},
        {"key=val%xxue", "value + incomplete percent sequence"},
        {"k%C3%XZy=value", "key + 2-bytes invalid percent sequence (last)"},
        {"key=val%C3%XZue", "value + 2-bytes invalid percent sequence (last)"},
        {"k%E2%80%XZy=value", "key + 3-bytes invalid percent sequence (last)"},
        {"key=val%E2%80%XZue", "value + 3-bytes invalid percent sequence (last)"},
        {"k%E2%XZ%8By=value", "key + 3-bytes invalid percent sequence (second)"},
        {"key=val%E2%XZ%8Bue", "value + 3-bytes invalid percent sequence (second)"},
        {"k%F0%XZ%98%80=value", "key + 4-bytes invalid percent sequence (second)"},
        {"key=val%F0%XZ%98%80ue", "value + 4-bytes invalid percent sequence (second)"},
        {"k%F0%9F%XZ%80=value", "key + 4-bytes invalid percent sequence (third)"},
        {"key=val%F0%9F%XZ%80ue", "value + 4-bytes invalid percent sequence (third)"},
        {"k%F0%9F%98%XZ=value", "key + 4-bytes invalid percent sequence (fourth)"},
        {"key=val%F0%9F%98%XZue", "value + 4-bytes invalid percent sequence (fourth)"},

        {"k%Gy=value", "key + 1-byte invalid percent sequence (non-hex character)"},
        {"key=val%G0ue", "value + 1-byte invalid percent sequence (non-hex character)"},
        {"k%=value", "key + 1-byte empty percent sequence"},
        {"key=val%ue", "value + 1-byte empty percent sequence"},
        {"k%2=value", "key + 1-byte incomplete percent sequence (single hex digit)"},
        {"key=val%2ue", "value + 1-byte incomplete percent sequence (single hex digit)"},

        {"k%C3y=value", "key + 2-bytes incomplete percent sequence (missing second byte)"},
        {"key=val%C3ue", "value + 2-bytes incomplete percent sequence (missing second byte)"},
        {"k%80%80y=value", "key + 2-bytes invalid percent sequence (invalid leading byte)"},
        {"key=val%80%80ue", "value + 2-bytes invalid percent sequence (invalid leading byte)"},
        {"k%C3%GGy=value", "key + 2-bytes invalid percent sequence (non-hex character)"},
        {"key=val%C3%GGue", "value + 2-bytes invalid percent sequence (non-hex character)"},

        {"k%E2%80y=value", "key + 3-bytes incomplete percent sequence (missing third byte)"},
        {"key=val%E2%80ue", "value + 3-bytes incomplete percent sequence (missing third byte)"},
        {"k%E0%80%80y=value", "key + 3-bytes invalid percent sequence (invalid leading byte)"},
        {"key=val%E0%80%80ue", "value + 3-bytes invalid percent sequence (invalid leading byte)"},
        {"k%E2%80%GGy=value", "key + 3-bytes invalid percent sequence (non-hex character in last)"},
        {"key=val%E2%80%GGue", "value + 3-bytes invalid percent sequence (non-hex character in last)"},
        {"k%E2%GG%80y=value", "key + 3-bytes invalid percent sequence (non-hex character in second)"},
        {"key=val%E2 procedimientos%GG%80ue", "value + 3-bytes invalid percent sequence (non-hex character in second)"},

        {"k%F0%9F%98y=value", "key + 4-bytes incomplete percent sequence (missing fourth byte)"},
        {"key=val%F0%9F%98ue", "value + 4-bytes incomplete percent sequence (missing fourth byte)"},
        {"k%F5%80%80%80y=value", "key + 4-bytes invalid percent sequence (invalid leading byte > U+10FFFF)"},
        {"key=val%F5%80%80%80ue", "value + 4-bytes invalid percent sequence (invalid leading byte > U+10FFFF)"},
        {"k%F0%80%GG%80y=value", "key + 4-bytes invalid percent sequence (non-hex character in third)"},
        {"key=val%F0%80%GG%80ue", "value + 4-bytes invalid percent sequence (non-hex character in third)"},
        {"k%F0%GG%98%80y=value", "key + 4-bytes invalid percent sequence (non-hex character in second)"},
        {"key=val%F0%GG%98%80ue", "value + 4-bytes invalid percent sequence (non-hex character in second)"},

        // test stringbuilder code path
        {"%6Bey%xz=value", "key + invalid percent sequence"},
        {"key=%76al%xxue", "value + incomplete percent sequence"}
    };
  }

  @Test(dataProvider = "percentInvalidProvider")
  public void percentInvalid(String raw, String description) throws IOException {
    try {
      parse(
          "?%s HTTP/1.1".formatted(raw).getBytes(StandardCharsets.ISO_8859_1)
      );

      Assert.fail("It should have thrown");
    } catch (HttpClientException expected) {
      assertEquals(expected.kind, Kind.INVALID_REQUEST_LINE);
    }
  }

  @DataProvider
  public Object[][] ioExceptionProvider() {
    return new Object[][] {
        {"?ke", "Thrown while parsing key"},
        {"?k%7", "Thrown while parsing key (encoded)"},
        {"?key=v", "Thrown while parsing value"},
        {"?key=v%7", "Thrown while parsing value (encoded)"}
    };
  }

  @Test(dataProvider = "ioExceptionProvider")
  public void ioException(String req, String description) {
    final IOException ex;
    ex = Y.trimStackTrace(new IOException(), 1);

    try {
      parse(
          req,

          ex
      );

      Assert.fail("It should have thrown");
    } catch (IOException expected) {
      assertSame(expected, ex);
    }
  }

  @Test
  public void uriTooLong() throws IOException {
    final String veryLongValue;
    veryLongValue = "ba7f9045".repeat(200);

    try {
      parse(
          "?hash=%s HTTP/1.1".formatted(veryLongValue)
      );

      Assert.fail("It should have thrown");
    } catch (HttpClientException expected) {
      assertEquals(expected.getMessage(), "Buffer overflow while parsing URI query");

      assertEquals(expected.kind, Kind.URI_TOO_LONG);
    }
  }

  @Test(description = """
  UriQuery: empty
  """)
  public void testCase01() {
    HttpRequestTarget q;
    q = queryOf("");

    assertEquals(q.queryParam("foo"), null);
    assertEquals(q.queryParam(""), null);
    assertEquals(q.queryParamNames(), Set.of());
  }

  @Test(description = """
  UriQuery: single value
  """)
  public void testCase02() {
    HttpRequestTarget q;
    q = queryOf("foo=bar");

    assertEquals(q.queryParam("foo"), "bar");
    assertEquals(q.queryParam("x"), null);
    assertEquals(q.queryParamNames(), Set.of("foo"));
  }

  @Test(description = """
  UriQuery: name only
  """)
  public void testCase03() {
    HttpRequestTarget q;
    q = queryOf("foo");

    assertEquals(q.queryParam("foo"), "");
    assertEquals(q.queryParam("x"), null);
    assertEquals(q.queryParamNames(), Set.of("foo"));
  }

  @Test(description = """
  UriQuery: empty value
  """)
  public void testCase04() {
    HttpRequestTarget q;
    q = queryOf("foo=");

    assertEquals(q.queryParam("foo"), "");
    assertEquals(q.queryParam("x"), null);
    assertEquals(q.queryParamNames(), Set.of("foo"));
  }

  @Test(description = """
  UriQuery: corner cases
  """)
  public void testCase05() {
    HttpRequestTarget q;
    q = queryOf("a&foo=");

    assertEquals(q.queryParam("a"), "");
    assertEquals(q.queryParam("foo"), "");
    assertEquals(q.queryParam("x"), null);
    assertEquals(q.queryParamNames(), Set.of("a", "foo"));
  }

  @Test(description = """
  UriQuery: corner cases
  """)
  public void testCase06() {
    HttpRequestTarget q;
    q = queryOf("a=1+2+3&b=foo");

    assertEquals(q.queryParam("a"), "1 2 3");
    assertEquals(q.queryParam("b"), "foo");
    assertEquals(q.queryParam("x"), null);
    assertEquals(q.queryParamNames(), Set.of("a", "b"));
  }

  @Test(description = """
  UriQuery: getAsInt()
  """)
  public void testCase07() {
    HttpRequestTarget q;
    q = queryOf("a=123&b=-456&c=foo&d=&e&f=123.45");

    assertEquals(q.queryParamAsInt("a", -1), 123);
    assertEquals(q.queryParamAsInt("b", -1), -456);
    assertEquals(q.queryParamAsInt("c", -1), -1);
    assertEquals(q.queryParamAsInt("d", -1), -1);
    assertEquals(q.queryParamAsInt("e", -1), -1);
    assertEquals(q.queryParamAsInt("f", -1), -1);
    assertEquals(q.queryParamNames(), Set.of("a", "b", "c", "d", "e", "f"));
  }

  @Test(description = """
  UriQuery: set
  """)
  public void testCase08() {
    HttpRequestTarget q;
    q = queryOf("a=1&b=2&c=3&d");

    assertEquals(q.queryParam("a"), "1");
    assertEquals(q.queryParam("b"), "2");
    assertEquals(q.queryParam("c"), "3");
    assertEquals(q.queryParam("d"), "");
    assertEquals(q.queryParamNames(), Set.of("a", "b", "c", "d"));
  }

  @Test(description = """
  UriQuery: duplicate name should return first value
  """)
  public void testCase09() {
    HttpRequestTarget q;
    q = queryOf("a=123&b=xpto&c&b=");

    assertEquals(q.queryParam("a"), "123");
    assertEquals(q.queryParam("b"), "xpto");
    assertEquals(q.queryParam("c"), "");
    assertEquals(q.queryParamNames(), Set.of("a", "b", "c"));
  }

  @Test
  public void testCase10() {
    HttpRequestTarget q;
    q = queryOf("%26=the%20%26%20char&foo=bar");

    assertEquals(q.queryParam("&"), "the & char");
    assertEquals(q.queryParam("foo"), "bar");
    assertEquals(q.queryParam("%26"), null);
    assertEquals(q.queryParamNames(), Set.of("&", "foo"));
  }

  private HttpRequestTarget queryOf(String q) {
    try {
      final Map<String, Object> map;
      map = parse("?" + q + " HTTP/1.1");

      return new HttpRequest0(null, null, map, null, null, null);
    } catch (IOException e) {
      throw new AssertionError("failed", e);
    }
  }

}