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

  private enum Context {

    NORMAL,

    STRING;

  }

  private final SyntaxJavaConfig config;

  private Context context;

  private boolean eol;

  private Html.Markup html;

  private int line;

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

    context = Context.NORMAL;

    line = 1;

    sourceIndex = 0;

    sourceLength = source.length();

    while (sourceIndex < sourceLength) {
      normalIndex = sourceIndex;

      renderLine();
    }
  }

  private void renderLine() {
    html.span(

        html.attr(Syntax.DATA_LINE, Integer.toString(line++)),

        html.renderFragment(this::parseLine)

    );
  }

  private void parseLine() {
    // we'll consume characters until we find something to execute:
    // 1) EOL
    // 2) comment
    // 3) TBD

    eol = false;

    if (context == Context.STRING) {
      maybeString();
    }

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

      else if (isBoundary(c)) {
        consumeBoundary();
      }

      else if (Ascii.isLowerCase(c)) {
        maybeKeyword();
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
    renderNormal();

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

    final String text;
    text = source.substring(beginIndex, sourceIndex);

    html.span(

        html.attr(Syntax.DATA_HIGH, "comment"),

        html.text(text)

    );

    normalIndex = sourceIndex;
  }

  private void maybeString() {
    // where the string begins
    final int beginIndex;
    beginIndex = sourceIndex;

    // our 'parser' state
    enum Parser {
      START_QUOTE,

      CONTENTS,

      END_QUOTE;
    }

    Parser parser;
    parser = Parser.START_QUOTE;

    // initial state depends on context
    switch (context) {
      case NORMAL -> {
        // render any preceding normal text (if necessary)
        renderNormal();

        // consume opening '"'
        sourceIndex++;

        // we are in a string now
        context = Context.STRING;
      }

      case STRING -> {
        // we continue previous string
        parser = Parser.CONTENTS;
      }
    }

    outer: while (sourceIndex < sourceLength) {

      final char c;
      c = source.charAt(sourceIndex);

      if (Ascii.isLineTerminator(c)) {

        switch (parser) {
          case START_QUOTE -> { eol = true; normalIndex = sourceIndex; }

          case CONTENTS -> { eol = true; normalIndex = sourceIndex; }

          case END_QUOTE -> { eol = true; context = Context.NORMAL; normalIndex = sourceIndex; }
        }

        break outer;

      }

      else if (c == '"') {

        switch (parser) {
          case START_QUOTE -> { parser = Parser.START_QUOTE; sourceIndex++; }

          case CONTENTS -> { parser = Parser.END_QUOTE; sourceIndex++; }

          case END_QUOTE -> { parser = Parser.END_QUOTE; sourceIndex++; }
        }

      }

      else {

        switch (parser) {
          case START_QUOTE -> { parser = Parser.CONTENTS; sourceIndex++; }

          case CONTENTS -> { parser = Parser.CONTENTS; sourceIndex++; }

          case END_QUOTE -> {
            // we found the end of the string
            context = Context.NORMAL;

            // set next normal text start
            normalIndex = sourceIndex;

            break outer;
          }
        }

      }

    }

    final String text;
    text = source.substring(beginIndex, sourceIndex);

    html.span(

        html.attr(Syntax.DATA_HIGH, "string"),

        html.text(text)

    );

    // do not emit normal text
    normalIndex = sourceIndex;
  }

  private void consumeBoundary() {
    // consume current
    sourceIndex++;

    while (sourceIndex < sourceLength) {
      final char peek;
      peek = source.charAt(sourceIndex);

      if (!isBoundary(peek)) {
        break;
      }

      sourceIndex++;
    }
  }

  private void maybeKeyword() {
    // if this is a keyword, it begins here
    final int beginIndex;
    beginIndex = sourceIndex;

    // consume first char
    int endIndex;
    endIndex = sourceIndex + 1;

    while (endIndex < sourceLength) {
      final char peek;
      peek = source.charAt(endIndex);

      if (!Ascii.isLowerCase(peek) && peek != '-') {
        break;
      }

      endIndex++;
    }

    final String maybe;
    maybe = source.substring(beginIndex, endIndex);

    if (!config.isKeyword(maybe)) {
      sourceIndex = endIndex;

      return;
    }

    renderNormal();

    normalIndex = sourceIndex = endIndex;

    html.span(

        html.attr(Syntax.DATA_HIGH, "keyword"),

        html.text(maybe)

    );
  }

  private void renderNormal() {
    if (normalIndex < sourceIndex) {

      final String text;
      text = source.substring(normalIndex, sourceIndex);

      html.span(text);

    }
  }

  private boolean isBoundary(char c) {
    return isOperator(c) || isSeparator(c) || isWhiteSpace(c);
  }

  // https://docs.oracle.com/javase/specs/jls/se23/html/jls-3.html#jls-3.12
  private boolean isOperator(char c) {
    return switch (c) {
      case '=',
           '>', '<', '!',
           '~', '&', '|', '^',
           '+', '-', '*', '/', '%',
           '?' -> true;

      default -> false;
    };
  }

  // https://docs.oracle.com/javase/specs/jls/se23/html/jls-3.html#jls-3.11
  private boolean isSeparator(char c) {
    return switch (c) {
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

  // https://docs.oracle.com/javase/specs/jls/se23/html/jls-3.html#jls-3.6
  private boolean isWhiteSpace(char c) {
    // we don't consider line terminator as they're handled separately...
    return c == ' ' || c == '\t' || c == '\f';
  }

}