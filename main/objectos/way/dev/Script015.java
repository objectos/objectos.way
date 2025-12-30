/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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

Test case 015

- event: click
- action: morph
- always morph frame with name only

*/
@Css.Source
public final class Script015 extends AbstractDevScript {

  public static final Script.JsAction ACTION = Script.byId("subject").morph(
      Script.byId("src").innerHTML()
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
            id("subject"),

            div(
                dataFrame("root", "x"),

                span(className("hd"), text("SUBJECT")),

                span(className("fixed"), text("Fixed")),

                div(
                    className("contents"),

                    dataFrame("name-only"),

                    text("Before")
                )
            )
        ),

        div(
            id("src"),

            div(
                dataFrame("root", "x"),

                span(className("hd"), text("SRC")),

                div(
                    className("contents"),

                    dataFrame("name-only"),

                    text("After")
                )
            )
        )
    );
  }

}
