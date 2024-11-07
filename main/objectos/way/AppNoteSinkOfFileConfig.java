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

final class AppNoteSinkOfFileConfig implements App.NoteSink.OfFile.Config {

  private final int bufferSize = 4096;

  private Clock clock = Clock.systemDefaultZone();

  private Predicate<Note> filter = note -> true;

  private final Path file;

  public AppNoteSinkOfFileConfig(Path file) {
    this.file = Objects.requireNonNull(file, "file == null");
  }

  @Override
  public final void clock(Clock clock) {
    this.clock = Objects.requireNonNull(clock, "clock == null");
  }

  @Override
  public final void filter(Predicate<Note> filter) {
    this.filter = Objects.requireNonNull(filter, "filter == null");
  }

  final AppNoteSinkOfFile build() throws IOException {
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