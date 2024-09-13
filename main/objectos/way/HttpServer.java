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

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.time.Clock;
import objectos.notes.NoOpNoteSink;
import objectos.notes.NoteSink;

/**
 * The Objectos Way {@link HttpServer} implementation.
 */
final class HttpServer implements Http.Server {

  static final class Builder {

    int bufferSizeInitial = 1024;

    int bufferSizeMax = 4096;

    Clock clock = Clock.systemUTC();

    final Http.HandlerFactory factory;

    NoteSink noteSink = NoOpNoteSink.of();

    int port = 0;

    public Builder(Http.HandlerFactory factory) {
      this.factory = factory;
    }

    public final Http.Server build() {
      return new HttpServer(this);
    }

  }

  private final int bufferSizeInitial;

  private final int bufferSizeMax;

  private final Clock clock;

  private final Http.HandlerFactory factory;

  private final NoteSink noteSink;

  private final int port;

  private ServerSocket serverSocket;

  private Thread thread;

  public HttpServer(Builder builder) {
    bufferSizeInitial = builder.bufferSizeInitial;
    bufferSizeMax = builder.bufferSizeMax;
    clock = builder.clock;
    factory = builder.factory;
    noteSink = builder.noteSink;
    port = builder.port;
  }

  @Override
  public final void start() throws IOException {
    if (thread != null) {
      throw new IllegalStateException(
          "The service has already been started."
      );
    }

    InetAddress address;
    address = InetAddress.getLoopbackAddress();

    InetSocketAddress socketAddress;
    socketAddress = new InetSocketAddress(address, port);

    serverSocket = new ServerSocket();

    serverSocket.bind(socketAddress);

    HttpServerLoop loop;
    loop = new HttpServerLoop(serverSocket, factory);

    loop.bufferSizeInitial = bufferSizeInitial;

    loop.bufferSizeMax = bufferSizeMax;

    loop.clock = clock;

    loop.noteSink = noteSink;

    thread = Thread.ofPlatform().name("HTTP").start(loop);
  }

  @Override
  public final InetAddress address() {
    checkStarted();

    return serverSocket.getInetAddress();
  }

  @Override
  public final int port() {
    checkStarted();

    return serverSocket.getLocalPort();
  }

  private void checkStarted() {
    if (thread == null) {
      throw new IllegalStateException(
          "Cannot query this service: service is not running."
      );
    }
  }

  @Override
  public final void close() throws IOException {
    if (thread == null) {
      throw new IllegalStateException(
          "Cannot stop this service: service is not running."
      );
    }

    try {
      thread.interrupt();
    } finally {
      try {
        serverSocket.close();
      } finally {
        thread = null;

        serverSocket = null;
      }
    }
  }

}