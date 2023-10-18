/*
 * Copyright 2023 Objectos Software LTDA.
 *
 * Reprodução parcial ou total proibida.
 */
package selfgen.css.util;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import objectos.code.ClassName;
import objectos.code.Code;

final class PropertyEnumStep extends ThisTemplate {

  private PropertyClass property;

  PropertyEnumStep(CssUtilSelfGen spec) {
    super(spec);
  }

  @Override
  public final void writeTo(Path directory) throws IOException {
    List<PropertyClass> properties;
    properties = spec.properties;

    for (var property : properties) {
      this.property = property;

      super.writeTo(directory);
    }
  }

  @Override
  final String contents() {
    ClassName cn;
    cn = ClassName.of(CSS_UTIL, property.simpleName);

    className(cn);

    return code."""
    /*
     * Copyright (C) 2023 Objectos Software LTDA.
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
    package \{packageName};
    \{importList}
    \{generateJavadoc()}
    \{GENERATED_MSG}
    \{generateProperty()}
    """;
  }

  final String generateJavadoc() {
    return property.javadoc(code);
  }

  final String generateProperty() {
    return property.generate(code, SelectorKind.STANDARD);
  }

}