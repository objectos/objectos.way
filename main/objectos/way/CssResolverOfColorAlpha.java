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

final class CssResolverOfColorAlpha implements CssResolver {

  private final Css.Key key;

  private final Map<String, String> colors;

  private final String propertyName1;

  private final String propertyName2;

  public CssResolverOfColorAlpha(Css.Key key, Map<String, String> colors, String propertyName1) {
    this(key, colors, propertyName1, null);
  }

  public CssResolverOfColorAlpha(Css.Key key, Map<String, String> colors, String propertyName1, String propertyName2) {
    this.key = key;
    this.colors = colors;
    this.propertyName1 = propertyName1;
    this.propertyName2 = propertyName2;
  }

  @Override
  public final CssUtility resolve(String className, Css.Modifier modifier, boolean negative, CssValueType type, String value) {
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