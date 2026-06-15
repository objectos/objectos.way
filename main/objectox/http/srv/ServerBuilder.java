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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import objectos.http.HostOptions;
import objectos.http.ServerOptions;
import objectos.way.Note.Sink;
import objectox.http.host.Host;
import objectox.http.host.HostGlobals;
import objectox.http.host.HostMap;
import objectox.http.host.HostStage;
import objectox.http.host.HostStageBuilder;

public final class ServerBuilder
    implements
    ServerOptions {

  private InetAddress address;

  private final Map<String, HostStage> hosts = new LinkedHashMap<>();

  private int port;

  @Override
  public final void host(Consumer<? super HostOptions> opts) {
    final HostStageBuilder builder;
    builder = new HostStageBuilder();

    opts.accept(builder);

    final HostStage stage;
    stage = builder.build();

    final String name;
    name = stage.name();

    final HostStage existing;
    existing = hosts.put(name, stage);

    if (existing != null) {
      final String msg;
      msg = "A host with the same name was already registered: %s".formatted(name);

      throw new IllegalArgumentException(msg);
    }
  }

  @Override
  public final void noteSink(Sink value) {

  }

  @Override
  public final void port(int value) {
    if (value < 0 || value > 0xFFFF) {
      throw new IllegalArgumentException("Invalid port: value must be in the interval 0 <= value < 65536 but found " + value);
    }

    this.port = value;
  }

  private record Globals(int port) implements HostGlobals {}

  public final ServerPojo build() throws IOException {
    final ServerSocket serverSocket;
    serverSocket = buildServerSocket();

    int localPort;
    localPort = serverSocket.getLocalPort();

    final Globals globals;
    globals = new Globals(localPort);

    return new ServerPojo(
        buildHostMap(globals),

        serverSocket
    );
  }

  private HostMap buildHostMap(HostGlobals globals) {
    final List<Host> list;
    list = hosts.values().stream().map(stage -> stage.toHost(globals)).toList();

    return HostMap.of(list);
  }

  private ServerSocket buildServerSocket() throws IOException {
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

    return serverSocket;
  }

}
