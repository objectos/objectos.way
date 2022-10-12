/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.java.declaration;

import static br.com.objectos.code.java.declaration.AnnotationCode.annotation;
import static br.com.objectos.code.java.declaration.MethodCode.method;
import static br.com.objectos.code.java.declaration.MethodCode.overriding;
import static br.com.objectos.code.java.declaration.MethodCode.signatureOf;
import static br.com.objectos.code.java.declaration.Modifiers._abstract;
import static br.com.objectos.code.java.declaration.Modifiers._final;
import static br.com.objectos.code.java.declaration.Modifiers._native;
import static br.com.objectos.code.java.declaration.Modifiers._private;
import static br.com.objectos.code.java.declaration.Modifiers._protected;
import static br.com.objectos.code.java.declaration.Modifiers._public;
import static br.com.objectos.code.java.declaration.Modifiers._static;
import static br.com.objectos.code.java.declaration.Modifiers._strictfp;
import static br.com.objectos.code.java.declaration.Modifiers._synchronized;
import static br.com.objectos.code.java.declaration.ParameterCode.param;
import static br.com.objectos.code.java.declaration.ParameterCode.params;
import static br.com.objectos.code.java.element.Keywords._this;
import static br.com.objectos.code.java.element.NewLine.nl;
import static br.com.objectos.code.java.expression.Expressions.empty;
import static br.com.objectos.code.java.expression.Expressions.id;
import static br.com.objectos.code.java.expression.Expressions.invoke;
import static br.com.objectos.code.java.expression.Expressions.parens;
import static br.com.objectos.code.java.expression.Expressions.ternary;
import static br.com.objectos.code.java.statement.ReturnStatement._return;
import static br.com.objectos.code.java.statement.Statements.statements;
import static br.com.objectos.code.java.type.NamedTypeParameter.typeParam;
import static br.com.objectos.code.java.type.NamedTypes._int;
import static br.com.objectos.code.java.type.NamedTypes._short;
import static br.com.objectos.code.java.type.NamedTypes._void;
import static br.com.objectos.code.java.type.NamedTypes.a;
import static br.com.objectos.code.java.type.NamedTypes.t;
import static br.com.objectos.code.java.type.NamedTypes.tvar;
import static br.com.objectos.code.java.type.NamedTypes.wildcardExtends;

import br.com.objectos.code.java.expression.Identifier;
import br.com.objectos.code.java.expression.MethodInvocation;
import br.com.objectos.code.java.type.NamedClass;
import br.com.objectos.code.model.element.ProcessingMethod;
import br.com.objectos.code.util.AbstractCodeJavaTest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.lang.model.element.ExecutableElement;
import objectos.util.UnmodifiableList;
import org.testng.annotations.Test;

public class MethodCodeTest extends AbstractCodeJavaTest {

  @Test
  public void _abstract_primitive_method() {
    test(
        method(
            Modifiers._abstract(), _int(), id("value")
        ),
        "abstract int value();"
    );
    test(
        method(
            Modifiers._abstract(), _short(), id("primitiveMethod")
        ),
        "abstract short primitiveMethod();"
    );
  }

  @Test
  public void _abstract_String_name() {
    test(
        method(
            Modifiers._abstract(), t(String.class), id("name")
        ),
        "abstract java.lang.String name();"
    );
  }

  @Test
  public void _abstract_void_method() {
    test(
        method(
            Modifiers._abstract(), _void(), id("method")
        ),
        "abstract void method();"
    );
  }

  @Test
  public void _throwsTest() {
    UnmodifiableList<ThrowsElement> empty;
    empty = UnmodifiableList.of();

    test(
        method(
            ThrowsShorthand._throws(empty)
        ),
        "void unnamed();"
    );
    test(
        method(
            ThrowsShorthand._throws(IOException.class)
        ),
        "void unnamed() throws java.io.IOException;"
    );
    test(
        method(
            ThrowsShorthand._throws(NamedClass.of(InterruptedException.class))
        ),
        "void unnamed() throws java.lang.InterruptedException;"
    );
    test(
        method(
            ThrowsShorthand._throws(
                NamedClass.of(InterruptedException.class),
                NamedClass.of(IOException.class)
            )
        ),
        "void unnamed() throws java.lang.InterruptedException, java.io.IOException;"
    );
    test(
        method(
            ThrowsShorthand._throws(
                NamedClass.of(InterruptedException.class),
                NamedClass.of(IOException.class),
                NamedClass.of(ExecutionException.class)
            )
        ),
        "void unnamed() throws java.lang.InterruptedException, java.io.IOException, java.util.concurrent.ExecutionException;"
    );
    test(
        method(
            ThrowsShorthand._throws(
                UnmodifiableList.of(
                    NamedClass.of(InterruptedException.class),
                    NamedClass.of(IOException.class),
                    NamedClass.of(ExecutionException.class)
                )
            )
        ),
        "void unnamed() throws java.lang.InterruptedException, java.io.IOException, java.util.concurrent.ExecutionException;"
    );
  }

