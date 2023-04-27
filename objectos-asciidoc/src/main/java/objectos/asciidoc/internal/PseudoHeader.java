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
import objectos.asciidoc.pseudom.Node.Header;

public final class PseudoHeader extends PseudoNode
    implements Header, IterableOnce<Node>, Iterator<Node> {

  static final int NODES = -200;
  static final int ITERATOR = -201;
  static final int TITLE = -202;
  static final int TITLE_CONSUMED = -203;
  static final int PARSE = -204;
  static final int EXHAUSTED = -205;

  PseudoHeader(InternalSink sink) {
    super(sink);
  }

  @Override
  public final boolean hasNext() {
    return sink.headerHasNext();
  }

  @Override
  public final Iterator<Node> iterator() {
    sink.headerIterator();

    return this;
  }

  @Override
  public final Node next() {
    return nextNodeDefault();
  }

  @Override
  public final IterableOnce<Node> nodes() {
    sink.headerNodes();

    return this;
  }

}
