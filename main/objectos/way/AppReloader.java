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

import java.io.File;
import java.io.IOException;
import java.lang.module.Configuration;
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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import objectos.way.Lang.InvalidClassFileException;

final class AppReloader implements App.Reloader {

  record Notes(
      Note.Ref2<String, Path> watchModule,
      Note.Ref1<Path> watchDirectory,

      Note.Ref1<WatchKey> watchKeyIgnored,
      Note.Ref2<WatchEvent.Kind<?>, Object> watchEvent,

      Note.Ref1<Class<?>> loaded,
      Note.Ref1<String> skipped,

      Note.Ref2<Path, IOException> registerFailed,
      Note.Ref1<Throwable> reloadFailed
  ) {

    static Notes get() {
      Class<?> s;
      s = App.Reloader.class;

      return new Notes(
          Note.Ref2.create(s, "MOD", Note.INFO),
          Note.Ref1.create(s, "DIR", Note.INFO),

          Note.Ref1.create(s, "IGN", Note.DEBUG),
          Note.Ref2.create(s, "FSE", Note.TRACE),

          Note.Ref1.create(s, "REL", Note.TRACE),
          Note.Ref1.create(s, "SKI", Note.DEBUG),

          Note.Ref2.create(s, "FSX", Note.ERROR),
          Note.Ref1.create(s, "REX", Note.ERROR)
      );
    }

  }

  private final Notes notes;

  private final Note.Sink noteSink;

  private final Lang.ClassReader classReader;

  private volatile Http.Handler handler;

  private final App.Reloader.HandlerFactory handlerFactory;

  private final Map<WatchKey, Path> keys;

  private final ReadWriteLock lock;

  private final Configuration moduleConfiguration;

  private final Path moduleLocation;

  private final String moduleName;

  private final ModuleLayer parentLayer;

  private final WatchService service;

  private final boolean serviceClose;

  AppReloader(AppReloaderBuilder builder) {
    notes = builder.notes;

    noteSink = builder.noteSink;

    classReader = Lang.createClassReader(noteSink);

    handlerFactory = builder.handlerFactory;

    keys = new HashMap<>();

    lock = new ReentrantReadWriteLock();

    moduleConfiguration = builder.moduleConfiguration;

    moduleLocation = builder.moduleLocation;

    moduleName = builder.moduleName;

    parentLayer = builder.parentLayer;

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
  public final void handle(Http.Exchange http) {
    reloadIfNecessary();

    handler.handle(http);
  }

  @Override
  public final String toString() {
    return "App.Reloader[" + moduleName + ";" + moduleLocation + "]";
  }

  private void reloadIfNecessary() {
    lock.readLock().lock();
    try {

      WatchKey key;
      key = service.poll();

      if (key == null) {
        return;
      }

      lock.readLock().unlock();
      lock.writeLock().lock();
      try {

        boolean shouldReload;
        shouldReload = false;

        do {
          boolean createdOrModified;
          createdOrModified = process(key);

          if (!shouldReload && createdOrModified) {
            shouldReload = true;
          }

          key = service.poll();
        } while (key != null);

        if (shouldReload) {
          reload();
        }

        lock.readLock().lock();
      } finally {
        lock.writeLock().unlock();
      }

    } finally {
      lock.readLock().unlock();
    }
  }

  final void start(Iterable<Path> directories) {
    register(moduleLocation);

    noteSink.send(notes.watchModule, moduleName, moduleLocation);

    for (Path directory : directories) {
      register(directory);

      noteSink.send(notes.watchDirectory, directory);
    }

    // initial load
    reload();
  }

  private boolean process(WatchKey key) {
    final Path directory;
    directory = keys.get(key);

    if (directory == null) {
      noteSink.send(notes.watchKeyIgnored, key);

      return false;
    }

    boolean createdOrModified;
    createdOrModified = false;

    for (WatchEvent<?> event : key.pollEvents()) {
      noteSink.send(notes.watchEvent, event.kind(), event.context());

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

  private final FileVisitor<Path> registerVisitor = new SimpleFileVisitor<Path>() {
    @Override
    public final FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
      final WatchKey key;
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

  private void register(Path directory) {
    try {
      Files.walkFileTree(directory, registerVisitor);
    } catch (IOException e) {
      noteSink.send(notes.registerFailed, directory, e);
    }
  }

  private void reload() {
    Http.Handler result;
    result = Http.Handler.noop();

    try {
      final ThisLoader parentLoader;
      parentLoader = new ThisLoader();

      final ModuleLayer layer;
      layer = parentLayer.defineModulesWithOneLoader(moduleConfiguration, parentLoader);

      final ClassLoader loader;
      loader = layer.findLoader(moduleName);

      Http.Handler instance;
      instance = handlerFactory.reload(loader);

      if (instance == null) {
        Throwable t;
        t = new NullPointerException("Reloader returned a null handler");

        noteSink.send(notes.reloadFailed, t);
      } else {
        result = instance;
      }
    } catch (Throwable e) {
      noteSink.send(notes.reloadFailed, e);
    }

    handler = result;
  }

  private class ThisLoader extends ClassLoader {

    public ThisLoader() {
      super(null); // no parent
    }

    @Override
    protected final Class<?> findClass(String name) throws ClassNotFoundException {
      try {
        String fileName;
        fileName = name.replace('.', File.separatorChar);

        fileName += ".class";

        Path file;
        file = moduleLocation.resolve(fileName);

        byte[] bytes;
        bytes = Files.readAllBytes(file);

        classReader.init(bytes);

        if (classReader.annotatedWith(App.DoNotReload.class)) {
          noteSink.send(notes.skipped, name);

          return null;
        }

        Class<?> clazz;
        clazz = defineClass(name, bytes, 0, bytes.length);

        noteSink.send(notes.loaded, clazz);

        return clazz;
      } catch (NoSuchFileException e) {
        throw new ClassNotFoundException(name, e);
      } catch (IOException e) {
        throw new ClassNotFoundException(name, e);
      } catch (InvalidClassFileException e) {
        throw new ClassNotFoundException(name, e);
      }
    }

  }

}