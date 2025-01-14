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
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

final class AppReloaderConfig implements App.Reloader.Config {

  String binaryName;

  final List<Path> directories = Util.createList();

  final Map<WatchKey, Path> keys = new HashMap<>();

  final AppReloader.Notes notes = AppReloader.Notes.get();

  Note.Sink noteSink = Note.NoOpSink.INSTANCE;

  WatchService service;

  boolean serviceClose;

  private final FileVisitor<Path> visitor = new SimpleFileVisitor<Path>() {
    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
      WatchKey key;
      key = dir.register(
          service,
          StandardWatchEventKinds.ENTRY_CREATE,
          StandardWatchEventKinds.ENTRY_DELETE,
          StandardWatchEventKinds.ENTRY_MODIFY
      );

      keys.put(key, dir);

      return FileVisitResult.CONTINUE;
    }
  };

  @Override
  public final void binaryName(String value) {
    binaryName = Objects.requireNonNull(value, "value == null");
  }

  @Override
  public final void directory(Path value) {
    if (!Files.isDirectory(value)) {
      throw new IllegalArgumentException("Path does not represent a directory: " + value);
    }

    directories.add(value);
  }

  @Override
  public final void noteSink(Note.Sink value) {
    noteSink = Objects.requireNonNull(value, "value == null");
  }

  @Override
  public final void watchService(WatchService value) {
    service = Objects.requireNonNull(value, "value == null");
  }

  final AppReloader build() throws IOException {
    if (binaryName == null) {
      throw new IllegalArgumentException("No binary name specified. Please set a name with the config.binaryName(String) method.");
    }

    if (service == null) {
      FileSystem fileSystem;
      fileSystem = FileSystems.getDefault();

      service = fileSystem.newWatchService();

      serviceClose = true;
    }

    for (var directory : directories) {
      noteSink.send(notes.watching(), directory);

      Files.walkFileTree(directory, visitor);
    }

    return new AppReloader(this);
  }

}