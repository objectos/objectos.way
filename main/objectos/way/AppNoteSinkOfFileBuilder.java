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

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.function.Predicate;

final class AppNoteSinkOfFileBuilder implements App.NoteSink.OfFile.Options {

  private final int bufferSize = 4096;

  private Clock clock = Clock.systemDefaultZone();

  private Predicate<Note> filter = note -> true;

  private Path file;

  @Override
  public final void clock(Clock value) {
    this.clock = Objects.requireNonNull(value, "value == null");
  }

  @Override
  public final void file(Path value) {
    file = Objects.requireNonNull(value, "value == null");
  }

  @Override
  public final void filter(Predicate<Note> value) {
    this.filter = Objects.requireNonNull(value, "value == null");
  }

  final AppNoteSinkOfFile build() throws IOException {
    if (file == null) {
      throw new IllegalArgumentException("No output file specified. Please set an output file with the config.file(String) method.");
    }

    ByteBuffer buffer;
    buffer = ByteBuffer.allocateDirect(bufferSize);

    Path parent;
    parent = file.getParent();

    if (!Files.exists(parent)) {
      Files.createDirectories(parent);
    }

    if (Files.exists(file)) {
      LocalDateTime now;
      now = LocalDateTime.now(clock);

      String suffix;
      suffix = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(now);

      Path target;
      target = file.resolveSibling(file.getFileName().toString() + "." + suffix);

      Files.copy(file, target);
    }

    SeekableByteChannel channel;
    channel = Files.newByteChannel(
        file,

        StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE
    );

    return new AppNoteSinkOfFile(clock, filter, buffer, channel);
  }

}