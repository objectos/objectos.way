/*
 * Copyright (C) 2025-2026 Objectos Software LTDA.
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
import java.io.Writer;
import java.lang.module.Configuration;
import java.lang.module.ModuleFinder;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

final class AppReloaderHelper implements AutoCloseable, App.Reloader.HandlerFactory {

  private final Path root;
  private final Path src;
  private final Path cls;

  private final JavaCompiler javaCompiler;
  private final StandardJavaFileManager fileManager;

  private final List<Path> sourceFiles = Util.createList();

  private Path resources;

  private String message;

  public AppReloaderHelper(
      Path root,
      Path src,
      Path cls,
      JavaCompiler javaCompiler,
      StandardJavaFileManager fileManager) {
    this.root = root;
    this.src = src;
    this.cls = cls;
    this.javaCompiler = javaCompiler;
    this.fileManager = fileManager;
  }

  public static AppReloaderHelper of() throws IOException {
    Path root;
    root = Files.createTempDirectory("class-reloader-");

    JavaCompiler javaCompiler;
    javaCompiler = ToolProvider.getSystemJavaCompiler();

    StandardJavaFileManager fileManager;
    fileManager = javaCompiler.getStandardFileManager(null, null, null);

    fileManager.setLocationForModule(StandardLocation.MODULE_PATH, "objectos.way", List.of(Path.of("work", "main")));

    Path src;
    src = root.resolve("src");

    Files.createDirectories(src);

    fileManager.setLocationFromPaths(StandardLocation.SOURCE_PATH, List.of(src));

    Path cls;
    cls = root.resolve("cls");

    Files.createDirectories(cls);

    fileManager.setLocationFromPaths(StandardLocation.CLASS_OUTPUT, List.of(cls));

    return new AppReloaderHelper(
        root, src, cls, javaCompiler, fileManager
    );
  }

  public final void writeModuleInfo() throws IOException {
    writeJavaFile(
        Path.of("module-info.java"),

        """
        module test.way {
          exports test;

          requires objectos.way;
        }
        """
    );
  }

  public final void writeSubject(String string) throws IOException {
    writeJavaFile(
        Path.of("test", "Subject.java"),

        """
        package test;

        public class Subject implements objectos.way.Http.Handler {
          public void handle(objectos.way.Http.Exchange http) {}
          public String get() {
            %s
          }
        }
        """.formatted(string)
    );
  }

  public final void writeDelegate(String string) throws IOException {
    writeJavaFile(
        Path.of("test", "Delegate.java"),

        """
        package test;

        @objectos.way.App.DoNotReload
        class Delegate {
          static final String SUBJECT = "%s";
        }
        """.formatted(string)
    );
  }

  public final Path resources() throws IOException {
    if (resources == null) {
      Path dir;
      dir = root.resolve("resources");

      Files.createDirectories(dir);

      resources = dir;
    }

    return resources;
  }

  public final void writeJavaFile(Path path, String source) throws IOException {
    Path javaFile;
    javaFile = src.resolve(path);

    Path parent;
    parent = javaFile.getParent();

    Files.createDirectories(parent);

    try (Writer w = Files.newBufferedWriter(javaFile)) {
      w.write(source);
    }

    sourceFiles.add(javaFile);
  }

  public final boolean compile() {
    Iterable<? extends JavaFileObject> compilationUnits;
    compilationUnits = fileManager.getJavaFileObjectsFromPaths(sourceFiles);

    sourceFiles.clear();

    CompilationTask task;
    task = javaCompiler.getTask(null, fileManager, null, null, null, compilationUnits);

    Boolean result;
    result = task.call();

    return result.booleanValue();
  }

  public final Path classOutput() {
    return cls;
  }

  public final String get() {
    return message;
  }

  @Override
  public final void close() throws IOException {
    try (fileManager) {
      Rmdir.rmdir(root);
    }
  }

  public final Class<?> load() {
    final ModuleLayer parentLayer;
    parentLayer = ModuleLayer.boot();

    final Configuration parentConfig;
    parentConfig = parentLayer.configuration();

    final ModuleFinder before;
    before = ModuleFinder.of(cls);

    final ModuleFinder after;
    after = ModuleFinder.of();

    final Set<String> roots;
    roots = Set.of("test.way");

    final Configuration config;
    config = parentConfig.resolve(before, after, roots);

    final ModuleLayer layer;
    layer = parentLayer.defineModulesWithOneLoader(config, ClassLoader.getSystemClassLoader());

    final ClassLoader loader;
    loader = layer.findLoader("test.way");

    try {
      return loader.loadClass("test.Subject");
    } catch (ClassNotFoundException e) {
      throw new RuntimeException("Failed to load class", e);
    }
  }

  @Override
  public final Http.Handler reload(ClassLoader classLoader) throws Exception {
    Class<?> subject;
    subject = classLoader.loadClass("test.Subject");

    Constructor<?> constructor;
    constructor = subject.getConstructor();

    Object instance;
    instance = constructor.newInstance();

    Method getMethod;
    getMethod = subject.getMethod("get");

    message = (String) getMethod.invoke(instance);

    return (Http.Handler) instance;
  }

}