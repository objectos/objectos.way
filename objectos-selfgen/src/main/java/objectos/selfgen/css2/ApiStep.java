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
package objectos.selfgen.css2;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import objectos.code.ClassTypeName;
import objectos.util.GrowableList;

final class ApiStep extends ThisTemplate {

  @Override
  protected final void definition() {
    packageDeclaration(CSS_TMPL);

    autoImports();

    classDeclaration(
      annotation(GENERATED, annotationValue(s(GENERATOR))),
      PUBLIC, FINAL, name(API),

      constructor(PRIVATE),

      include(this::style),

      include(this::selectors),

      include(this::valueTypes),

      include(this::keywords),

      include(this::colorValue),

      include(this::lengthType),

      include(this::percentageType),

      include(this::literalTypes),

      include(this::url),

      include(this::zero)
    );
  }

  private void style() {
    interfaceDeclaration(
      PUBLIC, SEALED, name(MEDIA_RULE_ELEM)
    );

    interfaceDeclaration(
      PUBLIC, SEALED, name(MEDIA_FEATURE),
      extendsClause(MEDIA_RULE_ELEM)
    );

    interfaceDeclaration(
      PUBLIC, SEALED, name("MediaFeatureOrStyleDeclaration"),
      extendsClause(MEDIA_FEATURE, STYLE_DECLARATION),
      permitsClause(INTERNAL_INSTRUCTION)
    );

    interfaceDeclaration(
      PUBLIC, SEALED, name("MediaQuery"),
      extendsClause(MEDIA_RULE_ELEM),
      permitsClause(ClassTypeName.of(CSS_INTERNAL, "MediaType"))
    );

    interfaceDeclaration(
      PUBLIC, SEALED, name(STYLE_RULE),
      extendsClause(MEDIA_RULE_ELEM),
      permitsClause(INTERNAL_INSTRUCTION)
    );

    interfaceDeclaration(
      PUBLIC, SEALED, name(STYLE_RULE_ELEM)
    );

    interfaceDeclaration(
      PUBLIC, SEALED, name(STYLE_DECLARATION),
      extendsClause(STYLE_RULE_ELEM)
    );

    FilterFunction filterFunction;
    filterFunction = spec.filterFunction;

    if (filterFunction != null) {
      interfaceDeclaration(
        PUBLIC, SEALED, name(FILTER_FUNCTION)
      );
    }

    List<ClassTypeName> superTypes;
    superTypes = new GrowableList<>();

    for (var property : spec.properties.values()) {
      if (property.isHash()) {
        ClassTypeName className;
        className = property.declarationClassName;

        superTypes.add(className);

        interfaceDeclaration(
          PUBLIC, SEALED, name(className), extendsClause(STYLE_DECLARATION)
        );

        className = property.hashClassName;

        superTypes.add(className);

        interfaceDeclaration(
          PUBLIC, SEALED, name(className), extendsClause(STYLE_DECLARATION)
        );
      }

      else if (property.filterFunction) {
        ClassTypeName className;
        className = property.declarationClassName;

        superTypes.add(className);

        interfaceDeclaration(
          PUBLIC, SEALED, name(className), extendsClause(FILTER_FUNCTION, STYLE_DECLARATION)
        );
      }
    }

    interfaceDeclaration(
      PUBLIC, SEALED, name(STYLE_DECL_INST),
      include(() -> {
        for (var superType : superTypes) {
          extendsClause(superType);
        }
      }),
      permitsClause(INTERNAL_INSTRUCTION)
    );
  }

  private void selectors() {
    interfaceDeclaration(
      PUBLIC, SEALED, name(SELECTOR),
      extendsClause(STYLE_RULE_ELEM)
    );

    interfaceDeclaration(
      PUBLIC, SEALED, name(SELECTOR_INSTRUCTION),
      extendsClause(SELECTOR),
      permitsClause(ClassTypeName.of(CSS_INTERNAL, "Combinator")),
      permitsClause(INTERNAL_INSTRUCTION),
      permitsClause(STANDARD_NAME),
      permitsClause(ClassTypeName.of(CSS_UTIL, "ClassSelector")),
      permitsClause(ClassTypeName.of(CSS_UTIL, "IdSelector")),
      permitsClause(STANDARD_PSEUDO_CLASS_SELECTOR),
      permitsClause(STANDARD_PSEUDO_ELEMENT_SELECTOR),
      permitsClause(STANDARD_TYPE_SELECTOR)
    );
  }

  private void valueTypes() {
    interfaceDeclaration(
      PUBLIC, SEALED, name(PROPERTY_VALUE)
    );

    Collection<ValueType> valueTypes;
    valueTypes = spec.valueTypes.values();

    List<ClassTypeName> superTypes;
    superTypes = new GrowableList<>();

    for (var valueType : valueTypes) {
      superTypes.add(valueType.className);

      valueType(valueType);
    }

    if (superTypes.isEmpty()) {
      return;
    }

    interfaceDeclaration(
      PUBLIC, SEALED, name(VALUE_INSTRUCTION),
      include(() -> {
        for (ClassTypeName superType : superTypes) {
          extendsClause(NL, superType);
        }
      }),
      permitsClause(STANDARD_NAME)
    );
  }

  private void valueType(ValueType valueType) {
    interfaceDeclaration(
      PUBLIC, SEALED, name(valueType.className),

      include(() -> valueTypeExtends(valueType))
    );
  }

