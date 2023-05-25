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
package objectos.selfgen.css.spec;

import java.util.List;
import objectos.code.ClassTypeName;
import objectos.code.tmpl.ConstructorDeclarationInstruction;
import objectos.code.tmpl.TypeName;
import objectos.selfgen.css.spec.MethodSignature.Abstract1;
import objectos.selfgen.css.spec.MethodSignature.SigHash;
import objectos.selfgen.css.spec.MethodSignature.SigZero;
import objectos.selfgen.css.spec.MethodSignature.Signature1;
import objectos.selfgen.css.spec.MethodSignature.Signature2;
import objectos.selfgen.css.spec.MethodSignature.Signature3;
import objectos.selfgen.css.spec.MethodSignature.Signature4;
import objectos.selfgen.css.spec.MethodSignature.Signature5;
import objectos.selfgen.css.spec.MethodSignature.Signature6;
import objectos.selfgen.util.JavaNames;
import objectos.util.GrowableList;

final class GeneratedStyleSheetStep extends ThisTemplate {

  private static final ClassTypeName CSS = ClassTypeName.of(css, "Css");

  private static final ClassTypeName STD_FN = ClassTypeName.of(
    ThisTemplate.function, "StandardFunctionName");

  private static final ClassTypeName STD_PN = ClassTypeName.of(
    ThisTemplate.property, "StandardPropertyName");

  private final class ThisAbstract1 extends ThisSignature {
    private final Abstract1 signature;

    public ThisAbstract1(FunctionOrProperty property, Abstract1 sig) {
      super(property);
      signature = sig;
    }

    @Override
    final void execute() {
      var returnType = ClassTypeName.of(
        sheet, "GeneratedStyleSheet", property.singleDeclarationSimpleName()
      );

      method(
        PROTECTED, ABSTRACT, returnType, name(property.methodName()),
        parameter(t(signature.type), name(signature.name))
      );
    }
  }

  private final class ThisSigHash extends ThisSignature {
    public ThisSigHash(FunctionOrProperty property, SigHash signature) {
      super(property);
    }

    @Override
    final void execute() {
      var returnType = ClassTypeName.of(
        sheet, "GeneratedStyleSheet", property.multiDeclarationSimpleName()
      );

      var paramType = ClassTypeName.of(
        sheet, "GeneratedStyleSheet", property.singleDeclarationSimpleName()
      );

      method(
        PROTECTED, FINAL, returnType, name(property.methodName()),
        parameter(paramType, ELLIPSIS, name("declarations")),
        p(
          RETURN, v(property.addMethodName()),
          argument(funcOrProp(), n(property.enumName())),
          argument(n("declarations"))
        )
      );
    }
  }

  private abstract sealed class ThisSignature {

    final FunctionOrProperty property;

    public ThisSignature(FunctionOrProperty property) { this.property = property; }

    void execute() {}

    final ClassTypeName funcOrProp() {
      if (property instanceof FunctionName) {
        return STD_FN;
      } else {
        return STD_PN;
      }
    }

    final ClassTypeName funcOrPropSingle() {
      if (property instanceof FunctionName) {
        return ClassTypeName.of(
          ThisTemplate.function, property.singleDeclarationSimpleName());
      } else {
        return ClassTypeName.of(
          sheet, "GeneratedStyleSheet", property.singleDeclarationSimpleName());
      }
    }
  }

  private final class ThisSignature1 extends ThisSignature {
    private final Signature1 signature;

    public ThisSignature1(FunctionOrProperty property, Signature1 signature) {
      super(property);
      this.signature = signature;
    }

    @Override
    final void execute() {
      method(
        PROTECTED, FINAL, funcOrPropSingle(), name(property.methodName()),
        parameter(t(signature.type), name(signature.name)),
        p(
          RETURN, v(property.addMethodName()),
          argument(funcOrProp(), n(property.enumName())),
          argument(n(signature.name))
        )
      );
    }
  }

  private final class ThisSignature2 extends ThisSignature {
    private final Signature2 signature;

    public ThisSignature2(FunctionOrProperty property, Signature2 signature) {
      super(property);
      this.signature = signature;
    }

