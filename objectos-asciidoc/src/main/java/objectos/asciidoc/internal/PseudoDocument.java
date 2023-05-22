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
import java.util.LinkedHashMap;
import java.util.Map;
import objectos.asciidoc.pseudom.Document;
import objectos.asciidoc.pseudom.IterableOnce;
import objectos.asciidoc.pseudom.Node;

public final class PseudoDocument extends PseudoNode
    implements Document, IterableOnce<Node>, Iterator<Node> {

  static final int START = -100;
  static final int NODES = -101;
  static final int ITERATOR = -102;
  static final int TITLE = -103;
  static final int TITLE_CONSUMED = -104;
  static final int PARAGRAPH = -105;
  static final int PARAGRAPH_CONSUMED = -106;
  static final int SECTION = -107;
  static final int SECTION_CONSUMED = -108;
  static final int BLOCK = -109;
  static final int BLOCK_CONSUMED = -110;
  static final int EXHAUSTED = -111;

  private Map<String, String> attributes;

  PseudoDocument(InternalSink sink) {
    super(sink);
  }

  @Override
  public final void close() {
    sink.close();
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

  final PseudoDocument clear() {
    if (attributes != null) {
      attributes.clear();
    }

    return this;
  }

  final void putAttribute(String name, String value) {
    if (attributes == null) {
      attributes = new LinkedHashMap<>();
    }

    attributes.put(name, value);
  }

  final String getAttribute(String name) {
    if (attributes == null) {
      return null;
    } else {
      return attributes.get(name);
    }
  }

}