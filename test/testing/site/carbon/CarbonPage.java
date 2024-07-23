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

  private record MenuItem(String text, String href) {}

  private static final List<MenuItem> HEADER_MENU = List.of(
      new MenuItem("Components", "/components"),

      new MenuItem("Gallery", "#")
  );

  TopSection topSection = TopSection.HOME;

  protected CarbonPage(Http.Exchange http) {
    super(http);
  }

  @Override
  protected final void renderShell() {
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
        Script.removeClass(sideNav, Carbon.VISIBLE, Carbon.SIDE_NAV_WIDTH)
    );

    final Script.Action openMenuAction = Script.actions(
        Script.removeClass(closeButton, Carbon.HIDDEN),
        Script.addClass(menuButton, Carbon.HIDDEN),
        Script.removeClass(overlay, Carbon.HIDDEN, Carbon.OPACITY_0),
        Script.addClass(overlay, Carbon.OPACITY_100),
        Script.addClass(sideNav, Carbon.VISIBLE, Carbon.SIDE_NAV_WIDTH)
    );

    header(
        Carbon.G100, Carbon.HEADER,

        ariaLabel("Objectos Carbon"),

        button(
            menuButton, Carbon.HEADER_MENU_BUTTON,

            ariaLabel("Open menu"), title("Open"), type("button"),

            dataOnClick(openMenuAction),

            icon20(Carbon.Icon.MENU, ariaHidden("true"))
        ),

        button(
            closeButton, Carbon.HEADER_CLOSE_BUTTON,

            ariaLabel("Close menu"), title("Close"), type("button"),

            dataOnClick(closeMenuAction),

            icon20(Carbon.Icon.CLOSE, ariaHidden("true"))
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

                f(this::renderHeaderNavItems)
            )
        )
    );

    div(overlay, Carbon.OVERLAY, Carbon.HEADER_OFFSET);

    nav(
        sideNav, Carbon.G100, Carbon.SIDE_NAV, Carbon.SIDE_NAV_PERSISTENT, Carbon.HEADER_OFFSET,

        ariaLabel("Side navigation"),

        tabindex("-1"),

        ul(
            Carbon.SIDE_NAV_LIST,

            dataFrame("side-nav", getClass().getSimpleName()),

            ul(
                Carbon.SIDE_NAV_HEADER_LIST,

                f(this::renderSideNavHeaderItems, closeMenuAction)
            ),

            f(this::renderSideNavItems, closeMenuAction)
        )
    );

    main(
        Carbon.HEADER_OFFSET,

        dataFrame("main", getClass().getSimpleName()),

        f(this::renderContent)
    );
  }

  private void renderHeaderNavItems() {
    for (MenuItem item : HEADER_MENU) {
      li(
          a(
              Carbon.HEADER_NAV_LINK,

              currentPage(item.href)
                  ? Carbon.HEADER_NAV_LINK_ACTIVE
                  : Carbon.HEADER_NAV_LINK_INACTIVE,

              href(item.href),

              tabindex("0"),

              span(item.text)
          )
      );
    }
  }

  private void renderSideNavHeaderItems(Script.Action clickAction) {
    for (MenuItem item : HEADER_MENU) {
      li(
          a(
              Carbon.SIDE_NAV_HEADER_LINK,

              currentPage(item.href)
                  ? Carbon.SIDE_NAV_HEADER_LINK_ACTIVE
                  : Carbon.SIDE_NAV_HEADER_LINK_INACTIVE,

              dataOnClick(clickAction),

              dataOnClick(Script.location(item.href)),

              href(item.href),

              tabindex("0"),

              span(item.text)
          )
      );
    }
  }

  private void renderSideNavItems(Script.Action clickAction) {
    switch (topSection) {
      case COMPONENTS -> {
        renderSideNavLink("Button", "/components/button", clickAction);
      }

      default -> {}
    }
  }

  private void renderSideNavLink(String title, String href, Script.Action clickAction) {
    li(
        Carbon.SIDE_NAV_ITEM,

        a(
            Carbon.SIDE_NAV_LINK,

            currentPage(href)
                ? Carbon.SIDE_NAV_LINK_ACTIVE
                : Carbon.SIDE_NAV_LINK_INACTIVE,

            dataOnClick(clickAction),

            dataOnClick(Script.location(href)),

            href(href),

            span(title)
        )
    );
  }

  protected abstract void renderContent();

}