    @Override
    final void execute() {
      method(
        PROTECTED, FINAL, funcOrPropSingle(), name(property.methodName()),
        parameter(t(signature.type0), name(signature.name0)),
        parameter(t(signature.type1), name(signature.name1)),
        p(
          RETURN, v(property.addMethodName()),
          argument(funcOrProp(), n(property.enumName())),
          argument(n(signature.name0)),
          argument(n(signature.name1))
        )
      );
    }
  }

  private final class ThisSignature3 extends ThisSignature {
    private final Signature3 signature;

    public ThisSignature3(FunctionOrProperty property, Signature3 signature) {
      super(property);
      this.signature = signature;
    }

    @Override
    final void execute() {
      method(
        PROTECTED, FINAL, funcOrPropSingle(), name(property.methodName()),
        parameter(t(signature.type0), name(signature.name0)),
        parameter(t(signature.type1), name(signature.name1)),
        parameter(t(signature.type2), name(signature.name2)),
        p(
          RETURN, v(property.addMethodName()),
          argument(funcOrProp(), n(property.enumName())),
          argument(n(signature.name0)),
          argument(n(signature.name1)),
          argument(n(signature.name2))
        )
      );
    }
  }

  private final class ThisSignature4 extends ThisSignature {
    private final Signature4 signature;

    public ThisSignature4(FunctionOrProperty property, Signature4 signature) {
      super(property);
      this.signature = signature;
    }

    @Override
    final void execute() {
      method(
        PROTECTED, FINAL, funcOrPropSingle(), name(property.methodName()),
        parameter(t(signature.type0), name(signature.name0)),
        parameter(t(signature.type1), name(signature.name1)),
        parameter(t(signature.type2), name(signature.name2)),
        parameter(t(signature.type3), name(signature.name3)),
        p(
          RETURN, v(property.addMethodName()),
          argument(funcOrProp(), n(property.enumName())),
          argument(n(signature.name0)),
          argument(n(signature.name1)),
          argument(n(signature.name2)),
          argument(n(signature.name3))
        )
      );
    }
  }

  private final class ThisSignature5 extends ThisSignature {
    private final Signature5 signature;

    public ThisSignature5(FunctionOrProperty property, Signature5 signature) {
      super(property);
      this.signature = signature;
    }

    @Override
    final void execute() {
      method(
        PROTECTED, FINAL, funcOrPropSingle(), name(property.methodName()),
        parameter(t(signature.type0), name(signature.name0)),
        parameter(t(signature.type1), name(signature.name1)),
        parameter(t(signature.type2), name(signature.name2)),
        parameter(t(signature.type3), name(signature.name3)),
        parameter(t(signature.type4), name(signature.name4)),
        p(
          RETURN, v(property.addMethodName()),
          argument(funcOrProp(), n(property.enumName())),
          argument(n(signature.name0)),
          argument(n(signature.name1)),
          argument(n(signature.name2)),
          argument(n(signature.name3)),
          argument(n(signature.name4))
        )
      );
    }
  }

  private final class ThisSignature6 extends ThisSignature {
    private final Signature6 signature;

    public ThisSignature6(FunctionOrProperty property, Signature6 signature) {
      super(property);
      this.signature = signature;
    }

    @Override
    final void execute() {
      method(
        PROTECTED, FINAL, funcOrPropSingle(), name(property.methodName()),
        parameter(t(signature.type0), name(signature.name0)),
        parameter(t(signature.type1), name(signature.name1)),
        parameter(t(signature.type2), name(signature.name2)),
        parameter(t(signature.type3), name(signature.name3)),
        parameter(t(signature.type4), name(signature.name4)),
        parameter(t(signature.type5), name(signature.name5)),
        p(
          RETURN, v(property.addMethodName()),
          argument(funcOrProp(), n(property.enumName())),
          argument(n(signature.name0)),
          argument(n(signature.name1)),
          argument(n(signature.name2)),
          argument(n(signature.name3)),
          argument(n(signature.name4)),
          argument(n(signature.name5))
        )
      );
    }
  }

  private final class ThisSigZero extends ThisSignature {
    private static final ClassTypeName ZERO = ClassTypeName.of(type, "Zero");

    public ThisSigZero(FunctionOrProperty property, SigZero signature) {
      super(property);
    }

