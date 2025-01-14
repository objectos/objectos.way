/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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
package objectos.way.www.site;

import objectos.way.Http;
import objectos.way.Web;
import objectos.way.www.Injector;
import objectos.way.www.Stage;

public final class SiteModule extends Http.Module {

  private final Stage stage;

  private final Web.Resources webResources;

  public SiteModule(Injector injector) {
    stage = injector.stage();

    webResources = injector.webResources();
  }

  @Override
  protected final void configure() {
    route("/objectos.way", handlerFactory(Root::new));

    switch (stage) {
      case DEVELOPMENT -> {
        route("/ui/script.js", handler(webResources));
      }
    }
  }

}