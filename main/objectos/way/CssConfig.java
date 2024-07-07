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
import objectos.notes.NoteSink;
import objectos.util.map.GrowableMap;
import objectos.way.CssVariant.Breakpoint;

sealed abstract class CssConfig permits CssGenerator {

  private final Map<CssKey, CssProperties> overrides = new EnumMap<>(CssKey.class);

  private final Map<String, Set<CssKey>> prefixes = new HashMap<>();

  private final Map<CssKey, CssRuleResolver> resolvers = new EnumMap<>(CssKey.class);

  private final Map<String, CssStaticUtility> staticUtilities = new GrowableMap<>();

  public final CssRuleResolver getResolver(CssKey candidate) {
    throw new UnsupportedOperationException("Implement me");
  }

  public final CssStaticUtility getStatic(String value) {
    return staticUtilities.get(value);
  }

  public final void colorUtility(
      CssKey key,
      Map<String, String> values,
      String prefix, String propertyName1, String propertyName2) {
    CssRuleResolver resolver;
    resolver = new CssRuleResolver.OfColorAlpha(key, values, propertyName1, propertyName2);

    customUtility(key, prefix, resolver);
  }

  public final void customUtility(CssKey key, String prefix, CssRuleResolver resolver) {
    prefix(key, prefix);

    CssRuleResolver maybeExisting;
    maybeExisting = resolvers.put(key, resolver);

    if (maybeExisting != null) {
      throw new IllegalArgumentException(
          "Key " + key + " already mapped to " + maybeExisting
      );
    }
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

  //

  boolean skipReset;

  abstract NoteSink noteSink();

  abstract Iterable<Class<?>> classes();

  abstract List<Breakpoint> breakpoints();

  abstract CssVariant getVariant(String variantName);

  abstract Map<String, String> borderSpacing();

  abstract Map<String, String> borderRadius();

  abstract Map<String, String> borderWidth();

  abstract Map<String, String> colors();

  abstract Map<String, String> content();

  abstract Map<String, String> cursor();

  abstract Map<String, String> flexGrow();

  abstract Map<String, String> fontSize();

  abstract Map<String, String> fontWeight();

  abstract Map<String, String> gap();

  abstract Map<String, String> gridColumn();

  abstract Map<String, String> gridColumnEnd();

  abstract Map<String, String> gridColumnStart();

  abstract Map<String, String> gridTemplateColumns();

  abstract Map<String, String> gridTemplateRows();

  abstract Map<String, String> height();

  abstract Map<String, String> inset();

  abstract Map<String, String> letterSpacing();

  abstract Map<String, String> lineHeight();

  abstract Map<String, String> margin();

  abstract Map<String, String> maxWidth();

  abstract Map<String, String> minWidth();

  abstract Map<String, String> opacity();

  abstract Map<String, String> outlineOffset();

  abstract Map<String, String> outlineWidth();

  abstract Map<String, String> padding();

  abstract Map<String, String> rules();

  abstract Map<String, String> size();

  abstract Map<String, String> stroke();

  abstract Map<String, String> strokeWidth();

  abstract Map<String, String> transitionDuration();

  abstract Map<String, String> transitionProperty();

  abstract Map<String, String> utilities();

  abstract Map<String, String> width();

  abstract Map<String, String> zIndex();

  abstract Map<String, String> spacing();

}