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
package objectos.http;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.time.Clock;
import java.util.concurrent.atomic.AtomicLong;
import objectos.way.Note;

final class HttpServer1Loop implements Runnable {

  private static final Note.Ref0 STARTED;
  private static final Note.Ref0 STOPPED;

  private static final Note.Ref1<Socket> ACCEPTED;
  private static final Note.Ref1<Throwable> THROW;

  static {
    final Class<?> s;
    s = HttpServer.class;

    STARTED = Note.Ref0.create(s, "STA", Note.INFO);
    STOPPED = Note.Ref0.create(s, "STO", Note.INFO);

    ACCEPTED = Note.Ref1.create(s, "ACC", Note.TRACE);
    THROW = Note.Ref1.create(s, "THR", Note.ERROR);
  }

  private final HttpRequestBodyOptions bodyOptions;

  private final int bufferSize;

  private final Clock clock;

  private final HttpErrorResponses errorResponses;

  private final HttpHosts hosts;

  private final AtomicLong idSupplier = new AtomicLong(1);

  private final Note.Sink noteSink;

  private final ServerSocket serverSocket;

  HttpServer1Loop(
      HttpRequestBodyOptions bodyOptions,
      int bufferSize,
      Clock clock,
      HttpErrorResponses errorResponses,
      HttpHosts hosts,
      Note.Sink noteSink,
      ServerSocket serverSocket
  ) {
    this.bodyOptions = bodyOptions;

    this.bufferSize = bufferSize;

    this.clock = clock;

    this.errorResponses = errorResponses;

    this.hosts = hosts;

    this.noteSink = noteSink;

    this.serverSocket = serverSocket;
  }

  @Override
  public final void run() {
    noteSink.send(STARTED);

    try {
      while (!Thread.currentThread().isInterrupted()) {
        final Socket socket;
        socket = serverSocket.accept();

        noteSink.send(ACCEPTED, socket);

        final byte[] buffer;
        buffer = new byte[bufferSize];

        final long id;
        id = idSupplier.getAndIncrement();

        final HttpServerTask http;
        http = new HttpServerTask(bodyOptions, buffer, clock, errorResponses, hosts, id, noteSink, socket);

        final Thread task;
        task = Thread.ofVirtual().name("http-", id).unstarted(http);

        task.start();
      }
    } catch (SocketException e) {
      if (serverSocket != null && !serverSocket.isClosed()) {
        noteSink.send(THROW, e);
      }
    } catch (IOException e) {
      noteSink.send(THROW, e);
    } finally {
      noteSink.send(STOPPED);
    }
  }

}
