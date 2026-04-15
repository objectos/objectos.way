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
package objectos.http;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

final class HttpRequestBodySupport0 extends HttpRequestBodySupport {

  private Path file;

  private final long id;

  private final int memoryMax;

  private final long sizeMax;

  HttpRequestBodySupport0(long id, int memoryMax, long sizeMax) {
    this.id = id;

    this.memoryMax = memoryMax;

    this.sizeMax = sizeMax;
  }

  @Override
  public final void close() throws IOException {
    if (file != null) {
      final Path deleteMe;
      deleteMe = file;

      file = null;

      Files.delete(deleteMe);
    }
  }

  @Override
  final Path file() throws IOException {
    if (file == null) {
      final String format;

      if (id < 0) {
        format = "%019d.neg";
      } else {
        format = "%019d";
      }

      final String prefix;
      prefix = String.format(format, id);

      file = Files.createTempFile(prefix, ".body");
    }

    return file;
  }

  @Override
  final int memoryMax() {
    return memoryMax;
  }

  @Override
  final long sizeMax() {
    return sizeMax;
  }

}
