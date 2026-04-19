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
import java.net.InetAddress;
import java.net.ServerSocket;
import java.nio.file.Path;
import objectos.way.Io;

final class HttpServer2Pojo implements HttpServer {

  private final Path root;

  private final ServerSocket serverSocket;

  private final Thread thread;

  HttpServer2Pojo(Path root, ServerSocket serverSocket, Thread thread) {
    this.root = root;

    this.serverSocket = serverSocket;

    this.thread = thread;
  }

  @Override
  public final InetAddress address() {
    return serverSocket.getInetAddress();
  }

  @Override
  public final int port() {
    return serverSocket.getLocalPort();
  }

  @Override
  public final void close() throws IOException {
    try {
      thread.interrupt();
    } finally {
      try {
        serverSocket.close();
      } finally {
        Io.deleteRecursively(root);
      }
    }
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