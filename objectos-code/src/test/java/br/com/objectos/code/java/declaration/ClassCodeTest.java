/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.java.declaration;

import static br.com.objectos.code.java.declaration.AnnotationCode.annotation;
import static br.com.objectos.code.java.declaration.ClassCode._class;
import static br.com.objectos.code.java.declaration.ConstructorCode.constructor;
import static br.com.objectos.code.java.declaration.EnumCode._enum;
import static br.com.objectos.code.java.declaration.ExtendsOne._extends;
import static br.com.objectos.code.java.declaration.FieldCode.field;
import static br.com.objectos.code.java.declaration.FieldCode.init;
import static br.com.objectos.code.java.declaration.FieldsShorthand.fields;
import static br.com.objectos.code.java.declaration.Implements._implements;
import static br.com.objectos.code.java.declaration.InterfaceCode._interface;
import static br.com.objectos.code.java.declaration.MethodCode.method;
import static br.com.objectos.code.java.declaration.MethodsShorthand.methods;
import static br.com.objectos.code.java.declaration.Modifiers.PUBLIC;
import static br.com.objectos.code.java.declaration.Modifiers.STATIC;
import static br.com.objectos.code.java.declaration.Modifiers._abstract;
import static br.com.objectos.code.java.declaration.Modifiers._final;
import static br.com.objectos.code.java.declaration.Modifiers._private;
import static br.com.objectos.code.java.declaration.Modifiers._protected;
import static br.com.objectos.code.java.declaration.Modifiers._public;
import static br.com.objectos.code.java.declaration.Modifiers._static;
import static br.com.objectos.code.java.declaration.Modifiers._strictfp;
import static br.com.objectos.code.java.expression.Expressions.id;
import static br.com.objectos.code.java.expression.Expressions.l;
import static br.com.objectos.code.java.statement.ReturnStatement._return;
import static br.com.objectos.code.java.type.NamedTypeParameter.typeParam;
import static br.com.objectos.code.java.type.NamedTypes._int;
import static br.com.objectos.code.java.type.NamedTypes._void;
import static br.com.objectos.code.java.type.NamedTypes.t;
import static br.com.objectos.code.java.type.NamedTypes.tvar;

import br.com.objectos.code.java.type.NamedClass;
import br.com.objectos.code.java.type.NamedParameterized;
import br.com.objectos.code.util.AbstractCodeJavaTest;
import java.io.Closeable;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;
import javax.lang.model.element.TypeElement;
import objectos.util.UnmodifiableList;
import org.testng.annotations.Test;

public class ClassCodeTest extends AbstractCodeJavaTest {

  @Test(
      description = "Make sure that the _class() shorthand works correctly with different arities."
  )
  public void classShorthandArityTest() {
    test(
        _class(
            _public(),
            _static(),
            _strictfp(),
            _final(),
            id("Arity5")
        ),
        "public static strictfp final class Arity5 {}"
    );
    test(
        _class(
            annotation(TESTING_CODE.nestedClass("Ann00")),
            _public(),
            _static(),
            _strictfp(),
            _final(),
            id("Arity6")
        ),
        "@testing.code.Ann00",
        "public static strictfp final class Arity6 {}"
    );
    test(
        _class(
            annotation(TESTING_CODE.nestedClass("Ann00")),
            annotation(TESTING_CODE.nestedClass("Ann01")),
            _public(),
            _static(),
            _strictfp(),
            _final(),
            id("Arity7")
        ),
        "@testing.code.Ann00",
        "@testing.code.Ann01",
        "public static strictfp final class Arity7 {}"
    );
    test(
        _class(
            annotation(TESTING_CODE.nestedClass("Ann00")),
            annotation(TESTING_CODE.nestedClass("Ann01")),
            annotation(TESTING_CODE.nestedClass("Ann02")),
            _public(),
            _static(),
            _strictfp(),
            _final(),
            id("Arity8")
        ),
        "@testing.code.Ann00",
        "@testing.code.Ann01",
        "@testing.code.Ann02",
        "public static strictfp final class Arity8 {}"
    );
    test(
        _class(
            annotation(TESTING_CODE.nestedClass("Ann00")),
            annotation(TESTING_CODE.nestedClass("Ann01")),
            annotation(TESTING_CODE.nestedClass("Ann02")),
            annotation(TESTING_CODE.nestedClass("Ann03")),
            _public(),
            _static(),
            _strictfp(),
            _final(),
            id("Arity9")
        ),
        "@testing.code.Ann00",
        "@testing.code.Ann01",
        "@testing.code.Ann02",
        "@testing.code.Ann03",
        "public static strictfp final class Arity9 {}"
    );
    test(
        _class(
            annotation(TESTING_CODE.nestedClass("Ann00")),
            annotation(TESTING_CODE.nestedClass("Ann01")),
            annotation(TESTING_CODE.nestedClass("Ann02")),
            annotation(TESTING_CODE.nestedClass("Ann03")),
            annotation(TESTING_CODE.nestedClass("Ann04")),
            _public(),
            _static(),
            _strictfp(),
            _final(),
            id("Arity10")
        ),
        "@testing.code.Ann00",
        "@testing.code.Ann01",
        "@testing.code.Ann02",
        "@testing.code.Ann03",
        "@testing.code.Ann04",
        "public static strictfp final class Arity10 {}"
    );
  }

