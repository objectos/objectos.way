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
package objectos.way;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

final class AppReloaderHelper implements AutoCloseable {

  private final Path root;
  private final Path src;
  private final Path cls;

  private final JavaCompiler javaCompiler;
  private final StandardJavaFileManager fileManager;

  private final List<Path> sourceFiles = Util.createList();

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

  @Override
  public final void close() throws IOException {
    try (fileManager) {
      Rmdir.rmdir(root);
    }
  }

}