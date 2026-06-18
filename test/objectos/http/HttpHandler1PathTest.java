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

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import objectos.way.Y;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HttpHandler1PathTest {

  private final HttpHandler delegate = http -> {
    final String test;
    test = http.pathParam("test");

    final String value;
    value = String.valueOf(test);

    final HeaderName testHeader;
    testHeader = HeaderName.of("Way-Test");

    http.status(Status.OK);

    http.header(testHeader, value);

    http.header(HeaderName.CONNECTION, "close");

    http.send();
  };

  @DataProvider
  public Object[][] handleProvider() {
    return new Object[][] {
        {
            new RouteMatcherExact("/foo"),
            Map.of(),
            "/foo", null
        },
        {
            new RouteMatcherList(List.of(
                new RouteMatcherRegion("/foo/"),
                new RouteMatcherParamLast("test")
            )),
            Map.of(),
            "/foo/123", "123"
        },
        {
            new RouteMatcherList(List.of(
                new RouteMatcherRegion("/foo/"),
                new RouteMatcherParamLast("test")
            )),
            Map.of("test", PathParams.digits()),
            "/foo/123", "123"
        }
    };
  }

  @SuppressWarnings("exports")
  @Test(dataProvider = "handleProvider")
  public void handle(RouteMatcher matcher, Map<String, Predicate<String>> predicates, String path, String test) {
    final HttpExchange http;
    http = HttpExchange.create(opts -> {
      opts.clock(Y.clockFixed());

      opts.path(path);
    });

    final HttpHandler handler;
    handler = new HttpHandler1Path(matcher, predicates, List.of(delegate));

    assertEquals(http.processed(), false);

    handler.handle(http);

    assertEquals(http.processed(), true);

    assertEquals(http.toString(), """
    HTTP/1.1 200 OK\r
    Way-Test: %s\r
    Connection: close\r
    \r
    """.formatted(test));
  }

  @DataProvider
  public Object[][] handleNotProvider() {
    return new Object[][] {
        {
            new RouteMatcherExact("/foo"),
            Map.of(),
            "/"
        },
        {
            new RouteMatcherList(List.of(
                new RouteMatcherRegion("/foo/"),
                new RouteMatcherParamLast("test")
            )),
            Map.of("test", PathParams.digits()),
            "/foo/xpto"
        }
    };
  }

  @SuppressWarnings("exports")
  @Test(dataProvider = "handleNotProvider")
  public void handleNot(RouteMatcher matcher, Map<String, Predicate<String>> predicates, String path) {
    final HttpExchange http;
    http = HttpExchange.create(opts -> {
      opts.clock(Y.clockFixed());

      opts.path(path);
    });

    final HttpHandler handler;
    handler = new HttpHandler1Path(matcher, predicates, List.of(delegate));

    assertEquals(http.processed(), false);

    handler.handle(http);

    assertEquals(http.processed(), false);
  }

}
