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
import java.util.function.Function;
import objectos.asciidoc.pseudom.Document;
import objectos.asciidoc.pseudom.Node;
import objectos.asciidoc.pseudom.Phrase;
import objectos.lang.Check;
import objectos.util.IntArrays;

public class InternalSink {

  /*
  
  CC_WORD = CG_WORD = '\p{Word}'
  QuoteAttributeListRxt = %(\\[([^\\[\\]]+)\\])
  %(\[([^\[\]]+)\])
  CC_ALL = '.'
  
  [:strong, :constrained, /(^|[^#{CC_WORD};:}])(?:#{QuoteAttributeListRxt})?\*(\S|\S#{CC_ALL}*?\S)\*(?!#{CG_WORD})/m]
  
  /./m - Any character (the m modifier enables multiline mode)
  /\S/ - A non-whitespace character: /[^ \t\r\n\f\v]/
  
   */

  private enum HeaderParse {
    STOP,
    AFTER_TITLE,
    HEADER_END,
    EXHAUSTED;
  }

  private enum PhraseElement {
    TITLE;
  }

  private enum Phrasing {
    START,

    STOP,

    BLOB,

    TEXT,

    EOL,

    INLINE_MACRO,
    INLINE_MACRO_END,

    CUSTOM_INLINE_MACRO,

    URI_MACRO,
    URI_MACRO_ATTRLIST,
    URI_MACRO_ROLLBACK,
    URI_MACRO_TARGET,
    URI_MACRO_TARGET_LOOP,

    AUTOLINK;
  }

  private static final int ATTRLIST_BLOCK = -1;
  private static final int ATTRLIST_INLINE = -2;
  private static final int _ULIST_TOP = -3;

  private static final int PSEUDO_DOCUMENT = 0;
  private static final int PSEUDO_HEADER = 1;
  private static final int PSEUDO_TITLE = 2;
  private static final int PSEUDO_PARAGRAPH = 3;
  private static final int PSEUDO_SECTION = 4;
  private static final int PSEUDO_TEXT = 5;
  private static final int PSEUDO_ULIST = 6;
  private static final int PSEUDO_ATTRIBUTES = 7;
  private static final int PSEUDO_INLINE_MACRO = 8;
  private static final int PSEUDO_PHRASE = 9;
  private static final int PSEUDO_LENGTH = 10;

  private final Object[] pseudoArray = new Object[PSEUDO_LENGTH];

  Node nextNode;

  private PhraseElement phrase;

  private String source;

  private int sourceIndex;

  private int[] stackArray = new int[8];

  private int stackIndex = -1;

  protected final Document openDocument(String source) {
    Check.state(
      finalState(),

      """
      Concurrent processing is not supported.

      It seems a previous AsciiDoc document processing:

      - is currently running; or
      - finished abruptly (most likely due to a bug in this component, sorry...).
      """
    );

    start(source);

    stackPush(PseudoDocument.START);

    return pseudoDocument();
  }

  protected final Phrase openPhrase(String source) {
    Check.state(
      finalState(),

      """
      Concurrent processing is not supported.

      It seems a previous AsciiDoc document processing:

      - is currently running; or
      - finished abruptly (most likely due to a bug in this component, sorry...).
      """
    );

    start(source);

    var phrase = pseudoPhrase();

    phrase.start();

    return phrase;
  }

  final void appendTo(Appendable out, int start, int end) throws IOException {
    out.append(source, start, end);
  }

  final void close() throws IOException {
    nextNode = null;

    source = null;

    stackIndex = -1;
  }

  final boolean documentHasNext() {
    switch (stackPeek()) {
      case PseudoHeader.EXHAUSTED,
           PseudoParagraph.EXHAUSTED,
           PseudoSection.EXHAUSTED,
           PseudoUnorderedList.EXHAUSTED -> documentParse(Parse.BODY);

      case PseudoDocument.ITERATOR -> documentParse(Parse.DOCUMENT_START);

      case PseudoDocument.HEADER,
           PseudoDocument.PARAGRAPH,
           PseudoDocument.SECTION,
           PseudoDocument.ULIST -> {}

      default -> stackStub();
    }

    return nextNode != null;
  }

  final void documentIterator() {
    stackAssert(PseudoDocument.NODES);

    stackReplace(PseudoDocument.ITERATOR);
  }

