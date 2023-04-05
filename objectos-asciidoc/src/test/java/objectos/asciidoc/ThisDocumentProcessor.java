/*
 * Copyright 2023 Objectos Software LTDA.
 *
 * Reprodução parcial ou total proibida.
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
        """);
      } else {
        throw new UnsupportedOperationException(
          "Implement me :: type=" + node.getClass().getSimpleName()
        );
      }
    }
  }

  @Override
  public final String toString() {
    return out.toString();
  }

  private void heading(Heading heading) throws IOException {
    int level = heading.level();

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

  private void node(Node node) {
    if (node instanceof Text text) {
      out.append(text.value());
    } else {
      throw new UnsupportedOperationException(
        "Implement me :: type=" + node.getClass().getSimpleName()
      );
    }
  }

}