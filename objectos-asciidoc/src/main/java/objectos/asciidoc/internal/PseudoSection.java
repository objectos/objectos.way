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
import objectos.asciidoc.pseudom.Section;

public final class PseudoSection extends PseudoNode
    implements Section, IterableOnce<Node>, Iterator<Node> {

  private enum Parse {
    STOP,
    SECTION_BODY,
    SECTION_BODY_TRIM,

    PARAGRAPH,

    EXHAUSTED;
  }

  private static final int NODES = -700;
  private static final int ITERATOR = -701;
  private static final int TITLE = -702;
  static final int TITLE_CONSUMED = -703;
  private static final int PARSE = -704;
  private static final int PARAGRAPH = -705;
  static final int PARAGRAPH_CONSUMED = -706;
  static final int EXHAUSTED = -707;

  int level;

  PseudoSection(InternalSink sink) {
    super(sink);
  }

  @Override
  public final boolean hasNext() {
    switch (stackPeek()) {
      case PseudoHeading.EXHAUSTED -> parse(Parse.SECTION_BODY);

      case PseudoParagraph.EXHAUSTED -> parse(Parse.SECTION_BODY);

      case ITERATOR -> {
        stackPop();

        // stores this section level
        stackPush(level);

        var heading = heading();

        heading.level = level;

        nextNode(heading);

        stackPush(TITLE);
      }

      case PARAGRAPH, TITLE -> {}

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
  public final int level() {
    return level;
  }

  @Override
  public final Node next() {
    return nextNode();
  }

  @Override
  public final IterableOnce<Node> nodes() {
    stackAssert(PseudoDocument.SECTION_CONSUMED);

    stackReplace(NODES);

    return this;
  }

  private void parse(Parse initialState) {
    stackPop(); // previous state

    @SuppressWarnings("unused")
    int level = stackPeek();

    stackPush(PARSE);

    var state = initialState;

    while (state != Parse.STOP) {
      state = switch (state) {
        case EXHAUSTED -> parseExhausted();

        case PARAGRAPH -> parseParagraph();

        case SECTION_BODY -> parseSectionBody();

        case SECTION_BODY_TRIM -> parseSectionBodyTrim();

        default -> throw new UnsupportedOperationException(
          "Implement me :: state=" + state
        );
      };
    }
  }

  private Parse parseExhausted() {
    // pop section level
    stackPop();

    stackReplace(EXHAUSTED);

    return Parse.STOP;
  }

  private Parse parseParagraph() {
    stackReplace(PARAGRAPH);

    nextNode(paragraph());

    return Parse.STOP;
  }

  private Parse parseSectionBody() {
    if (!sourceMore()) {
      return Parse.EXHAUSTED;
    }

    return switch (sourcePeek()) {
      case '\n' -> advance(Parse.SECTION_BODY_TRIM);

      case '=' -> throw new UnsupportedOperationException("Implement me");

      default -> Parse.PARAGRAPH;
    };
  }

  private Parse parseSectionBodyTrim() {
    if (!sourceMore()) {
      return Parse.EXHAUSTED;
    }

    return switch (sourcePeek()) {
      case '\n' -> advance(Parse.SECTION_BODY_TRIM);

      default -> Parse.SECTION_BODY;
    };
  }

}