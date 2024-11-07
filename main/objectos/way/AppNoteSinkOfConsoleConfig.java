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
import java.util.function.Predicate;

final class AppNoteSinkOfConsoleConfig implements App.NoteSink.OfConsole.Config {

  private Clock clock = Clock.systemDefaultZone();

  private Predicate<Note> filter = note -> true;

  private PrintStream target = System.out;

  @Override
  public final void clock(Clock clock) {
    this.clock = Objects.requireNonNull(clock, "clock == null");
  }

  @Override
  public final void filter(Predicate<Note> filter) {
    this.filter = Objects.requireNonNull(filter, "filter == null");
  }

  @Override
  public final void target(PrintStream target) {
    this.target = Objects.requireNonNull(target, "target == null");
  }

  final AppNoteSinkOfConsole build() {
    return new AppNoteSinkOfConsole(clock, filter, target);
  }

}