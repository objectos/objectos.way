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
import objectos.way.Html;

/// A JS runtime `Element` instance.
public final class JsElement extends JsNode {

  /// Represents the `Element` JS type.
  public static final JsType<JsElement> type = JsElement.$type();

  private static final JsString EI = JsString.raw("EI");

  private static final JsString MO = JsString.raw("MO");

  static final JsElement TARGET = new JsElement("""
  ["ET"]""");

  private JsElement(String value) {
    super(value);
  }

  static JsElement byId(JsString value) {
    return byId0(value);
  }

  private static JsElement byId0(JsObject value) {
    Objects.requireNonNull(value, "value == null");

    final JsArray $value;
    $value = JsArray.raw(value);

    final JsArray elem;
    elem = JsArray.raw(EI, $value);

    return new JsElement(
        elem.toString()
    );
  }

  static JsType<JsElement> $type() {
    final JsString typeName;
    typeName = JsString.raw("Element");

    return new JsType<>(typeName, JsElement::new);
  }

  /// Returns the value of the attribute of the specified name.
  ///
  /// @param name the attribute name
  ///
  /// @return the attribute value
  public final JsString attr(Html.AttributeName name) {
    final String attrName;
    attrName = name.name();

    final JsString $name;
    $name = JsString.of(attrName);

    return attr($name);
  }

  /// Returns the value of the attribute of the specified name.
  ///
  /// @param name the attribute name
  ///
  /// @return the attribute value
  public final JsString attr(JsString name) {
    return invoke(JsString.type, "Element", "getAttribute", name);
  }

  /// Returns the HTML of the descendants of this element.
  ///
  /// @return a string representing the HTML
  public final JsString innerHTML() {
    return prop("Element", "innerHTML").as(JsString.type);
  }

  /// Morphs the contents of this element with the specified HTML.
  ///
  /// @param src the source HTML
  ///
  /// @return an object representing this action
  public final JsAction morph(JsString src) {
    Objects.requireNonNull(src, "src == null");

    final JsArray $src;
    $src = JsArray.raw(src);

    final JsArray action;
    action = JsArray.raw(MO, $src);

    return JsAction.of(this, action);
  }

  /// Removes this element from its parent node.
  ///
  /// @return an object representing this action
  public final JsAction remove() {
    return invoke("Element", "remove");
  }

  /// Toggles the specified `class` values on this element.
  ///
  /// @param first the first class name
  /// @param more additional class names
  ///
  /// @return an object representing this action
  public final JsAction toggleClass(String first, String... more) {
    final JsArray.Builder arrayBuilder;
    arrayBuilder = JsArray.jsBuilder();

    arrayBuilder.raw(first, "first");

    arrayBuilder.rawAll(more, "more");

    final JsArray array;
    array = arrayBuilder.build();

    final JsString arg0;
    arg0 = Js.args(0).as(JsString.type);

    final JsAction action;
    action = prop("Element", "classList").invoke("DOMTokenList", "toggle", arg0);

    return array.forEach(action);
  }

}
