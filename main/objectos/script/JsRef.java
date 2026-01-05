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

/// Represents a reference to a JS runtime value.
public final class JsRef extends JsBase {

  private JsRef(JsOp value) {
    super(value);
  }

  static JsRef args(int index) {
    if (index < 0) {
      throw new IllegalArgumentException(
          "index must not be negative"
      );
    }

    final JsNumber $index;
    $index = JsNumber.raw(index);

    final JsOp ref;
    ref = JsOp.of(JsString.AX, $index);

    return new JsRef(ref);
  }

  static JsRef var(String name) {
    Objects.requireNonNull(name, "name == null");

    final JsString $name;
    $name = JsString.raw(name);

    final JsOp ref;
    ref = JsOp.of(JsString.CR, $name);

    return new JsRef(ref);
  }

  /// Converts this reference to a JS reference of the specified type.
  ///
  /// @param <T> the JS runtime type
  ///
  /// @param type the handle representing the JS runtime type
  ///
  /// @return the converted reference
  public final <T> T as(JsType<T> type) {
    return type.as(this);
  }

}
