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
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import objectos.internal.IOFunction;

final class StaticFilesAttributes {

  private final IOFunction<Path, BasicFileAttributes> reader;

  StaticFilesAttributes(IOFunction<Path, BasicFileAttributes> reader) {
    this.reader = reader;
  }

  public final BasicFileAttributes read(Path path) throws IOException {
    try {
      final BasicFileAttributes attrs;
      attrs = reader.apply(path);

      if (attrs.isRegularFile()) {
        return attrs;
      } else {
        return null;
      }
    } catch (NoSuchFileException e) {
      return null;
    }
  }

}
