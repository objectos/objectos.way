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

import objectos.css.tmpl.AttributeValueElement;
import objectos.css.tmpl.PropertyValue;
import objectos.css.tmpl.StyleRuleElement;

public abstract class CssTemplateApi {

  abstract InternalInstruction addAttribute(int name, AttributeValueElement element);

  abstract InternalInstruction addDeclaration(Property property, double value);

  abstract InternalInstruction addDeclaration(Property property, int value);

  abstract InternalInstruction addDeclaration(Property property, String value);

  abstract InternalInstruction addDeclaration(Property property, PropertyValue... values);

  abstract InternalInstruction addInternal(int type, int value);

  abstract InternalInstruction addInternal(int type, int value0, int value1);

  abstract int addObject(Object value);

  abstract void addRule(StyleRuleElement... elements);

}