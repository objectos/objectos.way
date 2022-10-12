/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.java.expression;

import static br.com.objectos.code.java.expression.Expressions.id;
import static br.com.objectos.code.java.expression.Expressions.invoke;
import static br.com.objectos.code.java.expression.Expressions.l;

import br.com.objectos.code.util.AbstractCodeJavaTest;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

public class ShiftExpressionTest extends AbstractCodeJavaTest
    implements
    ShiftTest.Adapter {

  @Factory
  public Object[] testFactory() {
    return new Object[] {
        ShiftTest.with(this)
    };
  }

  @Override
  public final ShiftExpression shiftExpression() {
    return Expressions.leftShift(id("a"), id("b"));
  }

  @Test
  public void leftShiftTest() {
    test(Expressions.leftShift(id("i"), l(1)), "i << 1");
    test(Expressions.leftShift(id("i"), invoke("shift")), "i << shift()");
  }

  @Test
  public void rightShiftTest() {
    test(Expressions.rightShift(l(8), l(2)), "8 >> 2");
    test(Expressions.rightShift(id("j"), invoke("shift")), "j >> shift()");
  }

  @Test
  public void unsignedRightShift() {
    test(Expressions.unsignedRightShift(id("a"), id("b")), "a >>> b");
  }

}
