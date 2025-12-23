/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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

final class ScriptJsArray extends ScriptJsObject implements Script.JsArray {

  private static final Script.JsString Array = ScriptJsString.raw("Array");

  private ScriptJsArray(String value) {
    super(value);
  }

  public static ScriptJsArray array(String[] values) {
    final StringBuilder sb;
    sb = new StringBuilder();

    sb.append('[');
    sb.append('"');
    sb.append("JS");
    sb.append('"');
    sb.append(',');
    sb.append('[');

    if (values.length > 0) {
      final String v0;
      v0 = values[0];

      final ScriptJsString s0;
      s0 = ScriptJsString.raw(v0);

      sb.append(s0);

      for (int idx = 1; idx < values.length; idx++) {
        final String v;
        v = values[idx];

        final ScriptJsString s;
        s = ScriptJsString.raw(v);

        sb.append(',');

        sb.append(s);
      }
    }

    sb.append(']');
    sb.append(']');

    final String value;
    value = sb.toString();

    return new ScriptJsArray(value);
  }

  public static ScriptJsArray raw() {
    return new ScriptJsArray("[]");
  }

  public static ScriptJsArray raw(Object v0) {
    return new ScriptJsArray("[" + v0 + "]");
  }

  public static ScriptJsArray raw(Object v0, Object v1) {
    return new ScriptJsArray("[" + v0 + "," + v1 + "]");
  }

  public static ScriptJsArray raw(Object v0, Object v1, Object v2) {
    return new ScriptJsArray("[" + v0 + "," + v1 + "," + v2 + "]");
  }

  public static ScriptJsArray raw(Object v0, Object v1, Object v2, Object v3) {
    return new ScriptJsArray("[" + v0 + "," + v1 + "," + v2 + "," + v3 + "]");
  }

  public static ScriptJsArray rawArgs(Script.JsObject[] args) {
    return switch (args.length) {
      case 0 -> raw();

      default -> {
        final StringBuilder sb;
        sb = new StringBuilder();

        sb.append('[');

        final Script.JsObject args0;
        args0 = args[0];

        if (args0 == null) {
          throw new NullPointerException("args[0] == null");
        }

        final ScriptJsAction a0;
        a0 = ScriptJsAction.of(args0);

        sb.append(a0);

        for (int idx = 1; idx < args.length; idx++) {
          final Script.JsObject o;
          o = args[idx];

          if (o == null) {
            throw new NullPointerException("args[" + idx + "] == null");
          }

          sb.append(',');

          final ScriptJsAction a;
          a = ScriptJsAction.of(o);

          sb.append(a);
        }

        sb.append(']');

        final String value;
        value = sb.toString();

        yield new ScriptJsArray(value);
      }
    };
  }

  private static final Script.JsString FE = ScriptJsString.raw("FE");

  @Override
  public final Script.JsAction forEach(Script.JsAction value) {
    Objects.requireNonNull(value, "value == null");

    final Script.JsArray $value;
    $value = ScriptJsArray.raw(value);

    final Script.JsArray forEach;
    forEach = ScriptJsArray.raw(FE, Array, $value);

    return ScriptJsAction.of(this, forEach);
  }

}
