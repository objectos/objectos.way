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
import objectos.way.Css.Modifier;

final class CssResolverOfColorAlpha implements CssResolver {

  private final Css.Key key;

  private final Map<String, String> colors;

  private final String propertyName1;

  private final String propertyName2;

  public CssResolverOfColorAlpha(Css.Key key, Map<String, String> colors, String propertyName1, String propertyName2) {
    this.key = key;
    this.colors = colors;
    this.propertyName1 = propertyName1;
    this.propertyName2 = propertyName2;
  }

  @Override
  public final String resolve(String value) {
    return colors.get(value);
  }

  @Override
  public final String resolveWithType(CssValueType type, String value) {
    return null;
  }

  @Override
  public final CssUtility create(String className, Modifier modifier, boolean negative, String resolved) {
    CssProperties.Builder properties;
    properties = new CssProperties.Builder();

    properties.add(propertyName1, resolved);

    if (propertyName2 != null) {
      properties.add(propertyName2, resolved);
    }

    return new CssUtility(key, className, modifier, properties);
  }

}