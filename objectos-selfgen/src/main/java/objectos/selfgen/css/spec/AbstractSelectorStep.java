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

import java.util.List;
import objectos.code.ArrayTypeName;
import objectos.code.ClassTypeName;
import objectos.code.ParameterizedTypeName;
import objectos.selfgen.util.JavaNames;
import objectos.util.GrowableList;

abstract class AbstractSelectorStep extends ThisTemplate {

  private record Element(String name, String fieldName) {}

  private final List<Element> elementList = new GrowableList<>();

  AbstractSelectorStep(StepAdapter adapter) {
    super(adapter);
  }

  @Override
  public final void execute() {
    writeSelf();
  }

  @Override
  protected final void definition() {
    var implName = getImplName();

    var implArray = ArrayTypeName.of(implName);

    var unmodMap = ParameterizedTypeName.of(UNMODIFIABLE_MAP, STRING, implName);

    packageDeclaration(select);

    autoImports();

    classDeclaration(
      PUBLIC, FINAL, name(getGeneratedName()),

      include(this::fields),

      field(
        PRIVATE, STATIC, FINAL, implArray, name("ARRAY"),
        arrayInitializer(), include(this::constantNames)
      ),

      field(PRIVATE, STATIC, FINAL, unmodMap, name("MAP"), v("buildMap")),

      constructor(PRIVATE),

      method(
        PUBLIC, STATIC, implName, name("getByCode"),
        parameter(INT, name("code")),
        p(RETURN, n("ARRAY"), dim(n("code")))
      ),

      method(
        PUBLIC, STATIC, implName, name("getByName"),
        parameter(STRING, name("name")),
        p(RETURN, n("MAP"), v("get"), argument(n("name")))
      ),

      method(
        PRIVATE, STATIC, unmodMap, name("buildMap"),
        p(VAR, name("m"), NEW, ParameterizedTypeName.of(GROWABLE_MAP, STRING, implName)),
        include(this::mapStatements),
        p(RETURN, n("m"), v("toUnmodifiableMap"))
      )
    );
  }

  final void addPseudo(String name, String fieldName) {
    var element = new Element(name, fieldName);

    elementList.add(element);
  }

  abstract String getGeneratedName();

  abstract ClassTypeName getImplName();

  final String toFieldName(String simpleName) {
    String fieldName;
    fieldName = simpleName.replace('-', '_').toUpperCase();

    return JavaNames.toIdentifier(fieldName);
  }

  private void constantNames() {
    code(NL);

    for (var element : elementList) {
      value(n(element.fieldName), NL);
    }
  }

  private void fields() {
    var implTypeName = getImplName();

    int code = 0;

    for (var element : elementList) {
      field(
        PUBLIC, STATIC, FINAL, implTypeName,
        name(element.fieldName),
        NEW, implTypeName, argument(i(code++)), argument(s(element.name))
      );
    }
  }

  private void mapStatements() {
    for (var element : elementList) {
      p(n("m"), v("put"), argument(s(element.name)), argument(n(element.fieldName)));
    }
  }

}
