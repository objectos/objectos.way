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
import java.util.stream.Stream;
import org.testng.annotations.Test;

public class HttpExchangeTest7ShouldHandle {

  private record Tuple(String request, Media media) {}

  private final Media.Bytes ok = Media.Bytes.textPlain("OK");

  @Test
  public void shouldHandle01() {
    test(
        xch("""
        GET /1 HTTP/1.1\r
        Host: host\r
        Connection: close\r
        \r
        """, ok)
    );
  }

  @Test
  public void shouldHandle02() {
    test(
        xch("""
        GET /1 HTTP/1.1\r
        Host: host\r
        \r
        """, ok),

        xch("""
        GET /2 HTTP/1.1\r
        Host: host\r
        Connection: close\r
        \r
        """, ok)
    );
  }

  private Tuple xch(String req, Media resp) {
    return new Tuple(req, resp);
  }

  private void test(Tuple... tuples) {
    final Object[] data;
    data = Stream.of(tuples).map(Tuple::request).toArray();

    final Socket socket;
    socket = Y.socket(data);

    try (HttpExchange http = new HttpExchange(socket, 256, 512, TestingClock.FIXED, TestingNoteSink.INSTANCE)) {
      for (int count = 0, len = tuples.length; count < len; count++) {
        assertEquals(http.shouldHandle(), true);

        final Tuple t;
        t = tuples[count];

        final Media m;
        m = t.media;

        if (m instanceof Media.Bytes b) {
          http.ok(b);
        } else if (m instanceof Media.Stream) {
          throw new UnsupportedOperationException("Implement me");
        } else if (m instanceof Media.Text) {
          throw new UnsupportedOperationException("Implement me");
        } else {
          throw new UnsupportedOperationException("Implement me");
        }
      }

      assertEquals(http.shouldHandle(), false);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

}