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

sealed abstract class CssRuleResolver {

  static final class OfColorAlpha extends CssRuleResolver {

    private final CssKey key;

    private final Map<String, String> colors;

    private final String propertyName;

    private final String variableName;

    public OfColorAlpha(CssKey key, Map<String, String> colors, String propertyName, String variableName) {
      this.key = key;
      this.colors = colors;
      this.propertyName = propertyName;
      this.variableName = variableName;
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

      Map<String, String> properties;
      properties = Css.properties(variableName, Integer.toString(opacity), propertyName, color);

      return new CssRule.Of(key, className, variants, properties);
    }

  }

  public abstract CssRule resolve(String className, List<CssVariant> variants, boolean negative, String value);

}