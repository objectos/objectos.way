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
import objectos.way.Carbon.SideNav;
import objectos.way.Carbon.SideNavItems;
import objectos.way.Html.ClassName;

final class CarbonSideNav extends CarbonContainer implements SideNav {

  private final Html.Id id;

  private String ariaLabel;

  private String frameName;

  private String frameValue;

  private boolean header;

  private boolean persistent = true;

  private ClassName theme;

  CarbonSideNav(Html.Template tmpl) {
    super(tmpl);

    id = tmpl.nextId();
  }

  @Override
  public final SideNav ariaLabel(String value) {
    ariaLabel = Check.notNull(value, "value == null");
    return this;
  }

  @Override
  public final SideNav dataFrame(String name, String value) {
    frameName = Check.notNull(name, "name == null");
    frameValue = Check.notNull(value, "value == null");
    return this;
  }

  @Override
  public final SideNav offsetHeader() {
    header = true;
    return this;
  }

  @Override
  public final SideNav persistent(boolean value) {
    persistent = value;
    return this;
  }

  @Override
  public final SideNav theme(Html.ClassName value) {
    theme = Check.notNull(value, "value == null");
    return this;
  }

  private static final Html.ClassName VISIBLE = Html.className("more:visible");

  private static final Html.ClassName WIDTH = Html.className("more:w-256px");

  @Override
  public final Script.Action hideAction() {
    return Script.removeClass(id, VISIBLE, WIDTH);
  }

  @Override
  public final Script.Action showAction() {
    return Script.addClass(id, VISIBLE, WIDTH);
  }

  @Override
  public final SideNavItems addItems() {
    return addComponent(new CarbonSideNavItems(tmpl));
  }

  @Override
  public final void render() {
    tmpl.nav(
        id,

        tmpl.className("invisible fixed top-0px bottom-0px left-0px z-header"),
        tmpl.className("w-0px"),
        tmpl.className("bg"),
        tmpl.className("text-secondary"),
        tmpl.className("transition-all duration-100"),
        persistent ? tmpl.className("lg:visible lg:w-256px") : tmpl.className("lg:hidden"),

        header ? tmpl.className("mt-header") : tmpl.noop(),

        theme != null ? theme : tmpl.noop(),

        ariaLabel != null ? tmpl.ariaLabel(ariaLabel) : tmpl.noop(),

        frameName != null ? tmpl.dataFrame(frameName, frameValue) : tmpl.noop(),

        tmpl.tabindex("-1"),

        renderComponents()
    );
  }

}
