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
import static org.testng.Assert.assertFalse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HttpExchangeTest6ParseBody extends HttpExchangeTest {

  private static abstract class AbstractTmp implements HttpExchangeTmp {
    boolean closed;

    @Override
    public void close() throws IOException {
      closed = true;
    }

    @Override
    public InputStream input() throws IOException {
      throw new UnsupportedOperationException("Implement me");
    }

    @Override
    public OutputStream output() throws IOException {
      throw new UnsupportedOperationException("Implement me");
    }
  }

  @Test(description = "empty: no content-length")
  public void empty01() {
    test(
        """
        GET / HTTP/1.1\r
        Host: www.example.com\r
        Connection: close\r
        \r
        """,

        Util.EMPTY_BYTE_ARRAY
    );
  }

  @Test(description = "empty: content-length=0")
  public void empty02() {
    test(
        """
        POST / HTTP/1.1\r
        Host: www.example.com\r
        Content-Length: 0\r
        \r
        """,

        Util.EMPTY_BYTE_ARRAY
    );
  }

  @Test(description = "buffer: no read")
  public void buffer01() {
    test(
        """
        POST / HTTP/1.1\r
        Host: www.example.com\r
        Content-Length: 24\r
        Content-Type: application/x-www-form-urlencoded\r
        \r
        email=user%40example.com\
        """,

        ascii("email=user%40example.com")
    );
  }

  @Test(description = "buffer: read")
  public void buffer02() {
    test(
        """
        POST / HTTP/1.1\r
        Host: www.example.com\r
        Content-Length: 24\r
        Content-Type: application/x-www-form-urlencoded\r
        \r
        email=\
        """,

        "user%40example.com",

        ascii("email=user%40example.com")
    );
  }

  @Test(description = "buffer: read + resize")
  public void buffer03() {
    final String frag;
    frag = ".".repeat(100);

    test(
        2, 512,

        arr(
            """
            POST / HTTP/1.1\r
            Host: www.example.com\r
            Content-Type: text/plain\r
            Content-Length: 400\r
            \r
            """,

            frag, frag, frag, frag
        ),

        ascii(frag + frag + frag + frag)
    );
  }

  @Test(description = "buffer: read + IOException")
  public void buffer04() {
    final String frag;
    frag = ".".repeat(16);

    final IOException ioe;
    ioe = Y.trimStackTrace(new IOException(), 1);

    testError(
        256, 512,

        arr(
            """
            POST / HTTP/1.1\r
            Host: www.example.com\r
            Content-Length: 32\r
            Content-Type: text/plain\r
            \r
            """,

            frag, ioe
        ),

        ""
    );
  }

  @Test(description = "buffer: read + resize + IOException")
  public void buffer05() {
    final String frag;
    frag = ".".repeat(128);

    final IOException ioe;
    ioe = Y.trimStackTrace(new IOException(), 1);

    testError(
        2, 512,

        arr(
            """
            POST / HTTP/1.1\r
            Host: www.example.com\r
            Content-Length: 256\r
            Content-Type: text/plain\r
            \r
            """,

            frag, ioe
        ),

        ""
    );
  }

  @Test(description = "buffer: read + EOF")
  public void buffer06() {
    final String frag;
    frag = ".".repeat(32);

    testError(
        256, 512,

        arr(
            """
            POST / HTTP/1.1\r
            Host: www.example.com\r
            Content-Length: 256\r
            Content-Type: text/plain\r
            \r
            """,

            frag
        ),

        ""
    );
  }

  private static class Tester extends HttpExchangeBodyFiles {

    private final Path directory = Y.nextTempDir();

    private Path file;

    @Override
    public final Path file(long id) throws IOException {
      return file = super.file(id);
    }

    @Override
    final Path directory() {
      return directory;
    }

    public final boolean fileExists() {
      return Files.exists(file);
    }

  }

  @Test(description = "file: happy-path")
  public void file01() {
    final Tester tester;
    tester = new Tester();

    final String content;
    content = ".o".repeat(512);

    exec(test -> {
      test.bodyFiles(tester);

      test.bufferSize(2, 256);

      test.xch(xch -> {
        xch.req("""
        POST / HTTP/1.1\r
        Host: www.example.com\r
        Content-Type: text/plain\r
        Content-Length: 1024\r
        \r
        """);

        xch.req(content);

        xch.handler(http -> {
          try (InputStream in = http.bodyInputStream()) {
            assertEquals(in.readAllBytes(), ascii(content));
          } catch (IOException e) {
            throw new UncheckedIOException(e);
          }

          http.ok(OK);
        });

        xch.resp(OK_RESP);
      });
    });

    assertFalse(tester.fileExists());
  }

  @Test(enabled = false, description = "file: client read IOException")
  public void file02() {
    final String frag;
    frag = ".".repeat(512);

    final IOException ioe;
    ioe = Y.trimStackTrace(new IOException(), 1);

    testError(
        128, 256,

        arr(
            """
            POST / HTTP/1.1\r
            Host: www.example.com\r
            Content-Type: text/plain\r
            Content-Length: 1024\r
            \r
            """,

            frag, ioe
        ),

        ""
    );
  }

  @Test(enabled = false, description = "file: ISE on getOutputStream")
  public void file03() {
    final AbstractTmp tmp;
    tmp = new AbstractTmp() {
      @Override
      public final OutputStream output() throws IOException {
        throw Y.trimStackTrace(new IOException(), 1);
      }
    };

    final String contents;
    contents = ".".repeat(1024);

    testFile(
        128, 256,

        tmp,

        arr(
            """
            POST / HTTP/1.1\r
            Host: www.example.com\r
            Content-Type: text/plain\r
            Content-Length: 1024\r
            \r
            """,

            contents
        ),

        """
        HTTP/1.1 500 Internal Server Error\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 82\r
        Connection: close\r
        \r
        The server encountered an internal error and was unable to complete your request.
        """
    );

    assertEquals(tmp.closed, true);
  }

  @Test(enabled = false, description = "file: ISE on OutputStream.write")
  public void file04() {
    final AbstractTmp tmp;
    tmp = new AbstractTmp() {
      @Override
      public final OutputStream output() throws IOException {
        return new OutputStream() {
          @Override
          public final void write(int b) throws IOException {
            throw new UnsupportedOperationException("Implement me");
          }

          @Override
          public final void write(byte[] b, int off, int len) throws IOException {
            throw Y.trimStackTrace(new IOException(), 1);
          }
        };
      }
    };

    final String contents;
    contents = ".".repeat(1024);

    testFile(
        128, 256,

        tmp,

        arr(
            """
            POST / HTTP/1.1\r
            Host: www.example.com\r
            Content-Type: text/plain\r
            Content-Length: 1024\r
            \r
            """,

            contents
        ),

        """
        HTTP/1.1 500 Internal Server Error\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 82\r
        Connection: close\r
        \r
        The server encountered an internal error and was unable to complete your request.
        """
    );

    assertEquals(tmp.closed, true);
  }

  @Test(enabled = false, description = "file: ISE on OutputStream.close")
  public void file05() {
    final AbstractTmp tmp;
    tmp = new AbstractTmp() {
      @Override
      public final OutputStream output() throws IOException {
        return new OutputStream() {
          @Override
          public final void close() throws IOException {
            throw Y.trimStackTrace(new IOException(), 1);
          }

          @Override
          public final void write(int b) throws IOException {}

          @Override
          public final void write(byte[] b, int off, int len) throws IOException {}
        };
      }
    };

    final String contents;
    contents = ".".repeat(1024);

    testFile(
        128, 256,

        tmp,

        arr(
            """
            POST / HTTP/1.1\r
            Host: www.example.com\r
            Content-Type: text/plain\r
            Content-Length: 1024\r
            \r
            """,

            contents
        ),

        """
        HTTP/1.1 500 Internal Server Error\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 82\r
        Connection: close\r
        \r
        The server encountered an internal error and was unable to complete your request.
        """
    );

    assertEquals(tmp.closed, true);
  }

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
    formValid(payload, expected);
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

  @Test(dataProvider = "appFormInvalidProvider")
  public void appFormInvalid(String payload, String description) {
    formInvalid(payload);
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
  public void appFormPercentValid(String raw, Map<String, Object> expected, String description) {
    formValid(raw, expected);
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
  public void appFormPercentInvalid(String raw, String description) {
    formInvalid(raw);
  }

  @Test(enabled = false, dataProvider = "appFormValidProvider")
  public void appFormValidWithFile(String payload, Map<String, Object> expected, String description) {
    formValidWithFile(payload, expected);
  }

  @Test(enabled = false)
  public void appFormValidWithFile01() {
    formValidWithFile("key=value", Map.of("key", "value"));
  }

  private byte[] ascii(String s) {
    return s.getBytes(StandardCharsets.US_ASCII);
  }

  private void test(Object o1, byte[] body) {
    test(256, 512, arr(o1), body);
  }

  private void test(Object o1, Object o2, byte[] body) {
    test(256, 512, arr(o1, o2), body);
  }

  private void test(int initial, int max, Object[] data, byte[] body) {
    exec(test -> {
      test.bufferSize(initial, max);

      test.xch(xch -> {
        for (Object o : data) {
          xch.req(o);
        }

        xch.shouldHandle(true);

        xch.handler(http -> {
          final ByteArrayOutputStream out;
          out = new ByteArrayOutputStream();

          try (InputStream in = http.bodyInputStream()) {
            in.transferTo(out);
          } catch (IOException e) {
            throw new UncheckedIOException(e);
          }

          final byte[] result;
          result = out.toByteArray();

          assertEquals(result, body);

          http.ok(OK);
        });

        xch.resp(OK_RESP);
      });
    });
  }

  private void testError(int initial, int max, Object[] data, String expected) {
    exec(test -> {
      test.bufferSize(initial, max);
      test.xch(xch -> {
        for (Object o : data) {
          xch.req(o);
        }
        xch.shouldHandle(false);
        xch.resp(expected);
      });
    });
  }

  private void testFile(int initial, int max, HttpExchangeTmp tmp, Object[] data, String expected) {
    final Socket socket;
    socket = Y.socket(data);

    try (HttpExchange http = new HttpExchange(socket, initial, max, Y.clockFixed(), TestingNoteSink.INSTANCE)) {
      http.setObject(tmp);

      http.setState(HttpExchange.$PARSE_METHOD);

      assertEquals(http.shouldHandle(), false);

      assertEquals(Y.toString(socket), expected);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
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

  private void formValid(String payload, Map<String, Object> expected) {
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
          formAssert(http, expected);
        });

        xch.resp(OK_RESP);
      });
    });
  }

  @Test
  public void formFileRequestSize() {
    assertEquals(formFileRequest(".".repeat(9)).length(), 127);
    assertEquals(formFileRequest(".".repeat(99)).length(), 127);
    assertEquals(formFileRequest(".".repeat(999)).length(), 127);
  }

  private void formValidWithFile(String payload, Map<String, Object> expected) {
    exec(test -> {
      test.bufferSize(128, 128);

      test.xch(xch -> {
        xch.req(formFileRequest(payload));

        final byte[] bytes;
        bytes = payload.getBytes(StandardCharsets.ISO_8859_1);

        xch.req(bytes);

        xch.handler(http -> {
          formAssert(http, expected);
        });

        xch.resp(OK_RESP);
      });
    });
  }

  private String formFileRequest(String payload) {
    final int length;
    length = payload.length();

    final String contentLength;
    contentLength = Integer.toString(length);

    // leave 1-byte for file buffering
    final int remaining;
    remaining = 127 - 94 - contentLength.length();

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

  private void formInvalid(String payload) {
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

        xch.resp("""
        HTTP/1.1 400 Bad Request\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 67\r
        Connection: close\r
        \r
        Invalid application/x-www-form-urlencoded content in request body.
        """);
      });
    });
  }

}