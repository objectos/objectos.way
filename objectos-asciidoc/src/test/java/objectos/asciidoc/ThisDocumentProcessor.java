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
import objectos.asciidoc.pseudom.Header;
import objectos.asciidoc.pseudom.Heading;
import objectos.asciidoc.pseudom.Node;
import objectos.asciidoc.pseudom.Paragraph;
import objectos.asciidoc.pseudom.Section;
import objectos.asciidoc.pseudom.Text;

final class ThisDocumentProcessor {

  private final StringBuilder out = new StringBuilder();

  public final String process(Document document) throws IOException {
    out.setLength(0);

    out.append("<document>\n");

    for (var node : document.nodes()) {
      node(node);
    }

    out.append("</document>\n");

    return toString();
  }

  @Override
  public final String toString() {
    return out.toString();
  }

  private void header(Header header) throws IOException {
    for (var node : header.nodes()) {
      node(node);
    }
  }

  private void heading(Heading heading) throws IOException {
    out.append("<title>");

    for (var node : heading.nodes()) {
      node(node);
    }

    out.append("</title>\n");
  }

  private void node(Node node) throws IOException {
    if (node instanceof Header header) {
      header(header);
    } else if (node instanceof Paragraph paragraph) {
      paragraph(paragraph);
    } else if (node instanceof Section section) {
      section(section);
    } else if (node instanceof Text text) {
      text.appendTo(out);
    } else if (node instanceof Heading title) {
      heading(title);
    } else {
      throw new UnsupportedOperationException(
        "Implement me :: type=" + node.getClass().getSimpleName()
      );
    }
  }

  private void paragraph(Paragraph paragraph) throws IOException {
    out.append("<p>");

    for (var node : paragraph.nodes()) {
      node(node);
    }

    out.append("</p>\n");
  }

  private void section(Section section) throws IOException {
    int level = section.level();

    if (!"<document>\n".contentEquals(out)) {
      out.append('\n');
    }

    out.append("<section level=\"");
    out.append(level);
    out.append("\">\n");

    var attributes = section.attributes();

    out.append("<style>");
    out.append(attributes.getOrDefault("style", "null"));
    out.append("</style>\n");

    for (var node : section.nodes()) {
      node(node);
    }

    out.append("</section>\n");
  }

}