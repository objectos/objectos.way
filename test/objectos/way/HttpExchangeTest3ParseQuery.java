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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import org.testng.annotations.Test;

public class HttpExchangeTest3ParseQuery {

  @Test(description = "query: empty")
  public void query0() throws IOException {
    test(
        "",

        Map.of()
    );
  }

  @Test(description = "query: one")
  public void query1Test01() throws IOException {
    test(
        "key=value",

        Map.of("key", "value")
    );
  }

  @Test(description = "query: one + empty key")
  public void query1Test02() throws IOException {
    test(
        "=value",

        Map.of("", "value")
    );
  }

  @Test(description = "query: one + empty value")
  public void query1Test03() throws IOException {
    test(
        "key=",

        Map.of("key", "")
    );
  }

  @Test(description = "query: one + empty value + no equals")
  public void query1Test04() throws IOException {
    test(
        "key",

        Map.of("key", "")
    );
  }

  @Test(description = "query: two")
  public void query2Test01() throws IOException {
    test(
        "key1=value1&key2=value2",

        Map.of("key1", "value1", "key2", "value2")
    );
  }

  @Test(description = "query: two + empty key1")
  public void query2Test02() throws IOException {
    test(
        "=value1&key2=value2",

        Map.of("", "value1", "key2", "value2")
    );
  }

  @Test(description = "query: two + empty key2")
  public void query2Test03() throws IOException {
    test(
        "key1=value1&=value2",

        Map.of("key1", "value1", "", "value2")
    );
  }

  @Test(description = "query: two + empty value1")
  public void query2Test04() throws IOException {
    test(
        "key1=&key2=value2",

        Map.of("key1", "", "key2", "value2")
    );
  }

  @Test(description = "query: two + empty value2")
  public void query2Test05() throws IOException {
    test(
        "key1=value1&key2=",

        Map.of("key1", "value1", "key2", "")
    );
  }

  @Test(description = "query: two + empty value1 + no equals")
  public void query2Test06() throws IOException {
    test(
        "key1&key2=value2",

        Map.of("key1", "", "key2", "value2")
    );
  }

  @Test(description = "query: two + empty value2 + no equals")
  public void query2Test07() throws IOException {
    test(
        "key1=value1&key2",

        Map.of("key1", "value1", "key2", "")
    );
  }

  @Test(description = "query: two + duplicate keys")
  public void query2Test08() throws IOException {
    test(
        "key=value1&key=value2",

        Map.of("key", List.of("value1", "value2"))
    );
  }

  @Test(description = "percent: 1-byte + key")
  public void percent1Test01() throws IOException {
    test(
        "k%7D=value",

        Map.of("k}", "value")
    );
  }

  @Test(description = "percent: 1-byte + value")
  public void percent1Test02() throws IOException {
    test(
        "key=va%7Dl",

        Map.of("key", "va}l")
    );
  }

  @Test(description = "percent: 1-byte + key + value")
  public void percent1Test03() throws IOException {
    test(
        "%5E=%3D",

        Map.of("^", "=")
    );
  }

  @Test(description = "percent: 2-byte + key")
  public void percent2Test01() throws IOException {
    test(
        "k%C2%A0=value",

        Map.of("k\u00A0", "value")
    );
  }

  @Test(description = "percent: 2-byte + value")
  public void percent2Test02() throws IOException {
    test(
        "key=va%C3%91l",

        Map.of("key", "vaÑl")
    );
  }

  @Test(description = "percent: 2-byte + key + value")
  public void percent2Test03() throws IOException {
    test(
        "%C2%BF=%C3%80",

        Map.of("¿", "À")
    );
  }

  @Test(description = "percent: 3-byte + key")
  public void percent3Test01() throws IOException {
    test(
        "k%E2%80%8B=value",

        Map.of("k\u200B", "value")
    );
  }

  @Test(description = "percent: 3-byte + value")
  public void percent3Test02() throws IOException {
    test(
        "key=va%E2%82%ACl",

        Map.of("key", "va€l")
    );
  }

