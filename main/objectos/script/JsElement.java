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

import objectos.way.Html;

/// Represents a JS runtime `Element` instance.
public sealed interface JsElement extends JsObject permits ScriptJsElement {

  /// Represents the `Element` JS type.
  JsType<JsElement> type = ScriptJsElement.$type();

  /// Returns the value of the attribute of the specified name.
  ///
  /// @param name the attribute name
  ///
  /// @return the attribute value
  default JsString attr(Html.AttributeName name) {
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
  JsString attr(JsString name);

  /// Returns the HTML of the descendants of this element.
  ///
  /// @return a string representing the HTML
  JsString innerHTML();

  /// Morphs the contents of this element with the specified HTML.
  ///
  /// @param the source HTML
  ///
  /// @return an object representing this action
  JsAction morph(JsString src);

  /// Removes this element from its parent node.
  ///
  /// @return an object representing this action
  JsAction remove();

  /// Toggles the specified `class` values on this element.
  ///
  /// @param first the first class name
  /// @param more additional class names
  ///
  /// @return an object representing this action
  JsAction toggleClass(String first, String... more);

}