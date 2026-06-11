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
package objectos.y;

import java.io.Closeable;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import objectos.way.Io;
import objectos.way.Y;

public final class PathY implements Closeable {

  private static final PathY INSTANCE;

  static {
    try {
      final Path root;
      root = Files.createTempDirectory("path-y-");

      final PathY instance;
      instance = new PathY(root);

      Y.shutdownHook(instance);

      INSTANCE = instance;
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private final Path root;

  private PathY(Path root) {
    this.root = root;
  }

  public static Path nextDir() {
    return INSTANCE.$nextDir();
  }

  public static Path nextFile() {
    return INSTANCE.$nextFile();
  }

  public static Path nextFile(String source, Charset charset) {
    return INSTANCE.$nextFile("next-file-", ".tmp", source, charset);
  }

  public static Path nextFile(String prefix, String suffix, String source, Charset charset) {
    return INSTANCE.$nextFile(prefix, suffix, source, charset);
  }

  public static void write(Path directory, String other, String contents) {
    try {
      final Path file;
      file = directory.resolve(other);

      final Path parent;
      parent = file.getParent();

      Files.createDirectories(parent);

      Files.writeString(file, contents, StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  @Override
  public final void close() throws IOException {
    Io.deleteRecursively(root);
  }

  private Path $nextDir() {
    try {
      return Files.createTempDirectory(root, "next-dir-");
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private Path $nextFile() {
    try {
      return Files.createTempFile(root, "next-file-", ".tmp");
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private Path $nextFile(String prefix, String suffix, String source, Charset charset) {
    try {
      final Path file;
      file = Files.createTempFile(root, prefix, suffix);

      Files.writeString(file, source, charset);

      return file;
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

}
