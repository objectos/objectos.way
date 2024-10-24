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

import java.util.EnumMap;
import java.util.Map;
import objectos.way.Carbon.CarbonGridVariant;

final class CarbonGrid {

  private static final Html.ClassName __GRID = Html.ClassName.classText("""
      mx-auto grid w-full max-w-screen-max
      md:px-16px
      max:px-24px
      """);

  private static final Html.ClassName __GRID_WIDE = Html.ClassName.classText("""
      *:mx-16px
      """);

  private static final Html.ClassName __GRID_NARROW = Html.ClassName.classText("""
      *:mr-16px
      """);

  private static final Html.ClassName __GRID_CONDENSED = Html.ClassName.classText("""
      *:mx-0.5px
      """);

  static final Map<CarbonGridVariant, Html.ClassName> VARIANTS;

  static {
    VARIANTS = new EnumMap<>(CarbonGridVariant.class);

    VARIANTS.put(CarbonGridVariant.WIDE, Html.ClassName.className(__GRID, __GRID_WIDE));

    VARIANTS.put(CarbonGridVariant.NARROW, Html.ClassName.className(__GRID, __GRID_NARROW));

    VARIANTS.put(CarbonGridVariant.CONDENSED, Html.ClassName.className(__GRID, __GRID_CONDENSED));
  }

  public static Html.Instruction.OfElement render(
      Html.TemplateBase tmpl, Html.ElementName element, CarbonGridVariant variant, Html.Instruction... contents) {

    Html.ClassName variantClassName;
    variantClassName = VARIANTS.get(variant);

    return tmpl.element(
        element,

        variantClassName,

        tmpl.flatten(contents)
    );

  }

}
