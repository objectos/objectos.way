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
import objectos.asciidoc.pseudom.IterableOnce;
import objectos.asciidoc.pseudom.Node;
import objectos.asciidoc.pseudom.Node.UnorderedList;

public final class PseudoUnorderedList extends PseudoNode
    implements UnorderedList, IterableOnce<Node>, Iterator<Node> {

  static final int NODES = -800;
  static final int ITERATOR = -801;
  static final int ITEM = -802;
  static final int ITEM_CONSUMED = -803;
  static final int EXHAUSTED = -804;

  PseudoUnorderedList(InternalSink sink) {
    super(sink);
  }

  @Override
  public final boolean hasNext() {
    return sink.unorderedListHasNext();
  }

  @Override
  public final Iterator<Node> iterator() {
    sink.unorderedListIterator();

    return this;
  }

  @Override
  public final Node next() {
    return nextNodeDefault();
  }

  @Override
  public final IterableOnce<Node> nodes() {
    sink.unorderedListNodes();

    return this;
  }

}