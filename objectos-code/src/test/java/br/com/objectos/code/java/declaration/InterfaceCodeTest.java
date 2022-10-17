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
import static br.com.objectos.code.java.declaration.ExtendsMany._extends;
import static br.com.objectos.code.java.declaration.ExtendsOne._extends;
import static br.com.objectos.code.java.declaration.FieldCode.field;
import static br.com.objectos.code.java.declaration.FieldsShorthand.fields;
import static br.com.objectos.code.java.declaration.InterfaceCode._interface;
import static br.com.objectos.code.java.declaration.MethodCode.method;
import static br.com.objectos.code.java.declaration.Modifiers._abstract;
import static br.com.objectos.code.java.declaration.Modifiers._private;
import static br.com.objectos.code.java.declaration.Modifiers._protected;
import static br.com.objectos.code.java.declaration.Modifiers._public;
import static br.com.objectos.code.java.declaration.Modifiers._static;
import static br.com.objectos.code.java.declaration.Modifiers._strictfp;
import static br.com.objectos.code.java.expression.Expressions.id;
import static br.com.objectos.code.java.expression.Expressions.l;
import static br.com.objectos.code.java.statement.ReturnStatement._return;
import static br.com.objectos.code.java.type.NamedPrimitive._int;
import static br.com.objectos.code.java.type.NamedTypes.t;
import static br.com.objectos.code.java.type.NamedTypes.tvar;

import br.com.objectos.code.java.type.NamedClass;
import br.com.objectos.code.java.type.NamedParameterized;
import br.com.objectos.code.util.AbstractCodeJavaTest;
import java.io.Closeable;
import objectos.util.UnmodifiableList;
import org.testng.annotations.Test;

public class InterfaceCodeTest extends AbstractCodeJavaTest {

  @Test(
      description = "Make sure that the _interface() shorthand works correctly with different arities."
  )
  public void classShorthandArityTest() {
    test(
        _interface(
            _public(),
            _static(),
            _strictfp(),
            id("Arity4")
        ),
        "public static strictfp interface Arity4 {}"
    );
    test(
        _interface(
            annotation(TESTING_CODE.nestedClass("Ann00")),
            _public(),
            _static(),
            _strictfp(),
            id("Arity5")
        ),
        "@testing.code.Ann00",
        "public static strictfp interface Arity5 {}"
    );
    test(
        _interface(
            annotation(TESTING_CODE.nestedClass("Ann00")),
            annotation(TESTING_CODE.nestedClass("Ann01")),
            _public(),
            _static(),
            _strictfp(),
            id("Arity6")
        ),
        "@testing.code.Ann00",
        "@testing.code.Ann01",
        "public static strictfp interface Arity6 {}"
    );
    test(
        _interface(
            annotation(TESTING_CODE.nestedClass("Ann00")),
            annotation(TESTING_CODE.nestedClass("Ann01")),
            annotation(TESTING_CODE.nestedClass("Ann02")),
            _public(),
            _static(),
            _strictfp(),
            id("Arity7")
        ),
        "@testing.code.Ann00",
        "@testing.code.Ann01",
        "@testing.code.Ann02",
        "public static strictfp interface Arity7 {}"
    );
    test(
        _interface(
            annotation(TESTING_CODE.nestedClass("Ann00")),
            annotation(TESTING_CODE.nestedClass("Ann01")),
            annotation(TESTING_CODE.nestedClass("Ann02")),
            annotation(TESTING_CODE.nestedClass("Ann03")),
            _public(),
            _static(),
            _strictfp(),
            id("Arity8")
        ),
        "@testing.code.Ann00",
        "@testing.code.Ann01",
        "@testing.code.Ann02",
        "@testing.code.Ann03",
        "public static strictfp interface Arity8 {}"
    );
    test(
        _interface(
            annotation(TESTING_CODE.nestedClass("Ann00")),
            annotation(TESTING_CODE.nestedClass("Ann01")),
            annotation(TESTING_CODE.nestedClass("Ann02")),
            annotation(TESTING_CODE.nestedClass("Ann03")),
            annotation(TESTING_CODE.nestedClass("Ann04")),
            _public(),
            _static(),
            _strictfp(),
            id("Arity9")
        ),
        "@testing.code.Ann00",
        "@testing.code.Ann01",
        "@testing.code.Ann02",
        "@testing.code.Ann03",
        "@testing.code.Ann04",
        "public static strictfp interface Arity9 {}"
    );
    test(
        _interface(
            annotation(TESTING_CODE.nestedClass("Ann00")),
            annotation(TESTING_CODE.nestedClass("Ann01")),
            annotation(TESTING_CODE.nestedClass("Ann02")),
            annotation(TESTING_CODE.nestedClass("Ann03")),
            annotation(TESTING_CODE.nestedClass("Ann04")),
            annotation(TESTING_CODE.nestedClass("Ann05")),
            _public(),
            _static(),
            _strictfp(),
            id("Arity10")
        ),
        "@testing.code.Ann00",
        "@testing.code.Ann01",
        "@testing.code.Ann02",
        "@testing.code.Ann03",
        "@testing.code.Ann04",
        "@testing.code.Ann05",
        "public static strictfp interface Arity10 {}"
    );
  }

