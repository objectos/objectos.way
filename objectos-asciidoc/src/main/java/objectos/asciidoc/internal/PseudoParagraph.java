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
import objectos.asciidoc.pseudom.Paragraph;

public final class PseudoParagraph extends PseudoNode
    implements Paragraph, IterableOnce<Node>, Iterator<Node> {

  private static final int NODES = -600;
  private static final int ITERATOR = -601;
  private static final int PARSE = -602;
  private static final int COMPUTED = -603;
  static final int EXHAUSTED = -604;

  PseudoParagraph(InternalSink sink) {
    super(sink);
  }

  @Override
  public final boolean hasNext() {
    return switch (stackPeek()) {
      case ITERATOR -> {
        stackReplace(PARSE);

        parseText(ParseTxt.START, false);

        stackReplace(COMPUTED);

        yield hasNextText();
      }

      case COMPUTED -> hasNextText();

      case EXHAUSTED -> false;

      default -> stackStubBool();
    };
  }

  @Override
  public final Iterator<Node> iterator() {
    stackAssert(NODES);

    stackReplace(ITERATOR);

    return this;
  }

  @Override
  public final Node next() {
    return nextText();
  }

  @Override
  public final IterableOnce<Node> nodes() {
    stackAssert(PseudoDocument.PARAGRAPH_CONSUMED);

    stackReplace(NODES);

    return this;
  }

}