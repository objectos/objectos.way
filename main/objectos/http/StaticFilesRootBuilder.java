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
import java.util.Set;
import objectos.way.Io;

final class StaticFilesRootBuilder {

  private final Set<Path> directories;

  StaticFilesRootBuilder(Set<Path> directories) {
    this.directories = directories;
  }

  public final StaticFilesRoot build() throws IOException {
    return new StaticFilesRoot(
        create()
    );
  }

  public final Path create() throws IOException {
    final Path root;
    root = Files.createTempDirectory(null);

    for (Path source : directories) {
      final CopyDirectory copy;
      copy = new CopyDirectory(source, root);

      try {
        Files.walkFileTree(source, copy);
      } catch (IOException e) {
        try {
          Io.deleteRecursively(root);
        } catch (IOException suppressed) {
          e.addSuppressed(suppressed);
        }

        throw e;
      }
    }

    return root;
  }

  private class CopyDirectory extends SimpleFileVisitor<Path> {

    private final Path source;

    private final Path target;

    CopyDirectory(Path source, Path target) {
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

      return FileVisitResult.CONTINUE;
    }

  }
}
