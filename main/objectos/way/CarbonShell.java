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

final class CarbonShell implements Carbon.Shell {

  private CarbonShellContent content;

  private CarbonHeader header;

  private CarbonSideNav sideNavigation;

  private Carbon.Theme theme;

  private final Html.TemplateBase tmpl;

  CarbonShell(Html.TemplateBase tmpl, Carbon.Shell.Value[] values) {
    this.tmpl = tmpl;

    for (var value : values) {
      switch (value) {
        case CarbonShellContent o -> content = o;

        case CarbonHeader o -> header = o;

        case CarbonSideNav o -> sideNavigation = o;

        case Carbon.Theme o -> theme = o;
      }
    }
  }

  private void preRender() {
    if (header != null) {
      header.accept(sideNavigation);
    }

    if (sideNavigation != null) {
      sideNavigation.accept(header);
    }
  }

  public final void render() {
    preRender();

    tmpl.doctype();

    tmpl.html(
        theme != null ? theme : tmpl.noop(),

        tmpl.head(
            tmpl.meta(tmpl.charset("utf-8")),
            tmpl.meta(tmpl.httpEquiv("content-type"), tmpl.content("text/html; charset=utf-8")),
            tmpl.meta(tmpl.name("viewport"), tmpl.content("width=device-width, initial-scale=1")),
            tmpl.link(tmpl.rel("shortcut icon"), tmpl.type("image/x-icon"), tmpl.href("/favicon.png")),
            tmpl.link(tmpl.rel("stylesheet"), tmpl.type("text/css"), tmpl.href("/ui/carbon.css")),
            tmpl.script(tmpl.src("/ui/script.js"))
        ),

        tmpl.body(
            Carbon.render(tmpl, header),

            Carbon.render(tmpl, sideNavigation),

            Carbon.render(tmpl, content)
        )
    );
  }

}