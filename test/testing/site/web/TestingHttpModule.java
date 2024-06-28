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
import objectos.way.Http.Exchange;
import objectos.way.Web;
import objectos.web.WebResources;
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
    sessionStore(injector.sessionStore());

    route("/ui/*", new UiHttpModule(injector));

    route("/login", factory(Login::new, injector));

    route("/common/*", this::common);

    route("/styles.css", new Styles(injector));

    filter(this::requireLogin);

    route("/", factory(Home::new, injector));
  }

  private void common(Exchange http) {
    WebResources webResources;
    webResources = injector.webResources();

    webResources.handle(http);
  }

  private void requireLogin(Exchange http) {
    Web.Session session;
    session = http.session();

    User user;
    user = session.get(User.class);

    if (user == null) {
      http.found("/login");
    }
  }

}