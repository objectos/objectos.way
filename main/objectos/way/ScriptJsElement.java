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

import java.util.Objects;

final class ScriptJsElement extends ScriptJsObject implements Script.JsElement {

  private static final Script.JsString Element = ScriptJsString.raw("Element");

  static final ScriptJsElement TARGET = new ScriptJsElement("""
  ["ET"]""");

  private ScriptJsElement(String value) {
    super(value);
  }

  static ScriptJsElement byId(Script.JsString value) {
    return byId0(value);
  }

  private static final Script.JsString EI = ScriptJsString.raw("EI");

  private static ScriptJsElement byId0(Script.JsObject value) {
    Objects.requireNonNull(value, "value == null");

    final Script.JsArray $value;
    $value = ScriptJsArray.raw(value);

    final Script.JsArray elem;
    elem = ScriptJsArray.raw(EI, $value);

    return new ScriptJsElement(
        elem.toString()
    );
  }

  public static ScriptJsElement cast(ScriptJsRef ref) {
    return ref.cast(ScriptJsElement::new, Element);
  }

  @Override
  public final Script.JsAction remove() {
    return invoke("Element", "remove");
  }

  @Override
  public final Script.JsAction toggleClass(String first, String... more) {
    Objects.requireNonNull(first, "first == null");
    Objects.requireNonNull(more, "more == null");

    final Script.JsObject[] strings;
    strings = new Script.JsObject[more.length + 1];

    strings[0] = ScriptJsString.of(first);

    for (int idx = 0; idx < more.length; idx++) {
      final String v;
      v = more[idx];

      if (v == null) {
        throw new NullPointerException("more[" + idx + "] == null");
      }

      strings[idx + 1] = ScriptJsString.of(v);
    }

    final Script.JsArray array;
    array = ScriptJsArray.of(strings);

    final Script.JsString arg0;
    arg0 = Script.args(0).asString();

    final Script.JsAction action;
    action = prop("Element", "classList").invoke("DOMTokenList", "toggle", arg0);

    return array.forEach(action);
  }

}
