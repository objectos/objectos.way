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
package testing.site.carbonated;

import objectos.way.Carbonated;
import objectos.way.Http;
import objectos.way.Ui;
import objectos.way.Web;
import testing.zite.TestingSiteInjector;

public final class CarbonatedWeb extends Web.Module {

  private final Carbonated carbonated;

  public CarbonatedWeb(TestingSiteInjector injector) {
    carbonated = injector.carbonated();
  }

  @Override
  protected final void configure() {
    install(carbonated.createHttpModule());

    filter(this::carbonated);

    route("/carbonated", GET(Index::new));
  }

  private void carbonated(Http.Exchange http) {
    http.set(Ui.Binder.class, carbonated);
  }

}