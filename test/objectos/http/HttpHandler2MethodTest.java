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
import java.util.List;
import java.util.stream.Stream;
import objectos.way.Media;
import objectos.way.Y;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HttpHandler2MethodTest {

  private final HttpHandler ok = http -> http.ok(Media.Bytes.textPlain("OK\n"));

  @DataProvider
  public Iterator<HttpMethod> methodProvider() {
    return Stream.of(HttpMethod.VALUES).iterator();
  }

  @Test(dataProvider = "methodProvider")
  public void handle(HttpMethod method) {
    final HttpExchange http;
    http = HttpExchange.create(opts -> {
      opts.clock(Y.clockFixed());

      opts.method(method);
    });

    final HttpHandler2Method handler;
    handler = of(method, ok);

    assertEquals(http.processed(), false);

    handler.handle(http);

    assertEquals(http.processed(), true);
  }

  @Test
  public void handleHead() {
    final HttpHandler2Method handler;
    handler = of(HttpMethod.GET, ok);

    final HttpExchange http;
    http = HttpExchange.create(opts -> {
      opts.clock(Y.clockFixed());

      opts.method(HttpMethod.HEAD);
    });

    assertEquals(http.processed(), false);

    handler.handle(http);

    assertEquals(http.processed(), true);

    assertEquals(http.toString(), """
    HTTP/1.1 200 OK\r
    Date: Wed, 28 Jun 2023 12:08:43 GMT\r
    Content-Type: text/plain; charset=utf-8\r
    Content-Length: 3\r
    \r
    """);
  }

  @Test
  public void handleNot() {
    final HttpHandler2Method handler;
    handler = of(HttpMethod.GET, ok);

    final HttpExchange http;
    http = HttpExchange.create(opts -> opts.method(HttpMethod.GET));

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
  public void handleNot(HttpMethod method) {
    final HttpHandler2Method handler;
    handler = of(method, ok);

    final EnumSet<HttpMethod> single;
    single = EnumSet.of(method);

    final EnumSet<HttpMethod> others;
    others = EnumSet.complementOf(single);

    final HttpMethod other;
    other = others.iterator().next();

    final HttpExchange http;
    http = HttpExchange.create(opts -> opts.method(other));

    assertEquals(http.processed(), false);

    handler.handle(http);

    assertEquals(http.processed(), false);
  }

  private HttpHandler2Method of(HttpMethod method, HttpHandler handler) {
    return new HttpHandler2Method(method, List.of(handler));
  }

}