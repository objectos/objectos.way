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
package objectos.css.internal;

import objectos.css.tmpl.PropertyValue;
import objectos.css.tmpl.StyleRuleElement;

abstract class GeneratedCssTemplate {

  protected static final StyleRuleElement A = TypeSelector.A;

  protected static final StyleRuleElement BODY = TypeSelector.BODY;

  protected static final StyleRuleElement LI = TypeSelector.LI;

  protected static final StyleRuleElement UL = TypeSelector.UL;

  protected static final StyleRuleElement OR = Combinator.LIST;

  protected static final StyleRuleElement SP = Combinator.DESCENDANT;

  protected static final StyleRuleElement ACTIVE = PseudoClassSelector.ACTIVE;

  protected static final StyleRuleElement VISITED = PseudoClassSelector.VISITED;

  protected static final StyleRuleElement AFTER = PseudoElementSelector.AFTER;

  protected final StyleRuleElement display(PropertyValue.Display1 value) {
    return addDeclaration(Property.DISPLAY, value.self());
  }

  abstract StyleRuleElement addDeclaration(Property property, PropertyValue value);

}
