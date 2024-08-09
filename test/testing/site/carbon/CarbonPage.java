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

abstract class CarbonPage extends Carbon.Template {

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
  protected final void render() throws Exception {
    doctype();

    html(
        className("theme-white"),

        head(
            f(this::renderStandardHead),
            f(this::renderHead)
        ),

        body(
            f(this::renderBody)
        )
    );
  }

  protected abstract void renderHead();

  @SuppressWarnings("unused")
  private void renderBody() {
    final Html.Id closeButton;
    closeButton = Html.id("close-button");

    final Html.Id menuButton;
    menuButton = Html.id("menu-button");

    final Html.Id overlay;
    overlay = Html.id("overlay");

    final Html.Id sideNav;
    sideNav = Html.id("side-nav");

    final Script.Action openMenuAction = Script.actions(
        Script.replaceClass(closeButton, "header-close-button", "header-close-button-toggle"),
        Script.replaceClass(menuButton, "header-menu-button", "header-menu-button-toggle"),
        Script.replaceClass(overlay, "overlay", "overlay-toggle"),
        Script.replaceClass(sideNav, "side-nav", "side-nav-toggle")
    );

    final Script.Action closeMenuAction = Script.actions(
        Script.replaceClass(closeButton, "header-close-button", "header-close-button-toggle", true),
        Script.replaceClass(menuButton, "header-menu-button", "header-menu-button-toggle", true),
        Script.replaceClass(overlay, "overlay", "overlay-toggle", true),
        Script.replaceClass(sideNav, "side-nav", "side-nav-toggle", true)
    );

    List<Carbon.Header.MenuItem> headerMenuItems = List.of(
        carbon.headerMenuItem("Components", "/components", topSection == TopSection.COMPONENTS),

        carbon.headerMenuItem("Gallery", "#", false)
    );

    carbon.header()
        .theme(Carbon.THEME_G100)
        .description("Objectos Carbon")
        .menuButton(
            carbon.headerMenuButton().description("Open menu")
        )
        .closeButton(
            carbon.headerCloseButton().description("Close menu")
        )
        .name(
            carbon.headerName()
                .prefix("Objectos")
                .text("Carbon")
                .href("/")
        )
        .navigation(
            carbon.headerNavigation()
                .dataFrame("header-nav", topSection.name())
                .description("Objectos Carbon navigation")
                .addItems(headerMenuItems)
        )
        .render();

    /*
    header(
        className("theme-g100 header"),
    
        ariaLabel("Objectos Carbon"),
    
        button(
            menuButton, className("header-menu-button"),
    
            ariaLabel("Open menu"), title("Open"), type("button"),
    
            dataOnClick(openMenuAction),
    
            icon20(Carbon.Icon.MENU, ariaHidden("true"))
        ),
    
        button(
            closeButton, className("header-close-button"),
    
            ariaLabel("Close menu"), title("Close"), type("button"),
    
            dataOnClick(closeMenuAction),
    
            icon20(Carbon.Icon.CLOSE, ariaHidden("true"))
        ),
    
        a(
            className("header-name"),
    
            dataOnClick(closeMenuAction),
            dataOnClick(Script.location("/")),
    
            href("/"),
    
            span("Objectos"), nbsp(), t("Carbon")
        ),
    
        nav(
            className("header-nav"),
    
            ariaLabel("Objectos Carbon navigation"),
    
            dataFrame("header-nav", topSection.name()),
    
            ul(
                className("header-nav-list"),
    
                f(this::renderHeaderNavItems)
            )
        )
    );
    
    div(overlay, className("overlay header-offset"));
    
    nav(
        sideNav, className("theme-g100 side-nav side-nav-persistent header-offset"),
    
        ariaLabel("Side navigation"),
    
        tabindex("-1"),
    
        ul(
            className("side-nav-list"),
    
            dataFrame("side-nav", getClass().getSimpleName()),
    
            ul(
                className("side-nav-header-list"),
    
                f(this::renderSideNavHeaderItems, closeMenuAction)
            ),
    
            f(this::renderSideNavItems, closeMenuAction)
        )
    );
    */

    main(
        dataFrame("main", getClass().getSimpleName()),

        className("header-offset side-nav-offset"),

        f(this::renderContent)
    );
  }

  private void renderHeaderNavItems() {
    for (MenuItem item : HEADER_MENU) {
      li(
          a(
              currentPageStartsWith(item.href)
                  ? className("header-nav-link-active")
                  : className("header-nav-link-inactive"),

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
              currentPage(item.href)
                  ? className("side-nav-header-link-active")
                  : className("side-nav-header-link-inactive"),

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
        renderSideNavLink("Data table", "/components/data-table", clickAction);
        renderSideNavLink("Grid", "/components/grid", clickAction);
        renderSideNavLink("Link", "/components/link", clickAction);
        renderSideNavLink("Page header", "/components/page-header", clickAction);
        renderSideNavLink("Progress indicator", "/components/progress-indicator", clickAction);
        renderSideNavLink("Tearsheet", "/components/tearsheet", clickAction);
      }

      default -> {}
    }
  }

  private void renderSideNavLink(String title, String href, Script.Action clickAction) {
    li(
        className("side-nav-item"),

        a(
            currentPage(href)
                ? className("side-nav-link-active")
                : className("side-nav-link-inactive"),

            dataOnClick(clickAction),

            dataOnClick(Script.location(href)),

            href(href),

            span(title)
        )
    );
  }

  protected abstract void renderContent();

}
