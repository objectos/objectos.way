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

- JsElement
- action: render

*/
@Css.Source
public final class ScriptJsElementRender00 extends AbstractDevScript {

  public static final JsAction ACTION = Js.byId("subject").render("/script/element/render/00?after=true");

  private boolean after;

  @Override
  public final void handle(Http.Exchange http) {
    after = http.queryParam("after") != null;

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

        button(
            id("click-me"),
            onclick(ACTION),
            text("Click me"),
            type("button")
        ),

        div(
            id("subject"),

            text(after ? "After" : "Before")
        )
    );
  }

}
