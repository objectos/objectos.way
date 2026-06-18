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
package objectox.http.srv;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import objectos.http.Content;
import objectos.http.HeaderName;
import objectos.http.MediaType;
import objectos.way.Y;
import objectos.y.SocketY;
import objectox.http.HeaderNamePojo;
import objectox.http.Rfc;
import objectox.http.handler.HandlerNoop;
import objectox.http.req.RequestHeaders;
import objectox.http.req.RequestPojo;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ServerTaskTest5Headers {

  private final Content ok = Content.of(MediaType.TEXT_PLAIN, "OK\n");

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
                HeaderName.HOST, "www.example.com",
                HeaderName.CONNECTION, "close",
                HeaderName.USER_AGENT, "x"
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
                HeaderName.HOST, "www.example.com",
                HeaderName.CONNECTION, "close",
                HeaderName.of("foo"), "x"
            ),

            "name: unknown"
        },
        {
            """
            Host: www.example.com\r
            Connection: close\r
            %s: x\r
            """.formatted(Rfc.tchar()),

            Map.of(
                HeaderName.HOST, "www.example.com",
                HeaderName.CONNECTION, "close",
                HeaderName.of(Rfc.tchar()), "x"
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
                HeaderName.HOST, "www.example.com",
                HeaderName.CONNECTION, "close",
                HeaderName.ACCEPT_ENCODING, "gzip",
                HeaderName.REFERER, "www.google.com",
                HeaderName.USER_AGENT, "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/135.0.0.0 Safari/537.36"
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
                HeaderName.HOST, "www.example.com",
                HeaderName.CONNECTION, "close",
                HeaderName.ACCEPT_ENCODING, "x",
                HeaderName.REFERER, "x  y",
                HeaderName.USER_AGENT, "z"
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
                HeaderName.HOST, "www.example.com",
                HeaderName.CONNECTION, "close",
                HeaderName.ACCEPT_ENCODING, "",
                HeaderName.REFERER, "",
                HeaderName.USER_AGENT, ""
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
                HeaderName.HOST, "www.example.com",
                HeaderName.CONNECTION, "close",
                HeaderName.REFERER, valueAllValidChars()
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
  public void valid(String payload, Map<HeaderName, Object> expected, String description) throws IOException {
    assertEquals(
        ServerTaskY.resp(opts -> {
          opts.host("www.example.com", req -> {
            final RequestPojo request;
            request = (RequestPojo) req;

            final RequestHeaders headers;
            headers = request.headers();

            assertEquals(headers.headers(), expected);

            return ok;
          });

          opts.socket(iso8859("""
          GET / HTTP/1.1\r
          %s\
          \r
          """.formatted(payload)));
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
  public Iterator<HeaderName> validAllPredefinedNamesProvider() {
    final List<HeaderName> list;
    list = new ArrayList<>();

    for (HeaderNamePojo name : HeaderNamePojo.VALUES) {
      if (name.isResponseOnly()) {
        continue;
      }

      if (name.equals(HeaderNamePojo.TRANSFER_ENCODING) ||
          name.equals(HeaderNamePojo.HOST) ||
          name.equals(HeaderNamePojo.CONNECTION)) {
        continue;
      }

      list.add(name);
    }

    return list.iterator();
  }

  @Test(dataProvider = "validAllPredefinedNamesProvider")
  public void validAllPredefinedNames(HeaderName name) throws IOException {
    valid(
        name != HeaderName.CONTENT_TYPE
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

        name != HeaderName.CONTENT_TYPE
            ? Map.of(
                HeaderName.HOST, "www.example.com",
                HeaderName.CONNECTION, "close",
                name, "0"
            )
            : Map.of(
                HeaderName.HOST, "www.example.com",
                HeaderName.CONNECTION, "close",
                HeaderName.CONTENT_TYPE, "x",
                HeaderName.CONTENT_LENGTH, "0"
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
    tchar = Rfc.tchar();

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
        ServerTaskY.resp(opts -> {
          opts.socket = SocketY.of(iso8859("""
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
        ServerTaskY.resp(opts -> {
          opts.socket = SocketY.of(iso8859("""
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
        ServerTaskY.resp(opts -> {
          opts.bufferSize = 512;

          opts.host("www.example.com", HandlerNoop.INSTANCE);

          opts.socket(iso8859("""
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
  public void slowClient(String payload, Map<HeaderName, Object> expected, String description) throws IOException {
    assertEquals(
        ServerTaskY.resp(opts -> {
          opts.host("www.example.com", req -> {
            final RequestPojo request;
            request = (RequestPojo) req;

            final RequestHeaders headers;
            headers = request.headers();

            assertEquals(headers.headers(), expected);

            return ok;
          });

          opts.socket(Y.slowStream(1, iso8859("""
          GET / HTTP/1.1\r
          %s\
          \r
          """.formatted(payload))));
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