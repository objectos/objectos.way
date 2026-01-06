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

Test case 019

- event: click
- type: boolean
- action: branch

*/
@Css.Source
public final class Script019 extends AbstractDevScript {

  public static final JsAction ACTION = Js.target().hasAttribute(DATA_TEST).test(
      Js.target().remove(),
      Js.noop()
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
            id("btn-1"),
            dataOnClick(ACTION),
            type("button"),
            text("Button 1")
        ),

        button(
            id("btn-2"),
            attr(DATA_TEST, "true"),
            dataOnClick(ACTION),
            type("button"),
            text("Button 2")
        ),

        button(
            id("btn-3"),
            dataOnClick(ACTION),
            type("button"),
            text("Button 3")
        )
    );
  }

}
