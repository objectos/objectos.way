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

import objectos.way.Carbon.CarbonDescription;
import objectos.way.Carbon.CarbonId;

final class CarbonHeaderMenuButton implements Carbon.HeaderMenuButton {

  static final Html.ClassName BASE = Html.className("""
  cursor-pointer appearance-none
  size-header items-center justify-center
  border border-transparent
  transition-colors duration-100
  active:bg-background-active
  focus:border-focus focus:outline-none
  hover:bg-background-hover

  lg:hidden
  """);

  static final Html.ClassName HEADER_MENU_BUTTON = Html.className(
      BASE, "flex"
  );

  static final Html.ClassName HEADER_BUTTON_ICON = Html.className(
      "fill-primary"
  );

  private Script.Action dataOnClick;

  private String description;

  private final Html.Id id;

  private final Html.TemplateBase tmpl;

  CarbonHeaderMenuButton(Html.TemplateBase tmpl, Carbon.HeaderMenuButton.Value[] values) {
    this.tmpl = tmpl;

    Html.Id id = null;

    for (var value : values) {
      switch (value) {
        case CarbonDescription o -> description = o.value();

        case CarbonId o -> id = o.value();
      }
    }

    this.id = id != null ? id : tmpl.nextId();
  }

  @Override
  public final Html.ElementInstruction render() {
    return tmpl.button(
        id != null ? id : tmpl.nextId(),

        HEADER_MENU_BUTTON,

        description != null ? tmpl.ariaLabel(description) : tmpl.noop(),

        description != null ? tmpl.title(description) : tmpl.noop(),

        tmpl.type("button"),

        dataOnClick != null ? tmpl.dataOnClick(dataOnClick) : tmpl.noop(),

        Carbon.icon20(
            tmpl, Carbon.Icon.MENU,
            tmpl.className("fill-icon-primary"),
            tmpl.ariaHidden("true")
        )
    );
  }

  final void accept(CarbonHeaderCloseButton closeButton, CarbonSideNav sideNavigation) {
    dataOnClick = Script.actions(
        hideAction(),
        closeButton != null ? closeButton.showAction() : Script.noop(),
        sideNavigation != null ? sideNavigation.showAction() : Script.noop()
    );
  }

  final Script.Action hideAction() {
    return Script.replaceClass(id, "hidden", "flex", true);
  }

  final Script.Action showAction() {
    return Script.replaceClass(id, "hidden", "flex");
  }

}