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

final class FunctionStep extends ThisTemplate {

  @Override
  protected final void definition() {
    packageDeclaration(CSS_INTERNAL);

    autoImports();

    enumDeclaration(
      annotation(GENERATED, annotationValue(s(GENERATOR))),
      PUBLIC, name(FUNCTION),

      include(this::constants),

      field(
        PRIVATE, STATIC, FINAL, ArrayTypeName.of(FUNCTION), name("VALUES"),
        v("values")
      ),

      field(PUBLIC, FINAL, STRING, name("cssName")),

      constructor(
        PRIVATE,
        parameter(STRING, name("cssName")),
        p(THIS, n("cssName"), IS, n("cssName"))
      ),

      method(
        PUBLIC, STATIC, FUNCTION, name("byOrdinal"),
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

  private void constants() {
    spec.functions.values().stream()
        .sorted(Function.ORDER_BY_CONSTANT_NAME)
        .forEach(this::constantsImpl);
  }

  private void constantsImpl(Function function) {
    enumConstant(name(function.constantName), argument(s(function.functionName)));
  }

}