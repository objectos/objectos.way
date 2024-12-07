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

record CssResolverOfBoxShadow(Map<String, String> props) implements CssResolver {

  @Override
  public final Css.Rule resolve(String className, Css.Modifier modifier, boolean negative, CssValueType type, String value) {
    String resolved;
    resolved = props.get(value);

    if (type == CssValueType.BOXED) {
      resolved = type.get(value);
    }

    if (resolved == null) {
      return null;
    }

    CssProperties.Builder builder;
    builder = new CssProperties.Builder();

    builder.add("--tw-shadow", resolved);

    String colored;
    colored = CssConfig.replaceColor(resolved, "var(--tw-shadow-color)");

    builder.add("--tw-shadow-colored", colored);

    builder.add(
        "box-shadow",
        "var(--tw-ring-offset-shadow, 0 0 #0000), var(--tw-ring-shadow, 0 0 #0000), var(--tw-shadow, 0 0 #0000)"
    );

    return new CssUtility(Css.Key.BOX_SHADOW, className, modifier, builder);
  }

}