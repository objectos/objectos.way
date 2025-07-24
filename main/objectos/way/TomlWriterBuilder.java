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
import java.io.OutputStream;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.Objects;

final class TomlWriterBuilder implements Toml.Writer.Options {

  private int bufferSize = 16 * 1024;

  MethodHandles.Lookup lookup;

  private OpenOption[] openOptions;

  OutputStream outputStream;

  private Object target;

  @Override
  public final void bufferSize(int value) {
    if (value <= 0) {
      throw new IllegalArgumentException("Buffer size must be a positive number");
    }

    bufferSize = value;
  }

  @Override
  public final void file(Path value, OpenOption... options) {
    target = Objects.requireNonNull(value, "value == null");
    openOptions = Objects.requireNonNull(options, "options == null").clone();
  }

  @Override
  public final void lookup(MethodHandles.Lookup value) {
    lookup = Objects.requireNonNull(value, "value == null");
  }

  final TomlWriter build() throws IOException {
    if (lookup == null) {
      throw new IllegalStateException("A MethodHandles.Lookup was not defined");
    }

    if (target == null) {
      throw new IllegalStateException("A target was not defined");
    }

    outputStream = switch (target) {
      case null -> throw new IllegalStateException("A source was not defined");

      case Path file -> Files.newOutputStream(file, openOptions);

      default -> throw new AssertionError("Unexpected value: " + target);
    };

    return new TomlWriter(this);
  }

  final byte[] buffer() {
    return new byte[bufferSize];
  }

}