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

import br.com.objectos.code.java.type.NamedVoid;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PNoTypeTest extends AbstractPTypeMirrorTest {

  @Test
  public void getName() {
    PTypeMirror a;
    a = getReturnType(Subject.class, "a");

    assertEquals(
        a.getName(),
        NamedVoid._void()
    );
  }

  @Test
  public void isInstanceOf() {
    PTypeMirror a;
    a = getReturnType(Subject.class, "a");

    assertFalse(a.isInstanceOf(Void.class));
    assertFalse(a.isInstanceOf(Object.class));
    assertFalse(a.isInstanceOf(Integer.class));
  }

  @Test
  public void isNoType() {
    PTypeMirror a;
    a = getReturnType(Subject.class, "a");

    assertTrue(a.isNoType());

    PTypeMirror notOfType;
    notOfType = getReturnType(Subject.class, "notOfType");

    assertFalse(notOfType.isNoType());

    TypeElement objectTypeElement;
    objectTypeElement = getTypeElement(Object.class);

    TypeMirror objectSuperclassType;
    objectSuperclassType = objectTypeElement.getSuperclass();

    PTypeMirror objectSuperclassPType;
    objectSuperclassPType = PTypeMirror.adapt(processingEnv, objectSuperclassType);

    assertTrue(objectSuperclassPType.isNoType());
  }

  @Test
  public void toNoType() {
    PTypeMirror a;
    a = getReturnType(Subject.class, "a");

    PNoType resultA;
    resultA = a.toNoType();

    assertNotNull(resultA);

    PTypeMirror notOfType;
    notOfType = getReturnType(Subject.class, "notOfType");

    try {
      notOfType.toNoType();

      Assert.fail();
    } catch (AssertionError expected) {
      String message;
      message = expected.getMessage();

      assertTrue(message.contains("See isNoType()"));
    }
  }

  private abstract static class Subject {

    abstract void a();

    abstract Object notOfType();

  }

}
