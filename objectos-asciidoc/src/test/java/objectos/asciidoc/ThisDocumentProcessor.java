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
import objectos.asciidoc.pseudom.Text;

final class ThisDocumentProcessor {

  private static final int START = 0;

  private static final int HEADER = 1 << 0;

  private static final int PREAMBLE = 1 << 1;

  private static final int CONTENT = 1 << 2;

  private final StringBuilder contentOut = new StringBuilder();

  private final StringBuilder headerOut = new StringBuilder();

  private final StringBuilder preambleOut = new StringBuilder();

  private StringBuilder out;

  private int state;

  public final String process(Document document) throws IOException {
    contentOut.setLength(0);

    headerOut.setLength(0);

    preambleOut.setLength(0);

    state = START;

    for (var node : document.nodes()) {
      if (node instanceof Header header) {
        out = headerOut;

        state = state | HEADER;

        header(header);
      } else if (node instanceof Paragraph paragraph) {
        out = preambleOut;

        state = state | PREAMBLE;

        paragraph(paragraph);
      } else {
        throw new UnsupportedOperationException(
          "Implement me :: type=" + node.getClass().getSimpleName()
        );
      }

      out = null;
    }

    return toString();
  }

  @Override
  public final String toString() {
    return switch (state) {
      case HEADER -> """
        <div id="header">
        %s</div>
        <div id="content">

        </div>
        """.formatted(headerOut);

      case PREAMBLE -> """
        <div id="header">
        </div>
        <div id="content">
        %s</div>
        """.formatted(preambleOut);

      case CONTENT -> """
        <div id="header">
        </div>

        <div id="content">
        %s
        </div>
        """.formatted(contentOut);

      case HEADER | PREAMBLE -> """
        <div id="header">
        %s
        </div>

        <div id="content">
        %s
        </div>
        """.formatted(headerOut, preambleOut);

      case HEADER | PREAMBLE | CONTENT -> """
        <div id="header">
        %s
        </div>
        <div id="content">
        <div id="preamble">
        <div class="sectionbody">
        %s
        </div>
        </div>
        %s
        </div>
        """.formatted(headerOut, preambleOut, contentOut);

      case PREAMBLE | CONTENT -> """
        <div id="header">
        </div>
        <div id="content">
        %s
        %s
        </div>
        """.formatted(preambleOut, contentOut);

      default -> throw new UnsupportedOperationException(
        "Implement me :: state=" + Integer.toBinaryString(state));
    };
  }

  private void header(Header header) throws IOException {
    for (var node : header.nodes()) {
      if (node instanceof Heading heading) {
        heading(heading);
      } else {
        throw new UnsupportedOperationException(
          "Implement me :: type=" + node.getClass().getSimpleName()
        );
      }
    }
  }

  private void heading(Heading heading) throws IOException {
    int level = heading.level() + 1;

    out.append("<h");
    out.append(level);
    out.append(">");

    for (var node : heading.nodes()) {
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

  private void paragraph(Paragraph paragraph) throws IOException {
    out.append("""
    <div class="paragraph">
    <p>""");

    for (var node : paragraph.nodes()) {
      node(node);
    }

    out.append("""
    </p>
    </div>
    """);
  }

}