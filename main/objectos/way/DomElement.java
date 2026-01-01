/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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
package objectos.way;

import java.util.Iterator;

final class DomElement implements Dom.Element, Lang.IterableOnce<Dom.Node>, Iterator<Dom.Node> {

  private class ThisAttributes implements Lang.IterableOnce<Dom.Attribute>, Iterator<Dom.Attribute> {

    @Override
    public final boolean hasNext() {
      return player.elementAttributesHasNext(name);
    }

    @Override
    public final Iterator<Dom.Attribute> iterator() {
      player.elementAttributesIterator();

      return this;
    }

    @Override
    public final Dom.Attribute next() {
      return player.elementAttributesNext();
    }

  }

  private ThisAttributes attributes;

  private final HtmlMarkupOfHtml player;

  HtmlElementName name;

  DomElement(HtmlMarkupOfHtml player) {
    this.player = player;
  }

  @Override
  public final Lang.IterableOnce<Dom.Attribute> attributes() {
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
  public final Iterator<Dom.Node> iterator() {
    player.elementNodesIterator();

    return this;
  }

  @Override
  public final String name() {
    return name.name();
  }

  @Override
  public final Dom.Node next() {
    return player.elementNodesNext();
  }

  @Override
  public final Lang.IterableOnce<Dom.Node> nodes() {
    player.elementNodes();

    return this;
  }

  @Override
  public final boolean voidElement() {
    return !name.endTag();
  }

}