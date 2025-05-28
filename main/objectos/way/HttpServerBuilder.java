/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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
package objectos.way;

import java.time.Clock;
import java.util.Objects;

final class HttpServerBuilder implements Http.Server.Options {

  int bufferSizeInitial = 16 * 1024;

  int bufferSizeMax = 16 * 1024;

  Clock clock = Clock.systemUTC();

  Http.Handler handler = http -> {};

  long requestBodySizeMax = 10 * 1024 * 1024;

  Note.Sink noteSink = Note.NoOpSink.INSTANCE;

  int port = 0;

  public final Http.Server build() {
    return new HttpServer(this);
  }

  @Override
  public final void bufferSize(int initial, int max) {
    Check.argument(initial >= 128, "initial size must be >= 128");
    Check.argument(max >= 128, "max size must be >= 128");
    Check.argument(max >= initial, "max size must be >= initial size");

    bufferSizeInitial = initial;

    bufferSizeMax = max;
  }

  @Override
  public final void clock(Clock value) {
    clock = Objects.requireNonNull(value, "value == null");
  }

  @Override
  public final void handler(Http.Handler value) {
    handler = Objects.requireNonNull(value, "value == null");
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
  public final void requestBodySize(long max) {
    Check.argument(max >= 0, "max request body size must not be negative");

    requestBodySizeMax = max;
  }

}