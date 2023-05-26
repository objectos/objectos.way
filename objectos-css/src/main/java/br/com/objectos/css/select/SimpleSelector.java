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

public abstract class SimpleSelector extends Selector
    implements
    ComplexSelectorHead,
    ComplexSelectorTail,
    SelectorElement {

  SimpleSelector() {}

  @Override
  public final void acceptRuleElementList(GrowableList<RuleElement> elements) {
    elements.add(this);
  }

  @Override
  public final void acceptSelectorBuilderDsl(Selector.BuilderDsl dsl) {
    dsl.addSimpleSelector(this);
  }

  @Override
  public final <R, P> R acceptSelectorVisitor(SelectorVisitor<R, P> visitor, P p) {
    return acceptSimpleSelectorVisitor(visitor, p);
  }

  public abstract <R, P> R acceptSimpleSelectorVisitor(SimpleSelectorVisitor<R, P> visitor, P p);

}
