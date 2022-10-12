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
import org.testng.annotations.Test;

public class ShiftTest extends AbstractCodeJavaTest {

  public interface Adapter {

    ShiftExpression shiftExpression();

  }

  private final Adapter adapter;

  private ShiftTest(Adapter adapter) {
    this.adapter = adapter;
  }

  public static ShiftTest with(Adapter adapter) {
    return new ShiftTest(adapter);
  }

  @Test
  public void leftShiftTest() {
    ShiftExpression e = shiftExpression();
    test(e.leftShift(l(1)), e + " << 1");
    test(e.leftShift(invoke("shift")), e + " << shift()");
  }

  @Test
  public void rightShiftTest() {
    ShiftExpression e = shiftExpression();
    test(e.rightShift(l(2)), e + " >> 2");
    test(e.rightShift(invoke("shift")), e + " >> shift()");
  }

  @Test
  public void unsignedRightShift() {
    ShiftExpression e = shiftExpression();
    test(e.unsignedRightShift(id("b")), e + " >>> b");
  }

  private ShiftExpression shiftExpression() {
    return adapter.shiftExpression();
  }

}
