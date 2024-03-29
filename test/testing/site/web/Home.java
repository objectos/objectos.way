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
import objectos.http.Handler;
import objectos.http.Method;
import objectos.http.ServerExchange;
import objectos.ui.Ui;
import objectos.ui.UiPage;
import testing.zite.TestingSiteInjector;

final class Home extends HtmlTemplate implements Handler {

  private final TestingSiteInjector injector;

  private ShellHeader header;

  Home(TestingSiteInjector injector) {
    this.injector = injector;
  }

  @Override
  public final void handle(ServerExchange http) {
    http.methodMatrix(Method.GET, this::get);
  }

  // GET

  private void get(ServerExchange http) {
    header = new ShellHeader(this);

    header.home();

    http.ok(this);
  }

  @Override
  protected final void definition() {
    Ui ui;
    ui = injector.ui(this);

    UiPage page;
    page = ui.page();

    page.title("Home");

    page.render(this::bodyImpl);
  }

  private void bodyImpl() {
    dataFrame("root");
    dataFrameValue("shell");

    header.render();
  }

}