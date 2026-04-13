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

import objectos.way.Media;
import objectos.way.Y;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HttpServerTaskTest4Version {

  @DataProvider
  public Object[][] versionValidProvider() {
    return new Object[][] {
        {"GET / HTTP/1.1", HttpVersion0.HTTP_1_1, "1.1 + path"},
        {"GET /%C3%A1 HTTP/1.1", HttpVersion0.HTTP_1_1, "1.1 + path + percent"},
        {"GET /url?key=value HTTP/1.1", HttpVersion0.HTTP_1_1, "1.1 + query key + value"},
        {"GET /url?key HTTP/1.1", HttpVersion0.HTTP_1_1, "1.1 + query key only"},
        {"GET /url?key=val%C3%A1 HTTP/1.1", HttpVersion0.HTTP_1_1, "1.1 + query key + value percent-encoded"},
        {"GET /url?key%C3%A1 HTTP/1.1", HttpVersion0.HTTP_1_1, "1.1 + query key only percent-encoded"},
        {"GET /url?key= HTTP/1.1", HttpVersion0.HTTP_1_1, "1.1 + query key only (equals)"},
        {"GET /url?key%C3%A1= HTTP/1.1", HttpVersion0.HTTP_1_1, "1.1 + query key only percent-encoded (equals)"}
    };
  }

  @Test(dataProvider = "versionValidProvider")
  public void versionValid(String line, HttpVersion expected, String description) {
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = Y.socket("""
          %s\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """.formatted(line));

          opts.handler = http -> {
            assertEquals(http.version(), expected);

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
  public Object[][] invalidLineTerminatorProvider() {
    return new Object[][] {
        {"GET / HTTP/1.1\n", "lf only"},
        {"GET / HTTP/1.1\r", "cr only"}
    };
  }

  @Test(dataProvider = "invalidLineTerminatorProvider")
  public void invalidLineTerminator(String line, String description) {
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = Y.socket("""
          %s\
          Host: www.example.com\r
          \r
          """.formatted(line));
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
  public Object[][] versionNotSupportedProvider() {
    return new Object[][] {
        {"GET / HTTP/1.0", "1.0 is not supported"},
        {"GET / HTTP/2", "2 is not supported (yet)"},
        {"GET / HTTP/9.9", "9.9 is not supported (yet)"}
    };
  }

  @Test(dataProvider = "versionNotSupportedProvider")
  public void versionNotSupported(String line, String description) {
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = Y.socket("""
          %s\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """.formatted(line));
        }),

        """
        HTTP/1.1 505 HTTP Version Not Supported\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Connection: close\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 29\r
        \r
        Supported versions: HTTP/1.1
        """
    );
  }

  @DataProvider
  public Object[][] badRequestProvider() {
    return new Object[][] {
        {"GET / HPTP/1.1", "valid chars but just nonsense"},
        {"GET / ABCD/1.1", "invalid chars"},
        {"GET / HTTP/1.", "Almost valid"},
        {"GET / HTTP/.1", "Almost valid"},
        {"GET / HTTP/", "Almost valid"},

        {"GET / HTTP/123456789012345678901234567890.123456789012345678901234567890", "Not supported"},

        {"GET /", "0.9 path"},
        {"GET /?", "0.9 path + empty"},
        {"GET /?key", "0.9 path + key"},
        {"GET /?key=", "0.9 path + key"},
        {"GET /?key=value", "0.9 path + key + value"},

        {"GET /%40", "0.9 path(perc)"},
        {"GET /%40?", "0.9 path(perc) + empty"},

        {"GET /%C3%A1", "0.9 "},
        {"GET /?key%C3%A1", "0.9 key(perc)"},
        {"GET /?key%C3%A1=", "0.9 key(perc)"},
        {"GET /?key=val%C3%A1", "0.9 "},
        {"GET /?key%C3%A1=val", "0.9 "}
    };
  }

  @Test(dataProvider = "badRequestProvider")
  public void badRequest(String line, String description) {
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = Y.socket("""
          %s\r
          Host: www.example.com\r
          \r
          """.formatted(line));
        }),

        """
        HTTP/1.1 400 Bad Request\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Connection: close\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 22\r
        \r
        Invalid request line.
        """
    );
  }

  @Test(dataProvider = "versionValidProvider")
  public void slowClientValid(String line, HttpVersion expected, String description) {
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = Y.socket(Y.slowStream(1, """
          %s\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """.formatted(line)));

          opts.handler = http -> {
            assertEquals(http.version(), expected);

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