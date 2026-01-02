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
public final class JsRef {

  private static final JsString AX = JsString.raw("AX");

  private static final JsString CR = JsString.raw("CR");

  private final String value;

  private JsRef(String value) {
    this.value = value;
  }

  static JsRef args(int index) {
    if (index < 0) {
      throw new IllegalArgumentException(
          "index must not be negative"
      );
    }

    final JsNumber $index;
    $index = JsNumber.raw(index);

    final JsArray ref;
    ref = JsArray.raw(AX, $index);

    return of(ref);
  }

  private static JsRef of(JsObject v0) {
    return new JsRef(
        v0.toString()
    );
  }

  static JsRef of(JsObject v0, JsObject v1) {
    return new JsRef(
        v0.toString() + "," + v1.toString()
    );
  }

  static JsRef var(String name) {
    Objects.requireNonNull(name, "name == null");

    final JsString $name;
    $name = JsString.raw(name);

    final JsArray ref;
    ref = JsArray.raw(CR, $name);

    return of(ref);
  }

  /// Converts this reference to a JS reference of the specified type.
  ///
  /// @param <T> the JS runtime type
  ///
  /// @param type the handle representing the JS runtime type
  ///
  /// @return the converted reference
  public final <T> T as(JsType<T> type) {
    final JsType<T> impl;
    impl = (JsType<T>) type;

    return impl.as(value);
  }

  @Override
  public final String toString() {
    return value;
  }

}
