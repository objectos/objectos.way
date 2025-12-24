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

Test case 008

- event: click
- Element.toggleClass

*/
@Css.Source
public final class Script008 extends AbstractDevScript {

  public static final Script.JsAction ACTION = Script.byId("subject").toggleClass("opacity:0", "opacity:1");

  @Override
  final void renderBody() {
    div(
        css("""
        background-color:black
        height:256px
        opacity:1
        width:256px

        padding:16px
        """),

        id("subject"),

        div(
            css("""
            background-color:white
            height:100%
            width:100%
            """),

            button(
                id("click-me"),
                dataOnClick(ACTION),
                type("button"),
                text("Click me")
            )
        )
    );
  }

}
