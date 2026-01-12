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

/// A JS runtime `History` instance.
public final class JsHistory extends JsObject {

  /// Represents the `History` JS type.
  public static final JsType<JsHistory> type = new JsType<>(
      JsString.raw("History"), JsHistory::new
  );

  private JsHistory(JsBase recv, JsOp op) {
    super(recv, op);
  }

  /// Adds an entry to the browser's history stack.
  ///
  /// @param state object associated with the new history entry
  /// @param unused unused parameter but cannot be omitted
  /// @param the history entry's URL
  ///
  /// @return an object representing this action
  public final JsAction pushState(JsObject state, JsString unused, JsString url) {
    Objects.requireNonNull(state, "state == null");
    Objects.requireNonNull(unused, "unused == null");
    Objects.requireNonNull(url, "url == null");

    return invoke("History", "pushState", state, unused, url);
  }

}
