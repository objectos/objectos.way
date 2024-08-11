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

abstract class CarbonPage extends Carbon.Template {

  TopSection topSection = TopSection.HOME;

  CarbonPage(Http.Exchange http) {
    super(http);
  }

  @Override
  protected final void render() throws Exception {
    carbon.shell(
        Carbon.THEME_WHITE,

        carbon.header(
            Carbon.THEME_G100,
            carbon.description("Objectos Carbon"),
            carbon.headerMenuButton(
                carbon.id("menu-button"),
                carbon.description("Open menu")
            ),
            carbon.headerCloseButton(
                carbon.id("close-button"),
                carbon.description("Close menu")
            ),
            carbon.headerName("Objectos", "Carbon", "/"),
            carbon.headerNavigation(
                carbon.dataFrame("header-nav", topSection.name()),
                carbon.description("Objectos Carbon navigation"),
                carbon.headerMenuItem("Components", "/components", topSection == TopSection.COMPONENTS),
                carbon.headerMenuItem("Gallery", "#", false)
            )
        ),

        carbon.sideNav(
            Carbon.THEME_G100,
            carbon.id("side-nav"),
            carbon.dataFrame("side-nav", getClass().getSimpleName()),
            carbon.description("Side navigation"),
            carbon.persistent(),
            carbon.sideNavMenuItem("Button", "/components/button", this::currentPage),
            carbon.sideNavMenuItem("Data table", "/components/data-table", this::currentPage),
            carbon.sideNavMenuItem("Grid", "/components/grid", this::currentPage),
            carbon.sideNavMenuItem("Link", "/components/link", this::currentPage),
            carbon.sideNavMenuItem("Page header", "/components/page-header", this::currentPage),
            carbon.sideNavMenuItem("Progress indicator", "/components/progress-indicator", this::currentPage),
            carbon.sideNavMenuItem("Tearsheet", "/components/tearsheet", this::currentPage),
            carbon.sideNavMenuItem("Typography", "/components/typography", this::currentPage)
        ),

        carbon.shellContent(
            carbon.dataFrame("main", getClass().getSimpleName()),
            carbon.content(this::renderContent)
        )
    );
  }

  protected abstract void renderHead();

  protected abstract void renderContent();

}
