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

final class ScriptJsString extends ScriptJsObject implements Script.JsString {

  ScriptJsString(String value) {
    super(value);
  }

  public static ScriptJsString of(String s, String name, int idx) {
    if (s == null) {
      throw new NullPointerException(name + "[" + idx + "] == null");
    }

    // TODO escape json string literal
    return new ScriptJsString('"' + s + '"');
  }

  public static String jsLiteral(String value, String name) {
    if (value == null) {
      throw new NullPointerException(name + " == null");
    }

    // TODO escape json string literal
    return '"' + value + '"';
  }

  public static String wayLiteral(String s, String name) {
    final String js;
    js = jsLiteral(s, name);

    return "[\"JS\"," + js + "]";
  }

}
