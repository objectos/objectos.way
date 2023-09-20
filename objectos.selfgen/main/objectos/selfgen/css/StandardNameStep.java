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
package objectos.selfgen.css;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

final class StandardNameStep extends ThisTemplate {

  public StandardNameStep(CssSelfGen spec) {
    super(spec);
  }

  @Override
  final String contents() {
    className(STANDARD_NAME);

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
    public enum \{simpleName} implements \{superInterfaces()} {
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

  private String superInterfaces() {
    StringBuilder sb;
    sb = new StringBuilder();

    sb.append(code."\{SELECTOR_INSTRUCTION}, \{COLOR_VALUE}");

    Optional<ValueType> maybeValues;
    maybeValues = spec.valueTypes.values().stream()
        .filter(ValueType::permitsStandardName)
        .findAny();

    if (maybeValues.isPresent()) {
      sb.append(code.", \{VALUE_INSTRUCTION}");
    }

    Optional<KeywordName> maybeKeywords;
    maybeKeywords = spec.keywords.values().stream()
        .filter(KeywordName::shouldGenerate)
        .findAny();

    if (maybeKeywords.isPresent()) {
      sb.append(code.", \{KEYWORD_INSTRUCTION}");
    }

    return sb.toString();
  }

  private String constants() {
    List<String> result;
    result = new ArrayList<>();

    // universal selector
    result.add("  any(\"*\")");

    ColorValue colorValue;
    colorValue = spec.colorValue;

    if (colorValue != null) {
      Collection<ColorName> unsorted;
      unsorted = colorValue.names;

      List<ColorName> sorted;
      sorted = new ArrayList<>(unsorted);

      Comparator<ColorName> byFieldName;
      byFieldName = (self, that) -> (self.fieldName().compareTo(that.fieldName()));

      sorted.sort(byFieldName);

      for (int i = 0, size = sorted.size(); i < size; i++) {
        ColorName color;
        color = sorted.get(i);

        result.add(
          code."  \{color.fieldName()}(\"\{color.colorName()}\")"
        );
      }
    }

    {
      Collection<KeywordName> unsorted;
      unsorted = spec.keywords.values();

      List<KeywordName> sorted;
      sorted = new ArrayList<>(unsorted);

      sorted.sort(KeywordName.ORDER_BY_FIELD_NAME);

      for (int i = 0, size = sorted.size(); i < size; i++) {
        KeywordName kw;
        kw = sorted.get(i);

        result.add(
          code."  \{kw.fieldName}(\"\{kw.keywordName}\")"
        );
      }
    }

    return result.stream().collect(Collectors.joining(",\n\n", "", ";"));
  }

}
