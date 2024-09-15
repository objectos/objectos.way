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

import objectos.way.Web;
import testing.zite.TestingSiteInjector;

public final class CarbonWeb extends Web.Module {

  private final TestingSiteInjector injector;

  public CarbonWeb(TestingSiteInjector injector) {
    this.injector = injector;
  }

  @Override
  protected final void configure() {
    route("/ui/carbon.css", handler(injector.carbonHandler()));
    route("/ui/script.js", handler(injector.webResources()));

    Web.Store sessionStore;
    sessionStore = injector.sessionStore();

    filter(sessionStore::filter);

    route("/", handlerFactory(Index::new));
    route("/components", handlerFactory(Components::new));
    route("/components/button", handlerFactory(ComponentsButton::new));
    route("/components/data-table", handlerFactory(ComponentsDataTable::new));
    route("/components/form", handlerFactory(ComponentsForm::new));
    route("/components/grid", handlerFactory(ComponentsGrid::new));
    route("/components/link", handlerFactory(ComponentsLink::new));
    route("/components/page-header", handlerFactory(ComponentsPageHeader::new));
    route("/components/progress-indicator", handlerFactory(ComponentsProgressIndicator::new));
    route("/components/tearsheet", handlerFactory(ComponentsTearsheet::new));
    route("/components/tile", handlerFactory(ComponentsTile::new));
    route("/components/typography", handlerFactory(ComponentsTypography::new));
  }

}