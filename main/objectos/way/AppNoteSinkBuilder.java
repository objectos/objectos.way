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

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.nio.file.StandardOpenOption.WRITE;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.function.Predicate;

final class AppNoteSinkBuilder implements App.NoteSink.Options {

  private static final class AlwaysTrue implements Predicate<Note> {
    @Override
    public final boolean test(Note t) {
      return true;
    }
  }

  Clock clock = Clock.systemDefaultZone();

  Predicate<? super Note> filter = new AlwaysTrue();

  AppNoteSinkBuilder() {}

  @Override
  public final void clock(Clock value) {
    this.clock = Objects.requireNonNull(value, "value == null");
  }

  @Override
  public final void filter(Predicate<? super Note> value) {
    if (filter instanceof AlwaysTrue) {
      filter = Objects.requireNonNull(value, "value == null");
    } else {
      throw new IllegalStateException("A filter has already been defined");
    }
  }

  final AppNoteSink ofAppendable(Appendable out) {
    return new AppNoteSink(this, out);
  }

  final AppNoteSink ofFile(Path file) throws IOException {
    final Path parent;
    parent = file.getParent();

    if (!Files.exists(parent)) {
      Files.createDirectories(parent);
    }

    if (Files.exists(file)) {
      final LocalDateTime now;
      now = LocalDateTime.now(clock);

      final String suffix;
      suffix = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(now);

      final Path target;
      target = file.resolveSibling(file.getFileName().toString() + "." + suffix);

      Files.copy(file, target);
    }

    final Charset charset;
    charset = StandardCharsets.UTF_8;

    final BufferedWriter writer;
    writer = Files.newBufferedWriter(file, charset, CREATE, TRUNCATE_EXISTING, WRITE);

    return new AppNoteSink(this, writer);
  }

}