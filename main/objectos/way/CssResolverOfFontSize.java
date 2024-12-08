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

final class CssResolverOfFontSize implements CssResolver {

  private final Map<String, String> fontSize;

  private final Map<String, String> lineHeight;

  public CssResolverOfFontSize(Map<String, String> fontSize, Map<String, String> lineHeight) {
    this.fontSize = fontSize;
    this.lineHeight = lineHeight;
  }

  @Override
  public final CssUtility resolve(String className, Css.Modifier modifier, boolean negative, CssValueType type, String value) {
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

  private CssUtility size(String className, Css.Modifier modifier, String value) {
    String size;
    size = fontSize.get(value);

    if (size == null) {
      return null;
    } else {
      return rule(className, modifier, size);
    }
  }

  private CssUtility rule(String className, Css.Modifier modifier, String value) {
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