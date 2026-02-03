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
  public static final JsType<JsElement> type = new JsType<>(
      JsString.raw("Element"), JsElement::new
  );

  static final JsElement TARGET = new JsElement(JsOp.ET);

  private JsElement(JsOp op) {
    super(op);
  }

  private JsElement(JsBase recv, JsOp op) {
    super(recv, op);
  }

  static JsElement byId(JsString value) {
    return byId0(value);
  }

  private static JsElement byId0(JsObject value) {
    Objects.requireNonNull(value, "value == null");

    final JsOp op;
    op = JsOp.of(JsString.EI, value);

    return new JsElement(op);
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

  /// Returns a boolean value indicating whether the element has the attribute
  /// with the specified name.
  ///
  /// @param name the attribute name
  ///
  /// @return a boolean
  public final JsBoolean hasAttribute(Html.AttributeName name) {
    final String attrName;
    attrName = name.name();

    final JsString $name;
    $name = JsString.of(attrName);

    return hasAttribute($name);
  }

  /// Returns a boolean value indicating whether the element has the attribute
  /// with the specified name.
  ///
  /// @param name the attribute name
  ///
  /// @return a boolean
  public final JsBoolean hasAttribute(JsString name) {
    return invoke(JsBoolean.type, "Element", "hasAttribute", name);
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

    final JsOp op;
    op = JsOp.of(JsString.MO, src);

    return action(op);
  }

  /// Returns the first descendant of this element matching the specified
  /// selectors.
  ///
  /// @param selectors the selectors to match
  ///
  /// @return the first descendant matching the selectors
  public final JsElement querySelector(JsString selectors) {
    return invoke(JsElement.type, "Element", "querySelector", selectors);
  }

  /// Removes this element from its parent node.
  ///
  /// @return an object representing this action
  public final JsAction remove() {
    return invoke("Element", "remove");
  }

  /// Ensures that this element is visible to the user.
  ///
  /// @return an object representing this action
  public final JsAction scrollIntoView() {
    return invoke("Element", "scrollIntoView");
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
