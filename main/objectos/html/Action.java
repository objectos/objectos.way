/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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
package objectos.html;

import objectos.html.Api.ExternalAttribute;
import objectos.lang.object.Check;

public abstract class Action {

  Action() {}

  public static Action delay(int ms, Action... actions) {
    Check.argument(ms >= 0, "ms must not be negative");
    Check.notNull(actions, "actions == null");

    return new Action() {
      @Override
      final void writeTo(StringBuilder json) {
        json.append('{');
        
        writeCommandName(json, "delay");
        
        json.append(',');
        
        writeStringLiteral(json, "ms");
        json.append(':');
        json.append(ms);
        
        json.append(',');
        
        writeStringLiteral(json, "actions");
        json.append(':');
        writeActions(json, actions);
        
        json.append('}');
      }
    };
  }
  
  public static Action replaceClass(ExternalAttribute.Id id,
                                    ExternalAttribute.StyleClass from,
                                    ExternalAttribute.StyleClass to) {
    Check.notNull(id, "id == null");
    Check.notNull(from, "from == null");
    Check.notNull(to, "to == null");

    return new Action() {
      @Override
      final void writeTo(StringBuilder json) {
        json.append('{');
        writeCommandName(json, "replace-class");
        json.append(',');
        writeStringLiteral(json, "args");
        json.append(':');
        json.append('[');
        writeStringLiteral(json, id.id());
        json.append(',');
        writeStringLiteral(json, from.className());
        json.append(',');
        writeStringLiteral(json, to.className());
        json.append(']');
        json.append('}');
      }
    };
  }

  public static Action submit(ExternalAttribute.Id id) {
    Check.notNull(id, "id == null");

    return new Action() {
      @Override
      final void writeTo(StringBuilder json) {
        json.append('{');
        writeCommandName(json, "submit");
        json.append(',');
        writeStringLiteral(json, "id");
        json.append(':');
        writeStringLiteral(json, id.id());
        json.append('}');
      }
    };
  }

  static SingleQuotedValue join(Action[] actions) {
    record AttributeValue(String value) implements SingleQuotedValue {
      @Override
      public final String toString() {
        return value;
      }
    }

    StringBuilder json;
    json = new StringBuilder();

    writeActions(json, actions);

    String value;
    value = json.toString();

    return new AttributeValue(value);
  }

  private static void writeActions(StringBuilder json, Action[] actions) {
    json.append('[');

    if (actions.length > 0) {
      Action action;
      action = actions[0];

      action.writeTo(json);

      for (int idx = 1; idx < actions.length; idx++) {
        json.append(',');

        action = actions[idx];

        action.writeTo(json);
      }
    }

    json.append(']');
  }

  abstract void writeTo(StringBuilder json);

  final void writeCommandName(StringBuilder json, String value) {
    writeStringLiteral(json, "cmd");
    json.append(':');
    writeStringLiteral(json, value);
  }

  final void writeArgs(StringBuilder json, String s0, String s1) {
    writeStringLiteral(json, "args");
    json.append(':');
    json.append('[');
    writeStringLiteral(json, s0);
    json.append(',');
    writeStringLiteral(json, s1);
    json.append(']');
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

  static final class ReplaceClass extends Action {

    final ExternalAttribute.Id id;
    final ExternalAttribute.StyleClass from;
    final ExternalAttribute.StyleClass to;

    public ReplaceClass(ExternalAttribute.Id id,
                        ExternalAttribute.StyleClass from,
                        ExternalAttribute.StyleClass to) {
      this.id = id;
      this.from = from;
      this.to = to;
    }

    @Override
    final void writeTo(StringBuilder json) {
      json.append('{');

      writeCommandName(json, "replace-class");

      json.append(',');

      writeArgs(json, id.id(), from.className(), to.className());

      json.append('}');
    }

  }


  }