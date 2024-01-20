/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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
package objectos.web;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import objectos.lang.object.Check;
import objectos.notes.NoOpNoteSink;
import objectos.notes.Note1;
import objectos.notes.NoteSink;

public final class DefaultWebResources implements AutoCloseable, WebResources {

  public static final Note1<Path> CREATED;

  private static final Note1<Path> ABSOLUTE;

  private static final Note1<Path> TRAVERSAL;

  static {
    Class<?> source;
    source = DefaultWebResources.class;

    CREATED = Note1.info(source, "File created");

    ABSOLUTE = Note1.error(source, "Absolute!");

    TRAVERSAL = Note1.error(source, "Traversal!");
  }

  private NoteSink noteSink = NoOpNoteSink.of();

  private final Path target;

  public DefaultWebResources(Path target) {
    Check.notNull(target, "target == null");
    checkDirectory(target);

    this.target = target;
  }

  private static void checkDirectory(Path dir) {
    if (!Files.isDirectory(dir)) {
      throw new IllegalArgumentException("""
          Invalid directory '%s'

          - the path does not exist
          - the path exists but it is not a directory
          - the path may exist but it is not accessible
          """.formatted(dir));
    }
  }

  public final DefaultWebResources noteSink(NoteSink noteSink) {
    this.noteSink = Check.notNull(noteSink, "noteSink == null");

    return this;
  }

  public final void copyDirectory(Path directory) throws IOException {
    Check.notNull(directory, "directory == null");
    checkDirectory(directory);

    var task = new SimpleFileVisitor<Path>() {
      @Override
      public final FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        Path path;
        path = directory.relativize(dir);

        Path dest;
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
        Path path;
        path = directory.relativize(file);

        Path dest;
        dest = target.resolve(path);

        Files.copy(file, dest);

        noteSink.send(CREATED, dest);

        return FileVisitResult.CONTINUE;
      }
    };

    Files.walkFileTree(directory, task);
  }

  @Override
  public final Path regularFile(Path path) {
    Check.notNull(path, "path == null");

    if (path.isAbsolute()) {
      noteSink.send(ABSOLUTE, path);

      return null;
    }

    Path file;
    file = target.resolve(path);

    file = file.normalize();

    if (!file.startsWith(target)) {
      noteSink.send(TRAVERSAL, path);

      return null;
    }

    if (!Files.isRegularFile(file)) {
      return null;
    }

    return file;
  }

  @Override
  public final void close() throws IOException {
    var rm = new SimpleFileVisitor<Path>() {
      @Override
      public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        Files.delete(dir);
        return FileVisitResult.CONTINUE;
      }

      @Override
      public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        Files.delete(file);
        return FileVisitResult.CONTINUE;
      }
    };

    Files.walkFileTree(target, rm);
  }

}