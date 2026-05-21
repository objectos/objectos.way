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
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import objectos.way.Note;

final class ServerTask implements Runnable {

  static final Note.Long1Ref1<Throwable> THROW = Note.Long1Ref1.create(ServerTask.class, "THR", Note.ERROR);

  private long id;

  private final Note.Sink noteSink;

  private final Socket socket;

  ServerTask(Note.Sink noteSink, Socket socket) {
    this.noteSink = noteSink;

    this.socket = socket;
  }

  @Override
  @SuppressWarnings("unused")
  public final void run() {
    final Thread currentThread;
    currentThread = Thread.currentThread();

    id = currentThread.threadId();

    try (socket) {
      final InputStream inputStream;
      inputStream = socket.getInputStream();

      final OutputStream outputStream;
      outputStream = socket.getOutputStream();
    } catch (IOException e) {
      noteSink.send(THROW, id, e);
    }
  }

}
