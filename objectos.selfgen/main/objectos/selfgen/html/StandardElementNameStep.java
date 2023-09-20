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
package objectos.selfgen.html;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

final class StandardElementNameStep extends ThisTemplate {

  public StandardElementNameStep(HtmlSelfGen spec) {
    super(spec);
  }

  @Override
  final String contents() {
    className(STD_ELEMENT_NAME);

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
    public enum \{simpleName} implements \{ELEMENT_NAME} {
    \{constants()}

      private static final \{simpleName}[] ARRAY = \{simpleName}.values();

      private final \{ELEMENT_KIND} kind;

      private final String name;

      private \{simpleName}(\{ELEMENT_KIND} kind, String name) {
        this.kind = kind;
        this.name = name;
      }

      public static \{simpleName} getByCode(int code) {
        return ARRAY[code];
      }

      public static int size() {
        return ARRAY.length;
      }

      @Override
      public final int getCode() {
        return ordinal();
      }

      @Override
      public final \{ELEMENT_KIND} getKind() {
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

    for (var element : spec.elements()) {
      String javaName;
      javaName = element.constantName;

      String kind;
      kind = element.hasEndTag() ? "NORMAL" : "VOID";

      String htmlName;
      htmlName = element.name();

      constants.add(
        code."  \{javaName}(\{ELEMENT_KIND}.\{kind}, \"\{htmlName}\")"
      );
    }

    return constants.stream().collect(Collectors.joining(",\n\n", "", ";"));
  }

}
