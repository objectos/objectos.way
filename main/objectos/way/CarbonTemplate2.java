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

abstract class CarbonTemplate2 extends Html.Template implements Web.Action {

  private final Http.Exchange http;

  private Html.ClassName theme = Carbon.WHITE;

  private String title = "New Objectos Carbon application";

  protected CarbonTemplate2(Http.Exchange http) {
    this.http = http;
  }

  @Override
  public void execute() {
    http.ok(this);
  }

  @Override
  protected final void render() throws Exception {
    Carbon carbon;
    carbon = http.get(Carbon.class);

    Carbon.Shell shell;
    shell = carbon.createShell(this);

    shell.theme(theme);

    shell.title(title);

    renderShell(shell);
  }

  protected abstract void renderShell(Carbon.Shell shell);

  protected final boolean currentPage(String href) {
    Http.Request.Target target;
    target = http.target();

    String path;
    path = target.path();

    return path.equals(href);
  }

  protected final void shellTheme(Html.ClassName theme) {
    this.theme = Check.notNull(theme, "theme == null");
  }

  protected final void shellTitle(String title) {
    this.title = Check.notNull(title, "title == null");
  }

}