/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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
package objectos.way;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import objectos.way.Http.Routing;

public final class TestingHttpServer {

  private TestingHttpServer() {}

  public static void bindHttpRoutingTest(HttpRoutingTest test) {
    ServerHolder.bindHttpRoutingTest(test);
  }

  public static void bindHttpServerTest(HttpServerTest test) {
    ServerHolder.bindHttpServerTest(test);
  }

  public static Socket newSocket() throws IOException {
    Http.Server server;
    server = ServerHolder.SERVER;

    InetAddress address;
    address = server.address();

    int port;
    port = server.port();

    return new Socket(address, port);
  }

  public static int port() {
    Http.Server server;
    server = ServerHolder.SERVER;

    return server.port();
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

    int offset;
    offset = 0;

    int remaining;
    remaining = expectedBytes.length;

    while (remaining > 0) {
      int read;
      read = in.read(expectedBytes, offset, remaining);

      if (read <= 0) {
        break;
      }

      offset += read;

      remaining -= read;
    }

    byte[] responseBytes;
    responseBytes = Arrays.copyOf(expectedBytes, offset);

    String res;
    res = new String(responseBytes, StandardCharsets.UTF_8);

    assertEquals(res, expectedResponse);
  }

  private static class ServerHolder {

    static Http.Server SERVER = create();

    static ThisHandlerFactory HANDLER;

    public static void bindHttpRoutingTest(HttpRoutingTest test) {
      HANDLER.httpModuleTest.delegate = Http.Handler.of(test);
    }

    public static void bindHttpServerTest(HttpServerTest test) {
      HANDLER.httpServerTest.delegate = Http.Handler.of(test);
    }

    private static Http.Server create() {
      try {
        return create0();
      } catch (IOException e) {
        throw new UncheckedIOException("Failed to init HttpServer", e);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();

        throw new RuntimeException("Failed to init HttpServer", e);
      }
    }

    private static Http.Server create0() throws IOException, InterruptedException {
      HANDLER = new ThisHandlerFactory();

      CountDownLatch serverStarted;
      serverStarted = new CountDownLatch(1);

      CountDownLatch noteReceived;
      noteReceived = new CountDownLatch(1);

      ThisNoteSink noteSink;
      noteSink = new ThisNoteSink(serverStarted, noteReceived);

      Http.Server wayServer;
      wayServer = Http.Server.create(config -> {
        final Http.Handler serverHandler;
        serverHandler = Http.Handler.of(HANDLER::configure);

        config.handler(serverHandler);

        config.clock(Y.clockFixed());

        config.noteSink(noteSink);

        config.port(0);
      });

      TestingShutdownHook.register(wayServer);

      wayServer.start();

      serverStarted.countDown();

      noteReceived.await();

      return wayServer;
    }

  }

  private static class ThisHandlerFactory {

    private final DelegatingHandler httpModuleTest = new DelegatingHandler();

    private final DelegatingHandler httpServerTest = new DelegatingHandler();

    private final MarketingSite marketing = new MarketingSite();

    public final void configure(Routing r) {
      r.when(req -> host(req, "http.module.test"), matched -> {
        matched.handler(httpModuleTest);
      });

      r.when(req -> host(req, "http.server.test"), matched -> {
        matched.handler(httpServerTest);
      });

      r.when(req -> host(req, "marketing"), marketing);
    }

    private boolean host(Http.Request req, String hostName) {
      final String host;
      host = req.header(Http.HeaderName.HOST);

      return hostName.equals(host);
    }

    private static final class DelegatingHandler implements Http.Handler {

      Http.Handler delegate;

      @Override
      public final void handle(Http.Exchange http) {
        delegate.handle(http);
      }

    }

  }

  private static class ThisNoteSink extends TestingNoteSink {

    private final Http.Server.Notes notes = Http.Server.Notes.create();

    private final CountDownLatch serverStarted;
    private final CountDownLatch noteReceived;

    public ThisNoteSink(CountDownLatch serverStarted, CountDownLatch noteReceived) {
      this.serverStarted = serverStarted;
      this.noteReceived = noteReceived;
    }

    @Override
    protected void visitNote(Note note) {
      if (Objects.equals(note, notes.started())) {
        try {
          serverStarted.await();

          noteReceived.countDown();
        } catch (InterruptedException e) {
          // server interrupted...
          Thread.currentThread().interrupt();
        }
      }
    }

  }

}