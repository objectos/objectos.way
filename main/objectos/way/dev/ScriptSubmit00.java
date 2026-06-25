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
import objectos.http.HeaderName;
import objectos.http.Redirection;
import objectos.http.Request;
import objectos.http.Result;
import objectos.http.Status;
import objectos.script.Js;
import objectos.script.JsAction;

/*

- event: submit
- method: POST

*/
@CssSource
public final class ScriptSubmit00 extends AbstractDevScript {

  public static final JsAction ACTION = Js.submit();

  private boolean initial;

  private String input0;

  private boolean wayRequest;

  @Override
  public final Result handle(Request http) {
    final String path;
    path = http.path();

    if (path.endsWith("00")) {
      initial = true;

      return switch (http.method().name()) {
        case "GET" -> this;

        case "POST" -> {
          var input0 = http.formParam("input0");

          var wayRequest = http.header(HeaderName.WAY_REQUEST) != null;

          yield Redirection.found(
              "/script/submit/00/after?input0=" + input0 + "&wayRequest=" + wayRequest
          );
        }

        default -> Status.METHOD_NOT_ALLOWED;
      };
    } else {
      initial = false;

      input0 = http.queryParam("input0");

      wayRequest = "true".equals(http.queryParam("wayRequest"));

      return this;
    }
  }

  @Override
  final void renderBody() {
    div(
        initial
            ? form(
                css("""
                display:flex
                flex-direction:column
                height:200vh
                gap:16rx
                justify-content:space-between
                """),

                action("/script/submit/00"),

                method("post"),

                onsubmit(Js.submit()),

                div(
                    id("subject"),

                    text("Before")
                ),

                input(
                    id("input0"),
                    name("input0"),
                    type("text"),
                    value("Foo")
                ),

                button(
                    id("click-me"),
                    text("Click me"),
                    type("submit")
                )
            )
            : div(
                css("""
                display:flex
                flex-direction:column
                height:200vh
                gap:16rx
                justify-content:space-between
                """),

                div(
                    id("subject"),

                    text("input0=" + input0 + ":" + wayRequest)
                ),

                button(
                    id("click-me"),
                    text("Click me"),
                    type("button")
                )
            )
    );
  }

}
