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

import objectos.way.Media;
import objectos.way.Y;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HttpRoutingTest {

  private final HttpHandler ok = http -> http.ok(Media.Bytes.textPlain("OK\n"));

  @Test
  public void empty01() {
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = Y.socket("""
          GET /test HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """);

          opts.handler = HttpHandler.create(_ -> {});
        }),

        """
        HTTP/1.1 404 Not Found\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Connection: close\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 14\r
        \r
        404 Not Found
        """
    );
  }

  @Test
  public void GET01() {
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = Y.socket("""
          GET /test HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """);

          opts.handler = HttpHandler.create(r -> r.GET(ok));
        }),

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
  public void GET02() {
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = Y.socket("""
          HEAD /test HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """);

          opts.handler = HttpHandler.create(r -> r.GET(ok));
        }),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 3\r
        \r
        """
    );
  }

  @Test
  public void GET03() {
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = Y.socket("""
          POST /test HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """);

          opts.handler = HttpHandler.create(r -> r.GET(ok));
        }),

        """
        HTTP/1.1 405 Method Not Allowed\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Connection: close\r
        Content-Length: 0\r
        Allow: GET, HEAD\r
        \r
        """
    );
  }

  @Test
  public void POST01() {
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = Y.socket("""
          POST /test HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """);

          opts.handler = HttpHandler.create(r -> r.POST(ok));
        }),

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
  public void POST02() {
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = Y.socket("""
          GET /test HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """);

          opts.handler = HttpHandler.create(r -> r.POST(ok));
        }),

        """
        HTTP/1.1 405 Method Not Allowed\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Connection: close\r
        Content-Length: 0\r
        Allow: POST\r
        \r
        """
    );
  }

  @DataProvider
  public Object[][] pathExactProvider() {
    return new Object[][] {
        {"/test", true},
        {"/t%65st", true},

        {"/tes", false},
        {"/test/", false},
        {"/testt", false}
    };
  }

  @Test(dataProvider = "pathExactProvider")
  public void pathExact(String path, boolean resp200) {
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = Y.socket("""
          GET %s HTTP/1.1\r
          Host: www.example.com\r
          \r
          """.formatted(path));

          opts.handler = HttpHandler.create(r -> {
            r.path("/test", p -> p.GET(ok));
          });
        }),

        resp200
            ? """
              HTTP/1.1 200 OK\r
              Date: Wed, 28 Jun 2023 12:08:43 GMT\r
              Content-Type: text/plain; charset=utf-8\r
              Content-Length: 3\r
              \r
              OK
              """
            : """
              HTTP/1.1 404 Not Found\r
              Date: Wed, 28 Jun 2023 12:08:43 GMT\r
              Connection: close\r
              Content-Type: text/plain; charset=utf-8\r
              Content-Length: 14\r
              \r
              404 Not Found
              """
    );
  }

}
