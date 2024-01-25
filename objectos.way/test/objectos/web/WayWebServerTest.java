/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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
package objectos.web;

import static org.testng.Assert.assertEquals;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;
import objectos.http.HeaderName;
import objectos.http.Status;
import objectos.http.server.Handler;
import objectos.http.server.HandlerFactory;
import objectos.http.server.ServerExchange;
import objectos.http.server.ServerRequestHeaders;
import objectos.http.server.UriPath;
import objectos.http.server.UriPath.Segment;
import objectos.notes.Note;
import objectos.way.TestingNoteSink;
import objectos.way.TestingShutdownHook;
import objectox.http.server.TestingClock;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class WayWebServerTest implements Handler {

  private WayWebServer server;

  @BeforeClass
  public void startServer() throws Exception {
    HandlerFactory factory;
    factory = () -> this;

    server = new WayWebServer(factory);

    TestingShutdownHook.register(server);

    server.bufferSize(128, 256);

    server.clock(TestingClock.FIXED);

    server.noteSink(new ThisNoteSink(server));

    server.start();

    synchronized (server) {
      server.wait();
    }
  }

  @Override
  public final void handle(ServerExchange http) {
    ServerRequestHeaders headers;
    headers = http.headers();

    String host;
    host = headers.first(HeaderName.HOST);

    switch (host) {
      case "www.example.com" -> handle0(http);

      default -> throw new UnsupportedOperationException("Implement me");
    }
  }

  private void handle0(ServerExchange http) {
    UriPath path;
    path = http.path();

    List<Segment> segments;
    segments = path.segments();

    switch (segments.size()) {
      case 2 -> handle1(http, segments);

      default -> http.notFound();
    }
  }

  private void handle1(ServerExchange http, List<Segment> segments) {
    Segment first;
    first = segments.getFirst();

    if (!first.is("test")) {
      http.notFound();

      return;
    }

    Segment second;
    second = segments.get(1);

    String methodName;
    methodName = second.value();

    try {
      Class<? extends WayWebServerTest> testClass;
      testClass = getClass();

      Method handlingMethod;
      handlingMethod = testClass.getDeclaredMethod(methodName, ServerExchange.class);

      handlingMethod.invoke(this, http);
    } catch (Exception e) {
      http.internalServerError(e);
    }
  }

  @SuppressWarnings("unused")
  private void testCase01(ServerExchange exchange) {
    exchange.methodMatrix(objectos.http.Method.GET, this::testCase01Get);
  }

  private void testCase01Get(ServerExchange http) {
    http.status(Status.OK);
    http.header(HeaderName.CONTENT_TYPE, "text/plain");
    http.header(HeaderName.CONTENT_LENGTH, 5);
    http.dateNow();
    http.send("TC01\n".getBytes(StandardCharsets.UTF_8));
  }

  @Test(description = """
  methodMatrix with 1 method:
  - accept declared method
  - reject other methods
  """)
  public void testCase01() throws Exception {
    test(
        server,

        """
        GET /test/testCase01 HTTP/1.1\r
        Host: www.example.com\r
        \r
        """,

        """
        HTTP/1.1 200 OK\r
        Content-Type: text/plain\r
        Content-Length: 5\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        \r
        TC01
        """
    );

    test(
        server,

        """
        POST /test/testCase01 HTTP/1.1\r
        Host: www.example.com\r
        Connection: close\r
        \r
        """,

        """
        HTTP/1.1 405 METHOD NOT ALLOWED\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Connection: close\r
        \r
        """
    );
  }

  private void test(WayWebServer server, String request, String expectedResponse) throws Exception {
    InetAddress address;
    address = server.address();

    int port;
    port = server.port();

    try (Socket socket = new Socket(address, port)) {
      OutputStream out;
      out = socket.getOutputStream();

      byte[] requestBytes;
      requestBytes = request.getBytes(StandardCharsets.UTF_8);

      out.write(requestBytes);

      byte[] expectedBytes;
      expectedBytes = expectedResponse.getBytes(StandardCharsets.UTF_8);

      InputStream in;
      in = socket.getInputStream();

      byte[] responseBytes;
      responseBytes = in.readNBytes(expectedBytes.length);

      String res;
      res = new String(responseBytes, StandardCharsets.UTF_8);

      assertEquals(res, expectedResponse);
    }
  }

  private static class ThisNoteSink extends TestingNoteSink {

    private final Object lock;

    public ThisNoteSink(Object lock) { this.lock = lock; }

    @Override
    protected void visitNote(Note note) {
      if (note == WebServer.LISTENING) {
        synchronized (lock) {
          lock.notify();
        }
      }
    }

  }

}