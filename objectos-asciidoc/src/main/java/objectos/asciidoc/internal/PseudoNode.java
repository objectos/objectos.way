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
package objectos.asciidoc.internal;

import java.io.IOException;
import java.util.NoSuchElementException;
import objectos.asciidoc.pseudom.Node;

abstract class PseudoNode {

  private static final int INLINE_MACRO_MAX_LENGTH = 20;

  private static final int ATTRLIST_BLOCK = -1;

  private static final int ATTRLIST_INLINE = -2;

  static final int _ULIST_TOP = -3;

  static final int _NEXT_ITEM = -4;

  static final int _NESTED = -5;

  static final int _NESTED_END = -6;

  static final int _LIST_END = -7;

  final InternalSink sink;

  PseudoNode(InternalSink sink) {
    this.sink = sink;
  }

  public abstract boolean hasNext();

  final <E extends Enum<E>> E advance(E state) {
    sink.sourceAdvance();

    return state;
  }

  final int advance(int state) {
    sink.sourceAdvance();

    return state;
  }

  final void appendTo(Appendable out, int start, int end) throws IOException {
    sink.appendTo(out, start, end);
  }

  final void closeImpl() throws IOException {
    sink.close();
  }

  final boolean hasNextNode() {
    return sink.nextNode != null;
  }

  final PseudoHeader header() {
    return sink.pseudoHeader();
  }

  final PseudoTitle heading() {
    return sink.pseudoTitle();
  }

  final void nextNode(Node value) {
    sink.nextNode = value;
  }

  final Node nextNodeDefault() {
    if (hasNext()) {
      return sink.nextNode();
    } else {
      throw new NoSuchElementException();
    }
  }

  final Node nextNodeSink() {
    return sink.nextNode();
  }

  final PseudoParagraph paragraph() {
    return sink.pseudoParagraph();
  }

  final Parse parseDocumentOrSection(Parse state) {
    return switch (state) {
      case ATTRLIST -> parseAttrlist();

      case BODY -> parseBody();

      case MAYBE_ATTRLIST -> parseMaybeAttrlist();

      case MAYBE_ATTRLIST_END -> parseMaybeAttrlistEnd();

      case MAYBE_ATTRLIST_END_TRIM -> parseMaybeAttrlistEndTrim();

      case MAYBE_BOLD_OR_ULIST -> parseMaybeBoldOrUlist();

      case MAYBE_LISTING_OR_ULIST -> parseMaybeListingOrUlist();

      case MAYBE_SECTION -> parseMaybeSection();

      case MAYBE_SECTION_TRIM -> parseMaybeSectionTrim();

      case MAYBE_ULIST -> parseMaybeUlist();

      case NAME_OR_VALUE -> parseNameOrValue();

      case NOT_SECTION -> parseNotSection();

      default -> throw new UnsupportedOperationException(
        "Implement me :: state=" + state
      );
    };
  }

  final void phrasing() {
    var state = Phrasing.START;

    stackPush(sourceIndex());

    while (state != Phrasing.STOP) {
      state = switch (state) {
        case AUTOLINK -> phrasingAutolink();

        case BLOB -> phrasingBlob();

        case CUSTOM_INLINE_MACRO -> phrasingCustomInlineMacro();

        case EOL -> phrasingEol();

        case INLINE_MACRO -> phrasingInlineMacro();

        case INLINE_MACRO_END -> phrasingInlineMacroEnd();

        case START -> phrasingStart();

        case STOP -> throw new UnsupportedOperationException("Implement me");

        case TEXT -> phrasingText();

        case URI_MACRO -> phrasingUriMacro();

        case URI_MACRO_ATTRLIST -> phrasingUriMacroAttrlist();

        case URI_MACRO_ROLLBACK -> phrasingUriMacroRollback();

        case URI_MACRO_TARGET -> phrasingUriMacroTarget();

        case URI_MACRO_TARGET_LOOP -> phrasingUriMacroTargetLoop();
      };
    }
  }

  Phrasing phrasingEol() {
    throw new UnsupportedOperationException("Implement me");
  }

  Phrasing phrasingStart() {
    throw new UnsupportedOperationException("Implement me");
  }

  final Phrasing popAndStop() {
    // pops start index
    stackPop();

    return Phrasing.STOP;
  }

  final PseudoAttributes pseudoAttributes() {
    return sink.pseudoAttributes();
  }

  final PseudoSection section() {
    return sink.pseudoSection();
  }

  final void sourceAdvance() {
    sink.sourceAdvance();
  }

  final String sourceGet(int start, int end) {
    return sink.sourceGet(start, end);
  }

  final boolean sourceInc() {
    return sink.sourceInc();
  }

  final int sourceIndex() {
    return sink.sourceIndex();
  }

