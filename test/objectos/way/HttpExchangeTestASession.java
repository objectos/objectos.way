/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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

public class HttpExchangeTestASession {

  @Test
  public void session01() {
    final Http.SessionStore store;
    store = Http.SessionStore.create(options -> {
      options.randomGenerator(Y.randomGeneratorOfLongs(1L, 2L, 3L, 4L));
    });

    test(
        xch(
            """
            GET /1 HTTP/1.1\r
            Host: host\r
            \r
            """,

            http -> {
              store.ensureSession(http);

              http.sessionAttr(String.class, () -> "FOO\n");

              http.ok(Media.Bytes.textPlain("NEW\n"));
            },

            """
            HTTP/1.1 200 OK\r
            Date: Wed, 28 Jun 2023 12:08:43 GMT\r
            Content-Type: text/plain; charset=utf-8\r
            Content-Length: 4\r
            Set-Cookie: OBJECTOSWAY=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=; HttpOnly; Path=/; Secure\r
            \r
            NEW
            """
        ),

        xch(
            """
            GET /1 HTTP/1.1\r
            Host: host\r
            Cookie: OBJECTOSWAY=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=\r
            \r
            """,

            http -> {
              store.loadSession(http);

              final String attr;
              attr = http.sessionAttr(String.class);

              http.ok(Media.Bytes.textPlain(attr));
            },

            """
            HTTP/1.1 200 OK\r
            Date: Wed, 28 Jun 2023 12:08:43 GMT\r
            Content-Type: text/plain; charset=utf-8\r
            Content-Length: 4\r
            \r
            FOO
            """
        )
    );
  }

  private void test(Tuple... tuples) {
    final Object[] data;
    data = Stream.of(tuples).map(Tuple::request).toArray();

    final Socket socket;
    socket = Y.socket(data);

    String previousResponse;
    previousResponse = null;

    try (HttpExchange http = new HttpExchange(socket, 256, 512, Y.clockFixed(), TestingNoteSink.INSTANCE)) {
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

  private record Tuple(String request, boolean should, Consumer<HttpExchange> handler, String expectedResponse) {}

  private Tuple xch(String req, Consumer<HttpExchange> handler, String expected) {
    return new Tuple(req, true, handler, expected);
  }

}