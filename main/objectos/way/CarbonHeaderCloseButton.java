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

final class CarbonHeaderCloseButton implements Carbon.HeaderCloseButton {

  static final Html.ClassName HEADER_CLOSE_BUTTON = Html.className(
      CarbonHeaderMenuButton.BASE, "hidden"
  );

  private Script.Action action;

  private String description;

  private final Html.Id id;

  private final Html.TemplateBase tmpl;

  CarbonHeaderCloseButton(Html.TemplateBase tmpl, Carbon.HeaderCloseButton.Value[] values) {
    this.tmpl = tmpl;

    Html.Id id = null;

    for (var value : values) { // implicit null-check
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
        id,

        HEADER_CLOSE_BUTTON,

        description != null ? tmpl.ariaLabel(description) : tmpl.noop(),

        description != null ? tmpl.title(description) : tmpl.noop(),

        tmpl.type("button"),

        action != null ? tmpl.dataOnClick(action) : tmpl.noop(),

        Carbon.icon20(
            tmpl, Carbon.Icon.CLOSE,
            tmpl.className("fill-icon-primary"),
            tmpl.ariaHidden("true")
        )
    );
  }

  final void accept(CarbonHeaderMenuButton menuButton, CarbonSideNav sideNavigation) {
    action = Script.actions(
        hideAction(),
        sideNavigation != null ? sideNavigation.hideAction() : Script.noop(),
        menuButton != null ? menuButton.showAction() : Script.noop()
    );
  }

  final Script.Action action() {
    return action;
  }

  final Script.Action hideAction() {
    return Script.replaceClass(id, "hidden", "flex", true);
  }

  final Script.Action showAction() {
    return Script.replaceClass(id, "hidden", "flex");
  }

}