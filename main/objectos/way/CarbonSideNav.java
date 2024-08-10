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

import java.util.List;
import objectos.util.list.GrowableList;
import objectos.way.Carbon.CarbonDataFrame;
import objectos.way.Carbon.CarbonDescription;
import objectos.way.Carbon.CarbonId;
import objectos.way.Carbon.CarbonPersistent;

final class CarbonSideNav implements Carbon.SideNav {

  private CarbonDataFrame dataFrame;

  private String description;

  private boolean expressive;

  private CarbonHeader header;

  private final Html.Id idBody;

  private final Html.Id idOverlay;

  private final List<CarbonSideNavMenuItem> items = new GrowableList<>();

  private boolean persistent;

  private Carbon.Theme theme;

  private final Html.TemplateBase tmpl;

  CarbonSideNav(Html.TemplateBase tmpl, Carbon.SideNav.Value[] values) {
    this.tmpl = tmpl;

    Html.Id id = null;

    for (var value : values) {
      switch (value) {
        case CarbonDataFrame o -> dataFrame = o;

        case CarbonDescription o -> description = o.value();

        case CarbonId o -> id = o.value();

        case CarbonSideNavMenuItem o -> items.add(o);

        case CarbonPersistent o -> persistent = o.value();

        case Carbon.Theme o -> theme = o;
      }
    }

    this.idOverlay = id != null ? id : tmpl.nextId();

    this.idBody = id != null ? Html.id(id.value() + "-body") : tmpl.nextId();
  }

  static final Html.ClassName OVERLAY = Html.className("""
  invisible fixed inset-0px z-overlay
  bg-overlay
  opacity-0
  transition-opacity duration-300
  """);

  static final Html.ClassName SIDE_NAV = Html.className("""
  absolute top-0px bottom-0px left-0px z-header
  w-0px
  bg-background
  text-text-secondary
  transition-all duration-100
  """);

  static final Html.ClassName SIDE_NAV_LIST = Html.className("""
  flex-1 pt-16px
  """);

  static final Html.ClassName SIDE_NAV_HEADER_LIST = Html.className("""
  mb-32px
  lg:hidden
  """);

  @Override
  public final Html.ElementInstruction render() {
    return tmpl.div(
        idOverlay,

        OVERLAY,

        persistent ? tmpl.className("lg:visible lg:right-auto lg:w-side-nav lg:opacity-100") : tmpl.className("lg:hidden"),

        theme != null ? theme : tmpl.noop(),

        tmpl.nav(
            idBody,

            SIDE_NAV,

            header != null ? tmpl.className("mt-header") : tmpl.noop(),

            persistent ? tmpl.className("lg:w-side-nav") : tmpl.className("lg:hidden"),

            dataFrame != null ? dataFrame.render(tmpl) : tmpl.noop(),

            description != null ? tmpl.ariaLabel(description) : tmpl.noop(),

            tmpl.ul(
                SIDE_NAV_LIST,

                tmpl.f(this::renderNav)
            )
        )
    );
  }

  private void renderNav() {
    Script.Action closeButtonAction = null;

    if (header != null) {
      closeButtonAction = header.closeButtonAction();

      List<CarbonHeaderMenuItem> headerItems;
      headerItems = header.navigationItems();

      if (headerItems != null) {
        tmpl.ul(
            SIDE_NAV_HEADER_LIST,

            tmpl.f(this::renderHeaderItems, closeButtonAction, headerItems)
        );
      }
    }

    for (var item : items) {
      item.render(closeButtonAction);
    }
  }

  private void renderHeaderItems(Script.Action closeButtonAction, List<CarbonHeaderMenuItem> headerItems) {
    for (var item : headerItems) {
      item.renderSideNav(closeButtonAction, expressive);
    }
  }

  final void accept(CarbonHeader header) {
    this.header = header;
  }

  final Script.Action hideAction() {
    return Script.actions(
        Script.replaceClass(idOverlay, "opacity-0", "opacity-100", true),
        Script.delay(300, Script.replaceClass(idOverlay, "invisible", "visible", true)),
        Script.replaceClass(idBody, "w-0px", "w-side-nav", true)
    );
  }

  final Script.Action showAction() {
    return Script.actions(
        Script.replaceClass(idOverlay, "invisible", "visible"),
        Script.replaceClass(idOverlay, "opacity-0", "opacity-100"),
        Script.replaceClass(idBody, "w-0px", "w-side-nav")
    );
  }

}