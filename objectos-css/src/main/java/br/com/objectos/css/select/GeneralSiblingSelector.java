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
import objectos.util.UnmodifiableList;
import objectos.util.GrowableList;

public class GeneralSiblingSelector extends Selector implements ComplexSelector {

  private final UnmodifiableList<Selector> selectors;

  GeneralSiblingSelector(UnmodifiableList<Selector> selectors) {
    this.selectors = selectors;
  }

  public static GeneralSiblingSelector ofParser(
      ComplexSelectorHead head, ComplexSelectorTail tail) {
    Selector h = checkIsSelector(head, "head");
    Selector t = checkIsSelector(tail, "tail");
    return t.newGeneralSiblingSelectorWithPrevious(h);
  }

  @Override
  public final void acceptRuleElementList(GrowableList<RuleElement> elements) {
    acceptRuleElementListImpl(elements, selectors, Combinator.GENERAL_SIBLING);
  }

  @Override
  public final <R, P> R acceptSelectorVisitor(SelectorVisitor<R, P> visitor, P p) {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public final boolean equals(Object obj) {
    if (!(obj instanceof GeneralSiblingSelector)) {
      return false;
    }
    GeneralSiblingSelector that = (GeneralSiblingSelector) obj;
    return selectors.equals(that.selectors);
  }

  @Override
  public final int hashCode() {
    return selectors.hashCode();
  }

  @Override
  public final boolean matches(Selectable element) {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public final String toString() {
    return selectors.join(" ~ ");
  }

  @Override
  final GeneralSiblingSelector newGeneralSiblingSelectorWithPrevious(Selector previous) {
    GrowableList<Selector> newSelectors = new GrowableList<>();
    newSelectors.add(previous);
    newSelectors.addAll(selectors);
    return new GeneralSiblingSelector(newSelectors.toUnmodifiableList());
  }

}