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

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Path;
import objectos.way.Io;
import objectos.way.Note;

final class StaticFilesRoot implements Closeable {

  private static final Note.Ref1<String> TRAVERSAL = Note.Ref1.create(StaticFiles.class, "TRV", Note.ERROR);

  private final Path directory;

  private final Note.Sink noteSink;

  StaticFilesRoot(Path directory, Note.Sink noteSink) {
    this.directory = directory;

    this.noteSink = noteSink;
  }

  @Override
  public final void close() throws IOException {
    Io.deleteRecursively(directory);
  }

  public final Path resolve(String path) {
    final String relative;
    relative = path.substring(1);

    final Path resolved;
    resolved = directory.resolve(relative);

    final Path file;
    file = resolved.normalize();

    if (!file.startsWith(directory)) {
      noteSink.send(TRAVERSAL, path);

      return null;
    } else {
      return file;
    }
  }

}
