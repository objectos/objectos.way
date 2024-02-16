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

import objectos.html.HtmlTemplate;
import objectos.http.Method;
import objectos.http.ServerExchange;
import objectos.http.Session;
import objectos.ui.Ui;
import objectos.ui.UiPage;
import testing.site.auth.User;
import testing.zite.TestingSiteInjector;

final class Home extends HtmlTemplate {

  private final Ui ui;

  Home(TestingSiteInjector injector) {
    ui = injector.ui(this);
  }

  public final void handle(ServerExchange http) {
    http.methodMatrix(Method.GET, this::get);
  }

  private void get(ServerExchange http) {
    Session session;
    session = http.session();

    User user;
    user = session.get(User.class);

    if (user == null) {
      http.found("/login");
    } else {
      http.ok(this);
    }
  }

  @Override
  protected final void definition() {
    UiPage page;
    page = ui.page();

    page.title("Home");

    page.render(this::bodyImpl);
  }

  private void bodyImpl() {
    h1("Home");
  }

}