/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
package objectox.http;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import objectox.http.TestingInput.RegularInput;
import org.testng.annotations.Test;

public class HttpExchangeTest {

  @Test
  public void http003() throws IOException {
    HttpExchange exchange;
    exchange = new HttpExchange();

    Http003.INPUT.accept(exchange);

    assertTrue(exchange.active());

    Http003.response(exchange);

    assertFalse(exchange.active());

    TestableSocket socket;
    socket = (TestableSocket) exchange.socket;

    assertEquals(socket.outputAsString(), Http003.OUTPUT);
  }

  @Test
  public void http004() throws IOException {
    HttpExchange exchange;
    exchange = new HttpExchange();

    Http004.INPUT.accept(exchange);

    assertTrue(exchange.active());

    Http004.response(exchange);

    assertTrue(exchange.active());

    Http004.response(exchange);

    assertFalse(exchange.active());

    TestableSocket socket;
    socket = (TestableSocket) exchange.socket;

    assertEquals(socket.outputAsString(), Http004.OUTPUT);
  }

  @Test
  public void resolveAgainst() {
    String tmpdir;
    tmpdir = System.getProperty("java.io.tmpdir");

    Path tmp;
    tmp = Path.of(tmpdir);

    record Pair(String path, Path expected) {}

    List<Pair> pairs = List.of(
        new Pair("/index.html", tmp.resolve("index.html")),
        new Pair("/./index.html", tmp.resolve("index.html")),
        new Pair("/foo/index.html", tmp.resolve(Path.of("foo", "index.html"))),
        new Pair("/foo/../foo/../foo/index.html", tmp.resolve(Path.of("foo", "index.html")))
    );

    for (var pair : pairs) {
      Path result;
      result = resolveAgainst(tmp, pair.path);

      assertEquals(result, pair.expected);
    }
  }

  private Path resolveAgainst(Path directory, String path) {
    String input = """
        GET %s HTTP/1.1
        Host: www.example.com
        Connection: close

        """.formatted(path);

    HttpExchange exchange;
    exchange = ofInput(input);

    assertTrue(exchange.active());

    return exchange.resolveAgainst(directory);
  }

  private HttpExchange ofInput(String s) {
    String request;
    request = s.replace("\n", "\r\n");

    RegularInput input;
    input = new RegularInput(request);

    HttpExchange exchange;
    exchange = new HttpExchange();

    input.accept(exchange);

    return exchange;
  }

}