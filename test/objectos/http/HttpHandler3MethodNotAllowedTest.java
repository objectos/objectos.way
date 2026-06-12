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
import java.util.EnumSet;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import objectos.way.Y;
import objectox.http.RequestMethodEnum;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@SuppressWarnings("exports")
public class HttpHandler3MethodNotAllowedTest {

  @DataProvider
  public Iterator<RequestMethodEnum> methodProvider() {
    return Stream.of(RequestMethodEnum.VALUES).iterator();
  }

  @Test(dataProvider = "methodProvider")
  public void handle(RequestMethodEnum method) {
    final EnumSet<RequestMethodEnum> single;
    single = EnumSet.of(method);

    final EnumSet<RequestMethodEnum> allowed;
    allowed = EnumSet.complementOf(single);

    final HttpHandler handler;
    handler = new HttpHandler3MethodNotAllowed(allowed);

    final HttpExchange http;
    http = HttpExchange.create(opts -> {
      opts.clock(Y.clockFixed());

      opts.method(method);
    });

    assertEquals(http.processed(), false);

    handler.handle(http);

    assertEquals(http.processed(), true);

    assertEquals(http.toString(), """
    HTTP/1.1 405 Method Not Allowed\r
    Date: Wed, 28 Jun 2023 12:08:43 GMT\r
    Connection: close\r
    Content-Length: 0\r
    Allow: %s\r
    \r
    """.formatted(allowed.stream().sorted().map(Enum::name).collect(Collectors.joining(", "))));
  }

  @Test
  public void handleNot() {
    final EnumSet<RequestMethodEnum> allowed;
    allowed = EnumSet.of(RequestMethodEnum.GET);

    final HttpHandler handler;
    handler = new HttpHandler3MethodNotAllowed(allowed);

    final HttpExchange http;
    http = HttpExchange.create(opts -> opts.method(RequestMethodEnum.POST));

    http.send();

    assertEquals(http.processed(), true);

    handler.handle(http);

    assertEquals(http.processed(), true);

    assertEquals(http.toString(), """
    HTTP/1.1 200 OK\r
    \r
    """);
  }

  @Test(dataProvider = "methodProvider")
  public void handleNot(RequestMethodEnum method) {
    final EnumSet<RequestMethodEnum> allowed;
    allowed = EnumSet.of(method);

    final HttpHandler handler;
    handler = new HttpHandler3MethodNotAllowed(allowed);

    final HttpExchange http;
    http = HttpExchange.create(opts -> {
      opts.clock(Y.clockFixed());

      opts.method(method);
    });

    assertEquals(http.processed(), false);

    handler.handle(http);

    assertEquals(http.processed(), false);
  }

}
