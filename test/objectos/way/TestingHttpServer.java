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
import objectos.http.HttpExchange;
import objectos.http.HttpHandler;
import objectos.http.HttpHeaderName;
import objectos.http.HttpRequest;
import objectos.http.HttpRouting;
import objectos.http.HttpRoutingTest;
import objectos.http.HttpServer;
import objectos.http.HttpServerTest;
import objectos.http.HttpSessionStore;

public final class TestingHttpServer {

  private TestingHttpServer() {}

  public static void bindHttpRoutingTest(HttpRoutingTest test) {
    ServerHolder.bindHttpRoutingTest(test);
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

  public static int port() {
    HttpServer server;
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

    static HttpServer SERVER = create();

    static ThisHandlerFactory HANDLER;

    public static void bindHttpRoutingTest(HttpRoutingTest test) {
      HANDLER.httpModuleTest.delegate = HttpHandler.of(test);
    }

    public static void bindHttpServerTest(HttpServerTest test) {
      HANDLER.httpServerTest.delegate = HttpHandler.of(test);
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

      HttpServer server;
      server = HttpServer.create(opts -> {
        final HttpHandler serverHandler;
        serverHandler = HttpHandler.of(HANDLER::configure);

        opts.address(InetAddress.getLoopbackAddress());

        opts.handler(serverHandler);

        opts.clock(Y.clockFixed());

        opts.noteSink(Y.noteSink());

        opts.port(0);

        opts.sessionStore(HttpSessionStore.create(config -> {
          config.cookieName("HTTPMODULETEST");

          config.sessionGenerator(Y.randomGeneratorOfLongs(1L, 2L, 3L, 4L));
        }));
      });

      TestingShutdownHook.register(server);

      server.start();

      return server;
    }

  }

  private static class ThisHandlerFactory {

    private final DelegatingHandler httpModuleTest = new DelegatingHandler();

    private final DelegatingHandler httpServerTest = new DelegatingHandler();

    private final MarketingSite marketing = new MarketingSite();

    public final void configure(HttpRouting r) {
      r.when(req -> host(req, "http.module.test"), matched -> {
        matched.handler(httpModuleTest);
      });

      r.when(req -> host(req, "http.server.test"), matched -> {
        matched.handler(httpServerTest);
      });

      r.when(req -> host(req, "marketing"), marketing);
    }

    private boolean host(HttpRequest req, String hostName) {
      final String host;
      host = req.header(HttpHeaderName.HOST);

      return hostName.equals(host);
    }

    private static final class DelegatingHandler implements HttpHandler {

      HttpHandler delegate;

      @Override
      public final void handle(HttpExchange http) {
        delegate.handle(http);
      }

    }

  }

}