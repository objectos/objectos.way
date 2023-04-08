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
import objectos.asciidoc.pseudom.Document;
import objectos.asciidoc.pseudom.IterableOnce;
import objectos.asciidoc.pseudom.Node;

public final class PseudoDocument extends PseudoNode
    implements Document, IterableOnce<Node>, Iterator<Node> {

  private interface Parse {
    int START = 1;
    int STOP = 2;
    int HEADING = 3;
    int MAYBE_HEADING = 4;
    int MAYBE_HEADING_TRIM = 5;
    int NOT_HEADING = 6;
  }

  private static final int START = -100;
  private static final int NODES = -101;
  private static final int ITERATOR = -102;
  private static final int PARSE = -103;
  private static final int HEADING = -104;
  static final int HEADING_CONSUMED = -105;

  PseudoDocument(InternalSink sink) {
    super(sink);
  }

  @Override
  public final Iterator<Node> iterator() {
    stackAssert(NODES);

    stackReplace(ITERATOR);

    return this;
  }

  @Override
  public final IterableOnce<Node> nodes() {
    stackAssert(START);

    stackReplace(NODES);

    return this;
  }

  @Override
  public final boolean hasNext() {
    switch (stackPeek()) {
      case ITERATOR, PseudoHeader.EXHAUSTED -> {
        stackReplace(PARSE);

        parse();
      }

      case HEADING -> {}

      default -> stackStub();
    }

    return hasNextNode();
  }

  @Override
  public final Node next() {
    return nextNode();
  }

  final void start() {
    stackPush(START);
  }

  private void parse() {
    int state = Parse.START;

    while (state != Parse.STOP) {
      state = switch (state) {
        case Parse.HEADING -> parseHeading();

        case Parse.MAYBE_HEADING -> parseMaybeHeading();

        case Parse.MAYBE_HEADING_TRIM -> parseMaybeHeadingTrim();

        case Parse.START -> parseStart();

        default -> throw new UnsupportedOperationException(
          "Implement me :: state=" + state
        );
      };
    }
  }

  private int parseHeading() {
    int level = stackPop();

    int top = stackPop();

    assert top == PARSE;

    stackPush(level, HEADING);

    nextNode(header());

    return Parse.STOP;
  }

  private int parseMaybeHeadingTrim() {
    if (!sourceMore()) {
      return Parse.NOT_HEADING;
    }

    return switch (sourcePeek()) {
      case '\t', '\f', ' ' -> advance(Parse.MAYBE_HEADING_TRIM);

      case '\n', '\r' -> throw new UnsupportedOperationException(
        "Implement me :: preamble paragraph"
      );

      default -> Parse.HEADING;
    };
  }

  private int parseMaybeHeading() {
    if (!sourceMore()) {
      return Parse.NOT_HEADING;
    }

    return switch (sourcePeek()) {
      case '=' -> {
        // increase the heading level
        stackInc();

        yield advance(Parse.MAYBE_HEADING);
      }

      case '\t', '\f', ' ' -> advance(Parse.MAYBE_HEADING_TRIM);

      default -> Parse.NOT_HEADING;
    };
  }

  private int parseStart() {
    if (!sourceMore()) {
      // empty document...
      // just stop

      return Parse.STOP;
    }

    return switch (sourcePeek()) {
      case '=' -> {
        // pushes heading level
        stackPush(0);

        yield advance(Parse.MAYBE_HEADING);
      }

      default -> sourceStub();
    };
  }

}