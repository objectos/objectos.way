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

  private static final int ATTRLIST_BLOCK = -1;
  static final int ULIST_TOP = -2;

  private final InternalSink sink;

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

  final boolean isLast() {
    var next = sink.nextNode;

    if (next != null && next instanceof PseudoText text) {
      return text.last;
    } else {
      return false;
    }
  }

  final PseudoListItem listItem() {
    return sink.pseudoListItem();
  }

  final Node nextNode() {
    if (hasNext()) {
      return sink.nextNode();
    } else {
      throw new NoSuchElementException();
    }
  }

  final void nextNode(Node value) {
    sink.nextNode = value;
  }

  final PseudoParagraph paragraph() {
    return sink.pseudoParagraph();
  }

  final Parse parseBodyTrim() {
    if (!sourceMore()) {
      return Parse.EXHAUSTED;
    }

    return switch (sourcePeek()) {
      case '\n' -> advance(Parse.BODY_TRIM);

      default -> Parse.BODY;
    };
  }

  final Parse parseDocumentOrSection(Parse state) {
    return switch (state) {
      case ATTRLIST -> parseAttrlist();

      case BODY -> parseBody();

      case MAYBE_ATTRLIST -> parseMaybeAttrlist();

      case MAYBE_ATTRLIST_END -> parseMaybeAttrlistEnd();

      case MAYBE_ATTRLIST_END_TRIM -> parseMaybeAttrlistEndTrim();

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

  final void parseTextRegular() {
    sink.parseTextRegular();
  }

  final void parseTextSingleLine() {
    sink.parseTextSingleLine();
  }

  final PseudoAttributes pseudoAttributes() {
    return sink.pseudoAttributes();
  }

  final PseudoSection section() {
    return sink.pseudoSection();
  }

  final String sourceGet(int start, int end) {
    return sink.sourceGet(start, end);
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

  final int stackPop() {
    return sink.stackPop();
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

  final PseudoUnorderedList unorderedList() {
    return sink.pseudoUnorderedList();
  }

  private Parse parseAttrlist() {
    int type = stackPop();

    // pops rollback index
    stackPop();

    if (type == ATTRLIST_BLOCK) {
      return Parse.BODY;
    } else {
      throw new UnsupportedOperationException(
        "Implement me :: continue text"
      );
    }
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

    if (context != ATTRLIST_BLOCK) {
      throw new UnsupportedOperationException(
        "Implement me :: inline attrlist"
      );
    }

    return Parse.MAYBE_ATTRLIST_END_TRIM;
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

  private Parse parseMaybeListingOrUlist() {
    if (!sourceMore()) {
      return Parse.NOT_LISTING_OR_ULIST;
    }

    return switch (sourcePeek()) {
      case '\t', '\f', ' ' -> {
        int markerStart = stackPop();

        stackPush(ULIST_TOP, markerStart);

        // marker end
        stackPush(sourceIndex());

        yield advance(Parse.MAYBE_ULIST);
      }

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

}