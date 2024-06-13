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
import testing.zite.TestingSiteInjector;

final class Home extends WebTemplate implements Http.Handler {

  private ShellHeader header;

  Home(TestingSiteInjector injector) {
    title = "Home";
  }

  @Override
  public final void handle(Http.Exchange http) {
    http.methodMatrix(Http.GET, this::get);
  }

  // GET

  private void get(Http.Exchange http) {
    header = new ShellHeader(this);

    header.home();

    http.ok(this);
  }

  final void bodyImpl() {
    dataFrame("root", "shell");

    header.render();
  }

}