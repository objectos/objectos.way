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
import static org.testng.Assert.assertSame;

import module java.base;
import objectos.way.Y;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HttpRequestParser2MethodTest {

  private HttpMethod parse(Object... data) throws IOException {
    final Socket socket;
    socket = Y.socket(data);

    final HttpRequestParser0Input input;
    input = HttpRequestParser0Input.of(512, socket);

    final HttpRequestParser2Method parser;
    parser = new HttpRequestParser2Method(input);

    return parser.parse();
  }

  @DataProvider
  public Iterator<HttpMethod> methodProvider() {
    return Stream.of(HttpMethod.VALUES).iterator();
  }

  @Test(dataProvider = "methodProvider", description = "method: valid")
  public void parse01(HttpMethod method) throws IOException {
    assertEquals(
        parse(
            """
            %s /index.html HTTP/1.1\r
            Host: www.example.com\r
            \r
            """.formatted(method.name())
        ),

        method
    );
  }

  @Test(dataProvider = "methodProvider", description = "method: valid + slow client")
  public void parse03(HttpMethod method) throws IOException {
    if (!method.implemented) {
      return;
    }

    assertEquals(
        parse(
            Y.slowStream(1, """
            %s /index.html HTTP/1.1\r
            Host: www.example.com\r
            \r
            """.formatted(method.name()))
        ),

        method
    );
  }

  @DataProvider
  public Object[][] badRequestProvider() {
    return new Object[][] {
        {
            """
            XYZ /path?key=value HTTP/1.1\r
            Host: www.example.com\r
            \r
            """,
            "Unexpected byte 0x58 while parsing method first char"
        },
        {
            """
            \r
            \r
            """,
            "Unexpected byte 0x0D while parsing method first char"
        },
        {
            """
            POS /login HTTP/1.1\r
            Host: www.example.com\r
            \r
            """,
            "Unexpected byte 0x20 while parsing method POST"
        }
    };
  }

  @Test(dataProvider = "badRequestProvider")
  public void badRequest(String request, String message) throws IOException {
    try {
      parse(
          request
      );

      Assert.fail("It should have thrown");
    } catch (HttpClientException expected) {
      assertEquals(expected.getMessage(), message);

      assertEquals(expected.kind, HttpClientException.Kind.INVALID_REQUEST_LINE);
    }
  }

  @DataProvider
  public Object[][] ioExceptionProvider() {
    return new Object[][] {
        {"GE", Y.trimStackTrace(new IOException(), 1), ""},
        {"POS", Y.trimStackTrace(new IOException(), 1), ""}
    };
  }

  @Test(dataProvider = "ioExceptionProvider")
  public void ioException(String request, IOException ex, String description) {
    try {
      parse(
          request,
          ex
      );

      Assert.fail("It should have thrown");
    } catch (IOException expected) {
      assertSame(expected, ex);
    }
  }

  @Test
  public void eof01() throws IOException {
    try {
      parse();

      Assert.fail("It should have thrown");
    } catch (HttpClientException expected) {
      assertEquals(expected.getMessage(), "EOF while parsing method");

      assertEquals(expected.kind, HttpClientException.Kind.INVALID_REQUEST_LINE);
    }
  }

}