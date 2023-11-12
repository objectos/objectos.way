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

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import objectos.core.object.Check;
import objectos.css.util.IdSelector;
import objectos.html.BaseTemplateDsl;
import objectos.html.HtmlComponent;
import objectos.html.tmpl.Api.ElementContents;
import objectos.html.tmpl.Api.ExternalAttribute.StyleClass;

public final class WayJs extends HtmlComponent {

  public WayJs(BaseTemplateDsl parent) {
    super(parent);
  }

  public static byte[] getBytes() throws IOException {
    URL resource;
    resource = WayJs.class.getResource("objectos.way.js");

    if (resource == null) {
      throw new FileNotFoundException();
    }

    try (InputStream in = resource.openStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream()) {
      in.transferTo(out);

      return out.toByteArray();
    }
  }

  public final ElementContents click(Command... commands) {
    Check.notNull(commands, "commands == null");

    StringBuilder json;
    json = new StringBuilder();

    json.append('[');

    if (commands.length > 0) {
      Command c;
      c = commands[0];

      c.acceptJsonBuilder(json);

      for (int i = 1; i < commands.length; i++) {
        json.append(',');

        c = commands[i];

        c.acceptJsonBuilder(json);
      }
    }

    json.append(']');

    return dataWayClick(json.toString());
  }

  public final Command replaceClass(IdSelector id, StyleClass from, StyleClass to) {
    Check.notNull(id, "id == null");
    Check.notNull(from, "from == null");
    Check.notNull(to, "to == null");

    return new Command.ReplaceClass(id, from, to);
  }

}