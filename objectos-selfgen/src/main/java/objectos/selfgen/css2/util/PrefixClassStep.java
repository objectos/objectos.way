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
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import objectos.code.JavaSink;
import objectos.util.UnmodifiableList;

final class PrefixClassStep extends ThisTemplate {

  private Prefix prefix;

  private List<Property> properties;

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
    Map<Prefix, List<Property>> map;
    map = spec.properties;

    Set<Entry<Prefix, List<Property>>> entries;
    entries = map.entrySet();

    for (var entry : entries) {
      prefix = entry.getKey();

      properties = entry.getValue();

      super.writeHook(sink);
    }
  }

  private void propertyTypes() {
    for (var property : properties) {
      propertyTypes0(property);
    }
  }

  private void propertyTypes0(Property property) {
    classDeclaration(
      PUBLIC, STATIC, FINAL, name(property.className),

      include(() -> propertyTypes1Fields(property)),

      constructor(PRIVATE)
    );
  }

  private void propertyTypes1Fields(Property property) {
    UnmodifiableList<NamedArguments> names;
    names = property.names;

    for (var name : names) {
      field(
        PUBLIC, STATIC, FINAL, CLASS_SELECTOR, name(name.constantName),
        CLASS_SELECTOR, v("randomClassSelector"), argument(i(5))
      );
    }
  }

}