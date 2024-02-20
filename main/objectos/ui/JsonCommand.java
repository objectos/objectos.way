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
import objectos.css.select.IdSelector;
import objectos.html.HtmlTemplate;

abstract class JsonCommand {

  public static JsonCommand html(HtmlTemplate html) {
    return new JsonCommand() {
      @Override
      final void writeTo(Appendable out) throws IOException {
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

  public static JsonCommand locationHref(String location) {
    return new JsonCommand() {
      @Override
      final void writeTo(Appendable out) throws IOException {
        out.append('{');
        writeCommandName(out, "location-href");
        out.append(',');
        writeStringLiteral(out, "value");
        out.append(':');
        writeStringLiteral(out, location);
        out.append('}');
      }
    };
  }

  public static JsonCommand replace(IdSelector id) {
    return new JsonCommand() {
      @Override
      final void writeTo(Appendable out) throws IOException {
        out.append('{');
        writeCommandName(out, "replace");
        out.append(',');
        writeStringLiteral(out, "id");
        out.append(':');
        writeStringLiteral(out, id.id());
        out.append('}');
      }
    };
  }

  abstract void writeTo(Appendable out) throws IOException;

  final void writeCommandName(Appendable json, String value) throws IOException {
    writeStringLiteral(json, "cmd");
    json.append(':');
    writeStringLiteral(json, value);
  }

  final void writeStringLiteral(Appendable json, String value) throws IOException {
    json.append('"');
    json.append(value);
    json.append('"');
  }

}