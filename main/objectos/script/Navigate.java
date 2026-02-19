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

sealed abstract class Navigate permits Follow, Submit {

  // options
  static final JsString HI = JsString.raw("HI"); // history
  static final JsString SE = JsString.raw("SE"); // scroll: element (into view)
  static final JsString SO = JsString.raw("SO"); // scroll: off
  static final JsString UP = JsString.raw("UP"); // update: elements to update

  private final String name;

  private JsOp history;

  private JsOp scroll;

  private JsArray update;

  Navigate(String name) {
    this.name = name;
  }

  /// Configures whether the browser's history is updated on a successful
  /// response.
  ///
  /// @param value `true` if the browser's history should be updated;
  ///        `false` otherwise
  public final void history(boolean value) {
    if (value) {
      history = null;
    } else {
      history = JsOp.of(HI, "false");
    }
  }

  /// Configures whether the window scroll position should be reset on a
  /// successful response.
  ///
  /// @param value the element to be visible to the user
  public final void scroll(boolean value) {
    if (value) {
      scroll = null;
    } else {
      scroll = JsOp.of(SO);
    }
  }

  /// Scrolls to the specified element after the content has been updated.
  ///
  /// @param value the element to be visible to the user
  public final void scroll(JsElement value) {
    final JsElement el;
    el = Objects.requireNonNull(value, "value == null");

    scroll = JsOp.of(SE, el);
  }

  /// The list of elements, given by their `id` attribute values, whose content
  /// should be updated with the server response. Defaults to updating the
  /// `document.body` if not specified.
  ///
  /// @param first the `id` of the first element
  /// @param more the `id` of the additional elements
  public final void update(Html.Id first, Html.Id... more) {
    final JsArray.Builder builder;
    builder = JsArray.rawBuilder();

    builder.add(UP);

    builder.rawString(first, "first");

    for (int i = 0; i < more.length; i++) {
      final Html.Id id;
      id = more[i];

      builder.rawString(id, "more", i);
    }

    update = builder.build();
  }

  @Override
  public final String toString() {
    return "[\"" + name + "\"" + addIf(history) + addIf(scroll) + addIf(update) + "]";
  }

  final String addIf(Object op) {
    return op != null ? "," + op : "";
  }

}
