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

import objectos.way.Carbon;
import objectos.way.Html;
import objectos.way.Http;

abstract class CarbonPage extends Carbon.Shell {

  TopSection topSection = TopSection.HOME;

  CarbonPage(Http.Exchange http) {
    super(http);

    shellTheme(Carbon.G10);
  }

  @Override
  protected abstract void preRender();

  @Override
  protected final void renderUi() throws Exception {
    Html.Id overlay;
    overlay = Html.id("overlay");

    Html.Id sideNav;
    sideNav = Html.id("side-nav");

    ui(
        ui.header(
            ui.ariaLabel("Objectos Carbon"),

            ui.headerMenuButton(
                ui.ariaLabel("Open menu"),

                ui.title("Open")
            ),

            ui.headerName(
                ui.prefix("Objectos"),

                ui.name("Carbon"),

                ui.href("/")
            ),

            ui.headerNavigation(
                menuItems("header-nav", ui::headerMenuItem)
            )
        ),

        ui.overlay(
            ui.id(overlay)
        ),

        ui.sideNav(
            ui.id(sideNav),

            ui.sideNavItems(
                menuItems("side-nav-items", ui::sideNavMenuItem)
            )
        ),

        ui.content(
            this::renderContent
        )
    );
  }

  private Carbon.Component[] menuItems(String frameName, Carbon.Element.Provider provider) {
    return new Carbon.Component[] {
        ui.dataFrame(frameName, topSection.name()),

        provider.get(
            ui.name("Components"),

            ui.href("/components"),

            ui.isActive(topSection == TopSection.COMPONENTS)
        ),

        provider.get(
            ui.name("Gallery"),

            ui.href("#"),

            ui.isActive(false)
        )
    };
  }

  protected abstract void renderContent();

}