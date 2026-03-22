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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import objectos.http.HttpRequestParser.InvalidRequestLine;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HttpRequestTest2ParseQuery {

  @Test
  public void noQuery() throws IOException {
    final HttpRequest req;
    req = HttpRequestTester.parse(
        test -> test.bufferSize(256, 512),

        """
        GET /path HTTP/1.1\r
        Host: test\r
        \r
        """
    );

    assertEquals(req.queryParam("foo"), null);
    assertEquals(req.queryParamAll("foo"), List.of());
    assertEquals(req.queryParamNames(), Set.of());
  }

  private final boolean[] validBytes = queryValidBytes();

  private boolean[] queryValidBytes() {
    final boolean[] valid;
    valid = new boolean[256];

    final String validString;
    validString = Http.unreserved() + Http.subDelims() + ":@/?";

    for (int idx = 0, len = validString.length(); idx < len; idx++) {
      final char c;
      c = validString.charAt(idx);

      valid[c] = true;
    }

    return valid;
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
  public void queryValid(String raw, Map<String, Object> expected, String description) throws IOException {
    final HttpRequest req;
    req = HttpRequestTester.parse(
        test -> test.bufferSize(256, 512),

        """
        GET /path?%s HTTP/1.1\r
        Host: test\r
        \r
        """.formatted(raw)
    );

    queryAssert(req, expected);
  }

  @DataProvider
  public Object[][] queryInvalidProvider() {
    final List<Object[]> l;
    l = new ArrayList<>();

    for (int value = 0; value < validBytes.length; value++) {
      switch (value) {
        case ' ' -> {
          // will cause parsing to move to VERSION
        }

        case '\n', '\r' -> {
          // will trigger 505 not 400
        }

        case '%' -> {
          // tested in invalid percent
        }

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
  public void queryInvalid(String raw, String description) throws IOException {
    try {
      HttpRequestTester.parse(
          test -> test.bufferSize(256, 512),

          """
          GET /path?%s HTTP/1.1\r
          Host: test\r
          \r
          """.formatted(raw).getBytes(StandardCharsets.ISO_8859_1)
      );

      Assert.fail("It should have thrown");
    } catch (HttpClientException expected) {
      assertEquals(expected.kind, InvalidRequestLine.QUERY_CHAR);
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
        {"%F0%9F%90%8C=%F0%9F%8D%8F", Map.of("🐌", "🍏"), "percent: 4-byte + key + value"}
    };
  }

  @Test(dataProvider = "percentValidProvider")
  public void percentValid(String raw, Map<String, Object> expected, String description) throws IOException {
    final HttpRequest req;
    req = HttpRequestTester.parse(
        test -> test.bufferSize(256, 512),

        """
        GET /path?%s HTTP/1.1\r
        Host: test\r
        \r
        """.formatted(raw)
    );

    queryAssert(req, expected);
  }

  private Object[] arr(Object... arr) {
    // not safe, oh well...
    return arr;
  }

  private void queryAssert(HttpRequest http, Map<String, Object> expected) {
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
  }

}