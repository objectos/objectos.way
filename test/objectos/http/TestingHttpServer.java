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

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetAddress;
import java.net.Socket;
import objectos.notes.Note;
import objectos.way.TestingNoteSink;
import objectos.way.TestingShutdownHook;

final class TestingHttpServer {

  private TestingHttpServer() {}

  public static Socket newSocket() throws IOException {
    HttpServer server;
    server = ServerHolder.SERVER;

    InetAddress address;
    address = server.address();

    int port;
    port = server.port();

    return new Socket(address, port);
  }

  private static class ServerHolder {

    static HttpServer SERVER = create();

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
      HandlerFactory handlerFactory;
      handlerFactory = new ThisHandlerFactory();

      WayHttpServer wayServer;
      wayServer = new WayHttpServer(handlerFactory);

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

  private static class ThisHandlerFactory implements Handler, HandlerFactory {

    private final Handler marketing = new MarketingSite().compile();

    @Override
    public final Handler create() throws Exception {
      return this;
    }

    @Override
    public final void handle(ServerExchange http) {
      ServerRequestHeaders headers;
      headers = http.headers();

      String host;
      host = headers.first(HeaderName.HOST);

      switch (host) {
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