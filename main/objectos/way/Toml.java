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

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

/// The __Objectos TOML__ main class.
public final class Toml {

  public sealed interface Document permits TomlDocument {

    static Document create() {
      return new TomlDocument();
    }

    void add(String name, Record value);

  }

  @SuppressWarnings("serial")
  public static final class RecordException extends RuntimeException {

    RecordException(Throwable cause) {
      super(cause);
    }

  }

  public sealed interface Writer extends Closeable, Flushable permits TomlWriter {

    static Writer ofFile(Path file) throws IOException {
      Objects.requireNonNull(file, "file == null");

      final TomlWriterBuilder builder;
      builder = new TomlWriterBuilder();

      return builder.ofFile(file);
    }

    void writeRecord(Record value) throws IOException;

  }

  private Toml() {}

}