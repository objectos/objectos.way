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
package objectos.way;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import objectos.io.FileVisitors;

public final class TestingDir {

  private TestingDir() {}

  public static Path next() {
    try {
      return Files.createTempDirectory(Root.INSTANCE, "test-");
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private static final class Root {

    static final Path INSTANCE = createRoot();

    private static Path createRoot() {
      try {
        Path root;
        root = Files.createTempDirectory("objectos-way-testing-");

        DeleteOnShutdown delete;
        delete = new DeleteOnShutdown(root);

        TestingShutdownHook.register(delete);

        return root;
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
    }

  }

  private static final FileVisitor<Path> DELETE_RECURSIVELY = FileVisitors.deleteRecursively();

  public static void deleteRecursively(Path directory) throws IOException {
    if (Files.exists(directory)) {
      Files.walkFileTree(directory, DELETE_RECURSIVELY);
    }
  }

  private static class DeleteOnShutdown implements AutoCloseable {

    private final Path root;

    public DeleteOnShutdown(Path root) {
      this.root = root;
    }

    @Override
    public void close() throws IOException {
      deleteRecursively(root);
    }

  }

}