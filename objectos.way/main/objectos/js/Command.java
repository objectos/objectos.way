/*
 * Copyright (C) 2023 Objectos Software LTDA.
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
package objectos.js;

import objectos.css.util.IdSelector;
import objectos.html.tmpl.Api.ExternalAttribute.StyleClass;

public sealed abstract class Command {

  Command() {}

  abstract void acceptJsonBuilder(StringBuilder json);

  final void writeCommandName(StringBuilder json, String value) {
    writeStringLiteral(json, "cmd");
    json.append(':');
    writeStringLiteral(json, value);
  }

  final void writeArgs(StringBuilder json, String s0, String s1, String s2) {
    writeStringLiteral(json, "args");
    json.append(':');
    json.append('[');
    writeStringLiteral(json, s0);
    json.append(',');
    writeStringLiteral(json, s1);
    json.append(',');
    writeStringLiteral(json, s2);
    json.append(']');
  }

  final void writeStringLiteral(StringBuilder json, String value) {
    json.append('"');
    json.append(value);
    json.append('"');
  }

  static final class ReplaceClass extends Command {

    final IdSelector id;
    final StyleClass from;
    final StyleClass to;

    public ReplaceClass(IdSelector id, StyleClass from, StyleClass to) {
      this.id = id;
      this.from = from;
      this.to = to;
    }

    @Override
    final void acceptJsonBuilder(StringBuilder json) {
      json.append('{');

      writeCommandName(json, "replace-class");

      json.append(',');

      writeArgs(json, id.id(), from.className(), to.className());

      json.append('}');
    }

  }

}