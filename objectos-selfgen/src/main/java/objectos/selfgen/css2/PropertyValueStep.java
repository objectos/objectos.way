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
import objectos.code.JavaSink;

final class PropertyValueStep extends ThisTemplate {

  private ValueType valueType;

  @Override
  protected final void definition() {
    packageDeclaration(CSS_TMPL);

    autoImports();

    interfaceDeclaration(
      annotation(GENERATED, annotationValue(s(GENERATOR))),
      PUBLIC, SEALED, name(valueType.className),
      extendsClause(PROPERTY_VALUE),
      permitsClause(NAMED_ELEMENT)
    );
  }

  @Override
  final void writeHook(JavaSink sink) throws IOException {
    for (var valueType : spec.valueTypes()) {
      this.valueType = valueType;

      super.writeHook(sink);
    }
  }

}
