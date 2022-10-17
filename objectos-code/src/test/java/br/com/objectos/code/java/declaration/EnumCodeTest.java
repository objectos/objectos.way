/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
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
package br.com.objectos.code.java.declaration;

import static br.com.objectos.code.java.declaration.AnnotationCode.annotation;
import static br.com.objectos.code.java.declaration.ConstructorCode.constructor;
import static br.com.objectos.code.java.declaration.EnumCode._enum;
import static br.com.objectos.code.java.declaration.EnumConstantCode.enumConstant;
import static br.com.objectos.code.java.declaration.EnumConstantList.enumConstants;
import static br.com.objectos.code.java.declaration.FieldCode.field;
import static br.com.objectos.code.java.declaration.Implements._implements;
import static br.com.objectos.code.java.declaration.MethodCode.method;
import static br.com.objectos.code.java.declaration.Modifiers._abstract;
import static br.com.objectos.code.java.declaration.Modifiers._final;
import static br.com.objectos.code.java.declaration.Modifiers._private;
import static br.com.objectos.code.java.declaration.Modifiers._protected;
import static br.com.objectos.code.java.declaration.Modifiers._public;
import static br.com.objectos.code.java.declaration.Modifiers._static;
import static br.com.objectos.code.java.declaration.Modifiers._strictfp;
import static br.com.objectos.code.java.declaration.ParameterCode.param;
import static br.com.objectos.code.java.expression.Expressions.assign;
import static br.com.objectos.code.java.expression.Expressions.empty;
import static br.com.objectos.code.java.expression.Expressions.id;
import static br.com.objectos.code.java.expression.Expressions.l;
import static br.com.objectos.code.java.statement.ReturnStatement._return;
import static br.com.objectos.code.java.type.NamedTypes._double;
import static br.com.objectos.code.java.type.NamedTypes._int;
import static br.com.objectos.code.java.type.NamedTypes._void;
import static br.com.objectos.code.java.type.NamedTypes.t;
import static br.com.objectos.code.java.type.NamedTypes.tvar;

import br.com.objectos.code.java.element.Keywords;
import br.com.objectos.code.util.AbstractCodeJavaTest;
import java.io.Serializable;
import objectos.util.UnmodifiableList;
import org.testng.annotations.Test;

public class EnumCodeTest extends AbstractCodeJavaTest {

