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
import objectos.util.list.GrowableList;
import objectos.util.map.GrowableMap;
import objectos.way.CssVariant.AppendTo;
import objectos.way.CssVariant.Breakpoint;

final class CssConfig {

  private NoteSink noteSink = NoOpNoteSink.of();

  private List<Breakpoint> breakpoints = List.of(
      new Breakpoint(0, "sm", "640px"),
      new Breakpoint(1, "md", "768px"),
      new Breakpoint(2, "lg", "1024px"),
      new Breakpoint(3, "xl", "1280px"),
      new Breakpoint(4, "2xl", "1536px")
  );

  private List<String> baseLayer;

  private Set<Class<?>> classes;

  private Map<String, String> components;

  private final Map<CssKey, CssProperties> overrides = new EnumMap<>(CssKey.class);

  private final Map<String, Set<CssKey>> prefixes = new HashMap<>();

  private CssPropertyType propertyType = CssPropertyType.PHYSICAL;

  private final Map<CssKey, CssResolver> resolvers = new EnumMap<>(CssKey.class);

  private boolean skipReset;

  private final Map<String, CssStaticUtility> staticUtilities = new GrowableMap<>();

  private Map<String, CssVariant> variants;

  private boolean variantsInitialized;

  public final void addComponent(String name, String definition) {
    if (components == null) {
      components = new GrowableMap<>();
    }

    String existing;
    existing = components.put(name, definition);

    if (existing != null) {
      throw new IllegalArgumentException(
          "The class name " + name + " is mapped to an existing component."
      );
    }
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
    if (variants == null) {
      variants = new GrowableMap<>();
    }

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

      putVariant(variantName, variant);
    }
  }

  public final void baseLayer(String contents) {
    if (baseLayer == null) {
      baseLayer = new GrowableList<>();
    }

    baseLayer.add(contents);
  }

  public final void breakpoints(CssProperties properties) {
    int index = 0;

    GrowableList<Breakpoint> builder;
    builder = new GrowableList<>();

    for (var entry : properties) {
      String name;
      name = entry.getKey();

      String value;
      value = entry.getValue();

      Breakpoint breakpoint;
      breakpoint = new Breakpoint(index++, name, value);

      builder.add(breakpoint);
    }

    breakpoints = builder.toUnmodifiableList();
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

  public final void useLogicalProperties() {
    propertyType = CssPropertyType.LOGICAL;
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

  final List<Breakpoint> breakpoints() {
    return breakpoints;
  }

  final Iterable<String> baseLayer() {
    return baseLayer != null ? baseLayer : List.of();
  }

  final Iterable<Class<?>> classes() {
    return classes;
  }

  final CssVariant getVariant(String variantName) {
    return variants().get(variantName);
  }

  final NoteSink noteSink() {
    return noteSink;
  }

  final CssPropertyType propertyType() {
    return propertyType;
  }

  private Map<String, CssVariant> variants() {
    if (variants == null) {
      variants = new GrowableMap<>();
    }

    if (!variantsInitialized) {
      for (var breakpoint : breakpoints) {
        putVariant(breakpoint.name(), breakpoint);
      }

      putVariant("focus", new AppendTo(1, ":focus"));
      putVariant("hover", new AppendTo(2, ":hover"));
      putVariant("active", new AppendTo(3, ":active"));
      putVariant("*", new AppendTo(4, " > *"));

      putVariant("after", new AppendTo(5, "::after"));
      putVariant("before", new AppendTo(6, "::before"));

      putVariant("ltr", new AppendTo(7, ":where([dir=\"ltr\"], [dir=\"ltr\"] *)"));
      putVariant("rtl", new AppendTo(7, ":where([dir=\"rtl\"], [dir=\"rtl\"] *)"));

      variantsInitialized = true;
    }

    return variants;
  }

  private void putVariant(String name, CssVariant variant) {
    if (variants.containsKey(name)) {
      throw new IllegalArgumentException("Variant already defined: " + name);
    }

    variants.put(name, variant);
  }

}