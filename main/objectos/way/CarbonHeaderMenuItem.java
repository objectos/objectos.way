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
import objectos.way.Carbon.HeaderMenuItem;

final class CarbonHeaderMenuItem extends CarbonComponent implements Carbon.HeaderMenuItem {

  private boolean active;

  private String href;

  private boolean sideNav;

  private String text;

  private Script.Action onClick;

  CarbonHeaderMenuItem(Html.Template tmpl) {
    super(tmpl);
  }

  @Override
  public final HeaderMenuItem active(boolean value) {
    active = value;
    return this;
  }

  @Override
  public final HeaderMenuItem href(String value) {
    href = Check.notNull(value, "value == null");
    return this;
  }

  @Override
  public final HeaderMenuItem text(String value) {
    text = Check.notNull(value, "value == null");
    return this;
  }

  final CarbonHeaderMenuItem dataOnClick0(Script.Action value) {
    onClick = value;
    return this;
  }

  final CarbonHeaderMenuItem sideNav() {
    sideNav = true;
    return this;
  }

  @Override
  public final void render() {
    tmpl.li(
        tmpl.a(
            !sideNav ? Carbon.HEADER_MENU_ITEM : Carbon.SIDE_NAV_HEADER_ITEM,

            !sideNav
                ? active ? Carbon.HEADER_MENU_ITEM_ACTIVE : Carbon.HEADER_MENU_ITEM_INACTIVE
                : active ? Carbon.SIDE_NAV_HEADER_ITEM_ACTIVE : Carbon.SIDE_NAV_HEADER_ITEM_INACTIVE,

            onClick != null ? tmpl.dataOnClick(onClick) : tmpl.noop(),

            href != null && onClick != null ? tmpl.dataOnClick(Script.location(href)) : tmpl.noop(),

            href != null ? tmpl.href(href) : tmpl.noop(),

            tmpl.tabindex("0"),

            text != null ? tmpl.span(text) : tmpl.noop()
        )
    );
  }

}