  @Test(
      description = "Make sure that the _enum() shorthand works correctly with different arities."
  )
  public void enumShorthandArityTest() {
    test(
        _enum(
            annotation(TESTING_CODE.nestedClass("Ann00")),
            id("Arity2")
        ),
        "@testing.code.Ann00",
        "enum Arity2 {}"
    );
    test(
        _enum(
            annotation(TESTING_CODE.nestedClass("Ann00")),
            annotation(TESTING_CODE.nestedClass("Ann01")),
            id("Arity3")
        ),
        "@testing.code.Ann00",
        "@testing.code.Ann01",
        "enum Arity3 {}"
    );
    test(
        _enum(
            annotation(TESTING_CODE.nestedClass("Ann00")),
            annotation(TESTING_CODE.nestedClass("Ann01")),
            annotation(TESTING_CODE.nestedClass("Ann02")),
            id("Arity4")
        ),
        "@testing.code.Ann00",
        "@testing.code.Ann01",
        "@testing.code.Ann02",
        "enum Arity4 {}"
    );
    test(
        _enum(
            annotation(TESTING_CODE.nestedClass("Ann00")),
            annotation(TESTING_CODE.nestedClass("Ann01")),
            annotation(TESTING_CODE.nestedClass("Ann02")),
            annotation(TESTING_CODE.nestedClass("Ann03")),
            id("Arity5")
        ),
        "@testing.code.Ann00",
        "@testing.code.Ann01",
        "@testing.code.Ann02",
        "@testing.code.Ann03",
        "enum Arity5 {}"
    );
    test(
        _enum(
            annotation(TESTING_CODE.nestedClass("Ann00")),
            annotation(TESTING_CODE.nestedClass("Ann01")),
            annotation(TESTING_CODE.nestedClass("Ann02")),
            annotation(TESTING_CODE.nestedClass("Ann03")),
            annotation(TESTING_CODE.nestedClass("Ann04")),
            id("Arity6")
        ),
        "@testing.code.Ann00",
        "@testing.code.Ann01",
        "@testing.code.Ann02",
        "@testing.code.Ann03",
        "@testing.code.Ann04",
        "enum Arity6 {}"
    );
    test(
        _enum(
            annotation(TESTING_CODE.nestedClass("Ann00")),
            annotation(TESTING_CODE.nestedClass("Ann01")),
            annotation(TESTING_CODE.nestedClass("Ann02")),
            annotation(TESTING_CODE.nestedClass("Ann03")),
            annotation(TESTING_CODE.nestedClass("Ann04")),
            annotation(TESTING_CODE.nestedClass("Ann05")),
            id("Arity7")
        ),
        "@testing.code.Ann00",
        "@testing.code.Ann01",
        "@testing.code.Ann02",
        "@testing.code.Ann03",
        "@testing.code.Ann04",
        "@testing.code.Ann05",
        "enum Arity7 {}"
    );
    test(
        _enum(
            annotation(TESTING_CODE.nestedClass("Ann00")),
            annotation(TESTING_CODE.nestedClass("Ann01")),
            annotation(TESTING_CODE.nestedClass("Ann02")),
            annotation(TESTING_CODE.nestedClass("Ann03")),
            annotation(TESTING_CODE.nestedClass("Ann04")),
            annotation(TESTING_CODE.nestedClass("Ann05")),
            annotation(TESTING_CODE.nestedClass("Ann06")),
            id("Arity8")
        ),
        "@testing.code.Ann00",
        "@testing.code.Ann01",
        "@testing.code.Ann02",
        "@testing.code.Ann03",
        "@testing.code.Ann04",
        "@testing.code.Ann05",
        "@testing.code.Ann06",
        "enum Arity8 {}"
    );
    test(
        _enum(
            annotation(TESTING_CODE.nestedClass("Ann00")),
            annotation(TESTING_CODE.nestedClass("Ann01")),
            annotation(TESTING_CODE.nestedClass("Ann02")),
            annotation(TESTING_CODE.nestedClass("Ann03")),
            annotation(TESTING_CODE.nestedClass("Ann04")),
            annotation(TESTING_CODE.nestedClass("Ann05")),
            annotation(TESTING_CODE.nestedClass("Ann06")),
            annotation(TESTING_CODE.nestedClass("Ann07")),
            id("Arity9")
        ),
        "@testing.code.Ann00",
        "@testing.code.Ann01",
        "@testing.code.Ann02",
        "@testing.code.Ann03",
        "@testing.code.Ann04",
        "@testing.code.Ann05",
        "@testing.code.Ann06",
        "@testing.code.Ann07",
        "enum Arity9 {}"
    );
    test(
        _enum(
            annotation(TESTING_CODE.nestedClass("Ann00")),
            annotation(TESTING_CODE.nestedClass("Ann01")),
            annotation(TESTING_CODE.nestedClass("Ann02")),
            annotation(TESTING_CODE.nestedClass("Ann03")),
            annotation(TESTING_CODE.nestedClass("Ann04")),
            annotation(TESTING_CODE.nestedClass("Ann05")),
            annotation(TESTING_CODE.nestedClass("Ann06")),
            annotation(TESTING_CODE.nestedClass("Ann07")),
            annotation(TESTING_CODE.nestedClass("Ann08")),
            id("Arity10")
        ),
        "@testing.code.Ann00",
        "@testing.code.Ann01",
        "@testing.code.Ann02",
        "@testing.code.Ann03",
        "@testing.code.Ann04",
        "@testing.code.Ann05",
        "@testing.code.Ann06",
        "@testing.code.Ann07",
        "@testing.code.Ann08",
        "enum Arity10 {}"
    );
  }

  @Test(
      description = "The annotations should be rendered 'on top' of the enum declaration."
  )
  public void enumWithAnnotations() {
    test(
        _enum(
            annotation(TESTING_CODE.nestedClass("Annotation"), l(String.class)),
            id("Subject")
        ),
        "@testing.code.Annotation(java.lang.String.class)",
        "enum Subject {}"
    );
    test(
        _enum(
            annotation(TESTING_CODE.nestedClass("Annotation1")),
            annotation(TESTING_CODE.nestedClass("Annotation2")),
            id("Subject")
        ),
        "@testing.code.Annotation1",
        "@testing.code.Annotation2",
        "enum Subject {}"
    );
  }

  @Test(description = ""
      + "constants should be separated by comma and new lines. last one should have semicolon")
  public void enumWithConstants() {
    test(
        _enum(
            id("Subject"),
            enumConstant(id("ONE"))
        ),
        "enum Subject {",
        "",
        "  ONE;",
        "",
        "}"
    );
    test(
        _enum(
            id("Subject"),
            enumConstant(id("ONE")),
            enumConstant(id("TWO"))
        ),
        "enum Subject {",
        "",
        "  ONE,",
        "",
        "  TWO;",
        "",
        "}"
    );
  }

  @Test(description = ""
      + "_enum shorthand should honor enumConstants() shorthand."
      + "it is possible to mix enumConstant (singular) with enumConstants (plural)")
  public void enumWithConstantsFromEnumConstantsShorthand() {
    EnumConstantCode a = enumConstant(id("A"));
    EnumConstantCode b = enumConstant(id("B"));
    EnumConstantCode c = enumConstant(id("C"));
    UnmodifiableList<EnumConstantCode> abc = UnmodifiableList.of(a, b, c);
    UnmodifiableList<EnumConstantCode> bc = UnmodifiableList.of(b, c);
    test(
        _enum(
            id("Subject"),
            enumConstants(abc)
        ),
        "enum Subject {",
        "",
        "  A,",
        "",
        "  B,",
        "",
        "  C;",
        "",
        "}"
    );
    test(
        _enum(id("Subject"),
            a,
            enumConstants(bc)
        ),
        "enum Subject {",
        "",
        "  A,",
        "",
        "  B,",
        "",
        "  C;",
        "",
        "}"
    );
  }

