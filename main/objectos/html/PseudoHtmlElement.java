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
import objectos.html.pseudom.HtmlAttribute;
import objectos.html.pseudom.HtmlElement;
import objectos.html.pseudom.HtmlNode;
import objectos.lang.IterableOnce;

final class PseudoHtmlElement
    implements HtmlElement, IterableOnce<HtmlNode>, Iterator<HtmlNode> {

  private class ThisAttributes implements IterableOnce<HtmlAttribute>, Iterator<HtmlAttribute> {

    @Override
    public final boolean hasNext() {
      return player.elementAttributesHasNext(name);
    }

    @Override
    public final Iterator<HtmlAttribute> iterator() {
      player.elementAttributesIterator();

      return this;
    }

    @Override
    public final HtmlAttribute next() {
      return player.elementAttributesNext();
    }

  }

  private ThisAttributes attributes;

  private final Html player;

  StandardElementName name;

  PseudoHtmlElement(Html player) {
    this.player = player;
  }

  @Override
  public final IterableOnce<HtmlAttribute> attributes() {
    player.elementAttributes();

    if (attributes == null) {
      attributes = new ThisAttributes();
    }

    return attributes;
  }

  @Override
  public final boolean hasNext() {
    return player.elementNodesHasNext();
  }

  @Override
  public final boolean isVoid() {
    return name.getKind() == ElementKind.VOID;
  }

  @Override
  public final Iterator<HtmlNode> iterator() {
    player.elementNodesIterator();

    return this;
  }

  @Override
  public final String name() {
    return name.getName();
  }

  @Override
  public final HtmlNode next() {
    return player.elementNodesNext();
  }

  @Override
  public final IterableOnce<HtmlNode> nodes() {
    player.elementNodes();

    return this;
  }

}