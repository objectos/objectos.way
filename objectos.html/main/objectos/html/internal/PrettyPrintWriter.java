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

  private static final byte START = 1;
  private static final byte BLOCK_START = 2;
  private static final byte BLOCK_END = 3;
  private static final byte PHRASE = 4;
  private static final byte SCRIPT = 5;

  @Override
  public final void process(HtmlDocument document) {
    byte state;
    state = START;

    for (HtmlNode node : document.nodes()) {
      state = node(state, node);
    }

    if (state != START) {
      write(NL);
    }
  }

  private byte node(byte state, HtmlNode node) {
    return switch (node) {
      case HtmlDocumentType doctype -> doctype(state, doctype);

      case HtmlElement element -> element(state, element);

      case HtmlText text -> text(state, text);

      case HtmlRawText raw -> raw(state, raw);

      default -> throw new UnsupportedOperationException(
          "Implement me :: type=" + node.getClass()
      );
    };
  }

  private byte doctype(byte state, HtmlDocumentType doctype) {
    write("<!DOCTYPE html>");

    return BLOCK_END;
  }

  private byte element(byte state, HtmlElement element) {
    // start tag
    String elementName;
    elementName = element.name();

    byte nextState;

    byte childState;

    if (PHRASING.contains(elementName)) {
      nextState = childState = PHRASE;

      if (state == BLOCK_END) {
        write(NL);
      }
    }

    else {
      if (TEXT_AS_RAW.contains(elementName)) {
        nextState = childState = SCRIPT;
      }

      else {
        nextState = BLOCK_END;

        childState = BLOCK_START;
      }

      if (state != START) {
        // we should start this element in the next line
        // except if we are at the start of the document
        write(NL);
      }
    }

    write('<');
    write(elementName);

    for (HtmlAttribute attribute : element.attributes()) {
      elementAttribute(attribute);
    }

    write('>');

    if (!element.isVoid()) {
      int childCount;
      childCount = 0;

      for (HtmlNode node : element.nodes()) {
        childState = node(childState, node);

        childCount++;
      }

      // do we need a NL before the end tag?
      if (childCount > 0) {
        if (nextState == PHRASE && childState == BLOCK_END) {
          write(NL);
        }

        else if (nextState != PHRASE && childState != PHRASE) {
          write(NL);
        }
      }

      // end tag
      write('<');
      write('/');
      write(elementName);
      write('>');
    }

    return nextState;
  }

  private void elementAttribute(HtmlAttribute attribute) {
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

  private byte text(byte state, HtmlText text) {
    String value;
    value = text.value();

    switch (state) {
      case BLOCK_END -> {
        if (!endsWithNewLine(value)) {
          write(NL);
        }

        writeText(value);
      }

      case SCRIPT -> {
        if (!startsWithNewLine(value)) {
          write(NL);
        }

        write(value);

        if (!endsWithNewLine(value)) {
          write(NL);
        }
      }

      default -> writeText(value);
    }

    return PHRASE;
  }

  private byte raw(byte state, HtmlRawText raw) {
    String value;
    value = raw.value();

    write(value);

    return PHRASE;
  }

  private boolean startsWithNewLine(String value) {
    int length;
    length = value.length();

    if (length == 0) {
      return false;
    }

    char first;
    first = value.charAt(0);

    return isNewLine(first);
  }

  private boolean endsWithNewLine(String value) {
    int length;
    length = value.length();

    if (length == 0) {
      return false;
    }

    char last;
    last = value.charAt(length - 1);

    return isNewLine(last);
  }

  private boolean isNewLine(char c) {
    return c == '\n' || c == '\r';
  }

}