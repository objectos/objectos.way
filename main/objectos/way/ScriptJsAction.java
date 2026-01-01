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
package objectos.way;

import java.util.Objects;

final class ScriptJsAction implements Script.JsAction {

  private static final Script.JsString CW = ScriptJsString.raw("CW");

  private final String value;

  private ScriptJsAction(String value) {
    this.value = value;
  }

  static ScriptJsAction of(String s0) {
    return new ScriptJsAction(
        "[" + s0 + "]"
    );
  }

  static ScriptJsAction of(String s0, String s1) {
    return new ScriptJsAction(
        "[" + s0 + "," + s1 + "]"
    );
  }

  static ScriptJsAction of(Script.JsAction first, Script.JsAction second, Script.JsAction[] more) {
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
      final Script.JsAction o;
      o = more[idx];

      if (o == null) {
        throw new NullPointerException("more[" + idx + "] == null");
      }

      sb.append(',');

      sb.append(o);
    }

    final String value;
    value = sb.toString();

    return new ScriptJsAction(value);
  }

  public static ScriptJsAction of(Script.JsObject v0) {
    return new ScriptJsAction(
        "[" + v0.toString() + "]"
    );
  }

  public static ScriptJsAction of(Script.JsObject v0, Script.JsObject v1) {
    return new ScriptJsAction(
        "[" + v0.toString() + "," + v1.toString() + "]"
    );
  }

  static ScriptJsAction var(String name, Script.JsObject value) {
    Objects.requireNonNull(name, "name == null");
    Objects.requireNonNull(value, "value == null");

    final Script.JsString $name;
    $name = ScriptJsString.raw(name);

    final Script.JsArray $value;
    $value = ScriptJsArray.raw(value);

    final Script.JsArray action;
    action = ScriptJsArray.raw(CW, $name, $value);

    return of(action);
  }

  @Override
  public final String toString() {
    return value;
  }

}
