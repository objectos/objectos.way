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
import objectos.asciidoc.pseudom.Node.Symbol;
import objectos.lang.Check;
import objectos.util.IntArrays;

public class InternalSink {

  private enum HeaderParse {
    STOP,
    AFTER_TITLE,
    HEADER_END,
    EXHAUSTED;
  }

  private enum ListItemText {
    STOP,

    START,
    LOOP,
    EOL,
    EOL_TRIM,

    TEXT_EOF,

    CONTINUATION,

    INDENTATION,

    MARKER,
    MARKER_LOOP,
    MARKER_LOOP_TRIM,
    MARKER_STOP;
  }

  private enum Parse {
    STOP,

    /* document only */
    DOCUMENT_START,
    MAYBE_HEADER,
    MAYBE_HEADER_TRIM,
    HEADER,
    NOT_HEADER,

    /* document, section or phrase */

    ATTRIBUTE,
    ATTRIBUTE_BOOLEAN,
    ATTRIBUTE_NAME,
    ATTRIBUTE_NAME_FOUND,
    ATTRIBUTE_ROLLBACK,
    ATTRIBUTE_STRING,
    ATTRIBUTE_TRIM,
    ATTRIBUTE_VALUE,
    ATTRIBUTE_VALUE_LOOP,

    BODY,
    BODY_TRIM,

    ATTRLIST,
    ATTRLIST_END_MARKER,
    ATTRLIST_FOUND,
    ATTRLIST_NAME,
    ATTRLIST_NAME_LOOP,
    ATTRLIST_ROLLBACK,
    ATTRLIST_START_MARKER,

    BOLD_OR_ULIST,
    NOT_BOLD_OR_ULIST,

    LISTING_OR_ULIST,
    LISTING_OR_ULIST_ROLLBACK,

    LISTING_BLOCK,
    LISTING_BLOCK_COUNT,
    LISTING_BLOCK_LOOP,
    LISTING_BLOCK_ROLLBACK,
    LISTING_BLOCK_STOP,
    LISTING_BLOCK_TRIM,

    ULIST,
    ULIST_STOP,
    NOT_ULIST,

    MAYBE_SECTION,
    MAYBE_SECTION_TRIM,
    SECTION,
    NOT_SECTION,

    PARAGRAPH,

    /* section */

    EXHAUSTED;
  }

  private enum PhraseElement {
    FRAGMENT,

    PARAGRAPH,

    TITLE;
  }

  private enum Phrasing {
    START,

    STOP,

    BLOB,

    TEXT,

    CONSTRAINED_BOLD,
    CONSTRAINED_BOLD_EOL,
    CONSTRAINED_BOLD_FOUND,
    CONSTRAINED_BOLD_LOOP,
    CONSTRAINED_BOLD_ROLLBACK,

    CONSTRAINED_ITALIC,
    CONSTRAINED_ITALIC_EOL,
    CONSTRAINED_ITALIC_FOUND,
    CONSTRAINED_ITALIC_LOOP,
    CONSTRAINED_ITALIC_ROLLBACK,

    CONSTRAINED_MONOSPACE,
    CONSTRAINED_MONOSPACE_FOUND,
    CONSTRAINED_MONOSPACE_LOOP,
    CONSTRAINED_MONOSPACE_ROLLBACK,

    EOL,

    INLINE_MACRO,
    INLINE_MACRO_END,

    CUSTOM_INLINE,
    CUSTOM_INLINE_ROLLBACK,
    CUSTOM_INLINE_TARGET,
    CUSTOM_INLINE_TARGET_ROLLBACK,

    URI_MACRO,
    URI_MACRO_ATTRLIST,
    URI_MACRO_QUOTED,
    URI_MACRO_QUOTED_LOOP,
    URI_MACRO_QUOTED_TEXT,
    URI_MACRO_ROLLBACK,
    URI_MACRO_TARGET,
    URI_MACRO_TARGET_LOOP,
    URI_MACRO_TEXT,
    URI_MACRO_TEXT_END,
    URI_MACRO_TEXT_LOOP,

    AUTOLINK,

    ATTRIBUTE,
    ATTRIBUTE_FOUND,
    ATTRIBUTE_NAME,
    ATTRIBUTE_ROLLBACK,

    APOSTROPHE;
  }

  private enum Pre {
    STOP,

    START,
    LOOP,

    EOL,
    MAKE_TEXT,
    MARKER,
    MARKER_COUNT,
    MARKER_FOUND,
    MARKER_LOOP,
    MARKER_ROLLBACK,
    MARKER_TRIM,

    TRIM;
  }

  private static final int HINT_CONTINUE_OR_NEST = -1;
  private static final int HINT_LIST_END = -2;
  private static final int HINT_NEXT_ITEM = -3;
  private static final int HINT_NESTED_END = -4;
  private static final int MARKER_ULIST = -5;

  private static final int PSEUDO_DOCUMENT = 0;
  private static final int PSEUDO_HEADER = 1;
  private static final int PSEUDO_TITLE = 2;
  private static final int PSEUDO_PARAGRAPH = 3;
  private static final int PSEUDO_SECTION = 4;
  private static final int PSEUDO_TEXT = 5;
  private static final int PSEUDO_ULIST = 6;
  private static final int PSEUDO_LIST_ITEM = 7;
  private static final int PSEUDO_ATTRIBUTES = 8;
  private static final int PSEUDO_INLINE_MACRO = 9;
  private static final int PSEUDO_MONOSPACED = 10;
  private static final int PSEUDO_LISTING_BLOCK = 11;
  private static final int PSEUDO_EMPHASIS = 12;
  private static final int PSEUDO_STRONG = 13;
  private static final int PSEUDO_LENGTH = 14;

  private final Object[] pseudoArray = new Object[PSEUDO_LENGTH];

  private Node nextNode;

  private PhraseElement phrase;

  private String source;

  private int sourceIndex;

  private int sourceMax;

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

