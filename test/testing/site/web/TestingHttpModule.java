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
package testing.site.web;

import objectos.way.Http;
import objectos.way.Web;
import testing.site.auth.User;
import testing.site.ui.UiHttpModule;
import testing.zite.TestingSiteInjector;

public class TestingHttpModule extends Http.Module {

  private final TestingSiteInjector injector;

  public TestingHttpModule(TestingSiteInjector injector) {
    this.injector = injector;
  }

  @Override
  protected final void configure() {
    install(new UiHttpModule(injector));

    route("/login", handlerFactory(Login::new, injector));

    route("/common/*", handler(injector.webResources()));

    route("/styles.css", handler(new Styles(injector)));

    Web.Store sessionStore;
    sessionStore = injector.sessionStore();

    filter(sessionStore::filter);

    filter(this::requireLogin);
  }

  private void requireLogin(Http.Exchange http) {
    Web.Session session;
    session = http.get(Web.Session.class);

    if (session == null) {
      return;
    }

    User user;
    user = session.get(User.class);

    if (user != null) {
      return;
    }

    http.found("/login");
  }

}