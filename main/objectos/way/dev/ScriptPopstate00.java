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

- event: popstate

*/
@Css.Source
public final class ScriptPopstate00 extends AbstractDevScript {

  public static final JsAction ACTION = Js.popstate();

  private int step;

  private boolean wayRequest;

  @Override
  public final void handle(Http.Exchange http) {
    step = http.queryParamAsInt("step", 0);

    wayRequest = http.header(Http.HeaderName.WAY_REQUEST) != null;

    super.handle(http);
  }

  @Override
  final void renderBody() {
    onpopstate(ACTION);

    div(
        css("""
        display:flex
        flex-direction:column
        gap:16rx
        """),

        div(
            id("step"),

            text(Integer.toString(step))
        ),

        div(
            id("wayRequest"),

            text(Boolean.toString(wayRequest))
        ),

        a(
            id("click-me"),
            onclick(Js.follow()),
            href("/script/popstate/00?step=" + Integer.toString(step + 1)),
            text("Click me")
        )
    );

  }

}