  @Test(description = "percent: 3-byte + key + value")
  public void percent3Test03() throws IOException {
    test(
        "%E2%98%83=%E2%9C%93",

        Map.of("☃", "✓")
    );
  }

  @Test(description = "percent: 4-byte + key")
  public void percent4Test01() throws IOException {
    test(
        "k%F0%9F%98%80=value",

        Map.of("k😀", "value")
    );
  }

  @Test(description = "percent: 4-byte + value")
  public void percent4Test02() throws IOException {
    test(
        "key=va%F0%9F%8C%8Al",

        Map.of("key", "va🌊l")
    );
  }

  @Test(description = "percent: 4-byte + key + value")
  public void percent4Test03() throws IOException {
    test(
        "%F0%9F%90%8C=%F0%9F%8D%8F",

        Map.of("🐌", "🍏")
    );
  }

  private static final boolean[] VALID_BYTES;

  static {
    final boolean[] valid;
    valid = new boolean[256];

    final String validString;
    validString = Http.unreserved() + Http.subDelims() + ":@/?";

    for (int idx = 0, len = validString.length(); idx < len; idx++) {
      final char c;
      c = validString.charAt(idx);

      valid[c] = true;
    }

    // space is not valid per se, but will cause parsing to move to VERSION.
    valid[' '] = true;
    valid['\r'] = true;
    valid['\n'] = true;

    VALID_BYTES = valid;
  }

  @Test(description = "bad request: key + invalid byte value")
  public void badRequest01() throws IOException {
    for (int value = 0; value < VALID_BYTES.length; value++) {
      if (!VALID_BYTES[value]) {
        final ByteArrayOutputStream out;
        out = new ByteArrayOutputStream();

        out.write(ascii("GET /character?k"));
        out.write((byte) value);
        out.write(ascii("y=value HTTP/1.1\r\n\r\n"));

        badRequest(out.toByteArray());
      }
    }
  }

  @Test(description = "bad request: value + invalid byte value")
  public void badRequest02() throws IOException {
    for (int value = 0; value < VALID_BYTES.length; value++) {
      if (!VALID_BYTES[value]) {
        final ByteArrayOutputStream out;
        out = new ByteArrayOutputStream();

        out.write(ascii("GET /character?key=va"));
        out.write((byte) value);
        out.write(ascii("lue HTTP/1.1\r\n\r\n"));

        badRequest(out.toByteArray());
      }
    }
  }

  @Test(description = "bad request: key + invalid percent sequence")
  public void badRequest04() throws IOException {
    badRequest("""
    GET /bad?key%xz=value HTTP/1.1\r
    \r
    """);
  }

  @Test(description = "bad request: value + invalid percent sequence")
  public void badRequest05() throws IOException {
    badRequest("""
    GET /bad?key=val%xxue HTTP/1.1\r
    \r
    """);
  }

  @Test(description = "bad request: key + 2-bytes invalid percent sequence (last)")
  public void badRequest06() throws IOException {
    badRequest("""
    GET /bad?k%C3%XZy=value HTTP/1.1\r
    \r
    """);
  }

  @Test(description = "bad request: value + 2-bytes invalid percent sequence (last)")
  public void badRequest07() throws IOException {
    badRequest("""
    GET /bad?key=val%C3%XZue HTTP/1.1\r
    \r
    """);
  }

  @Test(description = "bad request: key + 3-bytes invalid percent sequence (last)")
  public void badRequest08() throws IOException {
    badRequest("""
    GET /bad?k%E2%80%XZy=value HTTP/1.1\r
    \r
    """);
  }

  @Test(description = "bad request: value + 3-bytes invalid percent sequence (last)")
  public void badRequest09() throws IOException {
    badRequest("""
    GET /bad?key=val%E2%80%XZue HTTP/1.1\r
    \r
    """);
  }

  @Test(description = "bad request: key + 3-bytes invalid percent sequence (second)")
  public void badRequest10() throws IOException {
    badRequest("""
    GET /bad?k%E2%XZ%8By=value HTTP/1.1\r
    \r
    """);
  }

