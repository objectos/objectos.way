/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.java.expression;

import static br.com.objectos.code.java.expression.Expressions.id;

import br.com.objectos.code.util.AbstractCodeJavaTest;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

public class IdentifierTest extends AbstractCodeJavaTest
    implements
    AdditiveTest.Adapter,
    ArrayReferenceExpressionTest.Adapter,
    CalleeTest.Adapter,
    ConditionalTest.Adapter,
    EqualityTest.Adapter,
    LeftHandSideTest.Adapter,
    MethodReferenceReferenceExpressionTest.Adapter,
    MultiplicativeTest.Adapter,
    PostfixExpressionTest.Adapter,
    RelationalTest.Adapter,
    ShiftTest.Adapter {

  @Factory
  public Object[] testFactory() {
    return new Object[] {
        AdditiveTest.with(this),
        ArrayReferenceExpressionTest.with(this),
        CalleeTest.with(this),
        ConditionalTest.with(this),
        EqualityTest.with(this),
        LeftHandSideTest.with(this),
        MethodReferenceReferenceExpressionTest.with(this),
        MultiplicativeTest.with(this),
        PostfixExpressionTest.with(this),
        RelationalTest.with(this),
        ShiftTest.with(this)
    };
  }

  @Override
  public final AdditiveExpression additiveExpression() {
    return id("add");
  }

  @Override
  public final ArrayReferenceExpression arrayReferenceExpression() {
    return id("a");
  }

  @Override
  public final Callee callee() {
    return id("foo");
  }

  @Override
  public final ConditionalAndExpression conditionalAndExpression() {
    return id("cond");
  }

  @Override
  public final ConditionalOrExpression conditionalOrExpression() {
    return conditionalAndExpression();
  }

  @Override
  public final EqualityExpression equalityExpression() {
    return id("equality");
  }

  @Override
  public final LeftHandSide leftHandSide() {
    return id("lhs");
  }

  @Override
  public final MethodReferenceReferenceExpression methodReferenceReferenceExpression() {
    return id("ref");
  }

  @Override
  public final MultiplicativeExpression multiplicativeExpression() {
    return id("mul");
  }

  @Override
  public final PostfixExpression postfixExpression() {
    return id("post");
  }

  @Override
  public final RelationalExpression relationalExpression() {
    return id("rel");
  }

  @Override
  public final ShiftExpression shiftExpression() {
    return id("shift");
  }

  @Test
  public void idTest() {
    test(id("a").id("b").id("c"), "a.b.c");
  }

  @Test
  public void postDec() {
    testToString(id("i").postDec(), "i--");
  }

  @Test
  public void postInc() {
    testToString(id("i").postInc(), "i++");
  }

}