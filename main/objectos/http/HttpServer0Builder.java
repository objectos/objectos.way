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
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import objectos.internal.Check;
import objectos.internal.NoOpSinkSingleton;
import objectos.way.Io;
import objectos.way.Note;

final class HttpServer0Builder implements HttpServer.Options {

  private InetAddress address;

  private int bufferSize = 16 * 1024;

  private Clock clock = Clock.systemUTC();

  private final Map<String, HttpHost0Builder> hostBuilders = new LinkedHashMap<>();

  private int requestBodyMemoryMax;

  private long requestBodySizeMax = 10 * 1024 * 1024;

  private Note.Sink noteSink = NoOpSinkSingleton.INSTANCE;

  private int port = 0;

  public final HttpServer build() throws IOException {
    // bodyOptions
    final int memoryMax;

    if (requestBodyMemoryMax == 0) {
      memoryMax = bufferSize;
    } else {
      memoryMax = requestBodyMemoryMax;
    }

    final HttpRequestBodyOptions0 bodyOptions;
    bodyOptions = new HttpRequestBodyOptions0(memoryMax, requestBodySizeMax);

    // hosts root directory
    final Path root;
    root = Files.createTempDirectory("objectos-way-http-server-root-");

    try {

      // hosts
      HttpHosts hosts;
      hosts = HttpHosts.of();

      for (HttpHost0Builder builder : hostBuilders.values()) {
        final HttpHost6Pojo host;
        host = builder.build(noteSink, port, root);

        hosts = host.addTo(hosts);
      }

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

      // serverLoop
      final Runnable serverLoop;
      serverLoop = new HttpServer1Loop(bodyOptions, memoryMax, clock, hosts, noteSink, serverSocket);

      // thread
      final Thread thread;
      thread = Thread.ofPlatform().name("HTTP").start(serverLoop);

      return new HttpServer2Pojo(root, serverSocket, thread);

    } catch (Throwable t) {

      try {
        Io.deleteRecursively(root);
      } catch (IOException e) {
        t.addSuppressed(e);
      }

      if (t instanceof Error err) {
        throw err;
      }

      if (t instanceof RuntimeException re) {
        throw re;
      }

      if (t instanceof IOException ioe) {
        throw ioe;
      }

      throw new IOException(t);

    }
  }

  @Override
  public final void address(InetAddress value) {
    address = Objects.requireNonNull(value, "value == null");
  }

  @Override
  public final void bufferSize(int value) {
    Check.argument(value >= 128, "value must be >= 128");

    bufferSize = value;
  }

  @Override
  public final void clock(Clock value) {
    clock = Objects.requireNonNull(value, "value == null");
  }

  @Override
  public final void host(Consumer<? super HttpHost> opts) {
    final HttpHost0Builder host;
    host = new HttpHost0Builder();

    opts.accept(host);

    final String name;
    name = host.name();

    final HttpHost0Builder existing;
    existing = hostBuilders.put(name, host);

    if (existing != null) {
      final String msg;
      msg = "A host with the same name was already registered";

      throw new IllegalArgumentException(msg);
    }
  }

  @Override
  public final void noteSink(Note.Sink value) {
    noteSink = Objects.requireNonNull(value, "value == null");
  }

  @Override
  public final void port(int port) {
    if (port < 0 || port > 0xFFFF) {
      throw new IllegalArgumentException("port out of range:" + port);
    }

    this.port = port;
  }

  @Override
  public final void requestBodySizeMax(long value) {
    Check.argument(value >= 0, "max request body size must not be negative");

    requestBodySizeMax = value;
  }

}