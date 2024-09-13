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
import objectos.way.Css;
import objectos.way.Html;
import objectos.way.Script;

@Css.Source
abstract class CarbonPage extends Carbon.Template {

  TopSection topSection = TopSection.HOME;

  @Override
  protected final void render() throws Exception {
    doctype();

    html(
        Carbon.THEME_WHITE,

        head(
            f(this::renderStandardHead),
            f(this::renderHead)
        ),

        body(
            f(this::renderBody)
        )
    );
  }

  private static final Html.Id menuButton = Html.id("menu-button");

  private static final Html.Id closeButton = Html.id("close-button");

  private static final Html.Id sideNav = Html.id("side-nav");

  private static final Html.Id sideNavBody = Html.id("side-nav-body");

  private static final Script.Action openMenu = Script.actions(
      Carbon.hideHeaderButton(menuButton),
      Carbon.showHeaderButton(closeButton),
      Carbon.showSideNav(sideNav),
      Carbon.showSideNavBody(sideNavBody)
  );

  private static final Script.Action closeMenu = Script.actions(
      Carbon.hideHeaderButton(closeButton),
      Carbon.showHeaderButton(menuButton),
      Carbon.hideSideNavBody(sideNavBody),
      Carbon.hideSideNav(sideNav)
  );

  private void renderBody() {
    List<Carbon.MenuElement> headerMenuItems = List.of(
        Carbon.menuLink("Components", "/components", topSection == TopSection.COMPONENTS, closeMenu),
        Carbon.menuLink("Gallery", "#", false, closeMenu)
    );

    List<Carbon.MenuElement> sideNavItems = List.of(
        Carbon.menuLink("Button", "/components/button", this::currentPage, closeMenu),
        Carbon.menuLink("Data table", "/components/data-table", this::currentPage, closeMenu),
        Carbon.menuLink("Form", "/components/form", this::currentPage, closeMenu),
        Carbon.menuLink("Grid", "/components/grid", this::currentPage, closeMenu),
        Carbon.menuLink("Link", "/components/link", this::currentPage, closeMenu),
        Carbon.menuLink("Page header", "/components/page-header", this::currentPage, closeMenu),
        Carbon.menuLink("Progress indicator", "/components/progress-indicator", this::currentPage, closeMenu),
        Carbon.menuLink("Tearsheet", "/components/tearsheet", this::currentPage, closeMenu),
        Carbon.menuLink("Tile", "/components/tile", this::currentPage, closeMenu),
        Carbon.menuLink("Typography", "/components/typography", this::currentPage, closeMenu)
    );

    carbon.header(
        Carbon.THEME_G100, ariaLabel("Objectos Carbon"),

        carbon.headerMenuButton(
            menuButton, ariaLabel("Open menu"), title("Open"), dataOnClick(openMenu)
        ),

        carbon.headerCloseButton(
            closeButton, ariaLabel("Close menu"), title("Close"), dataOnClick(closeMenu)
        ),

        carbon.headerName("Objectos", "Carbon", "/", closeMenu),

        carbon.headerNavigation(
            dataFrame("header-nav", topSection.name()), ariaLabel("Objectos Carbon navigation"),
            carbon.headerNavigationItems(headerMenuItems)
        )
    );

    carbon.sideNav(
        Carbon.THEME_G100, Carbon.SIDE_NAV_PERSISTENT, sideNav,

        carbon.sideNavBody(
            Carbon.HEADER_OFFSET, Carbon.SIDE_NAV_BODY_PERSISTENT, sideNavBody,
            dataFrame("side-nav", getClass().getSimpleName()), ariaLabel("Side navigation"),

            carbon.sideNavHeaderItems(headerMenuItems),

            carbon.sideNavItems(sideNavItems)
        )
    );

    main(
        dataFrame("main", getClass().getSimpleName()),

        Carbon.HEADER_OFFSET, Carbon.SIDE_NAV_OFFSET,
        className("flex flex-col gap-y-spacing-05"),

        f(this::renderContent)
    );
  }

  protected abstract void renderHead();

  protected abstract void renderContent();

}
