/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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

record CssUtility(

    Css.Key key,

    String className,

    CssModifier modifier,

    CssProperties properties

) implements Comparable<CssUtility> {

  CssUtility(Css.Key key, String className, CssModifier modifier, CssProperties.Builder properties) {
    this(key, className, modifier, properties.build());
  }

  @Override
  public final int compareTo(CssUtility o) {
    int result;
    result = key.compareTo(o.key);

    if (result != 0) {
      return result;
    }

    return modifier.compareTo(o.modifier);
  }

  final void writeClassName(StringBuilder sb) {
    modifier.writeClassName(sb, className);
  }

  final List<CssVariant> atRules() {
    return modifier.atRules();
  }

}