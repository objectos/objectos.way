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
import objectos.way.Carbon.Component.Header;
import objectos.way.Carbon.Shell;

final class CarbonShell extends CarbonContainer implements Carbon.Shell {

  private Html.ClassName theme = Carbon.WHITE;

  private String title;

  CarbonShell(Html.Template tmpl) {
    super(tmpl);
  }

  @Override
  public final Shell theme(Html.ClassName value) {
    theme = Check.notNull(value, "value == null");
    return this;
  }

  @Override
  public final Shell title(String value) {
    title = Check.notNull(value, "value == null");
    return this;
  }

  @Override
  public final Header addHeader() {
    return addComponent(new CarbonHeader(tmpl));
  }

  @Override
  public final void render() {
    tmpl.doctype();

    tmpl.html(
        theme,

        tmpl.head(
            tmpl.meta(tmpl.charset("utf-8")),
            tmpl.meta(tmpl.httpEquiv("content-type"), tmpl.content("text/html; charset=utf-8")),
            tmpl.meta(tmpl.name("viewport"), tmpl.content("width=device-width, initial-scale=1")),
            tmpl.script(tmpl.src("/ui/script.js")),
            tmpl.link(tmpl.rel("shortcut icon"), tmpl.type("image/x-icon"), tmpl.href("/favicon.png")),
            tmpl.link(tmpl.rel("stylesheet"), tmpl.type("text/css"), tmpl.href("/ui/carbon.css")),
            title != null ? tmpl.title(title) : tmpl.noop()
        ),

        tmpl.body(
            renderComponents()
        )
    );
  }

}