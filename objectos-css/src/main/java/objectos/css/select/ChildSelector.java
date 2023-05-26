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

import java.util.Iterator;
import java.util.Optional;
import objectos.util.UnmodifiableList;
import objectos.css.sheet.RuleElement;
import objectos.util.GrowableList;

public class ChildSelector extends Selector
    implements
    ComplexSelector,
    Iterable<Selector> {

  private final UnmodifiableList<Selector> selectors;

  ChildSelector(UnmodifiableList<Selector> selectors) {
    this.selectors = selectors;
  }

  public static ChildSelector ofParser(ComplexSelectorHead head, ComplexSelectorTail tail) {
    Selector h = checkIsSelector(head, "head");
    Selector t = checkIsSelector(tail, "tail");
    return t.newChildSelectorWithParent(h);
  }

  @Override
  public final void acceptRuleElementList(GrowableList<RuleElement> elements) {
    acceptRuleElementListImpl(elements, selectors, Combinator.CHILD);
  }

  @Override
  public final <R, P> R acceptSelectorVisitor(SelectorVisitor<R, P> visitor, P p) {
    return visitor.visitChildSelector(this, p);
  }

  @Override
  public final boolean equals(Object obj) {
    if (!(obj instanceof ChildSelector)) {
      return false;
    }
    ChildSelector that = (ChildSelector) obj;
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
    Optional<? extends Selectable> maybeCurrent = Optional.of(element);

    for (int i = selectors.size() - 1; i >= 0; i--) {
      if (!maybeCurrent.isPresent()) {
        return false;
      }

      Selectable current = maybeCurrent.get();
      Selector selector = selectors.get(i);

      if (!selector.matches(current)) {
        return false;
      }

      maybeCurrent = current.parent();
    }

    return true;
  }

  @Override
  public final String toString() {
    return selectors.join(" > ");
  }

  @Override
  final ChildSelector newChildSelectorWithParent(Selector parent) {
    GrowableList<Selector> newSelectors = new GrowableList<>();
    newSelectors.add(parent);
    newSelectors.addAll(selectors);
    return new ChildSelector(newSelectors.toUnmodifiableList());
  }

}