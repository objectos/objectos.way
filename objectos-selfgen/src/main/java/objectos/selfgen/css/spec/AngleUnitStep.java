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

final class AngleUnitStep extends ThisTemplate {

  private static final ClassTypeName ANGLE_UNIT = ClassTypeName.of(type, "AngleUnit");

  private static final ArrayTypeName ANGLE_UNIT_DIM = ArrayTypeName.of(ANGLE_UNIT);

  private final Set<String> simpleNames = new TreeSet<>();

  AngleUnitStep(StepAdapter adapter) { super(adapter); }

  @Override
  public final void addAngleUnit(String unit) {
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
      PUBLIC, name(ANGLE_UNIT),

      include(this::constants),

      field(PRIVATE, STATIC, FINAL, ANGLE_UNIT_DIM, name("ARRAY"), ANGLE_UNIT, v("values")),

      field(PRIVATE, FINAL, STRING, name("name")),

      constructor(
        PRIVATE,

        p(THIS, n("name"), IS, v("name"), v("toLowerCase"), argument(LOCALE, n("US")))
      ),

      method(
        PUBLIC, STATIC, ANGLE_UNIT, name("getByCode"),
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
    for (var unit : simpleNames) {
      enumConstant(name(unit));
    }
  }

}
