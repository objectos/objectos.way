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
import objectos.way.Http;

abstract class CarbonPage extends Carbon.Shell {

  TopSection topSection = TopSection.HOME;

  CarbonPage(Http.Exchange http) {
    super(http);

    shellTheme(Carbon.G100);
  }

  @Override
  protected abstract void preRender();

  @Override
  protected final void renderUi() throws Exception {
    div(
        dataFrame("site-header", topSection.name()),

        ui(
            ui.header(
                Carbon.G100,

                ui.headerMenuButton(
                    ui.title("Open menu")
                ),

                ui.headerName(
                    ui.prefix("Objectos"),

                    ui.name("Carbon"),

                    ui.href("/")
                ),

                ui.headerNavigation(
                    menuItems(ui::headerMenuItem)
                )
            )
        ),

        ui(
            ui.sideNav(
                ui.sideNavItems(
                    menuItems(ui::sideNavMenuItem)
                )
            )
        )
    );

    ui(
        ui.content(
            this::renderContent
        )
    );
  }

  private Carbon.Element[] menuItems(Carbon.Element.Provider provider) {
    return new Carbon.Element[] {
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