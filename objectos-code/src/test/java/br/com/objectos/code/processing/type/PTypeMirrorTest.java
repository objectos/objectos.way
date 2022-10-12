/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.processing.type;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import br.com.objectos.code.util.AbstractCodeCoreTest;
import java.util.List;
import javax.lang.model.type.TypeMirror;
import org.testng.annotations.Test;

public class PTypeMirrorTest extends AbstractCodeCoreTest {

  @Test
  public void isPrimitive() {
    assertTrue(returnInt().isPrimitiveType());
    assertFalse(returnString().isPrimitiveType());
    assertFalse(returnVoid().isPrimitiveType());
  }

  @Test
  public void isVoid() {
    assertFalse(returnInt().isNoType());
    assertFalse(returnString().isNoType());
    assertTrue(returnVoid().isNoType());
  }

  private PTypeMirror returnInt() {
    return returnNamed("returnInt");
  }

  private PTypeMirror returnNamed(String name) {
    TypeMirror returnType = getMethodElement(Subject.class, name).getReturnType();
    return PTypeMirror.adapt(processingEnv, returnType);
  }

  private PTypeMirror returnString() {
    return returnNamed("returnString");
  }

  private PTypeMirror returnVoid() {
    return returnNamed("returnVoid");
  }

  abstract class Subject {

    abstract List<Integer> m0();

    abstract int returnInt();

    abstract String returnString();

    abstract void returnVoid();
  }

}