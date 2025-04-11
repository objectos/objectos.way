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

public class HttpExchangeTest2ParsePath {

  @Test(description = "path: root")
  public void path01() throws IOException {
    test(
        """
        GET / HTTP/1.1\r
        \r
        """,

        "/"
    );
  }

  @Test(description = "path: with segment")
  public void path02() throws IOException {
    test(
        """
        GET /index.html HTTP/1.1\r
        \r
        """,

        "/index.html"
    );
  }

  @Test(description = "path: with percent-encoded values")
  public void path03() throws IOException {
    test(
        """
        GET /user%2Fjohn%40example.com HTTP/1.1\r
        \r
        """,

        "/user/john@example.com"
    );
  }

  @Test(description = "bad request: empty path")
  public void badRequest01() throws IOException {
    badRequest("""
    GET HTTP/1.1\r
    \r
    """);
  }

  @Test(description = "bad request: does not begin with '/'")
  public void badRequest02() throws IOException {
    badRequest("""
    GET index.html HTTP/1.1\r
    \r
    """);
  }

  @Test(description = "bad request: begins with empty segment")
  public void badRequest03() throws IOException {
    badRequest("""
    GET //index.html HTTP/1.1\r
    \r
    """);
  }

  @Test(description = "bad request: contains any invalid byte value")
  public void badRequest04() throws IOException {
    final StringBuilder sb;
    sb = new StringBuilder();

    for (int b = 0; b < 0xFF; b++) {
      System.out.println(b);

      final TestableSocket socket;
      socket = TestableSocket.of("GET /character/" + (char) b + " HTTP/1.1\r\n\r\n");

      try (HttpExchange http = new HttpExchange(socket, 256, 512, TestingClock.FIXED, TestingNoteSink.INSTANCE)) {
        if (http.shouldHandle()) {
          sb.append((char) b);
        } else {
          assertEquals(
              http.toString(),

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
      }
    }

    assertEquals(sb.toString(), " !$&'()*+,-./0123456789:;=?@ABCDEFGHIJKLMNOPQRSTUVWXYZ_abcdefghijklmnopqrstuvwxyz~");
  }

  @Test(description = "bad request: invalid percent encoded sequence")
  public void badRequest05() throws IOException {
    badRequest("""
    GET /pct/%xd HTTP/1.1\r
    \r
    """);
  }

  private void badRequest(String request) throws IOException {
    final TestableSocket socket;
    socket = TestableSocket.of(request);

    try (HttpExchange http = new HttpExchange(socket, 256, 512, TestingClock.FIXED, TestingNoteSink.INSTANCE)) {
      assertEquals(http.shouldHandle(), false);

      assertEquals(
          http.toString(),

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
  }

  private void test(String request, String expected) throws IOException {
    TestableSocket socket;
    socket = TestableSocket.of(request);

    try (HttpExchange http = new HttpExchange(socket, 256, 512, TestingClock.FIXED, TestingNoteSink.INSTANCE)) {
      assertEquals(http.shouldHandle(), true);

      assertEquals(http.path(), expected);
    }
  }

}