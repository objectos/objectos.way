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
import objectos.asciidoc.pseudom.Attributes;
import objectos.asciidoc.pseudom.IterableOnce;
import objectos.asciidoc.pseudom.Node;
import objectos.asciidoc.pseudom.Node.ListingBlock;

public final class PseudoListingBlock extends PseudoNode
    implements ListingBlock, IterableOnce<Node>, Iterator<Node> {

  static final int NODES = -900;
  static final int ITERATOR = -901;

  int markerSize;

  PseudoListingBlock(InternalSink sink) {
    super(sink);
  }

  @Override
  public final Attributes attributes() {
    var attributes = sink.attributes();

    return attributes.bindIfNecessary(this);
  }

  @Override
  public final boolean hasNext() {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public final Iterator<Node> iterator() {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public final Node next() {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public final IterableOnce<Node> nodes() {
    sink.listingBlockNodes();

    return this;
  }

}
