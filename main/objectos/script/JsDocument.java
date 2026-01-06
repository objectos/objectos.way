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
  public static final JsType<JsDocument> type = new JsType<>(
      JsString.raw("Document"), JsDocument::new
  );

  private JsDocument(JsBase recv, JsOp op) {
    super(recv, op);
  }

  /// Returns the title of this document.
  ///
  /// @return the title of this document
  public final JsString title() {
    return prop("Document", "title").as(JsString.type);
  }

}
