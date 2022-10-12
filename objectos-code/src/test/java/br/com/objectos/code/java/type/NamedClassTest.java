/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.java.type;

import static br.com.objectos.code.java.type.NamedTypes.t;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import br.com.objectos.code.java.expression.Callee;
import br.com.objectos.code.java.expression.CalleeTest;
import br.com.objectos.code.java.expression.MethodReferenceReferenceExpression;
import br.com.objectos.code.java.expression.MethodReferenceReferenceExpressionTest;
import br.com.objectos.code.util.AbstractCodeJavaTest;
import java.lang.annotation.ElementType;
import javax.lang.model.element.TypeElement;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

public class NamedClassTest extends AbstractCodeJavaTest
    implements
    CalleeTest.Adapter,
    MethodReferenceReferenceExpressionTest.Adapter {

  @Override
  public final Callee callee() {
    return TESTING_CODE.nestedClass("Subject");
  }

  @Test
  public void getArrayCreationExpressionName() {
    NamedClass className;
    className = NamedClass.of(String.class);

    assertEquals(className, className.getArrayCreationExpressionName());
  }

  @Test
  public void getCanonicalName() {
    NamedClass a;
    a = TESTING_CODE.nestedClass("A");

    assertEquals(a.getCanonicalName(), "testing.code.A");

    NamedClass b;
    b = a.nestedClass("B");

    assertEquals(b.getCanonicalName(), "testing.code.A.B");
  }

  @Test
  public void getPackage() {
    NamedClass subject;
    subject = TESTING_CODE.nestedClass("Subject");

    assertEquals(subject.getPackage(), TESTING_CODE);

    NamedClass one;
    one = subject.nestedClass("One");

    assertEquals(one.getPackage(), TESTING_CODE);

    NamedClass two;
    two = one.nestedClass("Two");

    assertEquals(two.getPackage(), TESTING_CODE);
  }

  @Test
  public void getSimpleName() {
    NamedClass a;
    a = TESTING_CODE.nestedClass("A");

    assertEquals(a.getSimpleName(), "A");

    NamedClass b;
    b = a.nestedClass("B");

    assertEquals(b.getSimpleName(), "B");
  }

  @Test
  public void hasCanonicalName() {
    NamedClass a;
    a = TESTING_CODE.nestedClass("A");

    assertTrue(a.hasCanonicalName("testing.code.A"));
    assertFalse(a.hasCanonicalName("testing.code.a"));

    NamedClass b;
    b = a.nestedClass("B");

    assertTrue(b.hasCanonicalName("testing.code.A.B"));
    assertFalse(b.hasCanonicalName("testing.code.A.b"));
  }

  @Test
  public void idTest() {
    test(t(ElementType.class).id("TYPE"), "java.lang.annotation.ElementType.TYPE");
  }

  @Test
  public void isJavaLangObject() {
    NamedClass object;
    object = NamedClass.of(Object.class);

    assertTrue(object.isJavaLangObject());

    NamedClass string;
    string = NamedClass.of(String.class);

    assertFalse(string.isJavaLangObject());
  }

  @Test
  public void isVoid() {
    NamedClass object;
    object = NamedClass.of(Object.class);

    assertFalse(object.isVoid());

    NamedClass voidClass;
    voidClass = NamedClass.of(Void.class);

    assertFalse(voidClass.isVoid());
  }

  @Override
  public final MethodReferenceReferenceExpression methodReferenceReferenceExpression() {
    return TESTING_CODE.nestedClass("Subject");
  }

  @Test
  public void nestedClass() {
    NamedClass res = TESTING_CODE.nestedClass("Outer").nestedClass("Subject");
    assertEquals(res.toString(), "testing.code.Outer.Subject");
  }

  @Test
  public void of() {
    NamedClass a;
    a = NamedClass.of(TESTING_CODE, "A");

    assertEquals(a.getCanonicalName(), "testing.code.A");
    assertEquals(a.getPackage(), TESTING_CODE);
    assertEquals(a.getSimpleName(), "A");

    NamedClass b;
    b = NamedClass.of(a, "B");

    assertEquals(b.getCanonicalName(), "testing.code.A.B");
    assertEquals(b.getPackage(), TESTING_CODE);
    assertEquals(b.getSimpleName(), "B");

    NamedClass fromClass;
    fromClass = NamedClass.of(Override.class);

    assertEquals(fromClass.getCanonicalName(), "java.lang.Override");

    TypeElement typeElement;
    typeElement = getTypeElement(String.class);

    NamedClass fromTypeElement;
    fromTypeElement = NamedClass.of(typeElement);

    assertEquals(fromTypeElement.getCanonicalName(), "java.lang.String");
  }

  @Factory
  public Object[] testFactory() {
    return new Object[] {
        CalleeTest.with(this),
        MethodReferenceReferenceExpressionTest.with(this)
    };
  }

  @Test
  public void toNamedArray() {
    NamedClass string = NamedClass.of(String.class);
    NamedArray a1 = string.toNamedArray();
    NamedArray a2 = a1.toNamedArray();
    NamedArray a3 = a2.toNamedArray();
    test(a1, "java.lang.String[]");
    test(a2, "java.lang.String[][]");
    test(a3, "java.lang.String[][][]");
  }

  @Test
  public void withPrefix() {
    NamedClass res = TESTING_CODE
        .nestedClass("Subject")
        .withPrefix("On");
    assertEquals(res.toString(), "testing.code.OnSubject");
  }

  @Test
  public void withSuffix() {
    NamedClass res = TESTING_CODE
        .nestedClass("Subject")
        .withSuffix("On");
    assertEquals(res.toString(), "testing.code.SubjectOn");
  }

}