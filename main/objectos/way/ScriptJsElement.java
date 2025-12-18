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

final class ScriptJsElement extends ScriptJsObject implements Script.JsElement {

  static final ScriptJsElement TARGET = new ScriptJsElement("""
  ["LO","TT"]""");

  ScriptJsElement(String value) {
    super(value);
  }

  static ScriptJsElement byId(String value) {
    var builder = new ScriptJsArray.Builder();

    builder.rawString("LO");
    builder.rawString("ID");
    builder.jsString(value, "value");

    return builder.build(ScriptJsElement::new);
  }

  @Override
  public final Script.JsAction toggleClass(String value) {
    final String[] parts;
    parts = value.split(" ");

    final Object[] args;
    args = parts;

    return prop("Element", "classList").invoke("DOMTokenList", "toggle", args);
  }

}
