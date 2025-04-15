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
import java.net.Socket;
import org.testng.annotations.Test;

public class HttpExchangeTest4ParseVersion {

  @Test(description = "version: 1.1 + path")
  public void version01() throws IOException {
    test(
        """
        GET / HTTP/1.1\r
        Host: host\r
        \r
        """,

        Http.Version.HTTP_1_1
    );
  }

  @Test(description = "version: 1.1 + path + percent")
  public void version02() throws IOException {
    test(
        """
        GET /%C3%A1 HTTP/1.1\r
        Host: host\r
        \r
        """,

        Http.Version.HTTP_1_1
    );
  }

  @Test(description = "version: 1.1 + query key + value")
  public void version03() throws IOException {
    test(
        """
        GET /url?key=value HTTP/1.1\r
        Host: host\r
        \r
        """,

        Http.Version.HTTP_1_1
    );
  }

  @Test(description = "version: 1.1 + query key only")
  public void version04() throws IOException {
    test(
        """
        GET /url?key HTTP/1.1\r
        Host: host\r
        \r
        """,

        Http.Version.HTTP_1_1
    );
  }

  @Test(description = "version: 1.1 + query key + value percent-encoded")
  public void version05() throws IOException {
    test(
        """
        GET /url?key=val%C3%A1 HTTP/1.1\r
        Host: host\r
        \r
        """,

        Http.Version.HTTP_1_1
    );
  }

  @Test(description = "version: 1.1 + query key only percent-encoded")
  public void version06() throws IOException {
    test(
        """
        GET /url?key%C3%A1 HTTP/1.1\r
        Host: host\r
        \r
        """,

        Http.Version.HTTP_1_1
    );
  }

  @Test(description = "version: 1.1 + query key only (equals)")
  public void version07() throws IOException {
    test(
        """
        GET /url?key= HTTP/1.1\r
        Host: host\r
        \r
        """,

        Http.Version.HTTP_1_1
    );
  }

  @Test(description = "version: 1.1 + query key only percent-encoded (equals)")
  public void version08() throws IOException {
    test(
        """
        GET /url?key%C3%A1= HTTP/1.1\r
        Host: host\r
        \r
        """,

        Http.Version.HTTP_1_1
    );
  }

  @Test
  public void invalidLineTerminator() throws IOException {
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

  @Test
  public void versionNotSupported01() throws IOException {
    versionNotSupported("""
    GET / HTTP/1.0\r
    Host: host\r
    \r
    """);
  }

  @Test
  public void versionNotSupported02() throws IOException {
    versionNotSupported("""
    GET / HTTP/2\r
    Host: host\r
    \r
    """);
  }

  @Test
  public void versionNotSupported03() throws IOException {
    versionNotSupported("""
    GET / HTTP/9.9\r
    Host: host\r
    \r
    """);
  }

  @Test
  public void version09NotSupported01() throws IOException {
    versionNotSupported("GET /\r\n\r\n");
    versionNotSupported("GET /\n\n");
  }

  @Test
  public void version09NotSupported02() throws IOException {
    versionNotSupported("GET /%C3%A1\r\n\r\n");
    versionNotSupported("GET /%C3%A1\n\n");
  }

  @Test
  public void version09NotSupported03() throws IOException {
    versionNotSupported("GET /?key=val%C3%A1\r\n\r\n");
    versionNotSupported("GET /?key=val%C3%A1\n\n");
  }

  @Test
  public void version09NotSupported04() throws IOException {
    versionNotSupported("GET /?key%C3%A1=val\r\n\r\n");
    versionNotSupported("GET /?key%C3%A1=val\n\n");
  }

  @Test
  public void version09NotSupported05() throws IOException {
    versionNotSupported("GET /?key%C3%A1=\r\n\r\n");
    versionNotSupported("GET /?key%C3%A1=\n\n");
  }

  @Test
  public void version09NotSupported06() throws IOException {
    versionNotSupported("GET /?key%C3%A1\r\n\r\n");
    versionNotSupported("GET /?key%C3%A1\n\n");
  }

  @Test(description = "bad request: valid chars but just nonsense")
  public void badRequest01() throws IOException {
    badRequest("""
    GET / HPTP/1.1\r
    Host: host\r
    \r
    """);
  }

  @Test(description = "bad request: invalid chars")
  public void badRequest02() throws IOException {
    badRequest("""
    GET / ABCD/1.1\r
    Host: host\r
    \r
    """);
  }

  public void versionTooLong() {}

  private void badRequest(String request) throws IOException {
    final Socket socket;
    socket = Y.socket(request);

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

  private void versionNotSupported(Object request) throws IOException {
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

  private void test(Object request, Http.Version expected) throws IOException {
    final Socket socket;
    socket = Y.socket(request);

    try (HttpExchange http = new HttpExchange(socket, 256, 512, TestingClock.FIXED, TestingNoteSink.INSTANCE)) {
      assertEquals(http.shouldHandle(), true);

      assertEquals(http.version(), expected);
    }
  }

  private void test(Object request, String expected) throws IOException {
    final Socket socket;
    socket = Y.socket(request);

    try (HttpExchange http = new HttpExchange(socket, 256, 512, TestingClock.FIXED, TestingNoteSink.INSTANCE)) {
      assertEquals(http.shouldHandle(), false);

      assertEquals(http.toString(), expected);
    }
  }

}