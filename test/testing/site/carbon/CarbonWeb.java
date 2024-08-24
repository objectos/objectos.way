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
import objectos.way.Web;
import testing.zite.TestingSiteInjector;

public final class CarbonWeb extends Web.Module {

  public CarbonWeb(TestingSiteInjector injector) {
  }

  @Override
  protected final void configure() {
    Http.Module carbon;
    carbon = Carbon.createHttpModule(
        Carbon.classes(
            CarbonPage.class,
            Components.class,
            ComponentsButton.class,
            ComponentsDataTable.class,
            ComponentsGrid.class,
            ComponentsLink.class,
            ComponentsPageHeader.class,
            ComponentsProgressIndicator.class,
            ComponentsTearsheet.class,
            ComponentsTile.class,
            ComponentsTypography.class,
            Index.class
        )
    );

    install(carbon);

    route("/", GET(Index::new));
    route("/components", GET(Components::new));
    route("/components/button", GET(ComponentsButton::new));
    route("/components/data-table", GET(ComponentsDataTable::new));
    route("/components/grid", GET(ComponentsGrid::new));
    route("/components/link", GET(ComponentsLink::new));
    route("/components/page-header", GET(ComponentsPageHeader::new));
    route("/components/progress-indicator", GET(ComponentsProgressIndicator::new));
    route("/components/tearsheet", GET(ComponentsTearsheet::new));
    route("/components/tile", GET(ComponentsTile::new));
    route("/components/typography", GET(ComponentsTypography::new));
  }

}