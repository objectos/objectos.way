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
import java.lang.classfile.Annotation;
import java.lang.classfile.Attributes;
import java.lang.classfile.ClassFile;
import java.lang.classfile.ClassModel;
import java.lang.classfile.attribute.RuntimeInvisibleAnnotationsAttribute;
import java.lang.classfile.constantpool.Utf8Entry;
import java.lang.module.Configuration;
import java.lang.module.ModuleFinder;
import java.lang.module.ModuleReference;
import java.lang.module.ResolvedModule;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

final class AppReloaderBuilder implements App.Reloader.Options {

  final List<Path> directories = Util.createList();

  Predicate<? super String> filterBinaryName;

  Predicate<? super byte[]> filterClassFile;

  App.Reloader.HandlerFactory handlerFactory;

  final Map<WatchKey, Path> keys = new HashMap<>();

  final AppReloader.Notes notes = AppReloader.Notes.get();

  Configuration moduleConfiguration;

  Path moduleLocation;

  String moduleName;

  Note.Sink noteSink = Note.NoOpSink.INSTANCE;

  ModuleLayer parentLayer;

  ClassLoader parentLoader;

  WatchService service;

  boolean serviceClose;

  @Override
  public final void directory(Path value) {
    if (!Files.isDirectory(value)) {
      throw new IllegalArgumentException("Path does not represent a directory: " + value);
    }

    directories.add(value);
  }

  @Override
  public final void filerBinaryName(Predicate<? super String> value) {
    filterBinaryName = Objects.requireNonNull(value, "value == null");
  }

  @Override
  public final void handlerFactory(App.Reloader.HandlerFactory value) {
    handlerFactory = Objects.requireNonNull(value, "value == null");
  }

  @Override
  public final void moduleOf(Class<?> value) {
    if (parentLayer != null) {
      throw new IllegalStateException("module has already been set");
    }

    Module module;
    module = value.getModule();

    String moduleName;
    moduleName = module.getName();

    ModuleLayer layer;
    layer = module.getLayer();

    Configuration configuration;
    configuration = layer.configuration();

    Optional<ResolvedModule> maybeResolved;
    maybeResolved = configuration.findModule(moduleName);

    if (maybeResolved.isEmpty()) {
      throw new IllegalArgumentException("Could not find ResolvedModule for " + moduleName);
    }

    ResolvedModule resolved;
    resolved = maybeResolved.get();

    ModuleReference reference;
    reference = resolved.reference();

    Optional<URI> maybeLocation;
    maybeLocation = reference.location();

    if (maybeLocation.isEmpty()) {
      throw new IllegalArgumentException("Could not resolved location for " + moduleName);
    }

    URI uri;
    uri = maybeLocation.get();

    Path location;
    location = Path.of(uri);

    if (!Files.isDirectory(location)) {
      throw new IllegalArgumentException("Module location does not represent a directory: " + location);
    }

    final ModuleFinder finder;
    finder = ModuleFinder.of(location);

    final ModuleFinder afterFinder;
    afterFinder = ModuleFinder.of();

    final Set<String> roots;
    roots = Set.of(moduleName);

    moduleConfiguration = configuration.resolve(finder, afterFinder, roots);

    this.moduleLocation = location;

    this.moduleName = moduleName;

    parentLayer = layer;

    parentLoader = value.getClassLoader();
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
    if (handlerFactory == null) {
      throw new IllegalStateException("No handler factory specified");
    }

    if (parentLayer == null) {
      throw new IllegalStateException("No module was specified");
    }

    if (filterBinaryName == null) {
      filterBinaryName = _ -> true;
    }

    if (filterClassFile == null) {
      filterClassFile = bytes -> {
        final ClassFile cf;
        cf = ClassFile.of();

        final ClassModel cm;
        cm = cf.parse(bytes);

        final Optional<RuntimeInvisibleAnnotationsAttribute> maybe;
        maybe = cm.findAttribute(Attributes.runtimeInvisibleAnnotations());

        if (maybe.isEmpty()) {
          return true;
        }

        final RuntimeInvisibleAnnotationsAttribute attr;
        attr = maybe.get();

        final List<Annotation> annotations;
        annotations = attr.annotations();

        for (Annotation annotation : annotations) {
          final Utf8Entry className;
          className = annotation.className();

          if (className.equalsString("Lobjectos/way/App$DoNotReload;")) {
            return false;
          }
        }

        return true;
      };
    }

    if (service == null) {
      FileSystem fileSystem;
      fileSystem = FileSystems.getDefault();

      service = fileSystem.newWatchService();

      serviceClose = true;
    }

    final AppReloader reloader;
    reloader = new AppReloader(this);

    reloader.start(directories);

    return reloader;
  }

}