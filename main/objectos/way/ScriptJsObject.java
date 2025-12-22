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

sealed class ScriptJsObject implements Script.JsObject
    permits
    ScriptJsElement,
    ScriptJsString {

  private final String value;

  ScriptJsObject(String value) {
    this.value = value;
  }

  @Override
  public final Script.JsAction invoke(String type, String method, Object... args) {
    final ScriptJsArray.Builder invoke;
    invoke = new ScriptJsArray.Builder();

    invoke.rawString("IV");
    invoke.jsString(type, "type");
    invoke.jsString(method, "method");

    final ScriptJsArray $args;
    $args = ScriptJsArray.of(args, "args");

    invoke.raw($args);

    return ScriptJsAction.of(value, invoke.buildString());
  }

  @Override
  public final Script.JsObject prop(String type, String name) {
    final ScriptJsArray.Builder prop;
    prop = new ScriptJsArray.Builder();

    prop.rawString("PR");
    prop.jsString(type, "type");
    prop.jsString(name, "name");

    return new ScriptJsObject(value + "," + prop.buildString());
  }

  @Override
  public final Script.JsAction prop(String type, String name, Object value) {
    final ScriptJsArray.Builder prop;
    prop = new ScriptJsArray.Builder();

    prop.rawString("PW");
    prop.jsString(type, "type");
    prop.jsString(name, "name");
    prop.way(value, "value");

    return ScriptJsAction.of(this.value, prop.buildString());
  }

  @Override
  public final String toString() {
    return value;
  }

  public final String wayLiteral() {
    return "[\"WA\",[" + value + "]]";
  }

}
