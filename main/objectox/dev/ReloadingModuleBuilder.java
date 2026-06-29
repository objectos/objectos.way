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

import java.lang.module.Configuration;
import java.lang.module.ModuleFinder;
import java.lang.module.ModuleReference;
import java.lang.module.ResolvedModule;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;

final class ReloadingModuleBuilder {

  private final Class<?> context;

  ReloadingModuleBuilder(Class<?> context) {
    this.context = context;
  }

  public final ReloadingModule build() {
    final Module module;
    module = context.getModule();

    final String moduleName;
    moduleName = module.getName();

    final ModuleLayer layer;
    layer = module.getLayer();

    final Configuration configuration;
    configuration = layer.configuration();

    final Optional<ResolvedModule> maybeResolved;
    maybeResolved = configuration.findModule(moduleName);

    if (maybeResolved.isEmpty()) {
      throw new IllegalArgumentException("Could not find ResolvedModule for " + moduleName);
    }

    final ResolvedModule resolved;
    resolved = maybeResolved.get();

    final ModuleReference reference;
    reference = resolved.reference();

    final Optional<URI> maybeLocation;
    maybeLocation = reference.location();

    if (maybeLocation.isEmpty()) {
      throw new IllegalArgumentException("Could not resolve location for " + moduleName);
    }

    final URI uri;
    uri = maybeLocation.get();

    final Path location;
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

    final Configuration moduleConfiguration;
    moduleConfiguration = configuration.resolve(finder, afterFinder, roots);

    return new ReloadingModule(
        moduleConfiguration,

        layer,

        location,

        moduleName,

        context.getClassLoader()
    );
  }

}
