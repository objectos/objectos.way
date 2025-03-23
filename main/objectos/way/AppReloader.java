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
import java.lang.module.ModuleFinder;
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
import java.util.Map;
import java.util.Set;

final class AppReloader implements App.Reloader, Runnable {

  record Notes(
      Note.Ref1<Path> watching,
      Note.Ref0 started,
      Note.Ref1<WatchKey> watchKeyIgnored,
      Note.Ref2<WatchEvent.Kind<?>, Object> watchEvent,
      Note.Ref1<Class<?>> loaded,
      Note.Ref1<Class<?>> systemLoaded,
      Note.Ref1<String> skipped,
      Note.Ref2<Path, IOException> registerFailed,
      Note.Ref1<Throwable> reloadFailed
  ) {

    static Notes get() {
      Class<?> s;
      s = App.Reloader.class;

      return new Notes(
          Note.Ref1.create(s, "Watch [directory]", Note.INFO),
          Note.Ref0.create(s, "Started", Note.INFO),
          Note.Ref1.create(s, "Watch key ignored", Note.DEBUG),
          Note.Ref2.create(s, "FS", Note.TRACE),
          Note.Ref1.create(s, "Load", Note.TRACE),
          Note.Ref1.create(s, "System Load", Note.TRACE),
          Note.Ref1.create(s, "Do not reload", Note.DEBUG),
          Note.Ref2.create(s, "Register error", Note.ERROR),
          Note.Ref1.create(s, "Reload error", Note.ERROR)
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

  private final Path[] directories;

  private volatile Http.Handler handler;

  private final App.Reloader.HandlerFactory handlerFactory;

  private final Map<WatchKey, Path> keys;

  private final Configuration moduleConfiguration;

  private final String moduleName;

  private final WatchService service;

  private final boolean serviceClose;

  private Thread thread;

  AppReloader(AppReloaderConfig builder) {
    notes = builder.notes;

    noteSink = builder.noteSink;

    classReader = Lang.createClassReader(noteSink);

    directories = builder.directories();

    handlerFactory = builder.handlerFactory;

    keys = builder.keys;

    final ModuleFinder finder;
    finder = ModuleFinder.of(directories);

    final ModuleLayer parent;
    parent = ModuleLayer.boot();

    final Configuration parentCfg;
    parentCfg = parent.configuration();

    final ModuleFinder afterFinder;
    afterFinder = ModuleFinder.of();

    moduleName = builder.moduleName;

    final Set<String> roots;
    roots = Set.of(moduleName);

    moduleConfiguration = parentCfg.resolve(finder, afterFinder, roots);

    service = builder.service;

    serviceClose = builder.serviceClose;
  }

  @Override
  public final void close() throws IOException {
    if (thread != null) {
      thread.interrupt();

      while (thread.isAlive()) {
        Thread.onSpinWait();
      }
    }

    if (serviceClose) {
      service.close();
    }
  }

  @Override
  public final void handle(Http.Exchange http) {
    handler.handle(http);
  }

  @Override
  public final void run() {
    noteSink.send(notes.started);

    try {
      while (!Thread.currentThread().isInterrupted()) {
        WatchKey key;
        key = service.take();

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
      }
    } catch (InterruptedException expected) {
      // we're shutting down
    }
  }

  final void start() {
    // initial load
    reload();

    // start reloader thread
    thread = Thread.ofPlatform().name("reloader").start(this);
  }

  private boolean process(WatchKey key) throws InterruptedException {
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
        final Path name;
        name = (Path) event.context();

        final Path child;
        child = directory.resolve(name);

        if (Files.isDirectory(child)) {
          createdOrModified = true;

          register0(child);
        }

        else if (!createdOrModified && isClassFile(name)) {
          createdOrModified = true;
        }
      }

      else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
        final Path name;
        name = (Path) event.context();

        if (!createdOrModified && isClassFile(name)) {
          createdOrModified = true;
        }
      }

    }

    final boolean valid;
    valid = key.reset();

    if (!valid) {
      keys.remove(key);
    }

    return createdOrModified;
  }

  private boolean isClassFile(Path name) {
    Path fileName;
    fileName = name.getFileName();

    String actualName;
    actualName = fileName.toString();

    return actualName.endsWith(".class");
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

  private void register0(Path directory) {
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
      ModuleLayer parent;
      parent = ModuleLayer.boot();

      ModuleLayer layer;
      layer = parent.defineModules(moduleConfiguration, this::loaderFunction);

      ClassLoader loader;
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

  private ClassLoader loaderFunction(String name) {
    if (name.equals(moduleName)) {
      return new ThisLoader();
    } else {
      return ModuleLayer.boot().findLoader(name);
    }
  }

  private class ThisLoader extends ClassLoader {

    public ThisLoader() {
      super(null); // no parent
    }

    @Override
    protected final Class<?> findClass(String name) throws ClassNotFoundException {
      for (int i = 0; i < directories.length; i++) {
        Path directory;
        directory = directories[i];

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

      Class<?> clazz;
      clazz = systemLoader.loadClass(name);

      noteSink.send(notes.systemLoaded, clazz);

      return clazz;
    }

  }

  private boolean doNotReload(String binaryName, byte[] contents) {
    classReader.init(binaryName, contents);

    return classReader.isAnnotationPresent(App.DoNotReload.class);
  }

}