    return pseudoDocument().clear();
  }

  final void appendTo(Appendable out, int start, int end) throws IOException {
    out.append(source, start, end);
  }

  final PseudoAttributes attributes() {
    return pseudoAttributes();
  }

  final void close() {
    assert stackIndex == 0 : stackToString();

    assert stackPop() == PseudoDocument.EXHAUSTED;

    nextNode = null;

    phrase = null;

    source = null;

    sourceIndex = sourceMax = 0;

    stackIndex = -1;
  }

  private String stackToString() {
    var sb = new StringBuilder();

    while (stackIndex >= 0) {
      sb.append("\n");
      sb.append(stackPop());
    }

    return sb.toString();
  }

  final boolean documentHasNext() {
    switch (stackPeek()) {
      case PseudoHeader.EXHAUSTED,
           PseudoListingBlock.EXHAUSTED,
           PseudoParagraph.EXHAUSTED,
           PseudoSection.EXHAUSTED,
           PseudoUnorderedList.EXHAUSTED -> {
        stackPop();

        documentParse(Parse.BODY);
      }

      case PseudoDocument.ITERATOR -> {
        stackPop();

        documentParse(Parse.DOCUMENT_START);
      }

      case PseudoDocument.BLOCK,
           PseudoDocument.HEADER,
           PseudoDocument.PARAGRAPH,
           PseudoDocument.SECTION -> {}

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

  final boolean emphasisHasNext() {
    switch (stackPeek()) {
      case PseudoEmphasis.ITERATOR -> {
        // pops ITERATOR
        stackPop();

        var em = pseudoEmphasis();

        // pushes return to indexes
        stackPush(sourceMax, sourceIndex);

        sourceIndex = em.textStart;

        sourceMax = em.textEnd;

        phrasing(PhraseElement.FRAGMENT);

        if (nextNode != null) {
          stackPush(PseudoEmphasis.NODE);
        } else {
          stackPush(PseudoEmphasis.EXHAUSTED);
        }
      }

      case PseudoEmphasis.NODE -> {}

      case PseudoEmphasis.NODE_CONSUMED -> {
        // pops NODE_CONSUMED
        stackPop();

        phrasing(PhraseElement.FRAGMENT);

        if (nextNode != null) {
          stackPush(PseudoEmphasis.NODE);
        } else {
          // restore source state
          sourceIndex = stackPop();
          sourceMax = stackPop();

          // we're done
          stackPush(PseudoEmphasis.EXHAUSTED);
        }
      }

      default -> stackStub();
    }

    return nextNode != null;
  }

  final void emphasisIterator() {
    stackAssert(PseudoEmphasis.NODES);

    stackReplace(PseudoEmphasis.ITERATOR);
  }

  final void emphasisNodes() {
    switch (stackPeek()) {
      case PseudoInlineMacro.NODE_CONSUMED,
           PseudoListItem.TEXT_CONSUMED,
           PseudoParagraph.NODE_CONSUMED,
           PseudoStrong.NODE_CONSUMED,
           PseudoTitle.NODE_CONSUMED -> stackReplace(PseudoEmphasis.NODES);

      default -> stackStub();
    }
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

  final boolean inlineMacroHasNext() {
    switch (stackPeek()) {
      case PseudoInlineMacro.ITERATOR -> {
        // pops ITERATOR
        stackPop();

        var macro = pseudoInlineMacro();

        if (macro.hasText()) {
          // pushes return to indexes
          stackPush(sourceMax, sourceIndex);

          sourceIndex = macro.textStart;

          sourceMax = macro.textEnd;

          phrasing(PhraseElement.FRAGMENT);

          if (nextNode != null) {
            stackPush(PseudoInlineMacro.NODE);
          } else {
            stackPush(PseudoInlineMacro.EXHAUSTED);
          }
        } else {
          throw new UnsupportedOperationException("Implement me");
        }
      }

      case PseudoInlineMacro.NODE -> {}

      case PseudoInlineMacro.NODE_CONSUMED,
           PseudoMonospaced.EXHAUSTED -> {
        // pops NODE_CONSUMED
        stackPop();

        phrasing(PhraseElement.FRAGMENT);

        if (nextNode != null) {
          stackPush(PseudoInlineMacro.NODE);
        } else {
          // restore source state
          sourceIndex = stackPop();
          sourceMax = stackPop();

          // we're done
          stackPush(PseudoInlineMacro.EXHAUSTED);
        }
      }

      default -> stackStub();
    }

    return nextNode != null;
  }

  final void inlineMacroIterator() {
    stackAssert(PseudoInlineMacro.NODES);

    stackReplace(PseudoInlineMacro.ITERATOR);
  }

  final void inlineMacroNodes() {
    switch (stackPeek()) {
      case PseudoListItem.TEXT_CONSUMED,
           PseudoParagraph.NODE_CONSUMED -> stackReplace(PseudoInlineMacro.NODES);

      default -> stackStub();
    }
  }

  /*
   * things to consider:
   *
   * - include directive is supported
   * - special characters are enabled by default
   * - callouts are enabled by default
   */
  final boolean listingBlockHasNext() {
    switch (stackPeek()) {
      case PseudoListingBlock.ITERATOR -> {
        stackPop();

        pre();

        stackPush(PseudoListingBlock.NODE);
      }

      case PseudoListingBlock.NODE -> {}

      case PseudoListingBlock.NODE_CONSUMED -> {
        stackPop();

        var block = pseudoListingBlock();

        if (block.last) {
          stackPush(PseudoListingBlock.EXHAUSTED);
        } else {
          throw new UnsupportedOperationException("Implement me");
        }
      }

      default -> stackStub();
    }

    return nextNode != null;
  }

  final void listingBlockIterator() {
    stackAssert(PseudoListingBlock.NODES);

    stackReplace(PseudoListingBlock.ITERATOR);
  }

  final void listingBlockNodes() {
    switch (stackPeek()) {
      case PseudoDocument.BLOCK_CONSUMED,
           PseudoSection.BLOCK_CONSUMED -> stackReplace(PseudoListingBlock.NODES);

      default -> stackStub();
    }
  }

  final boolean listItemHasNext() {
    switch (stackPeek()) {
      case PseudoListItem.BLOCK -> {}

      case PseudoListItem.ITERATOR -> {
        stackPop();

        var item = listItemText();

        // pushes return to indexes
        stackPush(sourceMax, sourceIndex);

        sourceIndex = item.textStart;

        sourceMax = item.textEnd;

        phrasing(PhraseElement.FRAGMENT);

        if (nextNode != null) {
          stackPush(PseudoListItem.TEXT);

          break;
        }

        throw new UnsupportedOperationException(
          "Implement me :: no text, maybe block?"
        );
      }

      case PseudoListItem.TEXT -> {}

      case PseudoInlineMacro.EXHAUSTED,
           PseudoListItem.TEXT_CONSUMED,
           PseudoMonospaced.EXHAUSTED -> {
        // pops TEXT_CONSUMED
        stackPop();

        phrasing(PhraseElement.FRAGMENT);

        if (nextNode != null) {
          stackPush(PseudoListItem.TEXT);

          break;
        }

        // restore source state
        sourceIndex = stackPop();

        sourceMax = stackPop();

        listItemHint();
      }

      case PseudoUnorderedList.EXHAUSTED -> {
        // TODO there might be more blocks below the nested unordered list...

        stackReplace(PseudoListItem.EXHAUSTED);
      }

      default -> stackStub();
    }

    return nextNode != null;
  }

  final void listItemIterator() {
    stackAssert(PseudoListItem.NODES);

    stackReplace(PseudoListItem.ITERATOR);
  }

  final void listItemNodes() {
    switch (stackPeek()) {
      case PseudoUnorderedList.ITEM_CONSUMED -> stackReplace(PseudoListItem.NODES);

      default -> stackStub();
    }
  }

  final boolean monospacedHasNext() {
    switch (stackPeek()) {
      case PseudoMonospaced.ITERATOR -> {
        // pops ITERATOR
        stackPop();

        var mono = pseudoMonospaced();

        // pushes return to indexes
        stackPush(sourceMax, sourceIndex);

        sourceIndex = mono.textStart;

        sourceMax = mono.textEnd;

        phrasing(PhraseElement.FRAGMENT);

        if (nextNode != null) {
          stackPush(PseudoMonospaced.NODE);
        } else {
          stackPush(PseudoMonospaced.EXHAUSTED);
        }
      }

      case PseudoMonospaced.NODE -> {}

      case PseudoMonospaced.NODE_CONSUMED -> {
        // pops NODE_CONSUMED
        stackPop();

        phrasing(PhraseElement.FRAGMENT);

        if (nextNode != null) {
          stackPush(PseudoMonospaced.NODE);
        } else {
          // restore source state
          sourceIndex = stackPop();
          sourceMax = stackPop();

          // we're done
          stackPush(PseudoMonospaced.EXHAUSTED);
        }
      }

      default -> stackStub();
    }

    return nextNode != null;
  }

  final void monospacedIterator() {
    stackAssert(PseudoMonospaced.NODES);

    stackReplace(PseudoMonospaced.ITERATOR);
  }

  final void monospacedNodes() {
    switch (stackPeek()) {
      case PseudoInlineMacro.NODE_CONSUMED,
           PseudoListItem.TEXT_CONSUMED,
           PseudoParagraph.NODE_CONSUMED,
           PseudoTitle.NODE_CONSUMED -> stackReplace(PseudoMonospaced.NODES);

      default -> stackStub();
    }
  }

  final Node nextNode() {
    var result = nextNode;

    nextNode = null;

    // change state to next adjacent state
    // e.g. HEADING -> HEADING_CONSUMED
    stackDec();

    return result;
  }

  final boolean paragraphHasNext() {
    switch (stackPeek()) {
      case PseudoEmphasis.EXHAUSTED,
           PseudoParagraph.ITERATOR,
           PseudoParagraph.NODE_CONSUMED,
           PseudoInlineMacro.EXHAUSTED,
           PseudoMonospaced.EXHAUSTED,
           PseudoStrong.EXHAUSTED -> {
        stackPop();

        phrasing(PhraseElement.PARAGRAPH);

        if (nextNode != null) {
          stackPush(PseudoParagraph.NODE);
        } else {
          stackPush(PseudoParagraph.EXHAUSTED);
        }
      }

      case PseudoParagraph.NODE -> {}

      default -> stackStub();
    }

    return nextNode != null;
  }

  final void paragraphIterator() {
    stackAssert(PseudoParagraph.NODES);

    stackReplace(PseudoParagraph.ITERATOR);
  }

  final void paragraphNodes() {
    switch (stackPeek()) {
      case PseudoDocument.PARAGRAPH_CONSUMED,
           PseudoSection.BLOCK_CONSUMED -> stackReplace(PseudoParagraph.NODES);

      default -> stackStub();
    }
  }

  final boolean sectionHasNext() {
    switch (stackPeek()) {
      case PseudoListingBlock.EXHAUSTED,
           PseudoParagraph.EXHAUSTED,
           PseudoSection.EXHAUSTED,
           PseudoTitle.EXHAUSTED,
           PseudoUnorderedList.EXHAUSTED -> {
        stackPop();

        sectionParse(Parse.BODY);
      }

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

      case PseudoSection.BLOCK,
           PseudoSection.SECTION,
           PseudoSection.TITLE -> {}

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

  final boolean strongHasNext() {
    switch (stackPeek()) {
      case PseudoStrong.ITERATOR -> {
        // pops ITERATOR
        stackPop();

        var em = pseudoStrong();

        // pushes return to indexes
        stackPush(sourceMax, sourceIndex);

        sourceIndex = em.textStart;

        sourceMax = em.textEnd;

        phrasing(PhraseElement.FRAGMENT);

        if (nextNode != null) {
          stackPush(PseudoStrong.NODE);
        } else {
          stackPush(PseudoStrong.EXHAUSTED);
        }
      }

      case PseudoStrong.NODE -> {}

      case PseudoEmphasis.EXHAUSTED,
           PseudoStrong.NODE_CONSUMED -> {
        // pops NODE_CONSUMED
        stackPop();

        phrasing(PhraseElement.FRAGMENT);

        if (nextNode != null) {
          stackPush(PseudoStrong.NODE);
        } else {
          // restore source state
          sourceIndex = stackPop();
          sourceMax = stackPop();

          // we're done
          stackPush(PseudoStrong.EXHAUSTED);
        }
      }

      default -> stackStub();
    }

    return nextNode != null;
  }

  final void strongIterator() {
    stackAssert(PseudoStrong.NODES);

    stackReplace(PseudoStrong.ITERATOR);
  }

  final void strongNodes() {
    switch (stackPeek()) {
      case PseudoInlineMacro.NODE_CONSUMED,
           PseudoListItem.TEXT_CONSUMED,
           PseudoParagraph.NODE_CONSUMED,
           PseudoTitle.NODE_CONSUMED -> stackReplace(PseudoStrong.NODES);

      default -> stackStub();
    }
  }

  final boolean titleHasNext() {
    switch (stackPeek()) {
      case PseudoMonospaced.EXHAUSTED,
           PseudoTitle.ITERATOR,
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

  final boolean unorderedListHasNext() {
    switch (stackPeek()) {
      case PseudoUnorderedList.ITEM -> {}

      case PseudoListItem.EXHAUSTED -> {
        // pops state
        stackPop();

        int hint = stackPop();

        switch (hint) {
          case HINT_LIST_END -> stackPush(PseudoUnorderedList.EXHAUSTED);

          case HINT_NESTED_END -> stackPush(PseudoUnorderedList.EXHAUSTED);

          case HINT_NEXT_ITEM -> {
            nextNode = pseudoListItem();

            stackPush(PseudoUnorderedList.ITEM);
          }

          default -> throw new UnsupportedOperationException(
            "Implement me :: hint=" + hint
          );
        }
      }

      case PseudoUnorderedList.ITERATOR -> {
        nextNode = pseudoListItem();

        stackReplace(PseudoUnorderedList.ITEM);
      }

      default -> stackStub();
    }

    return nextNode != null;
  }

  final void unorderedListIterator() {
    stackAssert(PseudoUnorderedList.NODES);

    stackReplace(PseudoUnorderedList.ITERATOR);
  }

  final void unorderedListNodes() {
    switch (stackPeek()) {
      case PseudoDocument.BLOCK_CONSUMED,
           PseudoListItem.BLOCK_CONSUMED,
           PseudoSection.BLOCK_CONSUMED -> stackReplace(PseudoUnorderedList.NODES);

      default -> stackStub();
    }
  }

  private <E extends Enum<E>> E advance(E state) {
    sourceAdvance();

    return state;
  }

  private Parse documentListingBlockStop() {
    stackPush(PseudoDocument.BLOCK);

    return Parse.STOP;
  }

  private void documentParse(Parse initialState) {
    pseudoAttributes().clear();

    var state = initialState;

    while (state != Parse.STOP) {
      state = switch (state) {
        case ATTRIBUTE_STRING -> documentParseAttributeString();

        case DOCUMENT_START -> documentParseDocumentStart();

        case EXHAUSTED -> documentParseExhausted();

        case HEADER -> documentParseHeader();

        case LISTING_BLOCK_STOP -> documentListingBlockStop();

        case MAYBE_HEADER -> documentParseMaybeHeader();

        case MAYBE_HEADER_TRIM -> documentParseMaybeHeaderTrim();

        case NOT_HEADER -> documentParseNotHeader();

        case PARAGRAPH -> documentParseParagraph();

        case SECTION -> documentParseSection();

        case ULIST_STOP -> documentParseUlistStop();

        default -> parse(state);
      };
    }
  }

  private Parse documentParseAttributeString() {
    int valueEnd = sourceIndex;
    int valueStart = stackPop();
    var value = sourceGet(valueStart, valueEnd);

    int nameEnd = stackPop();
    int nameStart = stackPop();
    var name = sourceGet(nameStart, nameEnd);

    var document = pseudoDocument();

    document.putAttribute(name, value);

    return Parse.BODY;
  }

  private Parse documentParseDocumentStart() {
    if (!sourceMore()) {
      return Parse.STOP;
    }

    stackPush(sourceIndex);

    return switch (sourcePeek()) {
      case '=' -> advance(Parse.MAYBE_HEADER);

      default -> Parse.NOT_HEADER;
    };
  }

  private Parse documentParseExhausted() {
    stackPush(PseudoDocument.EXHAUSTED);

    return Parse.STOP;
  }

  private Parse documentParseHeader() {
    // pops source index
    stackPop();

    stackPush(PseudoDocument.HEADER);

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

    stackPush(PseudoDocument.SECTION);

    var section = pseudoSection();

    section.level = level;

    nextNode = section;

    return Parse.STOP;
  }

  private Parse documentParseUlistStop() {
    stackPush(PseudoDocument.BLOCK);

    nextNode = pseudoUnorderedList();

    return Parse.STOP;
  }

  private boolean finalState() {
    return stackIndex == -1;
  }

  private Phrasing fragmentPhrasingEol() {
    return advance(Phrasing.BLOB);
  }

  private Phrasing fragmentPhrasingStart() {
    return Phrasing.BLOB;
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

  private void listItemHint() {
    int hint = stackPop();

    switch (hint) {
      case HINT_CONTINUE_OR_NEST -> listItemHintContinueOrNest();

      case HINT_LIST_END -> listItemHintListEnd();

      default -> throw new UnsupportedOperationException(
        "Implement me :: hint=" + hint
      );
    }
  }

  private void listItemHintContinueOrNest() {
    var thisEnd = stackPop();
    var thisStart = stackPop();
    var thisMarker = sourceGet(thisStart, thisEnd);

    int prevEnd = stackPop();
    int prevStart = stackPop();
    var prevMarker = sourceGet(prevStart, prevEnd);

    if (thisMarker.equals(prevMarker)) {
      // updates marker to current indexes
      stackPush(thisStart, thisEnd);

      // pushes HINT / next state
      stackPush(HINT_NEXT_ITEM, PseudoListItem.EXHAUSTED);

      return;
    }

    int maybeTop = stackPeek();

    if (maybeTop == MARKER_ULIST) {
      // keep first level marker around
      stackPush(prevStart, prevEnd);

      // pushes the new level marker
      stackPush(thisStart, thisEnd);

      nextNode = pseudoUnorderedList();

      stackPush(PseudoListItem.BLOCK);

      return;
    }

    var found = false;

    int offset = 0;

    while (maybeTop != MARKER_ULIST) {
      prevEnd = stackPeek(offset++);
      prevStart = stackPeek(offset++);
      prevMarker = sourceGet(prevStart, prevEnd);

      if (thisMarker.equals(prevMarker)) {
        found = true;

        break;
      }
    }

    if (found) {
      // pops finished levels
      stackPop(offset);

      // pushes the new level marker
      stackPush(thisStart, thisEnd);

      // pushes HINT
      stackPush(HINT_NEXT_ITEM, HINT_NESTED_END, PseudoListItem.EXHAUSTED);

      return;
    }

    throw new UnsupportedOperationException(
      "Implement me :: prevMarker=" + prevMarker + ";thisMarker=" + thisMarker
    );
  }

  private void listItemHintListEnd() {
    // marker end
    stackPop();

    // marker start
    stackPop();

    int top = stackPop();

    assert top == MARKER_ULIST;

    // pushes HINT / next state
    stackPush(HINT_LIST_END, PseudoListItem.EXHAUSTED);
  }

  private PseudoListItem listItemText() {
    var state = ListItemText.START;

    while (state != ListItemText.STOP) {
      state = switch (state) {
        case CONTINUATION -> listItemTextContinuation();

        case EOL -> listItemTextEol();

        case EOL_TRIM -> listItemTextEolTrim();

        case INDENTATION -> listItemTextIndentation();

        case LOOP -> listItemTextLoop();

        case MARKER -> listItemTextMarker();

        case MARKER_LOOP -> listItemTextMarkerLoop();

        case MARKER_LOOP_TRIM -> listItemTextMarkerLoopTrim();

        case MARKER_STOP -> listItemTextMarkerStop();

        case START -> listItemTextStart();

        case STOP -> state;

        case TEXT_EOF -> listItemTextTextEof();
      };
    }

    return pseudoListItem();
  }

  private ListItemText listItemTextContinuation() {
    throw new UnsupportedOperationException("Implement me");
  }

  private ListItemText listItemTextEol() {
    // maybe text end
    stackPush(sourceIndex);

    if (!sourceInc()) {
      return ListItemText.TEXT_EOF;
    }

    return switch (sourcePeek()) {
      case '\t', '\f', ' ' -> ListItemText.INDENTATION;

      case '\n' -> ListItemText.EOL_TRIM;

      case '-', '*' -> ListItemText.MARKER;

      case '+' -> ListItemText.CONTINUATION;

      default -> {
        // pops text end
        stackPop();

        yield ListItemText.LOOP;
      }
    };
  }

  private ListItemText listItemTextEolTrim() {
    if (!sourceInc()) {
      return ListItemText.TEXT_EOF;
    }

    return switch (sourcePeek()) {
      case '\n' -> ListItemText.EOL_TRIM;

      case '-', '*' -> ListItemText.MARKER;

      default -> ListItemText.TEXT_EOF;
    };
  }

  private ListItemText listItemTextIndentation() {
    if (!sourceInc()) {
      return ListItemText.TEXT_EOF;
    }

    return switch (sourcePeek()) {
      case '\t', '\f', ' ' -> ListItemText.INDENTATION;

      case '-', '*' -> ListItemText.MARKER;

      default -> throw new UnsupportedOperationException("Implement me");
    };
  }

  private ListItemText listItemTextLoop() {
    if (!sourceInc()) {
      // pushes text end
      stackPush(sourceIndex);

      return ListItemText.TEXT_EOF;
    }

    return switch (sourcePeek()) {
      case '\n' -> ListItemText.EOL;

      default -> ListItemText.LOOP;
    };
  }

  private ListItemText listItemTextMarker() {
    int markerStart = sourceIndex;

    char marker = sourceAt(markerStart);

    stackPush(markerStart, marker);

    return ListItemText.MARKER_LOOP;
  }

  private ListItemText listItemTextMarkerLoop() {
    if (!sourceInc()) {
      throw new UnsupportedOperationException("Implement me");
    }

    char peek = sourcePeek();

    char marker = (char) stackPeek();

    if (peek == marker) {
      return ListItemText.MARKER_LOOP;
    }

    return switch (peek) {
      case '\t', '\f', ' ' -> {
        // pops marker
        stackPop();

        // pushes marker end
        stackPush(sourceIndex);

        yield ListItemText.MARKER_LOOP_TRIM;
      }

      case '\n' -> {
        // pops
        // - marker
        // - marker start
        // - textEnd
        stackPop(3);

        // resumes before NL
        sourceIndex--;

        yield ListItemText.LOOP;
      }

      default -> throw new UnsupportedOperationException("Implement me");
    };
  }

  private ListItemText listItemTextMarkerLoopTrim() {
    if (!sourceInc()) {
      throw new UnsupportedOperationException("Implement me");
    }

    return switch (sourcePeek()) {
      case '\t', '\f', ' ' -> ListItemText.MARKER_LOOP_TRIM;

      default -> ListItemText.MARKER_STOP;
    };
  }

  private ListItemText listItemTextMarkerStop() {
    int markerEnd = stackPop();

    int markerStart = stackPop();

    var item = pseudoListItem();

    item.textEnd = stackPop();

    item.textStart = stackPop();

    stackPush(markerStart, markerEnd, HINT_CONTINUE_OR_NEST);

    return ListItemText.STOP;
  }

  private ListItemText listItemTextStart() {
    // text start
    stackPush(sourceIndex);

    return ListItemText.LOOP;
  }

  private ListItemText listItemTextTextEof() {
    var item = pseudoListItem();

    item.textEnd = stackPop();

    item.textStart = stackPop();

    stackPush(HINT_LIST_END);

    return ListItemText.STOP;
  }

  private Phrasing paragraphPhrasingEol() {
    int atEol = sourceIndex;

    int trimIndex = trimIndex();

    if (!sourceInc()) {
      return toPhrasingEnd(trimIndex);
    }

    return switch (sourcePeek()) {
      case '\n' -> {
        var next = toPhrasingEnd(trimIndex);

        sourceIndex(atEol);

        yield next;
      }

      default -> advance(Phrasing.BLOB);
    };
  }

  private int trimIndex() {
    int index = sourceIndex;

    loop: while (index > 0) {
      int maybe = index - 1;

      switch (sourceAt(maybe)) {
        case '\t', '\f', ' ' -> {
          index = maybe;
        }

        default -> {
          break loop;
        }
      }
    }

    return index;
  }

  private Phrasing paragraphPhrasingStart() {
    if (!sourceMore()) {
      return popAndStop();
    }

    return switch (sourcePeek()) {
      case '\n' -> {
        if (!sourceInc()) {
          yield popAndStop();
        }

        char next = sourcePeek();

        if (next == '\n') {
          yield popAndStop();
        }

        sourceIndex(stackPeek());

        yield Phrasing.BLOB;
      }

      default -> Phrasing.BLOB;
    };
  }

  private Parse parse(Parse state) {
    return switch (state) {
      case ATTRIBUTE -> parseAttribute();

      case ATTRIBUTE_NAME -> parseAttributeName();

      case ATTRIBUTE_NAME_FOUND -> parseAttributeNameFound();

      case ATTRIBUTE_TRIM -> parseAttributeTrim();

      case ATTRIBUTE_VALUE -> parseAttributeValue();

      case ATTRIBUTE_VALUE_LOOP -> parseAttributeValueLoop();

      case ATTRLIST -> parseAttrlist();

      case ATTRLIST_END_MARKER -> parseAttrlistEndMarker();

      case ATTRLIST_FOUND -> parseAttrlistFound();

      case ATTRLIST_NAME -> parseAttrlistName();

      case ATTRLIST_NAME_LOOP -> parseAttrlistNameLoop();

      case ATTRLIST_START_MARKER -> parseAttrlistStartMarker();

      case BODY -> parseBody();

      case BODY_TRIM -> parseBodyTrim();

      case BOLD_OR_ULIST -> parseBoldOrUlist();

      case LISTING_BLOCK -> parseListingBlock();

      case LISTING_BLOCK_COUNT -> parseListingBlockCount();

      case LISTING_BLOCK_LOOP -> parseListingBlockLoop();

      case LISTING_OR_ULIST -> parseListingOrUlist();

      case LISTING_OR_ULIST_ROLLBACK -> parseListingOrUlistRollback();

      case MAYBE_SECTION -> parseMaybeSection();

      case MAYBE_SECTION_TRIM -> parseMaybeSectionTrim();

      case NOT_SECTION -> parseNotSection();

      case ULIST -> parseUlist();

      default -> throw new UnsupportedOperationException(
        "Implement me :: state=" + state
      );
    };
  }

  private Parse parseAttribute() {
    if (!sourceInc()) {
      throw new UnsupportedOperationException("Implement me");
    }

    // name start
    stackPush(sourceIndex);

    char peek = sourcePeek();

    if (isWord(peek)) {
      return Parse.ATTRIBUTE_NAME;
    } else {
      return Parse.ATTRIBUTE_ROLLBACK;
    }
  }

  private Parse parseAttributeName() {
    if (!sourceInc()) {
      return Parse.ATTRIBUTE_ROLLBACK;
    }

    char peek = sourcePeek();

    if (isWord(peek)) {
      return Parse.ATTRIBUTE_NAME;
    }

    return switch (peek) {
      case ':' -> Parse.ATTRIBUTE_NAME_FOUND;

      case '-', '_' -> Parse.ATTRIBUTE_NAME;

      default -> Parse.ATTRIBUTE_ROLLBACK;
    };
  }

  private Parse parseAttributeNameFound() {
    // name end
    stackPush(sourceIndex);

    if (!sourceInc()) {
      return Parse.ATTRIBUTE_BOOLEAN;
    }

    return switch (sourcePeek()) {
      case '\t', '\f', ' ' -> Parse.ATTRIBUTE_TRIM;

      case '\n' -> Parse.ATTRIBUTE_BOOLEAN;

      default -> throw new UnsupportedOperationException(
        "Implement me :: attribute rollback? attribute ignore?"
      );
    };
  }

  private Parse parseAttributeTrim() {
    if (!sourceInc()) {
      // we'll do it for due dilligence...
      // as the doc ended so where can this be used?
      return Parse.ATTRIBUTE_BOOLEAN;
    }

    return switch (sourcePeek()) {
      case '\t', '\f', ' ' -> Parse.ATTRIBUTE_TRIM;

      case '\n' -> Parse.ATTRIBUTE_BOOLEAN;

      default -> Parse.ATTRIBUTE_VALUE;
    };
  }

  private Parse parseAttributeValue() {
    // value start
    stackPush(sourceIndex);

    return Parse.ATTRIBUTE_VALUE_LOOP;
  }

  private Parse parseAttributeValueLoop() {
    if (!sourceInc()) {
      return Parse.ATTRIBUTE_STRING;
    }

    return switch (sourcePeek()) {
      case '\n' -> Parse.ATTRIBUTE_STRING;

      default -> Parse.ATTRIBUTE_VALUE_LOOP;
    };
  }

  private Parse parseAttrlist() {
    // rollback index
    stackPush(sourceIndex);

    return Parse.ATTRLIST_START_MARKER;
  }

  private Parse parseAttrlistEndMarker() {
    if (!sourceInc()) {
      // pop rollback index
      stackPop();

      throw new UnsupportedOperationException(
        "Implement me :: doc ends w/ attrlist"
      );
    }

    return switch (sourcePeek()) {
      case '\t', '\f', ' ' -> Parse.ATTRLIST_END_MARKER;

      case '\n' -> Parse.ATTRLIST_FOUND;

      default -> throw new AssertionError(
        "Implement me :: rollback attrlist :: sourcePeek=" + sourcePeek()
      );
    };
  }

  private Parse parseAttrlistFound() {
    // pops rollback index
    stackPop();

    // skips EOL
    sourceInc();

    pseudoAttributes().active();

    return Parse.BODY;
  }

  private Parse parseAttrlistName() {
    // pushes name start
    stackPush(sourceIndex);

    // so loop analyzes current char
    sourceIndex--;

    return Parse.ATTRLIST_NAME_LOOP;
  }

  private Parse parseAttrlistNameLoop() {
    if (!sourceInc()) {
      return Parse.ATTRLIST_ROLLBACK;
    }

    return switch (sourcePeek()) {
      case ',' -> {
        parseAttrlistPositional();

        sourceInc();

        yield Parse.ATTRLIST_NAME;
      }

      case ']' -> {
        parseAttrlistPositional();

        yield Parse.ATTRLIST_END_MARKER;
      }

      default -> Parse.ATTRLIST_NAME_LOOP;
    };
  }

  private void parseAttrlistPositional() {
    int start = stackPop();

    int end = sourceIndex;

    var value = sourceGet(start, end);

    var attributes = pseudoAttributes();

    attributes.addPositional(value);
  }

  private Parse parseAttrlistStartMarker() {
    if (!sourceInc()) {
      return Parse.ATTRLIST_ROLLBACK;
    }

    return switch (sourcePeek()) {
      case '\t', '\f', ' ' -> Parse.ATTRLIST_ROLLBACK;

      case ']' -> Parse.ATTRLIST_END_MARKER;

      default -> Parse.ATTRLIST_NAME;
    };
  }

  private Parse parseBody() {
    if (!sourceMore()) {
      return Parse.EXHAUSTED;
    }

    return switch (sourcePeek()) {
      case '\n' -> Parse.BODY_TRIM;

      case ':' -> Parse.ATTRIBUTE;

      case '[' -> Parse.ATTRLIST;

      case '-' -> Parse.LISTING_OR_ULIST;

      case '*' -> Parse.BOLD_OR_ULIST;

      case '=' -> {
        stackPush(sourceIndex);

        // push title level
        stackPush(0);

        yield advance(Parse.MAYBE_SECTION);
      }

      default -> Parse.PARAGRAPH;
    };
  }

  private Parse parseBodyTrim() {
    if (!sourceInc()) {
      return Parse.STOP;
    }

    return switch (sourcePeek()) {
      case '\n' -> Parse.BODY_TRIM;

      default -> Parse.BODY;
    };
  }

  private Parse parseBoldOrUlist() {
    int markerStart = sourceIndex;

    if (!sourceInc()) {
      return Parse.NOT_BOLD_OR_ULIST;
    }

    return switch (sourcePeek()) {
      case '\t', '\f', ' ' -> {
        stackPush(markerStart);

        yield toUlist();
      }

      case '*' -> throw new UnsupportedOperationException("Implement me");

      default -> {
        sourceIndex = markerStart;

        yield Parse.PARAGRAPH;
      }
    };
  }

  private Parse parseListingBlock() {
    // pushes current dash count
    // - first on BODY
    // - second on LISTING_OR_ULIST
    stackPush(2);

    return Parse.LISTING_BLOCK_LOOP;
  }

  private Parse parseListingBlockCount() {
    int count = stackPop();

    if (count < 4) {
      return Parse.LISTING_BLOCK_ROLLBACK;
    }

    // pops rollback index
    stackPop();

    // skips new line
    sourceInc();

    var block = pseudoListingBlock();

    block.last = false;

    block.markerLength = count;

    nextNode = block;

    return Parse.LISTING_BLOCK_STOP;
  }

  private Parse parseListingBlockLoop() {
    if (!sourceInc()) {
      return Parse.LISTING_BLOCK_ROLLBACK;
    }

    return switch (sourcePeek()) {
      case '-' -> {
        stackInc();

        yield Parse.LISTING_BLOCK_LOOP;
      }

      case '\t', '\f', ' ' -> Parse.LISTING_BLOCK_TRIM;

      case '\n' -> Parse.LISTING_BLOCK_COUNT;

      default -> Parse.LISTING_BLOCK_ROLLBACK;
    };
  }

  private Parse parseListingOrUlist() {
    // rollback index or marker start
    stackPush(sourceIndex);

    if (!sourceInc()) {
      return Parse.LISTING_OR_ULIST_ROLLBACK;
    }

    return switch (sourcePeek()) {
      case '\t', '\f', ' ' -> toUlist();

      case '-' -> Parse.LISTING_BLOCK;

      default -> Parse.LISTING_OR_ULIST_ROLLBACK;
    };
  }

  private Parse parseListingOrUlistRollback() {
    // rollback
    sourceIndex = stackPop();

    return Parse.PARAGRAPH;
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

  private Parse parseNotSection() {
    // pops section level
    stackPop();

    sourceIndex(stackPop());

    return Parse.PARAGRAPH;
  }

  private Parse parseUlist() {
    if (!sourceInc()) {
      return Parse.NOT_ULIST;
    }

    return switch (sourcePeek()) {
      case '\t', '\f', ' ' -> Parse.ULIST;

      case '\n' -> Parse.NOT_ULIST;

      default -> Parse.ULIST_STOP;
    };
  }

  private void phrasing(PhraseElement phrase) {
    this.phrase = phrase;

    var state = Phrasing.START;

    stackPush(sourceIndex);

    while (state != Phrasing.STOP) {
      state = switch (state) {
        case APOSTROPHE -> phrasingApostrophe();

        case ATTRIBUTE -> phrasingAttribute();

        case ATTRIBUTE_FOUND -> phrasingAttributeFound();

        case ATTRIBUTE_NAME -> phrasingAttributeName();

        case ATTRIBUTE_ROLLBACK -> phrasingAttributeRollback();

        case AUTOLINK -> phrasingAutolink();

        case BLOB -> phrasingBlob();

        case CONSTRAINED_BOLD -> phrasingConstrainedBold();

        case CONSTRAINED_BOLD_EOL -> phrasingConstrainedBoldEol();

        case CONSTRAINED_BOLD_FOUND -> phrasingConstrainedBoldFound();

        case CONSTRAINED_BOLD_LOOP -> phrasingConstrainedBoldLoop();

        case CONSTRAINED_BOLD_ROLLBACK -> phrasingConstrainedBoldRollback();

        case CONSTRAINED_ITALIC -> phrasingConstrainedItalic();

        case CONSTRAINED_ITALIC_EOL -> phrasingConstrainedItalicEol();

        case CONSTRAINED_ITALIC_FOUND -> phrasingConstrainedItalicFound();

        case CONSTRAINED_ITALIC_LOOP -> phrasingConstrainedItalicLoop();

        case CONSTRAINED_ITALIC_ROLLBACK -> phrasingConstrainedItalicRollback();

        case CONSTRAINED_MONOSPACE -> phrasingConstrainedMonospace();

        case CONSTRAINED_MONOSPACE_FOUND -> phrasingConstrainedMonospaceFound();

        case CONSTRAINED_MONOSPACE_LOOP -> phrasingConstrainedMonospaceLoop();

        case CONSTRAINED_MONOSPACE_ROLLBACK -> phrasingConstrainedMonospaceRollback();

        case CUSTOM_INLINE -> phrasingCustomInline();

        case CUSTOM_INLINE_TARGET -> phrasingCustomInlineTarget();

        case CUSTOM_INLINE_TARGET_ROLLBACK -> phrasingCustomInlineTargetRollback();

        case CUSTOM_INLINE_ROLLBACK -> phrasingCustomInlineRollback();

        case EOL -> phrasingEol();

        case INLINE_MACRO -> phrasingInlineMacro();

        case INLINE_MACRO_END -> phrasingInlineMacroEnd();

        case START -> phrasingStart();

        case STOP -> throw new UnsupportedOperationException("Implement me");

        case TEXT -> phrasingText();

        case URI_MACRO -> phrasingUriMacro();

        case URI_MACRO_ATTRLIST -> phrasingUriMacroAttrlist();

        case URI_MACRO_QUOTED -> phrasingUriMacroQuoted();

        case URI_MACRO_QUOTED_LOOP -> phrasingUriMacroQuotedLoop();

        case URI_MACRO_QUOTED_TEXT -> phrasingUriMacroQuotedText();

        case URI_MACRO_ROLLBACK -> phrasingUriMacroRollback();

        case URI_MACRO_TARGET -> phrasingUriMacroTarget();

        case URI_MACRO_TARGET_LOOP -> phrasingUriMacroTargetLoop();

        case URI_MACRO_TEXT -> phrasingUriMacroText();

        case URI_MACRO_TEXT_END -> phrasingUriMacroTextEnd();

        case URI_MACRO_TEXT_LOOP -> phrasingUriMacroTextLoop();
      };
    }
  }

  private Phrasing phrasingApostrophe() {
    int symbol = sourceIndex;

    if (symbol == 0) {
      return Phrasing.BLOB;
    }

    int phrasingStart = stackPeek();

    int preTextLength = symbol - phrasingStart;

    if (preTextLength > 0) {
      // we'll resume at the (possible) symbol
      sourceIndex = symbol;

      return toPhrasingEnd(sourceIndex);
    }

    char before = sourceAt(sourceIndex - 1);

    if (!isWord(before)) {
      return advance(Phrasing.BLOB);
    }

    if (!sourceInc()) {
      return toPhrasingEnd(sourceIndex);
    }

    char after = sourcePeek();

    if (!isWord(after)) {
      return Phrasing.BLOB;
    }

    // pops phrasing start
    stackPop();

    nextNode = Symbol.RIGHT_SINGLE_QUOTATION_MARK;

    return Phrasing.STOP;
  }

  private Phrasing phrasingAttribute() {
    int startSymbol = sourceIndex;

    if (!sourceInc()) {
      return toPhrasingEnd(startSymbol);
    }

    int phrasingStart = stackPeek();

    int preTextLength = startSymbol - phrasingStart;

    if (preTextLength > 0) {
      // we'll resume at the (possible) attribute reference
      sourceIndex = startSymbol;

      return toPhrasingEnd(sourceIndex);
    }

    // name start
    stackPush(sourceIndex);

    // adjust for next state
    sourceIndex--;

    return Phrasing.ATTRIBUTE_NAME;
  }

  private Phrasing phrasingAttributeFound() {
    int end = sourceIndex;
    int start = stackPop();
    // pops phrasing start
    stackPop();
    var name = sourceGet(start, end);
    var value = searchAttribute(name);

    if (value != null) {
      // resume after '}'
      sourceIndex++;

      var text = pseudoText();

      text.value = value;

      nextNode = text;

      return Phrasing.STOP;
    } else {
      return Phrasing.BLOB;
    }
  }

  private Phrasing phrasingAttributeName() {
    if (!sourceInc()) {
      return toPhrasingEnd(sourceIndex);
    }

    return switch (sourcePeek()) {
      case '\t', '\f', ' ' -> Phrasing.ATTRIBUTE_ROLLBACK;

      case '\n' -> Phrasing.ATTRIBUTE_ROLLBACK;

      case '}' -> Phrasing.ATTRIBUTE_FOUND;

      default -> Phrasing.ATTRIBUTE_NAME;
    };
  }

  private Phrasing phrasingAttributeRollback() {
    throw new UnsupportedOperationException("Implement me");
  }

  private Phrasing phrasingAutolink() {
    throw new UnsupportedOperationException("Implement me");
  }

  private Phrasing phrasingBlob() {
    if (!sourceMore()) {
      return toPhrasingEnd(sourceIndex);
    }

    return switch (sourcePeek()) {
      case '\n' -> Phrasing.EOL;

      case '`' -> Phrasing.CONSTRAINED_MONOSPACE;

      case '*' -> Phrasing.CONSTRAINED_BOLD;

      case '_' -> Phrasing.CONSTRAINED_ITALIC;

      case ':' -> Phrasing.INLINE_MACRO;

      case '{' -> Phrasing.ATTRIBUTE;

      case '\'' -> Phrasing.APOSTROPHE;

      default -> advance(Phrasing.BLOB);
    };
  }

  /*
  
  CC_WORD = CG_WORD = '\p{Word}'
  CC_ALL = '.'
  QuoteAttributeListRxt = %(\\[([^\\[\\]]+)\\]) -> \[([^\[\\]]+)\]
  
  # _emphasis_
  /(^|[^\p{Word};:}])(?:#{QuoteAttributeListRxt})?\*(\S|\S.*?\S)\*(?!\p{Word})/m

   */
  private Phrasing phrasingConstrainedBold() {
    int startSymbol = sourceIndex;

    if (!sourceInc()) {
      return toPhrasingEnd(startSymbol);
    }

    if (startSymbol != 0) {
      char previous = sourceAt(startSymbol - 1);

      var rollback = switch (previous) {
        case ';', ':', '}' -> true;

        default -> isWord(previous);
      };

      if (rollback) {
        return Phrasing.CONSTRAINED_BOLD_ROLLBACK;
      }
    }

    int phrasingStart = stackPeek();

    int preTextLength = startSymbol - phrasingStart;

    if (preTextLength > 0) {
      // we'll resume at the (possible) italic
      sourceIndex = startSymbol;

      return toPhrasingEnd(sourceIndex);
    }

    return switch (sourcePeek()) {
      case '\t', '\f', ' ' -> Phrasing.CONSTRAINED_BOLD_ROLLBACK;

      default -> Phrasing.CONSTRAINED_BOLD_LOOP;
    };
  }

  private Phrasing phrasingConstrainedBoldEol() {
    throw new UnsupportedOperationException("Implement me");
  }

  private Phrasing phrasingConstrainedBoldFound() {
    int symbolEnd = sourceIndex;

    // safe
    char previous = sourceAt(symbolEnd - 1);

    return switch (previous) {
      case '\t', '\f', ' ' -> Phrasing.CONSTRAINED_BOLD_ROLLBACK;

      default -> {
        if (!sourceInc()) {
          yield toConstrainedBold();
        }

        char next = sourceAt(symbolEnd + 1);

        if (isWord(next)) {
          yield Phrasing.CONSTRAINED_BOLD_ROLLBACK;
        } else {
          yield toConstrainedBold();
        }
      }
    };
  }

  private Phrasing phrasingConstrainedBoldLoop() {
    if (!sourceInc()) {
      return toPhrasingEnd(sourceIndex);
    }

    return switch (sourcePeek()) {
      case '\n' -> Phrasing.CONSTRAINED_BOLD_EOL;

      case '*' -> Phrasing.CONSTRAINED_BOLD_FOUND;

      default -> Phrasing.CONSTRAINED_BOLD_LOOP;
    };
  }

  private Phrasing phrasingConstrainedBoldRollback() {
    throw new UnsupportedOperationException("Implement me");
  }

  /*
  
  CC_WORD = CG_WORD = '\p{Word}'
  CC_ALL = '.'
  QuoteAttributeListRxt = %(\\[([^\\[\\]]+)\\]) -> \[([^\[\\]]+)\]
  
  # _emphasis_
  /(^|[^\p{Word};:}])(?:#{QuoteAttributeListRxt})?_(\S|\S.*?\S)_(?!\p{Word})/m

   */
  private Phrasing phrasingConstrainedItalic() {
    int startSymbol = sourceIndex;

    if (!sourceInc()) {
      return toPhrasingEnd(startSymbol);
    }

    if (startSymbol != 0) {
      char previous = sourceAt(startSymbol - 1);

      var rollback = switch (previous) {
        case ';', ':', '}' -> true;

        default -> isWord(previous);
      };

      if (rollback) {
        return Phrasing.CONSTRAINED_ITALIC_ROLLBACK;
      }
    }

    int phrasingStart = stackPeek();

    int preTextLength = startSymbol - phrasingStart;

    if (preTextLength > 0) {
      // we'll resume at the (possible) italic
      sourceIndex = startSymbol;

      return toPhrasingEnd(sourceIndex);
    }

    return switch (sourcePeek()) {
      case '\t', '\f', ' ' -> Phrasing.CONSTRAINED_ITALIC_ROLLBACK;

      default -> Phrasing.CONSTRAINED_ITALIC_LOOP;
    };
  }

  private Phrasing phrasingConstrainedItalicEol() {
    if (!sourceInc()) {
      // end text before NL
      int textEnd = sourceIndex - 1;

      return toPhrasingEnd(textEnd);
    }

    return switch (sourcePeek()) {
      case '\n' -> Phrasing.CONSTRAINED_ITALIC_ROLLBACK;

      default -> Phrasing.CONSTRAINED_ITALIC_LOOP;
    };
  }

  private Phrasing phrasingConstrainedItalicFound() {
    int symbolEnd = sourceIndex;

    // safe
    char previous = sourceAt(symbolEnd - 1);

    return switch (previous) {
      case '\t', '\f', ' ' -> Phrasing.CONSTRAINED_ITALIC_ROLLBACK;

      default -> {
        if (!sourceInc()) {
          yield toConstrainedItalic();
        }

        char next = sourceAt(symbolEnd + 1);

        if (isWord(next)) {
          yield Phrasing.CONSTRAINED_ITALIC_ROLLBACK;
        } else {
          yield toConstrainedItalic();
        }
      }
    };
  }

  private Phrasing phrasingConstrainedItalicLoop() {
    if (!sourceInc()) {
      return toPhrasingEnd(sourceIndex);
    }

    return switch (sourcePeek()) {
      case '\n' -> Phrasing.CONSTRAINED_ITALIC_EOL;

      case '_' -> Phrasing.CONSTRAINED_ITALIC_FOUND;

      default -> Phrasing.CONSTRAINED_ITALIC_LOOP;
    };
  }

  private Phrasing phrasingConstrainedItalicRollback() {
    // resume after opening symbol
    sourceIndex = stackPeek() + 1;

    return Phrasing.BLOB;
  }

  /*
  
  CC_WORD = CG_WORD = '\p{Word}'
  CC_ALL = '.'
  QuoteAttributeListRxt = %(\\[([^\\[\\]]+)\\]) -> \[([^\[\\]]+)\]
  
  (^|[^\p{Xwd};:"'`}])(?:\[([^\[\\]]+)\])?`(\S|\S.*?\S)`(?![\p{Xwd}"'`])

   */
  private Phrasing phrasingConstrainedMonospace() {
    int startSymbol = sourceIndex;

    if (!sourceInc()) {
      return toPhrasingEnd(startSymbol);
    }

    if (startSymbol != 0) {
      char previous = sourceAt(startSymbol - 1);

      var rollback = switch (previous) {
        case ';', ':', '"', '\'', '`', '}' -> true;

        default -> isWord(previous);
      };

      if (rollback) {
        return Phrasing.CONSTRAINED_MONOSPACE_ROLLBACK;
      }
    }

    int phrasingStart = stackPeek();

    int preTextLength = startSymbol - phrasingStart;

    if (preTextLength > 0) {
      // we'll resume at the (possible) monospace
      sourceIndex = startSymbol;

      return toPhrasingEnd(sourceIndex);
    }

    return switch (sourcePeek()) {
      case '\t', '\f', ' ' -> Phrasing.CONSTRAINED_MONOSPACE_ROLLBACK;

      case '`' -> throw new UnsupportedOperationException(
        "Implement me :: maybe unconstrained"
      );

      default -> Phrasing.CONSTRAINED_MONOSPACE_LOOP;
    };
  }

  private Phrasing phrasingConstrainedMonospaceFound() {
    int symbolEnd = sourceIndex;

    // safe
    char previous = sourceAt(symbolEnd - 1);

    return switch (previous) {
      case '\t', '\f', ' ' -> Phrasing.CONSTRAINED_MONOSPACE_ROLLBACK;

      default -> {
        if (!sourceInc()) {
          yield toConstrainedMonospace();
        }

        char next = sourceAt(symbolEnd + 1);

        if (isWord(next)) {
          yield Phrasing.CONSTRAINED_MONOSPACE_ROLLBACK;
        }

        yield switch (next) {
          case '"', '\'', '`' -> Phrasing.CONSTRAINED_MONOSPACE_ROLLBACK;

          default -> toConstrainedMonospace();
        };
      }
    };
  }

  private Phrasing phrasingConstrainedMonospaceLoop() {
    if (!sourceInc()) {
      return toPhrasingEnd(sourceIndex);
    }

    return switch (sourcePeek()) {
      case '\n' -> throw new UnsupportedOperationException(
        "Implement me :: maybe block end"
      );

      case '`' -> Phrasing.CONSTRAINED_MONOSPACE_FOUND;

      default -> Phrasing.CONSTRAINED_MONOSPACE_LOOP;
    };
  }

  private Phrasing phrasingConstrainedMonospaceRollback() {
    // no saved state
    // just resume blob parsing

    return Phrasing.BLOB;
  }

  private Phrasing phrasingCustomInline() {
    if (!sourceInc()) {
      return Phrasing.CUSTOM_INLINE_ROLLBACK;
    }

    char peek = sourcePeek();

    if (peek == ':') {
      // resume after second semicolon
      sourceIndex++;

      return Phrasing.CUSTOM_INLINE_ROLLBACK;
    }

    // target start
    stackPush(sourceIndex);

    // adjust for CUSTOM_INLINE_TARGET
    sourceIndex--;

    return Phrasing.CUSTOM_INLINE_TARGET;
  }

  private Phrasing phrasingCustomInlineRollback() {
    // no saved state
    // just resume blob parsing

    return Phrasing.BLOB;
  }

  private Phrasing phrasingCustomInlineTarget() {
    if (!sourceInc()) {
      throw new UnsupportedOperationException("Implement me");
    }

    return switch (sourcePeek()) {
      case '\t', '\f', ' ' -> Phrasing.CUSTOM_INLINE_TARGET_ROLLBACK;

      case '\n' -> Phrasing.CUSTOM_INLINE_TARGET_ROLLBACK;

      case '[' -> Phrasing.URI_MACRO_ATTRLIST;

      default -> Phrasing.CUSTOM_INLINE_TARGET;
    };
  }

  private Phrasing phrasingCustomInlineTargetRollback() {
    // pops target start
    stackPop();

    return Phrasing.CUSTOM_INLINE_ROLLBACK;
  }

  private Phrasing phrasingEol() {
    return switch (phrase) {
      case FRAGMENT -> fragmentPhrasingEol();

      case PARAGRAPH -> paragraphPhrasingEol();

      case TITLE -> titlePhrasingEol();
    };
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
  
  as PCRE
  
  (^|link:|\h|&lt;|[>\(\)\[\];"'])(\\?(?:https?|file|ftp|irc):\/\/)(?:([^\s\[\]]+)\[(|.*?[^\\])\]|([^\s\[\]<]*([^\s,.?!\[\]<\)])))
  
  */
  private Phrasing phrasingInlineMacro() {
    int phrasingStart = stackPeek();

    int colon = sourceIndex;

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

    if (index == min) {
      found = true;
    }

    if (!found) {
      throw new UnsupportedOperationException(
        "Implement me :: not inline macro :: index=" + index
      );
    }

    int nameStart = index;

    int preTextLength = nameStart - phrasingStart;

    if (preTextLength > 0) {
      // we'll resume at the (possible) inline macro name
      sourceIndex = nameStart;

      return toPhrasingEnd(sourceIndex);
    }

    int nameEnd = colon;

    var name = sourceGet(nameStart, nameEnd);

    var macro = pseudoInlineMacro();

    macro.name = name;

    return switch (name) {
      case "https" -> {
        // target start
        // includes protocol name
        stackPush(nameStart);

        yield Phrasing.URI_MACRO;
      }

      default -> Phrasing.CUSTOM_INLINE;
    };
  }

  private Phrasing phrasingInlineMacroEnd() {
    // pops phrasing start
    stackPop();

    nextNode = pseudoInlineMacro();

    // resume after ']'
    sourceIndex++;

    return Phrasing.STOP;
  }

  private Phrasing phrasingStart() {
    return switch (phrase) {
      case FRAGMENT -> fragmentPhrasingStart();

      case PARAGRAPH -> paragraphPhrasingStart();

      case TITLE -> titlePhrasingStart();
    };
  }

  private Phrasing phrasingText() {
    int endIndex = stackPop();

    int startIndex = stackPop();

    if (startIndex < endIndex) {
      var text = pseudoText();

      text.value = sourceGet(startIndex, endIndex);

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
    int start = stackPop();

    int bracket = sourceIndex;

    if (!sourceInc()) {
      return Phrasing.URI_MACRO_ROLLBACK;
    }

    var macro = pseudoInlineMacro();

    int end = bracket;

    macro.target = sourceGet(start, end);

    return switch (sourcePeek()) {
      case ']' -> throw new UnsupportedOperationException(
        "Implement me :: empty attrlist"
      );

      case '"' -> Phrasing.URI_MACRO_QUOTED;

      default -> Phrasing.URI_MACRO_TEXT;
    };
  }

  private Phrasing phrasingUriMacroQuoted() {
    // pushes text start
    stackPush(sourceIndex + 1);

    return Phrasing.URI_MACRO_QUOTED_LOOP;
  }

  private Phrasing phrasingUriMacroQuotedLoop() {
    if (!sourceInc()) {
      // pops text start
      stackPop();

      return Phrasing.URI_MACRO_ROLLBACK;
    }

    return switch (sourcePeek()) {
      case '"' -> Phrasing.URI_MACRO_QUOTED_TEXT;

      default -> Phrasing.URI_MACRO_QUOTED_LOOP;
    };
  }

  private Phrasing phrasingUriMacroQuotedText() {
    int textStart = stackPop();

    int textEnd = sourceIndex;

    if (!sourceInc()) {
      return Phrasing.URI_MACRO_ROLLBACK;
    }

    var macro = pseudoInlineMacro();

    macro.textStart = textStart;

    macro.textEnd = textEnd;

    return switch (sourcePeek()) {
      case ']' -> Phrasing.INLINE_MACRO_END;

      default -> throw new UnsupportedOperationException("Implement me");
    };
  }

  private Phrasing phrasingUriMacroRollback() {
    throw new UnsupportedOperationException(
      "Implement me :: rollback url macro"
    );
  }

  private Phrasing phrasingUriMacroTarget() {
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

  private Phrasing phrasingUriMacroText() {
    // pushes text start
    stackPush(sourceIndex);

    return Phrasing.URI_MACRO_TEXT_LOOP;
  }

  private Phrasing phrasingUriMacroTextEnd() {
    var macro = pseudoInlineMacro();

    macro.textStart = stackPop();

    macro.textEnd = sourceIndex;

    return Phrasing.INLINE_MACRO_END;
  }

  private Phrasing phrasingUriMacroTextLoop() {
    if (!sourceInc()) {
      // pops text start
      stackPop();

      return Phrasing.URI_MACRO_ROLLBACK;
    }

    return switch (sourcePeek()) {
      case ']' -> Phrasing.URI_MACRO_TEXT_END;

      case '=' -> throw new UnsupportedOperationException(
        "Implement me :: maybe attrlist?"
      );

      default -> Phrasing.URI_MACRO_TEXT_LOOP;
    };
  }

  private Phrasing popAndStop() {
    // pops start index
    stackPop();

    return Phrasing.STOP;
  }

  private void pre() {
    var state = Pre.START;

    while (state != Pre.STOP) {
      state = switch (state) {
        case EOL -> preEol();

        case LOOP -> preLoop();

        case MAKE_TEXT -> preMakeText();

        case MARKER -> preMarker();

        case MARKER_COUNT -> preMarkerCount();

        case MARKER_FOUND -> preMarkerFound();

        case MARKER_LOOP -> preMarkerLoop();

        case MARKER_ROLLBACK -> preMarkerRollback();

        case MARKER_TRIM -> preMarkerTrim();

        case START -> preStart();

        case STOP -> preStop();

        case TRIM -> preTrim();
      };
    }
  }

  private Pre preEol() {
    if (!sourceInc()) {
      return Pre.MAKE_TEXT;
    }

    return switch (sourcePeek()) {
      case '-' -> Pre.MARKER;

      default -> Pre.LOOP;
    };
  }

  private Pre preLoop() {
    if (!sourceInc()) {
      return Pre.MAKE_TEXT;
    }

    return switch (sourcePeek()) {
      case '\n' -> Pre.EOL;

      default -> Pre.LOOP;
    };
  }

  private Pre preMakeText() {
    throw new UnsupportedOperationException("Implement me");
  }

  private Pre preMarker() {
    // pushes pre end
    // before EOL
    stackPush(sourceIndex - 1);

    // pushes marker count
    stackPush(0);

    return Pre.MARKER_LOOP;
  }

  private Pre preMarkerCount() {
    int length = stackPop();

    if (length == pseudoListingBlock().markerLength) {
      return Pre.MARKER_FOUND;
    } else {
      throw new UnsupportedOperationException("Implement me");
    }
  }

  private Pre preMarkerFound() {
    var block = pseudoListingBlock();

    block.last = true;

    var text = pseudoText();

    int end = stackPop();

    int start = stackPop();

    text.value = sourceGet(start, end);

    nextNode = text;

    return Pre.TRIM;
  }

  private Pre preMarkerLoop() {
    if (!sourceInc()) {
      return Pre.MARKER_ROLLBACK;
    }

    stackInc();

    return switch (sourcePeek()) {
      case '-' -> Pre.MARKER_LOOP;

      case '\t', '\f', ' ' -> Pre.MARKER_TRIM;

      case '\n' -> Pre.MARKER_COUNT;

      default -> Pre.MARKER_ROLLBACK;
    };
  }

  private Pre preMarkerRollback() {
    throw new UnsupportedOperationException("Implement me");
  }

  private Pre preMarkerTrim() {
    if (!sourceInc()) {
      return Pre.MARKER_COUNT;
    }

    return switch (sourcePeek()) {
      case '\t', '\f', ' ' -> Pre.MARKER_TRIM;

      case '\n' -> Pre.MARKER_COUNT;

      default -> Pre.MARKER_ROLLBACK;
    };
  }

  private Pre preStart() {
    // pushes pre start
    stackPush(sourceIndex);

    return Pre.LOOP;
  }

  private Pre preStop() {
    throw new AssertionError(
      "STOP action should not have been called"
    );
  }

  private Pre preTrim() {
    if (!sourceInc()) {
      return Pre.STOP;
    }

    return switch (sourcePeek()) {
      case '\n' -> Pre.TRIM;

      default -> Pre.STOP;
    };
  }

  private PseudoAttributes pseudoAttributes() {
    return pseudoFactory(PSEUDO_ATTRIBUTES, PseudoAttributes::new);
  }

  private PseudoDocument pseudoDocument() {
    return pseudoFactory(PSEUDO_DOCUMENT, PseudoDocument::new);
  }

  private PseudoEmphasis pseudoEmphasis() {
    return pseudoFactory(PSEUDO_EMPHASIS, PseudoEmphasis::new);
  }

  @SuppressWarnings("unchecked")
  private <T> T pseudoFactory(int index, Function<InternalSink, T> factory) {
    var result = pseudoArray[index];

    if (result == null) {
      result = pseudoArray[index] = factory.apply(this);
    }

    return (T) result;
  }

  private PseudoHeader pseudoHeader() {
    return pseudoFactory(PSEUDO_HEADER, PseudoHeader::new);
  }

  private PseudoInlineMacro pseudoInlineMacro() {
    return pseudoFactory(PSEUDO_INLINE_MACRO, PseudoInlineMacro::new);
  }

  private PseudoListingBlock pseudoListingBlock() {
    return pseudoFactory(PSEUDO_LISTING_BLOCK, PseudoListingBlock::new);
  }

  private PseudoListItem pseudoListItem() {
    return pseudoFactory(PSEUDO_LIST_ITEM, PseudoListItem::new);
  }

  private PseudoMonospaced pseudoMonospaced() {
    return pseudoFactory(PSEUDO_MONOSPACED, PseudoMonospaced::new);
  }

  private PseudoParagraph pseudoParagraph() {
    return pseudoFactory(PSEUDO_PARAGRAPH, PseudoParagraph::new);
  }

  private PseudoSection pseudoSection() {
    return pseudoFactory(PSEUDO_SECTION, PseudoSection::new);
  }

  private PseudoStrong pseudoStrong() {
    return pseudoFactory(PSEUDO_STRONG, PseudoStrong::new);
  }

  private PseudoText pseudoText() {
    return pseudoFactory(PSEUDO_TEXT, PseudoText::new);
  }

  private PseudoTitle pseudoTitle() {
    return pseudoFactory(PSEUDO_TITLE, PseudoTitle::new);
  }

  private PseudoUnorderedList pseudoUnorderedList() {
    return pseudoFactory(PSEUDO_ULIST, PseudoUnorderedList::new);
  }

  private String searchAttribute(String name) {
    var document = pseudoDocument();

    return document.getAttribute(name);
  }

  private void sectionParse(Parse initialState) {
    pseudoAttributes().clear();

    var state = initialState;

    while (state != Parse.STOP) {
      state = switch (state) {
        case BODY_TRIM -> sectionParseBodyTrim();

        case EXHAUSTED -> sectionParseExhausted();

        case LISTING_BLOCK_STOP -> sectionParseListingBlockStop();

        case PARAGRAPH -> sectionParseParagraph();

        case SECTION -> sectionParseSection();

        case ULIST_STOP -> sectionParseUlistStop();

        default -> parse(state);
      };
    }
  }

  private Parse sectionParseListingBlockStop() {
    stackPush(PseudoSection.BLOCK);

    return Parse.STOP;
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
    // replaces section level
    stackReplace(PseudoSection.EXHAUSTED);

    return Parse.STOP;
  }

  private Parse sectionParseParagraph() {
    stackPush(PseudoSection.BLOCK);

    nextNode = pseudoParagraph();

    return Parse.STOP;
  }

  private Parse sectionParseSection() {
    int nextLevel = stackPop();

    int sourceIndex = stackPop();

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

  private Parse sectionParseUlistStop() {
    int markerEnd = stackPop();

    int markerStart = stackPop();

    int ulistTop = stackPop();

    stackPush(ulistTop);

    stackPush(markerStart, markerEnd);

    stackPush(PseudoSection.BLOCK);

    nextNode = pseudoUnorderedList();

    return Parse.STOP;
  }

  private void sourceAdvance() {
    sourceIndex++;
  }

  private char sourceAt(int index) {
    return source.charAt(index);
  }

  private String sourceGet(int start, int end) {
    return source.substring(start, end);
  }

  private boolean sourceInc() {
    sourceAdvance();

    return sourceIndex < sourceMax;
  }

  private void sourceIndex(int value) {
    sourceIndex = value;
  }

  private boolean sourceMore() {
    return sourceIndex < sourceMax;
  }

  private char sourcePeek() {
    return source.charAt(sourceIndex);
  }

  private void stackAssert(int expected) {
    int actual = stackPeek();

    assert actual == expected : "actual=" + actual + ";expected=" + expected;
  }

  private void stackDec() {
    stackArray[stackIndex]--;
  }

  private void stackInc() {
    stackArray[stackIndex]++;
  }

  private int stackPeek() {
    return stackArray[stackIndex];
  }

  private int stackPeek(int offset) {
    return stackArray[stackIndex - offset];
  }

  private int stackPop() {
    return stackArray[stackIndex--];
  }

  private void stackPop(int count) {
    stackIndex -= count;
  }

  private void stackPush(int v0) {
    stackArray = IntArrays.growIfNecessary(stackArray, stackIndex + 1);
    stackArray[++stackIndex] = v0;
  }

  private void stackPush(int v0, int v1) {
    stackArray = IntArrays.growIfNecessary(stackArray, stackIndex + 2);
    stackArray[++stackIndex] = v0;
    stackArray[++stackIndex] = v1;
  }

  private void stackPush(int v0, int v1, int v2) {
    stackArray = IntArrays.growIfNecessary(stackArray, stackIndex + 3);
    stackArray[++stackIndex] = v0;
    stackArray[++stackIndex] = v1;
    stackArray[++stackIndex] = v2;
  }

  private void stackReplace(int value) {
    stackArray[stackIndex] = value;
  }

  private void stackStub() {
    int ctx = stackPeek();

    throw new UnsupportedOperationException(
      "Implement me :: ctx=" + ctx
    );
  }

  private void start(String source) {
    this.source = source;

    sourceIndex = 0;

    sourceMax = source.length();

    stackIndex = -1;
  }

  private Phrasing titlePhrasingEol() {
    return toPhrasingEnd(sourceIndex);
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

  private Phrasing toConstrainedBold() {
    int symbolStart = stackPop();

    int symbolEnd = sourceIndex;

    var strong = pseudoStrong();

    // start after symbol
    strong.textStart = symbolStart + 1;

    strong.textEnd = symbolEnd;

    nextNode = strong;

    return Phrasing.STOP;
  }

  private Phrasing toConstrainedItalic() {
    int symbolStart = stackPop();

    int symbolEnd = sourceIndex;

    var emphasis = pseudoEmphasis();

    // start after symbol
    emphasis.textStart = symbolStart + 1;

    emphasis.textEnd = symbolEnd;

    nextNode = emphasis;

    return Phrasing.STOP;
  }

  private Phrasing toConstrainedMonospace() {
    int symbolStart = stackPop();

    int symbolEnd = sourceIndex;

    var monospaced = pseudoMonospaced();

    // start after symbol
    monospaced.textStart = symbolStart + 1;

    monospaced.textEnd = symbolEnd;

    nextNode = monospaced;

    return Phrasing.STOP;
  }

  private Phrasing toPhrasingEnd(int last) {
    stackPush(last);

    return Phrasing.TEXT;
  }

  private Parse toUlist() {
    int markerStart = stackPop();

    stackPush(MARKER_ULIST, markerStart);

    // marker end
    stackPush(sourceIndex);

    return Parse.ULIST;
  }

}