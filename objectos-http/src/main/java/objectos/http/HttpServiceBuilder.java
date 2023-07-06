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

import java.net.SocketAddress;
import java.util.function.Supplier;
import objectos.http.server.Handler;
import objectos.lang.NoOpNoteSink;
import objectos.lang.NoteSink;

final class HttpServiceBuilder {

  private final SocketAddress address;

  private int bufferSize = 1024;

  private final Supplier<Handler> handlerSupplier;

  private NoteSink noteSink = NoOpNoteSink.getInstance();

  HttpServiceBuilder(SocketAddress address, Supplier<Handler> handlerSupplier) {
    this.address = address;

    this.handlerSupplier = handlerSupplier;
  }

  public final HttpService build() {
    return new HttpService(address, bufferSize, handlerSupplier, noteSink);
  }

  final void setBufferSize(int size) {
    this.bufferSize = size;
  }

  final void setLogger(NoteSink logger) {
    this.noteSink = logger;
  }

}