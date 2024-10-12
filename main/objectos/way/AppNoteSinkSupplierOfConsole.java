/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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

import java.io.PrintStream;
import java.time.Clock;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

final class AppNoteSinkSupplierOfConsole implements App.NoteSinkSupplier.OfConsole.Config {

  static final class Impl implements App.NoteSinkSupplier.OfConsole {

    private final AppNoteSink noteSink;

    Impl(AppNoteSink noteSink) {
      this.noteSink = noteSink;
    }

    @Override
    public final void close() throws Exception {
      // noop
    }

    @Override
    public final Note.Sink get() {
      return noteSink;
    }

  }

  private Clock clock = Clock.systemDefaultZone();

  private final Predicate<Note> filter = note -> true;

  private PrintStream target = System.out;

  @Override
  public final void clock(Clock clock) {
    this.clock = Objects.requireNonNull(clock, "clock == null");
  }

  @Override
  public final void target(PrintStream target) {
    this.target = Objects.requireNonNull(target, "target == null");
  }

  final Impl build() {
    Consumer<byte[]> sink;
    sink = target::writeBytes;

    AppNoteSink noteSink;
    noteSink = new AppNoteSink(clock, filter, sink);

    return new Impl(noteSink);
  }

}