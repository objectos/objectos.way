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
package objectos.ui;

import java.io.IOException;
import objectos.html.ElementId;
import objectos.html.HtmlTemplate;
import objectos.html.SingleQuotedValue;

final class UiCommands {

  private UiCommands() {}

  public static SingleQuotedValue attributeValue(UiCommand[] commands) {
    try {
      return attributeValue0(commands);
    } catch (IOException e) {
      throw new AssertionError("StringBuilder does not throw IOException", e);
    }
  }

  private static SingleQuotedValue attributeValue0(UiCommand[] commands) throws IOException {
    record AttributeValue(String value) implements SingleQuotedValue {
      @Override
      public final String toString() {
        return value;
      }
    }

    StringBuilder out;
    out = new StringBuilder();

    out.append('[');

    if (commands.length > 0) {
      UiCommand command;
      command = commands[0];

      command.writeTo(out);

      for (int idx = 1; idx < commands.length; idx++) {
        out.append(',');

        command = commands[idx];

        command.writeTo(out);
      }
    }

    out.append(']');

    out.append('\n');

    String value;
    value = out.toString();

    return new AttributeValue(value);
  }

  public static UiCommand html(HtmlTemplate html) {
    return new UiCommand() {
      @Override
      public final void writeTo(Appendable out) throws IOException {
        out.append('{');
        writeCommandName(out, "html");
        out.append(',');
        writeStringLiteral(out, "value");
        out.append(':');
        out.append('"');
        JsonHtmlFormatter.INSTANCE.formatTo(html, out);
        out.append('"');
        out.append('}');
      }
    };
  }

  public static UiCommand replaceClass(ElementId id, String from, String to) {
    return new UiCommand() {
      @Override
      public final void writeTo(Appendable out) throws IOException {
        out.append('{');

        writeCommandName(out, "replace-class");

        out.append(',');

        writeArgs(out, id.id(), from, to);

        out.append('}');
      }
    };
  }

  public static UiCommand submit(ElementId id) {
    return new UiCommand() {
      @Override
      public final void writeTo(Appendable out) throws IOException {
        out.append('{');

        writeCommandName(out, "submit");

        out.append(',');

        writeStringLiteral(out, "id");

        out.append(':');

        writeStringLiteral(out, id.id());

        out.append('}');
      }
    };
  }

  static void writeArgs(Appendable json, String s0, String s1, String s2) throws IOException {
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

  static void writeCommandName(Appendable json, String value) throws IOException {
    writeStringLiteral(json, "cmd");
    json.append(':');
    writeStringLiteral(json, value);
  }

  static void writeStringLiteral(Appendable json, String value) throws IOException {
    json.append('"');
    json.append(value);
    json.append('"');
  }

}