  @Test(description = "bad request: value + 3-bytes invalid percent sequence (second)")
  public void badRequest11() throws IOException {
    badRequest("""
    GET /bad?key=val%E2%XZ%8Bue HTTP/1.1\r
    \r
    """);
  }

  @Test(description = "bad request: key + 4-bytes invalid percent sequence (second)")
  public void badRequest12() throws IOException {
    badRequest("""
    GET /bad?k%F0%XZ%98%80=value HTTP/1.1\r
    \r
    """);
  }

  @Test(description = "bad request: value + 4-bytes invalid percent sequence (second)")
  public void badRequest13() throws IOException {
    badRequest("""
    GET /bad?key=val%F0%XZ%98%80ue HTTP/1.1\r
    \r
    """);
  }

  @Test(description = "bad request: key + 4-bytes invalid percent sequence (third)")
  public void badRequest14() throws IOException {
    badRequest("""
    GET /bad?k%F0%9F%XZ%80=value HTTP/1.1\r
    \r
    """);
  }

  @Test(description = "bad request: value + 4-bytes invalid percent sequence (third)")
  public void badRequest15() throws IOException {
    badRequest("""
    GET /bad?key=val%F0%9F%XZ%80ue HTTP/1.1\r
    \r
    """);
  }

  @Test(description = "bad request: key + 4-bytes invalid percent sequence (fourth)")
  public void badRequest16() throws IOException {
    badRequest("""
    GET /bad?k%F0%9F%98%XZ=value HTTP/1.1\r
    \r
    """);
  }

  @Test(description = "bad request: value + 4-bytes invalid percent sequence (fourth)")
  public void badRequest17() throws IOException {
    badRequest("""
    GET /bad?key=val%F0%9F%98%XZue HTTP/1.1\r
    \r
    """);
  }

  @Test
  public void uriTooLong() throws IOException {
    final String veryLongValue;
    veryLongValue = "ba7f9045".repeat(200);

    final Socket socket;
    socket = Y.socket("GET /entity?hash=" + veryLongValue + " HTTP/1.1\r\nHost: www.example.com\r\n\r\n");

    try (HttpExchange http = new HttpExchange(socket, 256, 512, TestingClock.FIXED, TestingNoteSink.INSTANCE)) {
      assertEquals(http.shouldHandle(), false);

      assertEquals(
          http.toString(),

          """
          HTTP/1.1 414 URI Too Long\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Content-Length: 0\r
          Connection: close\r
          \r
          """
      );
    }
  }

  private byte[] ascii(String s) {
    return s.getBytes(StandardCharsets.US_ASCII);
  }

  private void badRequest(Object request) throws IOException {
    final Socket socket;
    socket = Y.socket(request);

    try (HttpExchange http = new HttpExchange(socket, 256, 512, TestingClock.FIXED, TestingNoteSink.INSTANCE)) {
      assertEquals(http.shouldHandle(), false);

      assertEquals(
          http.toString(),

          """
          HTTP/1.1 400 Bad Request\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Content-Type: text/plain; charset=utf-8\r
          Content-Length: 22\r
          Connection: close\r
          \r
          Invalid request line.
          """
      );
    }
  }

  private void test(String queryString, Map<String, Object> expected) throws IOException {
    final String request = """
    GET /path?%s HTTP/1.1\r
    Host: www.example.com\r
    \r
    """.formatted(queryString);

    final Socket socket;
    socket = Y.socket(request);

    try (HttpExchange http = new HttpExchange(socket, 256, 512, TestingClock.FIXED, TestingNoteSink.INSTANCE)) {
      assertEquals(http.shouldHandle(), true);

      assertEquals(http.queryParamNames(), expected.keySet());

      for (var entry : expected.entrySet()) {
        final String key;
        key = entry.getKey();

        final Object value;
        value = entry.getValue();

        if (value instanceof String s) {
          assertEquals(http.queryParam(key), s, key);
        }

        else {
          assertEquals(http.queryParamAll(key), value, key);
        }
      }
    }
  }

}