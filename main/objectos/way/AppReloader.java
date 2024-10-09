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

import java.io.File;
import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Map;
import objectos.notes.NoOpNoteSink;
import objectos.notes.Note1;
import objectos.notes.Note2;
import objectos.notes.NoteSink;
import objectos.way.App.Reloader;

final class AppReloader implements App.Reloader {

  static final Note1<Path> WATCH;

  static final Note1<WatchKey> WATCH_KEY_IGNORED;

  static final Note2<WatchEvent.Kind<?>, Object> FS_EVENT;

  static final Note1<Class<?>> LOAD;

  static final Note1<String> DO_NOT_RELOAD;

  static {
    Class<?> s;
    s = App.Reloader.class;

    WATCH = Note1.info(s, "Watch [directory]");

    WATCH_KEY_IGNORED = Note1.debug(s, "Watch key ignored");

    FS_EVENT = Note2.trace(s, "FS");

    LOAD = Note1.trace(s, "Load");

    DO_NOT_RELOAD = Note1.debug(s, "Do not reload");
  }

  record Directory(Path path, String packageName) {
    public boolean contains(String name) {
      return name.startsWith(packageName);
    }
  }

  private NoteSink noteSink = NoOpNoteSink.of();

  private Lang.ClassReader classReader;

  private final List<Path> directories = Util.createList();

  private final Map<WatchKey, Path> keys = Util.createGrowableMap();

  private final String binaryName;

  private final WatchService service;

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

  private Class<?> clazz;

  AppReloader(String binaryName, WatchService service) {
    this.binaryName = binaryName;

    this.service = service;
  }

  @Override
  public final void close() {
  }

  @Override
  public final Class<?> get() throws ClassNotFoundException, IOException {
    reloadIfNecessary();

    if (clazz == null) {
      throw new ClassNotFoundException(binaryName);
    }

    return clazz;
  }

  @Override
  public final String toString() {
    return "ClassReloader[" + binaryName + "]";
  }

  final void addDirectory(Path path) {
    directories.add(path);
  }

  final Reloader init() throws IOException {
    for (var directory : directories) {
      noteSink.send(WATCH, directory);

      Files.walkFileTree(directory, visitor);
    }

    classReader = Lang.createClassReader(noteSink);

    return this;
  }

  final void noteSink(NoteSink value) {
    noteSink = value;
  }

  private void reloadIfNecessary() throws ClassNotFoundException, IOException {
    if (clazz == null) {
      synchronized (this) {
        if (clazz == null) {
          loadClass();
        }
      }

      return;
    }

    WatchKey key;
    key = pollKey();

    if (key == null) {
      // filesystem did not raise any change event
      // => do not reload
      return;
    }

    synchronized (this) {
      while (key != null) {
        process(key);

        key = pollKey();
      }

      loadClass();
    }
  }

  private void process(WatchKey first) throws IOException {
    WatchKey key;
    key = first;

    while (key != null) {
      Path directory;
      directory = keys.get(key);

      if (directory == null) {
        noteSink.send(WATCH_KEY_IGNORED, key);

        continue;
      }

      for (WatchEvent<?> event : key.pollEvents()) {
        noteSink.send(FS_EVENT, event.kind(), event.context());

        WatchEvent.Kind<?> kind;
        kind = event.kind();

        if (kind != StandardWatchEventKinds.ENTRY_CREATE) {
          continue;
        }

        Path name;
        name = (Path) event.context();

        Path child;
        child = directory.resolve(name);

        if (!Files.isDirectory(child)) {
          continue;
        }

        register(child);
      }

      boolean valid;
      valid = key.reset();

      if (!valid) {
        keys.remove(key);
      }

      key = pollKey();
    }
  }

  private WatchKey pollKey() {
    try {
      return service.poll();
    } catch (ClosedWatchServiceException e) {
      throw new UnsupportedOperationException("Implement me", e);
    }
  }

  private void loadClass() throws ClassNotFoundException {
    ThisLoader loader;
    loader = new ThisLoader();

    clazz = loader.loadClass(binaryName);
  }

  private void register(Path directory) throws IOException {
    class ThisVisitor extends SimpleFileVisitor<Path> {
      @Override
      public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        registerImpl(dir);
        return FileVisitResult.CONTINUE;
      }
    }

    Files.walkFileTree(directory, new ThisVisitor());
  }

  private void registerImpl(Path directory) throws IOException {
    WatchKey key;
    key = directory.register(
        service,
        StandardWatchEventKinds.ENTRY_CREATE,
        StandardWatchEventKinds.ENTRY_DELETE,
        StandardWatchEventKinds.ENTRY_MODIFY
    );

    keys.put(key, directory);
  }

  private class ThisLoader extends ClassLoader {

    public ThisLoader() {
      super(null); // no parent
    }

    @Override
    protected final Class<?> findClass(String name) throws ClassNotFoundException {
      for (int i = 0, size = directories.size(); i < size; i++) {
        Path directory;
        directory = directories.get(i);

        String fileName;
        fileName = name.replace('.', File.separatorChar);

        fileName += ".class";

        Path file;
        file = directory.resolve(fileName);

        byte[] bytes;

        try {
          bytes = Files.readAllBytes(file);
        } catch (NoSuchFileException e) {
          continue;
        } catch (IOException e) {
          throw new ClassNotFoundException(name, e);
        }

        if (doNotReload(name, bytes)) {
          noteSink.send(DO_NOT_RELOAD, name);

          break;
        }

        Class<?> clazz;
        clazz = defineClass(name, bytes, 0, bytes.length);

        noteSink.send(LOAD, clazz);

        return clazz;
      }

      return load1(name);
    }

    private Class<?> load1(String name) throws ClassNotFoundException {
      ClassLoader systemLoader;
      systemLoader = ClassLoader.getSystemClassLoader();

      return systemLoader.loadClass(name);
    }

  }

  private boolean doNotReload(String binaryName, byte[] contents) {
    classReader.init(binaryName, contents);

    return classReader.isAnnotationPresent(App.DoNotReload.class);
  }

}