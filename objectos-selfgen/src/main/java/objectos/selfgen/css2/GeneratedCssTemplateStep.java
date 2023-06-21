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

import objectos.code.ClassTypeName;
import objectos.code.PrimitiveTypeName;

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

      include(this::colors),

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

  private void colors() {
    var colorValue = spec.colorValue();

    if (colorValue == null) {
      return;
    }

    colorValue.names.stream()
        .sorted((self, that) -> (self.fieldName().compareTo(that.fieldName())))
        .forEach(name -> field(
          PROTECTED, STATIC, FINAL, COLOR_VALUE, name(name.fieldName()),
          COLOR, n(name.constantName())
        ));
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
      if (signature instanceof Signature1 sig) {
        method(
          PROTECTED, FINAL, STYLE_DECLARATION, name(property.methodName),
          parameter(sig.type(), name(sig.name())),
          p(
            RETURN, NEW, STYLE_DECLARATION1,
            argument(PROPERTY, n(property.constantName)),
            argument(n(sig.name()), v("self"))
          )
        );
      } else if (signature instanceof Signature2 sig) {
        method(
          PROTECTED, FINAL, STYLE_DECLARATION, name(property.methodName),
          parameter(sig.type1(), name(sig.name1())),
          parameter(sig.type2(), name(sig.name2())),
          p(
            RETURN, NEW, STYLE_DECLARATION2,
            argument(PROPERTY, n(property.constantName)),
            argument(n(sig.name1()), v("self")),
            argument(n(sig.name2()), v("self"))
          )
        );
      } else if (signature instanceof Signature3 sig) {
        method(
          PROTECTED, FINAL, STYLE_DECLARATION, name(property.methodName),
          parameter(sig.type1(), name(sig.name1())),
          parameter(sig.type2(), name(sig.name2())),
          parameter(sig.type3(), name(sig.name3())),
          p(
            RETURN, NEW, STYLE_DECLARATION3,
            argument(PROPERTY, n(property.constantName)),
            argument(n(sig.name1()), v("self")),
            argument(n(sig.name2()), v("self")),
            argument(n(sig.name3()), v("self"))
          )
        );
      } else if (signature instanceof Signature4 sig) {
        method(
          PROTECTED, FINAL, STYLE_DECLARATION, name(property.methodName),
          parameter(sig.type1(), name(sig.name1())),
          parameter(sig.type2(), name(sig.name2())),
          parameter(sig.type3(), name(sig.name3())),
          parameter(sig.type4(), name(sig.name4())),
          p(
            RETURN, NEW, STYLE_DECLARATION4,
            argument(PROPERTY, n(property.constantName)),
            argument(n(sig.name1()), v("self")),
            argument(n(sig.name2()), v("self")),
            argument(n(sig.name3()), v("self")),
            argument(n(sig.name4()), v("self"))
          )
        );
      } else if (signature instanceof SignaturePrim sig) {
        var type = sig.type();

        ClassTypeName impl;

        if (type == PrimitiveTypeName.INT) {
          impl = STYLE_DECLARATION_INT;
        } else if (type == PrimitiveTypeName.DOUBLE) {
          impl = STYLE_DECLARATION_DOUBLE;
        } else {
          throw new UnsupportedOperationException("Implement me");
        }

        method(
          PROTECTED, FINAL, STYLE_DECLARATION, name(property.methodName),
          parameter(type, name(sig.name())),
          p(
            RETURN, NEW, impl,
            argument(PROPERTY, n(property.constantName)),
            argument(n(sig.name()))
          )
        );
      } else if (signature instanceof SignatureString sig) {
        method(
          PROTECTED, FINAL, STYLE_DECLARATION, name(property.methodName),
          parameter(sig.type(), name(sig.name())),
          p(
            OBJECTS, v("requireNonNull"),
            argument(n(sig.name())),
            argument(s(sig.name() + " == null"))
          ),
          p(
            RETURN, NEW, STYLE_DECLARATION_STRING,
            argument(PROPERTY, n(property.constantName)),
            argument(n(sig.name()))
          )
        );
      } else if (signature instanceof SignatureVarArgs sig) {
        method(
          PROTECTED, ABSTRACT, STYLE_DECLARATION, name(property.methodName),
          parameter(sig.typeName(), ELLIPSIS, name(sig.name()))
        );
      } else {
        throw new UnsupportedOperationException(
          "Implement me :: sig.type=" + signature.getClass().getSimpleName()
        );
      }
    }
  }

}
