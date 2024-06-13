/*
 * Copyright (C) 2023 Objectos Software LTDA.
 *
 * This file is part of the objectos.www project.
 *
 * objectos.www is NOT free software and is the intellectual property of Objectos Software LTDA.
 *
 * Source is available for educational purposes only.
 */
package objectos.http;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Clock;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import objectos.http.WayServerLoop.ParseStatus;
import objectos.notes.Note0;
import objectos.notes.Note1;
import objectos.notes.NoteSink;
import objectos.way.Http;
import objectos.way.SessionStore;
import objectos.way.Http.Exchange;

final class WayHttpServerLoop implements Runnable {

  public static final Note1<IOException> IO_ERROR;

  public static final Note1<Throwable> LOOP_ERROR;

  public static final Note1<Throwable> INTERNAL_SERVER_ERROR;

  static {
    Class<?> source;
    source = WayHttpServerLoop.class;

    IO_ERROR = Note1.error(source, "I/O Error");

    LOOP_ERROR = Note1.error(source, "Loop Error");

    INTERNAL_SERVER_ERROR = Note1.error(source, "Internal Server Error");
  }

  int bufferSizeInitial;

  int bufferSizeMax;

  Clock clock;

  NoteSink noteSink;

  SessionStore sessionStore;

  final ServerSocket serverSocket;

  final HandlerFactory handlerFactory;

  public WayHttpServerLoop(ServerSocket serverSocket, HandlerFactory handlerFactory) {
    this.serverSocket = serverSocket;

    this.handlerFactory = handlerFactory;
  }

  @Override
  public final void run() {
    ThreadFactory factory;
    factory = Thread.ofVirtual().name("http-", 1).factory();

    try (ExecutorService executor = Executors.newThreadPerTaskExecutor(factory)) {
      noteSink.send(HttpServer.LISTENING, serverSocket);

      // listen indefinitely
      while (!Thread.currentThread().isInterrupted()) {
        Socket socket;
        socket = serverSocket.accept();

        Runnable task;
        task = new ThisTask(socket);

        executor.submit(task);
      }
    } catch (IOException e) {
      Note1<IOException> errorNote;
      errorNote = Note1.info(getClass(), "Failed to open remote socket");

      noteSink.send(errorNote, e);
    }

    Note0 stopNote;
    stopNote = Note0.info(getClass(), "Stop");

    noteSink.send(stopNote);
  }

  private class ThisTask implements Runnable {

    private final Socket socket;

    public ThisTask(Socket socket) {
      this.socket = socket;
    }

    @Override
    public final void run() {
      WayServerLoop loop;
      loop = new WayServerLoop(socket);

      loop.bufferSize(bufferSizeInitial, bufferSizeMax);

      loop.clock(clock);

      loop.noteSink(noteSink);

      loop.sessionStore(sessionStore);

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