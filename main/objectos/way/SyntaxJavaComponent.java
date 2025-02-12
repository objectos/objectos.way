/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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

import java.util.Objects;

final class SyntaxJavaComponent implements Html.Component {

  private final SyntaxJavaConfig config;

  private Html.Markup html;

  private boolean eol;

  private int normalIndex;

  private final String source;

  private int sourceIndex;

  private int sourceLength;

  SyntaxJavaComponent(SyntaxJavaConfig config, String source) {
    this.config = config;

    this.source = source;
  }

  @Override
  public final void renderHtml(Html.Markup m) {
    html = Objects.requireNonNull(m, "m == null");

    sourceIndex = 0;

    sourceLength = source.length();

    while (sourceIndex < sourceLength) {
      normalIndex = sourceIndex;

      renderLine();
    }
  }

  private void renderLine() {
    html.div(

        html.renderFragment(this::parseLine)

    );
  }

  private void parseLine() {
    // we'll consume characters until we find something to execute:
    // 1) EOL
    // 2) comment
    // 3) TBD

    eol = false;

    while (sourceIndex < sourceLength) {
      final char c;
      c = source.charAt(sourceIndex);

      if (Ascii.isLineTerminator(c)) {
        renderLineTerminator();

        break;
      }

      else if (c == '/') {
        maybeComment();
      }

      else if (c == '"') {
        maybeString();
      }

      else {
        sourceIndex++;
      }
    }

    if (!eol) {
      // we are at the EOF
      // render any remaining normal text
      renderNormal();
    }
  }

  private void renderLineTerminator() {
    // we must signal parseLine
    eol = true;

    // consume current
    final char c;
    c = source.charAt(sourceIndex++);

    if (c != Ascii.CR) {
      return;
    }

    char peek;
    peek = source.charAt(sourceIndex);

    if (peek != Ascii.LF) {
      return;
    }

    // consume the LF after CR
    sourceIndex++;
  }

  private void maybeComment() {
    final int nextIndex;
    nextIndex = sourceIndex + 1;

    if (nextIndex < sourceLength) {

      // let's check if next char is:
      // 1) '/' => end of line comment
      // 2) '*' => traditional comment

      final char peekNext;
      peekNext = source.charAt(nextIndex);

      switch (peekNext) {
        case '/' -> renderCommentEndOfLine();

        default -> throw new UnsupportedOperationException("Implement me");
      }

    } else {

      // there's no next character
      // update sourceIndex to signal parseLine it should stop
      sourceIndex = nextIndex;

    }

  }

  private void renderCommentEndOfLine() {
    // render any preceding normal text (if necessary)
    renderNormal();

    // we must signal parseLine
    eol = true;

    // where the comment begins
    final int beginIndex;
    beginIndex = sourceIndex;

    // skip '/' '/'
    sourceIndex += 2;

    // find EOL / EOF
    while (sourceIndex < sourceLength) {
      final char peek;
      peek = source.charAt(sourceIndex);

      if (Ascii.isLineTerminator(peek)) {
        break;
      }

      sourceIndex++;
    }

    final String className;
    className = config.get(SyntaxJavaElement.COMMENT, "color:high-comment");

    final String text;
    text = source.substring(beginIndex, sourceIndex);

    html.span(

        html.className(className),

        html.text(text)

    );
  }

  private void maybeString() {
    // render any preceding normal text (if necessary)
    renderNormal();

    // where the string begins
    final int beginIndex = sourceIndex;

    // consume opening '"'
    sourceIndex++;

    while (sourceIndex < sourceLength) {
      final char peek;
      peek = source.charAt(sourceIndex++);

      if (peek == '"') {
        break;
      }
    }

    final String className;
    className = config.get(SyntaxJavaElement.STRING_LITERAL, "color:high-string");

    final String text;
    text = source.substring(beginIndex, sourceIndex);

    html.span(

        html.className(className),

        html.text(text)

    );

    // set next normal text start
    normalIndex = sourceIndex;
  }

  private void renderNormal() {
    if (normalIndex < sourceIndex) {

      final String text;
      text = source.substring(normalIndex, sourceIndex);

      html.span(text);

    }
  }

  // https://docs.oracle.com/javase/specs/jls/se23/html/jls-3.html#jls-3.12
  final boolean isOperator(char ch) {
    return switch (ch) {
      case '=',
           '>', '<', '!',
           '~', '&', '|', '^',
           '+', '-', '*', '/', '%',
           '?' -> true;

      default -> false;
    };
  }

  // https://docs.oracle.com/javase/specs/jls/se23/html/jls-3.html#jls-3.11
  final boolean isSeparator(char ch) {
    return switch (ch) {
      case '(', ')',
           '{', '}',
           '[', ']',
           ';',
           ',',
           '.',
           '@',
           ':' -> true;

      default -> false;
    };
  }

}