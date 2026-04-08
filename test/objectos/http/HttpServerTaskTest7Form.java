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
import static org.testng.Assert.assertFalse;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import objectos.way.Media;
import objectos.way.Y;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HttpServerTaskTest7Form {

  private final boolean[] validBytes = HttpY.queryValidBytes();

  @DataProvider
  public Object[][] appFormValidProvider() {
    return HttpY.queryValidProvider();
  }

  @Test(dataProvider = "appFormValidProvider")
  public void appFormValid(String payload, Map<String, Object> expected, String description) {
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = Y.socket("""
          POST / HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          Content-Type: application/x-www-form-urlencoded\r
          Content-Length: %d\r
          \r
          %s\
          """.formatted(payload.length(), payload));

          opts.handler = http -> {
            final HttpExchange0 exchange;
            exchange = (HttpExchange0) http;

            final HttpRequest0 request;
            request = exchange.request();

            final HttpRequestBody0 body;
            body = request.body();

            assertEquals(body.formParams(), expected);

            http.ok(Media.Bytes.textPlain("OK\n"));
          };
        }),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 3\r
        \r
        OK
        """
    );
  }

  @Test
  public void appFormValidWithFile() {
    final Path directory;
    directory = Y.nextTempDir();

    final String filename;
    filename = "%019d".formatted(123L);

    final Path file;
    file = directory.resolve(filename);

    final String value;
    value = ".".repeat(1536);

    final String payload;
    payload = "key=" + value;

    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = Y.socket("""
          POST / HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          Content-Type: application/x-www-form-urlencoded\r
          Content-Length: %d\r
          \r
          %s\
          """.formatted(payload.length(), payload));

          opts.bodyDirectory = directory;

          opts.handler = http -> {
            final HttpExchange0 exchange;
            exchange = (HttpExchange0) http;

            final HttpRequest0 request;
            request = exchange.request();

            final HttpRequestBody0 body;
            body = request.body();

            assertEquals(body.formParams(), Map.of("key", value));

            http.ok(Media.Bytes.textPlain("OK\n"));
          };
        }),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 3\r
        \r
        OK
        """
    );

    assertFalse(Files.exists(file));
  }

  @Test
  public void appFormValidWithFileSlow() {
    final Path directory;
    directory = Y.nextTempDir();

    final String filename;
    filename = "%019d".formatted(123L);

    final Path file;
    file = directory.resolve(filename);

    final String value;
    value = ".".repeat(1536);

    final String payload;
    payload = "key=" + value;

    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = Y.socket(Y.slowStream(1, """
          POST / HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          Content-Type: application/x-www-form-urlencoded\r
          Content-Length: %d\r
          \r
          %s\
          """.formatted(payload.length(), payload)));

          opts.bodyDirectory = directory;

          opts.handler = http -> {
            final HttpExchange0 exchange;
            exchange = (HttpExchange0) http;

            final HttpRequest0 request;
            request = exchange.request();

            final HttpRequestBody0 body;
            body = request.body();

            assertEquals(body.formParams(), Map.of("key", value));

            http.ok(Media.Bytes.textPlain("OK\n"));
          };
        }),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 3\r
        \r
        OK
        """
    );

    assertFalse(Files.exists(file));
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

    return HttpY.arr(key + "=value", "key contains the " + Integer.toHexString(value) + " invalid byte");
  }

  private Object[] appFormInvalidValue(int value) {
    final String val;
    val = Character.toString(value);

    return HttpY.arr("key=" + val, "value contains the " + Integer.toHexString(value) + " invalid byte");
  }

  @Test(dataProvider = "appFormInvalidProvider")
  public void appFormInvalid(String payload, String description) {
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = Y.socket(
              """
              POST / HTTP/1.1\r
              Host: www.example.com\r
              Connection: close\r
              Content-Type: application/x-www-form-urlencoded\r
              Content-Length: %d\r
              \r
              %s\
              """.formatted(payload.length(), payload).getBytes(StandardCharsets.ISO_8859_1)
          );
        }),

        """
        HTTP/1.1 400 Bad Request\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Connection: close\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 67\r
        \r
        Invalid application/x-www-form-urlencoded content in request body.
        """
    );
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
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = Y.socket("""
          POST / HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          Content-Type: application/x-www-form-urlencoded\r
          Content-Length: %d\r
          \r
          %s\
          """.formatted(payload.length(), payload));

          opts.handler = http -> {
            final HttpExchange0 exchange;
            exchange = (HttpExchange0) http;

            final HttpRequest0 request;
            request = exchange.request();

            final HttpRequestBody0 body;
            body = request.body();

            assertEquals(body.formParams(), expected);

            http.ok(Media.Bytes.textPlain("OK\n"));
          };
        }),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 3\r
        \r
        OK
        """
    );
  }

  @Test(dataProvider = "appFormPercentValidProvider")
  public void appFormValidPercentWithFile(String payload, Map<String, Object> expected, String description) {
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = Y.socket("""
          POST / HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          Content-Type: application/x-www-form-urlencoded\r
          Content-Length: %d\r
          \r
          %s\
          """.formatted(payload.length(), payload));

          opts.bodyDirectory = Y.nextTempDir();

          opts.bodyMemoryMax = 1;

          opts.handler = http -> {
            final HttpExchange0 exchange;
            exchange = (HttpExchange0) http;

            final HttpRequest0 request;
            request = exchange.request();

            final HttpRequestBody0 body;
            body = request.body();

            assertEquals(body.formParams(), expected);

            http.ok(Media.Bytes.textPlain("OK\n"));
          };
        }),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 3\r
        \r
        OK
        """
    );
  }

  @Test(dataProvider = "appFormPercentValidProvider")
  public void appFormValidPercentWithFileSlow(String payload, Map<String, Object> expected, String description) {
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = Y.socket(Y.slowStream(1, """
          POST / HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          Content-Type: application/x-www-form-urlencoded\r
          Content-Length: %d\r
          \r
          %s\
          """.formatted(payload.length(), payload)));

          opts.bodyDirectory = Y.nextTempDir();

          opts.bodyMemoryMax = 1;

          opts.handler = http -> {
            final HttpExchange0 exchange;
            exchange = (HttpExchange0) http;

            final HttpRequest0 request;
            request = exchange.request();

            final HttpRequestBody0 body;
            body = request.body();

            assertEquals(body.formParams(), expected);

            http.ok(Media.Bytes.textPlain("OK\n"));
          };
        }),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 3\r
        \r
        OK
        """
    );
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
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = Y.socket(
              """
              POST / HTTP/1.1\r
              Host: www.example.com\r
              Connection: close\r
              Content-Type: application/x-www-form-urlencoded\r
              Content-Length: %d\r
              \r
              %s\
              """.formatted(payload.length(), payload).getBytes(StandardCharsets.ISO_8859_1)
          );
        }),

        """
        HTTP/1.1 400 Bad Request\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Connection: close\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 67\r
        \r
        Invalid application/x-www-form-urlencoded content in request body.
        """
    );
  }

  @Test(dataProvider = "appFormPercentInvalidProvider")
  public void appFormPercentInvalidWithFile(String payload, String description) {
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = Y.socket(
              """
              POST / HTTP/1.1\r
              Host: www.example.com\r
              Connection: close\r
              Content-Type: application/x-www-form-urlencoded\r
              Content-Length: %d\r
              \r
              %s\
              """.formatted(payload.length(), payload).getBytes(StandardCharsets.ISO_8859_1)
          );

          opts.bodyDirectory = Y.nextTempDir();

          opts.bodyMemoryMax = 1;
        }),

        """
        HTTP/1.1 400 Bad Request\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Connection: close\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 67\r
        \r
        Invalid application/x-www-form-urlencoded content in request body.
        """
    );
  }

  @Test
  public void getAsInt01() {
    final String payload;
    payload = "vmax=" + Integer.MAX_VALUE + "&vmin=" + Integer.MIN_VALUE + "&text=abc";

    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = Y.socket("""
          POST / HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          Content-Type: application/x-www-form-urlencoded\r
          Content-Length: %d\r
          \r
          %s\
          """.formatted(payload.length(), payload));

          opts.handler = http -> {
            assertEquals(http.formParamNames().size(), 3);
            assertEquals(http.formParamAsInt("vmax", 0), Integer.MAX_VALUE);
            assertEquals(http.formParamAsInt("vmin", 0), Integer.MIN_VALUE);
            assertEquals(http.formParamAsInt("x", 1), 1);
            assertEquals(http.formParamAsInt("text", 1), 1);

            http.ok(Media.Bytes.textPlain("OK\n"));
          };
        }),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 3\r
        \r
        OK
        """
    );
  }

  @Test
  public void getAsLong01() {
    final String payload;
    payload = "vmax=" + Long.MAX_VALUE + "&vmin=" + Long.MIN_VALUE + "&text=abc";

    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = Y.socket("""
          POST / HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          Content-Type: application/x-www-form-urlencoded\r
          Content-Length: %d\r
          \r
          %s\
          """.formatted(payload.length(), payload));

          opts.handler = http -> {
            assertEquals(http.formParamNames().size(), 3);
            assertEquals(http.formParamAsLong("vmax", 0L), Long.MAX_VALUE);
            assertEquals(http.formParamAsLong("vmin", 0L), Long.MIN_VALUE);
            assertEquals(http.formParamAsLong("x", 1L), 1L);
            assertEquals(http.formParamAsLong("text", 1L), 1L);

            http.ok(Media.Bytes.textPlain("OK\n"));
          };
        }),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 3\r
        \r
        OK
        """
    );
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
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = Y.socket("""
          POST / HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          Content-Type: application/x-www-form-urlencoded\r
          Content-Length: %d\r
          \r
          %s\
          """.formatted(payload.length(), payload));

          opts.handler = http -> {
            assertEquals(http.formParamNames(), Set.of("v"));
            assertEquals(http.formParamAllAsInt("v", -1).toArray(), expected);

            http.ok(Media.Bytes.textPlain("OK\n"));
          };
        }),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 3\r
        \r
        OK
        """
    );
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
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = Y.socket("""
          POST / HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          Content-Type: application/x-www-form-urlencoded\r
          Content-Length: %d\r
          \r
          %s\
          """.formatted(payload.length(), payload));

          opts.handler = http -> {
            assertEquals(http.formParamNames(), Set.of("v"));
            assertEquals(http.formParamAllAsLong("v", -1).toArray(), expected);

            http.ok(Media.Bytes.textPlain("OK\n"));
          };
        }),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 3\r
        \r
        OK
        """
    );
  }

}