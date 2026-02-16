/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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
package objectos.way.dev;

import module objectos.way;

/*

- event: popstate

*/
@Css.Source
public final class ScriptPopstateIndex extends Html.Template implements Http.Handler {

  @Override
  public void handle(Http.Exchange http) {
    http.ok(this);
  }

  @Override
  protected final void render() {
    doctype();

    html(
        head(f(this::renderHead)),

        body(f(this::renderBody))
    );
  }

  private void renderHead() {
    meta(charset("utf-8"));
    meta(httpEquiv("content-type"), content("text/html; charset=utf-8"));
    meta(name("viewport"), content("width=device-width, initial-scale=1"));
    link(rel("stylesheet"), type("text/css"), href("/styles.css"));
  }

  private void renderBody() {
    ul(
        item("/script/popstate/00", "00")
    );
  }

  private Html.Instruction.OfElement item(String href, String text) {
    return li(
        a(
            css("""
            hover/text-decoration:underline
            """),
            href(href),
            text(text)
        )
    );
  }

}
