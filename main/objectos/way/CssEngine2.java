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
import java.util.Collection;
import java.util.EnumSet;
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

  private sealed interface Stage {}

  @SuppressWarnings("unused")
  private static final Notes NOTES = Notes.get();

  private Stage stage = new Configuring();

  // ##################################################################
  // # BEGIN: Configuring
  // ##################################################################

  static final class Configuring implements Stage {

    enum Flag {
      SKIP_SYSTEM_THEME,

      SKIP_SYSTEM_VARIANTS;
    }

    Set<Flag> flags = Set.of();

    Note.Sink noteSink = Note.NoOpSink.create();

    Set<Class<?>> scanClasses = Set.of();

    Set<Path> scanDirectories = Set.of();

    Set<Class<?>> scanJars = Set.of();

    final Map<String, List<CssEngineValue>> systemNamespaces = new HashMap<>();

    final Map<String, List<CssEngineValue>> userNamespaces = new HashMap<>();

    List<CssEngineValue> userTheme = List.of();

    final Map<String, CssVariant> variants = new HashMap<>();

    final Config configure() {
      if (!flags.contains(Flag.SKIP_SYSTEM_THEME)) {
        systemTheme();
      }

      if (!flags.contains(Flag.SKIP_SYSTEM_VARIANTS)) {
        systemVariants();
      }

      userNamespaces();

      breakpointVariants();

      return new Config(
          keywords(),

          noteSink,

          Set.copyOf(scanClasses),

          Set.copyOf(scanDirectories),

          Set.copyOf(scanJars),

          Map.copyOf(variants)
      );
    }

    final void flags(Flag... values) {
      if (flags.isEmpty()) {
        flags = EnumSet.noneOf(Flag.class);
      }

      for (Flag flag : values) {
        flags.add(flag);
      }
    }

    private void systemTheme() {
      // parse system them
      final List<CssEngineValue> systemTheme;
      systemTheme = new ArrayList<>();

      CssEngineValue.parse(Css.defaultTheme(), systemTheme);

      // map to namespace
      for (CssEngineValue value : systemTheme) {
        switch (value) {
          case CssEngineValue.CustomProp prop -> {}

          case CssEngineValue.ThemeSkip skip -> throw new IllegalArgumentException();

          case CssEngineValue.ThemeVar var -> {
            final String ns;
            ns = var.ns;

            final List<CssEngineValue> values;
            values = systemNamespaces.computeIfAbsent(ns, key -> new ArrayList<>());

            values.add(value);
          }
        }
      }
    }

    private void systemVariants() {
      variant("dark", CssVariant.atRule("@media (prefers-color-scheme: dark)"));

      variant("active", CssVariant.suffix(":active"));
      variant("checked", CssVariant.suffix(":checked"));
      variant("disabled", CssVariant.suffix(":disabled"));
      variant("first-child", CssVariant.suffix(":first-child"));
      variant("focus", CssVariant.suffix(":focus"));
      variant("focus-visible", CssVariant.suffix(":focus-visible"));
      variant("hover", CssVariant.suffix(":hover"));
      variant("last-child", CssVariant.suffix(":last-child"));
      variant("visited", CssVariant.suffix(":visited"));

      variant("after", CssVariant.suffix("::after"));
      variant("backdrop", CssVariant.suffix("::backdrop"));
      variant("before", CssVariant.suffix("::before"));
      variant("first-letter", CssVariant.suffix("::first-letter"));
      variant("first-line", CssVariant.suffix("::first-line"));

      variant("*", CssVariant.suffix(" > *"));
      variant("**", CssVariant.suffix(" *"));
    }

    private void userNamespaces() {
      for (CssEngineValue value : userTheme) {
        switch (value) {
          case CssEngineValue.CustomProp prop -> {}

          case CssEngineValue.ThemeSkip skip -> {
            final String ns;
            ns = skip.ns();

            if ("*".equals(ns)) {
              systemNamespaces.clear();
            } else {
              systemNamespaces.remove(ns);
            }
          }

          case CssEngineValue.ThemeVar var -> {
            final String ns;
            ns = var.ns;

            final List<CssEngineValue> values;
            values = userNamespaces.computeIfAbsent(ns, key -> new ArrayList<>());

            values.add(value);
          }
        }
      }
    }

    private void breakpointVariants() {
      final List<CssEngineValue> breakpoints;
      breakpoints = new ArrayList<>();

      final String ns;
      ns = "breakpoint";

      final List<CssEngineValue> systemValues;
      systemValues = systemNamespaces.getOrDefault(ns, List.of());

      breakpoints.addAll(systemValues);

      final List<CssEngineValue> userValues;
      userValues = userNamespaces.getOrDefault(ns, List.of());

      breakpoints.addAll(userValues);

      for (CssEngineValue breakpoint : breakpoints) {
        final CssEngineValue.ThemeVar var;
        var = (CssEngineValue.ThemeVar) breakpoint;

        final String id;
        id = var.id;

        final CssVariant variant;
        variant = CssVariant.atRule("@media (min-width: " + var.value + ")");

        variant(id, variant);
      }
    }

    private void variant(String name, CssVariant variant) {
      final CssVariant maybeExisting;
      maybeExisting = variants.put(name, variant);

      if (maybeExisting == null) {
        return;
      }

      // TODO restore existing and log?
    }

    private Map<String, CssEngineValue.ThemeVar> keywords() {
      final Map<String, CssEngineValue.ThemeVar> keywords;
      keywords = new HashMap<>();

      keywords0(keywords, systemNamespaces.values());

      keywords0(keywords, userNamespaces.values());

      return Map.copyOf(keywords);
    }

    private void keywords0(Map<String, CssEngineValue.ThemeVar> keywords, Collection<List<CssEngineValue>> values) {
      for (List<CssEngineValue> list : values) {
        for (CssEngineValue value : list) {
          final CssEngineValue.ThemeVar var;
          var = (CssEngineValue.ThemeVar) value;

          final String ns;
          ns = var.ns;

          final String id;

          if ("breakpoint".equals(ns)) {
            id = "screen-" + var.id;
          } else {
            id = var.id;
          }

          final CssEngineValue.ThemeVar maybeExisting;
          maybeExisting = keywords.put(id, var);

          if (maybeExisting != null) {
            throw new IllegalArgumentException("Duplicate mapping for " + id + ": " + maybeExisting.value + ", " + var.value);
          }
        }
      }
    }

  }

  final Configuring configuring() {
    if (stage instanceof Configuring c) {
      return c;
    } else {
      throw new IllegalStateException("Not in configuring stage");
    }
  }

  @Override
  public final void noteSink(Note.Sink value) {
    final Configuring stage;
    stage = configuring();

    if (stage.noteSink != null) {
      throw new IllegalStateException("A Note.Sink has already been defined");
    }

    stage.noteSink = Objects.requireNonNull(value, "value == null");
  }

  @Override
  public final void scanClass(Class<?> value) {
    final Class<?> c;
    c = Objects.requireNonNull(value, "value");

    final Configuring stage;
    stage = configuring();

    if (stage.scanClasses.isEmpty()) {
      stage.scanClasses = new HashSet<>();
    }

    stage.scanClasses.add(c);
  }

  @Override
  public final void scanDirectory(Path value) {
    final Path p;
    p = Objects.requireNonNull(value, "value == null");

    final Configuring stage;
    stage = configuring();

    if (stage.scanDirectories.isEmpty()) {
      stage.scanDirectories = new HashSet<>();
    }

    stage.scanDirectories.add(p);
  }

  @Override
  public final void scanJarFileOf(Class<?> value) {
    final Class<?> c;
    c = Objects.requireNonNull(value, "value == null");

    final Configuring stage;
    stage = configuring();

    if (stage.scanJars.isEmpty()) {
      stage.scanJars = new HashSet<>();
    }

    stage.scanJars.add(c);
  }

  @Override
  public final void theme(String value) {
    final String text;
    text = Objects.requireNonNull(value, "value == null");

    final Configuring stage;
    stage = configuring();

    if (stage.userTheme.isEmpty()) {
      stage.userTheme = new ArrayList<>();
    }

    CssEngineValue.parse(text, stage.userTheme);
  }

  // ##################################################################
  // # END: Configuring
  // ##################################################################

  // ##################################################################
  // # BEGIN: Configured
  // ##################################################################

  record Config(

      Map<String, CssEngineValue.ThemeVar> keywords,

      Note.Sink noteSink,

      Set<Class<?>> scanClasses,

      Set<Path> scanDirectories,

      Set<Class<?>> scanJars,

      Map<String, CssVariant> variants

  ) implements Stage {

  }

  final Config configure() {
    final Configuring configuring;
    configuring = configuring();

    final Config config;
    config = configuring.configure();

    stage = config;

    return config;
  }

  // ##################################################################
  // # END: Configured
  // ##################################################################

  // ##################################################################
  // # BEGIN: Execute
  // ##################################################################

  public final void init() {
    theme(Css.defaultTheme());
  }

  // ##################################################################
  // # END: Execute
  // ##################################################################

  // ##################################################################
  // # BEGIN: Test
  // ##################################################################

  // ##################################################################
  // # END: Test
  // ##################################################################

}