    @Override
    final void execute() {
      method(
        PROTECTED, FINAL, funcOrPropSingle(), name(property.methodName()),
        parameter(ZERO, name("zero")),
        p(
          RETURN, v(property.addMethodName()),
          argument(funcOrProp(), n(property.enumName())),
          argument(i(0))
        )
      );
    }
  }

  private final List<String> angleUnitList = new GrowableList<>();

  private final List<String> anyDeclarationList = new GrowableList<>();

  private final List<String> anyFunctionList = new GrowableList<>();

  private final List<String> colorList = new GrowableList<>();

  private final List<String> elementList = new GrowableList<>();

  private final List<KeywordName> keywordList = new GrowableList<>();

  private final List<String> lengthUnitList = new GrowableList<>();

  private final List<Property> propertyList = new GrowableList<>();

  private final List<String> pseudoClassList = new GrowableList<>();

  private final List<String> pseudoElementList = new GrowableList<>();

  private final List<ThisSignature> signatureList = new GrowableList<>();

  GeneratedStyleSheetStep(StepAdapter adapter) {
    super(adapter);
  }

  @Override
  public final void addAngleUnit(String unit) {
    angleUnitList.add(unit);
  }

  @Override
  public final void addColorName(ColorName colorName) {
    colorList.add(colorName.identifier);
  }

  @Override
  public final void addElementName(String elementName) {
    elementList.add(elementName);
  }

  @Override
  public final void addFunction(FunctionName function) {
    anyFunctionList.add(function.singleDeclarationSimpleName());
  }

  @Override
  public final void addKeyword(KeywordName keyword) {
    keywordList.add(keyword);
  }

  @Override
  public final void addLengthUnit(String unit) {
    lengthUnitList.add(unit);
  }

  @Override
  public final void addProperty(Property property) {
    propertyList.add(property);

    var multiName = property.multiDeclarationSimpleName();

    var singleName = property.singleDeclarationSimpleName();

    switch (property.kind) {
      case HASH -> {
        anyDeclarationList.add(multiName);

        anyDeclarationList.add(singleName);
      }

      case STANDARD -> {
        anyDeclarationList.add(singleName);
      }
    }
  }

  @Override
  public final void addMethodSignature(FunctionOrProperty property, MethodSignature signature) {
    ThisSignature result;

    if (signature instanceof Abstract1 sig) {
      result = new ThisAbstract1(property, sig);
    } else if (signature instanceof SigHash sig) {
      result = new ThisSigHash(property, sig);
    } else if (signature instanceof Signature1 sig) {
      result = new ThisSignature1(property, sig);
    } else if (signature instanceof Signature2 sig) {
      result = new ThisSignature2(property, sig);
    } else if (signature instanceof Signature3 sig) {
      result = new ThisSignature3(property, sig);
    } else if (signature instanceof Signature4 sig) {
      result = new ThisSignature4(property, sig);
    } else if (signature instanceof Signature5 sig) {
      result = new ThisSignature5(property, sig);
    } else if (signature instanceof Signature6 sig) {
      result = new ThisSignature6(property, sig);
    } else if (signature instanceof SigZero sig) {
      result = new ThisSigZero(property, sig);
    } else {
      throw new UnsupportedOperationException("Implement me :: " + signature.getClass());
    }

    signatureList.add(result);
  }

  @Override
  public final void addPseudoClass(String name) {
    var fieldName = toFieldName(name);

    pseudoClassList.add(fieldName);
  }

  @Override
  public final void addPseudoElement(String name) {
    var fieldName = toFieldName(name);

    pseudoElementList.add(fieldName);
  }

  @Override
  public final void execute() {
    writeSelf();
  }

