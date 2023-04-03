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
import java.util.Set;
import objectos.lang.Check;
import objectos.util.GrowableMap;
import objectos.util.IntArrays;

class Pass1 {

  interface Source {
    String substring(int start, int end);

    int tokenAt(int index);

    int tokens();
  }

  private static final Set<String> URL_MACROS = Set.of("https");

  private int attrCount;

  private final StringBuilder attributeValue = new StringBuilder();

  private int[] code;

  private int codeCursor;

  private int codeIndex;

  private final GrowableMap<String, String> docattr = new GrowableMap<>();

  private int[] list;

  private int listIndex = -1;

  private int[] section;

  private int sectionIndex = -1;

  private Source source;

  private int tokenIndex;

  private int tokenStart;

  private int tokenLength;

  boolean running = false;

  private int tokenEnd;

  Pass1() {
    code = new int[512];

    list = new int[8];

    section = new int[8];
  }

  public final void execute(Source source) {
    Check.state(
      !running,

      """
      Concurrent pass (1) is not supported.

      It seems a previous AsciiDoc document pass (1):

      - is currently running; or
      - finished abruptly (most likely due to a bug in this component, sorry...).
      """
    );

    running = true;

    reset(source);

    executeDocument();

    codeCursor = 0;
  }

  public final DocumentAttributes toDocumentAttributes() {
    return switch (docattr.size()) {
      case 0 -> DocumentAttributes.EMPTY;

      default -> DocumentAttributes.wrap(docattr.toUnmodifiableMap());
    };
  }

  final String attribute(String key) {
    return docattr.get(key);
  }

  final int codeAt(int index) {
    return code[index];
  }

  final int codeCursor() {
    return codeCursor;
  }

  final boolean hasCode() {
    return codeCursor < codeIndex;
  }

  final int nextCode() {
    return code[codeCursor++];
  }

  final int[] toCode() {
    return Arrays.copyOf(code, codeIndex);
  }

  private void add(int p0) {
    code = IntArrays.growIfNecessary(code, codeIndex);

    code[codeIndex++] = p0;
  }

  private void add(int p0, int p1) {
    code = IntArrays.growIfNecessary(code, codeIndex + 1);

    code[codeIndex++] = p0;
    code[codeIndex++] = p1;
  }

  private void add(int p0, int p1, int p2) {
    code = IntArrays.growIfNecessary(code, codeIndex + 2);

    code[codeIndex++] = p0;
    code[codeIndex++] = p1;
    code[codeIndex++] = p2;
  }

  private void add(int p0, int p1, int p2, int p3) {
    code = IntArrays.growIfNecessary(code, codeIndex + 3);

    code[codeIndex++] = p0;
    code[codeIndex++] = p1;
    code[codeIndex++] = p2;
    code[codeIndex++] = p3;
  }

  private void add(int p0, int p1, int p2, int p3, int p4) {
    code = IntArrays.growIfNecessary(code, codeIndex + 4);

    code[codeIndex++] = p0;
    code[codeIndex++] = p1;
    code[codeIndex++] = p2;
    code[codeIndex++] = p3;
    code[codeIndex++] = p4;
  }

  private void executeAttributeListBlock() {
    tokenIndex++; // Token.ATTR_LIST_START

    attrCount = 1;

    loop: do {

      var token = tokenAt(tokenIndex++);

      switch (token) {
        case Token.DQUOTE -> {}

        case Token.ATTR_NAME -> {
          var nameStart = tokenAt(tokenIndex++);
          var nameEnd = tokenAt(tokenIndex++);

          var value = tokenAt(tokenIndex++);

          if (value != Token.ATTR_VALUE_START) {
            throw new UnsupportedOperationException(
              "Implement me :: expecting Token.ATTR_VALUE_START but found=" + value);
          }

          executeAttributeValueEnd();

          add(Code.ATTR_NAMED, nameStart, nameEnd, tokenStart, tokenEnd);
        }

        case Token.ATTR_VALUE_START -> {
          executeAttributeValueEnd();

          add(Code.ATTR_POSITIONAL, attrCount, tokenStart, tokenEnd);
        }

        case Token.SEPARATOR -> {
          attrCount++;

          tokenIndex += 2; // start, end
        }

        case Token.ATTR_LIST_END -> { break loop; }

        default -> uoeToken(token);
      }

    } while (hasToken());
  }

