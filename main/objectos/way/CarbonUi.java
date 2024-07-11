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
import objectos.util.list.GrowableList;
import objectos.way.Carbon.Header;
import objectos.way.Carbon.HeaderMenuItem;
import objectos.way.Carbon.HeaderName;
import objectos.way.Carbon.HeaderNameText;
import objectos.way.Carbon.HeaderNavigation;
import objectos.way.Carbon.Href;
import objectos.way.Carbon.IsActive;
import objectos.way.Carbon.Name;

final class CarbonUi implements Carbon.Ui {

  private final Html.Template tmpl;

  CarbonUi(Html.Template tmpl) {
    this.tmpl = tmpl;
  }

  // attributes

  @Override
  public final Href href(String value) {
    return new Href(value);
  }

  @Override
  public final IsActive isActive(boolean value) {
    return new IsActive(value);
  }

  @Override
  public final Name name(String value) {
    return new Name(value);
  }

  //
  // Header
  //

  static final class CarbonHeader implements Header {
    HeaderName headerName;

    CarbonHeaderNavigation headerNavigation;

    CarbonTheme theme;

    CarbonHeader(Header.Component[] components) {
      for (Header.Component c : components) { // implicit null check
        switch (c) {
          case HeaderName o -> headerName = o;

          case CarbonHeaderNavigation o -> headerNavigation = o;

          case CarbonTheme o -> theme = o;
        }
      }
    }
  }

  @Override
  public final Html.ElementInstruction header(Header.Component... components) {
    CarbonHeader pojo;
    pojo = new CarbonHeader(components);

    return tmpl.header(
        tmpl.className("fixed inset-0px flex h-48px"),
        tmpl.className("border-b border-subtle"),
        tmpl.className("bg-background"),

        pojo.theme != null ? tmpl.className(pojo.theme.className) : tmpl.noop(),

        pojo.headerName != null ? headerName(pojo.headerName) : tmpl.noop()
    );
  }

  //
  // Header: HeaderMenuItem
  //

  static final class CarbonHeaderMenuItem implements HeaderMenuItem {
    CarbonHeaderMenuItem(HeaderMenuItem.Component[] components) {

    }
  }

  @Override
  public final HeaderMenuItem headerMenuItem(HeaderMenuItem.Component... components) {
    return new CarbonHeaderMenuItem(components);
  }

  //
  // Header: HeaderName
  //

  @Override
  public final HeaderName headerName(HeaderName.Component... components) {
    return new HeaderName(components);
  }

  private Html.ElementInstruction headerName(HeaderName pojo) {
    return tmpl.a(
        tmpl.className("flex h-full select-none items-center"),
        tmpl.className("border-2 border-transparent"),
        tmpl.className("pr-32px pl-16px"),
        tmpl.className("text-body-compact-01 font-semibold"),
        tmpl.className("outline-none"),
        tmpl.className("transition-colors duration-100"),
        tmpl.className("focus:border-focus"),

        pojo.href != null ? tmpl.href(pojo.href) : tmpl.noop(),
        pojo.text != null
            ? tmpl.flatten(
                tmpl.span(
                    tmpl.className("font-normal"),

                    tmpl.t(pojo.text.prefix)
                ),
                tmpl.raw("&nbsp;"),
                tmpl.t(pojo.text.text)
            )
            : tmpl.noop()
    );
  }

  @Override
  public final HeaderNameText headerNameText(String prefix, String text) {
    Check.notNull(prefix, "prefix == null");
    Check.notNull(text, "text == null");

    return new HeaderNameText(prefix, text);
  }

  //
  // Header: HeaderNavigation
  //

  static final class CarbonHeaderNavigation implements Carbon.HeaderNavigation {
    GrowableList<CarbonHeaderMenuItem> items;

    CarbonHeaderNavigation(HeaderNavigation.Component[] components) {
      for (HeaderNavigation.Component c : components) {
        switch (c) {
          case CarbonHeaderMenuItem o -> addItem(o);
        }
      }
    }

    private void addItem(CarbonHeaderMenuItem item) {
      if (items == null) {
        items = new GrowableList<>();
      }

      items.add(item);
    }
  }

  @Override
  public final HeaderNavigation headerNavigation(HeaderNavigation.Component... components) {
    return new CarbonHeaderNavigation(components);
  }

}