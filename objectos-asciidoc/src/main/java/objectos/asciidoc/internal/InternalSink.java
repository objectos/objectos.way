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

  private enum ListItemPhrasing {
    STOP,

    MARKER,
    MARKER_LOOP,
    MARKER_LOOP_TRIM,
    MARKER_STOP,

    MAYBE_INDENTATION,
    NEXT_ITEM_OR_NESTED,
    NEXT_ITEM,
    NOT_NEXT_ITEM,
    NESTED,
    NESTED_END,

    END_TRIM,
    END,

    TEXT;
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

    BODY,
    BODY_TRIM,

    MAYBE_ATTRLIST,
    NAME_OR_VALUE,
    MAYBE_ATTRLIST_END,
    MAYBE_ATTRLIST_END_TRIM,
    ATTRLIST,
    NOT_ATTRLIST,

    MAYBE_BOLD_OR_ULIST,
    NOT_BOLD_OR_ULIST,

    LISTING_OR_ULIST,
    NOT_LISTING_OR_ULIST,
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

    LIST_ITEM,

    LIST_ITEM_NEXT,

    PARAGRAPH,

    TITLE;
  }

  private enum Phrasing {
    START,

    STOP,

    BLOB,

    TEXT,

    CONSTRAINED_MONOSPACE,
    CONSTRAINED_MONOSPACE_END,
    CONSTRAINED_MONOSPACE_LOOP,
    CONSTRAINED_MONOSPACE_ROLLBACK,

    EOL,

    INLINE_MACRO,
    INLINE_MACRO_END,

    CUSTOM_INLINE,
    CUSTOM_INLINE_ROLLBACK,

    URI_MACRO,
    URI_MACRO_ATTRLIST,
    URI_MACRO_ROLLBACK,
    URI_MACRO_TARGET,
    URI_MACRO_TARGET_LOOP,
    URI_MACRO_TEXT,
    URI_MACRO_TEXT_END,
    URI_MACRO_TEXT_LOOP,

    AUTOLINK;
  }

  private static final int ATTRLIST_BLOCK = -1;
  private static final int _ULIST_TOP = -2;
  private static final int HINT_NEXT_ITEM = -3;
  private static final int HINT_NESTED_END = -4;

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
  private static final int PSEUDO_LENGTH = 11;

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

    return pseudoDocument();
  }

  final void appendTo(Appendable out, int start, int end) throws IOException {
    out.append(source, start, end);
  }

  final PseudoAttributes attributes() {
    return pseudoAttributes();
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

      case PseudoInlineMacro.NODE_CONSUMED -> {
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
      case PseudoParagraph.NODE_CONSUMED -> stackReplace(PseudoInlineMacro.NODES);

      default -> stackStub();
    }
  }

  final boolean listItemHasNext() {
    switch (stackPeek()) {
      case PseudoUnorderedList.EXHAUSTED -> stackReplace(PseudoListItem.EXHAUSTED);

      case PseudoListItem.ITERATOR -> {
        stackPop();

        phrasing(PhraseElement.LIST_ITEM);

        if (nextNode != null) {
          stackPush(PseudoListItem.TEXT);
        } else {
          throw new UnsupportedOperationException("Implement me");
        }
      }

      case PseudoListItem.TEXT -> {}

      case PseudoListItem.TEXT_CONSUMED -> {
        // pops state
        stackPop();

        phrasing(PhraseElement.LIST_ITEM_NEXT);

        if (nextNode == null) {
          stackPush(PseudoListItem.EXHAUSTED);
        } else if (nextNode instanceof Node.Text) {
          stackPush(PseudoListItem.TEXT);
        } else if (nextNode instanceof Node.UnorderedList) {
          stackPush(PseudoListItem.ULIST);
        } else {
          throw new UnsupportedOperationException(
            "Implement me :: node.class=" + nextNode.getClass()
          );
        }

        /*
        int hint = stackPeek();
        
        switch (hint) {
          case _NESTED -> {
            // pops hint
            stackPop();
        
            nextNode = pseudoUnorderedList();
        
            stackPush(PseudoListItem.ULIST);
          }
        
          case _NESTED_END -> stackPush(PseudoListItem.EXHAUSTED);
        
          case _NEXT_ITEM -> stackPush(PseudoListItem.EXHAUSTED);
        
          default -> stackPush(_LIST_END, PseudoListItem.EXHAUSTED);
        }
        */
      }

      case PseudoListItem.ULIST -> {}

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
      case PseudoParagraph.NODE_CONSUMED -> stackReplace(PseudoMonospaced.NODES);

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
      case PseudoParagraph.ITERATOR,
           PseudoParagraph.NODE_CONSUMED,
           PseudoInlineMacro.EXHAUSTED,
           PseudoMonospaced.EXHAUSTED -> {
        stackReplace(PseudoParagraph.PARSE);

        phrasing(PhraseElement.PARAGRAPH);

        if (nextNode != null) {
          stackReplace(PseudoParagraph.NODE);
        } else {
          stackReplace(PseudoParagraph.EXHAUSTED);
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
           PseudoSection.PARAGRAPH_CONSUMED -> stackReplace(PseudoParagraph.NODES);

      default -> stackStub();
    }
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

  final boolean unorderedListHasNext() {
    switch (stackPeek()) {
      case PseudoUnorderedList.ITEM -> {}

      case PseudoListItem.EXHAUSTED -> {
        // pops state
        stackPop();

        int hint = stackPop();

        switch (hint) {
          case HINT_NESTED_END -> stackPush(PseudoUnorderedList.EXHAUSTED);

          case HINT_NEXT_ITEM -> {
            nextNode = pseudoListItem();

            stackPush(PseudoUnorderedList.ITEM);
          }

          /*
          case _LIST_END -> {
            // marker end
            stackPop();
          
            // marker start
            stackPop();
          
            int top = stackPop();
          
            assert top == _ULIST_TOP : "top=" + top;
          
            stackPush(PseudoUnorderedList.EXHAUSTED);
          }
          */

          default -> {
            // not a hint, push it back
            stackPush(hint);

            stackPush(PseudoUnorderedList.EXHAUSTED);
          }
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
      case PseudoDocument.ULIST_CONSUMED,
           PseudoSection.ULIST_CONSUMED,
           PseudoListItem.ULIST_CONSUMED -> stackReplace(PseudoUnorderedList.NODES);

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

        case ULIST_STOP -> documentParseUlistStop();

        default -> parse(state);
      };
    }
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

  private Parse documentParseUlistStop() {
    stackPush(PseudoDocument.ULIST);

    nextNode = pseudoUnorderedList();

    return Parse.STOP;
  }

  private boolean finalState() {
    return stackIndex == -1;
  }

  private Phrasing fragmentPhrasingEol() {
    throw new UnsupportedOperationException("Implement me");
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

  private Phrasing listItemNextPhrasingEol() {
    throw new UnsupportedOperationException("Implement me");
  }

  private Phrasing listItemNextPhrasingStart() {
    if (sourceEof()) {
      return Phrasing.STOP;
    }

    return switch (sourcePeek()) {
      case '-', '*' -> listItemPhrasing(sourceIndex, ListItemPhrasing.MARKER);

      default -> throw new UnsupportedOperationException("Implement me");
    };
  }

  private Phrasing listItemPhrasing(int eol, ListItemPhrasing initial) {
    Phrasing result = null;

    var state = initial;

    while (state != ListItemPhrasing.STOP) {
      state = switch (state) {
        case END -> {
          result = toPhrasingEnd(eol);

          yield ListItemPhrasing.STOP;
        }

        case END_TRIM -> listItemPhrasingEndTrim();

        case MARKER -> listItemPhrasingMarker();

        case MARKER_LOOP -> listItemPhrasingMarkerLoop();

        case MARKER_LOOP_TRIM -> listItemPhrasingMarkerLoopTrim();

        case MARKER_STOP -> listItemPhrasingMarkerStop();

        case MAYBE_INDENTATION -> listItemPhrasingMaybeIndentation();

        case NESTED -> {
          nextNode = pseudoUnorderedList();

          result = Phrasing.STOP;

          yield ListItemPhrasing.STOP;
        }

        case NESTED_END -> {
          result = Phrasing.STOP;

          yield ListItemPhrasing.STOP;
        }

        case NEXT_ITEM -> {
          result = Phrasing.STOP;

          yield ListItemPhrasing.STOP;
        }

        case NEXT_ITEM_OR_NESTED -> listItemPhrasingNextItemOrNested(eol);

        case NOT_NEXT_ITEM -> {
          throw new UnsupportedOperationException("Implement me");
        }

        case TEXT -> {
          result = toPhrasingEnd(eol);

          yield ListItemPhrasing.STOP;
        }

        default -> throw new UnsupportedOperationException(
          "Implement me :: state=" + state
        );
      };
    }

    assert result != null;

    return result;
  }

  private ListItemPhrasing listItemPhrasingEndTrim() {
    if (!sourceMore()) {
      return ListItemPhrasing.END;
    }

    return switch (sourcePeek()) {
      case '\n' -> advance(ListItemPhrasing.END_TRIM);

      default -> ListItemPhrasing.END;
    };
  }

  private Phrasing listItemPhrasingEol() {
    int atEol = sourceIndex;

    if (!sourceInc()) {
      return toPhrasingEnd(atEol);
    }

    return switch (sourcePeek()) {
      case '\t', '\f', ' ' -> listItemPhrasing(atEol, ListItemPhrasing.MAYBE_INDENTATION);

      case '\n' -> listItemPhrasing(atEol, ListItemPhrasing.END_TRIM);

      case '-', '*' -> listItemPhrasing(atEol, ListItemPhrasing.MARKER);

      default -> advance(Phrasing.BLOB);
    };
  }

  private ListItemPhrasing listItemPhrasingMarker() {
    int markerStart = sourceIndex;

    char marker = sourceAt(markerStart);

    // marker start
    stackPush(markerStart, marker);

    return ListItemPhrasing.MARKER_LOOP;
  }

  private ListItemPhrasing listItemPhrasingMarkerLoop() {
    if (!sourceInc()) {
      return ListItemPhrasing.NOT_NEXT_ITEM;
    }

    char peek = sourcePeek();

    char marker = (char) stackPeek();

    if (peek == marker) {
      return ListItemPhrasing.MARKER_LOOP;
    }

    return switch (peek) {
      case '\t', '\f', ' ' -> {
        // pops marker
        stackPop();

        // pushes marker end
        stackPush(sourceIndex);

        yield ListItemPhrasing.MARKER_LOOP_TRIM;
      }

      default -> ListItemPhrasing.NOT_NEXT_ITEM;
    };
  }

  private ListItemPhrasing listItemPhrasingMarkerLoopTrim() {
    if (!sourceInc()) {
      return ListItemPhrasing.NOT_NEXT_ITEM;
    }

    return switch (sourcePeek()) {
      case '\t', '\f', ' ' -> ListItemPhrasing.MARKER_LOOP_TRIM;

      default -> ListItemPhrasing.MARKER_STOP;
    };
  }

  private ListItemPhrasing listItemPhrasingMarkerStop() {
    return switch (phrase) {
      case LIST_ITEM -> {
        // pops marker end
        stackPop();

        // resume at marker start
        sourceIndex = stackPop();

        yield ListItemPhrasing.TEXT;
      }

      case LIST_ITEM_NEXT -> ListItemPhrasing.NEXT_ITEM_OR_NESTED;

      default -> throw new AssertionError(
        "Unexpected phrase=" + phrase
      );
    };
  }

  private ListItemPhrasing listItemPhrasingMaybeIndentation() {
    if (!sourceMore()) {
      return ListItemPhrasing.NOT_NEXT_ITEM;
    }

    return switch (sourcePeek()) {
      case '\t', '\f', ' ' -> advance(ListItemPhrasing.MAYBE_INDENTATION);

      case '-', '*' -> ListItemPhrasing.MARKER;

      default -> ListItemPhrasing.NOT_NEXT_ITEM;
    };
  }

  private ListItemPhrasing listItemPhrasingNextItemOrNested(int eol) {
    var thisEnd = stackPop();
    var thisStart = stackPop();
    /*var phrasingStart =*/stackPop();
    var thisMarker = sourceGet(thisStart, thisEnd);

    int prevEnd = stackPop();
    int prevStart = stackPop();
    var prevMarker = sourceGet(prevStart, prevEnd);

    if (thisMarker.equals(prevMarker)) {
      // updates marker to current indexes
      stackPush(thisStart, thisEnd);

      // pushes HINT
      stackPush(HINT_NEXT_ITEM);

      return ListItemPhrasing.NEXT_ITEM;
    }

    int maybeTop = stackPeek();

    if (maybeTop == _ULIST_TOP) {
      // keep first level marker around
      stackPush(prevStart, prevEnd);

      // pushes the new level marker
      stackPush(thisStart, thisEnd);

      return ListItemPhrasing.NESTED;
    }

    var found = false;

    int offset = 0;

    while (maybeTop != _ULIST_TOP) {
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
      stackPush(HINT_NEXT_ITEM, HINT_NESTED_END);

      return ListItemPhrasing.NESTED_END;
    }

    throw new UnsupportedOperationException(
      "Implement me :: prevMarker=" + prevMarker + ";thisMarker=" + thisMarker
    );
  }

  private Phrasing listItemPhrasingStart() {
    return Phrasing.BLOB;
  }

  private Phrasing paragraphPhrasingEol() {
    int atEol = sourceIndex;

    sourceAdvance();

    if (!sourceMore()) {
      return toPhrasingEnd(atEol);
    }

    return switch (sourcePeek()) {
      case '\n' -> {
        var next = toPhrasingEnd(atEol);

        sourceIndex(atEol);

        yield next;
      }

      default -> advance(Phrasing.BLOB);
    };
  }

  private Phrasing paragraphPhrasingStart() {
    if (!sourceMore()) {
      return popAndStop();
    }

    return switch (sourcePeek()) {
      case '\n' -> {
        sourceAdvance();

        if (!sourceMore()) {
          yield popAndStop();
        }

        char next = sourceNext();

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
      case ATTRLIST -> parseAttrlist();

      case BODY -> parseBody();

      case LISTING_OR_ULIST -> parseListingOrUlist();

      case MAYBE_ATTRLIST -> parseMaybeAttrlist();

      case MAYBE_ATTRLIST_END -> parseMaybeAttrlistEnd();

      case MAYBE_ATTRLIST_END_TRIM -> parseMaybeAttrlistEndTrim();

      case MAYBE_BOLD_OR_ULIST -> parseMaybeBoldOrUlist();

      case MAYBE_SECTION -> parseMaybeSection();

      case MAYBE_SECTION_TRIM -> parseMaybeSectionTrim();

      case NAME_OR_VALUE -> parseNameOrValue();

      case NOT_SECTION -> parseNotSection();

      case ULIST -> parseUlist();

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
        stackPush(sourceIndex, ATTRLIST_BLOCK);

        yield advance(Parse.MAYBE_ATTRLIST);
      }

      case '-' -> {
        // rollback index or marker start
        stackPush(sourceIndex);

        yield Parse.LISTING_OR_ULIST;
      }

      case '*' -> {
        // rollback index or marker start
        stackPush(sourceIndex);

        yield advance(Parse.MAYBE_BOLD_OR_ULIST);
      }

      case '=' -> {
        stackPush(sourceIndex);

        // push title level
        stackPush(0);

        yield advance(Parse.MAYBE_SECTION);
      }

      default -> Parse.PARAGRAPH;
    };
  }

  private Parse parseListingOrUlist() {
    if (!sourceInc()) {
      return Parse.NOT_LISTING_OR_ULIST;
    }

    return switch (sourcePeek()) {
      case '\t', '\f', ' ' -> toUlist();

      case '-' -> throw new UnsupportedOperationException("Implement me");

      default -> Parse.NOT_LISTING_OR_ULIST;
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
        stackPush(sourceIndex);

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
      case '\t', '\f', ' ' -> toUlist();

      case '*' -> throw new UnsupportedOperationException("Implement me");

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

    int end = sourceIndex;

    var value = sourceGet(start, end);

    var attributes = pseudoAttributes();

    attributes.add(value);
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
        case AUTOLINK -> phrasingAutolink();

        case BLOB -> phrasingBlob();

        case CONSTRAINED_MONOSPACE -> phrasingConstrainedMonospace();

        case CONSTRAINED_MONOSPACE_END -> phrasingConstrainedMonospaceEnd();

        case CONSTRAINED_MONOSPACE_LOOP -> phrasingConstrainedMonospaceLoop();

        case CONSTRAINED_MONOSPACE_ROLLBACK -> phrasingConstrainedMonospaceRollback();

        case CUSTOM_INLINE -> phrasingCustomInline();

        case CUSTOM_INLINE_ROLLBACK -> phrasingCustomInlineRollback();

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

        case URI_MACRO_TEXT -> phrasingUriMacroText();

        case URI_MACRO_TEXT_END -> phrasingUriMacroTextEnd();

        case URI_MACRO_TEXT_LOOP -> phrasingUriMacroTextLoop();
      };
    }
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

      case '*' -> throw new UnsupportedOperationException("Implement me");

      case '_' -> throw new UnsupportedOperationException("Implement me");

      case ':' -> Phrasing.INLINE_MACRO;

      default -> advance(Phrasing.BLOB);
    };
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
        case ';', ':', '"', '\'', '`' -> true;

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

  private Phrasing phrasingConstrainedMonospaceEnd() {
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

      case '`' -> Phrasing.CONSTRAINED_MONOSPACE_END;

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

    return switch (sourcePeek()) {
      case '\n' -> Phrasing.CUSTOM_INLINE_ROLLBACK;

      default -> throw new UnsupportedOperationException(
        "Implement me :: custom inline"
      );
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

  private Phrasing phrasingCustomInlineRollback() {
    // no saved state
    // just resume blob parsing

    return Phrasing.BLOB;
  }

  private Phrasing phrasingEol() {
    return switch (phrase) {
      case FRAGMENT -> fragmentPhrasingEol();

      case LIST_ITEM -> listItemPhrasingEol();

      case LIST_ITEM_NEXT -> listItemNextPhrasingEol();

      case PARAGRAPH -> paragraphPhrasingEol();

      case TITLE -> titlePhrasingEol();
    };
  }

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
      case "https" -> Phrasing.URI_MACRO;

      default -> Phrasing.CUSTOM_INLINE;
    };
  }

  private Phrasing phrasingInlineMacroEnd() {
    // pops phrasing start
    stackPop();

    nextNode = pseudoInlineMacro();

    return Phrasing.STOP;
  }

  private Phrasing phrasingStart() {
    return switch (phrase) {
      case FRAGMENT -> fragmentPhrasingStart();

      case LIST_ITEM -> listItemPhrasingStart();

      case LIST_ITEM_NEXT -> listItemNextPhrasingStart();

      case PARAGRAPH -> paragraphPhrasingStart();

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
    int bracket = sourceIndex;

    if (!sourceInc()) {
      return Phrasing.URI_MACRO_ROLLBACK;
    }

    var macro = pseudoInlineMacro();

    macro.targetEnd = bracket;

    return switch (sourcePeek()) {
      case ']' -> throw new UnsupportedOperationException(
        "Implement me :: empty attrlist"
      );

      case '"' -> throw new UnsupportedOperationException(
        "Implement me :: quoted text"
      );

      default -> Phrasing.URI_MACRO_TEXT;
    };
  }

  private Phrasing phrasingUriMacroRollback() {
    throw new UnsupportedOperationException(
      "Implement me :: rollback url macro"
    );
  }

  private Phrasing phrasingUriMacroTarget() {
    var macro = pseudoInlineMacro();

    macro.targetStart = sourceIndex;

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

    // we resume AFTER the ']' char
    sourceIndex++;

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

  private PseudoAttributes pseudoAttributes() {
    return pseudoFactory(PSEUDO_ATTRIBUTES, PseudoAttributes::new);
  }

  private PseudoDocument pseudoDocument() {
    return pseudoFactory(PSEUDO_DOCUMENT, PseudoDocument::new);
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

  private PseudoText pseudoText() {
    return pseudoFactory(PSEUDO_TEXT, PseudoText::new);
  }

  private PseudoTitle pseudoTitle() {
    return pseudoFactory(PSEUDO_TITLE, PseudoTitle::new);
  }

  private PseudoUnorderedList pseudoUnorderedList() {
    return pseudoFactory(PSEUDO_ULIST, PseudoUnorderedList::new);
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

        case ULIST_STOP -> sectionParseUlistStop();

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

  private Parse sectionParseUlistStop() {
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

  private void sourceAdvance() {
    sourceIndex++;
  }

  private char sourceAt(int index) {
    return source.charAt(index);
  }

  private boolean sourceEof() {
    return sourceIndex >= sourceMax;
  }

  private String sourceGet(int start, int end) {
    return source.substring(start, end);
  }

  private boolean sourceInc() {
    sourceAdvance();

    return sourceMore();
  }

  private void sourceIndex(int value) {
    sourceIndex = value;
  }

  private boolean sourceMore() {
    return sourceIndex < sourceMax;
  }

  private char sourceNext() {
    return source.charAt(sourceIndex++);
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

    stackPush(_ULIST_TOP, markerStart);

    // marker end
    stackPush(sourceIndex);

    return Parse.ULIST;
  }

}