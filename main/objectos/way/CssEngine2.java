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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

final class CssEngine2 implements Css.Engine {

  record Notes() {

    static Notes get() {
      @SuppressWarnings("unused")
      Class<?> s;
      s = CssEngine2.class;

      return new Notes();
    }

  }

  @SuppressWarnings("unused")
  private static final Notes NOTES = Notes.get();

  private static final String ROOT = ":root";

  private Note.Sink noteSink;

  private Set<Class<?>> scanClasses = Set.of();

  private Set<Path> scanDirectories = Set.of();

  private Set<Class<?>> scanJars = Set.of();

  private final Map<Object, List<CssEngineValue>> theme = new HashMap<>();

  // ##################################################################
  // # BEGIN: Init
  // ##################################################################

  public final void init() {
    theme(Css.defaultTheme());
  }

  // ##################################################################
  // # END: Init
  // ##################################################################

  // ##################################################################
  // # BEGIN: Css.Engine API
  // ##################################################################

  @Override
  public final void noteSink(Note.Sink value) {
    if (noteSink != null) {
      throw new IllegalStateException("A Note.Sink has already been defined");
    }

    noteSink = Objects.requireNonNull(value, "value == null");
  }

  @Override
  public final void scanClass(Class<?> value) {
    final Class<?> c;
    c = Objects.requireNonNull(value, "value");

    if (scanClasses.isEmpty()) {
      scanClasses = new HashSet<>();
    }

    scanClasses.add(c);
  }

  @Override
  public final void scanDirectory(Path value) {
    final Path p;
    p = Objects.requireNonNull(value, "value == null");

    if (scanDirectories.isEmpty()) {
      scanDirectories = new HashSet<>();
    }

    scanDirectories.add(p);
  }

  @Override
  public final void scanJarFileOf(Class<?> value) {
    final Class<?> c;
    c = Objects.requireNonNull(value, "value == null");

    if (scanJars.isEmpty()) {
      scanJars = new HashSet<>();
    }

    scanJars.add(c);
  }

  @Override
  public final void theme(String value) {
    final String text;
    text = Objects.requireNonNull(value, "value == null");

    final List<CssEngineValue> values;
    values = theme.computeIfAbsent(ROOT, k -> new ArrayList<>());

    CssEngineValue.parse(text, values);
  }

  // ##################################################################
  // # END: Css.Engine API
  // ##################################################################

  // ##################################################################
  // # BEGIN: Test
  // ##################################################################

  final List<CssEngineValue> themeValues() {
    final List<CssEngineValue> list;
    list = theme.get(ROOT);

    return list != null ? list : List.of();
  }

  // ##################################################################
  // # END: Test
  // ##################################################################

}