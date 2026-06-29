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
package objectox.dev;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import objectos.way.Note;

final class ReloadingFs implements ReloadingWatcherAdapter {

  private static final Note.Ref1<WatchKey> IGNORED = Note.Ref1.create(ReloadingFs.class, "IGN", Note.DEBUG);

  private static final Note.Ref2<WatchEvent.Kind<?>, Object> EVENT = Note.Ref2.create(ReloadingFs.class, "EVT", Note.TRACE);

  private static final Note.Ref1<Throwable> THROW = Note.Ref1.create(ReloadingFs.class, "THR", Note.ERROR);

  private final Map<WatchKey, Path> keys = new HashMap<>();

  private final Note.Sink noteSink;

  private final WatchService watchService;

  private final FileVisitor<Path> registerVisitor = new SimpleFileVisitor<Path>() {
    @Override
    public final FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
      final WatchKey key;
      key = dir.register(
          watchService,
          StandardWatchEventKinds.ENTRY_CREATE,
          StandardWatchEventKinds.ENTRY_DELETE,
          StandardWatchEventKinds.ENTRY_MODIFY
      );

      keys.put(key, dir);

      return FileVisitResult.CONTINUE;
    }
  };

  ReloadingFs(Note.Sink noteSink, WatchService watchService) {
    this.noteSink = noteSink;

    this.watchService = watchService;
  }

  public static ReloadingFs of(Note.Sink noteSink, Set<Path> directories) throws IOException {
    final FileSystem fileSystem;
    fileSystem = FileSystems.getDefault();

    final WatchService watchService;
    watchService = fileSystem.newWatchService();

    final ReloadingFs fs;
    fs = new ReloadingFs(noteSink, watchService);

    for (Path directory : directories) {
      fs.register(directory);
    }

    return fs;
  }

  @Override
  public final void close() throws IOException {
    watchService.close();
  }

  @Override
  public final boolean changed() {
    final WatchKey key;
    key = watchService.poll();

    if (key != null) {
      return changed0(key);
    } else {
      return false;
    }
  }

  public final void register(Path directory) {
    try {
      Files.walkFileTree(directory, registerVisitor);
    } catch (IOException e) {
      noteSink.send(THROW, e);
    }
  }

  private boolean changed0(WatchKey key) {
    boolean changed;
    changed = false;

    do {
      boolean createdOrModified;
      createdOrModified = process(key);

      if (!changed && createdOrModified) {
        changed = true;
      }

      key = watchService.poll();
    } while (key != null);

    return changed;
  }

  private boolean process(WatchKey key) {
    final Path directory;
    directory = keys.get(key);

    if (directory == null) {
      noteSink.send(IGNORED, key);

      return false;
    }

    boolean createdOrModified;
    createdOrModified = false;

    for (WatchEvent<?> event : key.pollEvents()) {
      noteSink.send(EVENT, event.kind(), event.context());

      final WatchEvent.Kind<?> kind;
      kind = event.kind();

      if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
        createdOrModified = true;

        final Path name;
        name = (Path) event.context();

        final Path child;
        child = directory.resolve(name);

        if (Files.isDirectory(child)) {
          register(child);
        }
      }

      else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
        createdOrModified = true;
      }
    }

    final boolean valid;
    valid = key.reset();

    if (!valid) {
      keys.remove(key);
    }

    return createdOrModified;
  }

}
