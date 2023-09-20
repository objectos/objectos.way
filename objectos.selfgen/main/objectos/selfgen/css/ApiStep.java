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
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import objectos.code.ClassName;

final class ApiStep extends ThisTemplate {

  public ApiStep(CssSelfGen spec) {
    super(spec);
  }

  @Override
  final String contents() {
    className(API);

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
    \{body()}
    }
    """;
  }

  private String body() {
    List<String> result;
    result = new ArrayList<>();

    result.add(code."""
      private Api() {}
    """);

    style(result);

    selectors(result);

    valueTypes(result);

    functions(result);

    keywords(result);

    colorValue(result);

    lengthType(result);

    percentageType(result);

    flexValue(result);

    literalTypes(result);

    url(result);

    zero(result);

    return result.stream().collect(Collectors.joining("\n", "\n", ""));
  }

  private void style(List<String> result) {
    result.add(code."""
      public sealed interface \{MEDIA_RULE_ELEM.simpleName()} {}
    """);

    result.add(code."""
      public sealed interface \{MEDIA_FEATURE.simpleName()} extends \{MEDIA_RULE_ELEM} {}
    """);

    result.add(code."""
      public sealed interface MediaFeatureOrStyleDeclaration extends \{MEDIA_FEATURE}, \{STYLE_DECLARATION} permits \{INTERNAL_INSTRUCTION} {}
    """);

    ClassName MEDIA_TYPE;
    MEDIA_TYPE = ClassName.of(CSS_INTERNAL, "MediaType");

    result.add(code."""
      public sealed interface MediaQuery extends \{MEDIA_RULE_ELEM} permits \{MEDIA_TYPE} {}
    """);

    result.add(code."""
      public sealed interface \{STYLE_RULE.simpleName()} extends \{MEDIA_RULE_ELEM} permits \{INTERNAL_INSTRUCTION} {}
    """);

    result.add(code."""
      public sealed interface \{STYLE_RULE_ELEM.simpleName()} {}
    """);

    result.add(code."""
      public sealed interface \{STYLE_DECLARATION.simpleName()} extends \{STYLE_RULE_ELEM} {}
    """);

    FilterFunction filterFunction;
    filterFunction = spec.filterFunction;

    if (filterFunction != null) {
      result.add(code."""
        public sealed interface \{FILTER_FUNCTION.simpleName()} {}
      """);
    }

    List<String> superTypes;
    superTypes = new ArrayList<>();

    for (var property : spec.properties.values()) {
      if (property.isHash()) {
        ClassName className;
        className = property.declarationClassName;

        superTypes.add(code."\{className}");

        result.add(code."""
          public sealed interface \{className.simpleName()} extends \{STYLE_DECLARATION} {}
        """);

        className = property.hashClassName;

        superTypes.add(code."\{className}");

        result.add(code."""
          public sealed interface \{className.simpleName()} extends \{STYLE_DECLARATION} {}
        """);
      }

      else if (property.filterFunction) {
        ClassName className;
        className = property.declarationClassName;

        superTypes.add(code."\{className}");

        result.add(code."""
          public sealed interface \{className.simpleName()} extends \{FILTER_FUNCTION}, \{STYLE_DECLARATION} {}
        """);
      }
    }

    String extendsClause;
    extendsClause = superTypes.stream().collect(Collectors.joining(", "));

    result.add(code."""
      public sealed interface \{STYLE_DECL_INST.simpleName()} extends \{extendsClause} permits \{INTERNAL_INSTRUCTION} {}
    """);
  }

  private void selectors(List<String> result) {
    result.add(code."""
      public sealed interface \{SELECTOR.simpleName()} extends \{STYLE_RULE_ELEM} {}
    """);

    ClassName COMBINATOR;
    COMBINATOR = ClassName.of(CSS_INTERNAL, "Combinator");

    ClassName CLASS_SELECTOR;
    CLASS_SELECTOR = ClassName.of(CSS_UTIL, "ClassSelector");

    ClassName ID_SELECTOR;
    ID_SELECTOR = ClassName.of(CSS_UTIL, "IdSelector");

    result.add(code."""
      public sealed interface \{SELECTOR_INSTRUCTION.simpleName()} extends \{SELECTOR} permits \{COMBINATOR}, \{INTERNAL_INSTRUCTION}, \{STANDARD_NAME}, \{CLASS_SELECTOR}, \{ID_SELECTOR}, \{STANDARD_PSEUDO_CLASS_SELECTOR}, \{STANDARD_PSEUDO_ELEMENT_SELECTOR}, \{STANDARD_TYPE_SELECTOR} {}
    """);
  }

  private void valueTypes(List<String> result) {
    result.add(code."""
      public sealed interface \{PROPERTY_VALUE.simpleName()} {}
    """);

    Collection<ValueType> valueTypes;
    valueTypes = spec.valueTypes.values();

    List<String> superTypes;
    superTypes = new ArrayList<>();

    for (var valueType : valueTypes) {
      ClassName className;
      className = valueType.className;

      superTypes.add(code."      \{className}");

      Collection<ClassName> types;
      types = valueType.superTypes();

      String extendsClause;

      if (types.isEmpty()) {
        extendsClause = code."\{PROPERTY_VALUE}";
      } else {
        extendsClause = extendsClause(types);
      }

      result.add(code."""
        public sealed interface \{className.simpleName()} extends \{extendsClause} {}
      """);
    }

    if (superTypes.isEmpty()) {
      return;
    }

    String extendsClause;
    extendsClause = superTypes.stream().collect(Collectors.joining(",\n"));

    result.add(code."""
      public sealed interface \{VALUE_INSTRUCTION.simpleName()} extends
    \{extendsClause} permits \{STANDARD_NAME} {}
    """);
  }

  private String extendsClause(Collection<ClassName> types) {
    List<ClassName> sorted;
    sorted = new ArrayList<>(types);

    Comparator<ClassName> bySimpleName;
    bySimpleName = (self, that) -> self.simpleName().compareTo(that.simpleName());

    sorted.sort(bySimpleName);

    List<String> names;
    names = new ArrayList<>();

    for (int i = 0, size = sorted.size(); i < size; i++) {
      ClassName cn;
      cn = sorted.get(i);

      names.add(code."\{cn}");
    }

    return names.stream().collect(Collectors.joining(", "));
  }

  private void functions(List<String> result) {
    List<String> superTypes;
    superTypes = new ArrayList<>();

    Collection<Function> functions;
    functions = spec.functions.values();

    for (var function : functions) {
      ClassName className;
      className = function.className;

      superTypes.add(code."      \{className}");

      String extendsClause;
      extendsClause = extendsClause(function.interfaces);

      result.add(code."""
        public sealed interface \{className.simpleName()} extends \{extendsClause} {}
      """);
    }

    if (superTypes.isEmpty()) {
      return;
    }

    String extendsClause;
    extendsClause = superTypes.stream().collect(Collectors.joining(",\n"));

    result.add(code."""
      public sealed interface \{FUNCTION_INSTRUCTION.simpleName()} extends
    \{extendsClause} permits \{INTERNAL_INSTRUCTION} {}
    """);
  }

  private void keywords(List<String> result) {
    Iterator<KeywordName> iterator;
    iterator = spec.keywords.values().stream()
        .filter(KeywordName::shouldGenerate)
        .iterator();

    List<String> superTypes;
    superTypes = new ArrayList<>();

    while (iterator.hasNext()) {
      KeywordName keywordName;
      keywordName = iterator.next();

      ClassName className;
      className = keywordName.className();

      superTypes.add(code."      \{className}");

      String extendsClause;
      extendsClause = extendsClause(keywordName.superTypes());

      result.add(code."""
        public sealed interface \{className.simpleName()} extends \{extendsClause} {}
      """);
    }

    if (superTypes.isEmpty()) {
      return;
    }

    String extendsClause;
    extendsClause = superTypes.stream().collect(Collectors.joining(",\n"));

    result.add(code."""
      public sealed interface \{KEYWORD_INSTRUCTION.simpleName()} extends
    \{extendsClause} permits \{STANDARD_NAME} {}
    """);
  }

  private void colorValue(List<String> result) {
    ColorValue colorValue;
    colorValue = spec.colorValue;

    if (colorValue == null) {
      return;
    }

    String extendsClause;
    extendsClause = extendsClause(colorValue.superTypes());

    result.add(code."""
      public sealed interface \{COLOR_VALUE.simpleName()} extends \{extendsClause} permits \{COLOR}, \{INTERNAL_INSTRUCTION}, \{STANDARD_NAME} {}
    """);
  }

  private void lengthType(List<String> result) {
    LengthType lengthType;
    lengthType = spec.lengthType;

    if (lengthType == null) {
      return;
    }

    String extendsClause;
    extendsClause = extendsClause(lengthType.interfaces);

    result.add(code."""
      public sealed interface \{LENGTH_VALUE.simpleName()} extends \{extendsClause} permits \{INTERNAL_INSTRUCTION}, \{LENGTH}, \{ZERO} {}
    """);
  }

  private void percentageType(List<String> result) {
    PercentageType percentageType;
    percentageType = spec.percentageType;

    if (percentageType == null) {
      return;
    }

    String extendsClause;
    extendsClause = extendsClause(percentageType.interfaces);

    result.add(code."""
      public sealed interface \{PERCENTAGE_VALUE.simpleName()} extends \{extendsClause} permits \{INTERNAL_INSTRUCTION}, \{PERCENTAGE}, \{ZERO} {}
    """);
  }

  private void flexValue(List<String> result) {
    FlexValue flexValue;
    flexValue = spec.flexValue;

    if (flexValue == null) {
      return;
    }

    String extendsClause;
    extendsClause = extendsClause(flexValue.interfaces);

    result.add(code."""
      public sealed interface \{FLEX_VALUE.simpleName()} extends \{extendsClause} permits \{INTERNAL_INSTRUCTION} {}
    """);
  }

  private void literalTypes(List<String> result) {
    StringType stringType;
    stringType = spec.stringType;

    if (stringType != null) {
      String extendsClause;
      extendsClause = extendsClause(stringType.interfaces);

      result.add(code."""
        public sealed interface \{STRING_LITERAL.simpleName()} extends \{extendsClause} permits \{INTERNAL_INSTRUCTION} {}
      """);
    }

    DoubleType doubleType;
    doubleType = spec.doubleType;

    if (doubleType != null) {
      String extendsClause;
      extendsClause = extendsClause(doubleType.interfaces);

      result.add(code."""
        public sealed interface \{DOUBLE_LITERAL.simpleName()} extends \{extendsClause} permits \{INTERNAL_INSTRUCTION} {}
      """);
    }

    IntType intType;
    intType = spec.intType;

    if (intType != null) {
      String extendsClause;
      extendsClause = extendsClause(intType.interfaces);

      result.add(code."""
        public sealed interface \{INT_LITERAL.simpleName()} extends \{extendsClause} permits \{INTERNAL_INSTRUCTION} {}
      """);
    }
  }

  private void url(List<String> result) {
    UrlType urlType;
    urlType = spec.urlType;

    if (urlType == null) {
      return;
    }

    String extendsClause;
    extendsClause = extendsClause(urlType.interfaces);

    result.add(code."""
      public sealed interface \{URL.simpleName()} extends \{extendsClause} permits \{INTERNAL_INSTRUCTION} {}
    """);
  }

  private void zero(List<String> result) {
    ZeroType zeroType;
    zeroType = spec.zeroType;

    if (zeroType == null) {
      return;
    }

    List<ClassName> superTypes;
    superTypes = new ArrayList<>();

    if (zeroType.lengthType) {
      superTypes.add(LENGTH_VALUE);
    }

    if (zeroType.percentageType) {
      superTypes.add(PERCENTAGE_VALUE);
    }

    String extendsClause;
    extendsClause = extendsClause(superTypes);

    result.add(code."""
      public sealed interface \{ZERO.simpleName()} extends \{extendsClause} permits \{INTERNAL_ZERO} {}
    """);
  }

}
