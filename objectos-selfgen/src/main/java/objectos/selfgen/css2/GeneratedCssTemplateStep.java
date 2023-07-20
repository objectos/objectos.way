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

import java.util.List;
import java.util.Locale;
import objectos.code.ClassTypeName;
import objectos.code.PrimitiveTypeName;
import objectos.code.tmpl.BlockInstruction;

final class GeneratedCssTemplateStep extends ThisTemplate {

  @Override
  protected final void definition() {
    packageDeclaration(CSS_INTERNAL);

    autoImports();

    classDeclaration(
      annotation(GENERATED, annotationValue(s(GENERATOR))),
      ABSTRACT, name("GeneratedCssTemplate"),

      include(this::selectors),

      include(this::colors),

      include(this::keywords),

      include(this::lengthUnits),

      include(this::properties),

      method(
        ABSTRACT, STYLE_DECLARATION, name("declaration"),
        parameter(PROPERTY, name("name")),
        parameter(PROPERTY_VALUE, name("value"))
      ),

      method(
        ABSTRACT, STYLE_DECLARATION, name("declaration"),
        parameter(PROPERTY, name("name")),
        parameter(PROPERTY_VALUE, name("value1")),
        parameter(PROPERTY_VALUE, name("value2"))
      ),

      method(
        ABSTRACT, STYLE_DECLARATION, name("declaration"),
        parameter(PROPERTY, name("name")),
        parameter(PROPERTY_VALUE, name("value1")),
        parameter(PROPERTY_VALUE, name("value2")),
        parameter(PROPERTY_VALUE, name("value3"))
      ),

      method(
        ABSTRACT, STYLE_DECLARATION, name("declaration"),
        parameter(PROPERTY, name("name")),
        parameter(PROPERTY_VALUE, name("value1")),
        parameter(PROPERTY_VALUE, name("value2")),
        parameter(PROPERTY_VALUE, name("value3")),
        parameter(PROPERTY_VALUE, name("value4"))
      ),

      method(
        ABSTRACT, STYLE_DECLARATION, name("declaration"),
        parameter(PROPERTY, name("name")),
        parameter(INT, name("value"))
      ),

      method(
        ABSTRACT, STYLE_DECLARATION, name("declaration"),
        parameter(PROPERTY, name("name")),
        parameter(DOUBLE, name("value"))
      ),

      method(
        ABSTRACT, STYLE_DECLARATION, name("declaration"),
        parameter(PROPERTY, name("name")),
        parameter(STRING, name("value"))
      )
    );
  }

  private void selectors() {
    spec.selectors().stream()
        .filter(s -> !s.disabled)
        .sorted(SelectorName.ORDER_BY_FIELD_NAME)
        .forEach(this::selectorField);

    field(SELECTOR, "any");
  }

  private void selectorField(SelectorName selector) {
    switch (selector.kind) {
      case TYPE -> field(
        PROTECTED, STATIC, FINAL, TYPE_SELECTOR, name(selector.fieldName),
        STANDARD_TYPE_SELECTOR, n(selector.fieldName)
      );

      case PSEUDO_CLASS -> field(
        PROTECTED, STATIC, FINAL, PSEUDO_CLASS_SELECTOR, name(selector.fieldName),
        STANDARD_PSEUDO_CLASS_SELECTOR, n(selector.fieldName)
      );

      case PSEUDO_ELEMENT -> field(
        PROTECTED, STATIC, FINAL, PSEUDO_ELEMENT_SELECTOR, name(selector.fieldName),
        STANDARD_PSEUDO_ELEMENT_SELECTOR, n(selector.fieldName)
      );

      default -> field(
        PROTECTED, STATIC, FINAL, SELECTOR, name(selector.fieldName),
        STANDARD_NAME, n(selector.fieldName)
      );
    }
  }

  private void field(ClassTypeName type, String fieldName) {
    field(
      PROTECTED, STATIC, FINAL, type, name(fieldName),
      STANDARD_NAME, n(fieldName)
    );
  }

  private void colors() {
    var colorValue = spec.colorValue();

    if (colorValue == null) {
      return;
    }

    colorValue.names.stream()
        .sorted((self, that) -> (self.fieldName().compareTo(that.fieldName())))
        .forEach(name -> field(COLOR_VALUE, name.fieldName()));
  }

