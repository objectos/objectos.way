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
import java.util.Set;

final class SyntaxJava implements Html.Component {

  private enum Context {

    NORMAL,

    TEXT_BLOCK,

    COMMENT;

  }

  private Context context;

  private boolean eol;

  private Html.Markup html;

  private int line;

  private int normalIndex;

  private final String source;

  private int sourceIndex;

  private int sourceLength;

  SyntaxJava(String source) {
    this.source = Objects.requireNonNull(source, "source == null");
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

        html.f(this::parseLine)

    );
  }

  private void parseLine() {
    // we'll consume characters until we find something to execute:
    // 1) EOL
    // 2) comment
    // 3) TBD

    eol = false;

    switch (context) {
      case NORMAL -> {}

      case TEXT_BLOCK -> renderTextBlock();

      case COMMENT -> renderCommentTraditional();
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

      else if (c == '\'') {
        renderCharLiteral();
      }

      else if (c == '"') {
        renderStringOrText();
      }

      else if (c == '@') {
        renderAnnotation();
      }

      else if (isWhiteSpace(c)) {
        consume(this::isWhiteSpace);
      }

      else if (isOperator(c)) {
        consume(this::isOperator);
      }

      else if (isSeparator(c)) {
        consume(this::isSeparator);
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

        case '*' -> renderCommentTraditional();

        default -> sourceIndex = nextIndex;
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

  private void renderCommentTraditional() {
    // where the string begins
    final int beginIndex;
    beginIndex = sourceIndex;

    // initial state depends on context
    switch (context) {
      case NORMAL -> {
        // render any preceding normal text (if necessary)
        renderNormal();

        // consume opening '/*'
        sourceIndex += 2;

        // we are in a string now
        context = Context.COMMENT;
      }

      case TEXT_BLOCK -> throw new IllegalStateException("Cannot render comment as we're in context " + context);

      case COMMENT -> {}
    }

    boolean wasStar = false;

    outer: while (sourceIndex < sourceLength) {

      final char c;
      c = source.charAt(sourceIndex);

      if (Ascii.isLineTerminator(c)) {

        eol = true;

        break outer;

      }

      else if (c == '*') {

        wasStar = true;

        sourceIndex++;

      }

      else if (c == '/' && wasStar) {

        // we found the end of the comment
        context = Context.NORMAL;

        // trailing '/' is part of the comment
        sourceIndex++;

        break outer;

      }

      else {

        wasStar = false;

        sourceIndex++;

      }

    }

    final String text;
    text = source.substring(beginIndex, sourceIndex);

    html.span(

        html.attr(Syntax.DATA_HIGH, "comment"),

        html.text(text)

    );

    // do not emit normal text
    normalIndex = sourceIndex;
  }

  private void renderCharLiteral() {
    renderString0('\'');
  }

  private void renderStringOrText() {
    // a minimal text block
    // """
    // """
    // 3 quotes + \n + 3 quotes = 7 characters

    final int remaining;
    remaining = sourceLength - sourceIndex;

    if (remaining < 7) {
      // smaller than minimal text block
      renderString();
    }

    else if (source.charAt(sourceIndex + 1) != '"'
        || source.charAt(sourceIndex + 2) != '"'
        || source.charAt(sourceIndex + 3) != '\n') {
      renderString();
    }

    else {
      renderTextBlock();
    }
  }

  private void renderString() {
    renderString0('"');
  }

  private void renderString0(char quote) {
    // where the string begins
    final int beginIndex;
    beginIndex = sourceIndex;

    // render any preceding normal text (if necessary)
    renderNormal();

    // consume initial quote
    sourceIndex++;

    // are we in a escape sequence?
    boolean escape;
    escape = false;

    outer: while (sourceIndex < sourceLength) {

      final char c;
      c = source.charAt(sourceIndex++);

      if (c == quote) {

        if (!escape) {
          break outer;
        }

        else {
          escape = false;
        }

      }

      else if (Ascii.isLineTerminator(c)) {

        // malformed, we move back the index
        // to delegate the line terminator to the readLine method
        sourceIndex--;

        break outer;

      }

      else if (c == '\\') {

        escape = true;

      }

      else {

        escape = false;

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

  private void renderTextBlock() {
    // where the string begins
    final int beginIndex;
    beginIndex = sourceIndex;

    // initial state depends on context
    switch (context) {
      case NORMAL -> {
        // render any preceding normal text (if necessary)
        renderNormal();

        // consume opening |"""|
        sourceIndex++;
        sourceIndex++;
        sourceIndex++;

        // we are in a text block now
        context = Context.TEXT_BLOCK;
      }

      case TEXT_BLOCK -> {}

      case COMMENT -> throw new IllegalStateException("Cannot render string as we're in a comment context");
    }

    // how many quotes in sequence have we seen?
    int quotes = 0;

    // are we in a escape sequence?
    boolean escape = false;

    outer: while (sourceIndex < sourceLength) {

      final char c;
      c = source.charAt(sourceIndex++);

      if (Ascii.isLineTerminator(c)) {

        sourceIndex--;

        break outer;

      }

      else if (c == '"') {

        quotes++;

        if (!escape && quotes == 3) {
          context = Context.NORMAL;

          break outer;
        }

        escape = false;

      }

      else if (c == '\\') {

        quotes = 0;

        escape = true;

      }

      else {

        quotes = 0;

        escape = false;

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

  private void renderAnnotation() {
    renderNormal();

    // where the (possibly) annotation begins
    final int beginIndex;
    beginIndex = sourceIndex;

    // skip '@'
    sourceIndex++;

    while (sourceIndex < sourceLength) {
      final char peek;
      peek = source.charAt(sourceIndex);

      if (isWhiteSpace(peek)) {
        break;
      }

      if (isSeparator(peek) && peek != '.') {
        break;
      }

      if (isOperator(peek)) {
        break;
      }

      if (Ascii.isLineTerminator(peek)) {
        break;
      }

      sourceIndex++;
    }

    final String text;
    text = source.substring(beginIndex, sourceIndex);

    html.span(

        html.attr(Syntax.DATA_HIGH, "annotation"),

        html.text(text)

    );

    // do not emit normal text
    normalIndex = sourceIndex;
  }

  @FunctionalInterface
  private interface CharPredicate {
    boolean test(char c);
  }

  private void consume(CharPredicate test) {
    // consume current
    sourceIndex++;

    while (sourceIndex < sourceLength) {
      final char peek;
      peek = source.charAt(sourceIndex);

      if (!test.test(peek)) {
        break;
      }

      sourceIndex++;
    }
  }

  private static final Set<String> KEYWORDS = Set.of(
      "strictfp",
      "assert",
      "enum",

      "public", "protected", "private",
      "abstract", "static", "final",
      "transient", "volatile", "synchronized",
      "native",

      // Declarations
      "class", "interface", "extends",
      "package", "throws", "implements",

      // Primitive types and void
      "boolean", "byte", "char",
      "short", "int", "long",
      "float", "double",
      "void",

      // Control flow
      "if", "else",
      "try", "catch", "finally",
      "do", "while",
      "for", "continue",
      "switch", "case", "default",
      "break", "throw", "return",

      // Other keywords
      "this", "new", "super",
      "import", "instanceof",

      // Forbidden!
      "goto", "const",

      // literals
      "null", "true", "false",

      // contextual
      "record",
      "var"
  );

  private void maybeKeyword() {
    // if this is a keyword, it begins here
    final int beginIndex;
    beginIndex = sourceIndex;

    // consume first char
    int endIndex;
    endIndex = sourceIndex + 1;

    // was the last char a boundary?
    boolean wasBoundary = false;

    while (endIndex < sourceLength) {
      final char peek;
      peek = source.charAt(endIndex);

      if (!Ascii.isLowerCase(peek) && peek != '-') {
        wasBoundary = isWhiteSpace(peek);

        wasBoundary |= isSeparator(peek);

        wasBoundary |= isOperator(peek);

        wasBoundary |= Ascii.isLineTerminator(peek);

        break;
      }

      endIndex++;
    }

    if (!wasBoundary && endIndex < sourceLength) {
      sourceIndex = endIndex;

      return;
    }

    final String maybe;
    maybe = source.substring(beginIndex, endIndex);

    if (!KEYWORDS.contains(maybe)) {
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
    // we don't consider '@' as it is handled separately
    return switch (c) {
      case '(', ')',
           '{', '}',
           '[', ']',
           ';',
           ',',
           '.',
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