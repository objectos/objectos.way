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

@Css.Source
public final class DevScriptElement extends ScriptJsElementHandler {

  @Override
  public final void handle(Http.Exchange http) {
    switch (http.pathParam("id")) {
      case "toggleClass0" -> http.ok(toggleClass0());
    }
  }

  public static final Script.JsAction TOGGLE_CLASS0 = Script
      .byId("subject").toggleClass("background-color:var(--color-red-200)");

  private Html.Component toggleClass0() {
    return page(
        "Script.Element.toggleClass0",

        m -> m.div(
            m.css("""
            display:flex
            flex-direction:column
            gap:16rx
            """),

            m.button(
                m.id("click-me"),
                m.dataOnClick(TOGGLE_CLASS0),
                m.type("button"),
                m.text("Click me")
            ),

            m.div(
                m.id("subject"),
                m.css("""
                height:64px
                """),
                m.text("Subject")
            )
        )
    );
  }

}
