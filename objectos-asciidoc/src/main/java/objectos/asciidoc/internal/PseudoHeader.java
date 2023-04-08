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
import objectos.asciidoc.pseudom.Header;
import objectos.asciidoc.pseudom.IterableOnce;
import objectos.asciidoc.pseudom.Node;

public final class PseudoHeader extends PseudoNode
    implements Header, IterableOnce<Node>, Iterator<Node> {

  private interface Parse {
    int START = 1;
    int STOP = 2;
  }

  private static final int NODES = -200;
  private static final int ITERATOR = -201;
  private static final int COMPUTED = -202;
  static final int HEADING_CONSUMED = -203;
  private static final int PARSE = -204;
  static final int EXHAUSTED = -205;

  PseudoHeader(InternalSink sink) {
    super(sink);
  }

  @Override
  public final boolean hasNext() {
    switch (stackPeek()) {
      case COMPUTED -> {}

      case ITERATOR -> {
        stackPop();

        switch (stackPeek()) {
          case PseudoDocument.HEADING_CONSUMED -> {
            stackPop();

            var heading = heading();

            heading.level = stackPop();

            nextNode(heading);

            stackPush(COMPUTED);
          }

          default -> stackStub();
        }
      }

      case HEADING_CONSUMED -> {
        stackReplace(PARSE);

        parse();
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
    stackAssert(PseudoDocument.HEADING_CONSUMED);

    stackPush(NODES);

    return this;
  }

  private void parse() {
    int state = Parse.START;

    while (state != Parse.STOP) {
      state = switch (state) {
        case Parse.START -> parseStart();

        default -> throw new UnsupportedOperationException(
          "Implement me :: state=" + state
        );
      };
    }

    stackAssert(PARSE);

    if (hasNextNode()) {
      stackReplace(COMPUTED);
    } else {
      stackReplace(EXHAUSTED);
    }
  }

  private int parseStart() {
    if (!sourceMore()) {
      return Parse.STOP;
    }

    throw new UnsupportedOperationException("Implement me");
  }

}
