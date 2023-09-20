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
package selfgen.css.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import objectos.code.ClassName;

final class PropertyStep extends ThisTemplate {

  public PropertyStep(CssSelfGen spec) {
    super(spec);
  }

  @Override
  final String contents() {
    className(
      ClassName.of(CSS_INTERNAL, "Property")
    );

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
    public enum \{simpleName} {
    \{constants()}

      private static final \{simpleName}[] VALUES = values();

      public final String cssName;

      private \{simpleName}(String cssName) {
        this.cssName = cssName;
      }

      public static \{simpleName} byOrdinal(int ordinal) {
        return VALUES[ordinal];
      }

      @Override
      public final String toString() {
        return cssName;
      }
    }
    """;
  }

  private String constants() {
    List<String> result;
    result = new ArrayList<>();

    Collection<Property> unsorted;
    unsorted = spec.properties.values();

    ArrayList<Property> sorted;
    sorted = new ArrayList<>(unsorted);

    sorted.sort(Property.ORDER_BY_CONSTANT_NAME);

    for (int i = 0, size = sorted.size(); i < size; i++) {
      Property property;
      property = sorted.get(i);

      String constantName;
      constantName = property.constantName;

      String propertyName;
      propertyName = property.propertyName;

      result.add(
        code."  \{constantName}(\"\{propertyName}\")"
      );
    }

    return result.stream().collect(Collectors.joining(",\n\n", "", ";"));
  }

}
