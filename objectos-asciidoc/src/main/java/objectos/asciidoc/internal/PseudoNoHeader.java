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
import java.util.NoSuchElementException;
import objectos.asciidoc.pseudom.Header;
import objectos.asciidoc.pseudom.IterableOnce;
import objectos.asciidoc.pseudom.Node;

public final class PseudoNoHeader extends PseudoNode
    implements Header, IterableOnce<Node>, Iterator<Node> {

  private static final int NODES = -500;
  private static final int ITERATOR = -501;
  static final int EXHAUSTED = -502;

  PseudoNoHeader(InternalSink sink) {
    super(sink);
  }

  @Override
  public final boolean hasNext() {
    switch (stackPeek()) {
      case ITERATOR -> stackReplace(EXHAUSTED);

      case EXHAUSTED -> {}

      default -> stackStub();
    }

    return false;
  }

  @Override
  public final Iterator<Node> iterator() {
    stackAssert(NODES);

    stackReplace(ITERATOR);

    return this;
  }

  @Override
  public final Node next() {
    throw new NoSuchElementException();
  }

  @Override
  public final IterableOnce<Node> nodes() {
    stackAssert(PseudoDocument.NO_HEADER_CONSUMED);

    stackReplace(NODES);

    return this;
  }

}