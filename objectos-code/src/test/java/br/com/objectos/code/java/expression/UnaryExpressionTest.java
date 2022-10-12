/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.java.expression;

import static br.com.objectos.code.java.expression.Expressions.id;
import static br.com.objectos.code.java.expression.Expressions.parens;
import static br.com.objectos.code.java.type.NamedTypes.t;

import br.com.objectos.code.util.AbstractCodeJavaTest;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

public class UnaryExpressionTest extends AbstractCodeJavaTest
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
    return Expressions.unaryMinus(id("j"));
  }

  @Test
  public void notTest() {
    test(Expressions.not(parens(id("obj").instanceOf(t(CharSequence.class)))),
        "!(obj instanceof java.lang.CharSequence)");
  }

  @Test
  public void unaryMinusTest() {
    test(Expressions.unaryMinus(id("i")), "-i");
  }

  @Test
  public void unaryPlusTest() {
    test(Expressions.unaryPlus(id("i")), "+i");
  }

}
