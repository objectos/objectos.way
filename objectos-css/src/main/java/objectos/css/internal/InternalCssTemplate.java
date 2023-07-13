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

import objectos.css.StyleSheet;
import objectos.css.om.PropertyName;
import objectos.css.om.PropertyValue;
import objectos.css.om.StyleDeclaration;
import objectos.css.om.StyleRule;
import objectos.css.tmpl.FontFamilyValue;
import objectos.css.tmpl.StyleRuleElement;
import objectos.lang.Check;

public abstract class InternalCssTemplate extends GeneratedCssTemplate {

  private CssTemplateApi api;

  public final StyleSheet compile() {
    try {
      api = new Compiler02();

      api.compilationStart();

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

  protected abstract void definition();

  @Override
  protected final StyleDeclaration fontFamily(FontFamilyValue... values) {
    throw new UnsupportedOperationException("Implement me");
  }

  protected final StyleRule style(StyleRuleElement... elements) {
    CssTemplateApi api = api();
    api.styleRuleStart();
    for (int i = 0; i < elements.length; i++) {
      api.styleRuleElement(
        Check.notNull(elements[i], "elements[", i, "] == null")
      );
    }
    api.styleRuleEnd();
    return InternalInstruction.STYLE_RULE;
  }

  @Override
  final StyleDeclaration declaration(
      PropertyName name,
      double value) {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  final StyleDeclaration declaration(
      PropertyName name,
      int value) {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  final StyleDeclaration declaration(
      PropertyName name,
      PropertyValue value) {
    CssTemplateApi api = api();
    api.declarationStart(name);
    api.declarationValue(value);
    api.declarationEnd();
    return InternalInstruction.DECLARATION;
  }

  @Override
  final StyleDeclaration declaration(
      PropertyName name,
      PropertyValue value1, PropertyValue value2) {
    CssTemplateApi api = api();
    api.declarationStart(name);
    api.declarationValue(value1);
    api.declarationValue(value2);
    api.declarationEnd();
    return InternalInstruction.DECLARATION;
  }

  @Override
  final StyleDeclaration declaration(
      PropertyName name,
      PropertyValue value1, PropertyValue value2, PropertyValue value3) {
    CssTemplateApi api = api();
    api.declarationStart(name);
    api.declarationValue(value1);
    api.declarationValue(value2);
    api.declarationValue(value3);
    api.declarationEnd();
    return InternalInstruction.DECLARATION;
  }

  @Override
  final StyleDeclaration declaration(
      PropertyName name,
      PropertyValue value1, PropertyValue value2, PropertyValue value3, PropertyValue value4) {
    CssTemplateApi api = api();
    api.declarationStart(name);
    api.declarationValue(value1);
    api.declarationValue(value2);
    api.declarationValue(value3);
    api.declarationValue(value4);
    api.declarationEnd();
    return InternalInstruction.DECLARATION;
  }

  @Override
  final StyleDeclaration declaration(
      PropertyName name,
      String value) {
    throw new UnsupportedOperationException("Implement me");
  }

  private CssTemplateApi api() {
    Check.state(api != null, "api not set");

    return api;
  }

}