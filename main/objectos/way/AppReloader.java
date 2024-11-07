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
import objectos.notes.Note1;
import objectos.notes.Note2;

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

  record Notes(
      Note.Ref1<Path> watching,
      Note.Ref1<WatchKey> watchKeyIgnored,
      Note.Ref2<WatchEvent.Kind<?>, Object> watchEvent,
      Note.Ref1<Class<?>> loaded,
      Note.Ref1<String> skipped
  ) {

    static Notes get() {
      Class<?> s;
      s = App.Reloader.class;

      return new Notes(
          Note.Ref1.create(s, "Watch [directory]", Note.INFO),
          Note.Ref1.create(s, "Watch key ignored", Note.DEBUG),
          Note.Ref2.create(s, "FS", Note.TRACE),
          Note.Ref1.create(s, "Load", Note.TRACE),
          Note.Ref1.create(s, "Do not reload", Note.DEBUG)
      );
    }

  }

  record Directory(Path path, String packageName) {
    public boolean contains(String name) {
      return name.startsWith(packageName);
    }
  }

  private final Notes notes;

  private final Note.Sink noteSink;

  private final Lang.ClassReader classReader;

  private final List<Path> directories;

  private final Map<WatchKey, Path> keys;

  private final String binaryName;

  private final WatchService service;

  private final boolean serviceClose;

  private Class<?> clazz;

  AppReloader(AppReloaderConfig builder) {
    notes = builder.notes;

    noteSink = builder.noteSink;

    classReader = Lang.createClassReader(noteSink);

    directories = builder.directories;

    keys = builder.keys;

    binaryName = builder.binaryName;

    service = builder.service;

    serviceClose = builder.serviceClose;
  }

  @Override
  public final void close() throws IOException {
    if (serviceClose) {
      service.close();
    }
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
        noteSink.send(notes.watchKeyIgnored, key);

        continue;
      }

      for (WatchEvent<?> event : key.pollEvents()) {
        noteSink.send(notes.watchEvent, event.kind(), event.context());

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
          noteSink.send(notes.skipped, name);

          break;
        }

        Class<?> clazz;
        clazz = defineClass(name, bytes, 0, bytes.length);

        noteSink.send(notes.loaded, clazz);

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