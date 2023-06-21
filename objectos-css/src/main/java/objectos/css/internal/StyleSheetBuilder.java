/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
package objectos.css.internal;

import objectos.css.SelectorElement;
import objectos.css.om.Selector;
import objectos.css.om.StyleDeclaration;
import objectos.css.om.StyleSheet;
import objectos.util.GrowableList;

public class StyleSheetBuilder {

  private final GrowableList<Object> rules = new GrowableList<>();

  private final StringBuilder block = new StringBuilder();

  private final StringBuilder selector = new StringBuilder();

  public final void addSelector(Selector s) {
    if (selector.length() > 0) {
      selector.append(", ");
    }

    selector.append(s);
  }

  public final void addSelectorElement(SelectorElement element) {
    selector.append(element);
  }

  public final void addStyleDeclaration(StyleDeclaration declaration) {
    block.append(System.lineSeparator());
    block.append("  ");
    block.append(declaration);
    block.append(';');
  }

  public final void beginSelector() {
    selector.setLength(0);
  }

  public final void beginStyleRule() {
    block.setLength(0);

    block.append(" {");

    selector.setLength(0);
  }

  public final StyleSheet build() {
    return new StyleSheetImpl(
      rules.toUnmodifiableList()
    );
  }

  public final Selector buildSelector() {
    return new NamedElement(
      selector.toString()
    );
  }

  public final void buildStyleRule() {
    if (block.length() != 2) {
      block.append(System.lineSeparator());
    }

    block.append('}');

    var rule = new StyleRule(selector.toString(), block.toString());

    rules.add(rule);
  }

}