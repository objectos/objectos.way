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
package objectox.http.handler;

import java.lang.module.Configuration;
import java.nio.file.Path;
import java.util.function.Function;
import java.util.function.Predicate;
import objectos.way.Note;

final class ReloadingModule {

  private final Configuration configuration;

  private final ModuleLayer layer;

  private final Path location;

  private final String name;

  private final ClassLoader parentLoader;

  ReloadingModule(Configuration configuration, ModuleLayer layer, Path location, String name, ClassLoader parentLoader) {
    this.configuration = configuration;

    this.layer = layer;

    this.location = location;

    this.name = name;

    this.parentLoader = parentLoader;
  }

  public final ClassLoader findLoader(
      Predicate<? super String> binaryNameFilter,
      Predicate<? super byte[]> classFileFilter,
      Note.Sink noteSink
  ) {
    final ReloadingClassLoader loader;
    loader = new ReloadingClassLoader(binaryNameFilter, classFileFilter, location, noteSink, parentLoader);

    final Function<String, ClassLoader> function;
    function = moduleName -> {
      if (name.equals(moduleName)) {
        return loader;
      } else {
        return parentLoader;
      }
    };

    final ModuleLayer reloaded;
    reloaded = layer.defineModules(configuration, function);

    return reloaded.findLoader(name);
  }

}
