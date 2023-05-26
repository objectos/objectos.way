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
import objectos.util.UnmodifiableList;
import objectos.css.sheet.RuleElement;
import objectos.util.GrowableList;

public class CompoundSelector
    extends Selector
    implements ComplexSelectorHead, ComplexSelectorTail, Iterable<Selector> {

  private final UnmodifiableList<Selector> selectors;

  CompoundSelector(UnmodifiableList<Selector> selectors) {
    this.selectors = selectors;
  }

  public static CompoundSelector ofParser(SimpleSelector first, Iterable<SimpleSelector> more) {
    GrowableList<Selector> list = new GrowableList<>();
    list.add(checkIsSelector(first, "first"));

    for (SimpleSelector s : more) {
      list.add(checkIsSelector(s, "more"));
    }

    return new CompoundSelector(list.toUnmodifiableList());
  }

  @Override
  public final void acceptRuleElementList(GrowableList<RuleElement> elements) {
    for (int i = 0; i < selectors.size(); i++) {
      Selector selector;
      selector = selectors.get(i);

      selector.acceptRuleElementList(elements);
    }
  }

  @Override
  public final <R, P> R acceptSelectorVisitor(SelectorVisitor<R, P> visitor, P p) {
    return visitor.visitCompoundSelector(this, p);
  }

  @Override
  public final boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof CompoundSelector)) {
      return false;
    }
    CompoundSelector that = (CompoundSelector) obj;
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
    for (Selector selector : selectors) {
      if (!selector.matches(element)) {
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
    return selectors.join();
  }

}
