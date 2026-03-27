/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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
import objectos.http.HttpRequestParser.InvalidRequestHeaders;
import objectos.way.Y;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HttpRequestTest4ParseHeaders {

  @DataProvider
  public Object[][] validProvider() {
    return new Object[][] {
        {
            """
            Accept-Encoding: x\r
            Referer: x\r
            User-Agent: x\r
            """,

            Map.of(
                HttpHeaderName.ACCEPT_ENCODING, "x",
                HttpHeaderName.REFERER, "x",
                HttpHeaderName.USER_AGENT, "x"
            ),

            "name: happy path"
        },
        {
            """
            Host: x\r
            Foo: x\r
            User-Agent: x\r
            """,

            Map.of(
                HttpHeaderName.HOST, "x",
                HttpHeaderName.of("foo"), "x",
                HttpHeaderName.USER_AGENT, "x"
            ),

            "name: unknown"
        },
        {
            """
            %s: x\r
            """.formatted(Http.tchar()),

            Map.of(
                HttpHeaderName.of(Http.tchar()), "x"
            ),

            "name: test all valid characters"
        },
        {
            """
            Accept-Encoding: gzip\r
            Referer: www.google.com\r
            User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/135.0.0.0 Safari/537.36\r
            """,

            Map.of(
                HttpHeaderName.ACCEPT_ENCODING, "gzip",
                HttpHeaderName.REFERER, "www.google.com",
                HttpHeaderName.USER_AGENT, "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/135.0.0.0 Safari/537.36"
            ),

            "value: happy path"
        },
        {
            """
            Accept-Encoding: x \r
            Referer:  x  y \r
            User-Agent:   \t z\t   \r
            """,

            Map.of(
                HttpHeaderName.ACCEPT_ENCODING, "x",
                HttpHeaderName.REFERER, "x  y",
                HttpHeaderName.USER_AGENT, "z"
            ),

            "value: trim"
        },
        {
            """
            Accept-Encoding: \r
            Referer:\r
            User-Agent:   \t\r
            """,

            Map.of(
                HttpHeaderName.ACCEPT_ENCODING, "",
                HttpHeaderName.REFERER, "",
                HttpHeaderName.USER_AGENT, ""
            ),

            "value: empty"
        },
        {
            """
            Referer: %s\r
            """.formatted(valueAllValidChars()),

            Map.of(
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
  public void valid(String headers, Map<HttpHeaderName, Object> expected, String description) throws IOException {
    final HttpRequest req;
    req = HttpRequestParserY.parse(
        test -> test.bufferSize(256, 512),

        iso8859("""
        GET / HTTP/1.1\r
        %s\
        \r
        """.formatted(headers))
    );

    for (var entry : expected.entrySet()) {
      final HttpHeaderName name;
      name = entry.getKey();

      final Object value;
      value = entry.getValue();

      if (value instanceof String s) {
        assertEquals(req.header(name), s);
      }

      else {
        throw new UnsupportedOperationException("Implement me");
      }
    }
  }

  @Test(description = "name: all predefined names")
  public void namePredefined() throws IOException {
    StringBuilder in;
    in = new StringBuilder();

    in.append("GET / HTTP/1.1\r\n");

    for (HttpHeaderNameImpl name : HttpHeaderNameImpl.VALUES) {
      if (!name.isResponseOnly() && !name.equals(HttpHeaderNameImpl.TRANSFER_ENCODING)) {
        in.append(name.headerCase());
        in.append(": ");
        in.append(Integer.toString(name.index()));
        in.append("\r\n");
      }
    }

    in.append("\r\n");

    final int contentLength;
    contentLength = HttpHeaderNameImpl.CONTENT_LENGTH.index();

    in.append("x".repeat(contentLength));

    final HttpRequest req;
    req = HttpRequestParserY.parse(
        test -> test.bufferSize(256, 512),

        iso8859(in.toString())
    );

    // headers
    for (HttpHeaderNameImpl name : HttpHeaderNameImpl.VALUES) {
      if (!name.isResponseOnly() && !name.equals(HttpHeaderNameImpl.TRANSFER_ENCODING)) {
        assertEquals(req.header(name), Integer.toString(name.index()));
      }
    }
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
            GET / HTTP/1.1\r
            Invalid%c: value\r
            \r
            """.formatted(c),

            InvalidRequestHeaders.NAME_CHAR,

            "Name contains the invalid Ox" + Integer.toHexString(c) + " character"
        });
      }
    }

    // name: request fields too large
    final String tooLong;
    tooLong = "Abc".repeat(200);

    list.add(new Object[] {
        """
        GET / HTTP/1.1\r
        Host: www.example.com\r
        %s: x\r
        \r
        """.formatted(tooLong),

        InvalidRequestHeaders.REQUEST_HEADER_FIELDS_TOO_LARGE,

        "Name is too long"
    });

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

    // LF is not valid but will trigger line terminator error
    valueValid['\n'] = true;

    for (int b = 0; b < valueValid.length; b++) {
      if (!valueValid[b]) {
        list.add(new Object[] {
            """
            GET / HTTP/1.1\r
            Referer: va%clue\r
            \r
            """.formatted(b),

            InvalidRequestHeaders.VALUE_CHAR,

            "Name contains the invalid Ox" + Integer.toHexString(b) + " character"
        });
      }
    }

    // value: request fields too large
    list.add(new Object[] {
        """
        GET / HTTP/1.1\r
        Host: www.example.com\r
        Referer: %s\r
        \r
        """.formatted(tooLong),

        InvalidRequestHeaders.REQUEST_HEADER_FIELDS_TOO_LARGE,

        "Value is too long"
    });

    return list.toArray(Object[][]::new);
  }

  @SuppressWarnings("exports")
  @Test(dataProvider = "invalidProvider")
  public void invalid(String headers, HttpClientException.Kind kind, String description) throws IOException {
    try {
      HttpRequestParserY.parse(
          test -> test.bufferSize(256, 512),

          iso8859(headers)
      );

      Assert.fail("It should have thrown");
    } catch (HttpClientException expected) {
      assertEquals(expected.kind, kind);
    }
  }

  @Test(dataProvider = "validProvider")
  public void slowClient(String headers, Map<HttpHeaderName, Object> expected, String description) throws IOException {
    final HttpRequest req;
    req = HttpRequestParserY.parse(
        test -> test.bufferSize(256, 512),

        Y.slowStream(1, iso8859("""
        GET / HTTP/1.1\r
        %s\
        \r
        """.formatted(headers)))
    );

    for (var entry : expected.entrySet()) {
      final HttpHeaderName name;
      name = entry.getKey();

      final Object value;
      value = entry.getValue();

      if (value instanceof String s) {
        assertEquals(req.header(name), s);
      }

      else {
        throw new UnsupportedOperationException("Implement me");
      }
    }
  }

  private byte[] iso8859(String s) {
    return s.getBytes(StandardCharsets.ISO_8859_1);
  }

}
