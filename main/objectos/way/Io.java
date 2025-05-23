/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * The <strong>Objectos IO</strong> main class.
 */
public final class Io {

  private Io() {}

  static FileVisitor<Path> createDeleteRecursivelyFileVisitor() {
    return new SimpleFileVisitor<Path>() {
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
  }

  public static void deleteRecursively(Path directory) throws IOException {
    if (Files.exists(directory)) {
      FileVisitor<Path> deleteRecursively;
      deleteRecursively = createDeleteRecursivelyFileVisitor();

      Files.walkFileTree(directory, deleteRecursively);
    }
  }

  static Path nextTmpDir() throws IOException {
    return Files.createTempDirectory("do-not-delete-objectos-way-");
  }

}