  @Override
  protected final void definition() {
    var anyDeclaration = ClassTypeName.of(sheet, "GeneratedStyleSheet", "AnyDeclaration");
    var anyFunction = ClassTypeName.of(sheet, "GeneratedStyleSheet", "AnyFunction");
    var multiDeclarationElem = ClassTypeName.of(sheet, "MultiDeclarationElement");
    var valueType = ClassTypeName.of(type, "Value");
    var angleType = ClassTypeName.of(type, "AngleType");
    var angleUnit = ClassTypeName.of(type, "AngleUnit");
    var lengthType = ClassTypeName.of(type, "LengthType");
    var lengthUnit = ClassTypeName.of(type, "LengthUnit");

    packageDeclaration(sheet);

    autoImports();

    classDeclaration(
      ABSTRACT, name("GeneratedStyleSheet"),

      include(this::fields),

      constructor(new ConstructorDeclarationInstruction[] {}),

      include(this::angleMethods),

      include(this::lengthMethods),

      include(this::propertyMethods),

      method(
        ABSTRACT, anyDeclaration, name("addDeclaration"),
        parameter(STD_PN, name("name")),
        parameter(INT, name("value"))
      ),

      method(
        ABSTRACT, anyDeclaration, name("addDeclaration"),
        parameter(STD_PN, name("name")),
        parameter(DOUBLE, name("value"))
      ),

      method(
        ABSTRACT, anyDeclaration, name("addDeclaration"),
        parameter(STD_PN, name("name")),
        parameter(multiDeclarationElem, ELLIPSIS, name("elements"))
      ),

      method(
        ABSTRACT, anyDeclaration, name("addDeclaration"),
        parameter(STD_PN, name("name")),
        parameter(STRING, name("value"))
      ),

      method(
        ABSTRACT, anyDeclaration, name("addDeclaration"),
        parameter(STD_PN, name("name")),
        parameter(valueType, name("v1"))
      ),

      method(
        ABSTRACT, anyDeclaration, name("addDeclaration"),
        parameter(STD_PN, name("name")),
        parameter(valueType, name("v1")),
        parameter(valueType, name("v2"))
      ),

      method(
        ABSTRACT, anyDeclaration, name("addDeclaration"),
        parameter(STD_PN, name("name")),
        parameter(valueType, name("v1")),
        parameter(valueType, name("v2")),
        parameter(valueType, name("v3"))
      ),

      method(
        ABSTRACT, anyDeclaration, name("addDeclaration"),
        parameter(STD_PN, name("name")),
        parameter(valueType, name("v1")),
        parameter(valueType, name("v2")),
        parameter(valueType, name("v3")),
        parameter(valueType, name("v4"))
      ),

      method(
        ABSTRACT, anyDeclaration, name("addDeclaration"),
        parameter(STD_PN, name("name")),
        parameter(valueType, name("v1")),
        parameter(valueType, name("v2")),
        parameter(valueType, name("v3")),
        parameter(valueType, name("v4")),
        parameter(valueType, name("v5"))
      ),

      method(
        ABSTRACT, anyDeclaration, name("addDeclaration"),
        parameter(STD_PN, name("name")),
        parameter(valueType, name("v1")),
        parameter(valueType, name("v2")),
        parameter(valueType, name("v3")),
        parameter(valueType, name("v4")),
        parameter(valueType, name("v5")),
        parameter(valueType, name("v6"))
      ),

      method(
        ABSTRACT, anyFunction, name("addFunction"),
        parameter(STD_FN, name("name")),
        parameter(valueType, name("v1"))
      ),

      method(
        ABSTRACT, angleType, name("getAngle"),
        parameter(angleUnit, name("unit")),
        parameter(DOUBLE, name("value"))
      ),

      method(
        ABSTRACT, angleType, name("getAngle"),
        parameter(angleUnit, name("unit")),
        parameter(INT, name("value"))
      ),

      method(
        ABSTRACT, lengthType, name("getLength"),
        parameter(lengthUnit, name("unit")),
        parameter(DOUBLE, name("value"))
      ),

      method(
        ABSTRACT, lengthType, name("getLength"),
        parameter(lengthUnit, name("unit")),
        parameter(INT, name("value"))
      ),

      include(this::propertyInterfaces)
    );
  }

  private void propertyInterfaces() {
    var declaration = ClassTypeName.of(sheet, "Declaration");

    var multiDeclaration = ClassTypeName.of(sheet, "MultiDeclarationElement");

    for (var property : propertyList) {
      PropertyKind kind = property.kind;

      switch (kind) {
        case HASH -> {
          interfaceDeclaration(
            PROTECTED, name(property.multiDeclarationSimpleName()),
            extendsClause(declaration)
          );

          interfaceDeclaration(
            PROTECTED, name(property.singleDeclarationSimpleName()),
            extendsClause(multiDeclaration)
          );
        }

        case STANDARD -> {
          interfaceDeclaration(
            PROTECTED, name(property.singleDeclarationSimpleName()),
            extendsClause(declaration)
          );
        }
      }
    }

    interfaceDeclaration(
      name("AnyDeclaration"),
      include(() -> {
        for (var simplName : anyDeclarationList) {
          extendsClause(ClassTypeName.of(sheet, "GeneratedStyleSheet", simplName));
        }
      })
    );

    interfaceDeclaration(
      name("AnyFunction"),
      include(() -> {
        for (var simplName : anyFunctionList) {
          extendsClause(ClassTypeName.of(function, simplName));
        }
      })
    );
  }

