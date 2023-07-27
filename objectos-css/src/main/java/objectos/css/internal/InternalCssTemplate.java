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
import objectos.css.om.MediaFeatureOrStyleDeclaration;
import objectos.css.om.MediaQuery;
import objectos.css.om.MediaRuleElement;
import objectos.css.om.Selector;
import objectos.css.om.SelectorElement;
import objectos.css.om.StyleDeclaration;
import objectos.css.om.StyleRule;
import objectos.css.om.StyleRuleElement;
import objectos.css.tmpl.BoxShadowDeclaration;
import objectos.css.tmpl.BoxShadowHashDeclaration;
import objectos.css.tmpl.PropertyValue;
import objectos.css.tmpl.PropertyValue.ColorValue;
import objectos.css.tmpl.PropertyValue.FontFamilyValue;
import objectos.css.tmpl.PropertyValue.LengthValue;
import objectos.css.tmpl.PropertyValue.PercentageValue;
import objectos.css.tmpl.PropertyValue.StringLiteral;
import objectos.css.tmpl.PropertyValue.Url;
import objectos.css.tmpl.PropertyValue.Zero;
import objectos.css.util.CustomProperty;
import objectos.lang.Check;

public abstract class InternalCssTemplate extends GeneratedCssTemplate {

  protected static final MediaQuery screen = MediaType.SCREEN;

  protected static final SelectorElement SIBLING = Combinator.ADJACENT_SIBLING;

  protected static final SelectorElement CHILD = Combinator.CHILD;

  protected static final SelectorElement GENERAL_SIBLING = Combinator.GENERAL_SIBLING;

  protected static final SelectorElement DESCENDANT = Combinator.DESCENDANT;

  protected static final AttributeOperator EQ = InternalAttributeOperator.EQUALS;

  protected static final Zero $0 = InternalZero.INSTANCE;

  private CssTemplateApi api;

  public final StyleSheet compile() {
    try {
      api = new Compiler02();

      api.compilationBegin();

      definition();

      api.compilationEnd();

      api.optimize();

      return api.compile();
    } finally {
      api = null;
    }
  }

  @Override
  public final String toString() {
    StyleSheet compiled;
    compiled = compile();

    return compiled.toString();
  }

  @SuppressWarnings("unchecked")
  protected final <T extends PropertyValue> T var(CustomProperty<T> variable) {
    Check.notNull(variable, "variable == null");

    CssTemplateApi api;
    api = api();

    api.varFunction(variable);

    return (T) InternalInstruction.VAR_FUNCTION;
  }

  protected final Selector attr(String name) {
    Check.notNull(name, "name == null");

    CssTemplateApi api;
    api = api();

    api.selectorAttribute(name);

    return InternalInstruction.INSTANCE;
  }

  protected final Selector attr(String name, AttributeOperator operator, String value) {
    Check.notNull(name, "name == null");
    Check.notNull(operator, "operator == null");
    Check.notNull(value, "value == null");

    CssTemplateApi api;
    api = api();

    api.selectorAttribute(name, operator, value);

    return InternalInstruction.INSTANCE;
  }

  protected final BoxShadowHashDeclaration boxShadow(BoxShadowDeclaration... values) {
    Check.argument(values.length > 0, "box-shadow hash declaration requires at least one value");

    CssTemplateApi api;
    api = api();

    api.declarationBegin(Property.BOX_SHADOW);

    api.propertyHash(
      Check.notNull(values[0], "values[0] == null")
    );

    for (int i = 1; i < values.length; i++) {
      api.propertyValueComma();

      api.propertyHash(
        Check.notNull(values[i], "values[", i, "] == null")
      );
    }

    api.declarationEnd();

    return InternalInstruction.INSTANCE;
  }

  protected abstract void definition();

  @Override
  protected final StyleDeclaration fontFamily(FontFamilyValue... values) {
    Check.argument(values.length > 0, "font-family requires at least one value");

    CssTemplateApi api;
    api = api();

    api.declarationBegin(Property.FONT_FAMILY);

    api.propertyValue(
      Check.notNull(values[0], "values[0] == null")
    );

    for (int i = 1; i < values.length; i++) {
      api.propertyValueComma();

      api.propertyValue(
        Check.notNull(values[i], "values[", i, "] == null")
      );
    }

    api.declarationEnd();

    return InternalInstruction.INSTANCE;
  }

  protected final ColorValue hex(String value) {
    Check.notNull(value, "value == null");

    api().colorHex(value);

    return InternalInstruction.COLOR_HEX;
  }

  protected final StringLiteral l(String value) {
    Check.notNull(value, "value == null");

    api().stringLiteral(value);

    return InternalInstruction.STRING_LITERAL;
  }

  protected final void media(MediaRuleElement... elements) {
    CssTemplateApi api;
    api = api();

    api.mediaRuleBegin();

    for (int i = 0; i < elements.length; i++) {
      api.mediaRuleElement(
        Check.notNull(elements[i], "elements[", i, "] == null")
      );
    }

    api.mediaRuleEnd();
  }

  protected final MediaFeatureOrStyleDeclaration minWidth(LengthValue value) {
    Check.notNull(value, "value == null");

    declaration(Property.MIN_WIDTH, value);

    return InternalInstruction.INSTANCE;
  }

  protected final MediaFeatureOrStyleDeclaration minWidth(Zero value) {
    Check.notNull(value, "value == null");

    declaration(Property.MIN_WIDTH, value);

    return InternalInstruction.INSTANCE;
  }

