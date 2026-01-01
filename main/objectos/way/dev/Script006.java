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

Test case 006

- event: click
- Script.of
- Script.var

*/
@Css.Source
public final class Script006 extends AbstractDevScript {

  public static final Script.JsAction ACTION = Script.of(
      Script.var("el", Script.byId("subject")),
      Script.var("el").as(Script.JsElement.type).remove()
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
            type("button"),
            text("Click me")
        ),

        div(
            id("subject"),

            css("""
            border-color:var(--color-gray-200)
            border-style:solid
            border-width:1px
            height:32px
            """),

            text("Subject")
        )
    );
  }

}
