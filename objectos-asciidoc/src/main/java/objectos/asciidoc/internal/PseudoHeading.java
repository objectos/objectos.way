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
import objectos.asciidoc.pseudom.Heading;
import objectos.asciidoc.pseudom.IterableOnce;
import objectos.asciidoc.pseudom.Node;

public final class PseudoHeading extends PseudoNode
    implements Heading, IterableOnce<Node>, Iterator<Node> {

  private static final int NODES = -300;
  private static final int ITERATOR = -301;
  private static final int PARSE = -302;
  private static final int NODE = -303;
  @SuppressWarnings("unused")
  private static final int NODE_CONSUMED = -304;
  private static final int LAST = -305;
  private static final int LAST_CONSUMED = -306;
  static final int EXHAUSTED = -307;

  int level;

  PseudoHeading(InternalSink sink) {
    super(sink);
  }

  @Override
  public final int level() {
    return level;
  }

  @Override
  public final boolean hasNext() {
    switch (stackPeek()) {
      case ITERATOR -> {
        stackReplace(PARSE);

        parseTextHeading();

        // sure to have at least one node
        stackReplace(isLast() ? LAST : NODE);
      }

      case LAST -> {}

      case LAST_CONSUMED -> stackReplace(EXHAUSTED);

      default -> stackStub();
    }

    return hasNextNode();
  }

  @Override
  public final Node next() {
    return nextNode();
  }

  @Override
  public final Iterator<Node> iterator() {
    stackAssert(NODES);

    stackReplace(ITERATOR);

    return this;
  }

  @Override
  public final IterableOnce<Node> nodes() {
    switch (stackPeek()) {
      case PseudoHeader.TITLE_CONSUMED,
           PseudoSection.TITLE_CONSUMED -> stackReplace(NODES);

      default -> stackStub();
    }

    return this;
  }

}