  protected final PercentageValue pct(double value) {
    api().percentage(value);

    return InternalInstruction.PERCENTAGE_DOUBLE;
  }

  protected final PercentageValue pct(int value) {
    api().percentage(value);

    return InternalInstruction.PERCENTAGE_INT;
  }

  protected final Selector sel(
      SelectorElement e1, SelectorElement e2) {
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");

    CssTemplateApi api;
    api = api();

    api.selectorBegin();
    api.selectorElement(e1);
    api.selectorElement(e2);
    api.selectorEnd();

    return InternalInstruction.INSTANCE;
  }

  protected final Selector sel(
      SelectorElement e1, SelectorElement e2, SelectorElement e3, SelectorElement e4,
      SelectorElement e5) {
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Check.notNull(e3, "e3 == null");
    Check.notNull(e4, "e4 == null");
    Check.notNull(e5, "e5 == null");

    CssTemplateApi api;
    api = api();

    api.selectorBegin();
    api.selectorElement(e1);
    api.selectorElement(e2);
    api.selectorElement(e3);
    api.selectorElement(e4);
    api.selectorElement(e5);
    api.selectorEnd();

    return InternalInstruction.INSTANCE;
  }

  protected final <T extends PropertyValue> StyleDeclaration set(
      CustomProperty<T> property, T value) {
    Check.notNull(property, "property == null");
    Check.notNull(value, "value == null");

    CssTemplateApi api;
    api = api();

    api.customPropertyBegin(property);
    api.propertyValue(value);
    api.customPropertyEnd();

    return InternalInstruction.INSTANCE;
  }

  protected final StyleRule style(StyleRuleElement... elements) {
    CssTemplateApi api;
    api = api();

    api.styleRuleBegin();

    for (int i = 0; i < elements.length; i++) {
      api.styleRuleElement(
        Check.notNull(elements[i], "elements[", i, "] == null")
      );
    }

    api.styleRuleEnd();

    return InternalInstruction.INSTANCE;
  }

  protected final Url url(String value) {
    Check.notNull(value, "value == null");

    api().url(value);

    return InternalInstruction.URL;
  }

  @Override
  final void declaration(
      Property name,
      double value) {
    CssTemplateApi api;
    api = api();

    api.declarationBegin(name);

    api.javaDouble(value);

    api.declarationEnd();
  }

  @Override
  final void declaration(
      Property name,
      int value) {
    CssTemplateApi api;
    api = api();

    api.declarationBegin(name);

    api.javaInt(value);

    api.declarationEnd();
  }

  @Override
  final void declaration(
      Property name,
      PropertyValue value) {
    CssTemplateApi api;
    api = api();

    api.declarationBegin(name);

    api.propertyValue(value);

    api.declarationEnd();
  }

  @Override
  final void declaration(
      Property name,
      PropertyValue value1, PropertyValue value2) {
    CssTemplateApi api;
    api = api();

    api.declarationBegin(name);

    api.propertyValue(value1);
    api.propertyValue(value2);

    api.declarationEnd();
  }

  @Override
  final void declaration(
      Property name,
      PropertyValue value1, PropertyValue value2, PropertyValue value3) {
    CssTemplateApi api;
    api = api();

    api.declarationBegin(name);

    api.propertyValue(value1);
    api.propertyValue(value2);
    api.propertyValue(value3);

    api.declarationEnd();
  }

  @Override
  final void declaration(
      Property name,
      PropertyValue value1, PropertyValue value2, PropertyValue value3, PropertyValue value4) {
    CssTemplateApi api;
    api = api();

    api.declarationBegin(name);

    api.propertyValue(value1);
    api.propertyValue(value2);
    api.propertyValue(value3);
    api.propertyValue(value4);

    api.declarationEnd();
  }

  @Override
  final void declaration(
      Property name,
      PropertyValue value1, PropertyValue value2, PropertyValue value3, PropertyValue value4,
      PropertyValue value5) {
    CssTemplateApi api;
    api = api();

    api.declarationBegin(name);

    api.propertyValue(value1);
    api.propertyValue(value2);
    api.propertyValue(value3);
    api.propertyValue(value4);
    api.propertyValue(value5);

    api.declarationEnd();
  }

  @Override
  final void declaration(
      Property name,
      PropertyValue value1, PropertyValue value2, PropertyValue value3, PropertyValue value4,
      PropertyValue value5, PropertyValue value6) {
    CssTemplateApi api;
    api = api();

    api.declarationBegin(name);

    api.propertyValue(value1);
    api.propertyValue(value2);
    api.propertyValue(value3);
    api.propertyValue(value4);
    api.propertyValue(value5);
    api.propertyValue(value6);

    api.declarationEnd();
  }

  @Override
  final void declaration(
      Property name,
      String value) {
    CssTemplateApi api;
    api = api();

    api.declarationBegin(name);

    api.javaString(value);

    api.declarationEnd();
  }

  @Override
  final LengthValue length(double value, LengthUnit unit) {
    api().length(value, unit);

    return InternalInstruction.LENGTH_DOUBLE;
  }

  @Override
  final LengthValue length(int value, LengthUnit unit) {
    api().length(value, unit);

    return InternalInstruction.LENGTH_INT;
  }

  private CssTemplateApi api() {
    Check.state(api != null, "api not set");

    return api;
  }

}