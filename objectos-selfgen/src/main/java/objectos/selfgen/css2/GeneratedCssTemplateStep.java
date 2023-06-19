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

final class GeneratedCssTemplateStep extends ThisTemplate {

  private static final SelectorName UNIVERSAL = new SelectorName("any", "*");

  @Override
  protected final void definition() {
    packageDeclaration(CSS);

    autoImports();

    classDeclaration(
      annotation(GENERATED, annotationValue(s(GENERATOR))),
      ABSTRACT, name("GeneratedCssTemplate"),

      include(this::selectors),

      include(this::keywords),

      method(
        PRIVATE, STATIC, NAMED_ELEMENT, name("named"),
        parameter(STRING, name("name")),
        p(RETURN, NEW, NAMED_ELEMENT, argument(n("name")))
      ),

      include(this::length),

      include(this::percentage),

      include(this::string),

      include(this::properties)
    );
  }

  private void selectors() {
    spec.selectors().stream()
        .filter(s -> !s.disabled)
        .sorted(SelectorName.ORDER_BY_FIELD_NAME)
        .forEach(this::selectorField);

    selectorField(UNIVERSAL);
  }

  private void selectorField(SelectorName selector) {
    field(
      PROTECTED, STATIC, FINAL, SELECTOR, name(selector.fieldName),
      v("named"), argument(s(selector.selectorName))
    );
  }

  private void keywords() {
    spec.keywords().stream()
        .sorted(KeywordName.ORDER_BY_FIELD_NAME)
        .forEach(this::keywordField);
  }

  private void keywordField(KeywordName keyword) {
    var type = keyword.className();

    field(
      PROTECTED, STATIC, FINAL, type, name(keyword.fieldName),
      v("named"), argument(s(keyword.keywordName))
    );
  }

  private void length() {
    var lengthType = spec.lengthType();

    if (lengthType != null) {
      lengthType.units.stream()
          .sorted()
          .forEach(this::lengthMethods);
    }
  }

  private void lengthMethods(String unit) {
    method(
      PROTECTED, FINAL, LENGTH, name(unit),
      parameter(DOUBLE, name("value")),
      p(RETURN, INTERNAL_LENGTH, v("of"), argument(s(unit)), argument(n("value")))
    );

    method(
      PROTECTED, FINAL, LENGTH, name(unit),
      parameter(INT, name("value")),
      p(RETURN, INTERNAL_LENGTH, v("of"), argument(s(unit)), argument(n("value")))
    );
  }

  private void percentage() {
    var percentageType = spec.percentageType();

    if (percentageType == null) {
      return;
    }

    method(
      PROTECTED, FINAL, PERCENTAGE, name("pct"),
      parameter(DOUBLE, name("value")),
      p(RETURN, INTERNAL_PERCENTAGE, v("of"), argument(n("value")))
    );

    method(
      PROTECTED, FINAL, PERCENTAGE, name("pct"),
      parameter(INT, name("value")),
      p(RETURN, INTERNAL_PERCENTAGE, v("of"), argument(n("value")))
    );
  }

  private void string() {
    var stringType = spec.stringType();

    if (stringType == null) {
      return;
    }

    method(
      PROTECTED, FINAL, STRING_LITERAL, name("l"),
      parameter(STRING, name("value")),
      p(RETURN, INTERNAL_STRING_LITERAL, v("of"), argument(n("value")))
    );
  }

  private void properties() {
    spec.properties().stream()
        .sorted(Property.ORDER_BY_METHOD_NAME)
        .forEach(this::propertyMethods);
  }

  private void propertyMethods(Property property) {
    for (var signature : property.signatures()) {
      var typeName = signature.type.typeName();

      switch (signature.style) {
        case BOX -> {
          method(
            PROTECTED, FINAL, STYLE_DECLARATION, name(property.methodName),
            parameter(typeName, name("all")),
            p(
              RETURN, NEW, STYLE_DECLARATION1,
              argument(PROPERTY, n(property.constantName)),
              argument(n("all"), v("self"))
            )
          );

          method(
            PROTECTED, FINAL, STYLE_DECLARATION, name(property.methodName),
            parameter(typeName, name("vertical")),
            parameter(typeName, name("horizontal")),
            p(
              RETURN, NEW, STYLE_DECLARATION2,
              argument(PROPERTY, n(property.constantName)),
              argument(n("vertical"), v("self")),
              argument(n("horizontal"), v("self"))
            )
          );

          method(
            PROTECTED, FINAL, STYLE_DECLARATION, name(property.methodName),
            parameter(typeName, name("top")),
            parameter(typeName, name("horizontal")),
            parameter(typeName, name("bottom")),
            p(
              RETURN, NEW, STYLE_DECLARATION3,
              argument(PROPERTY, n(property.constantName)),
              argument(n("top"), v("self")),
              argument(n("horizontal"), v("self")),
              argument(n("bottom"), v("self"))
            )
          );

          method(
            PROTECTED, FINAL, STYLE_DECLARATION, name(property.methodName),
            parameter(typeName, name("top")),
            parameter(typeName, name("right")),
            parameter(typeName, name("bottom")),
            parameter(typeName, name("left")),
            p(
              RETURN, NEW, STYLE_DECLARATION4,
              argument(PROPERTY, n(property.constantName)),
              argument(n("top"), v("self")),
              argument(n("right"), v("self")),
              argument(n("bottom"), v("self")),
              argument(n("left"), v("self"))
            )
          );
        }

        case DOUBLE -> {
          method(
            PROTECTED, FINAL, STYLE_DECLARATION, name(property.methodName),
            parameter(typeName, name("value")),
            p(
              RETURN, NEW, STYLE_DECLARATION_DOUBLE,
              argument(PROPERTY, n(property.constantName)),
              argument(n("value"))
            )
          );
        }

        case INT -> {
          method(
            PROTECTED, FINAL, STYLE_DECLARATION, name(property.methodName),
            parameter(typeName, name("value")),
            p(
              RETURN, NEW, STYLE_DECLARATION_INT,
              argument(PROPERTY, n(property.constantName)),
              argument(n("value"))
            )
          );
        }

        case VALUE -> {
          method(
            PROTECTED, FINAL, STYLE_DECLARATION, name(property.methodName),
            parameter(typeName, name("value")),
            p(
              RETURN, NEW, STYLE_DECLARATION1,
              argument(PROPERTY, n(property.constantName)),
              argument(n("value"), v("self"))
            )
          );
        }

        case VALUE2 -> {
          method(
            PROTECTED, FINAL, STYLE_DECLARATION, name(property.methodName),
            parameter(typeName, name("value1")),
            parameter(typeName, name("value2")),
            p(
              RETURN, NEW, STYLE_DECLARATION2,
              argument(PROPERTY, n(property.constantName)),
              argument(n("value1"), v("self")),
              argument(n("value2"), v("self"))
            )
          );
        }

        case VALUE3 -> {
          method(
            PROTECTED, FINAL, STYLE_DECLARATION, name(property.methodName),
            parameter(typeName, name("value1")),
            parameter(typeName, name("value2")),
            parameter(typeName, name("value3")),
            p(
              RETURN, NEW, STYLE_DECLARATION3,
              argument(PROPERTY, n(property.constantName)),
              argument(n("value1"), v("self")),
              argument(n("value2"), v("self")),
              argument(n("value3"), v("self"))
            )
          );
        }

        case VARARGS -> {
          method(
            PROTECTED, ABSTRACT, STYLE_DECLARATION, name(property.methodName),
            parameter(typeName, ELLIPSIS, name("values"))
          );
        }
      }
    }
  }

}
