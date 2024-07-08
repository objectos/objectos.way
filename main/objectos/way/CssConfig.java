/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import objectos.notes.NoOpNoteSink;
import objectos.notes.NoteSink;
import objectos.util.map.GrowableMap;
import objectos.way.CssVariant.AppendTo;
import objectos.way.CssVariant.Breakpoint;

final class CssConfig {

  private NoteSink noteSink = NoOpNoteSink.of();

  private final List<Breakpoint> breakpoints = List.of(
      new Breakpoint(0, "sm", "640px"),
      new Breakpoint(1, "md", "768px"),
      new Breakpoint(2, "lg", "1024px"),
      new Breakpoint(3, "xl", "1280px"),
      new Breakpoint(4, "2xl", "1536px")
  );

  private Set<Class<?>> classes;

  private final Map<CssKey, CssProperties> overrides = new EnumMap<>(CssKey.class);

  private final Map<String, Set<CssKey>> prefixes = new HashMap<>();

  private final Map<CssKey, CssResolver> resolvers = new EnumMap<>(CssKey.class);

  private Map<String, String> rules;

  private boolean skipReset;

  private final Map<String, CssStaticUtility> staticUtilities = new GrowableMap<>();

  private Map<String, CssVariant> variants;

  public final void addRule(String selector, String contents) {
    if (rules == null) {
      rules = new GrowableMap<>();
    }

    rules.put(selector, contents);
  }

  public final void addUtility(String className, CssProperties properties) {

    CssStaticUtility utility;
    utility = new CssStaticUtility(CssKey.CUSTOM, properties);

    CssStaticUtility maybeExisting;
    maybeExisting = staticUtilities.put(className, utility);

    if (maybeExisting != null) {
      throw new IllegalArgumentException(
          "The class name " + className + " is mapped to an existing utility."
      );
    }

  }

  public final void addVariants(CssProperties props) {
    for (Map.Entry<String, String> entry : props) {
      String variantName;
      variantName = entry.getKey();

      String formatString;
      formatString = entry.getValue();

      CssVariant variant;
      variant = CssVariant.parse(formatString);

      if (variant instanceof CssVariant.Invalid invalid) {
        throw new IllegalArgumentException("Invalid formatString: " + invalid.reason());
      }

      Map<String, CssVariant> map;
      map = variants();

      if (map.containsKey(variantName)) {
        throw new IllegalArgumentException("Variant already defined: " + variantName);
      }

      map.put(variantName, variant);
    }
  }

  public final void classes(Set<Class<?>> set) {
    classes = set;
  }

  public final void noteSink(NoteSink noteSink) {
    this.noteSink = noteSink;
  }

  public final boolean skipReset() {
    return skipReset;
  }

  public final void skipReset(boolean value) {
    skipReset = value;
  }

  public final CssResolver getResolver(CssKey key) {
    return resolvers.get(key);
  }

  public final CssStaticUtility getStatic(String value) {
    return staticUtilities.get(value);
  }

  public final void colorUtility(
      CssKey key,
      Map<String, String> values,
      String prefix, String propertyName1, String propertyName2) {
    CssResolver resolver;
    resolver = new CssResolver.OfColorAlpha(key, values, propertyName1, propertyName2);

    customUtility(key, prefix, resolver);
  }

  public final void customUtility(CssKey key, String prefix, CssResolver resolver) {
    prefix(key, prefix);

    CssResolver maybeExisting;
    maybeExisting = resolvers.put(key, resolver);

    if (maybeExisting != null) {
      throw new IllegalArgumentException(
          "Key " + key + " already mapped to " + maybeExisting
      );
    }
  }

  public final void funcUtility(
      CssKey key,
      Map<String, String> values, CssValueFormatter formatter,
      String prefix, String propertyName1, String propertyName2) {

    CssResolver resolver;
    resolver = new CssResolver.OfProperties(key, values, formatter, propertyName1, propertyName2);

    customUtility(key, prefix, resolver);

  }

  public final void staticUtility(CssKey key, String text) {
    Map<String, CssProperties> table;
    table = Css.parseTable(text);

    for (Map.Entry<String, CssProperties> entry : table.entrySet()) {
      String className;
      className = entry.getKey();

      CssProperties properties;
      properties = entry.getValue();

      CssStaticUtility utility;
      utility = new CssStaticUtility(key, properties);

      CssStaticUtility maybeExisting;
      maybeExisting = staticUtilities.put(className, utility);

      if (maybeExisting != null) {
        throw new IllegalArgumentException(
            "Class name " + className + " already mapped to " + maybeExisting
        );
      }
    }
  }

  final Set<CssKey> getCandidates(String prefix) {
    return prefixes.get(prefix);
  }

  final void override(CssKey key, CssProperties properties) {
    overrides.put(key, properties);
  }

  private void prefix(CssKey key, String prefix) {
    Set<CssKey> set;
    set = prefixes.computeIfAbsent(prefix, s -> EnumSet.noneOf(CssKey.class));

    set.add(key);
  }

  final Map<String, String> values(CssKey key, String defaults) {
    CssProperties properties;
    properties = overrides.get(key);

    if (properties != null) {
      return properties.toMap();
    }

    CssProperties defaultProperties;
    defaultProperties = Css.parseProperties(defaults);

    return defaultProperties.toMap();
  }

  final Map<String, String> values(CssKey key, Function<CssConfig, Map<String, String>> defaultSupplier) {
    CssProperties properties;
    properties = overrides.get(key);

    if (properties != null) {
      return properties.toMap();
    }

    return defaultSupplier.apply(this);
  }

  final Map<String, String> values(CssKey key, Map<String, String> defaults) {
    CssProperties properties;
    properties = overrides.get(key);

    if (properties != null) {
      return properties.toMap();
    }

    return defaults;
  }

  //

  final NoteSink noteSink() {
    return noteSink;
  }

  final Iterable<Class<?>> classes() {
    return classes;
  }

  final List<Breakpoint> breakpoints() {
    return breakpoints;
  }

  final CssVariant getVariant(String variantName) {
    return variants().get(variantName);
  }

  final Map<String, String> rules() {
    if (rules == null) {
      rules = Map.of();
    }

    return rules;
  }

  private Map<String, CssVariant> variants() {
    if (variants == null) {
      variants = new GrowableMap<>();

      for (var breakpoint : breakpoints) {
        variants.put(breakpoint.name(), breakpoint);
      }

      variants.put("focus", new AppendTo(1, ":focus"));
      variants.put("hover", new AppendTo(2, ":hover"));
      variants.put("active", new AppendTo(3, ":active"));

      variants.put("after", new AppendTo(4, "::after"));
      variants.put("before", new AppendTo(5, "::before"));
    }

    return variants;
  }

}