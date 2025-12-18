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
package objectos.way;

import objectos.way.Script.JsAction;

final class ScriptJsAction implements Script.JsAction {

  private final String value;

  ScriptJsAction(String value) {
    this.value = value;
  }

  static ScriptJsAction of(Script.JsAction first, Script.JsAction second, Script.JsAction[] more) {
    final ScriptJsArray.Builder builder;
    builder = new ScriptJsArray.Builder();

    builder.raw(first, "first");
    builder.raw(second, "second");

    for (int idx = 0; idx < more.length; idx++) {
      final JsAction o;
      o = more[idx];

      builder.raw(o, "more", idx);
    }

    return builder.build(ScriptJsAction::new);
  }

  static ScriptJsAction var(String name, Object value) {
    final ScriptJsArray.Builder builder;
    builder = new ScriptJsArray.Builder();

    builder.rawString("CW");
    builder.jsString(name, "name");
    builder.wayObject(value, "value");

    return builder.build(ScriptJsAction::new);
  }

  @Override
  public final String toString() {
    return value;
  }

}