  @Test(
      description = "The annotations should be rendered 'on top' of the class declaration."
  )
  public void classWithAnnotations() {
    test(
        _class(
            annotation(TESTING_CODE.nestedClass("Annotation"), l(String.class)),
            id("Subject")
        ),
        "@testing.code.Annotation(java.lang.String.class)",
        "class Subject {}"
    );
    test(
        _class(
            annotation(TESTING_CODE.nestedClass("Annotation1")),
            annotation(TESTING_CODE.nestedClass("Annotation2")),
            id("Subject")
        ),
        "@testing.code.Annotation1",
        "@testing.code.Annotation2",
        "class Subject {}"
    );
  }

  @Test(
      description = "_class() shorthand should honor constructor()"
  )
  public void classWithConstructor() {
    test(
        _class(
            id("Subject"),
            constructor(_private())
        ),
        "class Subject {",
        "",
        "  private Subject() {}",
        "",
        "}"
    );
  }

  @Test(
      description = ""
          + "_class() shorthand should honor _extends()."
          + " Also, it should be rendered in a single line."
  )
  public void classWithExtends() {
    NamedClass superName = TESTING_CODE.nestedClass("Super");

    NamedParameterized superNameWithT = t(superName, tvar("T"));

    test(
        _class(
            id("Subject"), _extends(superName)
        ),
        "class Subject extends testing.code.Super {}"
    );
    test(
        _class(
            id("Subject"), _extends(superNameWithT)
        ),
        "class Subject extends testing.code.Super<T> {}"
    );
  }

  @Test(
      description = "_class() shorthand should honor field()"
  )
  public void classWithFields() {
    test(
        _class(
            id("Subject"),
            field(_private(), _final(), t(String.class), id("name"))
        ),
        "class Subject {",
        "",
        "  private final java.lang.String name;",
        "",
        "}"
    );
  }

  @Test(description = "_class() shorthand should honor fields() shorthand")
  public void classWithFieldsShorthand() {
    test(
        _class(
            id("Subject"),
            fields(classWithFieldsShorthand0())
        ),
        "class Subject {",
        "",
        "  int a;",
        "",
        "  private static final java.lang.String b = \"something\";",
        "",
        "}"
    );
  }

