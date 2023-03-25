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
package objectos.html.internal;

import java.util.Iterator;
import objectos.html.pseudom.HtmlAttribute;
import objectos.html.pseudom.HtmlIterable;
import objectos.html.tmpl.AttributeKind;
import objectos.html.tmpl.AttributeName;
import objectos.html.tmpl.StandardAttributeName;

public final class PseudoHtmlAttribute
    implements HtmlAttribute, HtmlIterable<String>, Iterator<String> {

  AttributeName name;

  private final HtmlPlayer player;

  PseudoHtmlAttribute(HtmlPlayer player) {
    this.player = player;
  }

  @Override
  public final boolean hasName(StandardAttributeName name) {
    return this.name == name;
  }

  @Override
  public final String name() {
    return name.getName();
  }

  @Override
  public final boolean isBoolean() {
    return name.getKind() == AttributeKind.BOOLEAN;
  }

  @Override
  public final HtmlIterable<String> values() {
    player.attributeValues();

    return this;
  }

  @Override
  public final boolean hasNext() {
    return player.attributeValuesHasNext();
  }

  @Override
  public final String next() {
    return player.attributeValuesNext(name);
  }

  @Override
  public final Iterator<String> iterator() {
    player.attributeValuesIterator();

    return this;
  }

}