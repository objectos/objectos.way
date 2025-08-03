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
import java.io.UncheckedIOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.util.List;
import java.util.Map;
import org.testng.annotations.Test;

public class HttpExchangeTest5ParseHeaders extends HttpExchangeTest {

  private final Http.HeaderName foo = Http.HeaderName.of("foo");

  @Test(description = "name: happy-path")
  public void name01() {
    test(
        List.of(
            "Accept-Encoding: x",
            "Referer: x",
            "User-Agent: x"
        ),

        Map.of(
            Http.HeaderName.ACCEPT_ENCODING, "x",
            Http.HeaderName.HOST, "x",
            Http.HeaderName.REFERER, "x",
            Http.HeaderName.USER_AGENT, "x"
        )
    );
  }

  @Test(description = "name: unknown")
  public void name02() {
    test(
        List.of(
            "Accept-Encoding: x",
            "Foo: x",
            "User-Agent: x"
        ),

        Map.of(
            Http.HeaderName.HOST, "x",
            foo, "x",
            Http.HeaderName.USER_AGENT, "x"
        )
    );
  }

  private final String tokenChars = Http.tchar();

  @Test(description = "name: test all valid characters")
  public void name03() {
    test(
        List.of(tokenChars + ": x"),

        Map.of(
            Http.HeaderName.of(tokenChars), "x"
        )
    );
  }

  @Test(description = "name: any invalid character")
  public void name04() throws IOException {
    final boolean[] valid;
    valid = new boolean[256];

    for (int idx = 0, len = tokenChars.length(); idx < len; idx++) {
      final char validChar;
      validChar = tokenChars.charAt(idx);

      valid[validChar] = true;
    }

    // not valid but it is the valid separator
    valid[':'] = true;

    for (int c = 0; c < 0xFF; c++) {
      if (!valid[c]) {
        final ByteArrayOutputStream out;
        out = new ByteArrayOutputStream();

        out.write(ascii("GET / HTTP/1.1\r\n"));
        out.write(ascii("Invali"));
        out.write((byte) c);
        out.write(ascii(": value\r\n\r\n"));

        badRequest(out.toByteArray());
      }
    }
  }

  @Test(description = "name: too large")
  public void name05() {
    final String tooLong;
    tooLong = "Abc".repeat(200);

    requestHeaderFieldsTooLarge("""
    GET / HTTP/1.1\r
    Host: www.example.com\r
    %s: x\r
    \r
    """.formatted(tooLong));
  }

  @Test(description = "name: all predefined names")
  public void name06() {
    StringBuilder req;
    req = new StringBuilder();

    req.append("GET / HTTP/1.1\r\n");

    for (HttpHeaderName name : HttpHeaderName.VALUES) {
      if (!name.isResponseOnly() && !name.equals(HttpHeaderName.TRANSFER_ENCODING)) {
        req.append(name.headerCase());
        req.append(": ");
        req.append(Integer.toString(name.index()));
        req.append("\r\n");
      }
    }

    req.append("\r\n");

    final int contentLength;
    contentLength = HttpHeaderName.CONTENT_LENGTH.index();

    req.append("x".repeat(contentLength));

    Socket socket;
    socket = Y.socket(req.toString());

    try (HttpExchange http = Y.http(socket, 128, 256, Clock.systemDefaultZone(), Y.noteSink(), Long.MAX_VALUE)) {
      assertEquals(http.shouldHandle(), true);

      // headers
      for (HttpHeaderName name : HttpHeaderName.VALUES) {
        if (!name.isResponseOnly() && !name.equals(HttpHeaderName.TRANSFER_ENCODING)) {
          assertEquals(http.header(name), Integer.toString(name.index()));
        }
      }
    } catch (IOException e) {
      throw new AssertionError("Failed with IOException", e);
    }
  }

  @Test(description = "name: slow client")
  public void name07() {
    final List<String> headers;
    headers = List.of(
        "Accept-Encoding: x",
        "Referer: x",
        "User-Agent: x"
    );

    final String req;
    req = req(headers);

    test(
        Y.slowStream(1, req),

        Map.of(
            Http.HeaderName.ACCEPT_ENCODING, "x",
            Http.HeaderName.HOST, "x",
            Http.HeaderName.REFERER, "x",
            Http.HeaderName.USER_AGENT, "x"
        )
    );
  }

  @Test(description = "value: happy path")
  public void value01() {
    test(
        List.of(
            "Accept-Encoding: gzip",
            "Referer: www.google.com",
            "User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/135.0.0.0 Safari/537.36"
        ),

        Map.of(
            Http.HeaderName.ACCEPT_ENCODING, "gzip",
            Http.HeaderName.HOST, "x",
            Http.HeaderName.REFERER, "www.google.com",
            Http.HeaderName.USER_AGENT, "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/135.0.0.0 Safari/537.36"
        )
    );
  }

  @Test(description = "value: trim")
  public void value02() {
    test(
        List.of(
            "Accept-Encoding: x ",
            "Referer:  x  y ",
            "User-Agent:   \t z\t   "
        ),

        Map.of(
            Http.HeaderName.ACCEPT_ENCODING, "x",
            Http.HeaderName.HOST, "x",
            Http.HeaderName.REFERER, "x  y",
            Http.HeaderName.USER_AGENT, "z"
        )
    );
  }

