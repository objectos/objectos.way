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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import objectos.way.Media;
import objectos.way.Y;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HttpServerTaskTest5Headers {

  @DataProvider
  public Object[][] validProvider() {
    return new Object[][] {
        {
            """
            Host: www.example.com\r
            Connection: close\r
            User-Agent: x\r
            """,

            Map.of(
                HttpHeaderName.HOST, "www.example.com",
                HttpHeaderName.CONNECTION, "close",
                HttpHeaderName.USER_AGENT, "x"
            ),

            "name: happy path"
        },
        {
            """
            Host: www.example.com\r
            Connection: close\r
            Foo: x\r
            """,

            Map.of(
                HttpHeaderName.HOST, "www.example.com",
                HttpHeaderName.CONNECTION, "close",
                HttpHeaderName.of("foo"), "x"
            ),

            "name: unknown"
        },
        {
            """
            Host: www.example.com\r
            Connection: close\r
            %s: x\r
            """.formatted(Http.tchar()),

            Map.of(
                HttpHeaderName.HOST, "www.example.com",
                HttpHeaderName.CONNECTION, "close",
                HttpHeaderName.of(Http.tchar()), "x"
            ),

            "name: test all valid characters"
        },
        {
            """
            Host: www.example.com\r
            Connection: close\r
            Accept-Encoding: gzip\r
            Referer: www.google.com\r
            User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/135.0.0.0 Safari/537.36\r
            """,

            Map.of(
                HttpHeaderName.HOST, "www.example.com",
                HttpHeaderName.CONNECTION, "close",
                HttpHeaderName.ACCEPT_ENCODING, "gzip",
                HttpHeaderName.REFERER, "www.google.com",
                HttpHeaderName.USER_AGENT, "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/135.0.0.0 Safari/537.36"
            ),

            "value: happy path"
        },
        {
            """
            Host: www.example.com\r
            Connection: close\r
            Accept-Encoding: x \r
            Referer:  x  y \r
            User-Agent:   \t z\t   \r
            """,

            Map.of(
                HttpHeaderName.HOST, "www.example.com",
                HttpHeaderName.CONNECTION, "close",
                HttpHeaderName.ACCEPT_ENCODING, "x",
                HttpHeaderName.REFERER, "x  y",
                HttpHeaderName.USER_AGENT, "z"
            ),

            "value: trim"
        },
        {
            """
            Host: www.example.com\r
            Connection: close\r
            Accept-Encoding: \r
            Referer:\r
            User-Agent:   \t\r
            """,

            Map.of(
                HttpHeaderName.HOST, "www.example.com",
                HttpHeaderName.CONNECTION, "close",
                HttpHeaderName.ACCEPT_ENCODING, "",
                HttpHeaderName.REFERER, "",
                HttpHeaderName.USER_AGENT, ""
            ),

            "value: empty"
        },
        {
            """
            Host: www.example.com\r
            Connection: close\r
            Referer: %s\r
            """.formatted(valueAllValidChars()),

            Map.of(
                HttpHeaderName.HOST, "www.example.com",
                HttpHeaderName.CONNECTION, "close",
                HttpHeaderName.REFERER, valueAllValidChars()
            ),

            "value: all valid characters"
        }
    };
  }

  private String valueAllValidChars() {
    final StringBuilder chars;
    chars = new StringBuilder();

    for (char c = 0x21; c < 0x7F; c++) {
      chars.append(c);
    }

    // SP and HTAB are valid if not leading or trailing
    chars.insert(10, ' ');
    chars.insert(20, '\t');

    return chars.toString();
  }

  @Test(dataProvider = "validProvider")
  public void valid(String payload, Map<HttpHeaderName, Object> expected, String description) throws IOException {
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = Y.socket(iso8859("""
          GET / HTTP/1.1\r
          %s\
          \r
          """.formatted(payload)));

          opts.handler = http -> {
            final HttpExchange0 impl;
            impl = (HttpExchange0) http;

            final HttpRequest0 request;
            request = impl.request();

            final HttpRequestHeaders0 headers;
            headers = request.headers();

            assertEquals(headers.headers(), expected);

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
  public Iterator<HttpHeaderName> validAllPredefinedNamesProvider() {
    final List<HttpHeaderName> list;
    list = new ArrayList<>();

    for (HttpHeaderName0 name : HttpHeaderName0.VALUES) {
      if (name.isResponseOnly()) {
        continue;
      }

      if (name.equals(HttpHeaderName0.TRANSFER_ENCODING) ||
          name.equals(HttpHeaderName0.HOST) ||
          name.equals(HttpHeaderName0.CONNECTION)) {
        continue;
      }

      list.add(name);
    }

    return list.iterator();
  }

  @Test(dataProvider = "validAllPredefinedNamesProvider")
  public void validAllPredefinedNames(HttpHeaderName name) throws IOException {
    valid(
        name != HttpHeaderName.CONTENT_TYPE
            ? """
              Host: www.example.com\r
              Connection: close\r
              %s: 0\r
              """.formatted(name.headerCase())
            : """
              Host: www.example.com\r
              Connection: close\r
              Content-Type: x\r
              Content-Length: 0\r
              """,

        name != HttpHeaderName.CONTENT_TYPE
            ? Map.of(
                HttpHeaderName.HOST, "www.example.com",
                HttpHeaderName.CONNECTION, "close",
                name, "0"
            )
            : Map.of(
                HttpHeaderName.HOST, "www.example.com",
                HttpHeaderName.CONNECTION, "close",
                HttpHeaderName.CONTENT_TYPE, "x",
                HttpHeaderName.CONTENT_LENGTH, "0"
            ),

        ""
    );
  }

  @DataProvider
  public Object[][] invalidProvider() {
    final List<Object[]> list;
    list = new ArrayList<>();

    // name: any invalid character
    final boolean[] nameValid;
    nameValid = new boolean[256];

    final String tchar;
    tchar = Http.tchar();

    for (int idx = 0, len = tchar.length(); idx < len; idx++) {
      final char validChar;
      validChar = tchar.charAt(idx);

      nameValid[validChar] = true;
    }

    // not valid but it is the valid separator
    nameValid[':'] = true;

    for (int c = 0; c < 0xFF; c++) {
      if (!nameValid[c]) {
        list.add(new Object[] {
            """
            Invalid%c: value\r
            """.formatted(c),

            "Name contains the invalid Ox" + Integer.toHexString(c) + " character"
        });
      }
    }

    // value: any invalid character
    final boolean valueValid[];
    valueValid = new boolean[256];

    for (char c = 0x21; c < 0x7F; c++) {
      // visible are valid
      valueValid[c] = true;
    }

    // SP and HTAB are valid
    valueValid[' '] = true;
    valueValid['\t'] = true;

    // CRLF are not valid but will trigger line terminator error
    valueValid['\r'] = true;
    valueValid['\n'] = true;

    for (int b = 0; b < valueValid.length; b++) {
      if (!valueValid[b]) {
        list.add(new Object[] {
            """
            Referer: va%clue\r
            """.formatted(b),

            "Name contains the invalid Ox" + Integer.toHexString(b) + " character"
        });
      }
    }

    return list.toArray(Object[][]::new);
  }

  @Test(dataProvider = "invalidProvider")
  public void invalid(String payload, String description) {
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = Y.socket(iso8859("""
          GET / HTTP/1.1\r
          %s\
          \r
          """.formatted(payload)));
        }),

        """
        HTTP/1.1 400 Bad Request\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Connection: close\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 25\r
        \r
        Invalid request headers.
        """
    );
  }

  @DataProvider
  public Object[][] invalidLineTerminatorProvider() {
    return new Object[][] {
        {
            """
            Referer: foo\r\
            """,
            "CR only"
        },
        {
            """
            Referer: foo
            """,
            "LF only"
        }
    };
  }

  @Test(dataProvider = "invalidLineTerminatorProvider")
  public void invalidLineTerminator(String payload, String description) {
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = Y.socket(iso8859("""
          GET / HTTP/1.1\r
          %s\
          \r
          """.formatted(payload)));
        }),

        """
        HTTP/1.1 400 Bad Request\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Connection: close\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 25\r
        \r
        Invalid line terminator.
        """
    );
  }

  @DataProvider
  public Object[][] fieldsTooLargeProvider() {
    final String tooLong;
    tooLong = "Abc".repeat(200);

    return new Object[][] {
        {
            """
            %s: x\r
            """.formatted(tooLong),

            "name too large"
        },
        {
            """
            Referer: %s\r
            """.formatted(tooLong),

            "value too large"
        }
    };
  }

  @Test(dataProvider = "fieldsTooLargeProvider")
  public void fieldsTooLarge(String payload, String description) {
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = Y.socket(iso8859("""
          GET / HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          %s\
          \r
          """.formatted(payload)));
        }),

        """
        HTTP/1.1 431 Request Header Fields Too Large\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Connection: close\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 25\r
        \r
        Invalid request headers.
        """
    );
  }

  @Test(dataProvider = "validProvider")
  public void slowClient(String payload, Map<HttpHeaderName, Object> expected, String description) throws IOException {
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = Y.socket(Y.slowStream(1, iso8859("""
          GET / HTTP/1.1\r
          %s\
          \r
          """.formatted(payload))));

          opts.handler = http -> {
            final HttpExchange0 impl;
            impl = (HttpExchange0) http;

            final HttpRequest0 request;
            request = impl.request();

            final HttpRequestHeaders0 headers;
            headers = request.headers();

            assertEquals(headers.headers(), expected);

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

  private byte[] iso8859(String s) {
    return s.getBytes(StandardCharsets.ISO_8859_1);
  }

}