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

import java.util.function.Function;

final class ScriptJsArray {

  static final class Builder {

    private final StringBuilder sb;

    public Builder() {
      sb = new StringBuilder();

      sb.append('[');
    }

    public final ScriptJsArray build() {
      final String s;
      s = buildString();

      return new ScriptJsArray(s);
    }

    public final <T> T build(Function<String, T> constructor) {
      final String s;
      s = buildString();

      return constructor.apply(s);
    }

    public final String buildString() {
      sb.append(']');

      return sb.toString();
    }

    public final void jsNull() {
      raw("null");
    }

    public final void jsString(String s, String name) {
      final String literal;
      literal = ScriptJsString.jsLiteral(s, name);

      commaIfNecessary();

      sb.append(literal);
    }

    public final void raw(Object s) {
      commaIfNecessary();

      sb.append(s);
    }

    public final void raw(Object s, String name) {
      if (s == null) {
        throw new NullPointerException(name + " == null");
      }

      commaIfNecessary();

      sb.append(s);
    }

    public final void raw(Object s, String name, int idx) {
      if (s == null) {
        throw new NullPointerException(name + "[" + idx + "] == null");
      }

      commaIfNecessary();

      sb.append(s);
    }

    public final void rawString(String s) {
      commaIfNecessary();

      sb.append('"');

      sb.append(s);

      sb.append('"');
    }

    public final void way(Object o, String name) {
      switch (o) {
        case null -> wayNull();

        case ScriptJsObject obj -> wayObject(obj);

        case String s -> wayString(s, name);

        default -> {
          final Class<?> type;
          type = o.getClass();

          final String typeName;
          typeName = type.getName();

          throw new IllegalArgumentException("""
          Cannot convert %s to a JS object
          """.formatted(typeName));
        }
      }
    }

    public final void wayNull() {
      commaIfNecessary();

      sb.append("[\"JS\",null]");
    }

    public final void wayObject(ScriptJsObject o) {
      commaIfNecessary();

      sb.append(o.wayLiteral());
    }

    public final void wayString(String s, String name) {
      final String literal;
      literal = ScriptJsString.wayLiteral(s, name);

      commaIfNecessary();

      sb.append(literal);
    }

    private void commaIfNecessary() {
      if (sb.length() > 1) {
        sb.append(',');
      }
    }

  }

  private final String value;

  private ScriptJsArray(String value) {
    this.value = value;
  }

  public static ScriptJsArray of(Object[] values, String name) {
    if (values == null) {
      throw new NullPointerException(name + " == null");
    }

    final Builder builder;
    builder = new Builder();

    for (int idx = 0; idx < values.length; idx++) {
      final Object o;
      o = values[idx];

      builder.way(o, name + "[" + idx + "]");
    }

    return builder.build();
  }

  @Override
  public final String toString() {
    return value;
  }

}