  @Test(description = "_enum() shorthand should honor constructor()")
  public void enumWithConstructors() {
    test(
        _enum(
            id("WithConstructor"),
            enumConstant(id("INSTANCE")),
            constructor(
                _private(), param(_int(), id("value")),
                assign(Keywords._this().id("value"), id("value"))
            )
        ),
        "enum WithConstructor {",
        "",
        "  INSTANCE;",
        "",
        "  private WithConstructor(int value) {",
        "    this.value = value;",
        "  }",
        "",
        "}"
    );
  }

  @Test(
      description = "_enum() shorthand should honor field()"
  )
  public void enumWithFields() {
    test(
        _enum(
            id("WithFields"),
            enumConstant(id("INSTANCE")),
            field(_private(), _int(), id("value"))
        ),
        "enum WithFields {",
        "",
        "  INSTANCE;",
        "",
        "  private int value;",
        "",
        "}"
    );
    test(
        _enum(
            id("WithFields"),
            enumConstant(id("INSTANCE")),
            field(_private(), _final(), _double(), id("mass")),
            field(_private(), _final(), _double(), id("radius"))
        ),
        "enum WithFields {",
        "",
        "  INSTANCE;",
        "",
        "  private final double mass;",
        "",
        "  private final double radius;",
        "",
        "}"
    );
  }

  @Test
  public void enumWithImplements() {
    test(
        _enum(
            id("Singleton"),
            enumConstant(id("INSTANCE")),
            _implements(t(Serializable.class))
        ),
        "enum Singleton implements java.io.Serializable {",
        "",
        "  INSTANCE;",
        "",
        "}"
    );
    test(
        _enum(
            id("Singleton"),
            enumConstant(id("INSTANCE")),
            _implements(
                t(Serializable.class),
                t(t(Comparable.class), tvar("T"))
            )
        ),
        "enum Singleton implements java.io.Serializable, java.lang.Comparable<T> {",
        "",
        "  INSTANCE;",
        "",
        "}"
    );
  }

  @Test(
      description = "_enum() shorthand should honor method()"
  )
  public void enumWithMethods() {
    test(
        _enum(
            id("Subject"),
            method(_abstract(), t(String.class), id("name"))
        ),
        "enum Subject {",
        "",
        "  abstract java.lang.String name();",
        "",
        "}"
    );
    test(
        _enum(
            id("Singleton"),
            enumConstant(id("INSTANCE")),
            method(_abstract(), t(String.class), id("name"))
        ),
        "enum Singleton {",
        "",
        "  INSTANCE;",
        "",
        "  abstract java.lang.String name();",
        "",
        "}"
    );
    test(
        _enum(
            id("Singleton"),
            enumConstant(id("INSTANCE")),
            method(
                _final(), t(String.class), id("name"),
                _return("name")
            )
        ),
        "enum Singleton {",
        "",
        "  INSTANCE;",
        "",
        "  final java.lang.String name() {",
        "    return name;",
        "  }",
        "",
        "}"
    );
    test(
        _enum(
            id("Singleton"),
            enumConstant(id("INSTANCE")),
            method(
                _final(), t(String.class), id("name"),
                _return("name")
            ),
            method(
                _final(), _void(), id("noop"),
                empty()
            )
        ),
        "enum Singleton {",
        "",
        "  INSTANCE;",
        "",
        "  final java.lang.String name() {",
        "    return name;",
        "  }",
        "",
        "  final void noop() {}",
        "",
        "}"
    );
  }

  @Test(
      description = "More than one modifier should be rendered in insertion order."
  )
  public void moreThanOneModifier() {
    test(
        _enum(_protected(), _static(), id("Inner")),
        "protected static enum Inner {}"
    );
    test(
        _enum(_static(), _private(), _strictfp(), id("Inner")),
        "static private strictfp enum Inner {}"
    );
  }

  @Test(
      description = "An enum with only the name should be rendered in a single line."
  )
  public void nameOnlyEnum() {
    test(
        _enum(
            id("Subject")
        ),
        "enum Subject {}"
    );
    test(
        _enum(
            TESTING_CODE.nestedClass("Subject")
        ),
        "enum Subject {}"
    );
  }

  @Test(
      description = "Test all modifiers allowed in enums."
  )
  public void testAllModifiers() {
    test(_enum(_public(), id("Mod")),
        "public enum Mod {}");
    test(_enum(_protected(), id("Mod")),
        "protected enum Mod {}");
    test(_enum(_private(), id("Mod")),
        "private enum Mod {}");
    test(_enum(_static(), id("Mod")),
        "static enum Mod {}");
    test(_enum(_strictfp(), id("Mod")),
        "strictfp enum Mod {}");
  }

}
