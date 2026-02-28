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

/// A JS runtime `Document` instance.
public final class JsDocument extends JsNode {

  /// Represents the `Document` JS type.
  public static final JsType<JsDocument> type = new JsType<>("Document") {
    @Override
    final JsDocument create(Object value) {
      return new JsDocument(value);
    }

    @Override
    final JsDocument create(JsObject recv, JsArray op) {
      return new JsDocument(recv, op);
    }
  };

  private JsDocument(Object value) {
    super(value);
  }

  private JsDocument(JsObject recv, JsArray op) {
    super(recv, op);
  }

  /// Returns the root element of this document.
  ///
  /// @return the root element of this document
  public final JsElement documentElement() {
    return prop("Document", "documentElement").as(JsElement.type);
  }

  /// Returns the title of this document.
  ///
  /// @return the title of this document
  public final JsString title() {
    return prop("Document", "title").as(JsString.type);
  }

}
