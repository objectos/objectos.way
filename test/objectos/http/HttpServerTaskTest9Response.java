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
import java.util.EnumSet;
import java.util.Iterator;
import objectos.lang.Throwables;
import objectos.way.Y;
import objectos.y.SocketY;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HttpServerTaskTest9Response {

  @SuppressWarnings("exports")
  @DataProvider
  public Iterator<Y.MediaKind> mediaKindProvider() {
    return EnumSet.allOf(Y.MediaKind.class).iterator();
  }

  // 4xx responses

  @Test(description = "error(status)")
  public void error01() {
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = SocketY.of("""
          POST /1 HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """);

          opts.handler = http -> http.error(HttpStatus.BAD_REQUEST);
        }),

        """
        HTTP/1.1 400 Bad Request\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Connection: close\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 16\r
        \r
        400 Bad Request
        """
    );
  }

  @Test(description = "erorr(status, msg)")
  public void error02() {
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = SocketY.of("""
          POST /1 HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """);

          opts.handler = http -> http.error(HttpStatus.FORBIDDEN, "Invalid credentials");
        }),

        """
        HTTP/1.1 403 Forbidden\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Connection: close\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 35\r
        \r
        403 Forbidden

        Invalid credentials
        """
    );
  }

  @Test(description = "error(status, e)")
  public void error03() {
    final IOException e;
    e = Throwables.trimStackTrace(new IOException(), 1);

    final StackTraceElement[] copy;
    copy = new StackTraceElement[1];

    copy[0] = new StackTraceElement("objectos.way.Test", "error", "Test.java", 123);

    e.setStackTrace(copy);

    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = SocketY.of("""
          POST /1 HTTP/1.1\r
          Host: www.example.com\r
          Connection: close\r
          \r
          """);

          opts.handler = http -> http.error(HttpStatus.INTERNAL_SERVER_ERROR, e);
        }),

        """
        HTTP/1.1 500 Internal Server Error\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Connection: close\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 90\r
        \r
        500 Internal Server Error

        java.io.IOException
        	at objectos.way.Test.error(Test.java:123)
        """
    );
  }

}