/*
 * Copyright (C) 2015-2023 Objectos Software LTDA.
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
package objectos.html;

import java.util.Iterator;
import objectos.html.pseudom.HtmlDocument;
import objectos.html.pseudom.HtmlNode;
import objectos.lang.IterableOnce;

final class PseudoHtmlDocument
    implements HtmlDocument, IterableOnce<HtmlNode>, Iterator<HtmlNode> {

  private final Html player;

  public PseudoHtmlDocument(Html ctx) {
    this.player = ctx;
  }

  @Override
  public final IterableOnce<HtmlNode> nodes() {
    player.documentIterable();

    return this;
  }

  @Override
  public final Iterator<HtmlNode> iterator() {
    player.documentIterator();

    return this;
  }

  @Override
  public final boolean hasNext() {
    return player.documentHasNext();
  }

  @Override
  public final HtmlNode next() {
    return player.documentNext();
  }

}