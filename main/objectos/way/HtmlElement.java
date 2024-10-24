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
package objectos.way;

import java.util.Iterator;

final class HtmlElement implements Html.Element, Lang.IterableOnce<Html.Node>, Iterator<Html.Node> {

  private class ThisAttributes implements Lang.IterableOnce<Html.Attribute>, Iterator<Html.Attribute> {

    @Override
    public final boolean hasNext() {
      return player.elementAttributesHasNext(name);
    }

    @Override
    public final Iterator<Html.Attribute> iterator() {
      player.elementAttributesIterator();

      return this;
    }

    @Override
    public final Html.Attribute next() {
      return player.elementAttributesNext();
    }

  }

  private ThisAttributes attributes;

  private final HtmlCompiler player;

  HtmlElementName name;

  HtmlElement(HtmlCompiler player) {
    this.player = player;
  }

  @Override
  public final Lang.IterableOnce<Html.Attribute> attributes() {
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
    return !name.endTag();
  }

  @Override
  public final Iterator<Html.Node> iterator() {
    player.elementNodesIterator();

    return this;
  }

  @Override
  public final String name() {
    return name.name();
  }

  @Override
  public final String testField() {
    return player.elementTestField();
  }

  @Override
  public final Html.Node next() {
    return player.elementNodesNext();
  }

  @Override
  public final Lang.IterableOnce<Html.Node> nodes() {
    player.elementNodes();

    return this;
  }

}