  @Test
  public void arityTest() {
    MethodInvocation m0 = invoke("m0");
    MethodInvocation m1 = invoke("m1");
    MethodInvocation m2 = invoke("m2");
    MethodInvocation m3 = invoke("m3");
    MethodInvocation m4 = invoke("m4");
    MethodInvocation m5 = invoke("m5");
    MethodInvocation m6 = invoke("m6");
    MethodInvocation m7 = invoke("m7");
    MethodInvocation m8 = invoke("m8");
    MethodInvocation m9 = invoke("m9");
    test(
        method(m0),
        "void unnamed() {",
        "  m0();",
        "}"
    );
    test(
        method(m0, m1),
        "void unnamed() {",
        "  m0();",
        "  m1();",
        "}"
    );
    test(
        method(m0, m1, m2),
        "void unnamed() {",
        "  m0();",
        "  m1();",
        "  m2();",
        "}"
    );
    test(
        method(m0, m1, m2, m3),
        "void unnamed() {",
        "  m0();",
        "  m1();",
        "  m2();",
        "  m3();",
        "}"
    );
    test(
        method(m0, m1, m2, m3, m4),
        "void unnamed() {",
        "  m0();",
        "  m1();",
        "  m2();",
        "  m3();",
        "  m4();",
        "}"
    );
    test(
        method(m0, m1, m2, m3, m4, m5),
        "void unnamed() {",
        "  m0();",
        "  m1();",
        "  m2();",
        "  m3();",
        "  m4();",
        "  m5();",
        "}"
    );
    test(
        method(m0, m1, m2, m3, m4, m5, m6),
        "void unnamed() {",
        "  m0();",
        "  m1();",
        "  m2();",
        "  m3();",
        "  m4();",
        "  m5();",
        "  m6();",
        "}"
    );
    test(
        method(m0, m1, m2, m3, m4, m5, m6, m7),
        "void unnamed() {",
        "  m0();",
        "  m1();",
        "  m2();",
        "  m3();",
        "  m4();",
        "  m5();",
        "  m6();",
        "  m7();",
        "}"
    );
    test(
        method(m0, m1, m2, m3, m4, m5, m6, m7, m8),
        "void unnamed() {",
        "  m0();",
        "  m1();",
        "  m2();",
        "  m3();",
        "  m4();",
        "  m5();",
        "  m6();",
        "  m7();",
        "  m8();",
        "}"
    );
    test(
        method(m0, m1, m2, m3, m4, m5, m6, m7, m8, m9),
        "void unnamed() {",
        "  m0();",
        "  m1();",
        "  m2();",
        "  m3();",
        "  m4();",
        "  m5();",
        "  m6();",
        "  m7();",
        "  m8();",
        "  m9();",
        "}"
    );
  }

  @Test
  public void issue88() {
    test(
        method(
            annotation(Override.class),
            _protected(), _final(), _void(), id("m"),
            invoke("a", id("b")).invoke("c", nl(), invoke("d"), nl())
        ),
        "@java.lang.Override",
        "protected final void m() {",
        "  a(b).c(",
        "      d()",
        "  );",
        "}"
    );
  }

  @Test(
      description = "annotations should be rendered 'on top' of method signature"
  )
  public void methodWithAnnotation() {
    test(
        method(
            annotation(Override.class),
            _final(), t(String.class), id("name"),
            _return("name")
        ),
        "@java.lang.Override",
        "final java.lang.String name() {",
        "  return name;",
        "}"
    );
    test(
        method(
            annotation(Override.class),
            annotation(Deprecated.class),
            Modifiers._abstract(), t(String.class), id("getName")
        ),
        "@java.lang.Override",
        "@java.lang.Deprecated",
        "abstract java.lang.String getName();"
    );
  }

  @Test
  public void methodWithManyParameters() {
    test(
        method(
            _public(), _static(), _int(), id("max"),
            param(_int(), id("a")), param(_int(), id("b")),

            _return(ternary(parens(id("a").ge(id("b"))), id("a"), id("b")))
        ),
        "public static int max(int a, int b) {",
        "  return (a >= b) ? a : b;",
        "}"
    );
    test(
        method(
            _abstract(), _void(), id("m"),
            params(Arrays.asList(
                param(_int(), id("a")), param(_int(), id("b"))
            ))
        ),
        "abstract void m(int a, int b);"
    );
  }

  @Test
  public void methodWithParametersAndVarArgs() {
    test(
        method(
            Modifiers._abstract(), _void(), id("varargs"),
            param(t(String.class), id("name")),
            param(VarArgs.of(a(_int())), id("values"))
        ),
        "abstract void varargs(java.lang.String name, int... values);"
    );
  }

  @Test
  public void methodWithSingleParameter() {
    test(
        method(
            _public(), _void(), id("setName"),
            param(t(String.class), id("name")),

            _this().id("name").receive(id("name"))
        ),
        "public void setName(java.lang.String name) {",
        "  this.name = name;",
        "}"
    );
  }

