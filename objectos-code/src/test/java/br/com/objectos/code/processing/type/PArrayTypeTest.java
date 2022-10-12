/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.processing.type;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import br.com.objectos.code.java.type.NamedArray;
import br.com.objectos.code.java.type.NamedClass;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PArrayTypeTest extends AbstractPTypeMirrorTest {

  @Test
  public void getComponentType() {
    PArrayType a1;
    a1 = getReturnType(Subject.class, "a1").toArrayType();

    assertEquals(
        a1.getComponentType().getName(),
        NamedClass.of(A.class)
    );

    PArrayType a2;
    a2 = getReturnType(Subject.class, "a2").toArrayType();

    assertEquals(
        a2.getComponentType().getName(),
        NamedArray.of(NamedClass.of(A.class))
    );
  }

  @Test
  public void getName() {
    PTypeMirror a1;
    a1 = getReturnType(Subject.class, "a1");

    NamedClass a;
    a = NamedClass.of(A.class);

    assertEquals(
        a1.getName(),
        NamedArray.of(a)
    );

    PTypeMirror a2;
    a2 = getReturnType(Subject.class, "a2");

    assertEquals(
        a2.getName(),
        NamedArray.of(NamedArray.of(a))
    );
  }

  @Test
  public void isArrayType() {
    PTypeMirror a1;
    a1 = getReturnType(Subject.class, "a1");

    assertTrue(a1.isArrayType());

    PTypeMirror notOfType;
    notOfType = getReturnType(Subject.class, "notOfType");

    assertFalse(notOfType.isArrayType());
  }

  @Test
  public void isInstanceOf() {
    PTypeMirror a1;
    a1 = getReturnType(Subject.class, "a1");

    assertTrue(a1.isInstanceOf(A[].class));
    assertTrue(a1.isInstanceOf(Object.class));
    assertFalse(a1.isInstanceOf(A.class));
    assertFalse(a1.isInstanceOf(Integer.class));
  }

  @Test
  public void toArrayType() {
    PTypeMirror a1;
    a1 = getReturnType(Subject.class, "a1");

    PArrayType resultA;
    resultA = a1.toArrayType();

    assertNotNull(resultA);

    PTypeMirror notOfType;
    notOfType = getReturnType(Subject.class, "notOfType");

    try {
      notOfType.toArrayType();

      Assert.fail();
    } catch (AssertionError expected) {
      String message;
      message = expected.getMessage();

      assertTrue(message.contains("See isArrayType()"));
    }
  }

  private interface A {}

  private abstract static class Subject {
    abstract A[] a1();
    abstract A[][] a2();
    abstract A notOfType();
  }

}
