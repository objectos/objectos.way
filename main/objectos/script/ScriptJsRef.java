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

final class ScriptJsRef implements JsRef {

  private static final JsString AX = ScriptJsString.raw("AX");

  private static final JsString CR = ScriptJsString.raw("CR");

  private final String value;

  private ScriptJsRef(String value) {
    this.value = value;
  }

  public static ScriptJsRef args(int index) {
    if (index < 0) {
      throw new IllegalArgumentException(
          "index must not be negative"
      );
    }

    final ScriptJsNumber $index;
    $index = ScriptJsNumber.raw(index);

    final ScriptJsArray ref;
    ref = ScriptJsArray.raw(AX, $index);

    return of(ref);
  }

  private static ScriptJsRef of(JsObject v0) {
    return new ScriptJsRef(
        v0.toString()
    );
  }

  static ScriptJsRef of(JsObject v0, JsObject v1) {
    return new ScriptJsRef(
        v0.toString() + "," + v1.toString()
    );
  }

  public static ScriptJsRef var(String name) {
    Objects.requireNonNull(name, "name == null");

    final ScriptJsString $name;
    $name = ScriptJsString.raw(name);

    final ScriptJsArray ref;
    ref = ScriptJsArray.raw(CR, $name);

    return of(ref);
  }

  @Override
  public final <T> T as(JsType<T> type) {
    final ScriptJsType<T> impl;
    impl = (ScriptJsType<T>) type;

    return impl.as(value);
  }

  @Override
  public final String toString() {
    return value;
  }

}
