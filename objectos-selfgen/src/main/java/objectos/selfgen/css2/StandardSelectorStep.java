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

import java.io.IOException;
import objectos.code.ArrayTypeName;
import objectos.code.ClassTypeName;
import objectos.code.JavaSink;

final class StandardSelectorStep extends ThisTemplate {

  private SelectorKind kind;

  private ClassTypeName standardName;

  private ClassTypeName selectorName;

  @Override
  protected final void definition() {
    packageDeclaration(CSS_INTERNAL);

    autoImports();

    enumDeclaration(
      annotation(GENERATED, annotationValue(s(GENERATOR))),
      PUBLIC, name(standardName),
      implementsClause(selectorName),

      include(this::names),

      field(
        PRIVATE, STATIC, FINAL, ArrayTypeName.of(standardName), name("VALUES"),
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
        PUBLIC, STATIC, standardName, name("ofOrdinal"),
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

  @Override
  final void writeHook(JavaSink sink) throws IOException {
    execute(
      sink,
      SelectorKind.TYPE, STANDARD_TYPE_SELECTOR, TYPE_SELECTOR
    );

    execute(
      sink,
      SelectorKind.PSEUDO_CLASS, STANDARD_PSEUDO_CLASS_SELECTOR, PSEUDO_CLASS_SELECTOR
    );

    execute(
      sink,
      SelectorKind.PSEUDO_ELEMENT, STANDARD_PSEUDO_ELEMENT_SELECTOR, PSEUDO_ELEMENT_SELECTOR
    );
  }

  private void execute(
      JavaSink sink,
      SelectorKind kind, ClassTypeName standardName, ClassTypeName selectorName)
      throws IOException {
    this.kind = kind;

    this.standardName = standardName;

    this.selectorName = selectorName;

    super.writeHook(sink);
  }

  private void names() {
    spec.selectors().stream()
        .filter(s -> !s.disabled)
        .filter(s -> s.kind == kind)
        .sorted(SelectorName.ORDER_BY_FIELD_NAME)
        .forEach(name -> {
          enumConstant(name(name.fieldName), argument(s(name.selectorName)));
        });
  }

}
