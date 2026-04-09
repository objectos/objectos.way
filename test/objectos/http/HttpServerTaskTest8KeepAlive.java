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

import objectos.way.Media;
import objectos.way.Y;
import org.testng.annotations.Test;

public class HttpServerTaskTest8KeepAlive {

  @Test
  public void shouldHandle01() {
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = Y.socket("""
          GET /1 HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """);

          opts.handler = http -> http.ok(Media.Bytes.textPlain("1"));
        }),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 1\r
        \r
        1\
        """
    );
  }

  @Test
  public void shouldHandle02() {
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = Y.socket("""
          GET /1 HTTP/1.1\r
          Host: www.example.com\r
          \r
          """, """
          GET /2 HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """);

          opts.handler = http -> http.ok(Media.Bytes.textPlain(http.path().substring(1)));
        }),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 1\r
        \r
        1\
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 1\r
        \r
        2\
        """
    );
  }

  @Test
  public void shouldHandle03() {
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = Y.socket("""
          GET /1 HTTP/1.1\r
          Connection: keep-alive\r
          Host: www.example.com\r
          \r
          """, """
          GET /2 HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """);

          opts.handler = http -> http.ok(Media.Bytes.textPlain(http.path().substring(1)));
        }),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 1\r
        \r
        1\
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 1\r
        \r
        2\
        """
    );
  }

  @Test(description = "should not: bad request")
  public void shouldNot01() {
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = Y.socket("""
          GET bad HTTP/1.1\r
          Host: www.example.com\r
          \r
          """);
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

  @Test(description = "explicit close in response")
  public void shouldNot02() {
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = Y.socket("""
          GET /1 HTTP/1.1\r
          Host: www.example.com\r
          \r
          """);

          opts.handler = http -> {
            http.status(HttpStatus.OK);
            http.header(HttpHeaderName.CONTENT_LENGTH, 0L);
            http.header(HttpHeaderName.CONNECTION, "close");
            http.send();
          };
        }),

        """
        HTTP/1.1 200 OK\r
        Content-Length: 0\r
        Connection: close\r
        \r
        """
    );
  }

  @Test(description = "should not: no host header")
  public void shouldNot03() {
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = Y.socket("""
          GET /1 HTTP/1.1\r
          Referer: x\r
          User-Agent: x\r
          \r
          """);
        }),

        """
        HTTP/1.1 400 Bad Request\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Connection: close\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 13\r
        \r
        Host header.
        """
    );
  }

  @Test(description = "should not: empty host header")
  public void shouldNot04() {
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = Y.socket("""
          GET /1 HTTP/1.1\r
          Host: \r
          Referer: x\r
          User-Agent: x\r
          \r
          """);
        }),

        """
        HTTP/1.1 400 Bad Request\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Connection: close\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 13\r
        \r
        Host header.
        """
    );
  }

  @Test(description = "should not: multiple host headers")
  public void shouldNot05() {
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = Y.socket("""
          GET /1 HTTP/1.1\r
          Host: www.example.com\r
          Host: example.com\r
          Referer: x\r
          User-Agent: x\r
          \r
          """);
        }),

        """
        HTTP/1.1 400 Bad Request\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Connection: close\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 13\r
        \r
        Host header.
        """
    );
  }

}