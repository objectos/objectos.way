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

import objectos.code.ClassTypeName;

final class KeywordClassStep extends ThisTemplate {

  private static final ClassTypeName STD_KEYWORD = ClassTypeName.of(keyword, "StandardKeyword");

  private int code;

  private KeywordName keywordName;

  public KeywordClassStep(StepAdapter adapter) {
    super(adapter);
  }

  @Override
  public final void addKeyword(KeywordName keyword) {
    keywordName = keyword;

    writeSelf();
  }

  @Override
  protected final void definition() {
    var self = ClassTypeName.of(keyword, keywordName.simpleName);

    packageDeclaration(keyword);

    autoImports();

    classDeclaration(
      PUBLIC, FINAL, name(self),
      extendsClause(STD_KEYWORD),
      include(this::superInterfaces),

      field(STATIC, FINAL, self, name("INSTANCE"), NEW, self),

      constructor(
        PRIVATE,

        p(SUPER,
          argument(i(code++)),
          argument(s(keywordName.fieldName)),
          argument(s(keywordName.name))
        )
      )
    );
  }

  private void superInterfaces() {
    var names = keywordName.interfaceSet;

    for (var name : names) {
      var typeName = ClassTypeName.of(type, name);

      implementsClause(typeName);
    }
  }

}
