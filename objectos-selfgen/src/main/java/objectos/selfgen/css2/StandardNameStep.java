/*
 * Copyright (C) 2015-2023 Objectos Software LTDA.
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
package objectos.selfgen.css2;

import objectos.code.ArrayTypeName;

final class StandardNameStep extends ThisTemplate {

  private static final SelectorName UNIVERSAL = new SelectorName(SelectorKind.OTHER, "any", "*");

  @Override
  protected final void definition() {
    packageDeclaration(CSS_INTERNAL);

    autoImports();

    enumDeclaration(
      annotation(GENERATED, annotationValue(s(GENERATOR))),
      PUBLIC, name(STANDARD_NAME),
      include(this::superInterfaces),

      include(this::selectors),

      include(this::colors),

      include(this::keywords),

      field(
        PRIVATE, STATIC, FINAL, ArrayTypeName.of(STANDARD_NAME), name("VALUES"),
        v("values")
      ),

      field(
        PUBLIC, FINAL, STRING, name("cssName")
      ),

      constructor(
        PRIVATE,
        parameter(STRING, name("cssName")),
        p(THIS, n("cssName"), IS, n("cssName"))
      ),

      method(
        PUBLIC, STATIC, STANDARD_NAME, name("byOrdinal"),
        parameter(INT, name("ordinal")),
        p(RETURN, n("VALUES"), dim(n("ordinal")))
      ),

      method(
        annotation(OVERRIDE),
        PUBLIC, FINAL, STRING, name("toString"),
        p(RETURN, n("cssName"))
      )
    );
  }

  private void superInterfaces() {
    implementsClause(SELECTOR);

    implementsClause(COLOR_VALUE);

    spec.valueTypes().stream()
        .filter(ValueType::permitsStandardName)
        .sorted(ValueType.ORDER_BY_SIMPLE_NAME)
        .forEach(type -> implementsClause(NL, type.className));

    spec.keywords().stream()
        .filter(KeywordName::shouldGenerate)
        .sorted(KeywordName.ORDER_BY_SIMPLE_NAME)
        .forEach(kw -> implementsClause(NL, kw.className()));
  }

  private void selectors() {
    selectorConstant(UNIVERSAL);
  }

  private void selectorConstant(SelectorName selector) {
    enumConstant(name(selector.fieldName), argument(s(selector.selectorName)));
  }

  private void colors() {
    ColorValue colorValue;
    colorValue = spec.colorValue();

    if (colorValue == null) {
      return;
    }

    colorValue.names.stream()
        .sorted((self, that) -> (self.fieldName().compareTo(that.fieldName())))
        .forEach(
          name -> enumConstant(name(name.fieldName()), argument(s(name.colorName())))
        );
  }

  private void keywords() {
    spec.keywords().stream()
        .sorted(KeywordName.ORDER_BY_FIELD_NAME)
        .forEach(kw -> {
          enumConstant(name(kw.fieldName), argument(s(kw.keywordName)));
        });
  }

}
