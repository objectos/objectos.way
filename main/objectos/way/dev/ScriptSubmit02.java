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

- event: success

*/
@Css.Source
public final class ScriptSubmit02 extends AbstractDevScript {

  public static final JsAction ACTION = Js.submit(opts -> opts.scrollIntoView(Js.byId("bottom")));

  private boolean initial;

  @Override
  public final void handle(Http.Exchange http) {
    switch (http.method()) {
      case GET -> { initial = true; super.handle(http); }

      case POST -> { initial = false; super.handle(http); }

      default -> http.allow(Http.Method.GET, Http.Method.POST);
    }
  }

  @Override
  final void renderBody() {
    form(
        css("""
        display:flex
        flex-direction:column
        gap:16rx
        height:300vh
        justify-content:space-between
        """),

        action("/script/submit/02"),

        method("post"),

        onsubmit(ACTION),

        div(
            id("top"),

            text(initial ? "top:before" : "top:after")
        ),

        button(
            id("click-me"),
            text("Click me"),
            type("submit")
        ),

        div(
            id("bottom"),

            text(initial ? "bottom:before" : "bottom:after")
        )
    );
  }

}
