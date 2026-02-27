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

/// A JS runtime `Node` instance.
public sealed class JsNode extends JsObject
    permits
    JsDocument,
    JsElement {

  JsNode(Object value) {
    super(value);
  }

  JsNode(JsObject recv, JsOp op) {
    super(recv, op);
  }

  /// Returns the text content of this node and its descendants.
  ///
  /// @return the text content
  public final JsString textContent() {
    return prop("Node", "textContent").as(JsString.type);
  }

  /// Sets the text content of this node to the specfied value.
  ///
  /// @param value the new text content
  ///
  /// @return an object representing this action
  public final JsAction textContent(JsString value) {
    return prop("Node", "textContent", value);
  }

}
