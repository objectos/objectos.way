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

import java.util.Objects;
import objectos.css.tmpl.Keywords;
import objectos.css.tmpl.LengthUnit;
import objectos.css.tmpl.PropertyValue;
import objectos.css.tmpl.PropertyValue.Length;
import objectos.css.tmpl.StyleRuleElement;

abstract class GeneratedCssTemplate {

  protected static final StyleRuleElement ANY = UniversalSelector.INSTANCE;

  protected static final StyleRuleElement A = TypeSelector.A;

  protected static final StyleRuleElement BODY = TypeSelector.BODY;

  protected static final StyleRuleElement LI = TypeSelector.LI;

  protected static final StyleRuleElement UL = TypeSelector.UL;

  protected static final StyleRuleElement OR = Combinator.LIST;

  protected static final StyleRuleElement SP = Combinator.DESCENDANT;

  protected static final StyleRuleElement ACTIVE = PseudoClassSelector.ACTIVE;

  protected static final StyleRuleElement VISITED = PseudoClassSelector.VISITED;

  protected static final StyleRuleElement AFTER = PseudoElementSelector.AFTER;

  protected static final Keywords.Block BLOCK = Keyword.BLOCK;

  protected final StyleRuleElement content(String value) {
    Objects.requireNonNull(value, "value == null");
    return api().addDeclaration(Property.CONTENT, value);
  }

  protected final StyleRuleElement display(PropertyValue.Display1 value) {
    return api().addDeclaration(Property.DISPLAY, value.self());
  }

  protected final StyleRuleElement lineHeight(double value) {
    return api().addDeclaration(Property.LINE_HEIGHT, value);
  }

  protected final StyleRuleElement lineHeight(Length value) {
    return api().addDeclaration(Property.LINE_HEIGHT, value);
  }

  protected final StyleRuleElement minHeight(Length value) {
    return api().addDeclaration(Property.MIN_HEIGHT, value.self());
  }

  protected final StyleRuleElement zIndex(int value) {
    return api().addDeclaration(Property.Z_INDEX, value);
  }

  protected final Length px(int value) {
    return api().addLength(LengthUnit.PX, value);
  }

  protected final Length pt(double value) {
    return api().addLength(LengthUnit.PT, value);
  }

  abstract CssTemplateApi api();

}
