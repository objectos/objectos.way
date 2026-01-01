/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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
import java.lang.invoke.MethodHandles;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.function.Consumer;

/// The __Objectos TOML__ main class.
public final class Toml {

  @SuppressWarnings("serial")
  public static final class RecordException extends RuntimeException {

    RecordException(Throwable cause) {
      super(cause);
    }

  }

  public sealed interface Reader extends Closeable permits TomlReader {

    sealed interface Options permits TomlReaderBuilder {

      void bufferSize(int value);

      void file(Path value, OpenOption... options);

      void lookup(MethodHandles.Lookup value);

    }

    static Reader create(Consumer<? super Options> opts) throws IOException {
      final TomlReaderBuilder builder;
      builder = new TomlReaderBuilder();

      opts.accept(builder);

      return builder.build();
    }

    <T extends Record> T readRecord(Class<T> type) throws IOException;

  }

  public sealed interface Writer extends Closeable, Flushable permits TomlWriter {

    sealed interface Options permits TomlWriterBuilder {

      void bufferSize(int value);

      void file(Path value, OpenOption... options);

      void lookup(MethodHandles.Lookup value);

    }

    static Writer create(Consumer<? super Options> opts) throws IOException {
      final TomlWriterBuilder builder;
      builder = new TomlWriterBuilder();

      opts.accept(builder);

      return builder.build();
    }

    void writeRecord(Record value) throws IOException;

  }

  private Toml() {}

  static String basicUnescaped() {
    //basic-unescaped = wschar / %x21 / %x23-5B / %x5D-7E / non-ascii
    return wschar() + "!" + "#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[" + "]^_`abcdefghijklmnopqrstuvwxyz{|}~";
  }

  static String wschar() {
    return " \t";
  }

}