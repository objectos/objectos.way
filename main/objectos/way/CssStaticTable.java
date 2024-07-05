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
import objectos.util.map.GrowableMap;

final class CssStaticTable {

  private final Map<String, CssRuleFactory> table = new GrowableMap<>();

  public final CssStaticTable rule(CssKey key, String className, String text) {
    Map<String, String> properties;
    properties = Css.parseProperties(text);

    CssRuleFactory factory;
    factory = new CssRuleFactory.OfProperties(key, properties);

    CssRuleFactory maybePrevious;
    maybePrevious = table.put(className, factory);

    if (maybePrevious != null) {
      throw new IllegalArgumentException(
          "Key " + key + " already mapped to " + maybePrevious
      );
    }

    return this;
  }

  public final CssRuleFactory get(String value) {
    return table.get(value);
  }

}