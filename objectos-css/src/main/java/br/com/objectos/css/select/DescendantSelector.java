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
import java.util.Iterator;
import java.util.Optional;
import objectos.util.UnmodifiableList;
import objectos.util.GrowableList;

public class DescendantSelector
    extends Selector
    implements
    ComplexSelector,
    Iterable<Selector> {

  private final UnmodifiableList<Selector> selectors;

  DescendantSelector(UnmodifiableList<Selector> selectors) {
    this.selectors = selectors;
  }

  public static DescendantSelector ofParser(ComplexSelectorHead head, ComplexSelectorTail tail) {
    Selector h = checkIsSelector(head, "head");
    Selector t = checkIsSelector(tail, "tail");
    return t.newDescendantSelectorWithAncestor(h);
  }

  @Override
  public final void acceptRuleElementList(GrowableList<RuleElement> elements) {
    acceptRuleElementListImpl(elements, selectors, Combinator.DESCENDANT);
  }

  @Override
  public final <R, P> R acceptSelectorVisitor(SelectorVisitor<R, P> visitor, P p) {
    return visitor.visitDescendantSelector(this, p);
  }

  @Override
  public final boolean equals(Object obj) {
    if (!(obj instanceof DescendantSelector)) {
      return false;
    }
    DescendantSelector that = (DescendantSelector) obj;
    return selectors.equals(that.selectors);
  }

  @Override
  public final int hashCode() {
    return selectors.hashCode();
  }

  @Override
  public final Iterator<Selector> iterator() {
    return selectors.iterator();
  }

  @Override
  public final boolean matches(Selectable element) {
    int index = selectors.size();

    Selector selector = selectors.get(--index);
    if (!selector.matches(element)) {
      return false;
    }

    while (--index >= 0) {
      boolean parentMatched = false;
      selector = selectors.get(index);
      Optional<? extends Selectable> maybeParent = element.parent();

      while (maybeParent.isPresent()) {
        element = maybeParent.get();

        if (selector.matches(element)) {
          parentMatched = true;
          break;
        }

        maybeParent = element.parent();
      }

      if (!parentMatched) {
        return false;
      }
    }

    return true;
  }

  public final UnmodifiableList<Selector> selectors() {
    return selectors;
  }

  @Override
  public final String toString() {
    return selectors.join(" ");
  }

  @Override
  final DescendantSelector newDescendantSelectorWithAncestor(Selector ancestor) {
    GrowableList<Selector> newSelectors = new GrowableList<>();
    newSelectors.add(ancestor);
    newSelectors.addAll(selectors);
    return new DescendantSelector(newSelectors.toUnmodifiableList());
  }

}