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

import java.nio.file.Path;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import objectos.way.Css.Layer;

final class CssConfig implements Css.Config {

  String base = Css.defaultBase();

  Set<Class<?>> classesToScan = Set.of();

  Set<Path> directoriesToScan = Set.of();

  Set<Class<?>> jarFilesToScan = Set.of();

  Note.Sink noteSink = Note.NoOpSink.INSTANCE;

  final Set<Css.Layer> skipLayer = EnumSet.noneOf(Css.Layer.class);

  String theme = "";

  Map<String, String> themeQueries = Map.of();

  public final void base(String value) {
    base = Objects.requireNonNull(value, "value == null");
  }

  @Override
  public final void noteSink(Note.Sink value) {
    noteSink = Objects.requireNonNull(value, "value == null");
  }

  @Override
  public final void scanClass(Class<?> value) {
    Objects.requireNonNull(value, "value == null");

    if (classesToScan.isEmpty()) {
      classesToScan = Util.createSet();
    }

    classesToScan.add(value);
  }

  @Override
  public final void scanDirectory(Path value) {
    Objects.requireNonNull(value, "value == null");

    if (directoriesToScan.isEmpty()) {
      directoriesToScan = Util.createSet();
    }

    directoriesToScan.add(value);
  }

  @Override
  public final void scanJarFileOf(Class<?> value) {
    Objects.requireNonNull(value, "value == null");

    if (jarFilesToScan.isEmpty()) {
      jarFilesToScan = Util.createSet();
    }

    jarFilesToScan.add(value);
  }

  public final void skipLayer(Css.Layer value) {
    Objects.requireNonNull(value, "value == null");

    skipLayer.add(value);
  }

  @Override
  public final void theme(String value) {
    Check.state(theme.isEmpty(), "Theme was already set");

    theme = Objects.requireNonNull(value, "value == null");
  }

  @Override
  public final void theme(String query, String value) {
    Objects.requireNonNull(query, "query == null");
    Objects.requireNonNull(value, "value == null");

    if (themeQueries.isEmpty()) {
      themeQueries = LinkedHashMap.newLinkedHashMap(2);
    }

    String maybeExisting;
    maybeExisting = themeQueries.put(query, value);

    if (maybeExisting != null) {
      throw new IllegalStateException("Theme was already set for " + query);
    }
  }

  final boolean contains(Layer value) {
    return skipLayer.contains(value);
  }

}