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
import objectos.way.Web;
import testing.zite.TestingSiteInjector;

public final class CarbonWeb extends Web.Module {

  public CarbonWeb(TestingSiteInjector injector) {
  }

  @Override
  protected final void configure() {
    Carbon carbon;
    carbon = Carbon.create(
        Carbon.classes(
            CarbonPage.class,
            Components.class,
            ComponentsButton.class,
            ComponentsGrid.class,
            ComponentsLink.class,
            ComponentsPageHeader.class,
            Index.class
        )
    );

    install(carbon.createHttpModule());

    route("/", GET(Index::new));
    route("/components", GET(Components::new));
    route("/components/button", GET(ComponentsButton::new));
    route("/components/grid", GET(ComponentsGrid::new));
    route("/components/link", GET(ComponentsLink::new));
    route("/components/page-header", GET(ComponentsPageHeader::new));
  }

}