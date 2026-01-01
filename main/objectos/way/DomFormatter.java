/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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
package objectos.way;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;

class DomFormatter {

  static final DomFormatter JSON = new DomFormatterJson();

  static final DomFormatter STANDARD = new DomFormatter();

  private static final Set<String> PHRASING = Set.of(
      HtmlElementName.A.name(),
      HtmlElementName.ABBR.name(),
      HtmlElementName.B.name(),
      HtmlElementName.BR.name(),
      HtmlElementName.BUTTON.name(),
      HtmlElementName.CODE.name(),
      HtmlElementName.EM.name(),
      HtmlElementName.IMG.name(),
      HtmlElementName.INPUT.name(),
      HtmlElementName.KBD.name(),
      HtmlElementName.LABEL.name(),
      HtmlElementName.PROGRESS.name(),
      HtmlElementName.SAMP.name(),
      HtmlElementName.SELECT.name(),
      HtmlElementName.SMALL.name(),
      HtmlElementName.SPAN.name(),
      HtmlElementName.STRONG.name(),
      HtmlElementName.SUB.name(),
      HtmlElementName.SUP.name(),
      HtmlElementName.SVG.name(),
      HtmlElementName.TEMPLATE.name(),
      HtmlElementName.TEXTAREA.name()
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

  DomFormatter() {}

  public final void formatTo(Dom.Document document, Appendable appendable) throws IOException {
    Objects.requireNonNull(document, "document == null");
    Objects.requireNonNull(appendable, "appendable == null");

    format(document, appendable);
  }

  public final void formatTo(Html.Template template, Appendable appendable) throws IOException {
    Objects.requireNonNull(template, "template == null");
    Objects.requireNonNull(appendable, "appendable == null");

    final Html.Markup.OfHtml html;
    html = new Html.Markup.OfHtml();

    template.renderHtml(html);

    final Dom.Document document;
    document = Dom.Document.of(html);

    format(document, appendable);
  }

  void format(Dom.Document document, Appendable out) throws IOException {
    byte state;
    state = START;

    for (Dom.Node node : document.nodes()) {
      state = node(out, state, node);
    }

    if (state != START) {
      out.append(NL);
    }
  }

  private byte node(Appendable out, byte state, Dom.Node node) throws IOException {
    return switch (node) {
      case Dom.Document.Type doctype -> doctype(out, state, doctype);

      case Dom.Element element -> element(out, state, element);

      case Dom.Text text -> text(out, state, text);

      case Dom.Raw raw -> raw(out, state, raw);

      default -> throw new UnsupportedOperationException(
          "Implement me :: type=" + node.getClass()
      );
    };
  }

  private byte doctype(Appendable out, byte state, Dom.Document.Type doctype) throws IOException {
    out.append(doctype.value());

    return BLOCK_END;
  }

  private byte element(Appendable out, byte state, Dom.Element element) throws IOException {
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

    for (Dom.Attribute attribute : element.attributes()) {
      attribute(out, attribute);
    }

    out.append('>');

    if (!element.voidElement()) {
      int childCount;
      childCount = 0;

      for (Dom.Node node : element.nodes()) {
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

  enum Quotes {
    SINGLE('\'', "&#39;"),

    DOUBLE('\"', "&#34;");

    final char symbol;

    final String escape;

    private Quotes(char symbol, String escape) {
      this.symbol = symbol;

      this.escape = escape;
    }
  }

  private void attribute(Appendable out, Dom.Attribute attribute) throws IOException {
    String name;
    name = attribute.name();

    out.append(' ');

    out.append(name);

    if (attribute.booleanAttribute()) {
      return;
    }

    Quotes quotes;
    quotes = attribute.singleQuoted() ? Quotes.SINGLE : Quotes.DOUBLE;

    out.append('=');

    out.append(quotes.symbol);

    final String value;
    value = attribute.value();

    attributeValue(out, quotes, value);

    out.append(quotes.symbol);
  }

  // visible for testing
  final void attributeValue(Appendable out, Quotes quotes, String value) throws IOException {
    int idx;
    idx = 0;

    int len;
    len = value.length();

    for (; idx < len; idx++) {
      char c;
      c = value.charAt(idx);

      if (c == quotes.symbol) {
        break;
      }

      if (c == '&') {
        break;
      }
    }

    if (idx == len) {
      out.append(value);

      return;
    }

    out.append(value, 0, idx);

    while (idx < len) {
      char c;
      c = value.charAt(idx++);

      if (c == quotes.symbol) {
        out.append(quotes.escape);
      }

      else if (c == '&') {
        ampersand(out, value, idx, len);
      }

      else {
        out.append(c);
      }
    }
  }

  private byte text(Appendable out, byte state, Dom.Text text) throws IOException {
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

  private byte raw(Appendable out, byte state, Dom.Raw raw) throws IOException {
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

  // visible for testing
  final int ampersand(Appendable out, String value, int idx, int len) throws IOException {
    enum State {
      START,
      MAYBE_NAMED,
      MAYBE_NUMERIC,
      MAYBE_DECIMAL,
      MAYBE_HEX,
      ENTITY,
      TEXT;
    }

    int start;
    start = idx;

    State state;
    state = State.START;

    loop: while (idx < len) {
      char c;
      c = value.charAt(idx++);

      switch (state) {
        case START -> {
          if (c == '#') {
            state = State.MAYBE_NUMERIC;
          } else if (isAsciiAlphanumeric(c)) {
            state = State.MAYBE_NAMED;
          } else {
            state = State.TEXT;

            break loop;
          }
        }

        case MAYBE_NAMED -> {
          if (c == ';') {
            state = State.ENTITY;

            break loop;
          } else if (!isAsciiAlphanumeric(c)) {
            state = State.TEXT;

            break loop;
          }
        }

        case MAYBE_NUMERIC -> {
          if (c == 'x' || c == 'X') {
            state = State.MAYBE_HEX;
          } else if (isAsciiDigit(c)) {
            state = State.MAYBE_DECIMAL;
          } else {
            state = State.TEXT;

            break loop;
          }
        }

        case MAYBE_DECIMAL -> {
          if (c == ';') {
            state = State.ENTITY;

            break loop;
          } else if (!isAsciiDigit(c)) {
            state = State.TEXT;

            break loop;
          }
        }

        case MAYBE_HEX -> {
          if (c == ';') {
            state = State.ENTITY;

            break loop;
          } else if (!isAsciiHexDigit(c)) {
            state = State.TEXT;

            break loop;
          }
        }

        case ENTITY, TEXT -> {
          throw new AssertionError();
        }

        default -> {
          throw new UnsupportedOperationException(
              "Implement me :: state=" + state
          );
        }
      }
    }

    switch (state) {
      case START -> {
        out.append("&amp;");
      }

      case ENTITY -> {
        out.append('&');

        out.append(value, start, idx);
      }

      case TEXT -> {
        out.append("&amp;");

        idx = start;
      }

      default -> {
        throw new UnsupportedOperationException(
        );
      }
    }

    return idx;
  }

  private boolean isAsciiAlpha(char c) {
    return 'A' <= c && c <= 'Z'
        || 'a' <= c && c <= 'z';
  }

  private boolean isAsciiAlphanumeric(char c) {
    return isAsciiDigit(c) || isAsciiAlpha(c);
  }

  private boolean isAsciiDigit(char c) {
    return '0' <= c && c <= '9';
  }

  private boolean isAsciiHexDigit(char c) {
    return isAsciiDigit(c)
        || 'a' <= c && c <= 'f'
        || 'A' <= c && c <= 'F';
  }

}