  final void documentNodes() {
    stackAssert(PseudoDocument.START);

    stackReplace(PseudoDocument.NODES);
  }

  final boolean headerHasNext() {
    switch (stackPeek()) {
      case PseudoTitle.EXHAUSTED -> headerParse(HeaderParse.AFTER_TITLE);

      case PseudoHeader.ITERATOR -> {
        stackPop();

        var title = pseudoTitle();

        title.level = 0;

        nextNode = title;

        stackPush(PseudoHeader.TITLE);
      }

      case PseudoHeader.TITLE -> {}

      default -> stackStub();
    }

    return nextNode != null;
  }

  final void headerIterator() {
    stackAssert(PseudoHeader.NODES);

    stackReplace(PseudoHeader.ITERATOR);
  }

  final void headerNodes() {
    stackAssert(PseudoDocument.HEADER_CONSUMED);

    stackReplace(PseudoHeader.NODES);
  }

  final Node nextNode() {
    var result = nextNode;

    nextNode = null;

    // change state to next adjacent state
    // e.g. HEADING -> HEADING_CONSUMED
    stackDec();

    return result;
  }

  final PseudoAttributes pseudoAttributes() {
    return pseudoFactory(PSEUDO_ATTRIBUTES, PseudoAttributes::new);
  }

  final PseudoHeader pseudoHeader() {
    return pseudoFactory(PSEUDO_HEADER, PseudoHeader::new);
  }

  final PseudoInlineMacro pseudoInlineMacro() {
    return pseudoFactory(PSEUDO_INLINE_MACRO, PseudoInlineMacro::new);
  }

  final PseudoParagraph pseudoParagraph() {
    return pseudoFactory(PSEUDO_PARAGRAPH, PseudoParagraph::new);
  }

  final PseudoSection pseudoSection() {
    return pseudoFactory(PSEUDO_SECTION, PseudoSection::new);
  }

  final PseudoText pseudoText() {
    return pseudoFactory(PSEUDO_TEXT, PseudoText::new);
  }

  final PseudoTitle pseudoTitle() {
    return pseudoFactory(PSEUDO_TITLE, PseudoTitle::new);
  }

  final PseudoUnorderedList pseudoUnorderedList() {
    return pseudoFactory(PSEUDO_ULIST, PseudoUnorderedList::new);
  }

  final boolean sectionHasNext() {
    switch (stackPeek()) {
      case PseudoTitle.EXHAUSTED,
           PseudoParagraph.EXHAUSTED,
           PseudoSection.EXHAUSTED,
           PseudoUnorderedList.EXHAUSTED -> sectionParse(Parse.BODY);

      case PseudoSection.ITERATOR -> {
        stackPop();

        var section = pseudoSection();

        // stores this section level
        stackPush(section.level);

        var heading = pseudoTitle();

        heading.level = section.level;

        nextNode = heading;

        stackPush(PseudoSection.TITLE);
      }

      case PseudoSection.PARAGRAPH,
           PseudoSection.SECTION,
           PseudoSection.TITLE,
           PseudoSection.ULIST -> {}

      default -> stackStub();
    }

    return nextNode != null;
  }

  final void sectionIterator() {
    stackAssert(PseudoSection.NODES);

    stackReplace(PseudoSection.ITERATOR);
  }

  final void sectionNodes() {
    switch (stackPeek()) {
      case PseudoDocument.SECTION_CONSUMED,
           PseudoSection.SECTION_CONSUMED -> {
        stackReplace(PseudoSection.NODES);

        pseudoAttributes().clear();
      }

      default -> stackStub();
    }
  }

  final void sourceAdvance() {
    sourceIndex++;
  }

  final char sourceAt(int index) {
    return source.charAt(index);
  }

  final String sourceGet(int start, int end) {
    return source.substring(start, end);
  }

  final boolean sourceInc() {
    sourceAdvance();

    return sourceMore();
  }

  final int sourceIndex() {
    return sourceIndex;
  }

  final void sourceIndex(int value) {
    sourceIndex = value;
  }

  final boolean sourceMatches(int sourceOffset, String other) {
    return source.regionMatches(sourceOffset, other, 0, other.length());
  }

  final boolean sourceMore() {
    return sourceIndex < source.length();
  }

