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
import java.util.List;
import java.util.Locale;
import objectos.code.ClassTypeName;
import objectos.code.PrimitiveTypeName;
import objectos.code.tmpl.BlockInstruction;

final class GeneratedCssTemplateStep extends ThisTemplate {

  private boolean declSig1;
  private boolean declSig2;
  private boolean declSig3;
  private boolean declSig4;
  private boolean declSig5;
  private boolean declSig6;
  private boolean declSigPrim;
  private boolean declSigString;

  private boolean funcSig2;

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

      include(this::declarationMethods),

      include(this::functions)
    );
  }

  private void colors() {
    ColorValue colorValue;
    colorValue = spec.colorValue;

    if (colorValue == null) {
      return;
    }

    colorValue.names.stream()
        .sorted((self, that) -> (self.fieldName().compareTo(that.fieldName())))
        .forEach(name -> field(COLOR_VALUE, name.fieldName()));

    for (ColorHex colorHex : colorValue.palette) {
      field(
        PROTECTED, STATIC, FINAL, COLOR_VALUE, name(colorHex.constantName()),
        COLOR, v("ofHex"), argument(s(colorHex.hexValue()))
      );
    }
  }

  private void declarationMethods() {
    if (declSig1) {
      method(
        ABSTRACT, VOID, name("declaration"),
        parameter(PROPERTY, name("name")),
        parameter(PROPERTY_VALUE, name("value"))
      );
    }

    if (declSig2) {
      method(
        ABSTRACT, VOID, name("declaration"),
        parameter(PROPERTY, name("name")),
        parameter(PROPERTY_VALUE, name("value1")),
        parameter(PROPERTY_VALUE, name("value2"))
      );
    }

    if (declSig3) {
      method(
        ABSTRACT, VOID, name("declaration"),
        parameter(PROPERTY, name("name")),
        parameter(PROPERTY_VALUE, name("value1")),
        parameter(PROPERTY_VALUE, name("value2")),
        parameter(PROPERTY_VALUE, name("value3"))
      );
    }

    if (declSig4) {
      method(
        ABSTRACT, VOID, name("declaration"),
        parameter(PROPERTY, name("name")),
        parameter(PROPERTY_VALUE, name("value1")),
        parameter(PROPERTY_VALUE, name("value2")),
        parameter(PROPERTY_VALUE, name("value3")),
        parameter(PROPERTY_VALUE, name("value4"))
      );
    }

    if (declSig5) {
      method(
        ABSTRACT, VOID, name("declaration"),
        parameter(PROPERTY, name("name")),
        parameter(PROPERTY_VALUE, name("value1")),
        parameter(PROPERTY_VALUE, name("value2")),
        parameter(PROPERTY_VALUE, name("value3")),
        parameter(PROPERTY_VALUE, name("value4")),
        parameter(PROPERTY_VALUE, name("value5"))
      );
    }

    if (declSig6) {
      method(
        ABSTRACT, VOID, name("declaration"),
        parameter(PROPERTY, name("name")),
        parameter(PROPERTY_VALUE, name("value1")),
        parameter(PROPERTY_VALUE, name("value2")),
        parameter(PROPERTY_VALUE, name("value3")),
        parameter(PROPERTY_VALUE, name("value4")),
        parameter(PROPERTY_VALUE, name("value5")),
        parameter(PROPERTY_VALUE, name("value6"))
      );
    }

    if (declSigPrim) {
      method(
        ABSTRACT, VOID, name("declaration"),
        parameter(PROPERTY, name("name")),
        parameter(INT, name("value"))
      );

      method(
        ABSTRACT, VOID, name("declaration"),
        parameter(PROPERTY, name("name")),
        parameter(DOUBLE, name("value"))
      );
    }

    if (declSigString) {
      method(
        ABSTRACT, VOID, name("declaration"),
        parameter(PROPERTY, name("name")),
        parameter(STRING, name("value"))
      );
    }
  }

  private void field(ClassTypeName type, String fieldName) {
    field(
      PROTECTED, STATIC, FINAL, type, name(fieldName),
      STANDARD_NAME, n(fieldName)
    );
  }

  private void keywords() {
    spec.keywords.values().stream()
        .sorted(KeywordName.ORDER_BY_FIELD_NAME)
        .forEach(kw -> field(kw.className(), kw.fieldName));
  }

  private void lengthUnits() {
    LengthType lengthType;
    lengthType = spec.lengthType;

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
              PROTECTED, FINAL, LENGTH_VALUE, name(unit),
              parameter(primitive, name("value")),
              p(RETURN, v("length"), argument(n("value")), argument(LENGTH_UNIT, n(enumName)))
            );
          }
        });

    for (var primitive : primitives) {
      method(
        ABSTRACT, LENGTH_VALUE, name("length"),
        parameter(primitive, name("value")),
        parameter(LENGTH_UNIT, name("unit"))
      );
    }
  }

  private void properties() {
    spec.properties.values().stream()
        .sorted(Property.ORDER_BY_METHOD_NAME)
        .forEach(this::propertyMethods);
  }

  private BlockInstruction propertyCheckNotNull(String name) {
    return p(CHECK, v("notNull"), argument(n(name)), argument(s(name + " == null")));
  }

  private void propertyMethods(Property property) {
    for (var signature : property.signatures()) {
      propertyMethods0(property, signature);
    }
  }

  private void propertyMethods0(Property property, Signature signature) {
    ClassTypeName declarationClassName;
    declarationClassName = property.declarationClassName;

    if (signature instanceof Signature1 sig) {
      declSig1 = true;

      method(
        PROTECTED, FINAL, declarationClassName, name(property.methodName),
        parameter(sig.type(), name(sig.name())),
        propertyCheckNotNull(sig.name()),
        p(
          v("declaration"),
          argument(PROPERTY, n(property.constantName)),
          argument(n(sig.name()))
        ),
        p(RETURN, INTERNAL_INSTRUCTION, n("INSTANCE"))
      );
    }

    else if (signature instanceof Signature2 sig) {
      declSig2 = true;

      method(
        PROTECTED, FINAL, declarationClassName, name(property.methodName),
        parameter(sig.type1(), name(sig.name1())),
        parameter(sig.type2(), name(sig.name2())),
        propertyCheckNotNull(sig.name1()),
        propertyCheckNotNull(sig.name2()),
        p(
          v("declaration"),
          argument(PROPERTY, n(property.constantName)),
          argument(n(sig.name1())),
          argument(n(sig.name2()))
        ),
        p(RETURN, INTERNAL_INSTRUCTION, n("INSTANCE"))
      );
    }

    else if (signature instanceof Signature3 sig) {
      declSig3 = true;

      method(
        PROTECTED, FINAL, declarationClassName, name(property.methodName),
        parameter(sig.type1(), name(sig.name1())),
        parameter(sig.type2(), name(sig.name2())),
        parameter(sig.type3(), name(sig.name3())),
        propertyCheckNotNull(sig.name1()),
        propertyCheckNotNull(sig.name2()),
        propertyCheckNotNull(sig.name3()),
        p(
          v("declaration"),
          argument(PROPERTY, n(property.constantName)),
          argument(n(sig.name1())),
          argument(n(sig.name2())),
          argument(n(sig.name3()))
        ),
        p(RETURN, INTERNAL_INSTRUCTION, n("INSTANCE"))
      );
    }

    else if (signature instanceof Signature4 sig) {
      declSig4 = true;

      method(
        PROTECTED, FINAL, declarationClassName, name(property.methodName),
        parameter(sig.type1(), name(sig.name1())),
        parameter(sig.type2(), name(sig.name2())),
        parameter(sig.type3(), name(sig.name3())),
        parameter(sig.type4(), name(sig.name4())),
        propertyCheckNotNull(sig.name1()),
        propertyCheckNotNull(sig.name2()),
        propertyCheckNotNull(sig.name3()),
        propertyCheckNotNull(sig.name4()),
        p(
          v("declaration"),
          argument(PROPERTY, n(property.constantName)),
          argument(n(sig.name1())),
          argument(n(sig.name2())),
          argument(n(sig.name3())),
          argument(n(sig.name4()))
        ),
        p(RETURN, INTERNAL_INSTRUCTION, n("INSTANCE"))
      );
    }

    else if (signature instanceof Signature5 sig) {
      declSig5 = true;

      method(
        PROTECTED, FINAL, declarationClassName, name(property.methodName),
        parameter(sig.type1(), name(sig.name1())),
        parameter(sig.type2(), name(sig.name2())),
        parameter(sig.type3(), name(sig.name3())),
        parameter(sig.type4(), name(sig.name4())),
        parameter(sig.type5(), name(sig.name5())),
        propertyCheckNotNull(sig.name1()),
        propertyCheckNotNull(sig.name2()),
        propertyCheckNotNull(sig.name3()),
        propertyCheckNotNull(sig.name4()),
        propertyCheckNotNull(sig.name5()),
        p(
          v("declaration"),
          argument(PROPERTY, n(property.constantName)),
          argument(n(sig.name1())),
          argument(n(sig.name2())),
          argument(n(sig.name3())),
          argument(n(sig.name4())),
          argument(n(sig.name5()))
        ),
        p(RETURN, INTERNAL_INSTRUCTION, n("INSTANCE"))
      );
    }

    else if (signature instanceof Signature6 sig) {
      declSig6 = true;

      method(
        PROTECTED, FINAL, declarationClassName, name(property.methodName),
        parameter(sig.type1(), name(sig.name1())),
        parameter(sig.type2(), name(sig.name2())),
        parameter(sig.type3(), name(sig.name3())),
        parameter(sig.type4(), name(sig.name4())),
        parameter(sig.type5(), name(sig.name5())),
        parameter(sig.type6(), name(sig.name6())),
        propertyCheckNotNull(sig.name1()),
        propertyCheckNotNull(sig.name2()),
        propertyCheckNotNull(sig.name3()),
        propertyCheckNotNull(sig.name4()),
        propertyCheckNotNull(sig.name5()),
        propertyCheckNotNull(sig.name6()),
        p(
          v("declaration"),
          argument(PROPERTY, n(property.constantName)),
          argument(n(sig.name1())),
          argument(n(sig.name2())),
          argument(n(sig.name3())),
          argument(n(sig.name4())),
          argument(n(sig.name5())),
          argument(n(sig.name6()))
        ),
        p(RETURN, INTERNAL_INSTRUCTION, n("INSTANCE"))
      );
    }

    else if (signature instanceof SignaturePrim sig) {
      declSigPrim = true;

      method(
        PROTECTED, FINAL, declarationClassName, name(property.methodName),
        parameter(sig.type(), name(sig.name())),
        p(
          v("declaration"),
          argument(PROPERTY, n(property.constantName)),
          argument(n(sig.name()))
        ),
        p(RETURN, INTERNAL_INSTRUCTION, n("INSTANCE"))
      );
    }

    else if (signature instanceof SignatureString sig) {
      declSigString = true;

      method(
        PROTECTED, FINAL, declarationClassName, name(property.methodName),
        parameter(sig.type(), name(sig.name())),
        propertyCheckNotNull(sig.name()),
        p(
          v("declaration"),
          argument(PROPERTY, n(property.constantName)),
          argument(n(sig.name()))
        ),
        p(RETURN, INTERNAL_INSTRUCTION, n("INSTANCE"))
      );
    }

    else if (signature instanceof SignatureVarArgs sig) {
      method(
        PROTECTED, ABSTRACT, declarationClassName, name(property.methodName),
        parameter(sig.typeName(), ELLIPSIS, name(sig.name()))
      );
    }

    else {
      throw new UnsupportedOperationException(
        "Implement me :: sig.type=" + signature.getClass().getSimpleName()
      );
    }
  }

  private void selectorField(SelectorName selector) {
    switch (selector.kind) {
      case TYPE -> field(
        PROTECTED, STATIC, FINAL, SELECTOR, name(selector.fieldName),
        STANDARD_TYPE_SELECTOR, n(selector.fieldName)
      );

      case PSEUDO_CLASS -> field(
        PROTECTED, STATIC, FINAL, SELECTOR, name(selector.fieldName),
        STANDARD_PSEUDO_CLASS_SELECTOR, n(selector.fieldName)
      );

      case PSEUDO_ELEMENT -> field(
        PROTECTED, STATIC, FINAL, SELECTOR, name(selector.fieldName),
        STANDARD_PSEUDO_ELEMENT_SELECTOR, n(selector.fieldName)
      );

      default -> field(
        PROTECTED, STATIC, FINAL, SELECTOR, name(selector.fieldName),
        STANDARD_NAME, n(selector.fieldName)
      );
    }
  }

  private void selectors() {
    spec.selectors.values().stream()
        .filter(s -> !s.disabled)
        .sorted(SelectorName.ORDER_BY_FIELD_NAME)
        .forEach(this::selectorField);

    field(SELECTOR, "any");
  }

  private void functions() {
    Collection<Function> functions;
    functions = spec.functions.values();

    for (var function : functions) {
      for (var signature : function.signatures()) {
        functions0(function, signature);
      }
    }

    if (funcSig2) {
      method(
        ABSTRACT, VOID, name("function"),
        parameter(FUNCTION, name("name")),
        parameter(PROPERTY_VALUE, name("value1")),
        parameter(PROPERTY_VALUE, name("value2"))
      );
    }
  }

  private void functions0(Function function, Signature signature) {
    ClassTypeName functionClassName;
    functionClassName = function.className;

    if (signature instanceof Signature2 sig) {
      funcSig2 = true;

      method(
        PROTECTED, FINAL, functionClassName, name(function.methodName),
        parameter(sig.type1(), name(sig.name1())),
        parameter(sig.type2(), name(sig.name2())),
        propertyCheckNotNull(sig.name1()),
        propertyCheckNotNull(sig.name2()),
        p(
          v("function"),
          argument(FUNCTION, n(function.constantName)),
          argument(n(sig.name1())),
          argument(n(sig.name2()))
        ),
        p(RETURN, INTERNAL_INSTRUCTION, n("INSTANCE"))
      );
    }

    else {
      throw new UnsupportedOperationException(
        "Implement me :: sig.type=" + signature.getClass().getSimpleName()
      );
    }
  }

}
