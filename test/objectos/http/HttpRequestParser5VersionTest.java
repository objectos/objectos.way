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
import objectos.way.Y;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import objectos.http.HttpRequestParserException.Kind;

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
        {"HTTP/1.1\r\n", HttpVersion0.HTTP_1_1, "1.1"},
        {"HTTP/1.1\r\nHost: test\r\n", HttpVersion0.HTTP_1_1, "1.1"},
        {"HTTP/1.0\r\n", HttpVersion0.of(1, 0), "not supported"},
        {"HTTP/2\r\nHost: foo", HttpVersion0.of(2, 0), "not supported (yet)"},
        {"HTTP/9.9\r\n", HttpVersion0.of(9, 9), "not supported (yet)"}
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
        {"HTTP/22.3\r\n", Kind.INVALID_REQUEST_LINE, "Invalid HTTP version: major version should contain a single digit"},
        {"HTTP/9.11\r\n", Kind.INVALID_REQUEST_LINE, "Invalid HTTP version: minor version should contain a single digit"},
        {"HPTP/1.1\r\n", Kind.INVALID_REQUEST_LINE, "Invalid HTTP version: expected byte 0x54 but got 0x50"},
        {"ABCD/1.1\r\n", Kind.INVALID_REQUEST_LINE, "Invalid HTTP version: expected byte 0x48 but got 0x41"},
        {"HTTP/1.\r\n", Kind.INVALID_REQUEST_LINE, "Invalid HTTP version: byte 0x0D is not a valid digit"},
        {"HTTP/.1\r\n", Kind.INVALID_REQUEST_LINE, "Invalid HTTP version: byte 0x2E is not a valid digit"},
        {"HTTP/\r\n", Kind.INVALID_REQUEST_LINE, "Invalid HTTP version: byte 0x0D is not a valid digit"},
        {"HTTP/1.x", Kind.INVALID_REQUEST_LINE, "Invalid HTTP version: byte 0x78 is not a valid digit"},
        {"HTTP/1.1", Kind.INVALID_REQUEST_LINE, "EOF while parsing HTTP version"},
        {"HTTP/1.1\n", Kind.LINE_TERMINATOR, "CRLF sequence required as line terminator"},
        {"HTTP/1.1\r\t", Kind.LINE_TERMINATOR, "CRLF sequence required as line terminator"}
    };
  }

  @SuppressWarnings("exports")
  @Test(dataProvider = "invalidProvider")
  public void invalid(String line, Kind kind, String msg) throws IOException {
    try {
      parse(
          line
      );

      Assert.fail("It should have thrown");
    } catch (HttpRequestParserException expected) {
      assertEquals(expected.getMessage(), msg);

      assertEquals(expected.kind, kind);
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