  private void keywords() {
    spec.keywords().stream()
        .sorted(KeywordName.ORDER_BY_FIELD_NAME)
        .forEach(kw -> field(kw.className(), kw.fieldName));
  }

  private void lengthUnits() {
    LengthType lengthType;
    lengthType = spec.lengthType();

    if (lengthType == null) {
      return;
    }

    List<PrimitiveTypeName> primitives;
    primitives = List.of(DOUBLE, INT);

    lengthType.units.stream()
        .sorted()
        .forEach(unit -> {
          String enumName;
          enumName = unit.toUpperCase(Locale.US);

          for (var primitive : primitives) {
            method(
              PROTECTED, FINAL, LENGTH, name(unit),
              parameter(primitive, name("value")),
              p(RETURN, v("length"), argument(n("value")), argument(LENGTH_UNIT, n(enumName)))
            );
          }
        });

    for (var primitive : primitives) {
      method(
        ABSTRACT, LENGTH, name("length"),
        parameter(primitive, name("value")),
        parameter(LENGTH_UNIT, name("unit"))
      );
    }
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
          propertyCheckNotNull(sig.name()),
          p(
            RETURN, v("declaration"),
            argument(PROPERTY, n(property.constantName)),
            argument(n(sig.name()))
          )
        );
      } else if (signature instanceof Signature2 sig) {
        method(
          PROTECTED, FINAL, STYLE_DECLARATION, name(property.methodName),
          parameter(sig.type1(), name(sig.name1())),
          parameter(sig.type2(), name(sig.name2())),
          propertyCheckNotNull(sig.name1()),
          propertyCheckNotNull(sig.name2()),
          p(
            RETURN, v("declaration"),
            argument(PROPERTY, n(property.constantName)),
            argument(n(sig.name1())),
            argument(n(sig.name2()))
          )
        );
      } else if (signature instanceof Signature3 sig) {
        method(
          PROTECTED, FINAL, STYLE_DECLARATION, name(property.methodName),
          parameter(sig.type1(), name(sig.name1())),
          parameter(sig.type2(), name(sig.name2())),
          parameter(sig.type3(), name(sig.name3())),
          propertyCheckNotNull(sig.name1()),
          propertyCheckNotNull(sig.name2()),
          propertyCheckNotNull(sig.name3()),
          p(
            RETURN, v("declaration"),
            argument(PROPERTY, n(property.constantName)),
            argument(n(sig.name1())),
            argument(n(sig.name2())),
            argument(n(sig.name3()))
          )
        );
      } else if (signature instanceof Signature4 sig) {
        method(
          PROTECTED, FINAL, STYLE_DECLARATION, name(property.methodName),
          parameter(sig.type1(), name(sig.name1())),
          parameter(sig.type2(), name(sig.name2())),
          parameter(sig.type3(), name(sig.name3())),
          parameter(sig.type4(), name(sig.name4())),
          propertyCheckNotNull(sig.name1()),
          propertyCheckNotNull(sig.name2()),
          propertyCheckNotNull(sig.name3()),
          propertyCheckNotNull(sig.name4()),
          p(
            RETURN, v("declaration"),
            argument(PROPERTY, n(property.constantName)),
            argument(n(sig.name1())),
            argument(n(sig.name2())),
            argument(n(sig.name3())),
            argument(n(sig.name4()))
          )
        );
      } else if (signature instanceof SignaturePrim sig) {
        method(
          PROTECTED, FINAL, STYLE_DECLARATION, name(property.methodName),
          parameter(sig.type(), name(sig.name())),
          p(
            RETURN, v("declaration"),
            argument(PROPERTY, n(property.constantName)),
            argument(n(sig.name()))
          )
        );
      } else if (signature instanceof SignatureString sig) {
        method(
          PROTECTED, FINAL, STYLE_DECLARATION, name(property.methodName),
          parameter(sig.type(), name(sig.name())),
          propertyCheckNotNull(sig.name()),
          p(
            RETURN, v("declaration"),
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

  private BlockInstruction propertyCheckNotNull(String name) {
    return p(CHECK, v("notNull"), argument(n(name)), argument(s(name + " == null")));
  }

}
