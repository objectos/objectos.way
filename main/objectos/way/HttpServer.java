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

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.time.Clock;
import java.util.concurrent.atomic.AtomicLong;

/**
 * The Objectos Way {@link HttpServer} implementation.
 */
final class HttpServer implements Http.Server, Runnable {

  record Notes(
      Note.Ref1<Http.Server> started,
      Note.Ref0 stopped,

      Note.Ref1<Socket> accepted,

      Note.Ref1<IOException> ioError,
      Note.Ref1<Throwable> loopError,
      Note.Ref1<Throwable> internalServerError
  ) implements Http.Server.Notes {

    static Notes get() {
      final Class<?> s;
      s = Http.Server.class;

      return new Notes(
          Note.Ref1.create(s, "STA", Note.INFO),
          Note.Ref0.create(s, "STO", Note.INFO),

          Note.Ref1.create(s, "ACC", Note.TRACE),

          Note.Ref1.create(s, "IOE", Note.ERROR),
          Note.Ref1.create(s, "LOO", Note.ERROR),
          Note.Ref1.create(s, "ISE", Note.ERROR)
      );
    }

  }

  private static final AtomicLong ID = new AtomicLong(1);

  private final int bufferSizeInitial;

  private final int bufferSizeMax;

  private final Clock clock;

  private final Http.Handler handler;

  private final Notes notes = Notes.get();

  private final Note.Sink noteSink;

  private final long requestBodySizeMax;

  private ServerSocket serverSocket;

  private final InetSocketAddress socketAddress;

  private Thread thread;

  public HttpServer(HttpServerBuilder builder) {
    bufferSizeInitial = builder.bufferSizeInitial;

    bufferSizeMax = builder.bufferSizeMax;

    clock = builder.clock;

    handler = builder.handler;

    noteSink = builder.noteSink;

    requestBodySizeMax = builder.requestBodySizeMax;

    socketAddress = builder.socketAddress();
  }

  @Override
  public final InetAddress address() {
    checkStarted();

    return socketAddress.getAddress();
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
  public final void start() throws IOException {
    if (thread != null) {
      throw new IllegalStateException(
          "The service has already been started."
      );
    }

    serverSocket = new ServerSocket();

    serverSocket.bind(socketAddress);

    thread = Thread.ofPlatform().name("HTTP").start(this);
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

  @Override
  public final void run() {
    try {
      noteSink.send(notes.started, this);

      while (!Thread.currentThread().isInterrupted()) {
        final Socket socket;
        socket = serverSocket.accept();

        noteSink.send(notes.accepted, socket);

        final long id;
        id = ID.getAndIncrement();

        final HttpExchange http;
        http = new HttpExchange(
            HttpExchangeBodyFiles.standard(),
            bufferSizeInitial,
            bufferSizeMax,
            clock,
            handler,
            id,
            noteSink,
            requestBodySizeMax,
            Http.NoopResponseListener.INSTANCE,
            socket
        );

        final Thread task;
        task = Thread.ofVirtual().name("http-", id).unstarted(http);

        task.start();
      }

      noteSink.send(notes.stopped);
    } catch (SocketException e) {
      if (serverSocket != null && !serverSocket.isClosed()) {
        onIOException(e);
      }
    } catch (IOException e) {
      onIOException(e);
    }
  }

  private void onIOException(IOException e) {
    noteSink.send(notes.ioError, e);
  }

  @Override
  public final String toString() {
    StringBuilder sb;
    sb = new StringBuilder("Http.Server[");

    if (serverSocket == null) {
      sb.append("unbound");
    }

    else if (!serverSocket.isBound()) {
      sb.append("unbound");
    }

    else {
      sb.append("addr=");

      sb.append(serverSocket.getInetAddress());

      sb.append(",localport=");

      sb.append(serverSocket.getLocalPort());
    }

    sb.append(']');

    return sb.toString();
  }

}