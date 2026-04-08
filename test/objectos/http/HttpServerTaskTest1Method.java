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

import java.io.IOException;
import objectos.way.Media;
import objectos.way.Y;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HttpServerTaskTest1Method {

  @DataProvider
  public Object[][] methodProvider() {
    final HttpMethod[] values;
    values = HttpMethod.VALUES;

    final Object[][] methods;
    methods = new Object[values.length][];

    for (int idx = 0; idx < values.length; idx++) {
      methods[idx] = new Object[] {values[idx]};
    }

    return methods;
  }

  @Test(dataProvider = "methodProvider")
  public void valid(HttpMethod method) {
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = Y.socket("""
          %s /index.html HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """.formatted(method));

          opts.handler = http -> {
            assertEquals(http.method(), method);

            http.ok(Media.Bytes.textPlain("OK\n"));
          };
        }),

        method.implemented
            ? """
              HTTP/1.1 200 OK\r
              Date: Wed, 28 Jun 2023 12:08:43 GMT\r
              Content-Type: text/plain; charset=utf-8\r
              Content-Length: 3\r
              \r
              OK
              """
            : """
              HTTP/1.1 501 Not Implemented\r
              Date: Wed, 28 Jun 2023 12:08:43 GMT\r
              Content-Type: text/plain; charset=utf-8\r
              Content-Length: 56\r
              \r
              The requested method is not implemented by this server.
              """
    );
  }

  @Test(dataProvider = "methodProvider", description = "valid + slow client")
  public void validSlowClient(HttpMethod method) {
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = Y.socket(Y.slowStream(1, """
          %s /index.html HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """.formatted(method)));

          opts.handler = http -> {
            assertEquals(http.method(), method);

            http.ok(Media.Bytes.textPlain("OK\n"));
          };
        }),

        method.implemented
            ? """
              HTTP/1.1 200 OK\r
              Date: Wed, 28 Jun 2023 12:08:43 GMT\r
              Content-Type: text/plain; charset=utf-8\r
              Content-Length: 3\r
              \r
              OK
              """
            : """
              HTTP/1.1 501 Not Implemented\r
              Date: Wed, 28 Jun 2023 12:08:43 GMT\r
              Content-Type: text/plain; charset=utf-8\r
              Content-Length: 56\r
              \r
              The requested method is not implemented by this server.
              """
    );
  }

  @DataProvider
  public Object[][] badRequestProvider() {
    return new Object[][] {
        {
            "",
            "empty"
        },
        {
            """
            XYZ /path?key=value HTTP/1.1\r
            Host: www.example.com\r
            \r
            """,
            "Unknown method"
        },
        {
            """
            xyz\r
            \r
            """,
            "No method, garbage"
        },
        {
            """
            POS /login HTTP/1.1\r
            Host: www.example.com\r
            \r
            """,
            "Incomplete method name"
        }
    };
  }

  @Test(dataProvider = "badRequestProvider")
  public void badRequest(String data, String description) {
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = Y.socket(data);
        }),

        """
        HTTP/1.1 400 Bad Request\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Connection: close\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 22\r
        \r
        Invalid request line.
        """
    );
  }

  @Test
  public void ioException() {
    var noteSink = new HttpServerTaskYNoteSink();

    final IOException ioe;
    ioe = Y.trimStackTrace(new IOException(), 1);

    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.id = 123L;

          opts.noteSink = noteSink;

          opts.socket = Y.socket("GE", ioe);
        }),

        ""
    );

    assertEquals(noteSink.id, 123L);
    assertEquals(noteSink.event, "socket.read");
    assertEquals(noteSink.thrown, ioe);
  }

}