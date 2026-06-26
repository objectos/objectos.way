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
package objectox.http.media;

import java.io.IOException;
import java.nio.file.Path;
import objectos.http.Request;
import objectos.way.Io;

final class StaticFilesRoot {

  private final Path directory;

  StaticFilesRoot(Path directory) {
    this.directory = directory;
  }

  public final void delete() throws IOException {
    Io.deleteRecursively(directory);
  }

  public final Path resolve(Request request) throws StaticFilesErrTraversal {
    final String path;
    path = request.path();

    return resolve(path);
  }

  public final Path resolve(String path) throws StaticFilesErrTraversal {
    final String relative;
    relative = path.substring(1);

    final Path resolved;
    resolved = directory.resolve(relative);

    final Path file;
    file = resolved.normalize();

    if (!file.startsWith(directory)) {
      throw new StaticFilesErrTraversal(path);
    }

    return file;
  }

}
