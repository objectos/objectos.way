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
package objectos.selfgen.css.util;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import objectos.code.ClassName;

final class PrefixClassStep extends ThisTemplate {

  private Prefix prefix;

  public PrefixClassStep(CssUtilSelfGen spec) {
    super(spec);
  }

  @Override
  public final void writeTo(Path directory) throws IOException {
    List<Prefix> prefixList;
    prefixList = spec.prefixList;

    for (var prefix : prefixList) {
      this.prefix = prefix;

      super.writeTo(directory);
    }
  }

  @Override
  final String contents() {
    className(prefix.className);

    return code."""
    /*
     * Copyright (C) 2016-2023 Objectos Software LTDA.
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
    \{GENERATED_MSG}
    public final class \{simpleName} {

      private \{simpleName}() {}

    \{propertyTypes()}
    }
    """;
  }

  private String propertyTypes() {
    List<String> types;
    types = new ArrayList<>();

    List<PropertyClass> properties;
    properties = prefix.propertyClassList;

    for (var property : properties) {
      List<String> fields;
      fields = new ArrayList<>();

      List<StyleMethod> styles;
      styles = property.styleMethodList;

      for (var style : styles) {
        fields.add(
          code."""
              public static final \{CLASS_SELECTOR} \{style.constantName} = \{CLASS_SELECTOR}.next();
          """
        );
      }

      String constants;
      constants = fields.stream().collect(Collectors.joining("\n"));

      ClassName cn;
      cn = property.className;

      types.add(
        code."""
          public static final class \{cn} {
        \{constants}

            private \{cn.simpleName()}() {}
          }
        """
      );
    }

    return types.stream().collect(Collectors.joining("\n"));
  }

}