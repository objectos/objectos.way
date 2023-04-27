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

import java.io.IOException;
import java.util.Iterator;
import objectos.asciidoc.pseudom.Document;
import objectos.asciidoc.pseudom.IterableOnce;
import objectos.asciidoc.pseudom.Node;

public final class PseudoDocument extends PseudoNode
    implements Document, IterableOnce<Node>, Iterator<Node> {

  static final int START = -100;
  static final int NODES = -101;
  static final int ITERATOR = -102;
  static final int PARSE = -103;
  static final int HEADER = -104;
  static final int HEADER_CONSUMED = -105;
  static final int PARAGRAPH = -110;
  static final int PARAGRAPH_CONSUMED = -111;
  static final int SECTION = -112;
  static final int SECTION_CONSUMED = -113;
  static final int ULIST = -114;
  static final int ULIST_CONSUMED = -115;
  static final int EXHAUSTED = -116;

  PseudoDocument(InternalSink sink) {
    super(sink);
  }

  @Override
  public final void close() throws IOException {
    closeImpl();
  }

  @Override
  public final boolean hasNext() {
    return sink.documentHasNext();
  }

  @Override
  public final Iterator<Node> iterator() {
    sink.documentIterator();

    return this;
  }

  @Override
  public final Node next() {
    return nextNodeDefault();
  }

  @Override
  public final IterableOnce<Node> nodes() {
    sink.documentNodes();

    return this;
  }

}