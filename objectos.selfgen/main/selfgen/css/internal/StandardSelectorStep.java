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

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import objectos.code.ClassName;

final class StandardSelectorStep extends ThisTemplate {

  private SelectorKind kind;

  private ClassName standardName;

  private ClassName selectorName;

  public StandardSelectorStep(CssSelfGen spec) {
    super(spec);
  }

  @Override
  public final void writeTo(Path directory) throws IOException {
    execute(
      directory,
      SelectorKind.TYPE, STANDARD_TYPE_SELECTOR, SELECTOR_INSTRUCTION
    );

    execute(
      directory,
      SelectorKind.PSEUDO_CLASS, STANDARD_PSEUDO_CLASS_SELECTOR, SELECTOR_INSTRUCTION
    );

    execute(
      directory,
      SelectorKind.PSEUDO_ELEMENT, STANDARD_PSEUDO_ELEMENT_SELECTOR, SELECTOR_INSTRUCTION
    );
  }

  @Override
  final String contents() {
    className(standardName);

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
    public enum \{simpleName} implements \{selectorName} {
    \{constants()}

      private static final \{simpleName}[] VALUES = values();

      public final String cssName;

      private \{simpleName}(String cssName) {
        this.cssName = cssName;
      }

      public static \{simpleName} ofOrdinal(int ordinal) {
        return VALUES[ordinal];
      }

      @Override
      public final String toString() {
        return cssName;
      }
    }
    """;
  }

  private void execute(
      Path directory,
      SelectorKind kind, ClassName standardName, ClassName selectorName)
      throws IOException {
    this.kind = kind;

    this.standardName = standardName;

    this.selectorName = selectorName;

    super.writeTo(directory);
  }

  private String constants() {
    List<String> result;
    result = new ArrayList<>();

    Collection<SelectorName> unsorted;
    unsorted = spec.selectors.values();

    List<SelectorName> sorted;
    sorted = new ArrayList<>();

    for (var selector : unsorted) {
      if (selector.disabled) {
        continue;
      }

      if (selector.kind != kind) {
        continue;
      }

      sorted.add(selector);
    }

    sorted.sort(SelectorName.ORDER_BY_FIELD_NAME);

    for (int i = 0, size = sorted.size(); i < size; i++) {
      SelectorName selector;
      selector = sorted.get(i);

      String constantName;
      constantName = selector.fieldName;

      String selectorName;
      selectorName = selector.selectorName;

      result.add(
        code."  \{constantName}(\"\{selectorName}\")"
      );
    }

    return result.stream().collect(Collectors.joining(",\n\n", "", ";"));
  }

}
