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

import java.util.Map;
import java.util.Set;

@SuppressWarnings("exports")
interface CssResolver {

  static final class OfColorAlpha implements CssResolver {

    private final Css.Key key;

    private final Map<String, String> colors;

    private final String propertyName1;

    private final String propertyName2;

    public OfColorAlpha(Css.Key key, Map<String, String> colors, String propertyName1) {
      this(key, colors, propertyName1, null);
    }

    public OfColorAlpha(Css.Key key, Map<String, String> colors, String propertyName1, String propertyName2) {
      this.key = key;
      this.colors = colors;
      this.propertyName1 = propertyName1;
      this.propertyName2 = propertyName2;
    }

    @Override
    public final Css.Rule resolve(String className, Css.Modifier modifier, boolean negative, Css.ValueType type, String value) {
      String colorKey;
      colorKey = value;

      int slash;
      slash = value.indexOf('/');

      if (slash > 0) {
        throw new UnsupportedOperationException("Implement me :: " + value);
      }

      String color;
      color = colors.get(colorKey);

      if (color == null) {
        return null;
      }

      CssProperties.Builder properties;
      properties = new CssProperties.Builder();

      properties.add(propertyName1, color);

      if (propertyName2 != null) {
        properties.add(propertyName2, color);
      }

      return new CssUtility(key, className, modifier, properties);
    }

  }

  static final class OfFontSize implements CssResolver {

    private final Map<String, String> fontSize;

    private final Map<String, String> lineHeight;

    public OfFontSize(Map<String, String> fontSize, Map<String, String> lineHeight) {
      this.fontSize = fontSize;
      this.lineHeight = lineHeight;
    }

    @Override
    public final Css.Rule resolve(String className, Css.Modifier modifier, boolean negative, Css.ValueType type, String value) {
      int slash;
      slash = value.indexOf('/');

      if (slash == 0) {
        // empty size -> invalid
        return null;
      }

      if (slash < 0) {
        return size(className, modifier, value);
      }

      String sizeKey;
      sizeKey = value.substring(0, slash);

      String size;
      size = fontSize.get(sizeKey);

      if (size == null) {
        return null;
      }

      String heightKey;
      heightKey = value.substring(slash + 1);

      String height;
      height = lineHeight.get(heightKey);

      if (height == null) {
        return null;
      }

      size = extractSize(size);

      CssProperties.Builder properties;
      properties = new CssProperties.Builder();

      properties.add("font-size", size);

      properties.add("line-height", height);

      return new CssUtility(Css.Key.FONT_SIZE, className, modifier, properties);
    }

    private String extractSize(String size) {
      String result = size;

      int slash;
      slash = size.indexOf('/');

      if (slash > 0) {
        result = size.substring(0, slash);
      }

      return result;
    }

    private Css.Rule size(String className, Css.Modifier modifier, String value) {
      String size;
      size = fontSize.get(value);

      if (size == null) {
        return null;
      } else {
        return rule(className, modifier, size);
      }
    }

    private Css.Rule rule(String className, Css.Modifier modifier, String value) {
      CssProperties.Builder properties;
      properties = new CssProperties.Builder();

      int slash;
      slash = value.indexOf('/');

      if (slash < 0) {
        properties.add("font-size", value);

        return new CssUtility(Css.Key.FONT_SIZE, className, modifier, properties);
      }

      String fontSize;
      fontSize = value.substring(0, slash);

      properties.add("font-size", fontSize);

      String lineHeight;
      lineHeight = value.substring(slash + 1);

      slash = lineHeight.indexOf('/');

      if (slash < 0) {
        properties.add("line-height", lineHeight);

        return new CssUtility(Css.Key.FONT_SIZE, className, modifier, properties);
      }

      value = lineHeight;

      lineHeight = value.substring(0, slash);

      properties.add("line-height", lineHeight);

      String letterSpacing;
      letterSpacing = value.substring(slash + 1);

      slash = letterSpacing.indexOf('/');

      if (slash < 0) {
        properties.add("letter-spacing", letterSpacing);

        return new CssUtility(Css.Key.FONT_SIZE, className, modifier, properties);
      }

      value = letterSpacing;

      letterSpacing = value.substring(0, slash);

      properties.add("letter-spacing", letterSpacing);

      String fontWeight;
      fontWeight = value.substring(slash + 1);

      properties.add("font-weight", fontWeight);

      return new CssUtility(Css.Key.FONT_SIZE, className, modifier, properties);
    }

  }

