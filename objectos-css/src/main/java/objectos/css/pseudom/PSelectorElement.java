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
package objectos.css.pseudom;

import objectos.css.IdSelector;
import objectos.css.internal.AttributeValueOperator;
import objectos.css.internal.Combinator;
import objectos.css.internal.PAttributeSelectorImpl;
import objectos.css.internal.PAttributeValueSelectorImpl;
import objectos.css.internal.PClassSelectorImpl;
import objectos.css.internal.TypeSelector;

public sealed interface PSelectorElement {

  sealed interface PAttributeSelector extends PSelectorElement
      permits PAttributeSelectorImpl, PAttributeValueSelector {
    String attributeName();
  }

  sealed interface PAttributeValueSelector extends PAttributeSelector
      permits PAttributeValueSelectorImpl {
    AttributeValueOperator operator();

    String value();
  }

  sealed interface PClassSelector extends PSelectorElement permits PClassSelectorImpl {
    String className();
  }

  sealed interface PCombinator extends PSelectorElement permits Combinator {}

  sealed interface PIdSelector extends PSelectorElement permits IdSelector {
    String id();
  }

  sealed interface PPseudoClassSelector extends PSelectorElement
      permits objectos.css.internal.PseudoClassSelector {}

  sealed interface PPseudoElementSelector extends PSelectorElement
      permits objectos.css.internal.PseudoElementSelector {}

  sealed interface PTypeSelector extends PSelectorElement permits TypeSelector {}

}