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

import objectos.way.Carbon.CarbonTileVariant;
import objectos.way.Carbon.Icon;
import objectos.way.Html.Instruction;
import objectos.way.Html.TemplateBase;

final class CarbonTile {

  private static final Html.ClassName TILE_CLICKABLE = Html.ClassName.classText("""
      relative block min-w-128px min-h-64px
      cursor-pointer
      bg-layer
      p-16px
      text-text-primary
      outline outline-2 -outline-offset-2 outline-transparent
      transition-colors duration-200

      active:text-link-primary
      active:outline-1 active:outline-focus

      focus:outline-focus

      hover:bg-layer-hover
      """);

  private static final Html.ClassName TILE_ICON = Html.ClassName.classText("""
      absolute right-16px bottom-16px
      fill-icon-interactive
      """);

  private CarbonTile() {}

  public static Html.ElementInstruction renderTile(TemplateBase tmpl, CarbonTileVariant variant, Icon icon, Instruction[] contents) {
    return switch (variant) {
      case TILE_CLICKABLE -> tmpl.a(
          Carbon.BODY_COMPACT_01, TILE_CLICKABLE,

          tmpl.flatten(contents),

          icon != null ? Carbon.renderIcon16(tmpl, icon, TILE_ICON, Carbon.ARIA_HIDDEN_TRUE) : Html.NOOP
      );
    };
  }

}
