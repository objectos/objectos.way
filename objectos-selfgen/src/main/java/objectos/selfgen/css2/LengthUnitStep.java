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
import java.util.Locale;
import objectos.code.ArrayTypeName;
import objectos.code.JavaSink;

final class LengthUnitStep extends ThisTemplate {

  private LengthType lengthType;

  @Override
  protected final void definition() {
    packageDeclaration(CSS_INTERNAL);

    autoImports();

    enumDeclaration(
      annotation(GENERATED, annotationValue(s(GENERATOR))),
      PUBLIC, name(LENGTH_UNIT),

      include(this::lengthUnits),

      field(
        PRIVATE, STATIC, FINAL, ArrayTypeName.of(LENGTH_UNIT), name("VALUES"),
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
        PUBLIC, STATIC, LENGTH_UNIT, name("byOrdinal"),
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
    lengthType = spec.lengthType;

    if (lengthType != null) {
      super.writeHook(sink);
    }
  }

  private void lengthUnits() {
    lengthType.units.stream()
        .sorted()
        .forEach(unit -> enumConstant(name(unit.toUpperCase(Locale.US)), argument(s(unit))));
  }

}
