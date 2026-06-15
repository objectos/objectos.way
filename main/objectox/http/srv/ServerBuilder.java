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
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.util.function.Consumer;
import objectos.http.HostOptions;
import objectos.http.ServerOptions;
import objectos.way.Note.Sink;

public final class ServerBuilder implements ServerOptions {

  private InetAddress address;

  private int port;

  public final ServerPojo build() throws IOException {
    // socketAddress
    final InetAddress a;

    if (address == null) {
      a = InetAddress.getLoopbackAddress();
    } else {
      a = address;
    }

    final SocketAddress socketAddress;
    socketAddress = new InetSocketAddress(a, port);

    // serverSocket
    final ServerSocket serverSocket;
    serverSocket = new ServerSocket();

    serverSocket.bind(socketAddress);

    return new ServerPojo(serverSocket);
  }

  @Override
  public final void host(Consumer<? super HostOptions> opts) {

  }

  @Override
  public final void noteSink(Sink value) {

  }

  @Override
  public final void port(int value) {
    if (value < 0 || value > 0xFFFF) {
      throw new IllegalArgumentException("port out of range:" + value);
    }

    this.port = value;
  }

}
