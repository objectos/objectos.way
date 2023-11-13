/*
 * Copyright (C) 2023 Objectos Software LTDA.
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
package objectox.lang;

import java.io.File;
import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.FileVisitResult;
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
import objectos.lang.ClassReloader;
import objectos.notes.Note1;
import objectos.notes.Note2;
import objectos.notes.NoteSink;
import objectos.util.map.GrowableMap;

public final class ClassReloaderImpl implements objectos.lang.ClassReloader {

  static final Note2<Path, String> WATCH;

  static final Note1<WatchKey> WATCH_KEY_IGNORED;

  static final Note2<WatchEvent.Kind<?>, Object> FS_EVENT;

  static final Note1<Class<?>> LOAD;

  static {
    Class<?> s;
    s = ClassReloader.class;

    WATCH = Note2.info(s, "Watch");

    WATCH_KEY_IGNORED = Note1.debug(s, "Watch key ignored");

    FS_EVENT = Note2.trace(s, "FS");

    LOAD = Note1.trace(s, "Load");
  }

  record Directory(Path path, String packageName) {
    public boolean contains(String name) {
      return name.startsWith(packageName);
    }
  }

  private final NoteSink noteSink;

  private final List<Directory> directories;

  private final Map<WatchKey, Path> keys = new GrowableMap<>();

  private final String binaryName;

  private final WatchService service;

  private Class<?> clazz;

  ClassReloaderImpl(String binaryName, ClassReloaderBuilder builder) throws IOException {
    this.binaryName = binaryName;

    noteSink = builder.noteSink;

    directories = builder.directories.toUnmodifiableList();

    service = builder.watchService;

    init();
  }

  private void init() throws IOException {
    for (var directory : directories) {
      noteSink.send(WATCH, directory.path, directory.packageName);

      Path path;
      path = directory.path();

      register(path);
    }
  }

  @Override
  public final void close() throws IOException {
    service.close();
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
      public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
          throws IOException {
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
        Directory directory;
        directory = directories.get(i);

        if (!directory.contains(name)) {
          continue;
        }

        return load0(name, directory.path());
      }

      return load1(name);
    }

    private Class<?> load0(String name, Path directory) throws ClassNotFoundException {
      try {
        String fileName;
        fileName = name.replace('.', File.separatorChar);

        fileName += ".class";

        Path path;
        path = directory.resolve(fileName);

        byte[] bytes;
        bytes = Files.readAllBytes(path);

        Class<?> clazz;
        clazz = defineClass(name, bytes, 0, bytes.length);

        noteSink.send(LOAD, clazz);

        return clazz;
      } catch (NoSuchFileException e) {
        return load1(name);
      } catch (IOException e) {
        throw new ClassNotFoundException(name, e);
      }
    }

    private Class<?> load1(String name) throws ClassNotFoundException {
      ClassLoader systemLoader;
      systemLoader = ClassLoader.getSystemClassLoader();

      return systemLoader.loadClass(name);
    }

  }

}