  @Test(description = "method() shorthand should honor statements() shorthand")
  public void methodWithStatementsShorthand() {
    MethodInvocation a = invoke("a");
    MethodInvocation b = invoke("b");
    MethodInvocation c = invoke("c");
    List<MethodInvocation> abc = Arrays.asList(a, b, c);
    test(
        method(
            _void(), id("m"),
            statements(abc)
        ),
        "void m() {",
        "  a();",
        "  b();",
        "  c();",
        "}"
    );
  }

  @Test(description = "method() shorthand should allow typeParam() shorthand")
  public void methodWithTypeParameters() {
    Identifier list = id("list");
    Identifier c = id("c");
    test(
        method(
            _public(), _static(), typeParam("T"), id("sort"),
            param(t(t(List.class), tvar("T")), list),
            param(t(t(Comparator.class), wildcardExtends(tvar("T"))), c),
            list.invoke("sort", c)
        ),
        "public static <T> void sort(java.util.List<T> list, java.util.Comparator<? extends T> c) {",
        "  list.sort(c);",
        "}"
    );
  }

  @Test(
      description = ""
          + "a method with an empty body can be rendered "
          + "by passing the empty() element"
  )
  public void nonAbstractMethodWithEmptyBody() {
    test(
        method(_final(), _void(), id("notAbstract"), empty()),
        "final void notAbstract() {}"
    );
  }

  @Test
  public void overridingTest() {
    test(
        method(
            _final(), overriding(mq(OverridingSubject.class, "m0")),
            empty()
        ),
        "final void m0() {}"
    );
    test(
        method(
            overriding(mq(OverridingSubject.class, "m1")),
            _final(), empty()
        ),
        "public final <N extends java.lang.Number> N m1() {}"
    );
  }

  @Test(description = "method() shorthand should allow signatureOf()")
  public void signatureOfTest() {
    test(
        method(signatureOf(mq(SignatureOfSubject.class, "m0"))),
        "abstract void m0();"
    );
    test(
        method(signatureOf(mq(SignatureOfSubject.class, "m1")), empty()),
        "private int m1(int a, int b) {}"
    );
    test(
        method(signatureOf(mq(SignatureOfSubject.class, "m2")), empty()),
        "public static <T extends java.lang.Number> java.util.List<T> m2() {}"
    );
  }

  @Test
  public void test_all_modifiers() {
    test(method(_public(), _void(), id("method"), empty()),
        "public void method() {}");
    test(method(_protected(), _void(), id("method"), empty()),
        "protected void method() {}");
    test(method(_private(), _void(), id("method"), empty()),
        "private void method() {}");
    test(method(Modifiers._default(), _void(), id("method"), empty()),
        "default void method() {}");
    test(method(_static(), _void(), id("method"), empty()),
        "static void method() {}");
    test(method(_final(), _void(), id("method"), empty()),
        "final void method() {}");
    test(method(_synchronized(), _void(), id("method"), empty()),
        "synchronized void method() {}");
    test(method(_strictfp(), _void(), id("method"), empty()),
        "strictfp void method() {}");

    test(method(_native(), _void(), id("method")),
        "native void method();");
  }

  @Test
  public void test_modifiers_combinations() {
    test(method(_public(), _abstract(), _void(), id("method")),
        "public abstract void method();");
    test(method(_static(), _protected(), _void(), id("method"), empty()),
        "static protected void method() {}");
    test(method(_private(), _native(), _void(), id("method")),
        "private native void method();");
    test(method(_protected(), _abstract(), _synchronized(), _void(), id("method")),
        "protected abstract synchronized void method();");
  }

  @Test
  public void void_method_with_body() {
    test(
        method(
            _void(), id("voidMethodWithBody"),
            invoke("m1"),
            invoke("m2")
        ),
        "void voidMethodWithBody() {",
        "  m1();",
        "  m2();",
        "}"
    );
  }

  @Test
  public void void_method_with_empty_body() {
    test(
        method(
            _void(), id("emptyMethod"), empty()
        ),
        "void emptyMethod() {}"
    );
  }

  private ProcessingMethod mq(Class<?> type, String name) {
    ExecutableElement el = getMethodElement(type, name);
    return ProcessingMethod.adapt(processingEnv, el);
  }

  private void test(MethodCode code, String... lines) {
    testToString(code, lines);
  }

  @SuppressWarnings("unused")
  private static abstract class OverridingSubject {
    public <N extends Number> N m1() {
      throw new UnsupportedOperationException();
    }

    abstract void m0();
  }

  @SuppressWarnings("unused")
  private static abstract class SignatureOfSubject {
    public static <T extends Number> List<T> m2() {
      throw new UnsupportedOperationException();
    }

    abstract void m0();

    private int m1(int a, int b) {
      return a >= b ? a : b;
    }
  }

}