  private void executeAttributeListLink() {
    tokenIndex++; // Token.ATTR_LIST_START

    attrCount = 1;

    var token = tokenAt(tokenIndex++);

    switch (token) {
      case Token.ATTR_VALUE_START -> {
        add(Code.URL_TARGET_START);

        executeAttributeListLinkText();
      }

      default -> uoeToken(token);
    }
  }

  private void executeAttributeListLinkText() {
    executeAttributeValueEnd();

    add(Code.TOKENS, tokenStart, tokenEnd);

    var token = tokenAt(tokenIndex++);

    switch (token) {
      case Token.ATTR_LIST_END -> {
        add(Code.URL_TARGET_END);
      }

      case Token.SEPARATOR -> executeAttributeListLinkTextComma();

      default -> uoeToken(token);
    }
  }

  private void executeAttributeListLinkTextComma() {
    var commaStart = tokenIndex - 1;
    tokenIndex += 2;
    var commaEnd = tokenIndex;

    add(Code.TOKENS, commaStart, commaEnd);

    var token = tokenAt(tokenIndex++);

    switch (token) {
      case Token.ATTR_VALUE_START -> executeAttributeListLinkText();

      default -> uoeToken(token);
    }
  }

  private void executeAttributeValueEnd() {
    tokenStart = tokenIndex;
    tokenEnd = 0;
    var marker = 0;

    do {
      tokenEnd = tokenIndex;

      marker = tokenAt(tokenIndex++);
    } while (marker != Token.ATTR_VALUE_END);
  }

  private void executeBlocks() {
    loop: do {

      var token = tokenAt(tokenIndex);

      switch (token) {
        case //
             Token.BLOB, //
             Token.BOLD_START, //
             Token.ITALIC_START, //
             Token.MONO_START, //
             Token.INLINE_MACRO -> executeParagraph();

        case Token.ATTR_LIST_START -> executeAttributeListBlock();

        case Token.LISTING_BLOCK_DELIM -> executeListingBlock();

        case Token.ULIST_ASTERISK -> executeUnorderedListAsteriskStart();

        case Token.ULIST_HYPHEN -> executeUnorderedListHyphenStart();

        case Token.HEADING -> { break loop; }

        case Token.LF -> { tokenIndex++; }

        case Token.EOF -> { tokenIndex++; break loop; }

        default -> uoeToken(token);
      }

    } while (hasToken());
  }

  private void executeDocument() {
    add(Code.DOCUMENT_START);

    if (hasToken()) {
      executeHeader();
    }

    if (hasToken()) {
      executePreamble();
    }

    if (hasToken()) {
      executeSections();
    }

    add(Code.DOCUMENT_END);
  }

  private void executeDocumentAttributes() {
    loop: do {

      var token = tokenAt(tokenIndex);

      switch (token) {
        case Token.EOF -> { tokenIndex++; break loop; }

        case Token.DOCATTR -> executeDocumentAttributes0();

        default -> { break loop; }
      }

    } while (hasToken());
  }

  private void executeDocumentAttributes0() {
    tokenIndex++; // skip Token.DOCATTR

    var start = tokenAt(tokenIndex++);
    var end = tokenAt(tokenIndex++);

    var attributeName = source.substring(start, end);

    attributeValue.setLength(0);

    loop: do {

      var token = tokenAt(tokenIndex);

      switch (token) {
        case Token.BLOB -> {
          tokenIndex++; // skip Token.BLOB

          var s = source.substring(
            tokenAt(tokenIndex++),
            tokenAt(tokenIndex++)
          );

          attributeValue.append(s);
        }

        case Token.LF -> {
          var value = attributeValue.toString();

          docattr.put(attributeName, value);

          tokenIndex++;

          break loop;
        }

        default -> uoeToken(token);
      }

    } while (hasToken());
  }

  private void executeHeader() {
    var token = tokenAt(tokenIndex);

    if (token != Token.HEADING) {
      return;
    }

    var level = tokenAt(tokenIndex + 1);

    if (level != 1) {
      return;
    }

    tokenIndex += 2;

    executeHeading(level);

    if (hasToken()) {
      executeDocumentAttributes();
    }
  }

