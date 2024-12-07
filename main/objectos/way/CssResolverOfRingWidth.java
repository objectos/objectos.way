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

record CssResolverOfRingWidth(Map<String, String> props) implements CssResolver {

  @Override
  public final Css.Rule resolve(String className, Css.Modifier modifier, boolean negative, CssValueType type, String value) {
    String resolved;
    resolved = props.get(value);

    if (resolved == null && type == CssValueType.BOXED_LENGTH) {
      resolved = type.get(value);
    }

    if (resolved == null) {
      return null;
    }

    CssProperties.Builder builder;
    builder = new CssProperties.Builder();

    builder.add(
        "--tw-ring-offset-shadow",
        "var(--tw-ring-inset, ) 0 0 0 var(--tw-ring-offset-width, 0px) var(--tw-ring-offset-color, #fff)"
    );

    builder.add(
        "--tw-ring-shadow",
        "var(--tw-ring-inset, ) 0 0 0 calc(" + resolved + " + var(--tw-ring-offset-width, 0px)) var(--tw-ring-color, rgb(59 130 246 / 0.5))"
    );

    builder.add(
        "box-shadow",
        "var(--tw-ring-offset-shadow, 0 0 #0000), var(--tw-ring-shadow, 0 0 #0000), var(--tw-shadow, 0 0 #0000)"
    );

    return new CssUtility(Css.Key.RING_WIDTH, className, modifier, builder);
  }

}