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

  private static final int START = -100;
  private static final int NODES = -101;
  private static final int ITERATOR = -102;
  private static final int PARSE = -103;
  private static final int HEADER = -104;
  static final int HEADER_CONSUMED = -105;
  private static final int PARAGRAPH = -110;
  static final int PARAGRAPH_CONSUMED = -111;
  private static final int SECTION = -112;
  static final int SECTION_CONSUMED = -113;
  static final int EXHAUSTED = -114;

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
      case PseudoHeader.EXHAUSTED,
           PseudoParagraph.EXHAUSTED,
           PseudoSection.EXHAUSTED -> parse(Parse.BODY);

      case ITERATOR -> parse(Parse.DOCUMENT_START);

      case HEADER, PARAGRAPH, SECTION -> {}

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

  private void parse(Parse initialState) {
    stackReplace(PARSE);

    var state = initialState;

    while (state != Parse.STOP) {
      state = switch (state) {
        case BODY -> parseBody();

        case DOCUMENT_START -> parseDocumentStart();

        case EXHAUSTED -> parseExhausted();

        case HEADER -> parseHeader();

        case MAYBE_HEADER -> parseMaybeHeader();

        case MAYBE_HEADER_TRIM -> parseMaybeHeaderTrim();

        case MAYBE_SECTION -> parseMaybeSection();

        case MAYBE_SECTION_TRIM -> parseMaybeSectionTrim();

        case NOT_HEADER -> parseNotHeader();

        case PARAGRAPH -> parseParagraph();

        case SECTION -> parseSection();

        default -> throw new UnsupportedOperationException(
          "Implement me :: state=" + state
        );
      };
    }
  }

  private Parse parseExhausted() {
    stackAssert(PARSE);

    stackReplace(EXHAUSTED);

    return Parse.STOP;
  }

  private Parse parseDocumentStart() {
    if (!sourceMore()) {
      return Parse.STOP;
    }

    stackPush(sourceIndex());

    return switch (sourcePeek()) {
      case '=' -> advance(Parse.MAYBE_HEADER);

      default -> Parse.NOT_HEADER;
    };
  }

  private Parse parseHeader() {
    // pops source index
    stackPop();

    stackReplace(HEADER);

    nextNode(header());

    return Parse.STOP;
  }

  private Parse parseMaybeHeader() {
    if (!sourceMore()) {
      return Parse.NOT_HEADER;
    }

    return switch (sourcePeek()) {
      case '\t', '\f', ' ' -> advance(Parse.MAYBE_HEADER_TRIM);

      default -> Parse.NOT_HEADER;
    };
  }

  private Parse parseMaybeHeaderTrim() {
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

  private Parse parseNotHeader() {
    sourceIndex(stackPop());

    return switch (sourcePeek()) {
      case '*' -> throw new UnsupportedOperationException(
        "Implement me :: maybe unordered list"
      );

      default -> Parse.PARAGRAPH;
    };
  }

  private Parse parseParagraph() {
    stackPush(PARAGRAPH);

    nextNode(paragraph());

    return Parse.STOP;
  }

  private Parse parseSection() {
    int level = stackPop();

    // pops source index
    stackPop();

    stackPush(SECTION);

    var section = section();

    section.level = level;

    nextNode(section);

    return Parse.STOP;
  }

}