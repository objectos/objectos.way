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

import objectos.script.JsAction;

final class DomAttribute implements Dom.Attribute {

  HtmlAttributeName name;

  private final HtmlMarkupOfHtml player;

  Object value;

  DomAttribute(HtmlMarkupOfHtml player) {
    this.player = player;
  }

  @Override
  public final String name() {
    return name.name();
  }

  @Override
  public final boolean booleanAttribute() {
    return value == null;
  }

  @Override
  public final boolean singleQuoted() {
    return value instanceof JsAction;
  }

  @Override
  public final String value() {
    player.attributeValues();

    player.attributeValuesIterator();

    if (!hasNext()) {
      return "";
    }

    Object result;
    result = next();

    if (result instanceof JsAction) {

      final StringBuilder value;
      value = new StringBuilder();

      value.append("way.on(event,");

      value.append(result);

      value.append(')');

      while (hasNext()) {
        throw new UnsupportedOperationException("Implement me");
      }

      return value.toString();

    } else {

      if (!hasNext()) {
        return String.valueOf(result);
      }

      StringBuilder value;
      value = new StringBuilder();

      value.append(result);

      value.append(' ');

      value.append(next());

      while (hasNext()) {
        value.append(' ');

        value.append(next());
      }

      return value.toString();

    }

  }

  private boolean hasNext() {
    return player.attributeValuesHasNext();
  }

  private Object next() {
    Object result;
    result = player.attributeValuesNext(value);

    value = null;

    return result;
  }

}