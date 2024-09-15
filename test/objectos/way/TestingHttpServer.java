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
import objectos.notes.Note;

public final class TestingHttpServer {

  private TestingHttpServer() {}

  public static void bindCarbonStylesTest(CarbonStylesTest test) {
    ServerHolder.bindCarbonStylesTest(test);
  }

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

    public static void bindCarbonStylesTest(CarbonStylesTest test) {
      HANDLER.carbonStylesTest.delegate = test.compile();
    }

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

      Http.Server wayServer;
      wayServer = Http.createServer(
          HANDLER,

          Http.clock(TestingClock.FIXED),

          Http.noteSink(new ThisNoteSink(HANDLER)),

          Http.port(0)
      );

      TestingShutdownHook.register(wayServer);

      wayServer.start();

      synchronized (HANDLER) {
        HANDLER.wait();
      }

      return wayServer;
    }

  }

  private static class ThisHandlerFactory extends Http.Module implements Http.HandlerFactory {

    private final DelegatingHandler carbonStylesTest = new DelegatingHandler();

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
      host("carbon.styles.test", carbonStylesTest);

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

    private final Object lock;

    public ThisNoteSink(Object lock) { this.lock = lock; }

    @Override
    protected void visitNote(Note note) {
      if (note == Http.Server.LISTENING) {
        synchronized (lock) {
          lock.notify();
        }
      }
    }

  }

}