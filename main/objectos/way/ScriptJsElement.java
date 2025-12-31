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
import objectos.way.Script.JsAction;
import objectos.way.Script.JsString;

final class ScriptJsElement extends ScriptJsObject implements Script.JsElement {

  private static final Script.JsString EI = ScriptJsString.raw("EI");

  private static final Script.JsString MO = ScriptJsString.raw("MO");

  private static final Script.JsString Element = ScriptJsString.raw("Element");

  static final ScriptJsElement TARGET = new ScriptJsElement("""
  ["ET"]""");

  private ScriptJsElement(String value) {
    super(value);
  }

  static ScriptJsElement byId(Script.JsString value) {
    return byId0(value);
  }

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
  public final Script.JsString attr(Script.JsString name) {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public final Script.JsString innerHTML() {
    return prop("Element", "innerHTML").asString();
  }

  @Override
  public final JsAction morph(JsString src) {
    Objects.requireNonNull(src, "src == null");

    final Script.JsArray $src;
    $src = ScriptJsArray.raw(src);

    final Script.JsArray action;
    action = ScriptJsArray.raw(MO, $src);

    return ScriptJsAction.of(this, action);
  }

  @Override
  public final Script.JsAction remove() {
    return invoke("Element", "remove");
  }

  @Override
  public final Script.JsAction toggleClass(String first, String... more) {
    final ScriptJsArray.Builder arrayBuilder;
    arrayBuilder = ScriptJsArray.jsBuilder();

    arrayBuilder.raw(first, "first");

    arrayBuilder.rawAll(more, "more");

    final ScriptJsArray array;
    array = arrayBuilder.build();

    final Script.JsString arg0;
    arg0 = Script.args(0).asString();

    final Script.JsAction action;
    action = prop("Element", "classList").invoke("DOMTokenList", "toggle", arg0);

    return array.forEach(action);
  }

}
