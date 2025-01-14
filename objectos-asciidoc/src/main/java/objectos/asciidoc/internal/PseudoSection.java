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
import objectos.asciidoc.pseudom.Attributes;
import objectos.asciidoc.pseudom.IterableOnce;
import objectos.asciidoc.pseudom.Node;
import objectos.asciidoc.pseudom.Node.Section;

public final class PseudoSection extends PseudoNode
    implements Section, IterableOnce<Node>, Iterator<Node> {

  static final int NODES = -700;
  static final int ITERATOR = -701;
  static final int TITLE = -702;
  static final int TITLE_CONSUMED = -703;
  static final int BLOCK = -704;
  static final int BLOCK_CONSUMED = -705;
  static final int SECTION = -706;
  static final int SECTION_CONSUMED = -707;
  static final int EXHAUSTED = -708;

  int level;

  PseudoSection(InternalSink sink) {
    super(sink);
  }

  @Override
  public final Attributes attributes() {
    var attributes = sink.attributes();

    return attributes.bindIfNecessary(this);
  }

  @Override
  public final boolean hasNext() {
    return sink.sectionHasNext();
  }

  @Override
  public final Iterator<Node> iterator() {
    sink.sectionIterator();

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
    sink.sectionNodes();

    return this;
  }

}