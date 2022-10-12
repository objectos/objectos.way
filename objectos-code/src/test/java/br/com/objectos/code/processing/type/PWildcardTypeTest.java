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

import br.com.objectos.code.java.type.NamedClass;
import br.com.objectos.code.java.type.NamedWildcard;
import br.com.objectos.code.model.element.ProcessingMethod;
import br.com.objectos.code.model.element.ProcessingParameter;
import br.com.objectos.code.util.GetOnly;
import java.util.NoSuchElementException;
import objectos.util.UnmodifiableList;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PWildcardTypeTest extends AbstractPTypeMirrorTest {

  private interface Generic<T> {}

  private abstract static class Subject {
    abstract void lower(Generic<? super Number> lower);
    abstract void notOfType(Generic<Number> notOfType);
    abstract void raw(Generic<?> raw);
    abstract void upper(Generic<? extends Number> upper);
  }

  @Test
  public void getBound() {
    PWildcardType raw;
    raw = getOnlyTypeArgument(Subject.class, "raw").toWildcardType();

    try {
      raw.getBound();
      Assert.fail();
    } catch (NoSuchElementException expected) {

    }

    PWildcardType upper;
    upper = getOnlyTypeArgument(Subject.class, "upper").toWildcardType();

    assertEquals(
      upper.getBound().getName(),
      NamedClass.of(Number.class)
    );
  }

  @Test
  public void getName() {
    PTypeMirror raw;
    raw = getOnlyTypeArgument(Subject.class, "raw");

    assertEquals(
      raw.getName(),
      NamedWildcard.unbound()
    );

    PTypeMirror upper;
    upper = getOnlyTypeArgument(Subject.class, "upper");

    assertEquals(
      upper.getName(),
      NamedWildcard.extendsBound(NamedClass.of(Number.class))
    );

    PTypeMirror lower;
    lower = getOnlyTypeArgument(Subject.class, "lower");

    assertEquals(
      lower.getName(),
      NamedWildcard.superBound(NamedClass.of(Number.class))
    );
  }

  @Test
  public void isWildcardType() {
    PTypeMirror lower;
    lower = getOnlyTypeArgument(Subject.class, "lower");

    assertTrue(lower.isWildcardType());

    PTypeMirror notOfType;
    notOfType = getOnlyTypeArgument(Subject.class, "notOfType");

    assertFalse(notOfType.isWildcardType());
  }

  @Test
  public void toWildcardType() {
    PTypeMirror lower;
    lower = getOnlyTypeArgument(Subject.class, "lower");

    assertNotNull(lower.toWildcardType());

    PTypeMirror notOfType;
    notOfType = getOnlyTypeArgument(Subject.class, "notOfType");

    try {
      notOfType.toWildcardType();

      Assert.fail();
    } catch (AssertionError expected) {
      String message;
      message = expected.getMessage();

      assertTrue(message.contains("See isWildcardType()"));
    }
  }

  private PTypeMirror getOnlyTypeArgument(Class<?> type, String methodName) {
    ProcessingMethod method;
    method = getDeclaredMethod(type, methodName);

    UnmodifiableList<ProcessingParameter> parameters;
    parameters = method.getParameters();

    ProcessingParameter only;
    only = GetOnly.of(parameters);

    PTypeMirror parameterType;
    parameterType = only.getType();

    PDeclaredType declared;
    declared = parameterType.toDeclaredType();

    UnmodifiableList<PTypeMirror> typeArguments;
    typeArguments = declared.getTypeArguments();

    return GetOnly.of(typeArguments);
  }

}
