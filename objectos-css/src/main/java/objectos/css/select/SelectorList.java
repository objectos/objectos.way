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

public class SelectorList extends Selector implements Iterable<Selector>, SelectorListTail {

  private final UnmodifiableList<Selector> list;

  SelectorList(UnmodifiableList<Selector> list) {
    this.list = list;
  }

  SelectorList(Selector... selectors) {
    list = UnmodifiableList.copyOf(selectors);
  }

  public static SelectorList ofParser(SelectorListHead head, SelectorListTail tail) {
    Selector h = checkIsSelector(head, "head");
    Selector t = checkIsSelector(tail, "tail");
    return t.newSelectorListWithHead(h);
  }

  @Override
  public final void acceptRuleElementList(GrowableList<RuleElement> elements) {
    acceptRuleElementListImpl(elements, list, Combinator.LIST);
  }

  @Override
  public final <R, P> R acceptSelectorVisitor(SelectorVisitor<R, P> visitor, P p) {
    return visitor.visitSelectorList(this, p);
  }

  @Override
  public final boolean equals(Object obj) {
    if (!(obj instanceof SelectorList)) {
      return false;
    }
    SelectorList that = (SelectorList) obj;
    return list.equals(that.list);
  }

  @Override
  public final int hashCode() {
    return list.hashCode();
  }

  @Override
  public final Iterator<Selector> iterator() {
    return list.iterator();
  }

  @Override
  public final boolean matches(Selectable element) {
    throw new UnsupportedOperationException("Implement me");
  }

  public final UnmodifiableList<Selector> selectors() {
    return list;
  }

  public final int size() {
    return list.size();
  }

  @Override
  public final String toString() {
    StringBuilder sb = new StringBuilder();
    Iterator<Selector> it = list.iterator();

    if (it.hasNext()) {
      sb.append(it.next());
      while (it.hasNext()) {
        sb.append(", ");
        sb.append(it.next());
      }
    }

    return sb.toString();
  }

  @Override
  final SelectorList newSelectorListWithHead(Selector head) {
    GrowableList<Selector> newList = new GrowableList<>();
    newList.add(head);
    newList.addAll(list);
    return new SelectorList(newList.toUnmodifiableList());
  }

}