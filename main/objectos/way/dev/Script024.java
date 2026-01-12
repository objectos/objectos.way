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

Test case 024

- event: click
- js object literal

*/
@Css.Source
public final class Script024 extends AbstractDevScript {

  public static final JsAction ACTION = Js.of(
      Js.var("literal", Js.object("p1", 123, "p2", true, "p3", "third property")),
      Js.byId("div-1").textContent(Js.var("literal").propUnchecked("p1").toJsString()),
      Js.byId("div-2").textContent(Js.var("literal").propUnchecked("p2").toJsString()),
      Js.byId("div-3").textContent(Js.var("literal").propUnchecked("p3").toJsString())
  );

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
            dataOnClick(ACTION),
            text("Click me"),
            type("button")
        ),

        div(
            id("div-1"),
            text("Div 1")
        ),

        div(
            id("div-2"),
            text("Div 2")
        ),

        div(
            id("div-3"),
            text("Div 3")
        )
    );
  }

}