  private void executeHeading(int level) {
    tokenIndex += 2; // skip start,end

    add(Code.HEADING_START, level);

    tokenStart = tokenIndex;

    loop: do {

      var token = tokenAt(tokenIndex);

      switch (token) {
        case Token.BLOB -> tokenIndex += 3;

        case
             Token.BOLD_START, Token.BOLD_END,
             Token.ITALIC_START, Token.ITALIC_END,
             Token.MONO_START, Token.MONO_END,
             Token.APOSTROPHE -> tokenIndex += 2;

        case Token.INLINE_MACRO -> { executeInlineMacro(); tokenStart = tokenIndex; }

        case Token.LF, Token.EOF -> {
          // do not include LF/EOF in heading
          add(Code.TOKENS, tokenStart, tokenIndex);

          tokenIndex++;

          break loop;
        }

        default -> uoeToken(token);
      }

    } while (hasToken());

    add(Code.HEADING_END, level);
  }

  private void executeInlineMacro() {
    var tokenEnd = tokenIndex;

    if (tokenStart < tokenEnd) {
      add(Code.TOKENS, tokenStart, tokenEnd);
    }

    tokenIndex++; // Token.INLINE_MACRO;

    var start = tokenAt(tokenIndex++);
    var end = tokenAt(tokenIndex++);

    var name = source.substring(start, end);

    if (URL_MACROS.contains(name)) {
      // target
      var blob = tokenAt(tokenIndex++);

      if (blob != Token.BLOB) {
        throw new UnsupportedOperationException("Implement me");
      }

      tokenIndex++; // start
      end = tokenAt(tokenIndex++);

      add(Code.URL_MACRO, start, end);

      // attrlist
      var attrlist = tokenAt(tokenIndex);

      if (attrlist != Token.ATTR_LIST_START) {
        throw new UnsupportedOperationException("Implement me");
      }

      executeAttributeListLink();
    } else {
      add(Code.INLINE_MACRO, start, end);

      // target
      var blob = tokenAt(tokenIndex++);

      if (blob != Token.BLOB) {
        throw new UnsupportedOperationException("Implement me");
      }

      start = tokenAt(tokenIndex++);
      end = tokenAt(tokenIndex++);

      add(Code.MACRO_TARGET, start, end);

      // attrlist
      var attrlist = tokenAt(tokenIndex);

      if (attrlist != Token.ATTR_LIST_START) {
        throw new UnsupportedOperationException("Implement me");
      }

      executeAttributeListBlock();
    }
  }

  private void executeListingBlock() {
    add(Code.LISTING_BLOCK_START);

    tokenIndex++; // Token.LISTING_BLOCK_START

    var dashes = tokenAt(tokenIndex++);

    pushSection(dashes);

    var token = tokenAt(tokenIndex++);

    if (token != Token.LF) {
      throw new UnsupportedOperationException("Implement me");
    }

    tokenStart = tokenIndex;

    loop: do {

      token = tokenAt(tokenIndex);

      switch (token) {
        case Token.BLOB,
             Token.INLINE_MACRO,
             Token.LITERALI -> tokenIndex += 3;

        case Token.BOLD_END,
             Token.ITALIC_END,
             Token.ITALIC_START,
             Token.MONO_END -> tokenIndex += 2;

        case Token.ATTR_LIST_START,
             Token.ATTR_LIST_END,
             Token.ATTR_VALUE_START,
             Token.ATTR_VALUE_END,
             Token.LF -> tokenIndex += 1;

        case Token.LISTING_BLOCK_DELIM -> {
          tokenIndex++;

          dashes = tokenAt(tokenIndex);

          var previous = popSection();

          if (dashes != previous) {
            pushSection(previous);

            throw new UnsupportedOperationException("Implement me :: literal dashes?");
          }

          var tokenEnd = tokenIndex - 2; // dashes, last LF

          add(Code.VERBATIM, tokenStart, tokenEnd);

          tokenIndex++;

          break loop;
        }

        default -> uoeToken(token);
      }

    } while (hasToken());

    add(Code.LISTING_BLOCK_END);
  }