  final void sourceIndex(int value) {
    sink.sourceIndex(value);
  }

  final boolean sourceMore() {
    return sink.sourceMore();
  }

  final char sourceNext() {
    return sink.sourceNext();
  }

  final char sourcePeek() {
    return sink.sourcePeek();
  }

  final char sourcePeek(int offset) {
    return sink.sourcePeek(offset);
  }

  final int sourceStub() {
    return sink.sourceStub();
  }

  final <E extends Enum<E>> E sourceStub(E state) {
    sourceStub();

    return state;
  }

  final void stackAssert(int expected) {
    int actual = stackPeek();

    assert actual == expected : "actual=" + actual + ";expected=" + expected;
  }

  final void stackDec() {
    sink.stackDec();
  }

  final void stackInc() {
    sink.stackInc();
  }

  final int stackPeek() {
    return sink.stackPeek();
  }

  final int stackPeek(int offset) {
    return sink.stackPeek(offset);
  }

  final int stackPop() {
    return sink.stackPop();
  }

  final void stackPop(int count) {
    sink.stackPop(count);
  }

  final void stackPush(int v0) {
    sink.stackPush(v0);
  }

  final void stackPush(int v0, int v1) {
    sink.stackPush(v0, v1);
  }

  final void stackReplace(int value) {
    sink.stackReplace(value);
  }

  final int stackStub() {
    sink.stackStub();

    return Integer.MIN_VALUE;
  }

  final Phrasing toPhrasingEnd(int last) {
    stackPush(last);

    return Phrasing.TEXT;
  }

  final PseudoUnorderedList unorderedList() {
    return sink.pseudoUnorderedList();
  }

  private boolean isWord(char c) {
    int type = Character.getType(c);

    return switch (type) {
      case Character.LOWERCASE_LETTER,
           Character.MODIFIER_LETTER,
           Character.OTHER_LETTER,
           Character.TITLECASE_LETTER,
           Character.UPPERCASE_LETTER,

           Character.NON_SPACING_MARK,
           Character.COMBINING_SPACING_MARK,
           Character.ENCLOSING_MARK,

           Character.DECIMAL_DIGIT_NUMBER,
           Character.LETTER_NUMBER,
           Character.OTHER_NUMBER,

           Character.CONNECTOR_PUNCTUATION -> true;

      default -> false;
    };
  }

  private Parse parseAttrlist() {
    int type = stackPop();

    // pops rollback index
    stackPop();

    pseudoAttributes().active();

    return switch (type) {
      case ATTRLIST_BLOCK -> Parse.BODY;

      case ATTRLIST_INLINE -> Parse.INLINE_ATTRLIST;

      default -> throw new AssertionError(
        "Unexpected attrlist type=" + type
      );
    };
  }

  private Parse parseBody() {
    if (!sourceMore()) {
      return Parse.EXHAUSTED;
    }

    return switch (sourcePeek()) {
      case '\n' -> advance(Parse.BODY_TRIM);

      case '[' -> {
        // rollback index
        stackPush(sourceIndex(), ATTRLIST_BLOCK);

        yield advance(Parse.MAYBE_ATTRLIST);
      }

      case '-' -> {
        // rollback index or marker start
        stackPush(sourceIndex());

        yield advance(Parse.MAYBE_LISTING_OR_ULIST);
      }

      case '*' -> {
        // rollback index or marker start
        stackPush(sourceIndex());

        yield advance(Parse.MAYBE_BOLD_OR_ULIST);
      }

      case '=' -> {
        stackPush(sourceIndex());

        // push title level
        stackPush(0);

        yield advance(Parse.MAYBE_SECTION);
      }

      default -> Parse.PARAGRAPH;
    };
  }

  private Parse parseMaybeAttrlist() {
    if (!sourceMore()) {
      return Parse.NOT_ATTRLIST;
    }

    return switch (sourcePeek()) {
      case 't', ' ' -> Parse.NOT_ATTRLIST;

      default -> {
        var attributes = sink.pseudoAttributes();

        attributes.clear();

        // attr name/value start
        stackPush(sourceIndex());

        yield advance(Parse.NAME_OR_VALUE);
      }
    };
  }

  private Parse parseMaybeAttrlistEnd() {
    int context = stackPeek();

    if (!sourceMore()) {
      // pop rollback index
      stackPop();

      throw new UnsupportedOperationException(
        "Implement me :: doc ends w/ attrlist"
      );
    }

    return switch (context) {
      case ATTRLIST_BLOCK -> Parse.MAYBE_ATTRLIST_END_TRIM;

      case ATTRLIST_INLINE -> Parse.ATTRLIST;

      default -> throw new AssertionError(
        "Unexpected context = " + context
      );
    };
  }

