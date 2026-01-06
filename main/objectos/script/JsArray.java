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

/// Represents a JS runtime `Array` instance.
public final class JsArray extends JsObject {

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

    public final JsArray build() {
      sb.append(kind.trailer);

      final String value;
      value = sb.toString();

      return new JsArray(value);
    }

    public final void raw(String s, String name) {
      if (s == null) {
        throw new NullPointerException(name + " == null");
      }

      final JsString raw;
      raw = JsString.raw(s);

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

        final JsString raw;
        raw = JsString.raw(v);

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

  private JsArray(String value) {
    super(value);
  }

  static Builder jsBuilder() {
    return new Builder(Kind.JS);
  }

  static JsArray raw() {
    return new JsArray("[]");
  }

  static JsArray raw(Object v0) {
    return new JsArray("[" + v0 + "]");
  }

  static JsArray raw(Object v0, Object v1) {
    return new JsArray("[" + v0 + "," + v1 + "]");
  }

  static JsArray raw(Object v0, Object v1, Object v2) {
    return new JsArray("[" + v0 + "," + v1 + "," + v2 + "]");
  }

  static JsArray raw(Object v0, Object v1, Object v2, Object v3) {
    return new JsArray("[" + v0 + "," + v1 + "," + v2 + "," + v3 + "]");
  }

  static JsArray rawArgs(JsObject[] args) {
    return switch (args.length) {
      case 0 -> raw();

      default -> {
        final StringBuilder sb;
        sb = new StringBuilder();

        sb.append('[');

        final JsObject args0;
        args0 = args[0];

        if (args0 == null) {
          throw new NullPointerException("args[0] == null");
        }

        sb.append(args0);

        for (int idx = 1; idx < args.length; idx++) {
          final JsObject o;
          o = args[idx];

          if (o == null) {
            throw new NullPointerException("args[" + idx + "] == null");
          }

          sb.append(',');

          sb.append(o);
        }

        sb.append(']');

        final String value;
        value = sb.toString();

        yield new JsArray(value);
      }
    };
  }

  public final JsAction forEach(JsAction value) {
    Objects.requireNonNull(value, "value == null");

    final JsOp op;
    op = JsOp.of(JsString.FE, JsString.Array, value);

    return action(op);
  }

}