  private void angleMethods() {
    var angleType = ClassTypeName.of(type, "AngleType");
    var angleUnit = ClassTypeName.of(type, "AngleUnit");

    for (var unit : angleUnitList) {
      var enumSimpleName = unit.toUpperCase();

      method(
        PROTECTED, FINAL, angleType, name(unit),
        parameter(DOUBLE, name("value")),
        p(RETURN, v("getAngle"), argument(angleUnit, n(enumSimpleName)), argument(n("value")))
      );

      method(
        PROTECTED, FINAL, angleType, name(unit),
        parameter(INT, name("value")),
        p(RETURN, v("getAngle"), argument(angleUnit, n(enumSimpleName)), argument(n("value")))
      );
    }
  }

  private void fields() {
    var typeName = ClassTypeName.of(select, "TypeSelector");

    for (var elementName : elementList) {
      field(
        PROTECTED, STATIC, FINAL, typeName, name(elementName),
        CSS, n(elementName)
      );
    }

    typeName = ClassTypeName.of(select, "PseudoClassSelector");

    for (var name : pseudoClassList) {
      field(
        PROTECTED, STATIC, FINAL, typeName, name(name),
        CSS, n(name)
      );
    }

    typeName = ClassTypeName.of(select, "PseudoElementSelector");

    for (var name : pseudoElementList) {
      field(
        PROTECTED, STATIC, FINAL, typeName, name(name),
        CSS, n(name)
      );
    }

    typeName = ClassTypeName.of(type, "Color");

    for (var name : colorList) {
      field(
        PROTECTED, STATIC, FINAL, typeName, name(name),
        typeName, n(name)
      );
    }

    typeName = ClassTypeName.of(keyword, "Keywords");

    for (var kw : keywordList) {
      var id = kw.fieldName;

      var thisName = ClassTypeName.of(keyword, kw.simpleName);

      field(
        PROTECTED, STATIC, FINAL, thisName, name(id),
        typeName, n(id)
      );
    }
  }

  private void lengthMethods() {
    var lengthType = ClassTypeName.of(type, "LengthType");
    var lengthUnit = ClassTypeName.of(type, "LengthUnit");

    for (var unit : lengthUnitList) {
      var enumSimpleName = unit.toUpperCase();

      method(
        PROTECTED, FINAL, lengthType, name(unit),
        parameter(DOUBLE, name("value")),
        p(RETURN, v("getLength"), argument(lengthUnit, n(enumSimpleName)), argument(n("value")))
      );

      method(
        PROTECTED, FINAL, lengthType, name(unit),
        parameter(INT, name("value")),
        p(RETURN, v("getLength"), argument(lengthUnit, n(enumSimpleName)), argument(n("value")))
      );
    }
  }

  private void propertyMethods() {
    for (var signature : signatureList) {
      signature.execute();
    }
  }

  private TypeName t(ParameterType type) {
    if (type instanceof JavaType t) {
      return switch (t) {
        case DOUBLE -> DOUBLE;
        case INT -> INT;
        case STRING -> STRING;
      };
    } else if (type instanceof KeywordName name) {
      return ClassTypeName.of(keyword, name.simpleName);
    } else if (type instanceof PrimitiveType t) {
      var kind = t.kind;
      return ClassTypeName.of(ThisTemplate.type, kind.typeSimpleName());
    } else if (type instanceof ValueType t) {
      return ClassTypeName.of(ThisTemplate.type, t.simpleName);
    } else {
      throw new UnsupportedOperationException("Implement me :: " + type.getClass());
    }
  }

  private String toFieldName(String simpleName) {
    var fieldName = simpleName.replace('-', '_').toUpperCase();

    return JavaNames.toIdentifier(fieldName);
  }

}