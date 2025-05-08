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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HttpExchangeTest3ParseQuery extends HttpExchangeTest {

  private final boolean[] validBytes = queryValidBytes();

  @Test
  public void noQuery() {
    exec(test -> {
      test.bufferSize(256, 512);

      test.xch(xch -> {
        xch.req("""
        GET /path HTTP/1.1\r
        Host: www.example.com\r
        \r
        """);

        xch.handler(http -> {
          assertEquals(http.queryParam("foo"), null);
          assertEquals(http.queryParamNames(), Set.of());
          assertEquals(http.rawQuery(), null);
          assertEquals(http.rawQueryWith("page", "1"), "page=1");

          http.ok(Media.Bytes.textPlain("OK"));
        });

        xch.resp("""
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 2\r
        \r
        OK""");
      });
    });
  }

  @DataProvider
  public Object[][] queryValidProvider() {
    final List<Object[]> l;
    l = new ArrayList<>();

    l.add(arr("", Map.of(), "empty"));
    l.add(arr("key=value", Map.of("key", "value"), "one"));
    l.add(arr("=value", Map.of("", "value"), "one + empty key"));
    l.add(arr("key=", Map.of("key", ""), "one + empty value"));
    l.add(arr("key", Map.of("key", ""), "one + empty value + no equals"));
    l.add(arr("key1=value1&key2=value2", Map.of("key1", "value1", "key2", "value2"), "two"));
    l.add(arr("=value1&key2=value2", Map.of("", "value1", "key2", "value2"), "two + empty key1"));
    l.add(arr("key1=value1&=value2", Map.of("key1", "value1", "", "value2"), "two + empty key2"));
    l.add(arr("key1=&key2=value2", Map.of("key1", "", "key2", "value2"), "two + empty value1"));
    l.add(arr("key1=value1&key2=", Map.of("key1", "value1", "key2", ""), "two + empty value2"));
    l.add(arr("key1&key2=value2", Map.of("key1", "", "key2", "value2"), "two + empty value1 + no equals"));
    l.add(arr("key1=value1&key2", Map.of("key1", "value1", "key2", ""), "two + empty value2 + no equals"));
    l.add(arr("key=value1&key=value2", Map.of("key", List.of("value1", "value2")), "two + duplicate keys"));

    for (int value = 0; value < validBytes.length; value++) {
      switch (value) {
        case ' ' -> {/* will cause parsing to move to VERSION */}

        case '\n', '\r' -> {/* will trigger 505 not 400 */}

        case '&', '=' -> {/* valid in query string, but has special meaning*/}

        case '+' -> {
          l.add(arr("+=value", Map.of(" ", "value"), "key contains the '+' character"));
          l.add(arr("key=+", Map.of("key", " "), "value contains the '+' character"));
        }

        default -> {
          if (validBytes[value]) {
            l.add(queryValidKey(value));
            l.add(queryValidValue(value));
          }
        }
      }
    }

    return l.toArray(Object[][]::new);
  }

  private Object[] queryValidKey(int value) {
    final String key;
    key = Character.toString(value);

    return arr(key + "=value", Map.of(key, "value"), "key contains the " + Integer.toHexString(value) + " valid byte");
  }

  private Object[] queryValidValue(int value) {
    final String val;
    val = Character.toString(value);

    return arr("key=" + val, Map.of("key", val), "value contains the " + Integer.toHexString(value) + " valid byte");
  }

  @Test(dataProvider = "queryValidProvider")
  public void queryValid(String raw, Map<String, Object> expected, String description) {
    test(raw, expected);
  }

  @DataProvider
  public Object[][] queryInvalidProvider() {
    final List<Object[]> l;
    l = new ArrayList<>();

    for (int value = 0; value < validBytes.length; value++) {
      switch (value) {
        case ' ' -> {/* will cause parsing to move to VERSION */}

        case '\n', '\r' -> {/* will trigger 505 not 400 */}

        default -> {
          if (!validBytes[value]) {
            l.add(queryInvalidKey(value));
            l.add(queryInvalidValue(value));
          }
        }
      }
    }

    return l.toArray(Object[][]::new);
  }

  private Object[] queryInvalidKey(int value) {
    final String key;
    key = Character.toString(value);

    return arr(key + "=value", "key contains the " + Integer.toHexString(value) + " invalid byte");
  }

  private Object[] queryInvalidValue(int value) {
    final String val;
    val = Character.toString(value);

    return arr("key=" + val, "value contains the " + Integer.toHexString(value) + " invalid byte");
  }

  @Test(dataProvider = "queryInvalidProvider")
  public void queryInvalid(String raw, String description) {
    badRequest(
        """
        GET /path?%s HTTP/1.1\r
        Host: test\r
        \t
        """.formatted(raw).getBytes(StandardCharsets.ISO_8859_1)
    );
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
        {"%F0%9F%90%8C=%F0%9F%8D%8F", Map.of("🐌", "🍏"), "percent: 4-byte + key + value"}
    };
  }

  @Test(dataProvider = "percentValidProvider")
  public void percentValid(String raw, Map<String, Object> expected, String description) {
    test(raw, expected);
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
        {"key=val%F0%GG%98%80ue", "value + 4-bytes invalid percent sequence (non-hex character in second)"}
    };
  }

  @Test(dataProvider = "percentInvalidProvider")
  public void percentInvalid(String raw, String description) {
    badRequest(
        """
        GET /path?%s HTTP/1.1\r
        Host: test\r
        \t
        """.formatted(raw)
    );
  }

  @DataProvider
  public Object[][] rawQueryProvider() {
    final List<Object[]> l;
    l = new ArrayList<>();

    for (int value = 0; value < 128; value++) {
      switch (value) {
        // these are valid but have special meaning
        case '&', '=', '+' -> rawQuery(l, value);

        default -> {
          final String rawKey;
          rawKey = "/raw?%%%02X=value".formatted(value);

          final String rawValue;
          rawValue = "/raw?key=%%%02X".formatted(value);

          if (validBytes[value]) {
            l.add(new Object[] {rawKey, "/raw?" + (char) value + "=value"});
            l.add(new Object[] {rawValue, "/raw?key=" + (char) value});
          } else {
            rawQuery(l, value);
          }
        }
      }
    }

    return l.toArray(Object[][]::new);
  }

  private void rawQuery(List<Object[]> l, int value) {
    final String rawKey;
    rawKey = "/raw?%%%02X=value".formatted(value);

    final String rawValue;
    rawValue = "/raw?key=%%%02X".formatted(value);

    l.add(new Object[] {rawKey, rawKey});
    l.add(new Object[] {rawValue, rawValue});
  }

  @Test(dataProvider = "rawQueryProvider")
  public void rawQuery(String raw, String expected) {
    exec(test -> {
      test.bufferSize(256, 512);

      test.xch(xch -> {
        xch.req("""
        GET /path?%s HTTP/1.1\r
        Host: test\r
        \r
        """.formatted(raw));

        xch.handler(http -> {
          assertEquals(http.rawQuery(), expected);

          http.ok(Media.Bytes.textPlain("OK"));
        });

        xch.resp("""
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 2\r
        \r
        OK""");
      });
    });
  }

  @DataProvider
  public Object[][] ioExceptionProvider() {
    return new Object[][] {
        {"GET /path?ke", "Thrown while parsing key"},
        {"GET /path?k%7", "Thrown while parsing key (encoded)"},
        {"GET /path?key=v", "Thrown while parsing value"},
        {"GET /path?key=v%7", "Thrown while parsing value (encoded)"}
    };
  }

  @Test(dataProvider = "ioExceptionProvider")
  public void ioException(String req, String description) {
    exec(test -> {
      test.bufferSize(256, 512);

      test.xch(xch -> {
        xch.req(req);
        xch.req(new IOException(), 1);
        xch.shouldHandle(false);
        xch.resp("");
      });
    });
  }

  @Test
  public void uriTooLong() {
    exec(test -> {
      test.bufferSize(256, 512);

      test.xch(xch -> {
        final String veryLongValue;
        veryLongValue = "ba7f9045".repeat(200);

        xch.req("GET /entity?hash=" + veryLongValue + " HTTP/1.1\r\nHost: www.example.com\r\n\r\n");

        xch.shouldHandle(false);

        xch.resp("""
        HTTP/1.1 414 URI Too Long\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Length: 0\r
        Connection: close\r
        \r
        """);
      });
    });
  }

  private void badRequest(Object request) {
    exec(test -> {
      test.bufferSize(256, 512);

      test.xch(xch -> {
        xch.req(request);

        xch.shouldHandle(false);

        xch.resp("""
        HTTP/1.1 400 Bad Request\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 22\r
        Connection: close\r
        \r
        Invalid request line.
        """);
      });
    });
  }

  private void test(String queryString, Map<String, Object> expected) {
    exec(test -> {
      test.bufferSize(256, 512);

      test.xch(xch -> {
        xch.req("""
        GET /path?%s HTTP/1.1\r
        Host: test\r
        \r
        """.formatted(queryString));

        xch.handler(http -> {
          assertEquals(http.queryParamNames(), expected.keySet());

          for (var entry : expected.entrySet()) {
            final String key;
            key = entry.getKey();

            final Object value;
            value = entry.getValue();

            if (value instanceof String s) {
              assertEquals(http.queryParam(key), s, key);
              assertEquals(http.queryParamAll(key), List.of(s));
            }

            else {
              List<?> list = (List<?>) value;
              assertEquals(http.queryParam(key), list.get(0), key);
              assertEquals(http.queryParamAll(key), value, key);
            }
          }

          http.ok(Media.Bytes.textPlain("OK"));
        });

        xch.resp("""
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 2\r
        \r
        OK""");
      });
    });
  }

}