  private Parse parseMaybeAttrlistEndTrim() {
    if (!sourceMore()) {
      // pop rollback index
      stackPop();

      throw new UnsupportedOperationException(
        "Implement me :: doc ends w/ attrlist"
      );
    }

    return switch (sourcePeek()) {
      case '\t', '\f', ' ' -> advance(Parse.MAYBE_ATTRLIST_END_TRIM);

      case '\n' -> advance(Parse.ATTRLIST);

      default -> {
        throw new UnsupportedOperationException(
          "Implement me :: rollback attrlist"
        );
      }
    };
  }

  private Parse parseMaybeBoldOrUlist() {
    if (!sourceMore()) {
      return Parse.NOT_BOLD_OR_ULIST;
    }

    return switch (sourcePeek()) {
      case '\t', '\f', ' ' -> toMaybeUlist();

      case '*' -> throw new UnsupportedOperationException("Implement me");

      default -> Parse.NOT_LISTING_OR_ULIST;
    };
  }

  private Parse parseMaybeListingOrUlist() {
    if (!sourceMore()) {
      return Parse.NOT_LISTING_OR_ULIST;
    }

    return switch (sourcePeek()) {
      case '\t', '\f', ' ' -> toMaybeUlist();

      case '-' -> throw new UnsupportedOperationException("Implement me");

      default -> Parse.NOT_LISTING_OR_ULIST;
    };
  }

  private Parse parseMaybeSection() {
    if (!sourceMore()) {
      return Parse.NOT_SECTION;
    }

    return switch (sourcePeek()) {
      case '=' -> {
        // increase title level
        stackInc();

        yield advance(Parse.MAYBE_SECTION);
      }

      case '\t', '\f', ' ' -> advance(Parse.MAYBE_SECTION_TRIM);

      default -> Parse.NOT_SECTION;
    };
  }

  private Parse parseMaybeSectionTrim() {
    if (!sourceMore()) {
      return Parse.NOT_SECTION;
    }

    return switch (sourcePeek()) {
      case '\t', '\f', ' ' -> advance(Parse.MAYBE_SECTION_TRIM);

      default -> Parse.SECTION;
    };
  }

  private Parse parseMaybeUlist() {
    if (!sourceMore()) {
      return Parse.NOT_ULIST;
    }

    return switch (sourcePeek()) {
      case '\t', '\f', ' ' -> advance(Parse.MAYBE_ULIST);

      case '\n' -> Parse.NOT_ULIST;

      default -> Parse.ULIST;
    };
  }

  private Parse parseNameOrValue() {
    if (!sourceMore()) {
      return Parse.NOT_ATTRLIST;
    }

    return switch (sourcePeek()) {
      case ']' -> {
        parsePositional();

        yield advance(Parse.MAYBE_ATTRLIST_END);
      }

      default -> advance(Parse.NAME_OR_VALUE);
    };
  }

  private Parse parseNotSection() {
    // pops section level
    stackPop();

    sourceIndex(stackPop());

    return Parse.PARAGRAPH;
  }

  private void parsePositional() {
    int start = stackPop();

    int end = sourceIndex();

    var value = sink.sourceGet(start, end);

    var attributes = sink.pseudoAttributes();

    attributes.add(value);
  }

  private Phrasing phrasingAutolink() {
    throw new UnsupportedOperationException("Implement me");
  }

  private Phrasing phrasingBlob() {
    if (!sourceMore()) {
      return toPhrasingEnd(sourceIndex());
    }

    return switch (sourcePeek()) {
      case '\n' -> Phrasing.EOL;

      case '`' -> throw new UnsupportedOperationException("Implement me");

      case '*' -> throw new UnsupportedOperationException("Implement me");

      case '_' -> throw new UnsupportedOperationException("Implement me");

      case ':' -> Phrasing.INLINE_MACRO;

      default -> advance(Phrasing.BLOB);
    };
  }

  private Phrasing phrasingCustomInlineMacro() {
    throw new UnsupportedOperationException("Implement me");
  }

  private Phrasing phrasingInlineMacro() {
    int phrasingStart = stackPeek();

    int colon = sourceIndex();

    int min = Math.max(phrasingStart, colon - INLINE_MACRO_MAX_LENGTH);

    int index = colon;

    var found = false;

    loop: while (index > min) {
      int peekIndex = index - 1;

      char peek = sink.sourceAt(peekIndex);

      if (!isWord(peek)) {
        found = true;

        break loop;
      }

      index = peekIndex;
    }

    if (index == 0) {
      found = true;
    }

    if (!found) {
      throw new UnsupportedOperationException(
        "Implement me :: not inline macro :: index=" + index
      );
    }

    int nameStart = index;

    int nameEnd = colon;

    var name = sourceGet(nameStart, nameEnd);

    var macro = sink.pseudoInlineMacro();

    macro.name = name;

    return switch (name) {
      case "https" -> Phrasing.URI_MACRO;

      default -> {
        throw new UnsupportedOperationException(
          "Implement me :: name=" + name
        );
      }
    };
  }

