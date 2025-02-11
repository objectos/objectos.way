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

final class SyntaxJavaHtml implements Html.Component {

  private final SyntaxJavaConfig config;

  private Html.Markup html;

  private int lineLimit;

  private int lineNext;

  private int normalIndex;

  private final String source;

  private int sourceIndex;

  private final int sourceLength;

  SyntaxJavaHtml(SyntaxJavaConfig config, String source) {
    this.config = config;

    this.source = source;

    sourceLength = source.length();
  }

  @Override
  public final void renderHtml(Html.Markup m) {
    html = Objects.requireNonNull(m, "m == null");

    while (nextLine()) {
      parseLine();
    }
  }

  private boolean nextLine() {
    if (sourceIndex >= sourceLength) {
      // we've reached the EOF
      return false;
    }

    // we need to remember where the line starts
    final int startIndex;
    startIndex = sourceIndex;

    // handle EOL = EOF
    final int beforeLimit;
    beforeLimit = lineLimit;

    // number of consecutive CR chars
    int cr = 0;

    while (sourceIndex < sourceLength) {
      final char c;
      c = source.charAt(sourceIndex);

      if (c == '\n') {
        lineLimit = sourceIndex - cr;

        lineNext = sourceIndex + 1;

        break;
      }

      if (c == '\r') {
        cr++;
      }

      else if (cr > 0) {
        lineLimit = sourceIndex - cr;

        lineNext = sourceIndex;

        break;
      }

      sourceIndex++;
    }

    if (beforeLimit == lineLimit) {
      lineLimit = lineNext = sourceLength;
    }

    sourceIndex = startIndex;

    return true;
  }

  private void parseLine() {
    normalIndex = sourceIndex;

    while (sourceIndex < lineLimit) {
      final char peek;
      peek = source.charAt(sourceIndex);

      if (peek == '/') {
        tryComment();
      }

      else {
        throw new UnsupportedOperationException("Implement me");
      }

      sourceIndex++;
    }

    renderNormal();

    normalIndex = sourceIndex;

    sourceIndex = lineNext;

    renderNormal();
  }

  private void tryComment() {
    // let's check if next char is:
    // 1) '/' => end of line comment
    // 2) '*' => traditional comment

    final int nextIndex;
    nextIndex = sourceIndex + 1;

    if (nextIndex >= lineLimit) {
      return;
    }

    final char nextChar;
    nextChar = source.charAt(nextIndex);

    switch (nextChar) {
      case '/' -> renderCommentEndOfLine();

      default -> throw new UnsupportedOperationException("Implement me");
    }
  }

  private void renderCommentEndOfLine() {
    // render any preceding normal text (if necessary)
    renderNormal();

    // render the comment
    final String className;
    className = config.get(SyntaxJavaElement.COMMENT, "color:high-comment");

    final String text;
    text = source.substring(sourceIndex, lineLimit);

    html.span(

        html.className(className),

        html.text(text)

    );

    // signal that line terminators should be consumed
    normalIndex = lineLimit;

    // we resume after this line
    sourceIndex = lineLimit;
  }

  private void renderNormal() {
    final int endIndex;
    endIndex = Math.min(sourceIndex, lineNext);

    if (normalIndex < endIndex) {

      final String text;
      text = source.substring(normalIndex, endIndex);

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