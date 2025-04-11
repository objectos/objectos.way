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

import java.io.IOException;
import org.testng.annotations.Test;

public class HttpExchangeTest1ParseMethod {

  @Test(description = "method: valid")
  public void method01() throws IOException {
    for (Http.Method method : Http.Method.VALUES) {
      if (method.implemented) {
        final String request;
        request = method.name() + " /index.html HTTP/1.1\r\n\r\n";

        final TestableSocket socket;
        socket = TestableSocket.of(request);

        try (HttpExchange http = new HttpExchange(socket, 2, 256, TestingClock.FIXED, TestingNoteSink.INSTANCE)) {
          assertEquals(http.shouldHandle(), true);

          assertEquals(http.method(), method);
        }
      }
    }
  }

  @Test(description = "method: valid but not implemented")
  public void method02() throws IOException {
    for (Http.Method method : Http.Method.VALUES) {
      if (!method.implemented) {
        test(
            method.name() + " /index.html HTTP/1.1\r\n\r\n",

            """
            HTTP/1.1 501 NOT IMPLEMENTED\r
            Date: Wed, 28 Jun 2023 12:08:43 GMT\r
            Content-Length: 0\r
            Connection: close\r
            \r
            """
        );
      }
    }
  }

  @Test(description = "bad request: unknown method")
  public void badRequest01() throws IOException {
    test(
        """
        XYZ /path?key=value HTTP/1.1\r
        \r
        """,

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

  @Test(description = "bad request: no method")
  public void badRequest02() throws IOException {
    test(
        """
        \r
        \r
        """,

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

    test(
        """
        POS\r
        \r
        """,

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

  @Test(description = "bad request: incomplete method name")
  public void badRequest03() throws IOException {
    test(
        """
        POS /login HTTP/1.1\r
        \r
        """,

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

  private void test(String request, String response) throws IOException {
    TestableSocket socket;
    socket = TestableSocket.of(request);

    final int bufferInitial = 2; // force many buffer resizes

    try (HttpExchange http = new HttpExchange(socket, bufferInitial, 512, TestingClock.FIXED, TestingNoteSink.INSTANCE)) {
      assertEquals(http.shouldHandle(), false);

      assertEquals(http.toString(), response);
    }
  }

}