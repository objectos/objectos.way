/*
 * Copyright (C) 2021-2023 Objectos Software LTDA.
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
package objectos.asciidoc;

import java.io.IOException;
import objectos.asciidoc.pseudom.Document;
import objectos.asciidoc.pseudom.Heading;
import objectos.asciidoc.pseudom.Node;
import objectos.asciidoc.pseudom.Text;

final class ThisDocumentProcessor implements Document.Processor {

  private final StringBuilder out = new StringBuilder();

  @Override
  public final void process(Document document) throws IOException {
    out.setLength(0);

    out.append("""
    <div id="header">
    """);

    while (document.hasNext()) {
      var node = document.next();

      if (node instanceof Heading heading) {
        heading(heading);

        out.append("""
        </div>
        <div id="content">
        """);
      } else {
        throw new UnsupportedOperationException(
          "Implement me :: type=" + node.getClass().getSimpleName()
        );
      }
    }

    // end content
    out.append("""
    </div>
    """);
  }

  @Override
  public final String toString() {
    return out.toString();
  }

  private void heading(Heading heading) throws IOException {
    int level = heading.level() + 1;

    out.append("<h");
    out.append(level);
    out.append(">");

    while (heading.hasNext()) {
      var node = heading.next();

      node(node);
    }

    out.append("</h");
    out.append(level);
    out.append(">");
    out.append('\n');
  }

  private void node(Node node) throws IOException {
    if (node instanceof Text text) {
      text.appendTo(out);
    } else {
      throw new UnsupportedOperationException(
        "Implement me :: type=" + node.getClass().getSimpleName()
      );
    }
  }

}