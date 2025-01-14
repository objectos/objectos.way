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

public final class TestingHttpServer {

  private TestingHttpServer() {}

  public static void bindHttpModuleTest(HttpModuleTest test) {
    ServerHolder.bindHttpModuleTest(test);
  }

  public static void bindHttpServerTest(HttpServerTest test) {
    ServerHolder.bindHttpServerTest(test);
  }

  public static void bindWebModuleTest(WebModuleTest test) {
    ServerHolder.bindWebModuleTest(test);
  }

  public static void bindWebResourcesTest(WebResourcesTest test) {
    ServerHolder.bindWebResourcesTest(test);
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

    public static void bindHttpModuleTest(HttpModuleTest test) {
      HANDLER.httpModuleTest.delegate = test.compile();
    }

    public static void bindHttpServerTest(HttpServerTest test) {
      HANDLER.httpServerTest.delegate = test.compile();
    }

    public static void bindWebModuleTest(WebModuleTest test) {
      HANDLER.webModuleTest.delegate = test.compile();
    }

    public static void bindWebResourcesTest(WebResourcesTest test) {
      HANDLER.webResourcesTest.delegate = test.compile();
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
        config.handlerFactory(HANDLER);

        config.clock(TestingClock.FIXED);

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

  private static class ThisHandlerFactory extends Http.Module implements Http.HandlerFactory {

    private final DelegatingHandler httpModuleTest = new DelegatingHandler();

    private final DelegatingHandler httpServerTest = new DelegatingHandler();

    private final Http.Module marketing = new MarketingSite();

    private final DelegatingHandler webModuleTest = new DelegatingHandler();

    private final DelegatingHandler webResourcesTest = new DelegatingHandler();

    @Override
    public final Http.Handler create() throws Exception {
      return compile();
    }

    @Override
    protected final void configure() {
      host("http.module.test", httpModuleTest);

      host("http.server.test", httpServerTest);

      host("marketing", marketing);

      host("web.module.test", webModuleTest);

      host("web.resources.test", webResourcesTest);
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