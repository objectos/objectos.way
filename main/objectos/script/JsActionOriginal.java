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

/// Represents an action or a sequence of actions to be executed by the
/// JS runtime.
public final class JsActionOriginal {

  private static final JsString CW = JsString.raw("CW");

  static final JsActionOriginal NOOP = new JsActionOriginal("[\"NO\"]");

  private final String value;

  private JsActionOriginal(String value) {
    this.value = value;
  }

  static JsActionOriginal of(String s0) {
    return new JsActionOriginal(
        "[" + s0 + "]"
    );
  }

  static JsActionOriginal of(String s0, String s1) {
    return new JsActionOriginal(
        "[" + s0 + "," + s1 + "]"
    );
  }

  static JsActionOriginal of(JsActionOriginal first, JsActionOriginal second, JsActionOriginal[] more) {
    final StringBuilder sb;
    sb = new StringBuilder();

    sb.append(
        Objects.requireNonNull(first, "first == null")
    );

    sb.append(',');

    sb.append(
        Objects.requireNonNull(second, "second == null")
    );

    for (int idx = 0; idx < more.length; idx++) {
      final JsActionOriginal o;
      o = more[idx];

      if (o == null) {
        throw new NullPointerException("more[" + idx + "] == null");
      }

      sb.append(',');

      sb.append(o);
    }

    final String value;
    value = sb.toString();

    return new JsActionOriginal(value);
  }

  static JsActionOriginal of(JsObject v0) {
    return new JsActionOriginal(
        "[" + v0.toString() + "]"
    );
  }

  static JsActionOriginal of(JsObject v0, JsObject v1) {
    return new JsActionOriginal(
        "[" + v0.toString() + "," + v1.toString() + "]"
    );
  }

  static JsActionOriginal var(String name, JsObject value) {
    Objects.requireNonNull(name, "name == null");
    Objects.requireNonNull(value, "value == null");

    final JsString $name;
    $name = JsString.raw(name);

    final JsArray $value;
    $value = JsArray.raw(value);

    final JsArray action;
    action = JsArray.raw(CW, $name, $value);

    return of(action);
  }

  @Override
  public final String toString() {
    return value;
  }

}