  private void executeListItem() {
    add(Code.LI_START);

    tokenStart = tokenIndex;

    var tokenEnd = tokenIndex;

    var newLine = 0;

    loop: do {

      var token = source.tokenAt(tokenIndex);

      switch (token) {
        case Token.BLOB -> {
          if (newLine > 1) {
            tokenEnd = tokenIndex - newLine;

            break loop;
          }

          tokenIndex += 3;

          newLine = 0;
        }

        case
             Token.BOLD_START, Token.BOLD_END,
             Token.ITALIC_START, Token.ITALIC_END,
             Token.MONO_START, Token.MONO_END,
             Token.APOSTROPHE -> {
          if (newLine > 1) {
            tokenEnd = tokenIndex - newLine;

            break loop;
          }

          tokenIndex += 2;

          newLine = 0;
        }

        case Token.INLINE_MACRO -> {
          executeInlineMacro();

          newLine = 0;

          tokenStart = tokenIndex;
          tokenEnd = tokenIndex;
        }

        case Token.ULIST_ASTERISK -> {
          var count = tokenAt(tokenIndex + 1);

          if (shouldNest('*', count)) {
            if (tokenStart < tokenEnd) {
              add(Code.TOKENS, tokenStart, tokenEnd);
            }

            executeUnorderedListAsteriskStart();
          }

          break loop;
        }

        case Token.ULIST_HYPHEN -> {
          if (shouldNest('-', 1)) {
            if (tokenStart < tokenEnd) {
              add(Code.TOKENS, tokenStart, tokenEnd);
            }

            executeUnorderedListHyphenStart();
          }

          break loop;
        }

        case Token.LF -> {
          tokenEnd = tokenIndex++;

          newLine++;
        }

        case Token.EOF -> {
          if (newLine == 0) {
            tokenEnd = tokenIndex;
          }

          tokenIndex++;

          newLine = 0;

          break loop;
        }

        case Token.HEADING, Token.ATTR_LIST_START -> {
          if (newLine > 1) {
            tokenEnd = tokenIndex - newLine;
          }

          break loop;
        }

        default -> uoeToken(token);
      }

    } while (hasToken());

    if (tokenStart < tokenEnd) {
      add(Code.TOKENS, tokenStart, tokenEnd);
    }

    add(Code.LI_END);
  }

  private void executeListItems() {
    loop: do {

      var token = source.tokenAt(tokenIndex);

      switch (token) {
        case Token.ULIST_ASTERISK -> {
          var _count = tokenAt(tokenIndex + 1);

          if (sameList('*', _count)) {
            tokenIndex += 4; // Token.ULIST_ASTERISK, count, start, end

            executeListItem();
          } else {
            break loop;
          }
        }

        case Token.ULIST_HYPHEN -> {
          if (sameList('-', 1)) {
            tokenIndex += 3; // Token.ULIST_HYPHEN, start, end

            executeListItem();
          } else {
            break loop;
          }
        }

        case
             Token.HEADING,
             Token.ATTR_LIST_START,
             Token.BLOB,
             Token.MONO_START,
             Token.INLINE_MACRO -> {
          break loop;
        }

        default -> uoeToken(token);
      }

    } while (hasToken());
  }

  private void executeParagraph() {
    add(Code.PARAGRAPH_START);

    tokenStart = tokenIndex;

    var newLine = 0;

    loop: do {

      var token = tokenAt(tokenIndex);

      switch (token) {
        case Token.BLOB -> { tokenIndex += 3; newLine = 0; }

        case
             Token.BOLD_START, Token.BOLD_END,
             Token.ITALIC_START, Token.ITALIC_END,
             Token.MONO_START, Token.MONO_END,
             Token.APOSTROPHE -> {
          tokenIndex += 2;

          newLine = 0;
        }

        case Token.INLINE_MACRO -> {
          executeInlineMacro();

          newLine = 0;

          tokenStart = tokenIndex;
        }

        case Token.LF -> {
          tokenIndex++;

          newLine++;

          if (newLine > 1) {
            var tokenEnd = tokenIndex - newLine;

            add(Code.TOKENS, tokenStart, tokenEnd);

            break loop;
          }
        }

        case Token.EOF -> {
          var tokenEnd = tokenIndex;

          if (newLine > 0) {
            tokenEnd -= newLine;
          }

          add(Code.TOKENS, tokenStart, tokenEnd);

          tokenIndex++;

          break loop;
        }

        default -> uoeToken(token);
      }

    } while (hasToken());

    add(Code.PARAGRAPH_END);
  }

