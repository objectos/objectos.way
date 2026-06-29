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
import java.lang.module.Configuration;
import java.lang.module.ModuleFinder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import objectos.y.PathY;

final class ReloadingModuleY implements AutoCloseable {

  private final Path src;

  private final Path cls;

  private final JavaCompiler javaCompiler;

  private final StandardJavaFileManager fileManager;

  private final Set<Path> sourceFiles = new HashSet<>();

  ReloadingModuleY(
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

  public static ReloadingModuleY of() throws IOException {
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

    return new ReloadingModuleY(
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

  public final boolean compile() {
    final Iterable<? extends JavaFileObject> compilationUnits;
    compilationUnits = fileManager.getJavaFileObjectsFromPaths(sourceFiles);

    final CompilationTask task;
    task = javaCompiler.getTask(null, fileManager, null, null, null, compilationUnits);

    final Boolean result;
    result = task.call();

    return result.booleanValue();
  }

  public final void javaFile(String pathName, String source) throws IOException {
    final Path file;
    file = src.resolve(pathName);

    sourceFiles.add(file);

    final Path parent;
    parent = file.getParent();

    Files.createDirectories(parent);

    Files.writeString(file, source);
  }

  public final ClassLoader classLoader(String moduleName) {
    final ModuleLayer parentLayer;
    parentLayer = ModuleLayer.boot();

    final Configuration parentConfig;
    parentConfig = parentLayer.configuration();

    final ModuleFinder before;
    before = ModuleFinder.of(cls);

    final ModuleFinder after;
    after = ModuleFinder.of();

    final Set<String> roots;
    roots = Set.of(moduleName);

    final Configuration config;
    config = parentConfig.resolve(before, after, roots);

    final ModuleLayer layer;
    layer = parentLayer.defineModulesWithOneLoader(config, ClassLoader.getSystemClassLoader());

    return layer.findLoader(moduleName);
  }

}
