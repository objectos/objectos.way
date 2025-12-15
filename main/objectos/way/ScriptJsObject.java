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

final class ScriptJsObject
    implements
    Script.Action,
    Script.JsElement {

  static final ScriptJsObject TARGET = new ScriptJsObject("""
  ["LO","TT"]""");

  private final String value;

  ScriptJsObject(String value) {
    this.value = value;
  }

  @Override
  public final Script.Action invoke(String type, String method, Object... args) {
    final String $type;
    $type = escape(type, "type");

    final String $method;
    $method = escape(method, "method");

    final String $args;
    $args = args(args);

    final String invoke;
    invoke = """
    %s,["IV","%s","%s",%s]""".formatted(value, $type, $method, $args);

    return new ScriptJsObject(invoke);
  }

  @Override
  public final Script.JsObject prop(String type, String prop) {
    final String $type;
    $type = escape(type, "type");

    final String $prop;
    $prop = escape(prop, "prop");

    final String read;
    read = """
    %s,["RP","%s","%s"]""".formatted(value, $type, $prop);

    return new ScriptJsObject(read);
  }

  @Override
  public final String toString() {
    return "[" + value + "]";
  }

  private String args(Object[] args) {
    return args.length == 0
        ? "[]"
        : args0(args);
  }

  private String args0(Object[] args) {
    final StringBuilder sb;
    sb = new StringBuilder();

    sb.append('[');

    final Object arg0;
    arg0 = args[0];

    final String first;
    first = obj(arg0);

    sb.append(first);

    for (int idx = 1; idx < args.length; idx++) {
      sb.append(',');

      final Object arg;
      arg = args[idx];

      final String obj;
      obj = obj(arg);

      sb.append(obj);
    }

    sb.append(']');

    return sb.toString();
  }

  private String escape(String value, String name) {
    if (value == null) {
      throw new NullPointerException(
          name + " == null"
      );
    }

    return escape0(value);
  }

  private String escape0(String value) {
    // TODO escape json string literal
    return value;
  }

  private String obj(Object o) {
    return switch (o) {
      case String s -> """
                       ["JS","%s"]""".formatted(escape0(s));

      case null -> "null";

      default -> {
        final Class<?> type;
        type = o.getClass();

        final String typeName;
        typeName = type.getName();

        throw new IllegalArgumentException("""
        Cannot convert %s to a JS object
        """.formatted(typeName));
      }
    };
  }

}
