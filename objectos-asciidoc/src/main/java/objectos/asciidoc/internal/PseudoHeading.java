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
  private static final int COMPUTED = -303;
  static final int CONSUMED = -304;

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

        stackReplace(COMPUTED);
      }

      case COMPUTED -> {}

      case CONSUMED -> stackPop();

      default -> stackStub();
    }

    return hasNextText();
  }

  @Override
  public final Node next() {
    return nextText();
  }

  @Override
  public final Iterator<Node> iterator() {
    stackAssert(NODES);

    stackReplace(ITERATOR);

    return this;
  }

  @Override
  public final IterableOnce<Node> nodes() {
    stackAssert(PseudoHeader.HEADING_CONSUMED);

    stackPush(NODES);

    return this;
  }

}
