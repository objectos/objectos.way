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

  private static final int NODES = -700;
  private static final int ITERATOR = -701;
  private static final int TITLE = -702;
  static final int TITLE_CONSUMED = -703;
  private static final int PARSE = -704;
  private static final int PARAGRAPH = -705;
  static final int PARAGRAPH_CONSUMED = -706;
  private static final int SECTION = -706;
  static final int SECTION_CONSUMED = -707;
  static final int EXHAUSTED = -708;

  int level;

  PseudoSection(InternalSink sink) {
    super(sink);
  }

  @Override
  public final boolean hasNext() {
    switch (stackPeek()) {
      case PseudoHeading.EXHAUSTED,
           PseudoParagraph.EXHAUSTED,
           PseudoSection.EXHAUSTED -> parse(Parse.BODY);

      case ITERATOR -> {
        stackPop();

        // stores this section level
        stackPush(level);

        var heading = heading();

        heading.level = level;

        nextNode(heading);

        stackPush(TITLE);
      }

      case PARAGRAPH, SECTION, TITLE -> {}

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
    switch (stackPeek()) {
      case PseudoDocument.SECTION_CONSUMED,
           PseudoSection.SECTION_CONSUMED -> stackReplace(NODES);

      default -> stackStub();
    }

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
        case BODY -> parseBody();

        case BODY_TRIM -> parseBodyTrim();

        case EXHAUSTED -> parseExhausted();

        case MAYBE_SECTION -> parseMaybeSection();

        case MAYBE_SECTION_TRIM -> parseMaybeSectionTrim();

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

    // pops ASSERT
    stackPop();

    // replaces section level
    stackReplace(EXHAUSTED);

    return Parse.STOP;
  }

  private Parse parseParagraph() {
    stackReplace(PARAGRAPH);

    nextNode(paragraph());

    return Parse.STOP;
  }

  private Parse parseSection() {
    int nextLevel = stackPop();

    // pops source index
    stackPop();

    stackAssert(PARSE);

    stackPop();

    int thisLevel = stackPeek();

    if (nextLevel > thisLevel) {
      stackPush(SECTION);

      var section = section();

      section.level = nextLevel;

      nextNode(section);

      return Parse.STOP;
    } else {
      throw new UnsupportedOperationException("Implement me");
    }
  }

}