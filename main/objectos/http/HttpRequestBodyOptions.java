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

import module java.base;

record HttpRequestBodyOptions(Path directory, int memoryMax, long sizeMax) {

  private static final class StandardSupport extends HttpRequestBodySupport {

    private Path file;

    private final long id;

    private final HttpRequestBodyOptions options;

    private StandardSupport(long id, HttpRequestBodyOptions options) {
      this.id = id;

      this.options = options;
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
    final Path file() {
      if (file == null) {
        final Path directory;
        directory = options.directory;

        final String format;

        if (id < 0) {
          format = "%019d.neg";
        } else {
          format = "%019d";
        }

        final String name;
        name = String.format(format, id);

        file = directory.resolve(name);
      }

      return file;
    }

    @Override
    final int memoryMax() {
      return options.memoryMax;
    }

    @Override
    final long sizeMax() {
      return options.sizeMax;
    }

  }

  public final HttpRequestBodySupport supportOf(long id) {
    return new StandardSupport(id, this);
  }

}
