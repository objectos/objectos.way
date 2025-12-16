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
public final class DevScriptById extends ScriptJsElementHandler {

  @Override
  public final void handle(Http.Exchange http) {
    switch (http.pathParam("id")) {
      case "invoke0" -> http.ok(invoke0());

      case "invoke1" -> http.ok(invoke1());

      case "property0" -> http.ok(property0());
    }
  }

  public static final Script.Action INVOKE0 = Script
      .byId("subject").invoke("Element", "remove");

  private Html.Component invoke0() {
    return page(
        "Script.byId.invoke0",

        m -> m.div(
            m.css("""
            display:flex
            flex-direction:column
            gap:16rx
            """),

            m.button(
                m.id("click-me"),
                m.dataOnClick(INVOKE0),
                m.type("button"),
                m.text("Click me")
            ),

            m.div(
                m.id("subject"),
                m.text("Subject")
            )
        )
    );
  }

  public static final Script.Action INVOKE1 = Script
      .byId("subject").invoke("Element", "removeAttribute", "style");

  private Html.Component invoke1() {
    return page(
        "Script.byId.invoke1",

        m -> m.div(
            m.css("""
            display:flex
            flex-direction:column
            gap:16rx
            """),

            m.button(
                m.id("click-me"),
                m.dataOnClick(INVOKE1),
                m.type("button"),
                m.text("Click me")
            ),

            m.div(
                m.id("subject"),
                m.style("width:64px;height:64px;background-color:red"),
                m.text("Subject")
            )
        )
    );
  }

  public static final Script.Action PROPERTY0 = Script
      .byId("subject").prop("Node", "textContent", Script.target().prop("Element", "id"));

  private Html.Component property0() {
    return page(
        "Script.byId.property0",

        m -> m.div(
            m.css("""
            display:flex
            flex-direction:column
            gap:16rx
            """),

            m.button(
                m.id("click-me"),
                m.dataOnClick(PROPERTY0),
                m.type("button"),
                m.text("Click me")
            ),

            m.div(
                m.id("subject"),

                m.css("""
                border-color:var(--color-gray-200)
                border-style:solid
                border-width:1px
                padding:32rx
                """),

                m.text("My ID is...")
            )
        )
    );
  }

}
