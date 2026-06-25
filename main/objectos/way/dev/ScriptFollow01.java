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

import objectos.css.CssSource;
import objectos.http.Request;
import objectos.http.Result;
import objectos.script.Js;
import objectos.script.JsAction;

/*

Test case 026

- event: click
- action: navigate (head element)

*/
@CssSource
public final class ScriptFollow01 extends AbstractDevScript {

  public static final JsAction ACTION = Js.follow();

  private boolean initial;

  @Override
  public final Result handle(Request http) {
    initial = http.queryParam("next") == null;

    return super.handle(http);
  }

  @Override
  final void renderHead() {
    meta(charset("utf-8"));
    meta(httpEquiv("content-type"), content("text/html; charset=utf-8"));
    if (initial) {
      meta(name("initial"), content("true"));
    }
    meta(name("viewport"), content("width=device-width, initial-scale=1"));
    link(rel("stylesheet"), type("text/css"), href("/styles.css"));
    script(src("/script.js"));
    title(initial ? "026: initial" : "026: next");
  }

  @Override
  final void renderBody() {
    div(
        css("""
        display:flex
        flex-direction:column
        gap:16rx
        """),

        div(
            id("subject"),

            text(initial ? "Before" : "After")
        ),

        a(
            id("click-me"),
            onclick(ACTION),
            href("/script/follow/01?next=true"),
            text("Click me")
        )
    );
  }

}
