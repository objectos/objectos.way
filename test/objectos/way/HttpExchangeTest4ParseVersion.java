/*
 * Copyright (C) 2026 Objectos Software LTDA.
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

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HttpExchangeTest4ParseVersion extends HttpExchangeTest {

  @DataProvider
  public Object[][] versionValidProvider() {
    return new Object[][] {
        {"GET / HTTP/1.1", Http.Version.HTTP_1_1, "1.1 + path"},
        {"GET /%C3%A1 HTTP/1.1", Http.Version.HTTP_1_1, "1.1 + path + percent"},
        {"GET /url?key=value HTTP/1.1", Http.Version.HTTP_1_1, "1.1 + query key + value"},
        {"GET /url?key HTTP/1.1", Http.Version.HTTP_1_1, "1.1 + query key only"},
        {"GET /url?key=val%C3%A1 HTTP/1.1", Http.Version.HTTP_1_1, "1.1 + query key + value percent-encoded"},
        {"GET /url?key%C3%A1 HTTP/1.1", Http.Version.HTTP_1_1, "1.1 + query key only percent-encoded"},
        {"GET /url?key= HTTP/1.1", Http.Version.HTTP_1_1, "1.1 + query key only (equals)"},
        {"GET /url?key%C3%A1= HTTP/1.1", Http.Version.HTTP_1_1, "1.1 + query key only percent-encoded (equals)"}
    };
  }

  @Test(dataProvider = "versionValidProvider")
  public void versionValid(String line, Http.Version expected, String description) {
    test(
        """
        %s\r
        Host: host\r
        \r
        """.formatted(line),

        expected
    );
  }

  @Test
  public void invalidLineTerminator() {
    test(
        """
        GET / HTTP/1.1
        Host: host\r
        \r
        """,

        """
        HTTP/1.1 400 Bad Request\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 25\r
        Connection: close\r
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
        {"GET / HTTP/9.9", "9.9 is not supported (yet)"},
        {"GET / HTTP/123456789012345678901234567890.123456789012345678901234567890", "Not supported"}
    };
  }

  @Test(dataProvider = "versionNotSupportedProvider")
  public void versionNotSupported(String line, String description) {
    versionNotSupported("""
    %s\r
    Host: host\r
    \r
    """.formatted(line));
  }

  @DataProvider
  public Object[][] version09NotSupportedProvider() {
    return new Object[][] {
        {"GET /\r\n\r\n"},
        {"GET /\n\n"},
        {"GET /%C3%A1\r\n\r\n"},
        {"GET /%C3%A1\n\n"},
        {"GET /?key=val%C3%A1\r\n\r\n"},
        {"GET /?key=val%C3%A1\n\n"},
        {"GET /?key%C3%A1=val\r\n\r\n"},
        {"GET /?key%C3%A1=val\n\n"},
        {"GET /?key%C3%A1=\r\n\r\n"},
        {"GET /?key%C3%A1=\n\n"},
        {"GET /?key%C3%A1\r\n\r\n"},
        {"GET /?key%C3%A1\n\n"}
    };
  }

  @Test(dataProvider = "version09NotSupportedProvider")
  public void version09NotSupported(String line) {
    versionNotSupported(line);
  }

  @DataProvider
  public Object[][] badRequestProvider() {
    return new Object[][] {
        {"GET / HPTP/1.1", "valid chars but just nonsense"},
        {"GET / ABCD/1.1", "invalid chars"},
        {"GET / HTTP/1.", "Almost valid"},
        {"GET / HTTP/.1", "Almost valid"},
        {"GET / HTTP/", "Almost valid"}
    };
  }

  @Test(dataProvider = "badRequestProvider")
  public void badRequest(String line, String description) {
    test(
        """
        %s\r
        Host: host\r
        \r
        """.formatted(line),

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

  @Test(dataProvider = "versionValidProvider")
  public void slowClientValid(String line, Http.Version expected, String description) {
    test(
        Y.slowStream(1, """
        %s\r
        Host: test\r
        \r
        """.formatted(line)),

        expected
    );
  }

  private void versionNotSupported(Object request) {
    test(
        request,

        """
        HTTP/1.1 505 HTTP Version Not Supported\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 29\r
        Connection: close\r
        \r
        Supported versions: HTTP/1.1
        """
    );
  }

  private void test(Object request, Http.Version expected) {
    exec(test -> {
      test.bufferSize(256, 256);

      test.xch(xch -> {
        xch.req(request);

        xch.handler(http -> {
          assertEquals(http.version(), expected);

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

  private void test(Object request, String expected) {
    exec(test -> {
      test.bufferSize(256, 256);

      test.xch(xch -> {
        xch.req(request);
        xch.shouldHandle(false);
        xch.resp(expected);
      });
    });
  }

}