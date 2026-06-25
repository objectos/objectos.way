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

import objectos.css.CssSource;
import objectos.http.Request;
import objectos.http.Result;
import objectos.http.Status;
import objectos.script.Js;
import objectos.script.JsAction;

/*

- option: scrollIntoView

*/
@CssSource
public final class ScriptSubmit02 extends AbstractDevScript {

  public static final JsAction ACTION = Js.submit(opts -> opts.scroll(Js.byId("bottom")));

  private boolean initial;

  @Override
  public final Result handle(Request http) {
    return switch (http.method().name()) {
      case "GET" -> { initial = true; yield super.handle(http); }

      case "POST" -> { initial = false; yield super.handle(http); }

      default -> Status.METHOD_NOT_ALLOWED;
    };
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
