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
import java.io.UncheckedIOException;
import java.net.Socket;
import org.testng.annotations.Test;

public class HttpExchangeTest1ParseMethod {

  @Test(description = "method: valid")
  public void method01() {
    for (Http.Method method : Http.Method.VALUES) {
      if (method.implemented) {
        final String request;
        request = method.name() + " /index.html HTTP/1.1\r\nHost: www.example.com\r\n\r\n";

        final Socket socket;
        socket = Y.socket(request);

        try (HttpExchange http = new HttpExchange(socket, 2, 256, Y.clockFixed(), TestingNoteSink.INSTANCE)) {
          assertEquals(http.shouldHandle(), true);

          assertEquals(http.method(), method);
        } catch (IOException e) {
          throw new UncheckedIOException(e);
        }
      }
    }
  }

  @Test(description = "method: valid but not implemented")
  public void method02() throws IOException {
    for (Http.Method method : Http.Method.VALUES) {
      if (!method.implemented) {
        shouldNot(
            method.name() + " /index.html HTTP/1.1\r\nnHost: www.example.com\r\n\r\n",

            """
            HTTP/1.1 501 Not Implemented\r
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
    badRequest("""
        XYZ /path?key=value HTTP/1.1\r
        Host: www.example.com\r
        \r
        """);
  }

  @Test(description = "bad request: no method")
  public void badRequest02() throws IOException {
    badRequest("""
        \r
        \r
        """);
  }

  @Test(description = "bad request: incomplete method name")
  public void badRequest03() throws IOException {
    badRequest(
        """
        POS /login HTTP/1.1\r
        Host: www.example.com\r
        \r
        """);
  }

  @Test
  public void slowClient01() {
    should(
        Y.slowStream(1, """
        GET / HTTP/1.1\r
        Host: test\r
        Connection: close\r
        \r
        """),

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

  @Test
  public void ioException01() {
    Socket socket;
    socket = Y.socket("GE", Y.trimStackTrace(new IOException(), 1));

    try (HttpExchange http = new HttpExchange(socket, 256, 512, Y.clockFixed(), TestingNoteSink.INSTANCE)) {
      assertEquals(http.shouldHandle(), false);

      assertEquals(Y.toString(socket), "");
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  @Test
  public void ioException02() {
    Socket socket;
    socket = Y.socket("POS", Y.trimStackTrace(new IOException(), 1));

    try (HttpExchange http = new HttpExchange(socket, 256, 512, Y.clockFixed(), TestingNoteSink.INSTANCE)) {
      assertEquals(http.shouldHandle(), false);

      assertEquals(Y.toString(socket), "");
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  @Test
  public void eof01() {
    Socket socket;
    socket = Y.socket();

    try (HttpExchange http = new HttpExchange(socket, 256, 512, Y.clockFixed(), TestingNoteSink.INSTANCE)) {
      assertEquals(http.shouldHandle(), false);

      assertEquals(Y.toString(socket), "");
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private void badRequest(Object request) {
    shouldNot(
        request,

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

  private void should(Object request, String response) {
    Socket socket;
    socket = Y.socket(request);

    final int bufferInitial; // force many buffer resizes
    bufferInitial = 2;

    try (HttpExchange http = new HttpExchange(socket, bufferInitial, 512, Y.clockFixed(), TestingNoteSink.INSTANCE)) {
      assertEquals(http.shouldHandle(), true);

      http.ok(Media.Bytes.textPlain("OK\n"));

      assertEquals(http.shouldHandle(), false);

      assertEquals(Y.toString(socket), response);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private void shouldNot(Object request, String response) {
    Socket socket;
    socket = Y.socket(request);

    final int bufferInitial; // force many buffer resizes
    bufferInitial = 2;

    try (HttpExchange http = new HttpExchange(socket, bufferInitial, 512, Y.clockFixed(), TestingNoteSink.INSTANCE)) {
      assertEquals(http.shouldHandle(), false);

      assertEquals(Y.toString(socket), response);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

}