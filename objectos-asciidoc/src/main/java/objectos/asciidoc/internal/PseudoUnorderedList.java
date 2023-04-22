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

import java.util.Iterator;
import java.util.NoSuchElementException;
import objectos.asciidoc.pseudom.IterableOnce;
import objectos.asciidoc.pseudom.Node;
import objectos.asciidoc.pseudom.Node.UnorderedList;

public final class PseudoUnorderedList extends PseudoNode
    implements UnorderedList, IterableOnce<Node>, Iterator<Node> {

  public final class PseudoListItem implements ListItem, IterableOnce<Node>, Iterator<Node> {
    final PseudoUnorderedList outer = PseudoUnorderedList.this;

    @Override
    public final boolean hasNext() {
      return outer.itemHasNext();
    }

    @Override
    public final Iterator<Node> iterator() {
      outer.itemIterator();

      return this;
    }

    @Override
    public final Node next() {
      return outer.itemNext();
    }

    @Override
    public final IterableOnce<Node> nodes() {
      outer.itemNodes();

      return this;
    }
  }

  private enum ThisPhrasing {
    STOP,

    MAYBE_NEXT_ITEM,
    MAYBE_NEXT_ITEM_TRIM,
    NEXT_ITEM_OR_NESTED,
    NEXT_ITEM,
    NOT_NEXT_ITEM;
  }

  private static final int NODES = -800;
  private static final int ITERATOR = -801;
  private static final int ITEM = -802;
  static final int ITEM_CONSUMED = -803;
  private static final int ITEM_NODES = -804;
  private static final int ITEM_ITERATOR = -805;
  private static final int TEXT = -806;
  private static final int TEXT_CONSUMED = -807;
  private static final int ITEM_EXHAUSTED = -808;
  private static final int ULIST = -809;
  private static final int ULIST_CONSUMED = -810;
  static final int EXHAUSTED = -812;

  private final PseudoListItem item = new PseudoListItem();

  PseudoUnorderedList(InternalSink sink) {
    super(sink);
  }

  @Override
  public final boolean hasNext() {
    switch (stackPeek()) {
      case ITEM -> {}

      case ITEM_EXHAUSTED -> {
        // pops state
        stackPop();

        int hint = stackPop();

        switch (hint) {
          case _NESTED_END -> stackPush(EXHAUSTED);

          case _NEXT_ITEM -> {
            nextNode(item);

            stackPush(ITEM);
          }

          case _LIST_END -> {
            // marker end
            stackPop();

            // marker start
            stackPop();

            int top = stackPop();

            assert top == _ULIST_TOP : "top=" + top;

            stackPush(EXHAUSTED);
          }

          default -> throw new UnsupportedOperationException(
            "Implement me :: hint=" + hint
          );
        }
      }

      case ITERATOR -> {
        nextNode(item);

        stackReplace(ITEM);
      }

      default -> stackStub();
    }

    return hasNextNode();
  }

  @Override
  public final Iterator<Node> iterator() {
    stackAssert(NODES);

    stackReplace(ITERATOR);

    return this;
  }

  @Override
  public final Node next() {
    return nextNodeDefault();
  }

  @Override
  public final IterableOnce<Node> nodes() {
    switch (stackPeek()) {
      case PseudoDocument.ULIST_CONSUMED,
           ULIST_CONSUMED -> stackReplace(NODES);

      default -> stackStub();
    }

    return this;
  }

  @Override
  final Phrasing phrasingEol() {
    int atEol = sourceIndex();

    sourceAdvance();

    if (!sourceMore()) {
      return toPhrasingEnd(atEol);
    }

    return switch (sourcePeek()) {
      case '-', '*' -> thisPhrasing(atEol, ThisPhrasing.MAYBE_NEXT_ITEM);

      default -> advance(Phrasing.BLOB);
    };
  }

  @Override
  final Phrasing phrasingStart() {
    if (!sourceMore()) {
      return Phrasing.STOP;
    } else {
      return Phrasing.BLOB;
    }
  }

  private boolean itemHasNext() {
    switch (stackPeek()) {
      case EXHAUSTED -> stackReplace(ITEM_EXHAUSTED);

      case ITEM_ITERATOR -> {
        stackPop();

        phrasing();

        if (hasNextNode()) {
          stackPush(TEXT);
        } else {
          throw new UnsupportedOperationException("Implement me");
        }
      }

      case TEXT -> {}

      case TEXT_CONSUMED -> {
        // pops state
        stackPop();

        int hint = stackPeek();

        switch (hint) {
          case _NESTED -> {
            // pops hint
            stackPop();

            nextNode(this);

            stackPush(ULIST);
          }

          case _NESTED_END -> stackPush(ITEM_EXHAUSTED);

          case _NEXT_ITEM -> stackPush(ITEM_EXHAUSTED);

          default -> stackPush(_LIST_END, ITEM_EXHAUSTED);
        }
      }

      case ULIST -> {}

      default -> stackStub();
    }

    return hasNextNode();
  }

  private void itemIterator() {
    stackAssert(ITEM_NODES);

    stackReplace(ITEM_ITERATOR);
  }

  private Node itemNext() {
    if (itemHasNext()) {
      return nextNodeSink();
    } else {
      throw new NoSuchElementException();
    }
  }

  private void itemNodes() {
    switch (stackPeek()) {
      case PseudoUnorderedList.ITEM_CONSUMED -> stackReplace(ITEM_NODES);

      default -> stackStub();
    }
  }

  private Phrasing thisPhrasing(int eol, ThisPhrasing initial) {
    var markerStart = sourceIndex();

    var marker = sourceNext();

    Phrasing result = null;

    var state = initial;

    while (state != ThisPhrasing.STOP) {
      state = switch (state) {
        case MAYBE_NEXT_ITEM -> thisPhrasingMaybeNextItem(marker);

        case MAYBE_NEXT_ITEM_TRIM -> thisPhrasingMaybeNextItemTrim();

        case NEXT_ITEM -> {
          result = Phrasing.TEXT;

          yield ThisPhrasing.STOP;
        }

        case NEXT_ITEM_OR_NESTED -> thisPhrasingNextItemOrNested(eol, markerStart);

        case NOT_NEXT_ITEM -> {
          throw new UnsupportedOperationException("Implement me");
        }

        default -> throw new UnsupportedOperationException(
          "Implement me :: state=" + state
        );
      };
    }

    assert result != null;

    return result;
  }

  private ThisPhrasing thisPhrasingNextItemOrNested(int eol, int thisStart) {
    var thisEnd = stackPop();
    var phrasingStart = stackPop();
    var thisMarker = sourceGet(thisStart, thisEnd);

    int prevEnd = stackPop();
    int prevStart = stackPop();
    var prevMarker = sourceGet(prevStart, prevEnd);

    if (thisMarker.equals(prevMarker)) {
      // updates marker to current indexes
      stackPush(thisStart, thisEnd);

      // pushes hint
      stackPush(_NEXT_ITEM);

      // pushes text indexes
      stackPush(phrasingStart, eol);

      return ThisPhrasing.NEXT_ITEM;
    }

    int maybeTop = stackPeek();

    if (maybeTop == _ULIST_TOP) {
      // keep first level marker around
      stackPush(prevStart, prevEnd);

      // pushes the new level marker
      stackPush(thisStart, thisEnd);

      // pushes hint
      stackPush(_NESTED);

      // pushes text indexes
      stackPush(phrasingStart, eol);

      return ThisPhrasing.NEXT_ITEM;
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

      // pushes hint
      stackPush(_NEXT_ITEM, _NESTED_END);

      // pushes text indexes
      stackPush(phrasingStart, eol);

      return ThisPhrasing.NEXT_ITEM;
    }

    throw new UnsupportedOperationException(
      "Implement me :: prevMarker=" + prevMarker + ";thisMarker=" + thisMarker
    );
  }

  private ThisPhrasing thisPhrasingMaybeNextItem(char marker) {
    if (!sourceMore()) {
      return ThisPhrasing.NOT_NEXT_ITEM;
    }

    char peek = sourcePeek();

    if (peek == marker) {
      return advance(ThisPhrasing.MAYBE_NEXT_ITEM);
    }

    return switch (peek) {
      case '\t', '\f', ' ' -> {
        // marker end
        stackPush(sourceIndex());

        yield advance(ThisPhrasing.MAYBE_NEXT_ITEM_TRIM);
      }

      default -> ThisPhrasing.NOT_NEXT_ITEM;
    };
  }

  private ThisPhrasing thisPhrasingMaybeNextItemTrim() {
    if (!sourceMore()) {
      return ThisPhrasing.NOT_NEXT_ITEM;
    }

    return switch (sourcePeek()) {
      case '\t', '\f', ' ' -> advance(ThisPhrasing.MAYBE_NEXT_ITEM_TRIM);

      default -> ThisPhrasing.NEXT_ITEM_OR_NESTED;
    };
  }

}