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
package objectos.selfgen.html.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import objectos.code.ClassName;
import objectos.code.Code;
import objectos.code.Code.ImportList;

final class StandardAttributeNameStep extends ThisTemplate {

  public StandardAttributeNameStep(HtmlSelfGen spec) {
    super(spec);
  }

  @Override
  final String contents() {
    className(STD_ATTR_NAME);

    return code."""
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
    package \{packageName};
    \{importList}
    /**
     * TODO
     */
    \{GENERATED_MSG}
    public enum \{simpleName} implements \{ATTRIBUTE_NAME} {
    \{constants()}

      private static final \{className}[] ARRAY = \{className}.values();

      private static final \{UNMODIFIABLE_MAP}<String, \{className}> MAP = mapInit();

      private final \{ATTRIBUTE_KIND} kind;

      private final String name;

      StandardAttributeName(\{ATTRIBUTE_KIND} kind, String name) {
        this.kind = kind;
        this.name = name;
      }

      public static \{className} getByCode(int code) {
        return ARRAY[code];
      }

      public static \{className} getByName(String name) {
        return MAP.get(name);
      }

      public static int size() {
        return ARRAY.length;
      }

      private static \{UNMODIFIABLE_MAP}<String, \{className}> mapInit() {
        var builder = new NamesBuilder();
    \{mapInit()}
        return builder.build();
      }

      @Override
      public final int getCode() {
        return ordinal();
      }

      @Override
      public final \{ATTRIBUTE_KIND} getKind() {
        return kind;
      }

      @Override
      public final String getName() {
        return name;
      }
    }
    """;
  }

  private String constants() {
    List<String> constants;
    constants = new ArrayList<>();

    for (var attribute : spec.attributes()) {
      String javaName;
      javaName = attribute.constantName;

      String kind;
      kind = attribute.kind().name();

      String htmlName;
      htmlName = attribute.name();

      constants.add(
        code."  \{javaName}(\{ATTRIBUTE_KIND}.\{kind}, \"\{htmlName}\")"
      );
    }

    return constants.stream().collect(Collectors.joining(",\n\n", "", ";"));
  }

  private String mapInit() {
    StringBuilder sb;
    sb = new StringBuilder();

    for (var attribute : spec.attributes()) {
      String name;
      name = attribute.name();

      String constantName;
      constantName = attribute.constantName;

      sb.append(
        code."""
            builder.put("\{name}", \{constantName});
        """
      );
    }

    return sb.toString();
  }

}
