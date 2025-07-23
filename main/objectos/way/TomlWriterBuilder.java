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
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;

final class TomlWriterBuilder {

  private final int bufferSize = 16 * 1024;

  private boolean failIfExists;

  final TomlWriter ofFile(Path file) throws IOException {
    final EnumSet<StandardOpenOption> set;
    set = EnumSet.noneOf(StandardOpenOption.class);

    set.add(StandardOpenOption.WRITE);

    set.add(failIfExists ? StandardOpenOption.CREATE_NEW : StandardOpenOption.CREATE);

    final OpenOption[] options;
    options = set.toArray(OpenOption[]::new);

    final OutputStream out;
    out = Files.newOutputStream(file, options);

    return new TomlWriter(this, out);
  }

  final byte[] buffer() {
    return new byte[bufferSize];
  }

}