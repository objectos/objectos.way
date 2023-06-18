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

final class NamedElementStep extends ThisTemplate {

  @Override
  protected final void definition() {
    packageDeclaration(CSS_INTERNAL);

    autoImports();

    classDeclaration(
      annotation(GENERATED, annotationValue(s(GENERATOR))),
      PUBLIC, FINAL, name(NAMED_ELEMENT),
      implementsClause(SELECTOR),
      include(this::superInterfaces),

      field(
        PRIVATE, FINAL, STRING, name("name")
      ),

      constructor(
        PUBLIC,
        parameter(STRING, name("name")),
        p(THIS, n("name"), IS, n("name"))
      ),

      method(
        annotation(OVERRIDE),
        PUBLIC, FINAL, STRING, name("toString"),
        p(RETURN, n("name"))
      )
    );
  }

  private void superInterfaces() {
    spec.valueTypes().stream()
        .filter(ValueType::permitsNamedElement)
        .sorted(ValueType.ORDER_BY_SIMPLE_NAME)
        .forEach(this::valueTypeImpl);
  }

  private void valueTypeImpl(ValueType type) {
    implementsClause(NL, type.className);
  }

}