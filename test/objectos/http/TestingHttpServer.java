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
package objectos.http;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import objectos.notes.Note;
import objectos.way.Http;
import objectos.way.TestingClock;
import objectos.way.TestingNoteSink;
import objectos.way.TestingShutdownHook;

final class TestingHttpServer {

  private TestingHttpServer() {}

  public static void bindHttpModuleTest(HttpModuleTest test) {
    ServerHolder.bindHttpModuleTest(test);
  }

  public static void bindHttpServerTest(HttpServerTest test) {
    ServerHolder.bindHttpServerTest(test);
  }

  public static Socket newSocket() throws IOException {
    HttpServer server;
    server = ServerHolder.SERVER;

    InetAddress address;
    address = server.address();

    int port;
    port = server.port();

    return new Socket(address, port);
  }

  public static void test(Socket socket, String request, String expectedResponse) throws IOException {
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

  private static class ServerHolder {

    static HttpServer SERVER = create();

    static ThisHandlerFactory HANDLER;

    public static void bindHttpModuleTest(HttpModuleTest test) {
      HANDLER.httpModuleTest = test.compile();
    }

    public static void bindHttpServerTest(HttpServerTest test) {
      HANDLER.httpServerTest = test;
    }

    private static HttpServer create() {
      try {
        return create0();
      } catch (IOException e) {
        throw new UncheckedIOException("Failed to init HttpServer", e);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();

        throw new RuntimeException("Failed to init HttpServer", e);
      }
    }

    private static HttpServer create0() throws IOException, InterruptedException {
      HANDLER = new ThisHandlerFactory();

      WayHttpServer wayServer;
      wayServer = new WayHttpServer(HANDLER);

      TestingShutdownHook.register(wayServer);

      wayServer.clock(TestingClock.FIXED);

      wayServer.noteSink(new ThisNoteSink(wayServer));

      wayServer.port(0);

      wayServer.start();

      synchronized (wayServer) {
        wayServer.wait();
      }

      return wayServer;
    }

  }

  private static class ThisHandlerFactory implements Http.Handler, HandlerFactory {

    private Http.Handler httpModuleTest;

    private Http.Handler httpServerTest;

    private final Http.Handler marketing = new MarketingSite().compile();

    @Override
    public final Http.Handler create() throws Exception {
      return this;
    }

    @Override
    public final void handle(Http.Exchange http) {
      ServerRequestHeaders headers;
      headers = http.headers();

      String host;
      host = headers.first(Http.HOST);

      switch (host) {
        case "http.module.test" -> httpModuleTest.handle(http);

        case "http.server.test" -> httpServerTest.handle(http);

        case "marketing" -> marketing.handle(http);

        default -> http.notFound();
      }
    }

  }

  private static class ThisNoteSink extends TestingNoteSink {

    private final Object lock;

    public ThisNoteSink(Object lock) { this.lock = lock; }

    @Override
    protected void visitNote(Note note) {
      if (note == HttpServer.LISTENING) {
        synchronized (lock) {
          lock.notify();
        }
      }
    }

  }

}