  @Test(description = "value: empty")
  public void value03() {
    test(
        List.of(
            "Accept-Encoding: ",
            "Referer:",
            "User-Agent:   \t"
        ),

        Map.of(
            Http.HeaderName.ACCEPT_ENCODING, "",
            Http.HeaderName.HOST, "x",
            Http.HeaderName.REFERER, "",
            Http.HeaderName.USER_AGENT, ""
        )
    );
  }

  @Test(description = "value: all valid characters")
  public void value04() {
    final StringBuilder chars;
    chars = new StringBuilder();

    for (char c = 0x21; c < 0x7F; c++) {
      chars.append(c);
    }

    // SP and HTAB are valid if not leading or trailing
    chars.insert(10, ' ');
    chars.insert(20, '\t');

    final String value;
    value = chars.toString();

    test(
        List.of(
            "Referer: " + value
        ),

        Map.of(
            Http.HeaderName.REFERER, value
        )
    );
  }

  @Test(description = "value: all invalid characters")
  public void value05() throws IOException {
    final boolean valid[];
    valid = new boolean[256];

    for (char c = 0x21; c < 0x7F; c++) {
      // visible are valid
      valid[c] = true;
    }

    // SP and HTAB are valid
    valid[' '] = true;
    valid['\t'] = true;

    // LF is not valid but will trigger line terminator error
    valid['\n'] = true;

    for (int b = 0; b < valid.length; b++) {
      if (!valid[b]) {
        final ByteArrayOutputStream out;
        out = new ByteArrayOutputStream();

        out.write(ascii("GET / HTTP/1.1\r\n"));
        out.write(ascii("Referer: va"));
        out.write(b);
        out.write(ascii("lue\r\n\r\n"));

        badRequest(out.toByteArray());
      }
    }
  }

  @Test(description = "value: too large")
  public void value06() {
    final String tooLong;
    tooLong = "Abc".repeat(200);

    requestHeaderFieldsTooLarge("""
    GET / HTTP/1.1\r
    Host: www.example.com\r
    Referer: %s\r
    \r
    """.formatted(tooLong));
  }

  @Test(description = "value: slow client")
  public void value07() {
    final List<String> headers;
    headers = List.of(
        "Accept-Encoding: gzip",
        "Referer: www.google.com",
        "User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/135.0.0.0 Safari/537.36"
    );

    final String req;
    req = req(headers);

    test(
        Y.slowStream(1, req),

        Map.of(
            Http.HeaderName.ACCEPT_ENCODING, "gzip",
            Http.HeaderName.HOST, "x",
            Http.HeaderName.REFERER, "www.google.com",
            Http.HeaderName.USER_AGENT, "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/135.0.0.0 Safari/537.36"
        )
    );
  }

  private byte[] ascii(String s) {
    return s.getBytes(StandardCharsets.US_ASCII);
  }

  private void badRequest(Object request) {
    final Socket socket;
    socket = Y.socket(request);

    try (HttpExchange http = Y.http(socket, 256, 512, Y.clockFixed(), Y.noteSink(), 0L)) {
      assertEquals(http.shouldHandle(), false);

      assertEquals(
          Y.toString(socket),

          """
          HTTP/1.1 400 Bad Request\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Content-Type: text/plain; charset=utf-8\r
          Content-Length: 25\r
          Connection: close\r
          \r
          Invalid request headers.
          """
      );
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private void requestHeaderFieldsTooLarge(Object request) {
    final Socket socket;
    socket = Y.socket(request);

    try (HttpExchange http = Y.http(socket, 256, 512, Y.clockFixed(), Y.noteSink(), 0L)) {
      assertEquals(http.shouldHandle(), false);

      assertEquals(
          Y.toString(socket),

          """
          HTTP/1.1 431 Request Header Fields Too Large\r
          Date: Wed, 28 Jun 2023 12:08:43 GMT\r
          Content-Length: 0\r
          Connection: close\r
          \r
          """
      );
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private String req(List<String> headers) {
    final StringBuilder request;
    request = new StringBuilder();

    request.append("GET / HTTP/1.1\r\n");
    request.append("Host: x\r\n");

    headers.stream().map(s -> s + "\r\n").forEach(request::append);

    request.append("\r\n");

    return request.toString();
  }

  private void test(List<String> headers, Map<Http.HeaderName, Object> expected) {
    final String req;
    req = req(headers);

    test(req, expected);
  }

  private void test(Object request, Map<Http.HeaderName, Object> expected) {
    exec(test -> {
      test.bufferSize(256, 256);

      test.xch(xch -> {
        xch.req(request);

        xch.handler(http -> {
          for (var entry : expected.entrySet()) {
            final Http.HeaderName name;
            name = entry.getKey();

            final Object value;
            value = entry.getValue();

            if (value instanceof String s) {
              assertEquals(http.header(name), s);
            }

            else {
              throw new UnsupportedOperationException("Implement me");
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
        OK\
        """);
      });
    });
  }

}