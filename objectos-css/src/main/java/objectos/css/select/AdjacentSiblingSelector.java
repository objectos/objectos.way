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
package objectos.css.select;

import objectos.util.UnmodifiableList;
import objectos.css.sheet.RuleElement;
import objectos.util.GrowableList;

public class AdjacentSiblingSelector extends Selector {

  private final UnmodifiableList<Selector> selectors;

  AdjacentSiblingSelector(UnmodifiableList<Selector> selectors) {
    this.selectors = selectors;
  }

  public static AdjacentSiblingSelector ofParser(
      ComplexSelectorHead head, ComplexSelectorTail tail) {
    Selector h = checkIsSelector(head, "head");
    Selector t = checkIsSelector(tail, "tail");
    return t.newAdjacentSiblingSelectorWithPrevious(h);
  }

  @Override
  public final void acceptRuleElementList(GrowableList<RuleElement> elements) {
    acceptRuleElementListImpl(elements, selectors, Combinator.ADJACENT_SIBLING);
  }

  @Override
  public final <R, P> R acceptSelectorVisitor(SelectorVisitor<R, P> visitor, P p) {
    return visitor.visitAdjacentSiblingSelector(this, p);
  }

  @Override
  public final boolean equals(Object obj) {
    if (!(obj instanceof AdjacentSiblingSelector)) {
      return false;
    }
    AdjacentSiblingSelector that = (AdjacentSiblingSelector) obj;
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
    return selectors.join(" + ");
  }

  @Override
  final AdjacentSiblingSelector newAdjacentSiblingSelectorWithPrevious(Selector previous) {
    GrowableList<Selector> newSelectors = new GrowableList<>();
    newSelectors.add(previous);
    newSelectors.addAll(selectors);
    return new AdjacentSiblingSelector(newSelectors.toUnmodifiableList());
  }

}