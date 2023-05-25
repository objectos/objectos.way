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
package objectos.selfgen.css.spec;

import java.util.Set;
import java.util.TreeSet;
import objectos.code.ArrayTypeName;
import objectos.code.ClassTypeName;

final class LengthUnitStep extends ThisTemplate {

  private static final ClassTypeName LENGTH_UNIT = ClassTypeName.of(type, "LengthUnit");

  private static final ArrayTypeName LENGTH_UNIT_DIM = ArrayTypeName.of(LENGTH_UNIT);

  private final Set<String> simpleNames = new TreeSet<>();

  LengthUnitStep(StepAdapter adapter) {
    super(adapter);
  }

  @Override
  public final void addLengthUnit(String unit) {
    var simpleName = unit.toUpperCase();

    simpleNames.add(simpleName);
  }

  @Override
  public final void execute() {
    writeSelf();
  }

  @Override
  protected final void definition() {
    packageDeclaration(type);

    autoImports();

    enumDeclaration(
      PUBLIC, name("LengthUnit"),

      include(this::constants),

      field(
        PRIVATE, STATIC, FINAL, LENGTH_UNIT_DIM, name("ARRAY"),
        LENGTH_UNIT, v("values")
      ),

      field(PRIVATE, FINAL, STRING, name("name")),

      constructor(
        PRIVATE,

        p(THIS, n("name"), IS, v("name"), v("toLowerCase"), argument(LOCALE, n("US")))
      ),

      method(
        PUBLIC, STATIC, LENGTH_UNIT, name("getByCode"),
        parameter(INT, name("code")),

        p(RETURN, n("ARRAY"), dim(n("code")))
      ),

      method(
        PUBLIC, STATIC, INT, name("size"),

        p(RETURN, n("ARRAY"), n("length"))
      ),

      method(
        PUBLIC, FINAL, INT, name("getCode"),

        p(RETURN, v("ordinal"))
      ),

      method(
        PUBLIC, FINAL, STRING, name("getName"),

        p(RETURN, n("name"))
      )
    );
  }

  private void constants() {
    for (var simpleName : simpleNames) {
      enumConstant(name(simpleName));
    }
  }

}
