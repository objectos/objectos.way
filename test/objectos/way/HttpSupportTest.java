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

import java.util.function.Consumer;
import org.testng.annotations.Test;

public class HttpSupportTest {

  // 2xx responses

  @Test
  public void ok01() {
    final Media.Bytes media;
    media = Media.Bytes.textPlain("OK\n");

    test(
        http -> http.ok(media),

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

  // 4xx responses

  @Test
  public void badRequest01() {
    final Media.Bytes media;
    media = Media.Bytes.textPlain("Foo\n");

    test(
        http -> http.badRequest(media),

        """
        HTTP/1.1 400 Bad Request\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 4\r
        \r
        Foo
        """
    );
  }

  @Test
  public void notFound01() {
    final Media.Bytes media;
    media = Media.Bytes.textPlain("Not Found\n");

    test(
        http -> http.notFound(media),

        """
        HTTP/1.1 404 Not Found\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 10\r
        \r
        Not Found
        """
    );
  }

  private void test(Consumer<Http.Exchange> consumer, String expected) {
    final Http.Exchange http;
    http = Http.Exchange.create(config -> {
      config.clock(TestingClock.FIXED);
    });

    consumer.accept(http);

    assertEquals(http.toString(), expected);
  }

}