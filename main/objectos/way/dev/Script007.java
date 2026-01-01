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

Test case 007

- event: click
- Script.arr
- JsArray.forEach

*/
@Css.Source
public final class Script007 extends AbstractDevScript {

  public static final Script.JsAction ACTION = Script.array("el-1", "el-3").forEach(
      Script.byId(Script.args(0).as(Script.JsString.type)).remove()
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

        div(id("el-1"), text("Div 1")),

        div(id("el-2"), text("Div 2")),

        div(id("el-3"), text("Div 3"))
    );
  }

}
