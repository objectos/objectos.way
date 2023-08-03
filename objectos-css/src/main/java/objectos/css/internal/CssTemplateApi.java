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

import objectos.css.AttributeOperator;
import objectos.css.StyleSheet;
import objectos.css.om.MediaRuleElement;
import objectos.css.tmpl.Api.PropertyValue;
import objectos.css.tmpl.StyleDeclaration;
import objectos.css.tmpl.StyleRuleElement;
import objectos.css.util.CustomProperty;

public abstract class CssTemplateApi {

  CssTemplateApi() {}

  public abstract void colorHex(String value);

  public abstract void compilationBegin();

  public abstract void compilationEnd();

  public StyleSheet compile() {
    throw new UnsupportedOperationException();
  }

  public abstract void customPropertyBegin(CustomProperty<?> property);

  public abstract void customPropertyEnd();

  public abstract void declarationBegin(Property name);

  public abstract void declarationEnd();

  public abstract void javaDouble(double value);

  public abstract void javaInt(int value);

  public abstract void javaString(String value);

  public abstract void length(double value, LengthUnit unit);

  public abstract void length(int value, LengthUnit unit);

  public abstract void mediaRuleBegin();

  public abstract void mediaRuleElement(MediaRuleElement element);

  public abstract void mediaRuleEnd();

  public void optimize() {
    throw new UnsupportedOperationException();
  }

  public abstract void percentage(double value);

  public abstract void percentage(int value);

  public abstract void propertyHash(StyleDeclaration value);

  public abstract void propertyValue(PropertyValue value);

  public abstract void propertyValueComma();

  public abstract void selectorAttribute(String name);

  public abstract void selectorAttribute(String name, AttributeOperator operator, String value);

  public abstract void stringLiteral(String value);

  public abstract void styleRuleBegin();

  public abstract void styleRuleElement(StyleRuleElement element);

  public abstract void styleRuleEnd();

  public abstract void url(String value);

  public abstract void varFunction(CustomProperty<?> variable);

}
