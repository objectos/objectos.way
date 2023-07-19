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
import objectos.code.ClassTypeName;
import objectos.code.JavaSink;

final class UrlTypeStep extends ThisTemplate {

  private UrlType urlType;

  @Override
  protected final void definition() {
    packageDeclaration(CSS_TMPL);

    autoImports();

    interfaceDeclaration(
      annotation(GENERATED, annotationValue(s(GENERATOR))),
      PUBLIC, SEALED, name(URL),
      include(this::interfaces),
      permitsClause(INTERNAL_INSTRUCTION),
      permitsClause(INTERNAL_URL)
    );
  }

  @Override
  final void writeHook(JavaSink sink) throws IOException {
    urlType = spec.urlType();

    if (urlType != null) {
      super.writeHook(sink);
    }
  }

  private void interfaces() {
    urlType.interfaces.stream()
        .sorted((self, that) -> self.simpleName().compareTo(that.simpleName()))
        .forEach(this::interfacesImpl);
  }

  private void interfacesImpl(ClassTypeName className) {
    extendsClause(NL, className);
  }

}