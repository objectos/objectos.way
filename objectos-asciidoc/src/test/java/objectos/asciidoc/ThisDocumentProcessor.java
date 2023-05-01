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
import objectos.asciidoc.pseudom.Node;
import objectos.asciidoc.pseudom.Node.Header;
import objectos.asciidoc.pseudom.Node.InlineMacro;
import objectos.asciidoc.pseudom.Node.ListItem;
import objectos.asciidoc.pseudom.Node.Monospaced;
import objectos.asciidoc.pseudom.Node.Paragraph;
import objectos.asciidoc.pseudom.Node.Section;
import objectos.asciidoc.pseudom.Node.Text;
import objectos.asciidoc.pseudom.Node.Title;
import objectos.asciidoc.pseudom.Node.UnorderedList;

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

  private void inlineMacro(InlineMacro macro) throws IOException {
    var name = macro.name();

    switch (name) {
      case "https" -> urlMacro(name, macro);

      default -> throw new UnsupportedOperationException(
        "Implement me :: name=" + name
      );
    }
  }

  private void listItem(ListItem item) throws IOException {
    out.append("<item>\n");

    for (var node : item.nodes()) {
      if (node instanceof Text text) {
        out.append("<text>");
        text.appendTo(out);
        out.append("</text>\n");
      } else {
        node(node);
      }
    }

    out.append("</item>\n");
  }

  private void monospaced(Monospaced monospaced) throws IOException {
    out.append("<code>");

    for (var node : monospaced.nodes()) {
      node(node);
    }

    out.append("</code>");
  }

  private void node(Node node) throws IOException {
    if (node instanceof Header header) {
      header(header);
    } else if (node instanceof InlineMacro macro) {
      inlineMacro(macro);
    } else if (node instanceof ListItem listItem) {
      listItem(listItem);
    } else if (node instanceof Monospaced monospaced) {
      monospaced(monospaced);
    } else if (node instanceof Paragraph paragraph) {
      paragraph(paragraph);
    } else if (node instanceof Section section) {
      section(section);
    } else if (node instanceof Text text) {
      text.appendTo(out);
    } else if (node instanceof Title title) {
      title(title);
    } else if (node instanceof UnorderedList list) {
      unorderedList(list);
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

  private void title(Title title) throws IOException {
    out.append("<title>");

    for (var node : title.nodes()) {
      node(node);
    }

    out.append("</title>\n");
  }

  private void unorderedList(UnorderedList list) throws IOException {
    out.append("<unordered-list>\n");

    for (var node : list.nodes()) {
      node(node);
    }

    out.append("</unordered-list>\n");
  }

  private void urlMacro(String name, InlineMacro macro) throws IOException {
    out.append("<a href=\"");
    out.append(name);
    out.append("://");
    macro.targetTo(out);
    out.append("\">");

    for (var node : macro.nodes()) {
      node(node);
    }

    out.append("</a>");
  }

}