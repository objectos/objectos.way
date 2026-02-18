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

- event: click
- action: follow
- options: history[false]

*/
@Css.Source
public final class ScriptFollow04 extends AbstractDevScript {

  public static final JsAction ACTION = Js.follow(opts -> opts.history(false));

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
        """),

        a(
            id("click-me"),
            onclick(ACTION),
            href("/script/follow/04?next=true"),
            text("Click me")
        ),

        div(
            id("subject"),

            text(initial ? "Before" : "After")
        )
    );
  }

}
