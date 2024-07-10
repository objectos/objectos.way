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

import objectos.way.Http;
import objectos.way.Ui;
import objectos.way.Web;
import testing.zite.TestingSiteInjector;

public final class CarbonWeb extends Web.Module {

  private final Ui.Binder carbon;

  public CarbonWeb(TestingSiteInjector injector) {
    carbon = injector.carbon();
  }

  @Override
  protected final void configure() {
    install(carbon.createHttpModule());

    filter(this::carbon);

    route("/carbon", GET(Index::new));
  }

  private void carbon(Http.Exchange http) {
    http.set(Ui.Binder.class, carbon);
  }

}