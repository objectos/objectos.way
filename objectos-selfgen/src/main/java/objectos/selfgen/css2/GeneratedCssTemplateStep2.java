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

import objectos.code.ClassTypeName;

final class GeneratedCssTemplateStep2 extends ThisTemplate {

  @Override
  protected final void definition() {
    packageDeclaration(CSS_INTERNAL);

    autoImports();

    classDeclaration(
      annotation(GENERATED, annotationValue(s(GENERATOR))),
      ABSTRACT, name("GeneratedCssTemplate"),

      include(this::selectors),

      include(this::colors),

      include(this::keywords)
    );
  }

  private void selectors() {
    spec.selectors().stream()
        .filter(s -> !s.disabled)
        .sorted(SelectorName.ORDER_BY_FIELD_NAME)
        .forEach(sel -> field(SELECTOR, sel.fieldName));

    field(SELECTOR, "any");
  }

  private void field(ClassTypeName type, String fieldName) {
    field(
      PROTECTED, STATIC, FINAL, type, name(fieldName),
      STANDARD_NAME, n(fieldName)
    );
  }

  private void colors() {
    var colorValue = spec.colorValue();

    if (colorValue == null) {
      return;
    }

    colorValue.names.stream()
        .sorted((self, that) -> (self.fieldName().compareTo(that.fieldName())))
        .forEach(name -> field(COLOR_VALUE, name.fieldName()));
  }

  private void keywords() {
    spec.keywords().stream()
        .sorted(KeywordName.ORDER_BY_FIELD_NAME)
        .forEach(kw -> field(kw.className(), kw.fieldName));
  }

}