  static final class OfTransitionProperty implements CssResolver {

    private static final String[] PROPERTY_NAMES = {"transition-property", "transition-timing-function", "transition-duration"};

    private final Map<String, String> properties;

    public OfTransitionProperty(Map<String, String> properties) {
      this.properties = properties;
    }

    @Override
    public final Css.Rule resolve(String className, Css.Modifier modifier, boolean negative, Css.ValueType type, String value) {
      String resolved;
      resolved = properties.get(value);

      if (resolved == null) {
        return null;
      }

      return resolveSlashes(Css.Key.TRANSITION_PROPERTY, className, modifier, PROPERTY_NAMES, resolved);
    }

    final Css.Rule resolveSlashes(
        Css.Key key,
        String className, Css.Modifier modifier,
        String[] propertyNames, String value) {

      CssProperties.Builder properties;
      properties = new CssProperties.Builder();

      String previousName;
      previousName = null;

      for (int idx = 0; idx < propertyNames.length; idx++) {

        String propertyName;
        propertyName = propertyNames[idx];

        int slash;
        slash = value.indexOf('/');

        if (slash < 0) {
          properties.add(propertyName, value);

          return new CssUtility(key, className, modifier, properties);
        }

        String thisValue;
        thisValue = value.substring(0, slash);

        properties.add(propertyName, thisValue);

        previousName = propertyName;

        value = value.substring(slash + 1);

      }

      if (previousName != null) {
        properties.add(previousName, value);
      }

      return new CssUtility(key, className, modifier, properties);

    }

  }

  static final class OfProperties implements CssResolver {

    private final Css.Key key;

    private final Map<String, String> properties;

    private final Css.ValueFormatter valueFormatter;

    private final Set<Css.ValueType> valueTypes;

    private final String propertyName1;

    private final String propertyName2;

    public OfProperties(Css.Key key, Map<String, String> properties, Css.ValueFormatter valueFormatter, String propertyName1) {
      this(key, properties, valueFormatter, propertyName1, null);
    }

    public OfProperties(Css.Key key, Map<String, String> properties, Css.ValueFormatter valueFormatter, String propertyName1, String propertyName2) {
      this(key, properties, valueFormatter, Set.of(), propertyName1, propertyName2);
    }

    public OfProperties(Css.Key key, Map<String, String> properties, Css.ValueFormatter valueFormatter, Set<Css.ValueType> valueTypes, String propertyName1, String propertyName2) {
      this.key = key;
      this.properties = properties;
      this.valueFormatter = valueFormatter;
      this.valueTypes = valueTypes;
      this.propertyName1 = propertyName1;
      this.propertyName2 = propertyName2;
    }

    @Override
    public final Css.Rule resolve(String className, Css.Modifier modifier, boolean negative, Css.ValueType type, String value) {
      String resolved;

      if (type == Css.ValueType.STANDARD) {
        resolved = properties.get(value);
      } else if (valueTypes.contains(type)) {
        resolved = type.get(value);
      } else {
        resolved = null;
      }

      if (resolved == null) {
        return null;
      }

      resolved = valueFormatter.format(resolved, negative);

      CssProperties.Builder properties;
      properties = new CssProperties.Builder();

      properties.add(propertyName1, resolved);

      if (propertyName2 != null) {
        properties.add(propertyName2, resolved);
      }

      return new CssUtility(key, className, modifier, properties);
    }

  }

  Css.Rule resolve(String className, Css.Modifier modifier, boolean negative, Css.ValueType type, String value);

}