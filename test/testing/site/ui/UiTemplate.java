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
package testing.site.ui;

import objectos.html.HtmlTemplate;
import objectos.http.Handler;
import objectos.http.Method;
import objectos.http.ServerExchange;

abstract class UiTemplate extends HtmlTemplate implements Handler {

  @Override
  public void handle(ServerExchange http) {
    http.methodMatrix(Method.GET, this::get);
  }

  void get(ServerExchange http) {
    http.ok(this);
  }

  @Override
  protected final void definition() {
    doctype();
    html(
        head(include(this::headImpl)),
        body(include(this::bodyImpl))
    );
  }

  void headImpl() {
    meta(charset("utf-8"));
    meta(httpEquiv("x-ua-compatible"), content("ie=edge"));
    meta(name("viewport"), content("width=device-width, initial-scale=1"));
    script(src("/common/way.js"));
    link(rel("stylesheet"), type("text/css"), href("/common/preflight.css"));
    link(rel("stylesheet"), type("text/css"), href("/ui/styles.css"));
  }

  abstract void bodyImpl();

}