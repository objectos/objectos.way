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

record CssResolverOfBoxShadow(Map<String, String> props) implements CssResolver {

  @Override
  public final String resolve(String value) {
    return props.get(value);
  }

  @Override
  public final String resolveWithType(CssValueType type, String value) {
    return type == CssValueType.BOXED ? type.get(value) : null;
  }

  @Override
  public final CssUtility create(String className, Modifier modifier, boolean negative, String resolved) {
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