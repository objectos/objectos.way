/*
 * Copyright (C) 2021-2025 Objectos Software LTDA.
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
import objectos.asciidoc.pseudom.IterableOnce;
import objectos.asciidoc.pseudom.Node;
import objectos.asciidoc.pseudom.Node.ListItem;

public final class PseudoListItem extends PseudoNode
    implements ListItem, IterableOnce<Node>, Iterator<Node> {

  static final int NODES = -1000;
  static final int ITERATOR = -1001;
  static final int TEXT = -1002;
  static final int TEXT_CONSUMED = -1003;
  static final int BLOCK = -1004;
  static final int BLOCK_CONSUMED = -1005;
  static final int EXHAUSTED = -1006;

  int textStart;

  int textEnd;

  PseudoListItem(InternalSink sink) {
    super(sink);
  }

  @Override
  public final boolean hasNext() {
    return sink.listItemHasNext();
  }

  @Override
  public final Iterator<Node> iterator() {
    sink.listItemIterator();

    return this;
  }

  @Override
  public final Node next() {
    if (hasNext()) {
      return nextNodeSink();
    } else {
      throw new NoSuchElementException();
    }
  }

  @Override
  public final IterableOnce<Node> nodes() {
    sink.listItemNodes();

    return this;
  }

}