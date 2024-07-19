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
import objectos.way.Carbon.Component.Header.MenuItem;

final class CarbonHeaderMenuItem implements Carbon.Component.Header.MenuItem {

  private final Html.Template tmpl;

  private boolean active;

  private String href;

  private String text;

  CarbonHeaderMenuItem(Html.Template tmpl) {
    this.tmpl = tmpl;
  }

  @Override
  public final MenuItem active(boolean value) {
    active = value;
    return this;
  }

  @Override
  public final MenuItem href(String value) {
    href = Check.notNull(value, "value == null");
    return this;
  }

  @Override
  public final MenuItem text(String value) {
    text = Check.notNull(value, "value == null");
    return this;
  }

  @Override
  public final Html.ElementInstruction render() {
    return tmpl.li(
        tmpl.a(
            Carbon.HEADER_MENU_ITEM,

            active ? Carbon.HEADER_MENU_ITEM_ACTIVE : Carbon.HEADER_MENU_ITEM_INACTIVE,

            href != null ? tmpl.href(href) : tmpl.noop(),

            tmpl.tabindex("0"),

            text != null ? tmpl.span(text) : tmpl.noop()
        )
    );
  }

}
