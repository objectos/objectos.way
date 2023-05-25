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
import objectos.code.ParameterizedTypeName;

final class GeneratedColorStep extends ThisTemplate {

  private static final ClassTypeName COLOR_NAME = ClassTypeName.of(type, "ColorName");

  private static final ArrayTypeName COLOR_NAME_DIM = ArrayTypeName.of(COLOR_NAME);

  private final Set<ColorName> colorNames = new TreeSet<>();

  GeneratedColorStep(StepAdapter adapter) {
    super(adapter);
  }

  @Override
  public final void addColorName(ColorName colorName) {
    colorName.assertIdentifierIsEqualToName();

    colorNames.add(colorName);
  }

  @Override
  public final void execute() {
    writeSelf();
  }

  @Override
  protected final void definition() {
    var unmodMap = ParameterizedTypeName.of(UNMODIFIABLE_MAP, STRING, COLOR_NAME);

    packageDeclaration(type);

    autoImports();

    classDeclaration(
      ABSTRACT, name("GeneratedColor"),

      include(this::colorFields),

      field(
        PRIVATE, STATIC, FINAL, COLOR_NAME_DIM, name("ARRAY"),
        arrayInitializer(), include(this::constantNames)
      ),

      field(
        PRIVATE, STATIC, FINAL, unmodMap, name("MAP"), v("buildMap")
      ),

      method(
        PUBLIC, STATIC, COLOR_NAME, name("getByCode"),
        parameter(INT, name("code")),
        p(RETURN, n("ARRAY"), dim(n("code")))
      ),

      method(
        PUBLIC, STATIC, COLOR_NAME, name("getByName"),
        parameter(STRING, name("name")),
        p(VAR, name("c"), n("MAP"), v("get"), argument(n("name"))),
        p(IF, condition(n("c"), EQ, NULL), block(
          p(THROW, NEW, IAE, argument(n("name")))
        )),
        p(RETURN, n("c"))
      ),

      method(
        PUBLIC, STATIC, BOOLEAN, name("isColor"),
        parameter(STRING, name("name")),
        p(RETURN, n("MAP"), v("containsKey"), argument(n("name")))
      ),

      method(
        PRIVATE, STATIC, unmodMap, name("buildMap"),
        p(VAR, name("m"), NEW, ParameterizedTypeName.of(GROWABLE_MAP, STRING, COLOR_NAME)),
        include(this::mapStatements),
        p(RETURN, n("m"), v("toUnmodifiableMap"))
      )
    );
  }

  private void colorFields() {
    int code = 0;

    for (var color : colorNames) {
      field(
        PUBLIC, STATIC, FINAL, COLOR_NAME,
        name(color.identifier),
        NEW, COLOR_NAME, argument(i(code++)), argument(s(color.name))
      );
    }
  }

  private void constantNames() {
    code(NL);

    for (var color : colorNames) {
      code(value(n(color.name)), NL);
    }
  }

  private void mapStatements() {
    for (var color : colorNames) {
      p(n("m"), v("put"), argument(s(color.name)), argument(n(color.identifier)));
    }
  }

}