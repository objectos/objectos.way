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

import java.util.Map;
import org.asciidoctor.ast.Block;
import org.asciidoctor.ast.ContentNode;
import org.asciidoctor.ast.Document;
import org.asciidoctor.ast.List;
import org.asciidoctor.ast.ListItem;
import org.asciidoctor.ast.PhraseNode;
import org.asciidoctor.ast.Section;
import org.asciidoctor.ast.StructuralNode;
import org.asciidoctor.converter.ConverterFor;
import org.asciidoctor.converter.StringConverter;
import org.asciidoctor.log.LogRecord;
import org.asciidoctor.log.Severity;

@ConverterFor("tester")
public class ThisDoctorConverter extends StringConverter {

  public ThisDoctorConverter(String backend, Map<String, Object> opts) {
    super(backend, opts);
  }

  @SuppressWarnings("exports")
  @Override
  public String convert(
      ContentNode node, String transform, Map<Object, Object> o) {

    var out = new StringBuilder();

    if (transform == null) {
      transform = node.getNodeName();
    }

    if (node instanceof Document document) {
      out.append("<document>\n");

      var doctitle = document.getAttribute("doctitle");

      var title = document.getDoctitle();

      if (doctitle != null && title != null) {
        out.append("<title>");
        out.append(title);
        out.append("</title>\n");
      }

      var content = (String) document.getContent();

      content = content.replaceAll("\n\n<p>", "\n<p>");

      content = content.replaceAll("\n\n<section", "\n<section");

      content = content.replaceAll("\n\n<unordered-list", "\n<unordered-list");

      out.append(content);
      out.append("</document>\n");
    } else if (node instanceof Section section) {
      out.append("<section level=\"");
      out.append(section.getLevel());
      out.append("\">\n");

      out.append("<style>");
      out.append(section.getAttribute("style", "null"));
      out.append("</style>\n");

      out.append("<title>");
      out.append(section.getTitle());
      out.append("</title>\n");

      out.append(section.getContent());
      out.append("</section>\n");
    } else if (transform.equals("inline_anchor")) {
      urlMacro(out, node);
    } else if (transform.equals("inline_quoted")) {
      var phrase = (PhraseNode) node;

      var type = phrase.getType();

      switch (type) {
        case "monospaced" -> monospaced(out, phrase);

        default -> {
          var log = new LogRecord(
            Severity.WARN,
            "Unexpected phrase: type=" + type
          );

          log(log);
        }
      }
    } else if (transform.equals("listing")) {
      listing(out, node);
    } else if (transform.equals("paragraph")) {
      paragraph(out, node);
    } else if (transform.equals("preamble")) {
      StructuralNode block = (StructuralNode) node;
      out.append(block.getContent());
    } else if (transform.equals("ulist")) {
      out.append("<unordered-list>\n");

      List list = (List) node;

      for (var item : list.getItems()) {
        if (item instanceof ListItem listItem) {
          out.append("<item>\n");

          if (listItem.hasText()) {
            out.append("<text>");
            out.append(listItem.getText());
            out.append("</text>\n");
          }

          out.append(listItem.getContent());

          out.append("</item>\n");
        } else {
          out.append("node name: ");
          out.append(item.getNodeName());
          out.append('\n');
        }
      }

      out.append("</unordered-list>\n");
    } else {
      var type = node.getClass();

      var log = new LogRecord(
        Severity.WARN,
        "Unexpected node: type=" + type + "; transform=" + transform
      );

      log(log);
    }

    return out.toString();
  }

  private void listing(StringBuilder out, ContentNode node) {
    var block = (Block) node;

    out.append("<listing>\n");

    out.append("<style>");
    out.append(block.getAttribute("style", "null"));
    out.append("</style>\n");

    out.append("<pre>");
    out.append(block.getContent());
    out.append("</pre>\n");

    out.append("</listing>\n");
  }

  private void monospaced(StringBuilder out, PhraseNode node) {
    out.append("<code>");
    out.append(node.getText());
    out.append("</code>");
  }

  private void paragraph(StringBuilder out, ContentNode node) {
    var block = (StructuralNode) node;

    out.append("<p>");
    out.append(block.getContent());
    out.append("</p>\n");
  }

  private void urlMacro(StringBuilder out, ContentNode node) {
    var phrase = (PhraseNode) node;

    out.append("<a href=\"");
    out.append(phrase.getTarget());
    out.append("\">");
    out.append(phrase.getText());
    out.append("</a>");
  }

}