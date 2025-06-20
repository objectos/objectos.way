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

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HttpExchangeTest7ParseAppForm extends HttpExchangeTest {

  private final boolean[] validBytes = queryValidBytes();

  @DataProvider
  public Object[][] appFormValidProvider() {
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
            l.add(appFormValidKey(value));
            l.add(appFormValidValue(value));
          }
        }
      }
    }

    return l.toArray(Object[][]::new);
  }

  private Object[] appFormValidKey(int value) {
    final String key;
    key = Character.toString(value);

    return arr(key + "=value", Map.of(key, "value"), "key contains the " + Integer.toHexString(value) + " valid byte");
  }

  private Object[] appFormValidValue(int value) {
    final String val;
    val = Character.toString(value);

    return arr("key=" + val, Map.of("key", val), "value contains the " + Integer.toHexString(value) + " valid byte");
  }

  @Test(dataProvider = "appFormValidProvider")
  public void appFormValid(String payload, Map<String, Object> expected, String description) {
    exec(test -> {
      test.bufferSize(128, 256);

      test.xch(xch -> {
        xch.req("""
        POST / HTTP/1.1\r
        Host: Host\r
        Content-Type: application/x-www-form-urlencoded\r
        Content-Length: %d\r
        \r
        %s\
        """.formatted(payload.length(), payload));

        xch.handle(http -> {
          formAssert(http, expected);
        });

        xch.resp(OK_RESP);
      });
    });
  }

  @Test(dataProvider = "appFormValidProvider")
  public void appFormValidWithFile(String payload, Map<String, Object> expected, String description) {
    exec(test -> {
      test.bufferSize(128, 128);

      test.xch(xch -> {
        xch.req(formFileRequest(127, payload));

        final byte[] bytes;
        bytes = payload.getBytes(StandardCharsets.ISO_8859_1);

        xch.req(bytes);

        xch.handle(http -> {
          formAssert(http, expected);
        });

        xch.resp(OK_RESP);
      });
    });
  }

  @Test(dataProvider = "appFormValidProvider")
  public void appFormValidWithFileSlow(String payload, Map<String, Object> expected, String description) {
    exec(test -> {
      test.bufferSize(128, 128);

      test.xch(xch -> {
        xch.req(formFileRequest(127, payload));

        final byte[] bytes;
        bytes = payload.getBytes(StandardCharsets.ISO_8859_1);

        xch.req(Y.slowStream(1, bytes));

        xch.handle(http -> {
          formAssert(http, expected);
        });

        xch.resp(OK_RESP);
      });
    });
  }

  @Test
  public void appFormValidWithFileAndQuery() {
    String query = "k1=v1&k2=v2";
    Map<String, Object> queryExpected = Map.of("k1", "v1", "k2", "v2");
    String payload = "f1=t1&f2=t2";
    Map<String, Object> formExpected = Map.of("f1", "t1", "f2", "t2");

    exec(test -> {
      test.bufferSize(128, 128);

      test.xch(xch -> {
        xch.req(formFileRequestAndQuery(127, query, payload));

        final byte[] bytes;
        bytes = payload.getBytes(StandardCharsets.ISO_8859_1);

        xch.req(bytes);

        xch.handle(http -> {
          queryAssert(http, queryExpected);
          formAssert(http, formExpected);
        });

        xch.resp(OK_RESP);
      });
    });
  }

  @DataProvider
  public Object[][] appFormInvalidProvider() {
    final List<Object[]> l;
    l = new ArrayList<>();

    for (int value = 0; value < validBytes.length; value++) {
      switch (value) {
        case ' ' -> {/* will cause parsing to move to VERSION */}

        case '\n', '\r' -> {/* will trigger 505 not 400 */}

        default -> {
          if (!validBytes[value]) {
            l.add(appFormInvalidKey(value));
            l.add(appFormInvalidValue(value));
          }
        }
      }
    }

    return l.toArray(Object[][]::new);
  }

  private Object[] appFormInvalidKey(int value) {
    final String key;
    key = Character.toString(value);

    return arr(key + "=value", "key contains the " + Integer.toHexString(value) + " invalid byte");
  }

  private Object[] appFormInvalidValue(int value) {
    final String val;
    val = Character.toString(value);

    return arr("key=" + val, "value contains the " + Integer.toHexString(value) + " invalid byte");
  }

  private static final String APP_FORM_BAD_REQUEST = """
  HTTP/1.1 400 Bad Request\r
  Date: Wed, 28 Jun 2023 12:08:43 GMT\r
  Content-Type: text/plain; charset=utf-8\r
  Content-Length: 67\r
  Connection: close\r
  \r
  Invalid application/x-www-form-urlencoded content in request body.
  """;

  @Test(dataProvider = "appFormInvalidProvider")
  public void appFormInvalid(String payload, String description) {
    exec(test -> {
      test.bufferSize(128, 256);

      test.xch(xch -> {
        xch.req("""
        POST / HTTP/1.1\r
        Host: Host\r
        Content-Type: application/x-www-form-urlencoded\r
        Content-Length: %d\r
        \r
        %s\
        """.formatted(payload.length(), payload).getBytes(StandardCharsets.ISO_8859_1));

        xch.shouldHandle(false);

        xch.resp(APP_FORM_BAD_REQUEST);
      });
    });
  }

  @Test(dataProvider = "appFormInvalidProvider")
  public void appFormInvalidWithFile(String payload, String description) {
    exec(test -> {
      test.bufferSize(128, 128);

      test.xch(xch -> {
        xch.req(formFileRequest(127, payload));

        final byte[] bytes;
        bytes = payload.getBytes(StandardCharsets.ISO_8859_1);

        xch.req(bytes);

        xch.shouldHandle(false);

        xch.resp(APP_FORM_BAD_REQUEST);
      });
    });
  }

  @Test(dataProvider = "appFormInvalidProvider")
  public void appFormInvalidWithFileSlow(String payload, String description) {
    exec(test -> {
      test.bufferSize(128, 128);

      test.xch(xch -> {
        xch.req(formFileRequest(127, payload));

        final byte[] bytes;
        bytes = payload.getBytes(StandardCharsets.ISO_8859_1);

        xch.req(Y.slowStream(1, bytes));

        xch.shouldHandle(false);

        xch.resp(APP_FORM_BAD_REQUEST);
      });
    });
  }

  @DataProvider
  public Object[][] appFormPercentValidProvider() {
    return new Object[][] {
        {"k%7D=value", Map.of("k}", "value"), "percent: 1-byte + key"},
        {"k%7D", Map.of("k}", ""), "percent: 1-byte + key (EOF)"},
        {"key=va%7Dl", Map.of("key", "va}l"), "percent: 1-byte + value"},
        {"%5E=%3D", Map.of("^", "="), "percent: 1-byte + key + value"},
        {"k%C2%A0=value", Map.of("k\u00A0", "value"), "percent: 2-byte + key"},
        {"k%C2%A0", Map.of("k\u00A0", ""), "percent: 2-byte + key (EOF)"},
        {"key=va%C3%91l", Map.of("key", "vaÑl"), "percent: 2-byte + value"},
        {"%C2%BF=%C3%80", Map.of("¿", "À"), "percent: 2-byte + key + value"},
        {"k%E2%80%8B=value", Map.of("k\u200B", "value"), "percent: 3-byte + key"},
        {"k%E2%80%8B", Map.of("k\u200B", ""), "percent: 3-byte + key (EOF)"},
        {"key=va%E2%82%ACl", Map.of("key", "va€l"), "percent: 3-byte + value"},
        {"%E2%98%83=%E2%9C%93", Map.of("☃", "✓"), "percent: 3-byte + key + value"},
        {"k%F0%9F%98%80=value", Map.of("k😀", "value"), "percent: 4-byte + key"},
        {"k%F0%9F%98%80", Map.of("k😀", ""), "percent: 4-byte + key (EOF)"},
        {"key=va%F0%9F%8C%8Al", Map.of("key", "va🌊l"), "percent: 4-byte + value"},
        {"%F0%9F%90%8C=%F0%9F%8D%8F", Map.of("🐌", "🍏"), "percent: 4-byte + key + value"}
    };
  }

  @Test(dataProvider = "appFormPercentValidProvider")
  public void appFormPercentValid(String payload, Map<String, Object> expected, String description) {
    exec(test -> {
      test.bufferSize(128, 256);

      test.xch(xch -> {
        xch.req("""
        POST / HTTP/1.1\r
        Host: Host\r
        Content-Type: application/x-www-form-urlencoded\r
        Content-Length: %d\r
        \r
        %s\
        """.formatted(payload.length(), payload));

        xch.handle(http -> {
          formAssert(http, expected);
        });

        xch.resp(OK_RESP);
      });
    });
  }

  @Test(dataProvider = "appFormPercentValidProvider")
  public void appFormValidPercentWithFile(String payload, Map<String, Object> expected, String description) {
    exec(test -> {
      test.bufferSize(128, 128);

      test.xch(xch -> {
        xch.req(formFileRequest(127, payload));

        final byte[] bytes;
        bytes = payload.getBytes(StandardCharsets.ISO_8859_1);

        xch.req(bytes);

        xch.handle(http -> {
          formAssert(http, expected);
        });

        xch.resp(OK_RESP);
      });
    });
  }

  @Test(dataProvider = "appFormPercentValidProvider")
  public void appFormValidPercentWithFileSlow(String payload, Map<String, Object> expected, String description) {
    exec(test -> {
      test.bufferSize(128, 128);

      test.xch(xch -> {
        xch.req(formFileRequest(127, payload));

        final byte[] bytes;
        bytes = payload.getBytes(StandardCharsets.ISO_8859_1);

        xch.req(Y.slowStream(1, bytes));

        xch.handle(http -> {
          formAssert(http, expected);
        });

        xch.resp(OK_RESP);
      });
    });
  }

  @DataProvider
  public Object[][] appFormPercentInvalidProvider() {
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
        {"k%2", "key + 1-byte incomplete percent sequence (single hex digit) (EOF)"},
        {"key=val%2ue", "value + 1-byte incomplete percent sequence (single hex digit)"},

        {"k%C3y=value", "key + 2-bytes incomplete percent sequence (missing second byte)"},
        {"k%C3", "key + 2-bytes incomplete percent sequence (missing second byte) (EOF)"},
        {"key=val%C3ue", "value + 2-bytes incomplete percent sequence (missing second byte)"},
        {"k%80%80y=value", "key + 2-bytes invalid percent sequence (invalid leading byte)"},
        {"key=val%80%80ue", "value + 2-bytes invalid percent sequence (invalid leading byte)"},
        {"k%C3%GGy=value", "key + 2-bytes invalid percent sequence (non-hex character)"},
        {"key=val%C3%GGue", "value + 2-bytes invalid percent sequence (non-hex character)"},

        {"k%E2%80y=value", "key + 3-bytes incomplete percent sequence (missing third byte)"},
        {"k%E2%80", "key + 3-bytes incomplete percent sequence (missing third byte) (EOF)"},
        {"key=val%E2%80ue", "value + 3-bytes incomplete percent sequence (missing third byte)"},
        {"k%E0%80%80y=value", "key + 3-bytes invalid percent sequence (invalid leading byte)"},
        {"key=val%E0%80%80ue", "value + 3-bytes invalid percent sequence (invalid leading byte)"},
        {"k%E2%80%GGy=value", "key + 3-bytes invalid percent sequence (non-hex character in last)"},
        {"key=val%E2%80%GGue", "value + 3-bytes invalid percent sequence (non-hex character in last)"},
        {"k%E2%GG%80y=value", "key + 3-bytes invalid percent sequence (non-hex character in second)"},
        {"key=val%E2 procedimientos%GG%80ue", "value + 3-bytes invalid percent sequence (non-hex character in second)"},

        {"k%F0%9F%98y=value", "key + 4-bytes incomplete percent sequence (missing fourth byte)"},
        {"k%F0%9F%98", "key + 4-bytes incomplete percent sequence (missing fourth byte) (EOF)"},
        {"key=val%F0%9F%98ue", "value + 4-bytes incomplete percent sequence (missing fourth byte)"},
        {"k%F5%80%80%80y=value", "key + 4-bytes invalid percent sequence (invalid leading byte > U+10FFFF)"},
        {"key=val%F5%80%80%80ue", "value + 4-bytes invalid percent sequence (invalid leading byte > U+10FFFF)"},
        {"k%F0%80%GG%80y=value", "key + 4-bytes invalid percent sequence (non-hex character in third)"},
        {"key=val%F0%80%GG%80ue", "value + 4-bytes invalid percent sequence (non-hex character in third)"},
        {"k%F0%GG%98%80y=value", "key + 4-bytes invalid percent sequence (non-hex character in second)"},
        {"key=val%F0%GG%98%80ue", "value + 4-bytes invalid percent sequence (non-hex character in second)"}
    };
  }

  @Test(dataProvider = "appFormPercentInvalidProvider")
  public void appFormPercentInvalid(String payload, String description) {
    exec(test -> {
      test.bufferSize(128, 256);

      test.xch(xch -> {
        xch.req("""
        POST / HTTP/1.1\r
        Host: Host\r
        Content-Type: application/x-www-form-urlencoded\r
        Content-Length: %d\r
        \r
        %s\
        """.formatted(payload.length(), payload).getBytes(StandardCharsets.ISO_8859_1));

        xch.shouldHandle(false);

        xch.resp(APP_FORM_BAD_REQUEST);
      });
    });
  }

  @Test(dataProvider = "appFormPercentInvalidProvider")
  public void appFormPercentInvalidWithFile(String payload, String description) {
    exec(test -> {
      test.bufferSize(128, 128);

      test.xch(xch -> {
        xch.req(formFileRequest(127, payload));

        final byte[] bytes;
        bytes = payload.getBytes(StandardCharsets.ISO_8859_1);

        xch.req(bytes);

        xch.shouldHandle(false);

        xch.resp(APP_FORM_BAD_REQUEST);
      });
    });
  }

  @Test(dataProvider = "appFormPercentInvalidProvider")
  public void appFormPercentInvalidWithFileSlow(String payload, String description) {
    exec(test -> {
      test.bufferSize(128, 128);

      test.xch(xch -> {
        xch.req(formFileRequest(127, payload));

        final byte[] bytes;
        bytes = payload.getBytes(StandardCharsets.ISO_8859_1);

        xch.req(Y.slowStream(1, bytes));

        xch.shouldHandle(false);

        xch.resp(APP_FORM_BAD_REQUEST);
      });
    });
  }

  @Test
  public void getAsInt01() {
    final String payload;
    payload = "vmax=" + Integer.MAX_VALUE + "&vmin=" + Integer.MIN_VALUE + "&text=abc";

    exec(test -> {
      test.bufferSize(128, 256);

      test.xch(xch -> {
        xch.req("""
        POST / HTTP/1.1\r
        Host: Host\r
        Content-Type: application/x-www-form-urlencoded\r
        Content-Length: %d\r
        \r
        %s\
        """.formatted(payload.length(), payload));

        xch.handler(http -> {
          assertEquals(http.formParamNames().size(), 3);
          assertEquals(http.formParamAsInt("vmax", 0), Integer.MAX_VALUE);
          assertEquals(http.formParamAsInt("vmin", 0), Integer.MIN_VALUE);
          assertEquals(http.formParamAsInt("x", 1), 1);
          assertEquals(http.formParamAsInt("text", 1), 1);
          http.ok(OK);
        });

        xch.resp(OK_RESP);
      });
    });
  }

  @Test
  public void getAsLong01() {
    final String payload;
    payload = "vmax=" + Long.MAX_VALUE + "&vmin=" + Long.MIN_VALUE + "&text=abc";

    exec(test -> {
      test.bufferSize(128, 256);

      test.xch(xch -> {
        xch.req("""
        POST / HTTP/1.1\r
        Host: Host\r
        Content-Type: application/x-www-form-urlencoded\r
        Content-Length: %d\r
        \r
        %s\
        """.formatted(payload.length(), payload));

        xch.handler(http -> {
          assertEquals(http.formParamNames().size(), 3);
          assertEquals(http.formParamAsLong("vmax", 0L), Long.MAX_VALUE);
          assertEquals(http.formParamAsLong("vmin", 0L), Long.MIN_VALUE);
          assertEquals(http.formParamAsLong("x", 1L), 1L);
          assertEquals(http.formParamAsLong("text", 1L), 1L);
          http.ok(OK);
        });

        xch.resp(OK_RESP);
      });
    });
  }

  @DataProvider
  public Object[][] getAllAsIntProvider() {
    return new Object[][] {
        {"v=1&v=2&v=3", new int[] {1, 2, 3}},
        {"v=1&v=x&v=3", new int[] {1, -1, 3}}
    };
  }

  @Test(dataProvider = "getAllAsIntProvider")
  public void getAllAsInt(String payload, int[] expected) {
    exec(test -> {
      test.bufferSize(128, 256);

      test.xch(xch -> {
        xch.req("""
        POST / HTTP/1.1\r
        Host: Host\r
        Content-Type: application/x-www-form-urlencoded\r
        Content-Length: %d\r
        \r
        %s\
        """.formatted(payload.length(), payload));

        xch.handler(http -> {
          assertEquals(http.formParamNames(), Set.of("v"));
          assertEquals(http.formParamAllAsInt("v", -1).toArray(), expected);
          http.ok(OK);
        });

        xch.resp(OK_RESP);
      });
    });
  }

  @DataProvider
  public Object[][] getAllAsLongProvider() {
    return new Object[][] {
        {"v=1&v=2&v=3", new long[] {1, 2, 3}},
        {"v=1&v=x&v=3", new long[] {1, -1, 3}}
    };
  }

  @Test(dataProvider = "getAllAsLongProvider")
  public void getAllAsLong(String payload, long[] expected) {
    exec(test -> {
      test.bufferSize(128, 256);

      test.xch(xch -> {
        xch.req("""
        POST / HTTP/1.1\r
        Host: Host\r
        Content-Type: application/x-www-form-urlencoded\r
        Content-Length: %d\r
        \r
        %s\
        """.formatted(payload.length(), payload));

        xch.handler(http -> {
          assertEquals(http.formParamNames(), Set.of("v"));
          assertEquals(http.formParamAllAsLong("v", -1).toArray(), expected);
          http.ok(OK);
        });

        xch.resp(OK_RESP);
      });
    });
  }

  private void formAssert(HttpExchange http, Map<String, Object> expected) {
    assertEquals(http.formParamNames(), expected.keySet());

    for (var entry : expected.entrySet()) {
      final String key;
      key = entry.getKey();

      final Object value;
      value = entry.getValue();

      if (value instanceof String s) {
        assertEquals(http.formParam(key), s, key);
        assertEquals(http.formParamAll(key), List.of(s));
      }

      else {
        List<?> list = (List<?>) value;
        assertEquals(http.formParam(key), list.get(0), key);
        assertEquals(http.formParamAll(key), value, key);
      }
    }

    http.ok(OK);
  }

  @Test
  public void formFileRequestSize() {
    assertEquals(formFileRequest(127, ".".repeat(9)).length(), 127);
    assertEquals(formFileRequest(127, ".".repeat(99)).length(), 127);
    assertEquals(formFileRequest(127, ".".repeat(999)).length(), 127);
    assertEquals(formFileRequestAndQuery(127, "k1=v1&k2=v2", ".".repeat(999)).length(), 127);
  }

  private String formFileRequest(int buffer, String payload) {
    final int length;
    length = payload.length();

    final String contentLength;
    contentLength = Integer.toString(length);

    // leave 1-byte for file buffering
    final int remaining;
    remaining = buffer - 94 - contentLength.length();

    final String host;
    host = "X".repeat(remaining);

    return """
    POST / HTTP/1.1\r
    Host: %s\r
    Content-Type: application/x-www-form-urlencoded\r
    Content-Length: %s\r
    \r
    """.formatted(host, contentLength);
  }

  private String formFileRequestAndQuery(int buffer, String query, String payload) {
    final int length;
    length = payload.length();

    final String contentLength;
    contentLength = Integer.toString(length);

    // leave 1-byte for file buffering
    final int remaining;
    remaining = buffer - 94 - 1 - query.length() - contentLength.length();

    final String host;
    host = "X".repeat(remaining);

    return """
    POST /?%s HTTP/1.1\r
    Host: %s\r
    Content-Type: application/x-www-form-urlencoded\r
    Content-Length: %s\r
    \r
    """.formatted(query, host, contentLength);
  }

}