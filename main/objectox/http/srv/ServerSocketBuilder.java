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
import objectox.http.host.HostGlobals;

final class ServerSocketBuilder implements HostGlobals {

  private InetAddress address;

  private int port;

  public final ServerSocket build() throws IOException {
    final InetAddress a;

    if (address == null) {
      a = InetAddress.getLoopbackAddress();
    } else {
      a = address;
    }

    final SocketAddress socketAddress;
    socketAddress = new InetSocketAddress(a, port);

    final ServerSocket serverSocket;
    serverSocket = new ServerSocket();

    serverSocket.bind(socketAddress);

    return serverSocket;
  }

  @Override
  public final int port() {
    return port;
  }

  public final void port(int value) {
    if (value < 0 || value > 0xFFFF) {
      throw new IllegalArgumentException("Invalid port: value must be in the interval 0 <= value < 65536 but found " + value);
    }

    this.port = value;
  }

}
