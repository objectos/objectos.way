/*
 * Copyright (C) 2023 Objectos Software LTDA.
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
package objectos.http.server;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.time.ZonedDateTime;
import objectos.http.Http;
import objectos.http.Method;
import objectos.http.server.HttpExchange.Processed;
import objectos.notes.NoOpNoteSink;
import objectos.notes.Note;
import objectos.notes.Note1;
import objectos.notes.NoteSink;
import objectox.http.StandardHeaderName;
import objectox.http.server.Bytes;
import objectox.http.server.Http001;
import objectox.http.server.ObjectoxHttpServer;
import objectox.http.server.TestableSocket;
import objectox.http.server.TestingInput.RegularInput;
import org.testng.annotations.Test;

public class HttpExchangeTest {

  @Test(description = """
  GET / HTTP/1.1
  Host: www.example.com
  Connection: close
  """)
  public void testCase001() throws IOException {
    TestableSocket socket;
    socket = TestableSocket.of("""
    GET / HTTP/1.1\r
    Host: www.example.com\r
    Connection: close\r
    \r
    """);

    HttpExchange exchange;
    exchange = create(socket);

    // must always return true
    assertEquals(exchange.hasNext(), true);

    // request phase

    ServerExchangeResult result;
    result = exchange.next();

    if (!(result instanceof ServerRequest req)) {
      throw new AssertionError("Expected ServerRequest but got " + result.getClass());
    }

    // request line
    UriPath path;
    path = req.path();

    assertEquals(req.method(), Method.GET);
    assertEquals(path.is("/"), true);

    // headers
    ServerRequestHeaders headers;
    headers = req.headers();

    assertEquals(headers.size(), 2);
    assertEquals(headers.first(StandardHeaderName.HOST), "www.example.com");
    assertEquals(headers.first(StandardHeaderName.CONNECTION), "close");

    // body
    ServerRequestBody body;
    body = req.body();

    byte[] bytes;
    bytes = ObjectoxHttpServer.readAllBytes(body);

    assertEquals(bytes.length, 0);

    // response phase
    /*
    byte[] msg;
    msg = "Hello World!\n".getBytes(StandardCharsets.UTF_8);
    
    ServerResponse resp;
    resp = req.response();
    
    resp.ok();
    
    resp.contentType("text/plain; charset=utf-8");
    
    resp.contentLength(msg.length);
    
    resp.dateNow();
    
    resp.send(msg);
    
    assertEquals(
        socket.outputAsString(),
    
        """
        HTTP/1.1 200 OK\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 14\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        \r
        Hello World!
        """
    );
    
    assertEquals(exchange.hasNext(), false);
    */
  }

  private HttpExchange create(TestableSocket socket) {
    HttpExchange exchange;
    exchange = HttpExchange.create(socket);

    exchange.bufferSize(128);

    exchange.noteSink(TestingNoteSink.INSTANCE);

    return exchange;
  }

  @Test
  public void http001() throws IOException {
    RegularInput input;
    input = Http001.INPUT;

    TestableSocket socket;
    socket = TestableSocket.of(input.request());

    var noteSink = new NoOpNoteSink() {
      Processed processed;

      @Override
      public boolean isEnabled(Note note) { return true; }

      @Override
      public <T1> void send(Note1<T1> note, T1 v1) {
        if (note == HttpExchange.PROCESSED) {
          processed = (Processed) v1;
        }
      }
    };

    HttpExchange exchange;
    exchange = HttpExchange.of(socket, HttpExchange.Option.noteSink(noteSink));

    try (exchange) {
      assertTrue(exchange.active());

      assertEquals(exchange.method(), Http.Method.GET);

      assertEquals(exchange.path(), "/");

      assertFalse(exchange.hasResponse());

      byte[] bytes;
      bytes = Bytes.utf8("Hello World!\n");

      exchange.status(Http.Status.OK_200);

      exchange.header(Http.Header.CONTENT_TYPE, "text/plain; charset=utf-8");

      exchange.header(Http.Header.CONTENT_LENGTH, Long.toString(bytes.length));

      ZonedDateTime date;
      date = Http001.DATE;

      exchange.header(Http.Header.DATE, Http.formatDate(date));

      exchange.body(bytes);

      assertTrue(exchange.hasResponse());

      assertFalse(exchange.active());

      Processed processed;
      processed = noteSink.processed;

      assertNotNull(processed);
      assertEquals(processed.method(), Http.Method.GET);
      assertEquals(processed.status(), Http.Status.OK_200);
    }

    assertEquals(socket.outputAsString(), Http001.OUTPUT);
  }

  @Test
  public void unknownRequestHeaders() throws IOException {
    TestableSocket socket;
    socket = TestableSocket.of("""
		GET / HTTP/1.1
		Host: www.example.com
		Connection: close
		Foo: bar

		""".replace("\n", "\r\n"));

    NoteSink noteSink;
    noteSink = TestingNoteSink.INSTANCE;

    HttpExchange exchange;
    exchange = HttpExchange.of(socket, HttpExchange.Option.noteSink(noteSink));

    try (exchange) {
      assertTrue(exchange.active());

      assertEquals(exchange.header(Http.Header.HOST).toString(), "www.example.com");
      assertEquals(exchange.header(Http.Header.CONNECTION).toString(), "close");
      assertEquals(exchange.method(), Http.Method.GET);
      assertEquals(exchange.path(), "/");
    }
  }

}