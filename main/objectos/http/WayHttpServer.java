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
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.time.Clock;
import objectos.lang.object.Check;
import objectos.notes.NoOpNoteSink;
import objectos.notes.NoteSink;

/**
 * The Objectos Way {@link HttpServer} implementation.
 */
public class WayHttpServer implements HttpServer {

  private int bufferSizeInitial = 1024;

  private int bufferSizeMax = 4096;

  private Clock clock = Clock.systemUTC();

  private final HandlerFactory factory;

  private NoteSink noteSink = NoOpNoteSink.of();

  private int port = 0;

  private SessionStore sessionStore = NoOpSessionStore.INSTANCE;

  private ServerSocket serverSocket;

  private Thread thread;

  public WayHttpServer(HandlerFactory factory) {
    this.factory = Check.notNull(factory, "factory == null");
  }

  public final void bufferSize(int initial, int max) {
    checkConfig();

    Check.argument(initial >= 128, "initial size must be >= 128");
    Check.argument(max >= 128, "max size must be >= 128");
    Check.argument(max >= initial, "max size must be >= initial size");

    bufferSizeInitial = initial;

    bufferSizeMax = max;
  }

  public final void clock(Clock clock) {
    checkConfig();

    this.clock = Check.notNull(clock, "clock == null");
  }

  public final void noteSink(NoteSink noteSink) {
    checkConfig();

    this.noteSink = Check.notNull(noteSink, "noteSink == null");
  }

  public final void port(int port) {
    checkConfig();

    if (port < 0 || port > 0xFFFF) {
      throw new IllegalArgumentException("port out of range:" + port);
    }

    this.port = port;
  }

  /**
   * Use the specified {@link SessionStore} for session handling.
   *
   * @param sessionStore
   *        the session store to use
   */
  public final void sessionStore(SessionStore sessionStore) {
    checkConfig();

    this.sessionStore = Check.notNull(sessionStore, "sessionStore == null");
  }

  private void checkConfig() {
    if (thread != null) {
      throw new IllegalStateException(
          "Configuration method can only be called before starting this service."
      );
    }
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

    WayHttpServerLoop loop;
    loop = new WayHttpServerLoop(serverSocket, factory);

    loop.bufferSizeInitial = bufferSizeInitial;

    loop.bufferSizeMax = bufferSizeMax;

    loop.clock = clock;

    loop.noteSink = noteSink;

    loop.sessionStore = sessionStore;

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