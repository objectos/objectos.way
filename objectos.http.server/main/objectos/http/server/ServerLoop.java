/*
 * Copyright (C) 2023 Objectos Software LTDA.
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
package objectos.http.server;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;
import objectos.lang.object.Check;
import objectos.notes.NoteSink;
import objectox.http.server.ObjectoxServerLoop;

public interface ServerLoop extends Closeable, ServerExchange {

  static ServerLoop create(Socket socket) {
    Check.notNull(socket, "socket == null");

    return new ObjectoxServerLoop(socket);
  }

  // config methods

  void bufferSize(int initial, int max);

  void noteSink(NoteSink noteSink);

  // user methods

  /**
   * Closes and ends this exchange by closing its underlying socket.
   *
   * @throws IOException
   *         if an I/O error occurs
   */
  @Override
  void close() throws IOException;

  void parse() throws IOException, IllegalStateException;

  boolean badRequest() throws IllegalStateException;

  void commit() throws IOException, IllegalStateException;

  boolean keepAlive();

}