  @Test(
      description = "_class() shorthand should honor _implements()"
  )
  public void classWithImplements() {
    PackageName abc = PackageName.named("abc");
    NamedClass a = abc.nestedClass("A");
    NamedClass b = abc.nestedClass("B");
    NamedClass c = abc.nestedClass("C");
    NamedClass d = abc.nestedClass("D");
    NamedClass e = abc.nestedClass("E");
    NamedClass f = abc.nestedClass("F");
    NamedClass g = abc.nestedClass("G");
    NamedClass h = abc.nestedClass("H");
    NamedClass i = abc.nestedClass("I");
    NamedClass j = abc.nestedClass("J");
    test(
        _class(
            id("Subject"),
            _implements(t(Serializable.class))
        ),
        "class Subject implements java.io.Serializable {}"
    );
    test(
        _class(
            id("Subject"),
            _implements(
                t(Serializable.class),
                t(t(Comparable.class), tvar("T"))
            )
        ),
        "class Subject implements java.io.Serializable, java.lang.Comparable<T> {}"
    );
    test(
        _class(
            id("Subject"),
            _implements(a, b, c)
        ),
        "class Subject implements abc.A, abc.B, abc.C {}"
    );
    test(
        _class(
            id("Subject"),
            _implements(a, b, c, d)
        ),
        "class Subject implements abc.A, abc.B, abc.C, abc.D {}"
    );
    test(
        _class(
            id("Subject"),
            _implements(a, b, c, d, e)
        ),
        "class Subject implements abc.A, abc.B, abc.C, abc.D, abc.E {}"
    );
    test(
        _class(
            id("Subject"),
            _implements(a, b, c, d, e, f)
        ),
        "class Subject implements abc.A, abc.B, abc.C, abc.D, abc.E, abc.F {}"
    );
    test(
        _class(
            id("Subject"),
            _implements(a, b, c, d, e, f, g)
        ),
        "class Subject implements abc.A, abc.B, abc.C, abc.D, abc.E, abc.F, abc.G {}"
    );
    test(
        _class(
            id("Subject"),
            _implements(a, b, c, d, e, f, g, h)
        ),
        "class Subject implements abc.A, abc.B, abc.C, abc.D, abc.E, abc.F, abc.G, abc.H {}"
    );
    test(
        _class(
            id("Subject"),
            _implements(a, b, c, d, e, f, g, h, i)
        ),
        "class Subject implements abc.A, abc.B, abc.C, abc.D, abc.E, abc.F, abc.G, abc.H, abc.I {}"
    );
    test(
        _class(
            id("Subject"),
            _implements(a, b, c, d, e, f, g, h, i, j)
        ),
        "class Subject implements abc.A, abc.B, abc.C, abc.D, abc.E, abc.F, abc.G, abc.H, abc.I, abc.J {}"
    );
    test(
        _class(
            id("Subject"),
            _implements(UnmodifiableList.of(a, b, c, d, e, f, g, h, i, j))
        ),
        "class Subject implements abc.A, abc.B, abc.C, abc.D, abc.E, abc.F, abc.G, abc.H, abc.I, abc.J {}"
    );
  }

  @Test(
      description = "_class() shorthand should honor method()"
  )
  public void classWithMethods() {
    test(
        _class(
            id("Subject"),
            method(_abstract(), t(String.class), id("name"))
        ),
        "class Subject {",
        "",
        "  abstract java.lang.String name();",
        "",
        "}"
    );
    test(
        _class(
            id("Subject"),
            method(
                _final(), t(String.class), id("name"),
                _return("name")
            )
        ),
        "class Subject {",
        "",
        "  final java.lang.String name() {",
        "    return name;",
        "  }",
        "",
        "}"
    );
    test(
        _class(
            id("Subject"),
            method(
                annotation(Override.class),
                _final(), t(String.class), id("name"),
                _return("name")
            )
        ),
        "class Subject {",
        "",
        "  @java.lang.Override",
        "  final java.lang.String name() {",
        "    return name;",
        "  }",
        "",
        "}"
    );
  }

  @Test(description = "_class() shorthand should honor methods() shorthand")
  public void classWithMethodsShorthand() {
    test(
        _class(
            id("Subject"),
            methods(classWithMethodsShorthand0())
        ),
        "class Subject {",
        "",
        "  abstract void m0();",
        "",
        "  abstract void m1();",
        "",
        "  abstract void m2();",
        "",
        "}"
    );
  }

  @Test(
      description = "_class() shorthand should allow nested types"
  )
  public void classWithNestedTypes() {
    test(
        _class(
            id("Subject"),
            _class(id("Inner"))
        ),
        "class Subject {",
        "",
        "  class Inner {}",
        "",
        "}"
    );
  }

