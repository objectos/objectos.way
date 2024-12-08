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

final class CssResolverOfTransitionProperty implements CssResolver {

  private static final String[] PROPERTY_NAMES = {"transition-property", "transition-timing-function", "transition-duration"};

  private final Map<String, String> properties;

  public CssResolverOfTransitionProperty(Map<String, String> properties) {
    this.properties = properties;
  }

  @Override
  public final CssUtility resolve(String className, Css.Modifier modifier, boolean negative, CssValueType type, String value) {
    String resolved;
    resolved = properties.get(value);

    if (resolved == null) {
      return null;
    }

    return resolveSlashes(Css.Key.TRANSITION_PROPERTY, className, modifier, PROPERTY_NAMES, resolved);
  }

  private CssUtility resolveSlashes(
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