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
package objectox.http.srv;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import objectos.http.Content;
import objectos.http.MediaType;
import objectos.lang.Throwables;
import objectos.way.Y;
import objectos.y.SocketY;
import objectox.http.RequestMethodEnum;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@SuppressWarnings("exports")
public class ServerTaskTest1Method {

  private final Content ok = Content.of(MediaType.TEXT_PLAIN, "OK\n");

  @DataProvider
  public Object[][] methodProvider() {
    final RequestMethodEnum[] values;
    values = RequestMethodEnum.VALUES;

    final Object[][] methods;
    methods = new Object[values.length][];

    for (int idx = 0; idx < values.length; idx++) {
      methods[idx] = new Object[] {values[idx]};
    }

    return methods;
  }

  @Test(dataProvider = "methodProvider")
  public void valid(RequestMethodEnum method) {
    assertEquals(
        ServerTaskY.resp(opts -> {
          opts.host("www.example.com", req -> {
            assertEquals(req.method(), method);

            return ok;
          });

          opts.socket("""
          %s /index.html HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """.formatted(method));
        }),

        method.implemented
            ? method != RequestMethodEnum.HEAD
                ? """
                  HTTP/1.1 200 OK\r
                  Date: Wed, 28 Jun 2023 12:08:43 GMT\r
                  Content-Type: text/plain; charset=utf-8\r
                  Content-Length: 3\r
                  \r
                  OK
                  """
                : """
                  HTTP/1.1 200 OK\r
                  Date: Wed, 28 Jun 2023 12:08:43 GMT\r
                  Content-Type: text/plain; charset=utf-8\r
                  Content-Length: 3\r
                  \r
                  """
            : """
              HTTP/1.1 501 Not Implemented\r
              Date: Wed, 28 Jun 2023 12:08:43 GMT\r
              Connection: close\r
              Content-Type: text/plain; charset=utf-8\r
              Content-Length: 56\r
              \r
              The requested method is not implemented by this server.
              """
    );
  }

  @Test(dataProvider = "methodProvider", description = "valid + slow client")
  public void validSlowClient(RequestMethodEnum method) {
    assertEquals(
        ServerTaskY.resp(opts -> {
          opts.host("www.example.com", req -> {
            assertEquals(req.method(), method);

            return ok;
          });

          opts.socket(Y.slowStream(1, """
          %s /index.html HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """.formatted(method)));
        }),

        method.implemented
            ? method != RequestMethodEnum.HEAD
                ? """
                  HTTP/1.1 200 OK\r
                  Date: Wed, 28 Jun 2023 12:08:43 GMT\r
                  Content-Type: text/plain; charset=utf-8\r
                  Content-Length: 3\r
                  \r
                  OK
                  """
                : """
                  HTTP/1.1 200 OK\r
                  Date: Wed, 28 Jun 2023 12:08:43 GMT\r
                  Content-Type: text/plain; charset=utf-8\r
                  Content-Length: 3\r
                  \r
                  """
            : """
              HTTP/1.1 501 Not Implemented\r
              Date: Wed, 28 Jun 2023 12:08:43 GMT\r
              Connection: close\r
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
        ServerTaskY.resp(opts -> {
          opts.socket(data);
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
    var noteSink = new ServerTaskYNoteSink();

    final IOException ioe;
    ioe = Throwables.trimStackTrace(new IOException(), 1);

    assertEquals(
        ServerTaskY.resp(opts -> {
          opts.noteSink = noteSink;

          opts.socket = SocketY.of("GE", ioe);
        }),

        ""
    );

    assertEquals(noteSink.thrown, ioe);
  }

}