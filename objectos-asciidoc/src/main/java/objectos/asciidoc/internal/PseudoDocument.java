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
    int PARAGRAPH = 7;
  }

  private static final int START = -100;
  private static final int NODES = -101;
  private static final int ITERATOR = -102;
  private static final int DOCUMENT_START = -103;
  private static final int HEADING = -104;
  static final int HEADING_CONSUMED = -105;
  private static final int DOCUMENT_MAYBE_PREAMBLE = -106;
  private static final int NO_HEADER = -107;
  static final int NO_HEADER_CONSUMED = -108;
  private static final int DOCUMENT_BODY = -109;
  private static final int PARAGRAPH = -110;
  static final int PARAGRAPH_CONSUMED = -111;

  PseudoDocument(InternalSink sink) {
    super(sink);
  }

  @Override
  public final void close() throws IOException {
    closeImpl();
  }

  @Override
  public final boolean hasNext() {
    switch (stackPeek()) {
      case PseudoHeader.EXHAUSTED -> parse(DOCUMENT_MAYBE_PREAMBLE);

      case PseudoNoHeader.EXHAUSTED,
           PseudoParagraph.EXHAUSTED -> parse(DOCUMENT_BODY);

      case ITERATOR -> parse(DOCUMENT_START);

      case HEADING, NO_HEADER, PARAGRAPH -> {}

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
    stackAssert(START);

    stackReplace(NODES);

    return this;
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

        case Parse.NOT_HEADING -> parseNotHeading();

        case Parse.PARAGRAPH -> parseParagraph();

        case Parse.START -> parseStart();

        default -> throw new UnsupportedOperationException(
          "Implement me :: state=" + state
        );
      };
    }
  }

  private void parse(int nextState) {
    stackReplace(nextState);

    parse();
  }

  private int parseHeading() {
    int level = stackPop();

    // pops source index
    stackPop();

    int top = stackPop();

    switch (top) {
      case DOCUMENT_START -> {}

      default -> throw new AssertionError(
        "Stack top must not be top=" + top
      );
    }

    stackPush(level, HEADING);

    nextNode(header());

    return Parse.STOP;
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

  private int parseNotHeading() {
    // pops heading level
    stackPop();

    sourceIndex(stackPop());

    return switch (stackPeek()) {
      case DOCUMENT_BODY -> {
        stackReplace(PARAGRAPH);

        nextNode(paragraph());

        yield Parse.STOP;
      }

      case DOCUMENT_START -> {
        stackReplace(NO_HEADER);

        nextNode(noHeader());

        yield Parse.STOP;
      }

      default -> stackStub();
    };
  }

  private int parseParagraph() {
    stackPush(PARAGRAPH);

    nextNode(paragraph());

    return Parse.STOP;
  }

  private int parseStart() {
    if (!sourceMore()) {
      // empty document...
      // just stop

      return Parse.STOP;
    }

    return switch (sourcePeek()) {
      case '=' -> {
        stackPush(sourceIndex());

        // pushes heading level
        stackPush(0);

        yield advance(Parse.MAYBE_HEADING);
      }

      default -> {
        // pseudo state for NOT_HEADING
        stackPush(sourceIndex());
        stackPush(0);

        yield Parse.NOT_HEADING;
      }
    };
  }

}