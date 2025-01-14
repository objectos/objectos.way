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
import objectos.asciidoc.pseudom.Node.InlineMacro;

public final class PseudoInlineMacro extends PseudoNode
    implements InlineMacro, IterableOnce<Node>, Iterator<Node> {

  static final int MAX_LENGTH = 20;
  static final int NODES = -400;
  static final int ITERATOR = -401;
  static final int NODE = -402;
  static final int NODE_CONSUMED = -403;
  static final int EXHAUSTED = -404;

  String name;

  String target;

  int textStart;

  int textEnd;

  PseudoInlineMacro(InternalSink sink) {
    super(sink);
  }

  @Override
  public final boolean hasNext() {
    return sink.inlineMacroHasNext();
  }

  @Override
  public final Iterator<Node> iterator() {
    sink.inlineMacroIterator();

    return this;
  }

  @Override
  public final String name() {
    return name;
  }

  @Override
  public final Node next() {
    return nextNodeDefault();
  }

  @Override
  public final IterableOnce<Node> nodes() {
    sink.inlineMacroNodes();

    return this;
  }

  @Override
  public final String target() {
    return target;
  }

  final boolean hasText() {
    return textEnd - textStart > 0;
  }

}