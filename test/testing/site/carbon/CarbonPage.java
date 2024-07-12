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

        ui.header(
            Carbon.G100,

            ui.headerName(
                ui.headerNameText("Objectos", "Carbon"),

                ui.href("/")
            ),

            ui.headerNavigation(
                ui.headerMenuItem(
                    ui.name("Components"),

                    ui.href("/components"),

                    ui.isActive(topSection == TopSection.COMPONENTS)
                ),

                ui.headerMenuItem(
                    ui.name("Gallery"),

                    ui.href("#"),

                    ui.isActive(false)
                )
            )
        )
    );

    ui.content(
        this::renderContent
    );
  }

  protected abstract void renderContent();

}