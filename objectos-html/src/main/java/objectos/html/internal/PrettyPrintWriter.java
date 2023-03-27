/*
 * Copyright (C) 2015-2023 Objectos Software LTDA.
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
package objectos.html.internal;

import java.util.EnumSet;
import java.util.Set;
import objectos.html.pseudom.HtmlAttribute;
import objectos.html.pseudom.HtmlDocument;
import objectos.html.pseudom.HtmlDocumentType;
import objectos.html.pseudom.HtmlElement;
import objectos.html.pseudom.HtmlNode;
import objectos.html.pseudom.HtmlRawText;
import objectos.html.pseudom.HtmlText;
import objectos.html.tmpl.StandardElementName;

public final class PrettyPrintWriter extends Writer {

  private static final Set<StandardElementName> PHRASING = EnumSet.of(
    StandardElementName.A,
    StandardElementName.ABBR,
    StandardElementName.B,
    StandardElementName.BR,
    StandardElementName.BUTTON,
    StandardElementName.CODE,
    StandardElementName.EM,
    StandardElementName.IMG,
    StandardElementName.INPUT,
    StandardElementName.KBD,
    StandardElementName.LABEL,
    StandardElementName.LINK,
    StandardElementName.META,
    StandardElementName.PROGRESS,
    StandardElementName.SAMP,
    StandardElementName.SCRIPT,
    StandardElementName.SELECT,
    StandardElementName.SMALL,
    StandardElementName.SPAN,
    StandardElementName.STRONG,
    StandardElementName.SUB,
    StandardElementName.SUP,
    StandardElementName.SVG,
    StandardElementName.TEMPLATE,
    StandardElementName.TEXTAREA
  );

  private static final String NL = System.lineSeparator();

  @Override
  public final void process(HtmlDocument document) {
    var nodes = document.nodes();

    var nodesIter = nodes.iterator();

    if (nodesIter.hasNext()) {
      documentNode(nodesIter.next());

      while (nodesIter.hasNext()) {
        write(NL);

        documentNode(nodesIter.next());
      }

      write(NL);
    }
  }

  private void attribute(HtmlAttribute attribute) {
    var name = attribute.name();

    write(' ');
    write(name);

    if (attribute.isBoolean()) {
      return;
    }

    var values = attribute.values();

    var valuesIter = values.iterator();

    if (valuesIter.hasNext()) {
      write('=');
      write('\"');
      writeAttributeValue(valuesIter.next());

      while (valuesIter.hasNext()) {
        write(' ');
        writeAttributeValue(valuesIter.next());
      }

      write('\"');
    }
  }

  private void documentNode(HtmlNode node) {
    if (node instanceof HtmlDocumentType) {
      documentType();
    } else if (node instanceof HtmlElement element) {
      element(element, false);
    } else {
      var type = node.getClass();

      throw new UnsupportedOperationException(
        "Implement me :: type=" + type
      );
    }
  }

  private void documentType() {
    write("<!DOCTYPE html>");
  }

  private void element(HtmlElement element, boolean metadata) {
    startTag(element);

    if (element.isVoid()) {
      return;
    }

    if (isHead(element)) {
      metadata = true;
    }

    var newLine = false;

    for (var node : element.nodes()) {
      newLine = elementNode(node, metadata, newLine);
    }

    endTag(element);
  }

  private boolean elementNode(HtmlNode node, boolean metadata, boolean wasNewLine) {
    var newLine = false;

    if (node instanceof HtmlElement child) {
      if (!metadata && isPhrasing(child)) {
        element(child, metadata);
      } else {
        if (!wasNewLine) {
          write(NL);
        }

        element(child, metadata);

        write(NL);

        newLine = true;
      }
    } else if (node instanceof HtmlText text) {
      var value = text.value();

      writeText(value);
    } else if (node instanceof HtmlRawText raw) {
      var value = raw.value();

      if (metadata) {
        if (!startsWithNewLine(value)) {
          write(NL);
        }

        write(value);

        if (!endsWithNewLine(value)) {
          write(NL);
        }
      } else {
        write(value);
      }
    } else {
      var type = node.getClass();

      throw new UnsupportedOperationException(
        "Implement me :: type=" + type
      );
    }

    return newLine;
  }

  private boolean endsWithNewLine(String value) {
    int length = value.length();

    if (length > 0) {
      var last = value.charAt(length - 1);

      return isNewLine(last);
    } else {
      return false;
    }
  }

  private void endTag(HtmlElement element) {
    write('<');
    write('/');
    write(element.name());
    write('>');
  }

  private boolean isHead(HtmlElement element) {
    return element.hasName(StandardElementName.HEAD);
  }

  private boolean isNewLine(char c) {
    return c == '\n' || c == '\r';
  }

  private boolean isPhrasing(HtmlElement element) {
    return element.matches(PHRASING::contains);
  }

  private boolean startsWithNewLine(String value) {
    int length = value.length();

    if (length > 0) {
      var first = value.charAt(0);

      return isNewLine(first);
    } else {
      return false;
    }
  }

  private void startTag(HtmlElement element) {
    var name = element.name();

    write('<');
    write(name);

    for (var attribute : element.attributes()) {
      attribute(attribute);
    }

    write('>');
  }

}