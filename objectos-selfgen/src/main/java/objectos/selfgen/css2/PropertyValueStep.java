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

final class PropertyValueStep extends ThisTemplate {

  @Override
  protected final void definition() {
    packageDeclaration(CSS_TMPL);

    autoImports();

    interfaceDeclaration(
      annotation(GENERATED, annotationValue(s(GENERATOR))),
      PUBLIC, SEALED, name(PROPERTY_VALUE),

      include(this::valueTypes),

      include(this::keywords),

      include(this::colorValue),

      include(this::lengthType),

      include(this::percentageType),

      include(this::stringType),

      include(this::url),

      include(this::zero)
    );
  }

  private void valueTypes() {
    Collection<ValueType> valueTypes;
    valueTypes = spec.valueTypes();

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
      SEALED, name(VALUE_INSTRUCTION),
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
      SEALED, name(valueType.className),

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
    iterator = spec.keywords().stream()
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
        SEALED, name(className),
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
      SEALED, name(KEYWORD_INSTRUCTION),
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
    colorValue = spec.colorValue();

    if (colorValue == null) {
      return;
    }

    interfaceDeclaration(
      SEALED, name(COLOR_VALUE),
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
    lengthType = spec.lengthType();

    if (lengthType == null) {
      return;
    }

    interfaceDeclaration(
      SEALED, name(LENGTH_VALUE),
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
    percentageType = spec.percentageType();

    if (percentageType == null) {
      return;
    }

    interfaceDeclaration(
      SEALED, name(PERCENTAGE_VALUE),
      include(() -> {
        percentageType.interfaces.stream()
            .sorted((self, that) -> self.simpleName().compareTo(that.simpleName()))
            .forEach(this::extendsClause);
      }),
      permitsClause(INTERNAL_INSTRUCTION, PERCENTAGE, ZERO)
    );
  }

  private void stringType() {
    StringType stringType;
    stringType = spec.stringType();

    if (stringType == null) {
      return;
    }

    interfaceDeclaration(
      SEALED, name(STRING_LITERAL),
      include(() -> {
        stringType.interfaces.stream()
            .sorted((self, that) -> self.simpleName().compareTo(that.simpleName()))
            .forEach(this::extendsClause);
      }),
      permitsClause(INTERNAL_INSTRUCTION)
    );
  }

  private void url() {
    UrlType urlType;
    urlType = spec.urlType();

    if (urlType == null) {
      return;
    }

    interfaceDeclaration(
      SEALED, name(URL),
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
    zeroType = spec.zeroType();

    if (zeroType == null) {
      return;
    }

    interfaceDeclaration(
      SEALED, name(ZERO),
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
