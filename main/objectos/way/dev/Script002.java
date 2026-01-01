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

/// Test case 002
///
/// - event: click 
/// - locator: target 
/// - property read and write
@Css.Source
public final class Script002 extends AbstractDevScript {

  public static final Script.JsAction ACTION = Script.target()
      .prop("Node", "textContent", Script.target().prop("Element", "id"));

  @Override
  final void renderBody() {
    div(
        id("foo-bar"),

        css("""
        border-color:var(--color-gray-200)
        border-style:solid
        border-width:1px
        padding:32rx
        """),

        dataOnClick(ACTION),

        text("My ID is...")
    );
  }

}
