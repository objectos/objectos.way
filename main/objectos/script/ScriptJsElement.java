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
package objectos.script;

import java.util.Objects;

final class ScriptJsElement extends ScriptJsObject implements JsElement {

  private static final JsString EI = ScriptJsString.raw("EI");

  private static final JsString MO = ScriptJsString.raw("MO");

  static final ScriptJsElement TARGET = new ScriptJsElement("""
  ["ET"]""");

  private ScriptJsElement(String value) {
    super(value);
  }

  static ScriptJsElement byId(JsString value) {
    return byId0(value);
  }

  private static ScriptJsElement byId0(JsObject value) {
    Objects.requireNonNull(value, "value == null");

    final JsArray $value;
    $value = ScriptJsArray.raw(value);

    final JsArray elem;
    elem = ScriptJsArray.raw(EI, $value);

    return new ScriptJsElement(
        elem.toString()
    );
  }

  static JsType<JsElement> $type() {
    final ScriptJsString typeName;
    typeName = ScriptJsString.raw("Element");

    return new ScriptJsType<>(typeName, ScriptJsElement::new);
  }

  @Override
  public final JsString attr(JsString name) {
    return invoke(JsString.type, "Element", "getAttribute", name);
  }

  @Override
  public final JsString innerHTML() {
    return prop("Element", "innerHTML").as(JsString.type);
  }

  @Override
  public final JsAction morph(JsString src) {
    Objects.requireNonNull(src, "src == null");

    final JsArray $src;
    $src = ScriptJsArray.raw(src);

    final JsArray action;
    action = ScriptJsArray.raw(MO, $src);

    return ScriptJsAction.of(this, action);
  }

  @Override
  public final JsAction remove() {
    return invoke("Element", "remove");
  }

  @Override
  public final JsAction toggleClass(String first, String... more) {
    final ScriptJsArray.Builder arrayBuilder;
    arrayBuilder = ScriptJsArray.jsBuilder();

    arrayBuilder.raw(first, "first");

    arrayBuilder.rawAll(more, "more");

    final ScriptJsArray array;
    array = arrayBuilder.build();

    final JsString arg0;
    arg0 = Js.args(0).as(JsString.type);

    final JsAction action;
    action = prop("Element", "classList").invoke("DOMTokenList", "toggle", arg0);

    return array.forEach(action);
  }

}
