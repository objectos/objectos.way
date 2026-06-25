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
package objectos.http;

import java.io.IOException;
import java.lang.module.Configuration;
import java.lang.module.ModuleFinder;
import java.lang.reflect.Constructor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import objectos.internal.Util;
import objectos.y.PathY;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

final class ReloadingHandlerY implements AutoCloseable {

  private final Path src;
  private final Path cls;

  private final JavaCompiler javaCompiler;
  private final StandardJavaFileManager fileManager;

  private final List<Path> sourceFiles = Util.createList();

  public ReloadingHandlerY(
      Path src,

      Path cls,

      JavaCompiler javaCompiler,

      StandardJavaFileManager fileManager
  ) {
    this.src = src;

    this.cls = cls;

    this.javaCompiler = javaCompiler;

    this.fileManager = fileManager;
  }

  public static ReloadingHandlerY of() throws IOException {
    final Path root;
    root = PathY.nextDir();

    final JavaCompiler javaCompiler;
    javaCompiler = ToolProvider.getSystemJavaCompiler();

    final StandardJavaFileManager fileManager;
    fileManager = javaCompiler.getStandardFileManager(null, null, null);

    fileManager.setLocationForModule(StandardLocation.MODULE_PATH, "objectos.way", List.of(Path.of("work", "main")));

    final Path src;
    src = root.resolve("src");

    Files.createDirectories(src);

    fileManager.setLocationFromPaths(StandardLocation.SOURCE_PATH, List.of(src));

    final Path cls;
    cls = root.resolve("cls");

    Files.createDirectories(cls);

    fileManager.setLocationFromPaths(StandardLocation.CLASS_OUTPUT, List.of(cls));

    return new ReloadingHandlerY(
        src,

        cls,

        javaCompiler,

        fileManager
    );
  }

  @Override
  public final void close() throws IOException {
    fileManager.close();
  }

  public final void javaFile(String pathName, String source) throws IOException {
    final Path javaFile;
    javaFile = src.resolve(pathName);

    final Path parent;
    parent = javaFile.getParent();

    Files.createDirectories(parent);

    Files.writeString(javaFile, source);

    sourceFiles.add(javaFile);
  }

  public final boolean compile() {
    final Iterable<? extends JavaFileObject> compilationUnits;
    compilationUnits = fileManager.getJavaFileObjectsFromPaths(sourceFiles);

    sourceFiles.clear();

    final CompilationTask task;
    task = javaCompiler.getTask(null, fileManager, null, null, null, compilationUnits);

    final Boolean result;
    result = task.call();

    return result.booleanValue();
  }

  public final Path classOutput() {
    return cls;
  }

  public final ClassLoader bootstrap() throws ClassNotFoundException {
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

    return layer.findLoader("test.way");
  }

  public final Handler handler(ClassLoader classLoader) throws Exception {
    final Class<?> subjectClass;
    subjectClass = classLoader.loadClass("test.Subject");

    final Constructor<?> constructor;
    constructor = subjectClass.getConstructor();

    final Object instance;
    instance = constructor.newInstance();

    return (Handler) instance;
  }

}