  private void executePreamble() {
    var token = tokenAt(tokenIndex);

    while (token == Token.LF) {
      tokenIndex++;

      token = tokenAt(tokenIndex);
    }

    switch (token) {
      case Token.EOF -> {
        tokenIndex++;

        return;
      }

      case Token.HEADING -> {
        return;
      }

      case Token.ATTR_LIST_START -> {
        var offset = 1;

        while (tokenAt(tokenIndex + offset) != Token.ATTR_LIST_END) {
          offset++;
        }

        offset++;

        if (tokenAt(tokenIndex + offset) != Token.LF) {
          throw new UnsupportedOperationException("Implement me");
        }

        offset++;

        var maybeHeading = tokenAt(tokenIndex + offset);

        if (maybeHeading == Token.HEADING) {
          return;
        }
      }
    }

    add(Code.PREAMBLE_START);

    executeBlocks();

    add(Code.PREAMBLE_END);
  }

  private void executeSections() {
    do {

      var token = tokenAt(tokenIndex++);

      if (token == Token.ATTR_LIST_START) {
        tokenIndex--;

        executeAttributeListBlock();

        var nl = tokenAt(tokenIndex++);

        if (nl != Token.LF) {
          throw new UnsupportedOperationException("Implement me");
        } else {
          token = tokenAt(tokenIndex++);
        }
      }

      if (token != Token.HEADING) {
        throw new UnsupportedOperationException("Implement me :: token=" + token);
      }

      var level = tokenAt(tokenIndex++);

      var section = level - 1;

      if (!hasSection()) {
        pushSection(section);
      } else {
        var prevSection = popSection();

        if (section > prevSection) {
          pushSection(prevSection);

          pushSection(section);
        }

        else {
          add(Code.SECTION_END);

          pushSection(section);
        }
      }

      add(Code.SECTION_START, section);

      executeHeading(level);

      executeBlocks();

    } while (hasToken());

    while (hasSection()) {
      add(Code.SECTION_END);

      popSection();
    }
  }

  private void executeUnorderedList(char symbol, int count) {
    add(Code.ULIST_START);

    pushList(symbol, count);

    executeListItems();

    popList();

    add(Code.ULIST_END);
  }

  private void executeUnorderedListAsteriskStart() {
    var count = tokenAt(tokenIndex + 1);

    executeUnorderedList('*', count);
  }

  private void executeUnorderedListHyphenStart() {
    executeUnorderedList('-', 1);
  }

  private boolean hasList() {
    return listIndex >= 0;
  }

  private boolean hasSection() {
    return sectionIndex >= 0;
  }

  private boolean hasToken() {
    return tokenIndex < tokenLength;
  }

  private int peekListCount() {
    return list[listIndex - 1];
  }

  private char peekListSymbol() {
    return (char) list[listIndex];
  }

  private void popList() {
    listIndex -= 2;
  }

  private int popSection() {
    return section[sectionIndex--];
  }

  private void pushList(char symbol, int count) {
    list = IntArrays.growIfNecessary(list, listIndex + 2);

    list[++listIndex] = count;
    list[++listIndex] = symbol;
  }

  private void pushSection(int level) {
    sectionIndex++;

    section = IntArrays.growIfNecessary(section, sectionIndex);

    section[sectionIndex] = level;
  }

  private void reset(Source source) {
    this.source = source;

    codeIndex = 0;

    docattr.clear();

    listIndex = -1;

    sectionIndex = -1;

    tokenIndex = 0;

    tokenLength = source.tokens();
  }

  private boolean sameList(char symbol, int count) {
    var prevSymbol = peekListSymbol();
    var prevCount = peekListCount();

    return symbol == prevSymbol
        && count == prevCount;
  }

  private boolean shouldNest(char symbol, int count) {
    if (!hasList()) {
      return false;
    }

    var prevSymbol = peekListSymbol();

    if (prevSymbol != symbol) {
      return true;
    }

    var prevCount = peekListCount();

    return count > prevCount;
  }

  private int tokenAt(int index) {
    return source.tokenAt(index);
  }

  private void uoeToken(int token) {
    throw new UnsupportedOperationException("Implement me :: token=" + token);
  }

}