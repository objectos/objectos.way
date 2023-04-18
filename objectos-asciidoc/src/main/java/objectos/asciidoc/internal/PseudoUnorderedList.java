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
import objectos.asciidoc.pseudom.Node.UnorderedList;

public final class PseudoUnorderedList extends PseudoNode
    implements UnorderedList, IterableOnce<Node>, Iterator<Node> {

  private static final int NODES = -800;
  private static final int ITERATOR = -801;
  private static final int ITEM = -802;
  static final int ITEM_CONSUMED = -803;
  static final int EXHAUSTED = -804;

  PseudoUnorderedList(InternalSink sink) {
    super(sink);
  }

  @Override
  public final boolean hasNext() {
    switch (stackPeek()) {
      case ITEM -> {}

      case ITERATOR -> {
        var listItem = listItem();

        nextNode(listItem);

        stackReplace(ITEM);
      }

      case PseudoListItem.LIST_EXHAUSTED -> {
        // pops state
        int top = stackPop();

        while (top != ULIST_TOP) {
          top = stackPop();
        }

        stackPush(EXHAUSTED);
      }

      case PseudoListItem.NEXT_ITEM -> {
        // pops state
        stackPop();

        int thisEnd = stackPop();
        int thisStart = stackPop();
        var thisMarker = sourceGet(thisStart, thisEnd);

        int prevEnd = stackPop();
        int prevStart = stackPop();
        var prevMarker = sourceGet(prevStart, prevEnd);

        if (prevMarker.equals(thisMarker)) {
          stackPush(thisStart, thisEnd);

          nextNode(listItem());

          stackPush(ITEM);
        } else {
          throw new UnsupportedOperationException(
            "Implement me :: prevMarker=" + prevMarker + ";thisMarker=" + thisMarker
          );
        }
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
    return nextNode();
  }

  @Override
  public final IterableOnce<Node> nodes() {
    switch (stackPeek()) {
      case PseudoDocument.ULIST_CONSUMED -> stackReplace(NODES);

      default -> stackStub();
    }

    return this;
  }

}