  @Test(
      description = "The annotations should be rendered 'on top' of the class declaration."
  )
  public void interfaceWithAnnotations() {
    test(
        _interface(
            annotation(TESTING_CODE.nestedClass("Annotation"), l(String.class)),
            id("Subject")
        ),
        "@testing.code.Annotation(java.lang.String.class)",
        "interface Subject {}"
    );
    test(
        _interface(
            annotation(TESTING_CODE.nestedClass("Annotation1")),
            annotation(TESTING_CODE.nestedClass("Annotation2")),
            id("Subject")
        ),
        "@testing.code.Annotation1",
        "@testing.code.Annotation2",
        "interface Subject {}"
    );
  }

  @Test(
      description = ""
          + "_interface() shorthand should honor _extends()."
          + " Also, it should be rendered in a single line."
  )
  public void interfaceWithExtends() {
    NamedClass superName = TESTING_CODE.nestedClass("Super");

    NamedParameterized superNameWithT = t(superName, tvar("T"));

    test(
        _interface(
            id("Subject"), _extends(superName)
        ),
        "interface Subject extends testing.code.Super {}"
    );
    test(
        _interface(
            id("Subject"), _extends(superNameWithT)
        ),
        "interface Subject extends testing.code.Super<T> {}"
    );
    test(
        _interface(
            id("A"), _extends(tc("B"), tc("C"))
        ),
        "interface A extends testing.code.B, testing.code.C {}"
    );
    test(
        _interface(
            id("A"), _extends(tc("B"), tc("C"), tc("D"))
        ),
        "interface A extends testing.code.B, testing.code.C, testing.code.D {}"
    );
    test(
        _interface(
            id("A"), _extends(tc("B"), tc("C"), tc("D"), tc("E"))
        ),
        "interface A extends testing.code.B, testing.code.C, testing.code.D, testing.code.E {}"
    );
    test(
        _interface(
            id("A"), _extends(tc("B"), tc("C"), tc("D"), tc("E"), tc("F"))
        ),
        "interface A extends testing.code.B, testing.code.C, testing.code.D, testing.code.E, testing.code.F {}"
    );
    test(
        _interface(
            id("A"), _extends(UnmodifiableList.of(t(Appendable.class), t(Closeable.class)))
        ),
        "interface A extends java.lang.Appendable, java.io.Closeable {}"
    );
  }

  @Test
  public void interfaceWithFields() {
    test(
        _interface(
            field(t(String.class), FieldCode.init(id("FOO"), l("BAR")))
        ),
        "interface Unnamed {",
        "",
        "  java.lang.String FOO = \"BAR\";",
        "",
        "}"
    );
    test(
        _interface(
            fields(
                UnmodifiableList.of(
                    field(_int(), FieldCode.init(id("F1"), l(1))),
                    field(_int(), FieldCode.init(id("F2"), l(2)))
                )
            )
        ),
        "interface Unnamed {",
        "",
        "  int F1 = 1;",
        "",
        "  int F2 = 2;",
        "",
        "}"
    );
  }

  @Test(description = "_interface() shorthand should allow method() elements")
  public void interfaceWithMethods() {
    test(
        _interface(
            id("BuilderDsl"),
            method(t(String.class), id("name"))
        ),
        "interface BuilderDsl {",
        "",
        "  java.lang.String name();",
        "",
        "}"
    );
    test(
        _interface(
            id("WithStaticMethod"),
            method(
                _static(), t(String.class), id("value"),
                _return(l("xpto"))
            )
        ),
        "interface WithStaticMethod {",
        "",
        "  static java.lang.String value() {",
        "    return \"xpto\";",
        "  }",
        "",
        "}"
    );
  }

  @Test(description = "more than one modifier should follow declaration order.")
  public void interfaceWithMoreThanOneModifier() {
    test(_interface(_public(), _abstract(), id("Mod")),
        "public abstract interface Mod {}");
    test(_interface(_abstract(), _private(), _strictfp(), id("Mod")),
        "abstract private strictfp interface Mod {}");
  }

  @Test(description = "a simple interface should be rendered in a single line.")
  public void simpleInterface() {
    test(
        _interface(
            id("BuilderDsl")
        ),
        "interface BuilderDsl {}"
    );
    test(
        _interface(TESTING_CODE.nestedClass("BuilderDsl")),
        "interface BuilderDsl {}"
    );
  }

  @Test(description = "test all allowed modifiers in a top-level or nested interface.")
  public void testAllModifiers() {
    test(_interface(_public(), id("Mod")),
        "public interface Mod {}");
    test(_interface(_protected(), id("Mod")),
        "protected interface Mod {}");
    test(_interface(_private(), id("Mod")),
        "private interface Mod {}");
    test(_interface(_abstract(), id("Mod")),
        "abstract interface Mod {}");
    test(_interface(_static(), id("Mod")),
        "static interface Mod {}");
    test(_interface(_strictfp(), id("Mod")),
        "strictfp interface Mod {}");
  }

  private NamedClass tc(String simpleName) {
    return TESTING_CODE.nestedClass(simpleName);
  }

  private void test(InterfaceCode code, String... lines) {
    testToString(code, lines);
  }

}