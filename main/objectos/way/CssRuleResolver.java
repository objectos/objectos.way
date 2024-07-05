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

import java.util.List;
import java.util.Map;
import java.util.function.Function;

@SuppressWarnings("exports")
abstract class CssRuleResolver {

  static final class OfColorAlpha extends CssRuleResolver {

    private final CssKey key;

    private final String variableName;

    private final Map<String, String> colors;

    private final String[] propertyNames;

    public OfColorAlpha(CssKey key, String variableName, Map<String, String> colors, String... propertyNames) {
      this.key = key;
      this.colors = colors;
      this.variableName = variableName;
      this.propertyNames = propertyNames;
    }

    @Override
    public final CssRule resolve(String className, List<CssVariant> variants, boolean negative, String value) {
      String colorKey;
      colorKey = value;

      int opacity = 1;

      int slash;
      slash = value.indexOf('/');

      if (slash > 0) {
        throw new UnsupportedOperationException("Implement me");
      }

      String color;
      color = colors.get(colorKey);

      if (color == null) {
        return null;
      }

      CssProperties properties;
      properties = new CssProperties();

      if (color.contains(variableName)) {
        properties.add(variableName, Integer.toString(opacity));
      }

      for (String propertyName : propertyNames) {
        properties.add(propertyName, color);
      }

      return new CssRule.Of(key, className, variants, properties);
    }

  }

  static final class OfProperties extends CssRuleResolver {

    private final CssKey key;

    private final Map<String, String> properties;

    private final String[] propertyNames;

    private final Function<String, String> valueConverter;

    public OfProperties(CssKey key, Map<String, String> properties, String[] propertyNames) {
      this.key = key;
      this.properties = properties;
      this.propertyNames = propertyNames;
      this.valueConverter = Function.identity();
    }

    public OfProperties(CssKey key, Map<String, String> properties, String[] propertyNames, Function<String, String> valueConverter) {
      this.key = key;
      this.properties = properties;
      this.propertyNames = propertyNames;
      this.valueConverter = valueConverter;
    }

    @Override
    public final CssRule resolve(String className, List<CssVariant> variants, boolean negative, String value) {
      String resolved;
      resolved = properties.get(value);

      if (resolved == null) {
        return null;
      }

      resolved = valueConverter.apply(resolved);

      CssProperties properties;
      properties = new CssProperties();

      for (String propertyName : propertyNames) {
        properties.add(propertyName, resolved);
      }

      return new CssRule.Of(key, className, variants, properties);
    }

  }

  public abstract CssRule resolve(String className, List<CssVariant> variants, boolean negative, String value);

}