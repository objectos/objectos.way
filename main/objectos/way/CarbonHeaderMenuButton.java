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
import objectos.way.Carbon.HeaderMenuButton;

final class CarbonHeaderMenuButton extends CarbonComponent implements HeaderMenuButton {

  private final Html.Id id;

  private String ariaLabel;

  private Script.Action onClick;

  private String title;

  public CarbonHeaderMenuButton(Html.Template tmpl) {
    super(tmpl);

    id = tmpl.nextId();
  }

  @Override
  public final HeaderMenuButton ariaLabel(String value) {
    ariaLabel = Check.notNull(value, "value == null");
    return this;
  }

  @Override
  public final HeaderMenuButton dataOnClick(Script.Action value) {
    Script.Action a;
    a = Check.notNull(value, "value");

    if (onClick == null) {
      onClick = a;
    } else {
      onClick = Script.join(onClick, a);
    }

    return this;
  }

  @Override
  public final HeaderMenuButton title(String value) {
    title = Check.notNull(value, "value == null");
    return this;
  }

  @Override
  public final Script.Action hideAction() {
    return Script.addClass(id, Carbon.HIDDEN);
  }

  @Override
  public final Script.Action showAction() {
    return Script.removeClass(id, Carbon.HIDDEN);
  }

  @Override
  public final void render() {
    tmpl.button(
        id, Carbon.HEADER_MENU_BUTTON,

        ariaLabel != null ? tmpl.ariaLabel(ariaLabel) : tmpl.noop(),

        title != null ? tmpl.title(title) : tmpl.noop(),

        tmpl.type("button"),

        onClick != null ? tmpl.dataOnClick(onClick) : tmpl.noop(),

        icon20(Carbon.Icon.MENU)
    );
  }

}