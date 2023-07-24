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
package objectos.selfgen.css2.util;

import java.io.IOException;
import java.util.List;
import objectos.code.JavaSink;

final class PrefixClassStep extends ThisTemplate {

  private Prefix prefix;

  @Override
  protected final void definition() {
    packageDeclaration(CSS_UTIL);

    autoImports();

    classDeclaration(
      annotation(GENERATED, annotationValue(s(GENERATOR))),
      PUBLIC, FINAL, name(prefix.className),

      constructor(PRIVATE),

      include(this::propertyTypes)
    );
  }

  @Override
  final void writeHook(JavaSink sink) throws IOException {
    List<Prefix> prefixList;
    prefixList = spec.prefixList;

    for (var prefix : prefixList) {
      this.prefix = prefix;

      super.writeHook(sink);
    }
  }

  private void propertyTypes() {
    List<PropertyClass> properties;
    properties = prefix.propertyClassList;

    for (var property : properties) {
      propertyTypes0(property);
    }
  }

  private void propertyTypes0(PropertyClass property) {
    classDeclaration(
      PUBLIC, STATIC, FINAL, name(property.className),

      include(() -> propertyTypes1Fields(property)),

      constructor(PRIVATE)
    );
  }

  private void propertyTypes1Fields(PropertyClass property) {
    List<StyleMethod> styles;
    styles = property.styleMethodList;

    for (var style : styles) {
      field(
        PUBLIC, STATIC, FINAL, CLASS_SELECTOR, name(style.constantName),
        CLASS_SELECTOR, v("randomClassSelector"), argument(i(5))
      );
    }
  }

}