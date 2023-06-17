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

final class GeneratedCssTemplateStep extends ThisTemplate {

  private static final SelectorName UNIVERSAL = new SelectorName("any", "*");

  @Override
  protected final void definition() {
    packageDeclaration(CSS);

    autoImports();

    classDeclaration(
      annotation(GENERATED, annotationValue(s(GENERATOR))),
      ABSTRACT, name("GeneratedCssTemplate"),

      include(this::selectors),

      method(
        PRIVATE, STATIC, NAMED_ELEMENT, name("named"),
        parameter(STRING, name("name")),
        p(RETURN, NEW, NAMED_ELEMENT, argument(n("name")))
      )
    );
  }

  private void selectors() {
    var selectors = spec.selectors();

    for (var selector : selectors) {
      selectorField(selector);
    }

    selectorField(UNIVERSAL);
  }

  private void selectorField(SelectorName selector) {
    field(
      PROTECTED, STATIC, FINAL, SELECTOR, name(selector.fieldName()),
      v("named"), argument(s(selector.selectorName()))
    );
  }

}
