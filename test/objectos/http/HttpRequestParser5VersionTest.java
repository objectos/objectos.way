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
import java.net.Socket;
import objectos.http.HttpRequestParser5Version.Invalid;
import objectos.way.Y;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HttpRequestParser5VersionTest {

  private HttpVersion parse(Object... data) throws IOException {
    final Socket socket;
    socket = Y.socket(data);

    final HttpRequestParser0Input input;
    input = HttpRequestParser0Input.of(512, socket);

    final HttpRequestParser5Version parser;
    parser = new HttpRequestParser5Version(input);

    return parser.parse();
  }

  @DataProvider
  public Object[][] validProvider() {
    return new Object[][] {
        {"HTTP/1.1\r\n", HttpVersion.HTTP_1_1, "1.1"},
        {"HTTP/1.1\r\nHost: test\r\n", HttpVersion.HTTP_1_1, "1.1"}
    };
  }

  @Test(dataProvider = "validProvider")
  public void versionValid(String line, HttpVersion expected, String description) throws IOException {
    assertEquals(
        parse(
            line
        ),

        expected
    );
  }

  @DataProvider
  public Object[][] invalidProvider() {
    return new Object[][] {
        {"HPTP/1.1\r\n", Invalid.VERSION, "valid chars but just nonsense"},
        {"ABCD/1.1\r\n", Invalid.VERSION, "invalid chars"},
        {"HTTP/1.\r\n", Invalid.VERSION, "Almost valid"},
        {"HTTP/.1\r\n", Invalid.VERSION, "Almost valid"},
        {"HTTP/\r\n", Invalid.VERSION, "Almost valid"},
        {"HTTP/1.x", Invalid.VERSION, "Almost valid"},
        {"HTTP/1.1", Invalid.EOF, "Almost valid... EOF"},
        {"HTTP/1.1\n", Invalid.LINE_TERMINATOR, "Almost valid... line terminator"},
        {"HTTP/1.1\r\t", Invalid.LINE_TERMINATOR, "Almost valid... line terminator"}
    };
  }

  @SuppressWarnings("exports")
  @Test(dataProvider = "invalidProvider")
  public void invalid(String line, Invalid kind, String description) throws IOException {
    try {
      parse(
          line
      );

      Assert.fail("It should have thrown");
    } catch (HttpClientException expected) {
      assertEquals(expected.kind, kind);
    }
  }

  @DataProvider
  public Object[][] versionNotSupportedProvider() {
    return new Object[][] {
        {"HTTP/1.0\r\n", "1.0 is not supported"},
        {"HTTP/2\r\nHost: foo", "2 is not supported (yet)"},
        {"HTTP/9.9\r\n", "9.9 is not supported (yet)"},
        {"HTTP/123456789012345678901234567890.123456789012345678901234567890\r\n", "Not supported"}
    };
  }

  @Test(dataProvider = "versionNotSupportedProvider")
  public void versionNotSupported(String line, String description) throws IOException {
    try {
      parse(
          line
      );

      Assert.fail("It should have thrown");
    } catch (HttpClientException expected) {
      assertEquals(expected.kind, Invalid.HTTP_VERSION_NOT_SUPPORTED);
    }
  }

  @Test(dataProvider = "validProvider")
  public void slowClientValid(String line, HttpVersion expected, String description) throws IOException {
    assertEquals(
        parse(
            Y.slowStream(1, line)
        ),

        expected
    );
  }

}