  final char sourceNext() {
    return source.charAt(sourceIndex++);
  }

  final char sourcePeek() {
    return source.charAt(sourceIndex);
  }

  final char sourcePeek(int offset) {
    return source.charAt(sourceIndex + offset);
  }

  final int sourceStub() {
    var c = sourcePeek();

    throw new UnsupportedOperationException(
      "Implement me :: char=" + c
    );
  }

  final void stackDec() {
    stackArray[stackIndex]--;
  }

  final void stackInc() {
    stackArray[stackIndex]++;
  }

  final int stackPeek() {
    return stackArray[stackIndex];
  }

  final int stackPeek(int offset) {
    return stackArray[stackIndex - offset];
  }

  final int stackPop() {
    return stackArray[stackIndex--];
  }

  final void stackPop(int count) {
    stackIndex -= count;
  }

  final void stackPush(int v0) {
    stackArray = IntArrays.growIfNecessary(stackArray, stackIndex + 1);
    stackArray[++stackIndex] = v0;
  }

  final void stackPush(int v0, int v1) {
    stackArray = IntArrays.growIfNecessary(stackArray, stackIndex + 2);
    stackArray[++stackIndex] = v0;
    stackArray[++stackIndex] = v1;
  }

  final void stackReplace(int value) {
    stackArray[stackIndex] = value;
  }

  final void stackStub() {
    int ctx = stackPeek();

    throw new UnsupportedOperationException(
      "Implement me :: ctx=" + ctx
    );
  }

  final boolean titleHasNext() {
    switch (stackPeek()) {
      case PseudoTitle.ITERATOR,
           PseudoTitle.NODE_CONSUMED -> {
        stackReplace(PseudoTitle.PARSE);

        phrasing(PhraseElement.TITLE);

        if (nextNode != null) {
          stackReplace(PseudoTitle.NODE);
        } else {
          stackReplace(PseudoTitle.EXHAUSTED);
        }
      }

      case PseudoTitle.NODE -> {}

      default -> stackStub();
    }

    return nextNode != null;
  }

  final void titleIterator() {
    stackAssert(PseudoTitle.NODES);

    stackReplace(PseudoTitle.ITERATOR);
  }

  final void titleNodes() {
    switch (stackPeek()) {
      case PseudoHeader.TITLE_CONSUMED,
           PseudoSection.TITLE_CONSUMED -> stackReplace(PseudoTitle.NODES);

      default -> stackStub();
    }
  }

  private <E extends Enum<E>> E advance(E state) {
    sourceAdvance();

    return state;
  }

  private void documentParse(Parse initialState) {
    stackReplace(PseudoDocument.PARSE);

    var state = initialState;

    while (state != Parse.STOP) {
      state = switch (state) {
        case DOCUMENT_START -> documentParseDocumentStart();

        case EXHAUSTED -> documentParseExhausted();

        case HEADER -> documentParseHeader();

        case MAYBE_HEADER -> documentParseMaybeHeader();

        case MAYBE_HEADER_TRIM -> documentParseMaybeHeaderTrim();

        case NOT_HEADER -> documentParseNotHeader();

        case PARAGRAPH -> documentParseParagraph();

        case SECTION -> documentParseSection();

        case ULIST -> documentParseUlist();

        default -> parse(state);
      };
    }
  }

  private Parse documentParseDocumentStart() {
    if (!sourceMore()) {
      return Parse.STOP;
    }

    stackPush(sourceIndex());

    return switch (sourcePeek()) {
      case '=' -> advance(Parse.MAYBE_HEADER);

      default -> Parse.NOT_HEADER;
    };
  }

  private Parse documentParseExhausted() {
    stackAssert(PseudoDocument.PARSE);

    stackReplace(PseudoDocument.EXHAUSTED);

    return Parse.STOP;
  }

  private Parse documentParseHeader() {
    // pops source index
    stackPop();

    stackReplace(PseudoDocument.HEADER);

    nextNode = pseudoHeader();

    return Parse.STOP;
  }

  private Parse documentParseMaybeHeader() {
    if (!sourceMore()) {
      return Parse.NOT_HEADER;
    }

    return switch (sourcePeek()) {
      case '\t', '\f', ' ' -> advance(Parse.MAYBE_HEADER_TRIM);

      default -> Parse.NOT_HEADER;
    };
  }

