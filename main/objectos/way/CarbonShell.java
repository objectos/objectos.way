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
package objectos.way;

import objectos.lang.object.Check;

abstract class CarbonShell extends Html.Template implements Web.Action {

  private final Http.Exchange http;

  protected final Carbon.Ui ui;

  private CarbonTheme theme = CarbonTheme.WHITE;

  private String title;

  protected CarbonShell(Http.Exchange http) {
    this.http = http;

    Carbon carbon;
    carbon = http.get(Carbon.class);

    ui = carbon.ui(this);
  }

  @Override
  public void execute() {
    http.ok(this);
  }

  @Override
  protected final void render() throws Exception {
    doctype();

    html(
        className(theme.className),

        head(
            meta(charset("utf-8")),
            meta(httpEquiv("content-type"), content("text/html; charset=utf-8")),
            meta(name("viewport"), content("width=device-width, initial-scale=1")),
            script(src("/ui/script.js")),
            link(rel("shortcut icon"), type("image/x-icon"), href("/favicon.png")),
            link(rel("stylesheet"), type("text/css"), href("/ui/carbon.css")),
            title != null ? title(title) : noop()
        ),

        body(
            f(this::renderUi)
        )
    );
  }

  protected abstract void renderUi() throws Exception;

  protected final void shellTheme(Carbon.Theme theme) {
    this.theme = (CarbonTheme) Check.notNull(theme, "theme == null");
  }

  protected final void shellTitle(String title) {
    this.title = Check.notNull(title, "title == null");
  }

}