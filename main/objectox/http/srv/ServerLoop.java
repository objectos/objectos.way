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
package objectox.http.srv;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Clock;
import java.util.concurrent.ThreadFactory;
import objectos.http.Server;
import objectos.way.Note;
import objectox.http.host.HostMap;
import objectox.http.req.RequestBodySupportFactory;
import objectox.http.resp.ResponseDate;

public final class ServerLoop extends Thread implements Server {

  private static final Note.Ref0 STARTED;
  private static final Note.Ref0 STOPPED;

  private static final Note.Ref1<Socket> ACCEPTED;
  private static final Note.Ref1<Throwable> THROW;

  static {
    final Class<?> s;
    s = Server.class;

    STARTED = Note.Ref0.create(s, "STA", Note.INFO);
    STOPPED = Note.Ref0.create(s, "STO", Note.INFO);

    ACCEPTED = Note.Ref1.create(s, "ACC", Note.TRACE);
    THROW = Note.Ref1.create(s, "THR", Note.ERROR);
  }

  private final int bufferSize;

  private final Clock clock;

  private final HostMap hostMap;

  private final Note.Sink noteSink;

  private final RequestBodySupportFactory requestBodySupportFactory;

  private final ServerSocket serverSocket;

  private final ThreadFactory threadFactory;

  ServerLoop(
      int bufferSize,

      Clock clock,

      HostMap hostMap,

      Note.Sink noteSink,

      RequestBodySupportFactory requestBodySupportFactory,

      ServerSocket serverSocket,

      ThreadFactory threadFactory
  ) {
    this.bufferSize = bufferSize;

    this.clock = clock;

    this.hostMap = hostMap;

    this.noteSink = noteSink;

    this.requestBodySupportFactory = requestBodySupportFactory;

    this.serverSocket = serverSocket;

    this.threadFactory = threadFactory;
  }

  @Override
  public final void close() {
    interrupt();
  }

  public final int port() {
    return serverSocket.getLocalPort();
  }

  @Override
  public final void run() {
    noteSink.send(STARTED);

    try (serverSocket) {
      final ResponseDate responseDate;
      responseDate = new ResponseDate(clock);

      while (!isInterrupted()) {
        final Socket socket;
        socket = serverSocket.accept();

        noteSink.send(ACCEPTED, socket);

        final ServerTask task;
        task = new ServerTask(bufferSize, hostMap, noteSink, requestBodySupportFactory, responseDate, socket);

        final Thread thread;
        thread = threadFactory.newThread(task);

        thread.start();
      }
    } catch (IOException e) {
      noteSink.send(THROW, e);
    } finally {
      noteSink.send(STOPPED);
    }
  }

  @Override
  public final String toString() {
    final StringBuilder sb;
    sb = new StringBuilder("Server[");

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
