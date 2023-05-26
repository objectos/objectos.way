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

import br.com.objectos.css.parser.IsNonTerminal;
import br.com.objectos.css.sheet.RuleElement;
import java.util.stream.Stream;
import objectos.lang.Check;
import objectos.util.UnmodifiableList;
import objectos.util.GrowableList;

public abstract class Selector implements IsNonTerminal {

  public static class Builder extends ModeDsl implements BuilderDsl {

    private GrowableList<Selector> combinatorList;

    private GrowableList<Selector> compoundList;
    private Combinator currentCombinator;
    private Mode mode = Mode.start();

    private Builder() {}

    @Override
    public final void addCombinator(Combinator combinator) {
      mode = mode.addCombinator(this, combinator);
    }

    @Override
    public final void addSimpleSelector(SimpleSelector selector) {
      mode = mode.addSimpleSelector(this, selector);
    }

    @Override
    public final void addUniversalSelector(UniversalSelector selector) {
      mode = mode.addUniversalSelector(this, selector);
    }

    public final Selector build() {
      return mode.build(this);
    }

    public final void reset() {
      if (combinatorList != null) {
        combinatorList.clear();
      }

      if (compoundList != null) {
        compoundList.clear();
      }

      currentCombinator = null;

      mode = Mode.start();
    }

    @Override
    protected final Selector getLastCompound() {
      int size;
      size = compoundList.size();

      return compoundList.get(size - 1);
    }

    @Override
    protected final Selector newCombinatorSelector(Selector selector) {
      addToCombinatorList(selector);

      UnmodifiableList<Selector> elements;
      elements = combinatorList.toUnmodifiableList();

      combinatorList = null;

      return currentCombinator.combine(elements);
    }

    @Override
    protected final Selector newCompoundSelector() {
      UnmodifiableList<Selector> elements;
      elements = compoundList.toUnmodifiableList();

      compoundList = null;

      return new CompoundSelector(elements);
    }

    @Override
    protected final Selector newSimpleSelector() {
      Selector selector;
      selector = getLastCompound();

      compoundList = null;

      return selector;
    }

    @Override
    protected final void toCombinatorCompoundMode(Selector selector) {
      toCompoundSelectorMode(selector);
    }

    @Override
    protected final void toCombinatorSimpleMode(Selector selector) {
      toSimpleSelectorMode(selector);
    }

    @Override
    protected final void toCombinatorStartMode(Combinator combinator) {
      currentCombinator = Check.notNull(combinator, "combinator == null");

      Selector selector;
      selector = build();

      addToCombinatorList(selector);
    }

    @Override
    protected final void toCombinatorStartMode(Combinator combinator, Selector selector) {
      Combinator previous = currentCombinator;

      currentCombinator = Check.notNull(combinator, "combinator == null");

      Selector toAdd = selector;

      if (previous != combinator) {
        toAdd = newCombinatorSelector(selector);
      }

      addToCombinatorList(toAdd);
    }

    @Override
    protected final void toCompoundSelectorMode(Selector selector) {
      toSimpleSelectorMode(selector);
    }

    @Override
    protected final void toSimpleSelectorMode(Selector selector) {
      if (compoundList == null) {
        compoundList = new GrowableList<>();
      }

      compoundList.addWithNullMessage(selector, "selector == null");
    }

    private void addToCombinatorList(Selector selector) {
      if (combinatorList == null) {
        combinatorList = new GrowableList<>();
      }

      combinatorList.add(selector);
    }

  }

  public interface BuilderDsl {

    void addCombinator(Combinator combinator);

    void addSimpleSelector(SimpleSelector selector);

    void addUniversalSelector(UniversalSelector selector);

  }

  Selector() {}

  public static Builder builder() {
    return new Builder();
  }

  static Selector checkIsSelector(Object o, String name) {
    Check.argument((o instanceof Selector), name + " not instanceof Selector");
    return (Selector) o;
  }

  public abstract void acceptRuleElementList(GrowableList<RuleElement> elements);

  public abstract <R, P> R acceptSelectorVisitor(SelectorVisitor<R, P> visitor, P p);

  public abstract boolean matches(Selectable element);

  @Override
  public abstract String toString();

  final void acceptRuleElementListImpl(
      GrowableList<RuleElement> elements, UnmodifiableList<Selector> list, Combinator combinator) {
    if (list.isEmpty()) {
      return;
    }

    Selector first;
    first = list.get(0);

    first.acceptRuleElementList(elements);

    for (int i = 1; i < list.size(); i++) {
      elements.add(combinator);

      Selector next;
      next = list.get(i);

      next.acceptRuleElementList(elements);
    }
  }

  AdjacentSiblingSelector newAdjacentSiblingSelectorWithPrevious(Selector previous) {
    return new AdjacentSiblingSelector(UnmodifiableList.of(previous, this));
  }

  ChildSelector newChildSelectorWithParent(Selector parent) {
    return new ChildSelector(UnmodifiableList.of(parent, this));
  }

  DescendantSelector newDescendantSelectorWithAncestor(Selector ancestor) {
    return new DescendantSelector(UnmodifiableList.of(ancestor, this));
  }

  GeneralSiblingSelector newGeneralSiblingSelectorWithPrevious(Selector previous) {
    return new GeneralSiblingSelector(UnmodifiableList.of(previous, this));
  }

  SelectorList newSelectorListWithHead(Selector head) {
    return new SelectorList(UnmodifiableList.of(head, this));
  }

  final Stream<? extends Selectable> parentStream(Selectable element) {
    return element.parent()
        .map(el -> Stream.concat(Stream.of(el), parentStream(el)))
        .orElse(Stream.of());
  }

  void uncheckedAddAttributeSelector(AttributeSelector selector) {}

  void uncheckedAddAttributeValueSelector(AttributeValueSelector selector) {}

  void uncheckedAddClassSelector(ClassSelector selector) {}

  void uncheckedAddIdSelector(IdSelector selector) {}

  void uncheckedAddPseudoClassSelector(PseudoClassSelector selector) {}

  void uncheckedAddPseudoElementSelector(PseudoElementSelector selector) {}

  void uncheckedAddTypeSelector(TypeSelector selector) {}

}