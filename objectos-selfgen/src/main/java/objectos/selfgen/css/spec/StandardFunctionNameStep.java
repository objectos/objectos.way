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

import java.util.Map;
import java.util.TreeMap;
import objectos.code.ArrayTypeName;
import objectos.code.ClassTypeName;
import objectos.code.ParameterizedTypeName;

final class StandardFunctionNameStep extends ThisTemplate {

  private static final ClassTypeName FUNCTION_NAME = ClassTypeName.of(function, "FunctionName");

  private static final ClassTypeName STD_FN = ClassTypeName.of(function, "StandardFunctionName");

  private static final ArrayTypeName STD_FN_DIM = ArrayTypeName.of(STD_FN);

  private final Map<String, FunctionName> nameMap = new TreeMap<>();

  public StandardFunctionNameStep(StepAdapter adapter) {
    super(adapter);
  }

  @Override
  public final void addFunction(FunctionName function) {
    var key = function.getName();

    nameMap.put(key, function);
  }

  @Override
  public final void execute() {
    writeSelf();
  }

  @Override
  protected final void definition() {
    var unmodMap = ParameterizedTypeName.of(UNMODIFIABLE_MAP, STRING, STD_FN);

    packageDeclaration(function);

    autoImports();

    enumDeclaration(
      PUBLIC, name(STD_FN), implementsClause(FUNCTION_NAME),

      include(this::enumConstants),

      field(PRIVATE, STATIC, FINAL, STD_FN_DIM, name("ARRAY"), STD_FN, v("values")),

      field(PRIVATE, STATIC, FINAL, unmodMap, name("MAP"), v("buildMap")),

      field(PRIVATE, FINAL, STRING, name("javaName")),

      field(PRIVATE, FINAL, STRING, name("name")),

      constructor(
        PRIVATE,
        parameter(STRING, name("javaName")),
        parameter(STRING, name("name")),

        p(THIS, n("javaName"), IS, n("javaName")),
        p(THIS, n("name"), IS, n("name"))
      ),

      method(
        PUBLIC, STATIC, STD_FN, name("getByCode"),
        parameter(INT, name("code")),
        p(RETURN, n("ARRAY"), dim(n("code")))
      ),

      method(
        PUBLIC, STATIC, STD_FN, name("getByName"),
        parameter(STRING, name("name")),
        p(RETURN, n("MAP"), v("get"), argument(n("name")))
      ),

      method(
        PRIVATE, STATIC, unmodMap, name("buildMap"),
        p(VAR, name("m"), NEW, ParameterizedTypeName.of(GROWABLE_MAP, STRING, STD_FN)),
        include(this::mapStatements),
        p(RETURN, n("m"), v("toUnmodifiableMap"))
      ),

      method(
        PUBLIC, STATIC, INT, name("size"),
        p(RETURN, n("ARRAY"), n("length"))
      ),

      method(
        annotation(Override.class),
        PUBLIC, FINAL, INT, name("getCode"),
        p(RETURN, v("ordinal"))
      ),

      method(
        PUBLIC, FINAL, STRING, name("getJavaName"),
        p(RETURN, n("javaName"))
      ),

      method(
        annotation(Override.class),
        PUBLIC, FINAL, STRING, name("getName"),
        p(RETURN, n("name"))
      )
    );
  }

  private void enumConstants() {
    for (var name : nameMap.values()) {
      enumConstant(
        name(name.enumName()),
        argument(s(name.methodName())),
        argument(s(name.getName()))
      );
    }
  }

  private void mapStatements() {
    for (var name : nameMap.values()) {
      p(n("m"), v("put"), argument(s(name.getName())), argument(n(name.enumName())));
    }
  }

}