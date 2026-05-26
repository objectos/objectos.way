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
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import objectos.way.Note;

final class HostCopyDirectory extends SimpleFileVisitor<Path> {

  private static final Note.Ref1<Path> CREATED = Note.Ref1.create(HostOptions.class, "ADD", Note.DEBUG);

  private final Note.Sink noteSink;

  private final Path source;

  private final Path target;

  HostCopyDirectory(Note.Sink noteSink, Path source, Path target) {
    this.noteSink = noteSink;

    this.source = source;

    this.target = target;
  }

  @Override
  public final FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
    final Path path;
    path = source.relativize(dir);

    final Path dest;
    dest = target.resolve(path);

    try {
      Files.copy(dir, dest);
    } catch (FileAlreadyExistsException e) {
      if (!Files.isDirectory(dest)) {
        throw e;
      }
    }

    return FileVisitResult.CONTINUE;
  }

  @Override
  public final FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
    final Path path;
    path = source.relativize(file);

    final Path dest;
    dest = target.resolve(path);

    Files.copy(file, dest, StandardCopyOption.COPY_ATTRIBUTES);

    noteSink.send(CREATED, dest);

    return FileVisitResult.CONTINUE;
  }

}
