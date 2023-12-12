/*
 * Copyright (C) 2020-2023 Objectos Software LTDA.
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
package objectos.git;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

final class TestingGit2 {

  public static void copyRecursively(Path source, Path target) throws IOException {
    SimpleFileVisitor<Path> copy = new SimpleFileVisitor<Path>() {
      @Override
      public final FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        Path relative;
        relative = source.relativize(dir);

        Path targetDir;
        targetDir = target.resolve(relative);

        try {
          Files.copy(dir, targetDir);
        } catch (FileAlreadyExistsException e) {
          if (!Files.isDirectory(targetDir)) {
            throw e;
          }
        }

        return FileVisitResult.CONTINUE;
      }

      @Override
      public final FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        Path relative;
        relative = source.relativize(file);

        Path targetFile;
        targetFile = target.resolve(relative);

        Files.copy(file, targetFile);

        return FileVisitResult.CONTINUE;
      }
    };

    Files.walkFileTree(source, copy);
  }

  public static void deleteRecursively(Path root) throws IOException {
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

    Files.walkFileTree(root, rm);
  }

  public static Path repo00() throws IOException {
    return parentOf("TEST-INF/repo00.git/HEAD");
  }

  public static Path repo02() throws IOException {
    return parentOf("TEST-INF/repo02.git/HEAD");
  }

  public static Path repo03() throws IOException {
    return parentOf("TEST-INF/repo03.git/HEAD");
  }

  private static Path parentOf(String resourceName) throws IOException {
    try {
      URL resource;
      resource = ClassLoader.getSystemResource(resourceName);

      URI uri;
      uri = resource.toURI();

      Path path;
      path = Path.of(uri);

      return path.getParent();
    } catch (URISyntaxException e) {
      throw new IOException(e);
    }
  }

  public static void emptyRepo(Path directory) throws IOException {
    Path objects;
    objects = directory.resolve("objects");

    Files.createDirectories(objects);
  }

}