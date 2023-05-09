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
import objectos.asciidoc.pseudom.Node.Strong;

public final class PseudoStrong extends PseudoNode
    implements Strong, IterableOnce<Node>, Iterator<Node> {

  static final int NODES = -1200;
  static final int ITERATOR = -1201;
  static final int NODE = -1202;
  static final int NODE_CONSUMED = -1203;
  static final int EXHAUSTED = -1204;

  int textStart;

  int textEnd;

  PseudoStrong(InternalSink sink) {
    super(sink);
  }

  @Override
  public final boolean hasNext() {
    return sink.strongHasNext();
  }

  @Override
  public final Iterator<Node> iterator() {
    sink.strongIterator();

    return this;
  }

  @Override
  public final Node next() {
    return nextNodeDefault();
  }

  @Override
  public final IterableOnce<Node> nodes() {
    sink.strongNodes();

    return this;
  }

}