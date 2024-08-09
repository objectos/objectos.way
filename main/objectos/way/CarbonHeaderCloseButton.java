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

import objectos.lang.object.Check;

final class CarbonHeaderCloseButton implements Carbon.Header.CloseButton {

  static final Html.ClassName HEADER_CLOSE_BUTTON = Html.className(
      CarbonHeaderMenuButton.BASE, "hidden"
  );

  private String description;

  private final Html.TemplateBase tmpl;

  CarbonHeaderCloseButton(Html.TemplateBase tmpl) {
    this.tmpl = tmpl;
  }

  @Override
  public final Carbon.Header.CloseButton description(String value) {
    description = Check.notNull(value, "value == null");

    return this;
  }

  @Override
  public final Html.ElementInstruction render() {
    return tmpl.button(
        HEADER_CLOSE_BUTTON,

        description != null ? tmpl.ariaLabel(description) : tmpl.noop(),

        description != null ? tmpl.title(description) : tmpl.noop(),

        tmpl.type("button"),

        Carbon.icon20(
            tmpl, Carbon.Icon.CLOSE,
            tmpl.className("fill-icon-primary"),
            tmpl.ariaHidden("true")
        )
    );
  }

}