  @Test(
      description = "_class() shorthand should honor typeParam()"
  )
  public void classWithTypeParameters() {
    test(
        _class(
            id("Subject"),
            typeParam("T")
        ),
        "class Subject<T> {}"
    );
    test(
        _class(
            id("Subject"),
            typeParam("A"), typeParam("B"), typeParam("C")
        ),
        "class Subject<A, B, C> {}"
    );
    test(
        _class(
            id("Subject"),
            typeParam("U"),
            typeParam("B", t(InputStream.class)),
            typeParam("I", t(InputStream.class), t(Closeable.class))
        ),
        "class Subject<U, B extends java.io.InputStream, I extends java.io.InputStream & java.io.Closeable> {}"
    );
  }

  @Test(description = "_class() shorthand should honor types()")
  public void classWithTypesShorthand() {
    test(
        _class(
            id("Subject"),
            TypesShorthand.types(
                UnmodifiableList.of(
                    _class(id("A")),
                    _interface(id("B")),
                    _enum(id("C"))
                )
            )
        ),
        "class Subject {",
        "",
        "  class A {}",
        "",
        "  interface B {}",
        "",
        "  enum C {}",
        "",
        "}"
    );
  }

  @Test(
      description = "More than one modifier should be rendered in insertion order."
  )
  public void moreThanOneModifier() {
    test(
        _class(_protected(), _static(), id("Inner")),
        "protected static class Inner {}"
    );
    test(
        _class(_static(), _private(), _strictfp(), id("Inner")),
        "static private strictfp class Inner {}"
    );
  }

  @Test(
      description = "A class with only the name should be rendered in a single line."
  )
  public void nameOnlyClass() {
    test(
        _class(
            id("Subject")
        ),
        "class Subject {}"
    );
    test(
        _class(
            TESTING_CODE.nestedClass("Subject")
        ),
        "class Subject {}"
    );
  }

  @Test(
      description = "Test all modifiers allowed in classes."
  )
  public void testAllModifiers() {
    test(_class(_public(), id("Mod")),
        "public class Mod {}");
    test(_class(_protected(), id("Mod")),
        "protected class Mod {}");
    test(_class(_private(), id("Mod")),
        "private class Mod {}");
    test(_class(_abstract(), id("Mod")),
        "abstract class Mod {}");
    test(_class(_final(), id("Mod")),
        "final class Mod {}");
    test(_class(_static(), id("Mod")),
        "static class Mod {}");
    test(_class(_strictfp(), id("Mod")),
        "strictfp class Mod {}");
  }

  @Test
  public void withModifierShouldOverwrite() {
    test(ClassCode.builder()
        .addModifier(PUBLIC)
        .addModifier(STATIC)
        .simpleName("Subject")
        .addModifiers(ClassModifierSet._final().build())
        .build(),
        "public static final class Subject {}");
  }

  @Test
  public void withTypeParametersFromEmpty() {
    TypeElement superclass = getTypeElement(Empty.class);
    test(ClassCode.builder().simpleName("Subject").addTypeParametersFrom(superclass).build(),
        "class Subject {}");
  }

  @Test
  public void withTypeParametersFromGeneric() {
    TypeElement superclass = getTypeElement(Generic.class);
    test(ClassCode.builder().simpleName("Subject").addTypeParametersFrom(superclass).build(),
        "class Subject<U, B extends java.io.InputStream, I extends java.io.InputStream & java.io.Closeable> {}");
  }

  private Iterable<FieldCode> classWithFieldsShorthand0() {
    return UnmodifiableList.of(
        field(_int(), id("a")),
        field(_private(), _static(), _final(), t(String.class), init(id("b"), l("something")))
    );
  }

  private Iterable<MethodCode> classWithMethodsShorthand0() {
    return Arrays.asList(
        method(_abstract(), _void(), id("m0")),
        method(_abstract(), _void(), id("m1")),
        method(_abstract(), _void(), id("m2"))
    );
  }

  private void test(ClassCode code, String... lines) {
    testToString(code, lines);
  }

}