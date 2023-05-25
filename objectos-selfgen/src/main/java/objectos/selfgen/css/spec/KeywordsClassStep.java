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
import objectos.util.GrowableList;

final class KeywordsClassStep extends ThisTemplate {

  private static final ClassTypeName STD_KW = ClassTypeName.of(keyword, "StandardKeyword");

  private static final ArrayTypeName STD_KW_DIM = ArrayTypeName.of(STD_KW);

  private final List<KeywordName> keywordList = new GrowableList<>();

  public KeywordsClassStep(StepAdapter adapter) {
    super(adapter);
  }

  @Override
  public final void addKeyword(KeywordName keyword) {
    keywordList.add(keyword);
  }

  @Override
  public final void execute() {
    writeSelf();
  }

  @Override
  protected final void definition() {
    var unmodMap = ParameterizedTypeName.of(UNMODIFIABLE_MAP, STRING, STD_KW);

    packageDeclaration(keyword);

    autoImports();

    classDeclaration(
      PUBLIC, FINAL, name("Keywords"),

      include(this::keywords),

      field(
        PRIVATE, STATIC, FINAL, STD_KW_DIM, name("ARRAY"),
        arrayInitializer(), include(this::constants)
      ),

      field(PRIVATE, STATIC, FINAL, unmodMap, name("MAP"), v("buildMap")),

      constructor(PRIVATE),

      method(
        PUBLIC, STATIC, STD_KW, name("getByCode"),
        parameter(INT, name("code")),
        p(RETURN, n("ARRAY"), dim(n("code")))
      ),

      method(
        PUBLIC, STATIC, STD_KW, name("getByName"),
        parameter(STRING, name("name")),
        p(VAR, name("k"), n("MAP"), v("get"), argument(n("name"))),
        p(IF, condition(n("k"), EQ, NULL), block(
          p(THROW, NEW, IAE, argument(n("name")))
        )),
        p(RETURN, n("k"))
      ),

      method(
        PUBLIC, STATIC, BOOLEAN, name("isKeyword"),
        parameter(STRING, name("name")),
        p(RETURN, n("MAP"), v("containsKey"), argument(n("name")))
      ),

      method(
        PRIVATE, STATIC, unmodMap, name("buildMap"),
        p(VAR, name("m"), NEW, ParameterizedTypeName.of(GROWABLE_MAP, STRING, STD_KW)),
        include(this::mapStatements),
        p(RETURN, n("m"), v("toUnmodifiableMap"))
      )
    );
  }

  private void mapStatements() {
    for (var kw : keywordList) {
      p(n("m"), v("put"), argument(s(kw.name)), argument(n(kw.fieldName)));
    }
  }

  private void constants() {
    code(NL);

    for (var kw : keywordList) {
      code(value(n(kw.fieldName)), NL);
    }
  }

  private void keywords() {
    for (var kw : keywordList) {
      var typeName = ClassTypeName.of(keyword, kw.simpleName);

      field(
        PUBLIC, STATIC, FINAL, typeName, name(kw.fieldName), typeName, n("INSTANCE")
      );
    }
  }

}
