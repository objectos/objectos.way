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

  private final Web.Store sessionStore;

  public CarbonWeb(TestingSiteInjector injector) {
    sessionStore = injector.sessionStore();
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

    filter(sessionStore::filter);

    route("/", f(Index::new));
    route("/components", f(Components::new));
    route("/components/button", f(ComponentsButton::new));
    route("/components/data-table", f(ComponentsDataTable::new));
    route("/components/grid", f(ComponentsGrid::new));
    route("/components/link", f(ComponentsLink::new));
    route("/components/page-header", f(ComponentsPageHeader::new));
    route("/components/progress-indicator", f(ComponentsProgressIndicator::new));
    route("/components/tearsheet", f(ComponentsTearsheet::new));
    route("/components/tile", f(ComponentsTile::new));
    route("/components/typography", f(ComponentsTypography::new));
  }

}