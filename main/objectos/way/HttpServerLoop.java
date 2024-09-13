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
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.time.Clock;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import objectos.notes.Note0;
import objectos.notes.Note1;
import objectos.notes.NoteSink;
import objectos.way.Http.Exchange;
import objectos.way.HttpExchangeLoop.ParseStatus;

final class HttpServerLoop implements Runnable {

  public static final Note1<IOException> IO_ERROR;

  public static final Note1<Throwable> LOOP_ERROR;

  public static final Note1<Throwable> INTERNAL_SERVER_ERROR;

  static {
    Class<?> source;
    source = HttpServerLoop.class;

    IO_ERROR = Note1.error(source, "I/O Error");

    LOOP_ERROR = Note1.error(source, "Loop Error");

    INTERNAL_SERVER_ERROR = Note1.error(source, "Internal Server Error");
  }

  int bufferSizeInitial;

  int bufferSizeMax;

  Clock clock;

  NoteSink noteSink;

  final ServerSocket serverSocket;

  final HandlerFactory handlerFactory;

  public HttpServerLoop(ServerSocket serverSocket, HandlerFactory handlerFactory) {
    this.serverSocket = serverSocket;

    this.handlerFactory = handlerFactory;
  }

  @Override
  public final void run() {
    ThreadFactory factory;
    factory = Thread.ofVirtual().name("http-", 1).factory();

    try (ExecutorService executor = Executors.newThreadPerTaskExecutor(factory)) {
      noteSink.send(Http.Server.LISTENING, serverSocket);

      // listen indefinitely
      while (!Thread.currentThread().isInterrupted()) {
        Socket socket;
        socket = serverSocket.accept();

        Runnable task;
        task = new ThisTask(socket);

        executor.submit(task);
      }
    } catch (SocketException e) {
      if (!serverSocket.isClosed()) {
        onIOException(e);
      }
    } catch (IOException e) {
      onIOException(e);
    }

    Note0 stopNote;
    stopNote = Note0.info(getClass(), "Stop");

    noteSink.send(stopNote);
  }

  private void onIOException(IOException e) {
    Note1<IOException> errorNote;
    errorNote = Note1.info(getClass(), "Failed to open remote socket");

    noteSink.send(errorNote, e);
  }

  private class ThisTask implements Runnable {

    private final Socket socket;

    public ThisTask(Socket socket) {
      this.socket = socket;
    }

    @Override
    public final void run() {
      HttpExchangeLoop loop;
      loop = new HttpExchangeLoop(socket);

      loop.bufferSize(bufferSizeInitial, bufferSizeMax);

      loop.clock(clock);

      loop.noteSink(noteSink);

      try (loop) {
        Exchange http;
        http = loop;

        while (!Thread.currentThread().isInterrupted()) {
          ParseStatus parse;
          parse = loop.parse();

          if (parse == ParseStatus.EOF) {
            break;
          }

          if (parse.isError()) {
            throw new UnsupportedOperationException("Implement me");
          }

          try {
            Http.Handler handler;
            handler = handlerFactory.create();

            handler.handle(http);
          } catch (Http.AbstractHandlerException ex) {
            ex.handle(http);
          } catch (Throwable t) {
            noteSink.send(INTERNAL_SERVER_ERROR, t);

            http.internalServerError(t);
          }

          loop.commit();

          if (!loop.keepAlive()) {
            break;
          }
        }
      } catch (IOException e) {
        noteSink.send(IO_ERROR, e);
      } catch (Throwable t) {
        noteSink.send(LOOP_ERROR, t);
      }
    }

  }

}