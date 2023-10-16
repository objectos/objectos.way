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
import selfgen.css.util.PropertyClass.Constant1;

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
    \{property.javadoc}
    \{GENERATED_MSG}
    \{generateProperty()}
    """;
  }

  final String generateProperty() {
    return switch (property) {
      case PropertyClass.Standard std -> code."""
        public enum \{std.simpleName} implements \{STYLE_CLASS} {
        \{generatePropertyConstants(std)}
          private final String className = \{CLASS_SEQ_ID}.next();

          private final String value;

          private \{std.simpleName}(String value) {
            this.value = value;
          }

          /**
           * Returns the CSS class name.
           *
           * @return the CSS class name
           */
          @Override
          public final String className() {
            return className;
          }

          /**
           * Returns the CSS style rule represented by this utility class.
           *
           * @return the CSS style rule
           */
          @Override
          public final String toString() {
            return "." + className + " { \{std.propertyName}: " + value + " }";
          }

        }""";
    };
  }

  private String generatePropertyConstants(PropertyClass.Standard std) {
    List<String> result;
    result = new ArrayList<>();

    List<Constant1> constants;
    constants = std.constants;

    for (var cte : constants) {
      result.add("  " + cte.name() + "(\"" + Code.escape(cte.value()) + "\")");
    }

    return result.stream().collect(Collectors.joining(",\n\n", "\n", ";\n"));
  }

}