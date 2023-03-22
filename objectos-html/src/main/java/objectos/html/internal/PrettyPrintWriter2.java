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
import objectos.html.tmpl.ElementKind;
import objectos.html.tmpl.StandardElementName;

public final class PrettyPrintWriter2 extends Writer2 {

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
      write(valuesIter.next());
      write('\"');

      while (valuesIter.hasNext()) {
        write(' ');
        write('\"');
        write(valuesIter.next());
        write('\"');
      }
    }
  }

  private void documentNode(HtmlNode node) {
    if (node instanceof HtmlDocumentType) {
      documentType();
    } else if (node instanceof HtmlElement element) {
      element(element);
    } else {
      var type = node.getClass();

      throw new UnsupportedOperationException(
        "Implement me :: type=" + type
      );
    }
  }

  private void documentType() {
    write("<!DOCTYPE html>");

    write(NL);
  }

  private void element(HtmlElement element) {
    startTag(element);

    if (isVoid(element)) {
      return;
    }

    var metadata = isHead(element);

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
        element(child);
      } else {
        if (!wasNewLine) {
          write(NL);
        }

        element(child);

        write(NL);

        newLine = true;
      }
    } else {
      var type = node.getClass();

      throw new UnsupportedOperationException(
        "Implement me :: type=" + type
      );
    }

    return newLine;
  }

  private void endTag(HtmlElement element) {
    write('<');
    write('/');
    write(element.name());
    write('>');
  }

  private boolean isHead(HtmlElement element) {
    return element.elementName() == StandardElementName.HEAD;
  }

  private boolean isPhrasing(HtmlElement element) {
    var name = element.elementName();

    return PHRASING.contains(name);
  }

  private boolean isVoid(HtmlElement element) {
    var name = element.elementName();

    var kind = name.getKind();

    return kind == ElementKind.VOID;
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