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

/// Represents an action to be executed by the JS runtime.
public final class JsAction {

  private static final JsString CW = JsString.raw("CW");

  private final String value;

  private JsAction(String value) {
    this.value = value;
  }

  static JsAction of(String s0) {
    return new JsAction(
        "[" + s0 + "]"
    );
  }

  static JsAction of(String s0, String s1) {
    return new JsAction(
        "[" + s0 + "," + s1 + "]"
    );
  }

  static JsAction of(JsAction first, JsAction second, JsAction[] more) {
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
      final JsAction o;
      o = more[idx];

      if (o == null) {
        throw new NullPointerException("more[" + idx + "] == null");
      }

      sb.append(',');

      sb.append(o);
    }

    final String value;
    value = sb.toString();

    return new JsAction(value);
  }

  static JsAction of(JsObject v0) {
    return new JsAction(
        "[" + v0.toString() + "]"
    );
  }

  static JsAction of(JsObject v0, JsObject v1) {
    return new JsAction(
        "[" + v0.toString() + "," + v1.toString() + "]"
    );
  }

  static JsAction var(String name, JsObject value) {
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
