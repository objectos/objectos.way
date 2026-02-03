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

/// Configures the `navigate` action.
public final class Navigate {

  static final Navigate DEFAULT = new Navigate();

  private JsElement scrollIntoView = Js.document().documentElement();

  Navigate() {}

  /// Invokes `scrollIntoView` on the specified element after the new content
  /// has been swapped. Defaults to `document.documentElement` if not specified.
  ///
  /// @param value the element to be visible to the user
  public final void scrollIntoView(JsElement value) {
    scrollIntoView = Objects.requireNonNull(value, "value == null");
  }

  @Override
  public final String toString() {
    return "{\"scrollIntoView\":" + scrollIntoView + "}";
  }

}
