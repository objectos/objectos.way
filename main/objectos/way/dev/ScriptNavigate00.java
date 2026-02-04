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
import objectos.script.Js;

/*

Test case 023

- event: click
- action: navigate

*/
@Css.Source
public final class ScriptNavigate00 extends AbstractDevScript {

  public static final JsAction ACTION = Js.navigate();

  private boolean initial;

  @Override
  public final void handle(Http.Exchange http) {
    initial = http.queryParam("next") == null;

    super.handle(http);
  }

  @Override
  final void renderBody() {
    div(
        css("""
        display:flex
        flex-direction:column
        gap:16rx
        height:200vh
        justify-content:space-between
        """),

        div(
            id("subject"),

            dataFrame("root", initial ? "1" : "2"),

            text(initial ? "Before" : "After")
        ),

        initial
            ? div(id("keep-me"), text("Keep me"))
            : noop(),

        a(
            id("click-me"),
            onclick(ACTION),
            href("/script/navigate/00?next=true"),
            text("Click me")
        )
    );
  }

}
