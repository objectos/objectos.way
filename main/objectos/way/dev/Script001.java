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

/// Test case 001
/// 
/// - event: click
/// - locator: target
/// - invoke method w/ 1 args
@Css.Source
public final class Script001 extends AbstractDevScript {

  public static final Script.Action ACTION = Script.target()
      .invoke("Element", "removeAttribute", "style");

  @Override
  final void renderBody() {
    div(
        css("""
        display:flex
        flex-direction:column
        gap:16rx
        """),

        div(
            id("div-1"),
            dataOnClick(ACTION),
            style("width:64px;height:64px;background-color:red"),
            text("Div 1")
        ),

        div(
            id("div-2"),
            dataOnClick(ACTION),
            style("width:64px;height:64px;background-color:red"),
            text("Div 2")
        ),

        div(
            id("div-3"),
            dataOnClick(ACTION),
            style("width:64px;height:64px;background-color:red"),
            text("Div 3")
        )
    );
  }

}