  private Parse documentParseMaybeHeaderTrim() {
    if (!sourceMore()) {
      return Parse.NOT_HEADER;
    }

    return switch (sourcePeek()) {
      case '\t', '\f', ' ' -> advance(Parse.MAYBE_HEADER_TRIM);

      case '\n', '\r' -> throw new UnsupportedOperationException(
        "Implement me :: preamble paragraph"
      );

      default -> Parse.HEADER;
    };
  }

  private Parse documentParseNotHeader() {
    sourceIndex(stackPop());

    return Parse.BODY;
  }

  private Parse documentParseParagraph() {
    stackPush(PseudoDocument.PARAGRAPH);

    nextNode = pseudoParagraph();

    return Parse.STOP;
  }

  private Parse documentParseSection() {
    int level = stackPop();

    // pops source index
    stackPop();

    stackAssert(PseudoDocument.PARSE);

    stackReplace(PseudoDocument.SECTION);

    var section = pseudoSection();

    section.level = level;

    nextNode = section;

    return Parse.STOP;
  }

  private Parse documentParseUlist() {
    stackPush(PseudoDocument.ULIST);

    nextNode = pseudoUnorderedList();

    return Parse.STOP;
  }

  private boolean finalState() {
    return stackIndex == -1;
  }

  private void headerParse(HeaderParse initialState) {
    stackReplace(PseudoHeader.PARSE);

    var state = initialState;

    while (state != HeaderParse.STOP) {
      state = switch (state) {
        case AFTER_TITLE -> headerParseAfterTitle();

        case EXHAUSTED -> headerParseExhausted();

        case HEADER_END -> headerParseHeaderEnd();

        default -> throw new UnsupportedOperationException(
          "Implement me :: state=" + state
        );
      };
    }
  }

  private HeaderParse headerParseAfterTitle() {
    if (!sourceMore()) {
      return HeaderParse.EXHAUSTED;
    }

    return switch (sourcePeek()) {
      case '\n' -> advance(HeaderParse.HEADER_END);

      case ':' -> throw new UnsupportedOperationException("Implement me");

      default -> HeaderParse.EXHAUSTED;
    };
  }

  private HeaderParse headerParseExhausted() {
    stackAssert(PseudoHeader.PARSE);

    stackReplace(PseudoHeader.EXHAUSTED);

    return HeaderParse.STOP;
  }

