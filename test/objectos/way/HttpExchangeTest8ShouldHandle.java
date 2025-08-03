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
import java.util.function.Consumer;
import java.util.stream.Stream;
import org.testng.annotations.Test;

public class HttpExchangeTest8ShouldHandle {

  private record Tuple(String request, boolean should, Consumer<HttpExchange> handler, String expectedResponse) {}

  @Test(description = "should: close")
  public void shouldHandle01() {
    test(
        xch("""
        GET /1 HTTP/1.1\r
        Host: host\r
        Connection: close\r
        \r
        """, http -> http.ok(Media.Bytes.textPlain("1\n")), """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 2\r
        \r
        1
        """)
    );
  }

  @Test(description = "should: keep-alive + close")
  public void shouldHandle02() {
    test(
        xch("""
        GET /1 HTTP/1.1\r
        Host: host\r
        \r
        """, http -> http.ok(Media.Bytes.textPlain("1\n")), """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 2\r
        \r
        1
        """),

        xch("""
        GET /2 HTTP/1.1\r
        Host: host\r
        Connection: close\r
        \r
        """, http -> http.ok(Media.Bytes.textPlain("2\n")), """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 2\r
        \r
        2
        """)
    );
  }

  @Test(description = "should: keep-alive (explicit) + close")
  public void shouldHandle03() {
    test(
        xch("""
        GET /1 HTTP/1.1\r
        Host: host\r
        Connection: keep-alive\r
        \r
        """, http -> http.ok(Media.Bytes.textPlain("1\n")), """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 2\r
        \r
        1
        """),

        xch("""
        GET /2 HTTP/1.1\r
        Host: host\r
        Connection: close\r
        \r
        """, http -> http.ok(Media.Bytes.textPlain("2\n")), """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 2\r
        \r
        2
        """)
    );
  }

  @Test(description = "should not: bad request")
  public void shouldNot01() {
    test(
        not("""
        GET bad HTTP/1.1\r
        Host: host\r
        \r
        """, http -> {}, """
        HTTP/1.1 400 Bad Request\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 22\r
        Connection: close\r
        \r
        Invalid request line.
        """)
    );
  }

  private void shouldNot02(HttpExchange http) {
    http.status(Http.Status.OK);
    http.header(Http.HeaderName.CONTENT_LENGTH, "0");
    http.header(Http.HeaderName.CONNECTION, "close");
    http.send();
  }

  @Test(description = "should not: explicit close in response")
  public void shouldNot02() {
    test(
        xch("""
        GET / HTTP/1.1\r
        Host: host\r
        \r
        """, this::shouldNot02, """
        HTTP/1.1 200 OK\r
        Content-Length: 0\r
        Connection: close\r
        \r
        """)
    );
  }

  @Test(description = "should not: no host header")
  public void shouldNot03() {
    test(
        not("""
        GET / HTTP/1.1\r
        Referer: x\r
        User-Agent: x\r
        \r
        """, http -> {}, """
        HTTP/1.1 400 Bad Request\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 21\r
        Connection: close\r
        \r
        Missing Host header.
        """)
    );
  }

  @Test(description = "should not: empty host header")
  public void shouldNot04() {
    test(
        not("""
        GET / HTTP/1.1\r
        Host: \r
        Referer: x\r
        User-Agent: x\r
        \r
        """, http -> {}, """
        HTTP/1.1 400 Bad Request\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 21\r
        Connection: close\r
        \r
        Missing Host header.
        """)
    );
  }

  private void test(Tuple... tuples) {
    final Object[] data;
    data = Stream.of(tuples).map(Tuple::request).toArray();

    final Socket socket;
    socket = Y.socket(data);

    String previousResponse;
    previousResponse = null;

    try (HttpExchange http = Y.http(socket, 256, 512, Y.clockFixed(), Y.noteSink(), 0L)) {
      for (int count = 0, len = tuples.length; count < len; count++) {
        final Tuple t;
        t = tuples[count];

        assertEquals(http.shouldHandle(), t.should);

        if (previousResponse != null) {
          assertEquals(Y.toString(socket), previousResponse);
        }

        previousResponse = t.expectedResponse;

        t.handler.accept(http);
      }

      assertEquals(http.shouldHandle(), false);

      if (previousResponse != null) {
        assertEquals(Y.toString(socket), previousResponse);
      }
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private Tuple xch(String req, Consumer<HttpExchange> handler, String expected) {
    return new Tuple(req, true, handler, expected);
  }

  private Tuple not(String req, Consumer<HttpExchange> handler, String expected) {
    return new Tuple(req, false, handler, expected);
  }

}