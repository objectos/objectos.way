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
import objectos.asciidoc.pseudom.IterableOnce;
import objectos.asciidoc.pseudom.Node;
import objectos.asciidoc.pseudom.Node.ListItem;

public final class PseudoListItem extends PseudoNode
    implements ListItem, IterableOnce<Node>, Iterator<Node> {

  private enum Parse {
    STOP,

    MAYBE_MORE_TEXT,
    NOT_MORE_TEXT,

    MAYBE_NEXT_ITEM,
    MAYBE_NEXT_ITEM_TRIM,
    NEXT_ITEM,
    NOT_NEXT_ITEM;
  }

  private static final int NODES = -900;
  private static final int ITERATOR = -901;
  private static final int FIRST = -902;
  @SuppressWarnings("unused")
  private static final int FIRST_CONSUMED = -903;
  private static final int FIRST_LAST = -904;
  private static final int FIRST_LAST_CONSUMED = -905;
  static final int NEXT_ITEM = -906;
  static final int LIST_EXHAUSTED = -907;

  PseudoListItem(InternalSink sink) {
    super(sink);
  }

  @Override
  public final boolean hasNext() {
    switch (stackPeek()) {
      case FIRST_LAST_CONSUMED -> parse(Parse.MAYBE_MORE_TEXT);

      case ITERATOR -> {
        parseTextSingleLine();

        // sure to have at least one node
        stackReplace(isLast() ? FIRST_LAST : FIRST);
      }

      case FIRST_LAST -> {}

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
    return nextNode();
  }

  @Override
  public final IterableOnce<Node> nodes() {
    switch (stackPeek()) {
      case PseudoUnorderedList.ITEM_CONSUMED -> stackReplace(NODES);

      default -> stackStub();
    }

    return this;
  }

  private void parse(Parse initialState) {
    stackPop(); // previous state

    var state = initialState;

    while (state != Parse.STOP) {
      state = switch (state) {
        case MAYBE_MORE_TEXT -> parseMaybeMoreText();

        case MAYBE_NEXT_ITEM -> parseMaybeNextItem();

        case MAYBE_NEXT_ITEM_TRIM -> parseMaybeNextItemTrim();

        case NEXT_ITEM -> parseNextItem();

        case NOT_MORE_TEXT -> parseNotMoreText();

        default -> throw new UnsupportedOperationException(
          "Implement me :: state=" + state
        );
      };
    }
  }

  private Parse parseNotMoreText() {
    stackPush(LIST_EXHAUSTED);

    return Parse.STOP;
  }

  private Parse parseMaybeNextItemTrim() {
    if (!sourceMore()) {
      return Parse.NOT_NEXT_ITEM;
    }

    return switch (sourcePeek()) {
      case '\t', '\f', ' ' -> advance(Parse.MAYBE_NEXT_ITEM_TRIM);

      default -> Parse.NEXT_ITEM;
    };
  }

  private Parse parseMaybeMoreText() {
    if (!sourceMore()) {
      return Parse.NOT_MORE_TEXT;
    }

    return switch (sourcePeek()) {
      case '-' -> {
        // marker start
        stackPush(sourceIndex());

        yield advance(Parse.MAYBE_NEXT_ITEM);
      }

      default -> throw new UnsupportedOperationException(
        "Implement me :: peek=" + sourcePeek()
      );
    };
  }

  private Parse parseMaybeNextItem() {
    if (!sourceMore()) {
      return Parse.NOT_NEXT_ITEM;
    }

    return switch (sourcePeek()) {
      case '\t', '\f', ' ' -> {
        // marker end
        stackPush(sourceIndex());

        yield advance(Parse.MAYBE_NEXT_ITEM_TRIM);
      }

      default -> Parse.NOT_NEXT_ITEM;
    };
  }

  private Parse parseNextItem() {
    stackPush(NEXT_ITEM);

    return Parse.STOP;
  }

}