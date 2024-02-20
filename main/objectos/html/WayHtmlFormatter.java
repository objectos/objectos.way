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
package objectos.html;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import objectos.html.pseudom.HtmlAttribute;
import objectos.html.pseudom.HtmlDocument;
import objectos.html.pseudom.HtmlDocumentType;
import objectos.html.pseudom.HtmlElement;
import objectos.html.pseudom.HtmlNode;
import objectos.html.pseudom.HtmlRawText;
import objectos.html.pseudom.HtmlText;
import objectos.lang.IterableOnce;

/**
 * The Objectos Way standard {@link HtmlFormatter} implementation.
 */
public final class WayHtmlFormatter extends HtmlFormatter {

  /**
   * The only instance.
   */
  public static final WayHtmlFormatter INSTANCE = new WayHtmlFormatter();

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

  private WayHtmlFormatter() {}

  @Override
  protected final void format(HtmlDocument document, Appendable out) throws IOException {
    byte state;
    state = START;

    for (HtmlNode node : document.nodes()) {
      state = node(out, state, node);
    }

    if (state != START) {
      out.append(NL);
    }
  }

  private byte node(Appendable out, byte state, HtmlNode node) throws IOException {
    return switch (node) {
      case HtmlDocumentType doctype -> doctype(out, state, doctype);

      case HtmlElement element -> element(out, state, element);

      case HtmlText text -> text(out, state, text);

      case HtmlRawText raw -> raw(out, state, raw);

      default -> throw new UnsupportedOperationException(
          "Implement me :: type=" + node.getClass()
      );
    };
  }

  private byte doctype(Appendable out, byte state, HtmlDocumentType doctype) throws IOException {
    out.append("<!DOCTYPE html>");

    return BLOCK_END;
  }

  private byte element(Appendable out, byte state, HtmlElement element) throws IOException {
    // start tag
    String elementName;
    elementName = element.name();

    byte nextState;

    byte childState;

    if (PHRASING.contains(elementName)) {
      nextState = childState = PHRASE;

      if (state == BLOCK_END) {
        out.append(NL);
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
        out.append(NL);
      }
    }

    out.append('<');
    out.append(elementName);

    for (HtmlAttribute attribute : element.attributes()) {
      attribute(out, attribute);
    }

    out.append('>');

    if (!element.isVoid()) {
      int childCount;
      childCount = 0;

      for (HtmlNode node : element.nodes()) {
        childState = node(out, childState, node);

        childCount++;
      }

      // do we need a NL before the end tag?
      if (childCount > 0) {
        if (nextState == PHRASE && childState == BLOCK_END) {
          out.append(NL);
        }

        else if (nextState != PHRASE && childState != PHRASE) {
          out.append(NL);
        }
      }

      // end tag
      out.append('<');
      out.append('/');
      out.append(elementName);
      out.append('>');
    }

    return nextState;
  }

  private void attribute(Appendable out, HtmlAttribute attribute) throws IOException {
    String name;
    name = attribute.name();

    out.append(' ');
    out.append(name);

    if (attribute.isBoolean()) {
      return;
    }

    IterableOnce<String> values;
    values = attribute.values();

    Iterator<String> valuesIter;
    valuesIter = values.iterator();

    if (valuesIter.hasNext()) {
      out.append('=');
      out.append('\"');
      attributeValue(out, valuesIter.next());

      while (valuesIter.hasNext()) {
        out.append(' ');
        attributeValue(out, valuesIter.next());
      }

      out.append('\"');
    }
  }

  private void attributeValue(Appendable out, String value) throws IOException {
    for (int idx = 0, len = value.length(); idx < len;) {
      char c;
      c = value.charAt(idx++);

      switch (c) {
        case '&' -> idx = ampersand(out, value, idx, len);

        case '<' -> out.append("&lt;");

        case '>' -> out.append("&gt;");

        case '"' -> out.append("&#34;");

        case '\'' -> out.append("&#39;");

        default -> out.append(c);
      }
    }
  }

  private byte text(Appendable out, byte state, HtmlText text) throws IOException {
    String value;
    value = text.value();

    switch (state) {
      case BLOCK_END -> {
        if (!startsWithNewLine(value)) {
          out.append(NL);
        }

        writeText(out, value);
      }

      case SCRIPT -> {
        if (!startsWithNewLine(value)) {
          out.append(NL);
        }

        out.append(value);

        if (!endsWithNewLine(value)) {
          out.append(NL);
        }
      }

      default -> writeText(out, value);
    }

    return PHRASE;
  }

  // visible for testing
  final void writeText(Appendable out, String value) throws IOException {
    for (int idx = 0, len = value.length(); idx < len;) {
      char c;
      c = value.charAt(idx++);

      switch (c) {
        case '&' -> out.append("&amp;");

        case '<' -> out.append("&lt;");

        case '>' -> out.append("&gt;");

        default -> out.append(c);
      }
    }
  }

  private byte raw(Appendable out, byte state, HtmlRawText raw) throws IOException {
    String value;
    value = raw.value();

    out.append(value);

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