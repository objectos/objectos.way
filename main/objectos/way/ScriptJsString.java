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

import objectos.way.Script.JsString;

final class ScriptJsString extends ScriptJsObject implements Script.JsString {

  private ScriptJsString(String value) {
    super(value);
  }

  public static ScriptJsString of(String s) {
    final String v;
    v = quote(s);

    return new ScriptJsString("[\"JS\"," + v + "]");
  }

  public static ScriptJsString raw(String s) {
    final String v;
    v = quote(s);

    return new ScriptJsString(v);
  }

  static ScriptJsType<JsString> $type() {
    final ScriptJsString typeName;
    typeName = ScriptJsString.raw("string");

    return new ScriptJsType<>(typeName, ScriptJsString::new);
  }

  private static String quote(String s) {
    if (s == null) {
      throw new NullPointerException("Cannot quote a null string value");
    }

    // TODO escape json string literal
    return '"' + s + '"';
  }

}
