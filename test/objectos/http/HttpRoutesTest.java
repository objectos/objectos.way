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

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import objectos.way.Y;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HttpRoutesTest {

  @DataProvider
  public Object[][] handleProvider() {
    final HttpHandler ok;
    ok = http -> { http.status(HttpStatus.OK); http.send(); };

    return new Object[][] {
        {
            "Empty configuration",

            HttpHandler.of(_ -> {}),

            """
            GET /test HTTP/1.1\r
            Host: www.example.com\r
            \r
            """,

            """
            HTTP/1.1 404 Not Found\r
            Date: Wed, 28 Jun 2023 12:08:43 GMT\r
            Connection: close\r
            Content-Type: text/plain; charset=utf-8\r
            Content-Length: 14\r
            \r
            404 Not Found
            """
        },
        {
            "Option:Handler",

            HttpHandler.of(r -> {
              r.at("/test", ok);
            }),

            """
            GET /test HTTP/1.1\r
            Host: www.example.com\r
            \r
            """,

            """
            HTTP/1.1 200 OK\r
            \r
            """
        }
    };
  }

  private final HttpHandler ok = http -> { http.status(HttpStatus.OK); http.send(); };

  @Test(description = "Empty configuration")
  public void empty() {
    test(
        _ -> {},

        """
        GET /test HTTP/1.1\r
        Host: www.example.com\r
        \r
        """,

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

  @DataProvider
  public Iterator<HttpMethod> methodProvider() {
    return Stream.of(HttpMethod.VALUES).filter(m -> m.implemented).iterator();
  }

  @Test(description = "Option:handler", dataProvider = "methodProvider")
  public void handler01(HttpMethod method) {
    test(
        r -> {
          r.at("/test", ok);
        },

        """
        %s /test HTTP/1.1\r
        Host: www.example.com\r
        \r
        """.formatted(method.name()),

        """
        HTTP/1.1 200 OK\r
        \r
        """
    );
  }

  private void test(Consumer<? super HttpRoutes> routes, String req, String resp) {
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.socket = Y.socket(req);

          opts.handler = HttpHandler.of(routes);
        }),

        resp
    );
  }

}
