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

import objectos.way.Script;

public final class DevScriptProp0 extends AbstractDevScript {

  public static final Script.Action SCRIPT = Script.target()
      .prop("Element", "classList")
      .invoke("DOMTokenList", "toggle", "background-color:gray-200");

  @Override
  final String path() {
    return "/script/prop0";
  }

  @Override
  final void renderBody() {
    div(
        id("click-me"),

        css("""
        align-items:center
        border-color:black
        border-style:solid
        border-width:1px
        display:flex
        height:100rx
        justify-content:space-between
        width:100rx
        """),

        span("Click me!")
    );
  }

}
