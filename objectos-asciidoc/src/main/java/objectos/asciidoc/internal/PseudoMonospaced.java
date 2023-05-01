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
import objectos.asciidoc.pseudom.Node.Monospaced;

public final class PseudoMonospaced extends PseudoNode
    implements Monospaced, IterableOnce<Node>, Iterator<Node> {

  static final int NODES = -500;
  static final int ITERATOR = -501;
  static final int NODE = -502;
  static final int NODE_CONSUMED = -503;
  static final int EXHAUSTED = -504;

  int textStart;

  int textEnd;

  PseudoMonospaced(InternalSink sink) {
    super(sink);
  }

  @Override
  public final boolean hasNext() {
    return sink.monospacedHasNext();
  }

  @Override
  public final Iterator<Node> iterator() {
    sink.monospacedIterator();

    return this;
  }

  @Override
  public final Node next() {
    return nextNodeDefault();
  }

  @Override
  public final IterableOnce<Node> nodes() {
    sink.monospacedNodes();

    return this;
  }

}