  private Phrasing phrasingInlineMacroEnd() {
    // phrasing start
    // make TC01 pass for now
    stackPop();

    nextNode(sink.pseudoInlineMacro());

    return Phrasing.STOP;
  }

  private Phrasing phrasingText() {
    int endIndex = stackPop();

    int startIndex = stackPop();

    if (startIndex < endIndex) {
      var text = sink.pseudoText();

      text.end = endIndex;

      text.start = startIndex;

      nextNode(text);
    } else {
      nextNode(null);
    }

    return Phrasing.STOP;
  }

  /*
  
  asciidoctor/lib/asciidoctor/rx.rb
  
  # Matches an implicit link and some of the link inline macro.
  #
  # Examples
  #
  #   https://github.com
  #   https://github.com[GitHub]
  #   <https://github.com>
  #   link:https://github.com[]
  #   "https://github.com[]"
  #   (https://github.com) <= parenthesis not included in autolink
  #
  InlineLinkRx = %r((^|link:|#{CG_BLANK}|&lt;|[>\(\)\[\];"'])(\\?(?:https?|file|ftp|irc)://)(?:([^\s\[\]]+)\[(|#{CC_ALL}*?[^\\])\]|([^\s\[\]<]*([^\s,.?!\[\]<\)]))))m

  CG_BLANK=\p{Blank}
  CG_ALL=.

  (^|link:|\p{Blank}|&lt;|[>\(\)\[\];"'])(\\?(?:https?|file|ftp|irc)://)(?:([^\s\[\]]+)\[(|.*?[^\\])\]|([^\s\[\]<]*([^\s,.?!\[\]<\)])))

  */

  private Phrasing phrasingUriMacro() {
    if (!sourceInc()) {
      return Phrasing.URI_MACRO_ROLLBACK;
    }

    if (sourcePeek() != '/') {
      return Phrasing.URI_MACRO_ROLLBACK;
    }

    if (!sourceInc()) {
      return Phrasing.URI_MACRO_ROLLBACK;
    }

    if (sourcePeek() != '/') {
      return Phrasing.URI_MACRO_ROLLBACK;
    }

    if (!sourceInc()) {
      return Phrasing.URI_MACRO_ROLLBACK;
    }

    return switch (sourcePeek()) {
      case '\t', '\f', ' ' -> Phrasing.URI_MACRO_ROLLBACK;

      case '[' -> Phrasing.URI_MACRO_ROLLBACK;

      default -> Phrasing.URI_MACRO_TARGET;
    };
  }

  private Phrasing phrasingUriMacroAttrlist() {
    int sourceIndex = sourceIndex();

    if (!sourceInc()) {
      return Phrasing.URI_MACRO_ROLLBACK;
    }

    var macro = sink.pseudoInlineMacro();

    macro.targetEnd = sourceIndex;

    var found = false;

    // attrlist rollback info
    stackPush(sourceIndex, ATTRLIST_INLINE);

    var parse = Parse.MAYBE_ATTRLIST;

    loop: while (parse != Parse.STOP) {
      parse = parseDocumentOrSection(parse);

      switch (parse) {
        case NOT_ATTRLIST -> {
          found = false;

          break loop;
        }

        case INLINE_ATTRLIST -> {
          found = true;

          break loop;
        }

        default -> {
          continue loop;
        }
      }
    }

    if (!found) {
      return Phrasing.URI_MACRO_ROLLBACK;
    } else {
      return Phrasing.INLINE_MACRO_END;
    }
  }

  private Phrasing phrasingUriMacroRollback() {
    throw new UnsupportedOperationException(
      "Implement me :: rollback url macro"
    );
  }

  private Phrasing phrasingUriMacroTarget() {
    var macro = sink.pseudoInlineMacro();

    macro.targetStart = sourceIndex();

    return Phrasing.URI_MACRO_TARGET_LOOP;
  }

  private Phrasing phrasingUriMacroTargetLoop() {
    if (!sourceInc()) {
      return Phrasing.AUTOLINK;
    }

    return switch (sourcePeek()) {
      case '\t', '\f', ' ' -> Phrasing.AUTOLINK;

      case '[' -> Phrasing.URI_MACRO_ATTRLIST;

      default -> Phrasing.URI_MACRO_TARGET_LOOP;
    };
  }

  private Parse toMaybeUlist() {
    int markerStart = stackPop();

    stackPush(_ULIST_TOP, markerStart);

    // marker end
    stackPush(sourceIndex());

    return advance(Parse.MAYBE_ULIST);
  }

}