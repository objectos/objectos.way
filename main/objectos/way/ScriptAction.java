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

non-sealed abstract class ScriptAction implements Script.Action {

  static final String CMD = "cmd";

  @Override
  public final String toString() {
    StringBuilder json;
    json = new StringBuilder();

    json.append('[');

    writeTo(json);

    json.append(']');

    return json.toString();
  }

  abstract void writeTo(StringBuilder json);

  final void actions(StringBuilder json, Script.Action[] actions) {
    if (actions.length > 0) {
      ScriptAction action;
      action = Script.cast(actions[0]);

      action.writeTo(json);

      for (int idx = 1; idx < actions.length; idx++) {
        json.append(',');

        action = Script.cast(actions[idx]);

        action.writeTo(json);
      }
    }
  }

  final void arrayEnd(StringBuilder json) {
    json.append(']');
  }

  final void arrayStart(StringBuilder json) {
    json.append('[');
  }

  final void comma(StringBuilder json) {
    json.append(',');
  }

  final void objectEnd(StringBuilder json) {
    json.append('}');
  }

  final void objectStart(StringBuilder json) {
    json.append('{');
  }

  final void property(StringBuilder json, String name, int value) {
    propertyStart(json, name);
    json.append(value);
  }

  final void property(StringBuilder json, String name, Script.Action[] actions) {
    propertyStart(json, name);
    json.append('[');
    actions(json, actions);
    json.append(']');
  }

  final void property(StringBuilder json, String name, String value) {
    propertyStart(json, name);
    stringLiteral(json, value);
  }

  final void propertyStart(StringBuilder json, String name) {
    json.append('"');
    json.append(name);
    json.append('"');
    json.append(':');
  }

  final void stringLiteral(StringBuilder json, String s) {
    json.append('"');
    // TODO escape json string literal
    json.append(s);
    json.append('"');
  }

}