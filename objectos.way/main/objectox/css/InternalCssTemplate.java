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
package objectox.css;

import objectos.css.AttributeOperator;
import objectos.css.CssTemplate;
import objectos.css.StyleSheet;
import objectos.css.tmpl.Api;
import objectos.css.tmpl.Api.BoxShadowDeclaration;
import objectos.css.tmpl.Api.BoxShadowHashDeclaration;
import objectos.css.tmpl.Api.ColorValue;
import objectos.css.tmpl.Api.FontFamilyValue;
import objectos.css.tmpl.Api.LengthValue;
import objectos.css.tmpl.Api.MediaFeatureOrStyleDeclaration;
import objectos.css.tmpl.Api.MediaQuery;
import objectos.css.tmpl.Api.MediaRuleElement;
import objectos.css.tmpl.Api.PercentageValue;
import objectos.css.tmpl.Api.PropertyValue;
import objectos.css.tmpl.Api.Selector;
import objectos.css.tmpl.Api.StringLiteral;
import objectos.css.tmpl.Api.StyleDeclaration;
import objectos.css.tmpl.Api.StyleRule;
import objectos.css.tmpl.Api.StyleRuleElement;
import objectos.css.tmpl.Api.Url;
import objectos.css.tmpl.Api.Zero;
import objectos.css.util.CustomProperty;
import objectos.css.util.Length;
import objectos.css.util.Percentage;
import objectos.lang.object.Check;

public abstract class InternalCssTemplate extends GeneratedCssTemplate {

  protected static final MediaQuery screen = MediaType.SCREEN;

  protected static final Selector PLUS = Combinator.ADJACENT_SIBLING;

  protected static final Selector GT = Combinator.CHILD;

  protected static final Selector TILDE = Combinator.GENERAL_SIBLING;

  protected static final Selector SP = Combinator.DESCENDANT;

  protected static final Selector OR = Combinator.LIST;

  protected static final AttributeOperator EQ = InternalAttributeOperator.EQUALS;

  protected static final Zero $0 = InternalZero.INSTANCE;

  protected static final LengthValue U0 = InternalZero.INSTANCE;

  protected static final LengthValue U1 = unit(1);

  protected static final LengthValue U2 = unit(2);

  protected static final LengthValue U3 = unit(3);

  protected static final LengthValue U4 = unit(4);

  protected static final LengthValue U5 = unit(5);

  protected static final LengthValue U6 = unit(6);

  protected static final LengthValue U7 = unit(7);

  protected static final LengthValue U8 = unit(8);

  protected static final LengthValue U9 = unit(9);

  protected static final LengthValue U10 = unit(10);

  protected static final LengthValue U11 = unit(11);

  protected static final LengthValue U12 = unit(12);

  protected static final LengthValue U14 = unit(14);

  protected static final LengthValue U16 = unit(16);

  protected static final LengthValue U20 = unit(20);

  protected static final LengthValue U24 = unit(24);

  protected static final LengthValue U28 = unit(28);

  protected static final LengthValue U32 = unit(32);

  protected static final LengthValue U36 = unit(36);

  protected static final LengthValue U40 = unit(40);

  protected static final LengthValue U44 = unit(44);

  protected static final LengthValue U48 = unit(48);

  protected static final LengthValue U52 = unit(52);

  protected static final LengthValue U56 = unit(56);

  protected static final LengthValue U60 = unit(60);

  protected static final LengthValue U64 = unit(64);

  protected static final LengthValue U68 = unit(68);

  protected static final LengthValue U72 = unit(72);

  protected static final LengthValue U80 = unit(80);

  protected static final LengthValue U96 = unit(96);

  protected static final PercentageValue FULL = Percentage.of(100);

  private CssTemplateApi api;

  private static LengthValue unit(int value) {
    return Length.rem(0.25 * value);
  }

  public final StyleSheet compile() {
    try {
      api = new CssCompiler02();

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

  protected final StyleDeclaration filter(Api.FilterFunction func) {
    Check.notNull(func, "func == null");

    CssTemplateApi api;
    api = api();

    api.declarationBegin(Property.FILTER);
    api.filterFunction(func);
    api.declarationEnd();

    return InternalInstruction.INSTANCE;
  }

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

  protected final Api.FlexValue fr(double value) {
    api().flexValue(value);

    return InternalInstruction.INSTANCE;
  }

  protected final Api.FlexValue fr(int value) {
    api().flexValue(value);

    return InternalInstruction.INSTANCE;
  }

  protected final ColorValue hex(String value) {
    Check.notNull(value, "value == null");

    api().colorHex(value);

    return InternalInstruction.INSTANCE;
  }

  protected final void install(CssTemplate template) {
    Check.notNull(template, "template == null");

    InternalCssTemplate internal;
    internal = template;

    internal.api = api();

    internal.definition();
  }

  protected final Api.DoubleLiteral l(double value) {
    api().literalDouble(value);

    return InternalInstruction.INSTANCE;
  }

  protected final Api.IntLiteral l(int value) {
    api().literalInt(value);

    return InternalInstruction.INSTANCE;
  }

  protected final StringLiteral l(String value) {
    Check.notNull(value, "value == null");

    api().literalString(value);

    return InternalInstruction.INSTANCE;
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

    return InternalInstruction.INSTANCE;
  }

  protected final PercentageValue pct(int value) {
    api().percentage(value);

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

    return InternalInstruction.INSTANCE;
  }

  @SuppressWarnings("unchecked")
  protected final <T extends PropertyValue> T var(CustomProperty<T> variable) {
    Check.notNull(variable, "variable == null");

    CssTemplateApi api;
    api = api();

    api.varFunctionBegin(variable);

    api.varFunctionEnd();

    return (T) InternalInstruction.INSTANCE;
  }

  @SuppressWarnings("unchecked")
  protected final <T extends PropertyValue> T var(CustomProperty<T> variable, T defaultValue) {
    Check.notNull(variable, "variable == null");
    Check.notNull(defaultValue, "defaultValue == null");

    CssTemplateApi api;
    api = api();

    api.varFunctionBegin(variable);

    api.propertyValueComma();

    api.propertyValue(defaultValue);

    api.varFunctionEnd();

    return (T) InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration webkitFilter(Api.FilterFunction func) {
    Check.notNull(func, "func == null");

    CssTemplateApi api;
    api = api();

    api.declarationBegin(Property._WEBKIT_FILTER);
    api.filterFunction(func);
    api.declarationEnd();

    return InternalInstruction.INSTANCE;
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
  final void function(Function name, PropertyValue value1, PropertyValue value2) {
    CssTemplateApi api;
    api = api();

    api.functionBegin(name);

    api.propertyValue(value1);
    api.propertyValue(value2);

    api.functionEnd();
  }

  @Override
  final LengthValue length(double value, LengthUnit unit) {
    api().length(value, unit);

    return InternalInstruction.INSTANCE;
  }

  @Override
  final LengthValue length(int value, LengthUnit unit) {
    api().length(value, unit);

    return InternalInstruction.INSTANCE;
  }

  private CssTemplateApi api() {
    Check.state(api != null, "api not set");

    return api;
  }

}