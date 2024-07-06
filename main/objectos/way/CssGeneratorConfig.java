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
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Function;
import objectos.notes.NoteSink;
import objectos.way.CssVariant.Breakpoint;

sealed abstract class CssGeneratorConfig permits CssGenerator {

  private final Map<CssKey, CssProperties> overrides = new EnumMap<>(CssKey.class);

  private final Map<CssKey, Object> store = new EnumMap<>(CssKey.class);

  private final Map<String, Set<CssKey>> prefixes = new HashMap<>();

  final Map<String, String> colorsAlpha(CssKey key, String variableName) {
    CssProperties properties;
    properties = overrides.get(key);

    if (properties != null) {
      return colorsAlpha(properties, variableName);
    }

    return colors();
  }

  private Map<String, String> colorsAlpha(CssProperties properties, String variableName) {
    return properties.toMap();
  }

  final Object get(CssKey key) {
    Object existing;
    existing = store.get(key);

    if (existing == null) {
      throw new NoSuchElementException(
          "Key " + key + " is not mapped to any value"
      );
    }

    return existing;
  }

  final Object getOrCompute(CssKey key, Function<CssKey, Object> function) {
    return store.computeIfAbsent(key, function);
  }

  final Set<CssKey> getCandidates(String prefix) {
    return prefixes.get(prefix);
  }

  final void override(CssKey key, CssProperties properties) {
    overrides.put(key, properties);
  }

  final Object prefix(CssKey key, String prefix) {
    Set<CssKey> set;
    set = prefixes.computeIfAbsent(prefix, s -> EnumSet.noneOf(CssKey.class));

    set.add(key);

    return null;
  }

  final void put(CssKey key, Object value) {
    Object maybeExisting;
    maybeExisting = store.put(key, value);

    if (maybeExisting != null) {
      throw new IllegalStateException(
          "Key " + key + " already mapped to " + maybeExisting
      );
    }
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

  final Map<String, String> values(CssKey key, Function<CssGeneratorConfig, Map<String, String>> defaultSupplier) {
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