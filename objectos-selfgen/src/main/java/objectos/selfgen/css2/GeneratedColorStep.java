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

final class GeneratedColorStep extends ThisTemplate {

  private ColorValue colorValue;

  @Override
  protected final void definition() {
    packageDeclaration(CSS_UTIL);

    autoImports();

    classDeclaration(
      annotation(GENERATED, annotationValue(s(GENERATOR))),
      ABSTRACT, name("GeneratedColor"),

      include(this::colors)
    );
  }

  @Override
  final void writeHook(JavaSink sink) throws IOException {
    colorValue = spec.colorValue();

    if (colorValue != null) {
      super.writeHook(sink);
    }
  }

  private void colors() {
    colorValue.names.stream()
        .sorted((self, that) -> (self.constantName().compareTo(that.constantName())))
        .forEach(name -> field(
          PUBLIC, STATIC, FINAL, COLOR, name(name.constantName()),
          COLOR, v("named"), argument(s(name.colorName()))
        ));
  }

}
