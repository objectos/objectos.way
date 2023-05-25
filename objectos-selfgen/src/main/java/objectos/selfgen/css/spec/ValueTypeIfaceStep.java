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

final class ValueTypeIfaceStep extends ThisTemplate {

  ValueType valueType;

  ValueTypeIfaceStep(StepAdapter adapter) {
    super(adapter);
  }

  @Override
  public final void addValueType(ValueType type) {
    valueType = type;

    writeSelf();
  }

  @Override
  protected final void definition() {
    packageDeclaration(type);

    autoImports();

    interfaceDeclaration(
      PUBLIC, name(valueType.simpleName),
      include(this::superInterfaces)
    );
  }

  private void superInterfaces() {
    for (var name : valueType.interfaceNames()) {
      var typeName = ClassTypeName.of(type, name);

      extendsClause(typeName);
    }
  }

}
