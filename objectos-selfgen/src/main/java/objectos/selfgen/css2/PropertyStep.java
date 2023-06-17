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

final class PropertyStep extends ThisTemplate {

  @Override
  protected final void definition() {
    packageDeclaration(CSS_INTERNAL);

    autoImports();

    enumDeclaration(
      annotation(GENERATED, annotationValue(s(GENERATOR))),
      PUBLIC, name("Property"),
      implementsClause(PROPERTY_NAME),

      include(this::constants),

      field(PRIVATE, FINAL, STRING, name("propertyName")),

      constructor(
        PRIVATE,
        parameter(STRING, name("propertyName")),
        p(THIS, n("propertyName"), IS, n("propertyName"))
      ),

      method(
        annotation(OVERRIDE),
        PUBLIC, FINAL, STRING, name("toString"),
        p(RETURN, n("propertyName"))
      )
    );
  }

  private void constants() {
    spec.properties().stream()
        .sorted(Property.ORDER_BY_CONSTANT_NAME)
        .forEach(this::constantsImpl);
  }

  private void constantsImpl(Property property) {
    enumConstant(name(property.constantName), argument(s(property.propertyName)));
  }

}
