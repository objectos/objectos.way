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

import java.nio.file.Path;

final class StaticFilesExtension {

  private final String defaultExtension;

  StaticFilesExtension(String defaultExtension) {
    this.defaultExtension = defaultExtension;
  }

  public final String get(Path file) {
    final Path fileNamePath;
    fileNamePath = file.getFileName();

    final String fileName;
    fileName = fileNamePath.toString();

    final int lastDotIndex;
    lastDotIndex = fileName.lastIndexOf('.');

    if (lastDotIndex == -1) {
      return defaultExtension;
    }

    else if (lastDotIndex >= (fileName.length() - 1)) {
      return defaultExtension;
    }

    else {
      return fileName.substring(lastDotIndex);
    }
  }

}
