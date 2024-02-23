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
import objectos.html.HtmlTemplate;
import objectos.lang.CharWritable;
import objectos.lang.object.Check;
import objectos.util.collection.UnmodifiableIterator;
import objectos.util.list.GrowableList;

public class UiCommand implements CharWritable {

  private final GrowableList<JsonCommand> commands = new GrowableList<>();

  public UiCommand() {}

  public final UiCommand html(HtmlTemplate html) {
    Check.notNull(html, "html == null");

    commands.add(JsonCommand.html(html));

    return this;
  }

  @Override
  public final void writeTo(Appendable out) throws IOException {
    out.append('[');

    UnmodifiableIterator<JsonCommand> iter;
    iter = commands.iterator();

    if (iter.hasNext()) {
      JsonCommand command;
      command = iter.next();

      command.writeTo(out);

      while (iter.hasNext()) {
        out.append(',');

        command = iter.next();

        command.writeTo(out);
      }
    }

    out.append(']');

    out.append('\n');
  }

}