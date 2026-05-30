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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import objectos.way.Html;
import objectos.way.Y;
import org.testng.annotations.Test;

public class ResponseWriterTest {

  private void test(HttpMethod method, Response r, String expected) throws IOException {
    final byte[] buffer;
    buffer = new byte[64];

    final ByteArrayOutputStream outputStream;
    outputStream = new ByteArrayOutputStream();

    final ResponseBuffered buffered;
    buffered = new ResponseBuffered(buffer, outputStream);

    final Clock clock;
    clock = Y.clockFixed();

    final ResponseDate date;
    date = new ResponseDate(clock);

    final boolean head;
    head = method == HttpMethod.HEAD;

    final ResponsePojo impl;
    impl = (ResponsePojo) r;

    try (var output = new ResponseWriter(buffered, date, head, impl)) {
      output.write();
    }

    final byte[] bytes;
    bytes = outputStream.toByteArray();

    final String s;
    s = new String(bytes, StandardCharsets.US_ASCII);

    assertEquals(s, expected);
  }

  private final Html.Component html = (Html.Markup m) -> {
    m.html(
        m.body("Html.Component")
    );
  };

  @Test
  public void ok01() throws IOException {
    test(
        HttpMethod.GET,

        Response.ok(html),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/html; charset=utf-8\r
        Transfer-Encoding: chunked\r
        \r
        2B\r
        <html>
        <body>Html.Component</body>
        </html>
        \r
        0\r
        \r
        """
    );
  }

  @Test
  public void ok02() throws IOException {
    test(
        HttpMethod.HEAD,

        Response.ok(html),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/html; charset=utf-8\r
        Transfer-Encoding: chunked\r
        \r
        """
    );
  }

}
