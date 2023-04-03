/*
 * Copyright (C) 2021-2023 Objectos Software LTDA.
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

import java.util.Arrays;
import objectos.lang.Check;
import objectos.util.IntArrays;

class Pass0 implements Pass1.Source, Pass2.Source {

  private static final int EOF = 0;

  private static final int LINE_START = 1;

  private static final int LINE_START_LIKE = 2;

  private static final int HEADING_START = 3;

  private static final int HEADING = 4;

  private static final int BLOB = 5;

  private static final int SPACE_LIKE = 6;

  private static final int BOLD_OR_LIST = 7;

  private static final int BOLD_START = 8;
  private static final int BOLD_END = 9;

  private static final int ITALIC_START = 10;
  private static final int ITALIC_END = 11;

  private static final int MONO_START = 12;
  private static final int MONO_END = 13;

  private static final int ATTR_NAME = 14;
  private static final int ATTR_VALUE = 15;
  private static final int ATTR_VALUE_MONO_START = 16;
  private static final int ATTR_VALUE_MONO_END = 17;
  private static final int ATTR_VALUE_SPACE_LIKE = 18;
  private static final int ATTR_QUOTES = 19;
  private static final int ATTR_SEPARATOR = 20;
  private static final int ATTR_LIST_END = 21;

  private static final int LISTING_BLOCK = 22;
  private static final int LISTING_BLOCK_OR_LIST = 23;

  private static final int LITERAL_OR_LIST = 24;

  private static final int MACRO_ANY_START = 25;
  private static final int MACRO_INLINE_START = 26;
  private static final int MACRO_INLINE = 27;

  private static final int DOCATTR_NAME = 28;
  private static final int DOCATTR_NAME_NEXT = 29;
  private static final int DOCATTR_VALUE = 30;

  private static final int _ATTRLIST_BLOCK = 1;
  private static final int _ATTRLIST_INLINE_MACRO = 2;

  private String source;

  private int sourceIndex;

  private int sourceRollback;

  private int state;

  private int[] token;

  private int tokenIndex;

  private int tokenRollback;

  private int counter;

  // aux states

  private int attrlist;

  private int auxiliaryStart;

  private int blobStart;

  private int boundaryStart;

  private int lineStart;

  Pass0() {
    token = new int[512];
  }

  public final void execute(String source) {
    Check.state(
      state == EOF,

      """
      Concurrent process (0) is not supported.

      It seems a previous AsciiDoc document process (0):

      - is currently running; or
      - finished abruptly (most likely due to a bug in this component, sorry...).
      """
    );

    this.source = source;

    sourceIndex = 0;

    tokenIndex = 0;

    state = LINE_START;

    attrlist = 0;
    auxiliaryStart = 0;
    blobStart = 0;
    boundaryStart = 0;
    lineStart = 0;

    while (state != EOF) {
      state = state(state);
    }
  }

  @Override
  public final String substring(int start, int end) {
    return source.substring(start, end);
  }

  @Override
  public final int token(int index) { return token[index]; }

  @Override
  public final int tokenAt(int index) { return token[index]; }

  @Override
  public final int tokens() { return tokenIndex; }

  final String source(int beginIndex, int endIndex) {
    return source.substring(beginIndex, endIndex);
  }

  final int[] toToken() {
    return Arrays.copyOf(token, tokenIndex);
  }

  private void add(int s0) {
    token = IntArrays.growIfNecessary(token, tokenIndex);

    token[tokenIndex++] = s0;
  }

  private void add(int s0, int s1) {
    token = IntArrays.growIfNecessary(token, tokenIndex + 1);

    token[tokenIndex++] = s0;
    token[tokenIndex++] = s1;
  }

  private void add(int s0, int s1, int s2) {
    token = IntArrays.growIfNecessary(token, tokenIndex + 2);

    token[tokenIndex++] = s0;
    token[tokenIndex++] = s1;
    token[tokenIndex++] = s2;
  }

  private void add(int s0, int s1, int s2, int s3) {
    token = IntArrays.growIfNecessary(token, tokenIndex + 3);

    token[tokenIndex++] = s0;
    token[tokenIndex++] = s1;
    token[tokenIndex++] = s2;
    token[tokenIndex++] = s3;
  }

  private void add(int s0, int s1, int s2, int s3, int s4) {
    token = IntArrays.growIfNecessary(token, tokenIndex + 4);

    token[tokenIndex++] = s0;
    token[tokenIndex++] = s1;
    token[tokenIndex++] = s2;
    token[tokenIndex++] = s3;
    token[tokenIndex++] = s4;
  }

  private void add(int s0, int s1, int s2, int s3, int s4, int s5) {
    token = IntArrays.growIfNecessary(token, tokenIndex + 5);

    token[tokenIndex++] = s0;
    token[tokenIndex++] = s1;
    token[tokenIndex++] = s2;
    token[tokenIndex++] = s3;
    token[tokenIndex++] = s4;
    token[tokenIndex++] = s5;
  }

  private int advance(int state) {
    sourceIndex++;

    return state;
  }

  private boolean hasChar() { return sourceIndex < source.length(); }

  private boolean isWord(char c) {
    int type = Character.getType(c);

    return switch (type) {
      case Character.LOWERCASE_LETTER:
      case Character.MODIFIER_LETTER:
      case Character.OTHER_LETTER:
      case Character.TITLECASE_LETTER:
      case Character.UPPERCASE_LETTER:

      case Character.NON_SPACING_MARK:
      case Character.COMBINING_SPACING_MARK:
      case Character.ENCLOSING_MARK:

      case Character.DECIMAL_DIGIT_NUMBER:
      case Character.LETTER_NUMBER:
      case Character.OTHER_NUMBER:

      case Character.CONNECTOR_PUNCTUATION:

        yield true;
      default:
        yield false;
    };
  }

  private char peek() { return source.charAt(sourceIndex); }

  private int rollbackAttributes() {
    attrlist = 0;
    sourceIndex = sourceRollback;
    sourceIndex++; // skips initial '['

    tokenIndex = tokenRollback;

    return BLOB;
  }

  private int rollbackMacro() {
    return rollbackMacro(0);
  }

  private int rollbackMacro(int skip) {
    sourceIndex = sourceRollback;
    sourceIndex += skip + 1; // skips initial ':' + additional skips

    tokenIndex = tokenRollback;

    return BLOB;
  }

  private int state(int state) {
    return switch (state) {
      case LINE_START -> stateLineStart();

      case LINE_START_LIKE -> stateLineStartLike();

      case HEADING_START -> stateHeadingStart();

      case HEADING -> stateHeading();

      case BLOB -> stateBlob();

      case SPACE_LIKE -> stateSpaceLike();

      case BOLD_OR_LIST -> stateBoldOrList();

      case BOLD_START -> stateBoldStart();

      case BOLD_END -> stateBoldEnd();

      case ITALIC_START -> stateItalicStart();

      case ITALIC_END -> stateItalicEnd();

      case MONO_START -> stateMonoStart();

      case MONO_END -> stateMonoEnd();

      case ATTR_LIST_END -> stateAttrListEnd();

      case ATTR_NAME -> stateAttrName();

      case ATTR_VALUE -> stateAttrValue();

      case ATTR_VALUE_MONO_START -> stateAttrValueMonoStart();

      case ATTR_VALUE_MONO_END -> stateAttrValueMonoEnd();

      case ATTR_VALUE_SPACE_LIKE -> stateAttrValueSpaceLike();

      case ATTR_QUOTES -> stateAttrQuotes();

      case ATTR_SEPARATOR -> stateAttrSeparator();

      case LISTING_BLOCK -> stateListingBlock();

      case LISTING_BLOCK_OR_LIST -> stateListingBlockOrList();

      case LITERAL_OR_LIST -> stateLiteralOrList();

      case MACRO_ANY_START -> stateMacroAnyStart();

      case MACRO_INLINE_START -> stateMacroInlineStart();

      case MACRO_INLINE -> stateMacroInline();

      case DOCATTR_NAME -> stateDocattrName();

      case DOCATTR_NAME_NEXT -> stateDocattrNameNext();

      case DOCATTR_VALUE -> stateDocattrValue();

      default -> uoe();
    };
  }

  private int stateAttrListEnd() {
    if (!hasChar()) {
      add(Token.ATTR_LIST_END, Token.EOF);

      attrlist = 0;

      return EOF;
    }

    return switch (peek()) {
      case '\n' -> {
        add(Token.ATTR_LIST_END, Token.LF);

        attrlist = 0;

        yield advance(LINE_START);
      }

      case ' ', '\t', '\f', '\u000B' -> switch (attrlist) {
        case _ATTRLIST_BLOCK -> advance(state);

        case _ATTRLIST_INLINE_MACRO -> stateAttrListEndInlineMacro();

        default -> throw new UnsupportedOperationException("Implement me :: attrlist=" + attrlist);
      };

      default -> switch (attrlist) {
        case _ATTRLIST_BLOCK -> rollbackAttributes();

        case _ATTRLIST_INLINE_MACRO -> stateAttrListEndInlineMacro();

        default -> throw new UnsupportedOperationException("Implement me :: attrlist=" + attrlist);
      };
    };
  }

  private int stateAttrListEndInlineMacro() {
    add(Token.ATTR_LIST_END);

    attrlist = 0;

    blobStart = sourceIndex;

    return advance(BLOB);
  }

  private int stateAttrName() {
    if (!hasChar()) {
      return rollbackAttributes();
    }

    return switch (peek()) {
      case '\n' -> rollbackAttributes();

      case '"' -> advance(ATTR_QUOTES);

      case ',' -> {
        add(Token.ATTR_VALUE_START);

        if (auxiliaryStart < sourceIndex) {
          add(Token.BLOB, auxiliaryStart, sourceIndex);
        }

        add(Token.ATTR_VALUE_END);

        auxiliaryStart = sourceIndex;

        yield advance(ATTR_SEPARATOR);
      }

      case '=' -> {
        add(Token.ATTR_NAME, auxiliaryStart, sourceIndex, Token.ATTR_VALUE_START);

        auxiliaryStart = sourceIndex + 1; // skip '='

        yield advance(ATTR_VALUE);
      }

      case ']' -> {
        if (auxiliaryStart < sourceIndex) {
          add(
            Token.ATTR_VALUE_START,
            Token.BLOB, auxiliaryStart, sourceIndex,
            Token.ATTR_VALUE_END);
        }

        yield advance(ATTR_LIST_END);
      }

      case '`' -> {
        add(Token.ATTR_VALUE_START);

        yield advance(ATTR_VALUE_MONO_START);
      }

      default -> advance(state);
    };
  }

  private int stateAttrQuotes() {
    if (!hasChar()) {
      return rollbackAttributes();
    }

    return switch (peek()) {
      case '\n' -> rollbackAttributes();

      case '"' -> {
        // trim initial quote from value
        add(
          Token.ATTR_VALUE_START,
          Token.DQUOTE, auxiliaryStart + 1, sourceIndex,
          Token.ATTR_VALUE_END);

        auxiliaryStart = sourceIndex + 1;

        yield advance(ATTR_NAME);
      }

      default -> advance(state);
    };
  }

  private int stateAttrSeparator() {
    if (!hasChar()) {
      return rollbackAttributes();
    }

    return switch (peek()) {
      case '\n' -> rollbackAttributes();

      case ' ', '\t', '\f', '\u000B' -> advance(state);

      default -> {
        add(Token.SEPARATOR, auxiliaryStart, sourceIndex);

        auxiliaryStart = sourceIndex;

        yield advance(ATTR_NAME);
      }
    };
  }

  private int stateAttrValue() {
    if (!hasChar()) {
      return rollbackAttributes();
    }

    return switch (peek()) {
      case '\n' -> rollbackAttributes();

      case ' ', '\t', '\f', '\u000B' -> advance(ATTR_VALUE_SPACE_LIKE);

      case '"' -> advance(ATTR_QUOTES);

      case ',' -> {
        add(Token.BLOB, auxiliaryStart, sourceIndex, Token.ATTR_VALUE_END);

        auxiliaryStart = sourceIndex;

        yield advance(ATTR_SEPARATOR);
      }

      case ']' -> {
        if (auxiliaryStart < sourceIndex) {
          add(Token.BLOB, auxiliaryStart, sourceIndex);
        }

        add(Token.ATTR_VALUE_END);

        yield advance(ATTR_LIST_END);
      }

      case '`' -> advance(ATTR_VALUE_MONO_END);

      default -> advance(state);
    };
  }

  private int stateAttrValueMonoEnd() {
    if (!hasChar()) {
      return rollbackAttributes();
    }

    var c = peek();

    if (!isWord(c)) {
      var endIndex = sourceIndex - 1;

      add(Token.BLOB, auxiliaryStart, endIndex, Token.MONO_END, endIndex);

      auxiliaryStart = sourceIndex;
    }

    return ATTR_VALUE;
  }

  private int stateAttrValueMonoStart() {
    if (!hasChar()) {
      return rollbackAttributes();
    }

    return switch (peek()) {
      case '\n' -> rollbackAttributes();

      case ' ', '\t', '\f', '\u000B' -> advance(ATTR_VALUE_SPACE_LIKE);

      default -> {
        var endIndex = sourceIndex - 1;

        if (auxiliaryStart < endIndex) {
          add(Token.BLOB, auxiliaryStart, endIndex);
        }

        add(Token.MONO_START, endIndex);

        auxiliaryStart = sourceIndex;

        yield advance(ATTR_VALUE);
      }
    };
  }

  private int stateAttrValueSpaceLike() {
    if (!hasChar()) {
      return rollbackAttributes();
    }

    return switch (peek()) {
      case '\n' -> rollbackAttributes();

      case ' ', '\t', '\f', '\u000B' -> advance(state);

      case '`' -> advance(ATTR_VALUE_MONO_START);

      default -> advance(ATTR_VALUE);
    };
  }

  private int stateBlob() {
    if (!hasChar()) {
      add(Token.BLOB, blobStart, sourceIndex, Token.EOF);

      return EOF;
    }

    return stateBlob0();
  }

  private int stateBlob0() {
    return switch (peek()) {
      case '\n' -> {
        add(Token.BLOB, blobStart, sourceIndex, Token.LF);

        yield advance(LINE_START);
      }

      case ' ', '\t', '\f', '\u000B' -> {
        boundaryStart = sourceIndex;

        yield advance(SPACE_LIKE);
      }

      case '\'' -> {
        var prev = source.charAt(sourceIndex - 1); // safe, we are not at start of line/doc

        if (isWord(prev)) {
          var nextIndex = sourceIndex + 1;

          if (nextIndex < source.length()) {
            var next = source.charAt(nextIndex);

            if (isWord(next)) {
              add(Token.BLOB, blobStart, sourceIndex, Token.APOSTROPHE, sourceIndex);

              blobStart = nextIndex;
            }
          }
        }

        yield advance(state);
      }

      case '*' -> advance(BOLD_END);

      case ':' -> {
        if (blobStart < boundaryStart) {
          add(Token.BLOB, blobStart, boundaryStart);

          blobStart = boundaryStart;
        }

        sourceRollback = sourceIndex;
        tokenRollback = tokenIndex;

        if (boundaryStart > lineStart) {
          // if this is a macro then it must be inline, otherwise error?
          add(Token.INLINE_MACRO, boundaryStart, sourceIndex);

          yield advance(MACRO_INLINE_START);
        }

        // assume inline macro for now
        add(Token.INLINE_MACRO, boundaryStart, sourceIndex);

        yield advance(MACRO_ANY_START);
      }

      case '_' -> advance(ITALIC_END);

      case '`' -> advance(MONO_END);

      default -> advance(state);
    };
  }

  private int stateBoldEnd() {
    var endIndex = sourceIndex - 1;

    if (!hasChar()) {
      add(
        Token.BLOB, blobStart, endIndex,
        Token.BOLD_END, endIndex,
        Token.EOF
      );

      return EOF;
    }

    var c = peek();

    if (isWord(c)) {
      return advance(BLOB);
    }

    if (blobStart < endIndex) {
      add(Token.BLOB, blobStart, endIndex);
    }

    add(Token.BOLD_END, endIndex);

    blobStart = sourceIndex;

    return switch (c) {
      case '\n' -> {
        add(Token.LF);

        yield advance(LINE_START);
      }

      case ' ', '\t', '\f', '\u000B' -> advance(SPACE_LIKE);

      default -> advance(BLOB);
    };
  }

  private int stateBoldOrList() {
    if (!hasChar()) {
      add(Token.BLOB, blobStart, sourceIndex, Token.EOF);

      return EOF;
    }

    return switch (peek()) {
      case '\n' -> {
        add(Token.BLOB, blobStart, sourceIndex, Token.LF);

        yield advance(LINE_START);
      }

      case ' ', '\t', '\f', '\u000B' -> {
        add(Token.ULIST_ASTERISK, counter, lineStart, sourceIndex);

        yield advance(LINE_START_LIKE);
      }

      case '*' -> {
        counter++;

        yield advance(state);
      }

      default -> {
        var endIndex = sourceIndex - 1;

        if (blobStart != lineStart) {
          add(Token.BLOB, blobStart, endIndex);
        }

        add(Token.BOLD_START, endIndex);

        blobStart = sourceIndex;

        yield advance(BLOB);
      }
    };
  }

  private int stateBoldStart() {
    if (!hasChar()) {
      add(Token.BLOB, blobStart, sourceIndex, Token.EOF);

      return EOF;
    }

    return switch (peek()) {
      case '\n' -> {
        add(Token.BLOB, blobStart, sourceIndex, Token.LF);

        yield advance(LINE_START);
      }

      case ' ', '\t', '\f', '\u000B' -> advance(SPACE_LIKE);

      case '_' -> stateBoldStart0(ITALIC_START);

      default -> stateBoldStart0(BLOB);
    };
  }

  private int stateBoldStart0(int next) {
    var endIndex = sourceIndex - 1;

    if (blobStart < endIndex) {
      add(Token.BLOB, blobStart, endIndex);
    }

    add(Token.BOLD_START, endIndex);

    blobStart = sourceIndex;

    return advance(next);
  }

  private int stateDocattrName() {
    if (!hasChar()) {
      add(Token.BLOB, blobStart, sourceIndex, Token.EOF);

      return EOF;
    }

    var c = peek();

    var next = stateDocattrNameIs(c) ? DOCATTR_NAME_NEXT : BLOB;

    return advance(next);
  }

  private boolean stateDocattrNameIs(char c) {
    return ('a' <= c && c <= 'z')
        || (c == '_')
        || ('A' <= c && c <= 'Z')
        || ('0' <= c && c <= '9');
  }

  private int stateDocattrNameNext() {
    if (!hasChar()) {
      add(Token.BLOB, blobStart, sourceIndex, Token.EOF);

      return EOF;
    }

    var c = peek();

    if (c == ':') {
      add(Token.DOCATTR, lineStart + 1, sourceIndex);

      return advance(DOCATTR_VALUE);
    } else if (stateDocattrNameNextIs(c)) {
      return advance(state);
    } else {
      return advance(BLOB);
    }
  }

  private boolean stateDocattrNameNextIs(char c) {
    return (c == '-') || stateDocattrNameIs(c);
  }

  private int stateDocattrValue() {
    if (!hasChar()) {
      add(Token.EOF);

      return EOF;
    }

    return switch (peek()) {
      case '\n' -> {
        add(Token.LF);

        yield advance(LINE_START);
      }

      case ' ', '\t', '\f', '\u000B' -> advance(state);

      default -> {
        blobStart = sourceIndex;

        yield advance(BLOB);
      }
    };
  }

  private int stateHeading() {
    if (!hasChar()) {
      throw new UnsupportedOperationException("Implement me :: empty heading|not H[1-6]?");
    }

    return switch (peek()) {
      case ' ' -> advance(state);

      default -> {
        add(Token.HEADING, counter, lineStart, sourceIndex);

        yield LINE_START_LIKE;
      }
    };
  }

  private int stateHeadingStart() {
    if (!hasChar()) {
      throw new UnsupportedOperationException("Implement me :: not H[1-6]");
    }

    var c = peek();

    return switch (c) {
      case '=' -> {
        counter++;

        yield advance(state);
      }

      case ' ' -> advance(HEADING);

      default -> {
        blobStart = lineStart;

        yield advance(BLOB);
      }
    };
  }

  private int stateItalicEnd() {
    var endIndex = sourceIndex - 1;

    if (!hasChar()) {
      add(
        Token.BLOB, blobStart, endIndex,
        Token.ITALIC_END, endIndex,
        Token.EOF
      );

      return EOF;
    }

    var c = peek();

    if (isWord(c)) {
      return advance(BLOB);
    }

    if (blobStart < endIndex) {
      add(Token.BLOB, blobStart, endIndex);
    }

    add(Token.ITALIC_END, endIndex);

    blobStart = sourceIndex;

    return switch (c) {
      case '\n' -> {
        add(Token.LF);

        yield advance(LINE_START);
      }

      case ' ', '\t', '\f', '\u000B' -> advance(SPACE_LIKE);

      case '*' -> advance(BOLD_END);

      default -> advance(BLOB);
    };
  }

  private int stateItalicStart() {
    if (!hasChar()) {
      add(
        Token.BLOB, blobStart, sourceIndex,
        Token.EOF
      );

      return EOF;
    }

    return switch (peek()) {
      case '\n' -> {
        add(
          Token.BLOB, blobStart, sourceIndex,
          Token.LF
        );

        yield advance(LINE_START);
      }

      case ' ', '\t', '\f', '\u000B' -> advance(SPACE_LIKE);

      default -> {
        var endIndex = sourceIndex - 1;

        if (blobStart < endIndex) {
          add(Token.BLOB, blobStart, endIndex);
        }

        add(Token.ITALIC_START, endIndex);

        blobStart = sourceIndex;

        yield advance(BLOB);
      }
    };
  }

  private int stateLineStart() {
    if (!hasChar()) {
      add(Token.EOF);

      return EOF;
    }

    attrlist = 0;
    lineStart = blobStart = boundaryStart = sourceIndex;

    return switch (peek()) {
      case '\n' -> {
        add(Token.LF);

        yield advance(state);
      }

      case ' ', '\t', '\f', '\u000B' -> {
        counter = 1;

        yield advance(LITERAL_OR_LIST);
      }

      case '-' -> {
        counter = 1;

        yield advance(LISTING_BLOCK_OR_LIST);
      }

      case ':' -> advance(DOCATTR_NAME);

      case '=' -> {
        counter = 1;

        yield advance(HEADING_START);
      }

      case '*' -> {
        counter = 1;

        yield advance(BOLD_OR_LIST);
      }

      case '[' -> {
        attrlist = _ATTRLIST_BLOCK;
        sourceRollback = sourceIndex;
        auxiliaryStart = sourceIndex + 1;
        tokenRollback = tokenIndex;

        add(Token.ATTR_LIST_START);

        yield advance(ATTR_NAME);
      }

      case '_' -> advance(ITALIC_START);

      case '`' -> advance(MONO_START);

      default -> advance(BLOB);
    };
  }

  private int stateLineStartLike() {
    if (!hasChar()) {
      add(Token.EOF);

      return EOF;
    }

    blobStart = boundaryStart = sourceIndex;

    return switch (peek()) {
      case '*' -> advance(BOLD_START);

      case '_' -> advance(ITALIC_START);

      case '`' -> advance(MONO_START);

      default -> BLOB;
    };
  }

  private int stateListingBlock() {
    if (!hasChar()) {
      if (counter >= 4) {
        add(Token.LISTING_BLOCK_DELIM, counter, Token.EOF);
      } else {
        add(Token.BLOB, blobStart, sourceIndex, Token.EOF);
      }

      return EOF;
    }

    return switch (peek()) {
      case '\n' -> {
        if (counter >= 4) {
          add(Token.LISTING_BLOCK_DELIM, counter, Token.LF);
        } else {
          add(Token.BLOB, blobStart, sourceIndex, Token.LF);
        }

        yield advance(LINE_START);
      }

      case ' ', '\t', '\f', '\u000B' -> {
        var next = counter >= 4 ? state : BLOB;

        yield next;
      }

      case '-' -> {
        counter++;

        yield advance(state);
      }

      default -> advance(BLOB);
    };
  }

  private int stateListingBlockOrList() {
    if (!hasChar()) {
      add(Token.BLOB, blobStart, sourceIndex, Token.EOF);

      return EOF;
    }

    return switch (peek()) {
      case '\n' -> {
        add(Token.BLOB, blobStart, sourceIndex, Token.LF);

        yield advance(LINE_START);
      }

      case ' ', '\t', '\f', '\u000B' -> {
        add(Token.ULIST_HYPHEN, lineStart, sourceIndex);

        yield advance(LINE_START_LIKE);
      }

      case '-' -> {
        counter++;

        yield advance(LISTING_BLOCK);
      }

      default -> advance(BLOB);
    };
  }

  private int stateLiteralOrList() {
    if (!hasChar()) {
      add(Token.EOF);

      return EOF;
    }

    return switch (peek()) {
      case '\n' -> {
        add(Token.LF);

        yield advance(LINE_START);
      }

      case ' ', '\t', '\f', '\u000B' -> {
        counter++;

        yield advance(state);
      }

      case '*' -> {
        counter = 1;

        yield advance(BOLD_OR_LIST);
      }

      default -> {
        var start = sourceIndex - counter;

        add(Token.LITERALI, start, sourceIndex);

        // do not advance!
        yield LINE_START_LIKE;
      }
    };
  }

  private int stateMacroAnyStart() {
    if (!hasChar()) {
      return rollbackMacro();
    }

    auxiliaryStart = sourceIndex;

    return switch (peek()) {
      case '\n' -> rollbackMacro();

      case ':' -> {
        // replace Token.INLINE_MACRO

        yield uoe();
      }

      default -> advance(MACRO_INLINE);
    };
  }

  private int stateMacroInline() {
    if (!hasChar()) {
      return rollbackMacro();
    }

    return switch (peek()) {
      case '\n' -> rollbackMacro();

      case ' ', '\t', '\f', '\u000B' -> rollbackMacro();

      case '[' -> {
        add(Token.BLOB, auxiliaryStart, sourceIndex, Token.ATTR_LIST_START);

        attrlist = _ATTRLIST_INLINE_MACRO;

        auxiliaryStart = sourceIndex + 1;

        yield advance(ATTR_NAME);
      }

      default -> advance(state);
    };
  }

  private int stateMacroInlineStart() {
    if (!hasChar()) {
      return rollbackMacro();
    }

    auxiliaryStart = sourceIndex;

    return switch (peek()) {
      case '\n' -> rollbackMacro();

      case ':' -> rollbackMacro(1);

      default -> advance(MACRO_INLINE);
    };
  }

  private int stateMonoEnd() {
    var endIndex = sourceIndex - 1;

    if (!hasChar()) {
      add(
        Token.BLOB, blobStart, endIndex,
        Token.MONO_END, endIndex,
        Token.EOF
      );

      return EOF;
    }

    var c = peek();

    if (isWord(c)) {
      return advance(BLOB);
    }

    add(
      Token.BLOB, blobStart, endIndex,
      Token.MONO_END, endIndex
    );

    blobStart = sourceIndex;

    return switch (c) {
      case '\n' -> {
        add(Token.LF);

        yield advance(LINE_START);
      }

      case ' ', '\t', '\f', '\u000B' -> advance(SPACE_LIKE);

      default -> advance(BLOB);
    };
  }

  private int stateMonoStart() {
    if (!hasChar()) {
      add(Token.BLOB, blobStart, sourceIndex, Token.EOF);

      return EOF;
    }

    return switch (peek()) {
      case '\n' -> {
        add(Token.BLOB, blobStart, sourceIndex, Token.LF);

        yield advance(LINE_START);
      }

      case ' ', '\t', '\f', '\u000B' -> advance(SPACE_LIKE);

      default -> {
        var endIndex = sourceIndex - 1;

        if (blobStart < endIndex) {
          add(Token.BLOB, blobStart, endIndex);
        }

        add(Token.MONO_START, endIndex);

        blobStart = sourceIndex;

        yield advance(BLOB);
      }
    };
  }

  private int stateSpaceLike() {
    if (!hasChar()) {
      add(Token.BLOB, blobStart, sourceIndex, Token.EOF);

      return EOF;
    }

    boundaryStart = sourceIndex;

    return switch (peek()) {
      case '\n' -> {
        add(Token.BLOB, blobStart, sourceIndex, Token.LF);

        yield advance(LINE_START);
      }

      case ' ', '\t', '\f', '\u000B' -> advance(state);

      case '*' -> advance(BOLD_START);

      case '_' -> advance(ITALIC_START);

      case '`' -> advance(MONO_START);

      default -> advance(BLOB);
    };
  }

  private int uoe() {
    throw new UnsupportedOperationException("Implement me :: state=" + state + ";peek=" + peek());
  }

}