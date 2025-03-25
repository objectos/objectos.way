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

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.time.Clock;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import objectos.way.HttpExchange.ParseStatus;

/**
 * The Objectos Way {@link HttpServer} implementation.
 */
final class HttpServer implements Http.Server, Runnable {

  record Notes(
      Note.Ref1<Http.Server> started,
      Note.Ref0 stopped,

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

          Note.Ref1.create(s, "IOE", Note.ERROR),
          Note.Ref1.create(s, "LOO", Note.ERROR),
          Note.Ref1.create(s, "ISE", Note.ERROR)
      );
    }

  }

  private final Notes notes = Notes.get();

  private final int bufferSizeInitial;

  private final int bufferSizeMax;

  private final Clock clock;

  private final Http.Handler handler;

  private final Note.Sink noteSink;

  private final int port;

  private ServerSocket serverSocket;

  private Thread thread;

  public HttpServer(HttpServerConfig builder) {
    bufferSizeInitial = builder.bufferSizeInitial;

    bufferSizeMax = builder.bufferSizeMax;

    clock = builder.clock;

    handler = builder.handler;

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

    thread = Thread.ofPlatform().name("HTTP").start(this);
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

  @Override
  public final void run() {
    ThreadFactory factory;
    factory = Thread.ofVirtual().name("http-", 1).factory();

    try (ExecutorService executor = Executors.newThreadPerTaskExecutor(factory)) {
      noteSink.send(notes.started, this);

      while (!Thread.currentThread().isInterrupted()) {
        Socket socket;
        socket = serverSocket.accept();

        Runnable task;
        task = new ExchangeTask(socket);

        executor.submit(task);
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

  private class ExchangeTask implements Runnable {

    private final Socket socket;

    public ExchangeTask(Socket socket) {
      this.socket = socket;
    }

    @Override
    public final void run() {
      try (HttpExchange http = new HttpExchange(socket, bufferSizeInitial, bufferSizeMax, clock, noteSink)) {
        while (!Thread.currentThread().isInterrupted()) {
          ParseStatus parse;
          parse = http.parse();

          if (parse == ParseStatus.EOF) {
            break;
          }

          if (parse.isError()) {
            throw new UnsupportedOperationException("Implement me");
          }

          try {
            handler.handle(http);
          } catch (Http.AbstractHandlerException ex) {
            ex.handle(http);
          } catch (HttpExchange.SendException ex) {
            IOException cause;
            cause = ex.getCause();

            noteSink.send(notes.ioError, cause);

            break; // could not send response. End this exchange
          } catch (Throwable t) {
            noteSink.send(notes.internalServerError, t);

            if (!http.processed()) {
              http.internalServerError(t);
            }
          }

          if (!http.keepAlive()) {
            break;
          }
        }
      } catch (IOException e) {
        noteSink.send(notes.ioError, e);
      } catch (Throwable t) {
        noteSink.send(notes.loopError, t);
      }
    }

  }

}