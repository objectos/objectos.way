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
import objectos.asciidoc.pseudom.Node.Header;

public final class PseudoHeader extends PseudoNode
    implements Header, IterableOnce<Node>, Iterator<Node> {

  private enum Parse {
    STOP,
    AFTER_TITLE,
    HEADER_END,
    EXHAUSTED;
  }

  private static final int NODES = -200;
  private static final int ITERATOR = -201;
  private static final int TITLE = -202;
  static final int TITLE_CONSUMED = -203;
  private static final int PARSE = -204;
  static final int EXHAUSTED = -205;

  PseudoHeader(InternalSink sink) {
    super(sink);
  }

  @Override
  public final boolean hasNext() {
    switch (stackPeek()) {
      case PseudoTitle.EXHAUSTED -> parse(Parse.AFTER_TITLE);

      case ITERATOR -> {
        stackPop();

        var heading = heading();

        heading.level = 0;

        nextNode(heading);

        stackPush(TITLE);
      }

      case TITLE -> {}

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
    stackAssert(PseudoDocument.HEADER_CONSUMED);

    stackReplace(NODES);

    return this;
  }

  private void parse(Parse initialState) {
    stackReplace(PARSE);

    var state = initialState;

    while (state != Parse.STOP) {
      state = switch (state) {
        case AFTER_TITLE -> parseAfterTitle();

        case EXHAUSTED -> parseExhausted();

        case HEADER_END -> parseHeaderEnd();

        default -> throw new UnsupportedOperationException(
          "Implement me :: state=" + state
        );
      };
    }
  }

  private Parse parseAfterTitle() {
    if (!sourceMore()) {
      return Parse.EXHAUSTED;
    }

    return switch (sourcePeek()) {
      case '\n' -> advance(Parse.HEADER_END);

      case ':' -> throw new UnsupportedOperationException("Implement me");

      default -> Parse.EXHAUSTED;
    };
  }

  private Parse parseExhausted() {
    stackAssert(PARSE);

    stackReplace(EXHAUSTED);

    return Parse.STOP;
  }

  private Parse parseHeaderEnd() {
    if (!sourceMore()) {
      return Parse.EXHAUSTED;
    }

    return switch (sourcePeek()) {
      case '\n' -> advance(Parse.HEADER_END);

      default -> Parse.EXHAUSTED;
    };
  }

}
