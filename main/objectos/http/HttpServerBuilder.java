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

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.time.Clock;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import objectos.internal.Check;
import objectos.internal.NoOpSinkSingleton;
import objectos.way.Note;

final class HttpServerBuilder implements HttpServer.Options {

  private InetAddress address;

  private int bufferSize = 16 * 1024;

  private Clock clock = Clock.systemUTC();

  private final Map<String, HttpHost0> hosts = new LinkedHashMap<>();

  private int requestBodyMemoryMax;

  private long requestBodySizeMax = 10 * 1024 * 1024;

  private Note.Sink noteSink = NoOpSinkSingleton.INSTANCE;

  private int port = 0;

  public final HttpServer build() {
    return new HttpServerImpl(
        bodyOptions(),

        bufferSize,

        clock,

        hosts(),

        noteSink,

        socketAddress()
    );
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
    final HttpHost0 host;
    host = new HttpHost0();

    opts.accept(host);

    final HttpHost0 existing;
    existing = hosts.put(host.name, host);

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

  /*
  
  @Override
  public final void staticFiles(ThrowingConsumer<? super HttpStaticFiles> opts) throws IOException {
    if (staticFiles == null) {
      final Path rootDirectory;
      rootDirectory = Files.createTempDirectory("way-http-static-files-");

      staticFiles = new HttpStaticFiles0(noteSink, rootDirectory);
    }

    opts.accept(staticFiles);
  }
  
  */

  private HttpRequestBodyOptions bodyOptions() {
    final int memoryMax;

    if (requestBodyMemoryMax == 0) {
      memoryMax = bufferSize;
    } else {
      memoryMax = requestBodyMemoryMax;
    }

    return new HttpRequestBodyOptions0(memoryMax, requestBodySizeMax);
  }

  private HttpHosts hosts() {
    HttpHosts res;
    res = HttpHosts.of();

    for (var stage : hosts.values()) {
      final String name;
      name = stage.name(port);

      final HttpHost1 host;
      host = stage.build(noteSink);

      res = res.add(name, host);
    }

    return res;
  }

  private InetSocketAddress socketAddress() {
    final InetAddress a;

    if (address == null) {
      a = InetAddress.getLoopbackAddress();
    } else {
      a = address;
    }

    return new InetSocketAddress(a, port);
  }

}