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
import java.lang.annotation.ElementType;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

public class ExpressionNameTest extends AbstractCodeJavaTest
    implements
    AdditiveTest.Adapter,
    ArrayReferenceExpressionTest.Adapter,
    CalleeTest.Adapter,
    ConditionalTest.Adapter,
    EqualityTest.Adapter,
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
        MethodReferenceReferenceExpressionTest.with(this),
        MultiplicativeTest.with(this),
        PostfixExpressionTest.with(this),
        RelationalTest.with(this),
        ShiftTest.with(this)
    };
  }

  @Override
  public final AdditiveExpression additiveExpression() {
    return multiplicativeExpression();
  }

  @Override
  public final ArrayReferenceExpression arrayReferenceExpression() {
    return Expressions.expressionName(id("a"), id("b"));
  }

  @Override
  public final Callee callee() {
    return Expressions.expressionName(id("a"), id("b"));
  }

  @Override
  public final ConditionalAndExpression conditionalAndExpression() {
    return Expressions.expressionName(id("a"), id("b"));
  }

  @Override
  public final ConditionalOrExpression conditionalOrExpression() {
    return conditionalAndExpression();
  }

  @Override
  public final EqualityExpression equalityExpression() {
    return Expressions.expressionName(id("a"), id("b"));
  }

  @Override
  public final MethodReferenceReferenceExpression methodReferenceReferenceExpression() {
    return Expressions.expressionName(id("a"), id("b"));
  }

  @Override
  public final MultiplicativeExpression multiplicativeExpression() {
    return Expressions.expressionName(id("foo"), id("mul"));
  }

  @Override
  public final PostfixExpression postfixExpression() {
    return Expressions.expressionName(id("foo"), id("post"));
  }

  @Override
  public final RelationalExpression relationalExpression() {
    return Expressions.expressionName(ElementType.class, "TYPE");
  }

  @Override
  public final ShiftExpression shiftExpression() {
    return Expressions.expressionName(id("a"), id("b"));
  }

  @Test
  public void _expressionName() {
    test(Expressions.expressionName(ElementType.class, "TYPE"),
        "java.lang.annotation.ElementType.TYPE");
  }

  @Test
  public void _expressionName_andThenInvoke() {
    test(Expressions.expressionName(ElementType.class, "TYPE").invoke("name"),
        "java.lang.annotation.ElementType.TYPE.name()");
  }

  @Test
  public void expressionNameFromAnotherExpressionName() {
    test(Expressions.expressionName(id("reference"), "field"), "reference.field");
    test(Expressions.expressionName(id("reference"), id("field")), "reference.field");
  }

  @Test
  public void idTest() {
    test(Expressions.expressionName(id("a"), id("b")).id("c"), "a.b.c");
    test(Expressions.expressionName(id("a"), id("b")).id(id("c")), "a.b.c");
  }

}
