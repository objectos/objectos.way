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
public final class DevScriptTarget extends ScriptJsElementHandler {

  public static final Script.Action INVOKE0 = Script.target()
      .invoke("Element", "remove");

  public static final Script.Action INVOKE1 = Script.target()
      .invoke("Element", "removeAttribute", "style");

  @Override
  public final void handle(Http.Exchange http) {
    switch (http.pathParam("id")) {
      case "invoke0" -> http.ok(invoke0());

      case "invoke1" -> http.ok(invoke1());
    }
  }

  private Html.Component invoke0() {
    return page(
        "Script.target.invoke0",

        m -> m.div(
            m.css("""
            display:flex
            flex-direction:column
            gap:16rx
            """),

            m.button(
                m.id("btn-1"),
                m.dataOnClick(INVOKE0),
                m.type("button"),
                m.text("Button 1")
            ),

            m.button(
                m.id("btn-2"),
                m.dataOnClick(INVOKE0),
                m.type("button"),
                m.text("Button 2")
            ),

            m.button(
                m.id("btn-3"),
                m.dataOnClick(INVOKE0),
                m.type("button"),
                m.text("Button 3")
            )
        )
    );
  }

  private Html.Component invoke1() {
    return page(
        "Script.target.invoke1",

        m -> m.div(
            m.css("""
            display:flex
            flex-direction:column
            gap:16rx
            """),

            m.div(
                m.id("div-1"),
                m.dataOnClick(INVOKE1),
                m.style("width:64px;height:64px;background-color:red"),
                m.text("Div 1")
            ),

            m.div(
                m.id("div-2"),
                m.dataOnClick(INVOKE1),
                m.style("width:64px;height:64px;background-color:red"),
                m.text("Div 2")
            ),

            m.div(
                m.id("div-3"),
                m.dataOnClick(INVOKE1),
                m.style("width:64px;height:64px;background-color:red"),
                m.text("Div 3")
            )
        )
    );
  }

}
