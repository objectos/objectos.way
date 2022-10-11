/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
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
package br.com.objectos.tools;

import java.lang.module.Configuration;
import java.lang.module.ModuleReference;
import java.lang.module.ResolvedModule;
import java.net.URI;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;
import objectos.util.GrowableList;
import objectos.util.GrowableMap;
import objectos.util.UnmodifiableList;
import objectos.util.UnmodifiableMap;

abstract class OptionsBuilderJava11 extends AbstractOptionsBuilder {

  private static class Config {

    final UnmodifiableList<String> addModules;

    final UnmodifiableMap<String, Path> modulePaths;

    Config(ConfigBuilder builder) {
      this.addModules = builder.getAddModules();
      this.modulePaths = builder.getModulePaths();
    }

    final void addTo(OptionsBuilderJava11 builder) {
      GrowableList<String> thatAddModules;
      thatAddModules = builder.addModules;

      thatAddModules.addAll(addModules);

      GrowableList<String> thatModulePaths;
      thatModulePaths = builder.modulePaths;

      for (Path path : modulePaths.values()) {
        String pathName;
        pathName = path.toString();

        thatModulePaths.add(pathName);
      }
    }

  }

  private static class ConfigBuilder {

    private final GrowableList<String> addModules = new GrowableList<>();

    private final GrowableMap<String, Path> modulePaths = new GrowableMap<>();

    public final Config build() {
      return new Config(this);
    }

    final void addModule(String name, Path path) {
      modulePaths.put(name, path);

      addModules.add(name);
    }

    final UnmodifiableList<String> getAddModules() {
      return addModules.toUnmodifiableList();
    }

    final UnmodifiableMap<String, Path> getModulePaths() {
      return modulePaths.toUnmodifiableMap();
    }

  }

  private static Config config = getConfig();
  private static final String PATH_SEPARATOR = System.getProperty("path.separator");
  private final GrowableList<String> addModules = new GrowableList<>();

  private final GrowableList<String> modulePaths = new GrowableList<>();

  private final GrowableList<String> patchModules = new GrowableList<>();

  OptionsBuilderJava11() {}

  private static Config getConfig() {
    ConfigBuilder builder;
    builder = new ConfigBuilder();

    getConfig0(builder);

    return builder.build();
  }

  private static void getConfig0(ConfigBuilder builder) {
    ModuleLayer boot;
    boot = ModuleLayer.boot();

    Configuration bootConfig;
    bootConfig = boot.configuration();

    Set<Module> bootModules;
    bootModules = boot.modules();

    for (Module bootModule : bootModules) {
      String name;
      name = bootModule.getName();

      if (name.startsWith("java.")) {
        continue;
      }

      if (name.startsWith("jdk.")) {
        continue;
      }

      Optional<ResolvedModule> maybeResolved;
      maybeResolved = bootConfig.findModule(name);

      if (maybeResolved.isEmpty()) {
        continue;
      }

      ResolvedModule resolved;
      resolved = maybeResolved.get();

      ModuleReference resolvedReference;
      resolvedReference = resolved.reference();

      Optional<URI> maybeLocation;
      maybeLocation = resolvedReference.location();

      if (maybeLocation.isEmpty()) {
        continue;
      }

      URI location;
      location = maybeLocation.get();

      Path path;
      path = Path.of(location);

      builder.addModule(name, path);
    }
  }

  @Override
  public final UnmodifiableList<String> build() {
    GrowableList<String> result;
    result = new GrowableList<>();

    config.addTo(this);

    doAddModules(result);

    doModulePath(result);

    doPatchModules(result);

    return result.toUnmodifiableList();
  }

  @Override
  public final void visitAddModule(String moduleName) {
    addModules.add(moduleName);
  }

  @Override
  public final void visitPatchModuleWithTestClasses(String moduleName) {
    UnmodifiableMap<String, Path> paths;
    paths = config.modulePaths;

    if (!paths.containsKey(moduleName)) {
      return;
    }

    Path path;
    path = paths.get(moduleName);

    if (!path.endsWith("classes")) {
      return;
    }

    Path target;
    target = path.getParent();

    Path testClasses;
    testClasses = target.resolve("test-classes");

    patchModules.add(moduleName + '=' + testClasses.toString());
  }

  private void doAddModules(GrowableList<String> result) {
    if (!addModules.isEmpty()) {
      result.add("--add-modules");

      String option;
      option = addModules.join(",");

      result.add(option);
    }
  }

  private void doModulePath(GrowableList<String> result) {
    if (!modulePaths.isEmpty()) {
      result.add("--module-path");

      String option;
      option = modulePaths.join(PATH_SEPARATOR);

      result.add(option);
    }
  }

  private void doPatchModules(GrowableList<String> result) {
    for (int i = 0; i < patchModules.size(); i++) {
      result.add("--patch-module");

      String patchModule;
      patchModule = patchModules.get(i);

      result.add(patchModule);
    }
  }

}
