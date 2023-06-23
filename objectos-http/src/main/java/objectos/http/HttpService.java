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
import objectos.lang.Check;
import objectos.lang.NoteSink;

/**
 * @since 4
 */
public final class HttpService {

  private final SocketAddress address;

  private final int bufferSize;

  private final NoteSink logger;

  private final HttpProcessorProvider processorProvider;

  private ServerSocketThread thread;

  HttpService(SocketAddress address,
              int bufferSize,
              NoteSink logger,
              HttpProcessorProvider processorProvider) {
    this.address = address;
    this.bufferSize = bufferSize;
    this.logger = logger;
    this.processorProvider = processorProvider;
  }

  public static Option bufferSize(final int size) {
    Http.checkBufferSize(size);

    return new Option() {
      @Override
      final void acceptBuilder(HttpServiceBuilder builder) {
        builder.setBufferSize(size);
      }
    };
  }

  public static HttpService create(
      SocketAddress address, HttpProcessorProvider processorProvider, Option... options) {
    Check.notNull(address, "address == null");
    Check.notNull(processorProvider, "processorProvider == null");
    Check.notNull(options, "options == null");

    HttpServiceBuilder builder;
    builder = new HttpServiceBuilder(address, processorProvider);

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
      HttpProcessor processor;
      processor = processorProvider.create();

      StringDeduplicator stringDeduplicator;
      stringDeduplicator = new HashMapStringDeduplicator();

      HttpEngine engine;
      engine = new HttpEngine(bufferSize, logger, processor, stringDeduplicator);

      engine.setInput(socket);

      Thread t;
      t = new Thread(engine);

      t.start();
    }

  }

}