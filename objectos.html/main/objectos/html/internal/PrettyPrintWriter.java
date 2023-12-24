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

import java.util.Iterator;
import java.util.Set;
import objectos.html.pseudom.HtmlAttribute;
import objectos.html.pseudom.HtmlDocument;
import objectos.html.pseudom.HtmlDocumentType;
import objectos.html.pseudom.HtmlElement;
import objectos.html.pseudom.HtmlIterable;
import objectos.html.pseudom.HtmlNode;
import objectos.html.pseudom.HtmlRawText;
import objectos.html.pseudom.HtmlText;

public final class PrettyPrintWriter extends Writer {

  private static final Set<String> PHRASING = Set.of(
      StandardElementName.A.getName(),
      StandardElementName.ABBR.getName(),
      StandardElementName.B.getName(),
      StandardElementName.BR.getName(),
      StandardElementName.BUTTON.getName(),
      StandardElementName.CODE.getName(),
      StandardElementName.EM.getName(),
      StandardElementName.IMG.getName(),
      StandardElementName.INPUT.getName(),
      StandardElementName.KBD.getName(),
      StandardElementName.LABEL.getName(),
      StandardElementName.LINK.getName(),
      StandardElementName.PROGRESS.getName(),
      StandardElementName.SAMP.getName(),
      StandardElementName.SELECT.getName(),
      StandardElementName.SMALL.getName(),
      StandardElementName.SPAN.getName(),
      StandardElementName.STRONG.getName(),
      StandardElementName.SUB.getName(),
      StandardElementName.SUP.getName(),
      StandardElementName.SVG.getName(),
      StandardElementName.TEMPLATE.getName(),
      StandardElementName.TEXTAREA.getName()
  );

  private static final Set<String> TEXT_AS_RAW = Set.of(
      "script", "style"
  );

  private static final String NL = System.lineSeparator();

  @Override
  public final void process(HtmlDocument document) {
    HtmlIterable<HtmlNode> nodes;
    nodes = document.nodes();

    Iterator<HtmlNode> nodesIter;
    nodesIter = nodes.iterator();

    if (nodesIter.hasNext()) {
      documentNode(nodesIter.next());

      while (nodesIter.hasNext()) {
        write(NL);

        documentNode(nodesIter.next());
      }

      write(NL);
    }
  }

  private void documentNode(HtmlNode node) {
    switch (node) {
      case HtmlDocumentType doctype -> write("<!DOCTYPE html>");

      case HtmlElement element -> element(element);

      case HtmlText text -> writeText(text.value());

      default -> throw new UnsupportedOperationException(
          "Implement me :: type=" + node.getClass()
      );
    }
  }

  private void element(HtmlElement element) {
    // start tag
    String elementName;
    elementName = element.name();

    write('<');
    write(elementName);

    for (HtmlAttribute attribute : element.attributes()) {
      attribute(attribute);
    }

    write('>');

    if (element.isVoid()) {
      // void element
      // - no attrs
      // - no children
      // - no end tag
      return;
    }

    boolean textAsRaw;
    textAsRaw = TEXT_AS_RAW.contains(elementName);

    boolean newLine;
    newLine = false;

    for (HtmlNode node : element.nodes()) {
      newLine = elementNode(node, textAsRaw, newLine);
    }

    // end tag
    write('<');
    write('/');
    write(elementName);
    write('>');
  }

  private void attribute(HtmlAttribute attribute) {
    String name;
    name = attribute.name();

    write(' ');
    write(name);

    if (attribute.isBoolean()) {
      return;
    }

    HtmlIterable<String> values;
    values = attribute.values();

    Iterator<String> valuesIter;
    valuesIter = values.iterator();

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

  private boolean elementNode(HtmlNode node, boolean textAsRaw, boolean wasNewLine) {
    boolean newLine;
    newLine = false;

    switch (node) {
      case HtmlElement child -> {
        if (isPhrasing(child)) {
          element(child);
        } else {
          if (!wasNewLine) {
            write(NL);
          }

          element(child);

          write(NL);

          newLine = true;
        }
      }

      case HtmlText text -> {
        String value;
        value = text.value();

        if (textAsRaw) {
          if (!startsWithNewLine(value)) {
            write(NL);
          }

          write(value);

          if (!endsWithNewLine(value)) {
            write(NL);
          }
        } else {
          writeText(value);
        }
      }

      case HtmlRawText raw -> {
        String value;
        value = raw.value();

        write(value);
      }

      default -> throw new UnsupportedOperationException(
          "Implement me :: type=" + node.getClass()
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

  private boolean isNewLine(char c) {
    return c == '\n' || c == '\r';
  }

  private boolean isPhrasing(HtmlElement element) {
    String name;
    name = element.name();

    return PHRASING.contains(name);
  }

  private boolean startsWithNewLine(String value) {
    int length;
    length = value.length();

    if (length > 0) {
      char first;
      first = value.charAt(0);

      return isNewLine(first);
    } else {
      return false;
    }
  }

}