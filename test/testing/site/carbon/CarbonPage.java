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
package testing.site.carbon;

import java.util.List;
import objectos.way.Carbon;
import objectos.way.Html;
import objectos.way.Http;
import objectos.way.Script;

abstract class CarbonPage extends Carbon.Shell {

  TopSection topSection = TopSection.HOME;

  CarbonPage(Http.Exchange http) {
    super(http);

    shellTheme(Carbon.G100);
  }

  @Override
  protected abstract void preRender();

  private record HeaderMenuItem(String text, String href, boolean active) {}

  @Override
  protected final void renderUi() throws Exception {
    final Html.Id closeButton;
    closeButton = Html.id("close-button");

    final Html.Id menuButton;
    menuButton = Html.id("menu-button");

    final Html.Id overlay;
    overlay = Html.id("overlay");

    final Html.Id sideNav;
    sideNav = Html.id("side-nav");

    final Script.Action closeMenuAction = Script.actions(
        Script.addClass(closeButton, Carbon.HIDDEN),
        Script.removeClass(menuButton, Carbon.HIDDEN),
        Script.addClass(overlay, Carbon.HIDDEN, Carbon.OPACITY_0),
        Script.removeClass(overlay, Carbon.OPACITY_100),
        Script.addClass(sideNav, Carbon.HIDDEN),
        Script.removeClass(sideNav, Carbon.SIDE_NAV_WIDTH)
    );

    final Script.Action openMenuAction = Script.actions(
        Script.removeClass(closeButton, Carbon.HIDDEN),
        Script.addClass(menuButton, Carbon.HIDDEN),
        Script.removeClass(overlay, Carbon.HIDDEN, Carbon.OPACITY_0),
        Script.addClass(overlay, Carbon.OPACITY_100),
        Script.removeClass(sideNav, Carbon.HIDDEN),
        Script.addClass(sideNav, Carbon.SIDE_NAV_WIDTH)
    );

    var headerItems = List.of(
        new HeaderMenuItem("Components", "/components", topSection == TopSection.COMPONENTS),

        new HeaderMenuItem("Gallery", "#", false)
    );

    header(
        Carbon.HEADER,

        ariaLabel("Objectos Carbon"),

        button(
            menuButton, Carbon.HEADER_MENU_BUTTON,

            ariaLabel("Open menu"), title("Open"), type("button"),

            dataOnClick(openMenuAction),

            icon20(Carbon.Icon.MENU)
        ),

        button(
            closeButton, Carbon.HEADER_CLOSE_BUTTON,

            ariaLabel("Close menu"), title("Close"), type("button"),

            dataOnClick(closeMenuAction),

            icon20(Carbon.Icon.CLOSE)
        ),

        a(
            Carbon.HEADER_NAME,

            dataOnClick(closeMenuAction),

            dataOnClick(Script.location("/")),

            href("/"),

            span("Objectos"), nbsp(), t("Carbon")
        ),

        nav(
            Carbon.HEADER_NAV,

            ariaLabel("Objectos Carbon navigation"),

            dataFrame("header-nav", topSection.name()),

            ul(
                Carbon.HEADER_NAV_LIST,

                f(this::headerMenuItems, headerItems)
            )
        )
    );

    div(overlay, Carbon.OVERLAY, Carbon.HEADER_OFFSET);

    nav(
        sideNav, Carbon.SIDE_NAV, Carbon.HEADER_OFFSET,

        ariaLabel("Side navigation"),

        dataFrame("side-nav", topSection.name()),

        tabindex("-1"),

        ul(
            Carbon.SIDE_NAV_ITEMS,

            ul(
                Carbon.SIDE_NAV_HEADER_LIST,

                f(this::sideNavHeaderItems, headerItems, closeMenuAction)
            )
        )
    );
  }

  private void headerMenuItems(List<HeaderMenuItem> items) {
    for (var item : items) {
      li(
          a(
              Carbon.HEADER_MENU_ITEM,

              item.active ? Carbon.HEADER_MENU_ITEM_ACTIVE : Carbon.HEADER_MENU_ITEM_INACTIVE,

              href(item.href),

              tabindex("0"),

              span(item.text)
          )
      );
    }
  }

  private void sideNavHeaderItems(List<HeaderMenuItem> items, Script.Action closeAction) {
    for (var item : items) {
      li(
          a(
              Carbon.SIDE_NAV_HEADER_ITEM,

              item.active ? Carbon.SIDE_NAV_HEADER_ITEM_ACTIVE : Carbon.SIDE_NAV_HEADER_ITEM_INACTIVE,

              dataOnClick(closeAction),

              dataOnClick(Script.location(item.href)),

              href(item.href),

              tabindex("0"),

              span(item.text)
          )
      );
    }
  }

  protected abstract void renderContent();

}