  private void valueTypeExtends(ValueType valueType) {
    Collection<ClassTypeName> types;
    types = valueType.superTypes();

    if (types.isEmpty()) {
      extendsClause(PROPERTY_VALUE);
    } else {
      var iter = types.stream()
          .sorted((self, that) -> self.simpleName().compareTo(that.simpleName()))
          .iterator();

      while (iter.hasNext()) {
        extendsClause(iter.next());
      }
    }
  }

  private void keywords() {
    Iterator<KeywordName> iterator;
    iterator = spec.keywords.values().stream()
        .filter(KeywordName::shouldGenerate)
        .iterator();

    List<ClassTypeName> superTypes;
    superTypes = new GrowableList<>();

    while (iterator.hasNext()) {
      KeywordName keywordName;
      keywordName = iterator.next();

      ClassTypeName className;
      className = keywordName.className();

      superTypes.add(className);

      interfaceDeclaration(
        PUBLIC, SEALED, name(className),
        include(() -> {
          keywordName.superTypes().stream()
              .sorted((self, that) -> self.simpleName().compareTo(that.simpleName()))
              .forEach(this::extendsClause);
        })
      );
    }

    if (superTypes.isEmpty()) {
      return;
    }

    interfaceDeclaration(
      PUBLIC, SEALED, name(KEYWORD_INSTRUCTION),
      include(() -> {
        for (ClassTypeName superType : superTypes) {
          extendsClause(NL, superType);
        }
      }),
      permitsClause(STANDARD_NAME)
    );
  }

  private void colorValue() {
    ColorValue colorValue;
    colorValue = spec.colorValue;

    if (colorValue == null) {
      return;
    }

    interfaceDeclaration(
      PUBLIC, SEALED, name(COLOR_VALUE),
      include(() -> {
        colorValue.superTypes().stream()
            .sorted((self, that) -> self.simpleName().compareTo(that.simpleName()))
            .forEach(this::extendsClause);
      }),
      permitsClause(COLOR, INTERNAL_INSTRUCTION, STANDARD_NAME)
    );
  }

  private void lengthType() {
    LengthType lengthType;
    lengthType = spec.lengthType;

    if (lengthType == null) {
      return;
    }

    interfaceDeclaration(
      PUBLIC, SEALED, name(LENGTH_VALUE),
      include(() -> {
        lengthType.interfaces.stream()
            .sorted((self, that) -> self.simpleName().compareTo(that.simpleName()))
            .forEach(this::extendsClause);
      }),
      permitsClause(INTERNAL_INSTRUCTION, LENGTH, ZERO)
    );
  }

  private void percentageType() {
    PercentageType percentageType;
    percentageType = spec.percentageType;

    if (percentageType == null) {
      return;
    }

    interfaceDeclaration(
      PUBLIC, SEALED, name(PERCENTAGE_VALUE),
      include(() -> {
        percentageType.interfaces.stream()
            .sorted((self, that) -> self.simpleName().compareTo(that.simpleName()))
            .forEach(this::extendsClause);
      }),
      permitsClause(INTERNAL_INSTRUCTION, PERCENTAGE, ZERO)
    );
  }

  private void literalTypes() {
    StringType stringType;
    stringType = spec.stringType;

    if (stringType != null) {
      interfaceDeclaration(
        PUBLIC, SEALED, name(STRING_LITERAL),
        include(() -> {
          stringType.interfaces.stream()
              .sorted((self, that) -> self.simpleName().compareTo(that.simpleName()))
              .forEach(this::extendsClause);
        }),
        permitsClause(INTERNAL_INSTRUCTION)
      );
    }

    DoubleType doubleType;
    doubleType = spec.doubleType;

    if (doubleType != null) {
      interfaceDeclaration(
        PUBLIC, SEALED, name(DOUBLE_LITERAL),
        include(() -> {
          doubleType.interfaces.stream()
              .sorted((self, that) -> self.simpleName().compareTo(that.simpleName()))
              .forEach(this::extendsClause);
        }),
        permitsClause(INTERNAL_INSTRUCTION)
      );
    }

    IntType intType;
    intType = spec.intType;

    if (intType != null) {
      interfaceDeclaration(
        PUBLIC, SEALED, name(INT_LITERAL),
        include(() -> {
          intType.interfaces.stream()
              .sorted((self, that) -> self.simpleName().compareTo(that.simpleName()))
              .forEach(this::extendsClause);
        }),
        permitsClause(INTERNAL_INSTRUCTION)
      );
    }
  }

  private void url() {
    UrlType urlType;
    urlType = spec.urlType;

    if (urlType == null) {
      return;
    }

    interfaceDeclaration(
      PUBLIC, SEALED, name(URL),
      include(() -> {
        urlType.interfaces.stream()
            .sorted((self, that) -> self.simpleName().compareTo(that.simpleName()))
            .forEach(this::extendsClause);
      }),
      permitsClause(INTERNAL_INSTRUCTION)
    );
  }

  private void zero() {
    ZeroType zeroType;
    zeroType = spec.zeroType;

    if (zeroType == null) {
      return;
    }

    interfaceDeclaration(
      PUBLIC, SEALED, name(ZERO),
      include(() -> {
        if (zeroType.lengthType) {
          extendsClause(LENGTH_VALUE);
        }

        if (zeroType.percentageType) {
          extendsClause(PERCENTAGE_VALUE);
        }
      }),
      permitsClause(INTERNAL_ZERO)
    );
  }

}