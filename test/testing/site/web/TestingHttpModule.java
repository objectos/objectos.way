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
import objectos.way.Session;
import objectos.way.Session.Repository;
import testing.site.auth.User;
import testing.site.carbon.CarbonWeb;
import testing.site.ui.UiHttpModule;
import testing.zite.TestingSiteInjector;

public class TestingHttpModule extends Http.Module {

  private final TestingSiteInjector injector;

  public TestingHttpModule(TestingSiteInjector injector) {
    this.injector = injector;
  }

  @Override
  protected final void configure() {
    host("dev.carbon:8003", new CarbonWeb(injector));

    route("/ui/*", new UiHttpModule(injector));

    route("/login", factory(Login::new, injector));

    route("/common/*", injector.webResources());

    route("/styles.css", new Styles(injector));

    Repository sessionStore;
    sessionStore = injector.sessionStore();

    filter(sessionStore::filter);

    filter(this::requireLogin);

    route("/", factory(Home::new, injector));
  }

  private void requireLogin(Http.Exchange http) {
    Session.Instance session;
    session = http.get(Session.Instance.class);

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