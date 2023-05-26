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
package br.com.objectos.css.select;

import br.com.objectos.css.sheet.RuleElement;
import objectos.util.GrowableList;

public final class UniversalSelector extends Selector
    implements
    SelectorElement,
    SelectorListHead,
    RuleElement {

  private static final UniversalSelector INSTANCE = new UniversalSelector();

  private UniversalSelector() {}

  public static UniversalSelector getInstance() {
    return INSTANCE;
  }

  @Override
  public final void acceptRuleElementList(GrowableList<RuleElement> elements) {
    elements.add(this);
  }

  @Override
  public final void acceptSelectorBuilderDsl(Selector.BuilderDsl builder) {
    builder.addUniversalSelector(this);
  }

  @Override
  public final <R, P> R acceptSelectorVisitor(SelectorVisitor<R, P> visitor, P p) {
    return visitor.visitUniversalSelector(this, p);
  }

  @Override
  public final boolean equals(Object obj) {
    return obj == INSTANCE;
  }

  @Override
  public final int hashCode() {
    return 1;
  }

  @Override
  public final boolean matches(Selectable element) {
    return true;
  }

  @Override
  public final String toString() {
    return "*";
  }

  @Override
  final void uncheckedAddTypeSelector(TypeSelector selector) {
    throw InvalidSelectorException.get(
      "Cannot append type selector '%s' to the universal selector '*'", selector
    );
  }

}
