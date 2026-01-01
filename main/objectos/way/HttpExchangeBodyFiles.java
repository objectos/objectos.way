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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

class HttpExchangeBodyFiles {

  private static final class StandardDirectoryHolder {

    static final Path DIRECTORY = create();

    private static Path create() {
      try {
        return Io.nextTmpDir();
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
    }

  }

  private static final HttpExchangeBodyFiles STANDARD = new HttpExchangeBodyFiles();

  HttpExchangeBodyFiles() {
  }

  public static HttpExchangeBodyFiles standard() {
    return STANDARD;
  }

  public Path file(long id) throws IOException {
    try {
      // fail early if error
      final Path directory;
      directory = directory();

      final String format;

      if (id < 0) {
        format = "%019d.neg";
      } else {
        format = "%019d";
      }

      final String name;
      name = String.format(format, id);

      return directory.resolve(name);
    } catch (UncheckedIOException e) {
      throw e.getCause();
    }
  }

  public InputStream newInputStream(Path file) throws IOException {
    return Files.newInputStream(file);
  }

  public OutputStream newOutputStream(Path file) throws IOException {
    return Files.newOutputStream(file);
  }

  Path directory() {
    return StandardDirectoryHolder.DIRECTORY;
  }

}