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
package objectox.http.server;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import objectos.http.Http.Method;
import objectos.http.Http.Status;
import objectos.http.server.TestingNoteSink;
import objectos.notes.NoteSink;
import objectox.http.server.TestingInput.RegularInput;
import org.testng.annotations.Test;

public class HttpExchangeTest {

  @Test
  public void http003() throws IOException {
    ObjectoxHttpExchange exchange;
    exchange = new ObjectoxHttpExchange();

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
    ObjectoxHttpExchange exchange;
    exchange = new ObjectoxHttpExchange();

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
  public void ofShouldSetTheNoteSink() throws IOException {
    TestableSocket socket;
    socket = TestableSocket.of();

    NoteSink noteSink;
    noteSink = TestingNoteSink.INSTANCE;

    objectos.http.server.HttpExchange exchange = objectos.http.server.HttpExchange.of(
        socket,

        objectos.http.server.HttpExchange.Option.noteSink(noteSink)
    );

    try (exchange) {
      ObjectoxHttpExchange x;
      x = (ObjectoxHttpExchange) exchange;

      assertSame(x.noteSink, noteSink);
    }
  }

  @Test
  public void methodIn() {
    ObjectoxHttpExchange get;
    get = ofMethod(Method.GET);

    assertEquals(get.methodIn(Method.GET, Method.POST), true);
    assertEquals(get.methodIn(Method.POST, Method.GET), true);
    assertEquals(get.methodIn(Method.POST, Method.PUT), false);

    ObjectoxHttpExchange post;
    post = ofMethod(Method.POST);

    assertEquals(post.methodIn(Method.GET, Method.POST), true);
    assertEquals(post.methodIn(Method.POST, Method.GET), true);
    assertEquals(post.methodIn(Method.DELETE, Method.PUT), false);
  }

  private ObjectoxHttpExchange ofMethod(Method method) {
    String input = """
        %s / HTTP/1.1
        Host: www.example.com
        Connection: close

        """.formatted(method.name());

    ObjectoxHttpExchange exchange;
    exchange = ofInput(input);

    assertTrue(exchange.active());

    return exchange;
  }

  @Test
  public void segmentsAsPath() {
    record Pair(String path, Path expected) {}

    List<Pair> pairs = List.of(
        new Pair("/index.html", Path.of("index.html")),
        new Pair("/index.html?v1", Path.of("index.html")),
        new Pair("/./index.html", Path.of("index.html")),
        new Pair("/foo/index.html", Path.of("foo", "index.html")),
        new Pair("/foo/../foo/../foo/index.html", Path.of("foo", "index.html"))
    );

    for (var pair : pairs) {
      Path result;
      result = segmentsAsPath(pair.path);

      assertEquals(result, pair.expected);
    }
  }

  private Path segmentsAsPath(String path) {
    String input = """
        GET %s HTTP/1.1
        Host: www.example.com
        Connection: close

        """.formatted(path);

    ObjectoxHttpExchange exchange;
    exchange = ofInput(input);

    assertTrue(exchange.active());

    return exchange.segmentsAsPath();
  }

  @Test
  public void statusPresent() {
    ObjectoxHttpExchange http;
    http = ofMethod(Method.GET);

    assertEquals(http.statusPresent(), false);

    http.status(Status.OK_200);

    assertEquals(http.statusPresent(), true);
  }

  private ObjectoxHttpExchange ofInput(String s) {
    String request;
    request = s.replace("\n", "\r\n");

    RegularInput input;
    input = new RegularInput(request);

    ObjectoxHttpExchange exchange;
    exchange = new ObjectoxHttpExchange();

    input.accept(exchange);

    return exchange;
  }

}