  private HeaderParse headerParseHeaderEnd() {
    if (!sourceMore()) {
      return HeaderParse.EXHAUSTED;
    }

    return switch (sourcePeek()) {
      case '\n' -> advance(HeaderParse.HEADER_END);

      default -> HeaderParse.EXHAUSTED;
    };
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

  private Parse parse(Parse state) {
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
        var attributes = pseudoAttributes();

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

    var value = sourceGet(start, end);

    var attributes = pseudoAttributes();

    attributes.add(value);
  }

  private void phrasing(PhraseElement phrase) {
    this.phrase = phrase;

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

  private Phrasing phrasingEol() {
    return switch (phrase) {
      case TITLE -> titlePhrasingEol();
    };
  }

  private Phrasing phrasingInlineMacro() {
    int phrasingStart = stackPeek();

    int colon = sourceIndex();

    int min = Math.max(phrasingStart, colon - PseudoInlineMacro.MAX_LENGTH);

    int index = colon;

    var found = false;

    loop: while (index > min) {
      int peekIndex = index - 1;

      char peek = sourceAt(peekIndex);

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

    var macro = pseudoInlineMacro();

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

    nextNode = pseudoInlineMacro();

    return Phrasing.STOP;
  }

  private Phrasing phrasingStart() {
    return switch (phrase) {
      case TITLE -> titlePhrasingStart();
    };
  }

  private Phrasing phrasingText() {
    int endIndex = stackPop();

    int startIndex = stackPop();

    if (startIndex < endIndex) {
      var text = pseudoText();

      text.end = endIndex;

      text.start = startIndex;

      nextNode = text;
    } else {
      nextNode = null;
    }

    return Phrasing.STOP;
  }

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

    var macro = pseudoInlineMacro();

    macro.targetEnd = sourceIndex;

    var found = false;

    // attrlist rollback info
    stackPush(sourceIndex, ATTRLIST_INLINE);

    var parse = Parse.MAYBE_ATTRLIST;

    loop: while (parse != Parse.STOP) {
      parse = parse(parse);

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
    var macro = pseudoInlineMacro();

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

  private Phrasing popAndStop() {
    // pops start index
    stackPop();

    return Phrasing.STOP;
  }

  private PseudoDocument pseudoDocument() {
    return pseudoFactory(PSEUDO_DOCUMENT, PseudoDocument::new);
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

  @SuppressWarnings("unchecked")
  private <T> T pseudoFactory(int index, Function<InternalSink, T> factory) {
    var result = pseudoArray[index];

    if (result == null) {
      result = pseudoArray[index] = factory.apply(this);
    }

    return (T) result;
  }

  private PseudoPhrase pseudoPhrase() {
    return pseudoFactory(PSEUDO_PHRASE, PseudoPhrase::new);
  }

  private void sectionParse(Parse initialState) {
    stackPop(); // previous state

    @SuppressWarnings("unused")
    int level = stackPeek();

    stackPush(PseudoSection.PARSE);

    var state = initialState;

    while (state != Parse.STOP) {
      state = switch (state) {
        case BODY_TRIM -> sectionParseBodyTrim();

        case EXHAUSTED -> sectionParseExhausted();

        case PARAGRAPH -> sectionParseParagraph();

        case SECTION -> sectionParseSection();

        case ULIST -> sectionParseUlist();

        default -> parse(state);
      };
    }
  }

  private Parse sectionParseBodyTrim() {
    if (!sourceMore()) {
      return Parse.EXHAUSTED;
    }

    return switch (sourcePeek()) {
      case '\n' -> advance(Parse.BODY_TRIM);

      default -> Parse.BODY;
    };
  }

  private Parse sectionParseExhausted() {
    stackAssert(PseudoSection.PARSE);

    // pops ASSERT
    stackPop();

    // replaces section level
    stackReplace(PseudoSection.EXHAUSTED);

    return Parse.STOP;
  }

  private Parse sectionParseParagraph() {
    stackReplace(PseudoSection.PARAGRAPH);

    nextNode = pseudoParagraph();

    return Parse.STOP;
  }

  private Parse sectionParseSection() {
    int nextLevel = stackPop();

    int sourceIndex = stackPop();

    stackAssert(PseudoSection.PARSE);

    stackPop();

    int thisLevel = stackPeek();

    if (nextLevel > thisLevel) {
      stackPush(PseudoSection.SECTION);

      var section = pseudoSection();

      section.level = nextLevel;

      nextNode = section;
    } else if (nextLevel == thisLevel) {
      sourceIndex(sourceIndex);

      // replaces section level
      stackReplace(PseudoSection.EXHAUSTED);
    } else {
      sourceIndex(sourceIndex);

      // replaces section level
      stackReplace(PseudoSection.EXHAUSTED);
    }

    return Parse.STOP;
  }

  private Parse sectionParseUlist() {
    int markerEnd = stackPop();

    int markerStart = stackPop();

    int ulistTop = stackPop();

    int parse = stackPop();

    assert parse == PseudoSection.PARSE : "expected PARSE but found " + parse;

    stackPush(ulistTop);

    stackPush(markerStart, markerEnd);

    stackPush(PseudoSection.ULIST);

    nextNode = pseudoUnorderedList();

    return Parse.STOP;
  }

  private void stackAssert(int expected) {
    int actual = stackPeek();

    assert actual == expected : "actual=" + actual + ";expected=" + expected;
  }

  private void start(String source) {
    this.source = source;

    sourceIndex = 0;

    stackIndex = -1;
  }

  private Phrasing titlePhrasingEol() {
    return toPhrasingEnd(sourceIndex());
  }

  private Phrasing titlePhrasingStart() {
    if (!sourceMore()) {
      return popAndStop();
    }

    return switch (sourcePeek()) {
      case '\n' -> advance(popAndStop());

      default -> Phrasing.BLOB;
    };
  }

  private Parse toMaybeUlist() {
    int markerStart = stackPop();

    stackPush(_ULIST_TOP, markerStart);

    // marker end
    stackPush(sourceIndex());

    return advance(Parse.MAYBE_ULIST);
  }

  private Phrasing toPhrasingEnd(int last) {
    stackPush(last);

    return Phrasing.TEXT;
  }

}