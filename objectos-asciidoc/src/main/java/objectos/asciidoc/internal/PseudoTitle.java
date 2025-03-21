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
import objectos.asciidoc.pseudom.Node.Title;

public final class PseudoTitle extends PseudoNode
    implements Title, IterableOnce<Node>, Iterator<Node> {

  static final int NODES = -300;
  static final int ITERATOR = -301;
  static final int PARSE = -302;
  static final int NODE = -303;
  static final int NODE_CONSUMED = -304;
  static final int EXHAUSTED = -307;

  int level;

  PseudoTitle(InternalSink sink) {
    super(sink);
  }

  @Override
  public final boolean hasNext() {
    return sink.titleHasNext();
  }

  @Override
  public final Iterator<Node> iterator() {
    sink.titleIterator();

    return this;
  }

  @Override
  public final int level() {
    return level;
  }

  @Override
  public final Node next() {
    return nextNodeDefault();
  }

  @Override
  public final IterableOnce<Node> nodes() {
    sink.titleNodes();

    return this;
  }

}
