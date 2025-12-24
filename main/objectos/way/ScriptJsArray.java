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

  private enum Kind {

    JS("""
    ["JS",[\
    """, """
    ]]\
    """),

    RAW("[", "]");

    final String header;

    final String trailer;

    private Kind(String header, String trailer) {
      this.header = header;

      this.trailer = trailer;
    }

  }

  static final class Builder {

    private final Kind kind;

    private final StringBuilder sb = new StringBuilder();

    private Builder(Kind kind) {
      this.kind = kind;

      sb.append(kind.header);
    }

    public final ScriptJsArray build() {
      sb.append(kind.trailer);

      final String value;
      value = sb.toString();

      return new ScriptJsArray(value);
    }

    public final void raw(String s, String name) {
      if (s == null) {
        throw new NullPointerException(name + " == null");
      }

      final ScriptJsString raw;
      raw = ScriptJsString.raw(s);

      add(raw);
    }

    public final void rawAll(String[] values, String name) {
      if (values == null) {
        throw new NullPointerException(name + " == null");
      }

      for (int idx = 0; idx < values.length; idx++) {
        final String v;
        v = values[idx];

        if (v == null) {
          throw new NullPointerException(name + "[" + idx + "] == null");
        }

        final ScriptJsString raw;
        raw = ScriptJsString.raw(v);

        add(raw);
      }
    }

    private void add(Object o) {
      final int len;
      len = sb.length();

      final int startLen;
      startLen = kind.header.length();

      if (len > startLen) {
        sb.append(',');
      }

      sb.append(o);
    }

  }

  private static final Script.JsString Array = ScriptJsString.raw("Array");

  private ScriptJsArray(String value) {
    super(value);
  }

  public static Builder jsBuilder() {
    return new Builder(Kind.JS);
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
