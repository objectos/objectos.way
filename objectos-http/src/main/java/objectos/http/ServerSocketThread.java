/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
import java.net.SocketAddress;

final class ServerSocketThread extends Thread {

  private final ServerSocketThreadAdapter adapter;

  final ServerSocket serverSocket;

  private Throwable error;

  ServerSocketThread(ServerSocketThreadAdapter adapter,
                     ServerSocket serverSocket) {
    super("http-sel");

    this.adapter = adapter;

    this.serverSocket = serverSocket;
  }

  public static ServerSocketThread create(
      ServerSocketThreadAdapter adapter, SocketAddress address) throws IOException {
    ServerSocket socket;
    socket = new ServerSocket();

    try {
      socket.bind(address);
    } catch (IOException e) {
      try {
        socket.close();
      } catch (IOException suppressed) {
        e.addSuppressed(suppressed);
      }

      throw e;
    }

    return new ServerSocketThread(adapter, socket);
  }

  @Override
  public final void run() {
    while (!isInterrupted()) {
      Socket socket;

      try {
        socket = serverSocket.accept();
      } catch (IOException e) {
        error = e;

        break;
      }

      adapter.acceptSocket(socket);
    }

    if (!serverSocket.isClosed()) {
      error = close(error, serverSocket);
    }
  }

  final boolean isOpen() {
    return !serverSocket.isClosed();
  }

  private Throwable close(Throwable original, AutoCloseable c) {
    Throwable rethrow = original;

    if (c != null) {
      try {
        c.close();
      } catch (Exception e) {
        if (rethrow == null) {
          rethrow = e;
        } else {
          rethrow.addSuppressed(e);
        }
      }
    }

    return rethrow;
  }

}