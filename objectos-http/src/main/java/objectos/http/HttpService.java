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

import java.net.Socket;
import java.net.SocketAddress;
import java.util.function.Supplier;
import objectos.http.internal.HttpExchange;
import objectos.http.internal.ServerSocketThread;
import objectos.http.internal.ServerSocketThreadAdapter;
import objectos.http.server.Handler;
import objectos.lang.Check;
import objectos.lang.NoteSink;

/**
 * @since 4
 */
public final class HttpService {

  private final SocketAddress address;

  private final int bufferSize;

  private final Supplier<Handler> handlerSupplier;

  private final NoteSink noteSink;

  private ServerSocketThread thread;

  HttpService(SocketAddress address,
              int bufferSize,
              Supplier<Handler> handlerSupplier,
              NoteSink noteSink) {
    this.address = address;
    this.bufferSize = bufferSize;
    this.handlerSupplier = handlerSupplier;
    this.noteSink = noteSink;
  }

  public static Option bufferSize(final int size) {
    Check.argument(size > 128, "buffer size must be > 128");

    return new Option() {
      @Override
      final void acceptBuilder(HttpServiceBuilder builder) {
        builder.setBufferSize(size);
      }
    };
  }

  public static HttpService create(
      SocketAddress address, Supplier<Handler> handlerSupplier, Option... options) {
    Check.notNull(address, "address == null");
    Check.notNull(handlerSupplier, "handlerSupplier == null");
    Check.notNull(options, "options == null");

    HttpServiceBuilder builder;
    builder = new HttpServiceBuilder(address, handlerSupplier);

    Option o;

    for (int i = 0; i < options.length; i++) {
      o = options[i];

      Check.notNull(o, "options[", i, "] == null");

      o.acceptBuilder(builder);
    }

    return builder.build();
  }

  public static Option logger(NoteSink logger) {
    Check.notNull(logger, "logger == null");

    return new Option() {
      @Override
      final void acceptBuilder(HttpServiceBuilder builder) {
        builder.setLogger(logger);
      }
    };
  }

  public final void startService() throws Exception {
    ThisSelectorThreadAdapter adapter;
    adapter = new ThisSelectorThreadAdapter();

    thread = ServerSocketThread.create(adapter, address);

    thread.start();
  }

  public final void stopService() throws Exception {
    thread.interrupt();
  }

  /**
   * A {@code HttpService} configuration option.
   */
  public abstract static class Option {

    Option() {}

    abstract void acceptBuilder(HttpServiceBuilder builder);

  }

  private class ThisSelectorThreadAdapter implements ServerSocketThreadAdapter {

    @Override
    public final void acceptSocket(Socket socket) {
      HttpExchange exchange;
      exchange = new HttpExchange(bufferSize, handlerSupplier, noteSink, socket);

      Thread.ofVirtual().start(exchange);
    }

  }

}