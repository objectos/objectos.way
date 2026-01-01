/*
 * Copyright (C) 2021-2026 Objectos Software LTDA.
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

class ThisProcessor implements AsciiDoc.Processor {

  private static final int START = 0;

  private static final int HEADER = 1 << 0;

  private static final int PREAMBLE = 1 << 1;

  private static final int CONTENT = 1 << 2;

  private int listItem;

  private int state;

  private String result;

  private final StringBuilder header = new StringBuilder();

  private final StringBuilder headingId = new StringBuilder();

  private int headingIdIndex;

  private final StringBuilder preamble = new StringBuilder();

  private final StringBuilder content = new StringBuilder();

  private StringBuilder sb;

  private int sectionLevel;

  DocumentAttributes attributes;

  @Override
  public final void boldEnd() {
    sb.append("</strong>");
  }

  @Override
  public final void boldStart() {
    sb.append("<strong>");
  }

  @Override
  public final void documentEnd() {
    result = switch (state) {
      case HEADER -> """
        <div id="header">
        %s
        </div>

        <div id="content">
        </div>
        """.formatted(header);

      case PREAMBLE -> """
        <div id="header">
        </div>

        <div id="content">
        %s
        </div>
        """.formatted(preamble);

      case CONTENT -> """
        <div id="header">
        </div>

        <div id="content">
        %s
        </div>
        """.formatted(content);

      case HEADER | PREAMBLE -> """
        <div id="header">
        %s
        </div>

        <div id="content">
        %s
        </div>
        """.formatted(header, preamble);

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
        """.formatted(header, preamble, content);

      case PREAMBLE | CONTENT -> """
        <div id="header">
        </div>
        <div id="content">
        %s
        %s
        </div>
        """.formatted(preamble, content);

      default -> throw new UnsupportedOperationException(
        "Implement me :: state=" + Integer.toBinaryString(state));
    };
  }

  @Override
  public final void documentStart(DocumentAttributes attributes) {
    this.attributes = attributes;

    listItem = 0;

    sectionLevel = 0;

    state = START;

    result = "";

    header.setLength(0);

    headingId.setLength(0);

    headingIdIndex = 0;

    content.setLength(0);

    preamble.setLength(0);

    sb = null;
  }

  @Override
  public final void headingEnd(int level) {
    sb.append("</h");
    sb.append(level);
    sb.append(">\n");

    switch (state) {
      case HEADER -> { /*noop*/ }
      case HEADER | PREAMBLE | CONTENT, PREAMBLE | CONTENT, CONTENT -> {
        sb.insert(headingIdIndex, headingId);

        if (sectionLevel == 1) {
          content.append("<div class=\"sectionbody\">\n");
        }
      }
      default -> throw new UnsupportedOperationException("Implement me :: state=" + state);
    }
  }

  @Override
  public final void headingStart(int level) {
    sb = switch (state) {
      case START -> {
        state = HEADER;

        header.append("<h");
        header.append(level);
        header.append(">");

        yield header;
      }

      default -> {
        headingId.setLength(0);
        headingId.append("_");

        content.append("<h");
        content.append(level);
        content.append(" id=\"");
        headingIdIndex = sb.length();
        content.append("\">");

        yield content;
      }
    };
  }

  @Override
  public final void inlineMacro(
      String name, String target, InlineMacroAttributes attributes) {
    switch (name) {
      case "i" -> {
        var href = target;

        sb.append("<a href=\"");
        sb.append(href);
        sb.append("\">");
        attributes.render("1");
        sb.append("</a>");
      }
    }
  }

  @Override
  public final void italicEnd() {
    sb.append("</em>");
  }

  @Override
  public final void italicStart() {
    sb.append("<em>");
  }

  @Override
  public final void lineFeed() {
    sb.append('\n');
  }

  @Override
  public final void link(String href, LinkText text) {
    sb.append("<a href=\"");
    sb.append(href);
    sb.append("\">");
    text.render();
    sb.append("</a>");
  }

  @Override
  public final void listingBlockEnd() {
    sb.append("</pre>");
    sb.append("</div>");
    sb.append("</div>");
  }

  @Override
  public final void listingBlockStart() {
    sb.append("<div class=\"listingblock\">");
    sb.append("<div class=\"content\">");
    sb.append("<pre>");
  }

  @Override
  public final void listItemEnd() {
    if (listItem > 0) {
      sb.append("</p>");

      listItem--;
    }

    sb.append("</li>");
  }

  @Override
  public final void listItemStart() {
    sb.append("\n<li><p>");

    listItem++;
  }

  @Override
  public final void monospaceEnd() {
    sb.append("</code>");
  }

  @Override
  public final void monospaceStart() {
    sb.append("<code>");
  }

  @Override
  public final void paragraphEnd() {
    sb.append("</p>\n</div>\n");
  }

  @Override
  public final void paragraphStart() {
    sb.append("<div class=\"paragraph\">\n<p>");
  }

  @Override
  public final void preambleEnd() {}

  @Override
  public final void preambleStart() {
    state = switch (state) {
      case START -> PREAMBLE;
      case HEADER -> HEADER | PREAMBLE;
      default -> throw new UnsupportedOperationException("Implement me :: state=" + state);
    };

    sb = preamble;
  }

  @Override
  public final void sectionEnd() {
    if (sectionLevel == 1) {
      sb.append("</div>\n"); // section body
    }
    sb.append("</div>\n"); // section

    sectionLevel = 0;
  }

  @Override
  public final void sectionStart(int level) {
    state = switch (state) {
      case START -> CONTENT;
      case HEADER | PREAMBLE -> HEADER | PREAMBLE | CONTENT;
      case HEADER | PREAMBLE | CONTENT -> state;
      case PREAMBLE -> PREAMBLE | CONTENT;
      case CONTENT -> state;
      default -> throw new UnsupportedOperationException("Implement me :: state=" + state);
    };

    sb = content;

    sb.append("<div class=\"sect");
    sb.append(level);
    sb.append("\">\n");

    sectionLevel = level;
  }

  @Override
  public final void sectionStart(int level, String style) {
    sectionStart(level);
  }

  @Override
  public final void sourceCodeBlockEnd() {
    sb.append("</code>");
    sb.append("</pre>");
    sb.append("</div>");
    sb.append("</div>");
  }

  @Override
  public final void sourceCodeBlockStart(String language) {
    sb.append("<div class=\"listingblock\">");
    sb.append("<div class=\"content\">");
    sb.append("<pre class=\"highlight\">");
    sb.append("<code class=\"language-");
    sb.append(language);
    sb.append("\" data-lang=\"");
    sb.append(language);
    sb.append("\">");
  }

  @Override
  public final void text(String s) {
    sb.append(s);

    if (headingIdIndex > 0) {
      headingId.append(s.replace(' ', '_').toLowerCase());
    }
  }

  @Override
  public final String toString() {
    return result.toString();
  }

  @Override
  public final void unorderedListEnd() {
    sb.append("</ul>");
    sb.append("</div>");
  }

  @Override
  public final void unorderedListStart() {
    if (listItem > 0) {
      sb.append("</p>");

      listItem--;
    }

    sb.append("<div class=\"ulist\">\n");
    sb.append("<ul>");
  }

  final String attribute(String key) {
    return attributes.getOrDefault(key, "");
  }
}