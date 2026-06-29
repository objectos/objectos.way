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
import java.lang.reflect.Constructor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import objectos.way.Note;
import objectos.way.Y;
import objectos.y.PathY;

public final class ReloadingClassLoaderY {

  public Predicate<? super String> binaryNameFilter = _ -> true;

  public Predicate<? super byte[]> classFileFilter = _ -> true;

  public String className = "objectox.dev.ReloadingClassLoaderSub";

  public Note.Sink noteSink = Y.noteSink();

  public ClassLoader parentLoader = ClassLoader.getSystemClassLoader();

  public static ReloadingClassLoader create(Consumer<? super ReloadingClassLoaderY> opts) throws IOException {
    final ReloadingClassLoaderY y;
    y = new ReloadingClassLoaderY();

    opts.accept(y);

    return y.build();
  }

  public static String sub(Consumer<? super ReloadingClassLoaderY> opts) {
    try {
      final ReloadingClassLoaderY y;
      y = new ReloadingClassLoaderY();

      opts.accept(y);

      final ReloadingClassLoader loader;
      loader = y.build();

      final Class<?> reloaded;
      reloaded = loader.loadClass(y.className);

      final Constructor<?> constructor;
      constructor = reloaded.getConstructor();

      final Object instance;
      instance = constructor.newInstance();

      return instance.toString();
    } catch (RuntimeException | Error e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private ReloadingClassLoader build() throws IOException {
    return new ReloadingClassLoader(
        binaryNameFilter,

        classFileFilter,

        moduleLocation(),

        noteSink,

        parentLoader
    );
  }

  private Path moduleLocation() throws IOException {
    final JavaCompiler javaCompiler;
    javaCompiler = ToolProvider.getSystemJavaCompiler();

    final StandardJavaFileManager fileManager;
    fileManager = javaCompiler.getStandardFileManager(null, null, null);

    final Path root;
    root = PathY.nextDir();

    final Path src;
    src = root.resolve("src");

    Files.createDirectories(src);

    fileManager.setLocationFromPaths(StandardLocation.SOURCE_PATH, List.of(src));

    final Path cls;
    cls = root.resolve("cls");

    Files.createDirectories(cls);

    fileManager.setLocationFromPaths(StandardLocation.CLASS_OUTPUT, List.of(cls));

    final Path javaFile;
    javaFile = src.resolve("objectox/dev/ReloadingClassLoaderSub.java");

    final Path parent;
    parent = javaFile.getParent();

    Files.createDirectories(parent);

    Files.writeString(javaFile, """
    package objectox.dev;

    public final class ReloadingClassLoaderSub {
      @Override
      public final String toString() {
        return "reloaded";
      }
    }
    """);

    final Iterable<? extends JavaFileObject> compilationUnits;
    compilationUnits = fileManager.getJavaFileObjectsFromPaths(List.of(javaFile));

    final CompilationTask task;
    task = javaCompiler.getTask(null, fileManager, null, null, null, compilationUnits);

    task.call();

    return cls;
  }

}
