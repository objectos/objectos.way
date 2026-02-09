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

/// Configures the `follow` action.
public final class Follow {

  // options
  static final JsString SE = JsString.raw("SE"); // scroll: element (into view)

  private JsOp scrollIntoView;

  @SuppressWarnings("unused")
  private JsArray update;

  Follow() {}

  /// Invokes `scrollIntoView` on the specified element after the content has
  /// been updated. Defaults to `document.documentElement` if not specified.
  ///
  /// @param value the element to be visible to the user
  public final void scrollIntoView(JsElement value) {
    final JsElement el;
    el = Objects.requireNonNull(value, "value == null");

    scrollIntoView = JsOp.of(SE, el);
  }

  /// The list of elements, given by their `id` attribute values, whose content
  /// should be updated with the server response. Defaults to updating the
  /// `document.body` if not specified.
  ///
  /// @param first the `id` of the first element
  /// @param more the `id` of the additional elements
  public final void update(Html.Id first, Html.Id... more) {

  }

  @Override
  public final String toString() {
    return "[\"FO\"" + addIf(scrollIntoView) + "]";
  }

  private String addIf(JsOp op) {
    return op != null ? "," + op : "";
  }

}
