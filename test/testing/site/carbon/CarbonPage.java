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
import objectos.way.Carbon.Icon;
import objectos.way.Carbon.IconSize;
import objectos.way.Html;
import objectos.way.Http;
import objectos.way.Script;

abstract class CarbonPage extends Carbon.Shell {

  TopSection topSection = TopSection.HOME;

  CarbonPage(Http.Exchange http) {
    super(http);

    shellTheme(Carbon.G10);
  }

  @Override
  protected abstract void preRender();

  private static final Html.Id _CLOSE_BUTTON = Html.id("close-button");

  private static final Html.Id _MENU_BUTTON = Html.id("menu-button");

  private static final Html.Id _OVERLAY = Html.id("overlay");

  private static final Html.Id _SIDE_NAV = Html.id("side-nav");

  static final Script.Action HIDE_MENU_ACTION = Script.actions(
      Script.addClass(_CLOSE_BUTTON, Carbon.HIDDEN),
      Script.removeClass(_MENU_BUTTON, Carbon.HIDDEN),
      Script.addClass(_OVERLAY, Carbon.HIDDEN, Carbon.OPACITY_0),
      Script.removeClass(_OVERLAY, Carbon.OPACITY_100),
      Script.addClass(_SIDE_NAV, Carbon.HIDDEN),
      Script.removeClass(_SIDE_NAV, Carbon.SIDE_NAV_WIDTH)
  );

  static final Script.Action SHOW_MENU_ACTION = Script.actions(
      Script.removeClass(_CLOSE_BUTTON, Carbon.HIDDEN),
      Script.addClass(_MENU_BUTTON, Carbon.HIDDEN),
      Script.removeClass(_OVERLAY, Carbon.HIDDEN, Carbon.OPACITY_0),
      Script.addClass(_OVERLAY, Carbon.OPACITY_100),
      Script.removeClass(_SIDE_NAV, Carbon.HIDDEN),
      Script.addClass(_SIDE_NAV, Carbon.SIDE_NAV_WIDTH)
  );

  private record HeaderMenuItem(String text, String href, boolean active) {}

  @Override
  protected final void renderUi() throws Exception {
    var headerItems = List.of(
        new HeaderMenuItem("Components", "/components", topSection == TopSection.COMPONENTS),

        new HeaderMenuItem("Gallery", "#", false)
    );

    header(
        Carbon.HEADER,
        ariaLabel("Objectos Carbon"),

        button(
            _MENU_BUTTON, Carbon.HEADER_MENU_BUTTON,
            ariaLabel("Open menu"), title("Open"), type("button"),
            dataOnClick(SHOW_MENU_ACTION),

            ui(ui.icon(Icon.MENU, IconSize.PX20))
        ),

        button(
            _CLOSE_BUTTON, Carbon.HEADER_CLOSE_BUTTON,
            className("hidden"),
            ariaLabel("Close menu"), title("Close"), type("button"),
            dataOnClick(HIDE_MENU_ACTION),

            ui(ui.icon(Icon.CLOSE, IconSize.PX20))
        ),

        a(
            Carbon.HEADER_NAME,
            dataOnClick(HIDE_MENU_ACTION),
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

    div(_OVERLAY, Carbon.OVERLAY, Carbon.HEADER_OFFSET);

    nav(
        _SIDE_NAV, Carbon.SIDE_NAV, Carbon.HEADER_OFFSET,
        ariaLabel("Side navigation"),
        dataFrame("side-nav", topSection.name()),
        tabindex("-1"),

        ul(
            Carbon.SIDE_NAV_ITEMS,

            ul(
                Carbon.SIDE_NAV_HEADER_LIST,

                f(this::sideNavHeaderItems, headerItems)
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

  private void sideNavHeaderItems(List<HeaderMenuItem> items) {
    for (var item : items) {
      li(
          a(
              Carbon.SIDE_NAV_HEADER_ITEM,
              item.active ? Carbon.SIDE_NAV_HEADER_ITEM_ACTIVE : Carbon.SIDE_NAV_HEADER_ITEM_INACTIVE,
              dataOnClick(HIDE_MENU_ACTION),
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