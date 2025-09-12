/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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

final class DomDocument implements Dom.Document, Lang.IterableOnce<Dom.Node>, Iterator<Dom.Node> {

  enum Type implements Dom.Document.Type {
    INSTANCE;

    @Override
    public final String value() {
      return "<!DOCTYPE html>";
    }
  }

  private final HtmlMarkupOfHtml player;

  public DomDocument(HtmlMarkupOfHtml ctx) {
    this.player = ctx;
  }

  @Override
  public final Lang.IterableOnce<Dom.Node> nodes() {
    player.documentIterable();

    return this;
  }

  @Override
  public final Iterator<Dom.Node> iterator() {
    player.documentIterator();

    return this;
  }

  @Override
  public final boolean hasNext() {
    return player.documentHasNext();
  }

  @Override
  public final Dom.Node next() {
    return player.documentNext();
  }

}