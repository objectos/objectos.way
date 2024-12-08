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

final class CssResolverOfProperties implements CssResolver {

  private final Css.Key key;

  private final Map<String, String> properties;

  private final Css.ValueFormatter valueFormatter;

  private final Set<CssValueType> valueTypes;

  private final String propertyName1;

  private final String propertyName2;

  public CssResolverOfProperties(Css.Key key, Map<String, String> properties, Css.ValueFormatter valueFormatter, String propertyName1, String propertyName2) {
    this(key, properties, valueFormatter, Set.of(), propertyName1, propertyName2);
  }

  public CssResolverOfProperties(Css.Key key, Map<String, String> properties, Css.ValueFormatter valueFormatter, Set<CssValueType> valueTypes, String propertyName1, String propertyName2) {
    this.key = key;
    this.properties = properties;
    this.valueFormatter = valueFormatter;
    this.valueTypes = valueTypes;
    this.propertyName1 = propertyName1;
    this.propertyName2 = propertyName2;
  }

  @Override
  public final CssUtility resolve(String className, Css.Modifier modifier, boolean negative, CssValueType type, String value) {
    String resolved;
    resolved = properties.get(value);

    if (resolved == null && valueTypes.contains(type)) {
      resolved = type.get(value);
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