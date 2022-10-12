/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.java.expression;

import static br.com.objectos.code.java.element.Keywords._super;
import static br.com.objectos.code.java.element.Keywords._this;
import static br.com.objectos.code.java.expression.Expressions.id;
import static br.com.objectos.code.java.expression.Expressions.invoke;
import static br.com.objectos.code.java.type.NamedTypes.t;

import br.com.objectos.code.util.AbstractCodeJavaTest;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

public class FieldAccessTest extends AbstractCodeJavaTest
    implements
    AdditiveTest.Adapter,
    CalleeTest.Adapter,
    ConditionalTest.Adapter,
    EqualityTest.Adapter,
    FieldAccessReferenceExpressionTest.Adapter,
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
        CalleeTest.with(this),
        ConditionalTest.with(this),
        EqualityTest.with(this),
        FieldAccessReferenceExpressionTest.with(this),
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
    return multiplicativeExpression();
  }

  @Override
  public final Callee callee() {
    return Expressions.fieldAccess(invoke("a"), "field");
  }

  @Override
  public final ConditionalAndExpression conditionalAndExpression() {
    return Expressions.fieldAccess(invoke("x"), "y");
  }

  @Override
  public final ConditionalOrExpression conditionalOrExpression() {
    return conditionalAndExpression();
  }

  @Override
  public final EqualityExpression equalityExpression() {
    return Expressions.fieldAccess(t(FieldAccessTest.class)._super(), "field");
  }

  @Override
  public final FieldAccessReferenceExpression fieldAccessReferenceExpression() {
    return Expressions.fieldAccess(invoke("a"), id("b"));
  }
  
  @Override
  public final LeftHandSide leftHandSide() {
    return Expressions.fieldAccess(_this(), "name");
  }

  @Override
  public final MethodReferenceReferenceExpression methodReferenceReferenceExpression() {
    return Expressions.fieldAccess(invoke("a"), id("b"));
  }

  @Override
  public final MultiplicativeExpression multiplicativeExpression() {
    return Expressions.fieldAccess(invoke("mul"), "field");
  }

  @Override
  public final PostfixExpression postfixExpression() {
    return Expressions.fieldAccess(invoke("post"), "field");
  }

  @Override
  public final RelationalExpression relationalExpression() {
    return Expressions.fieldAccess(_super(), "field");
  }

  @Override
  public final ShiftExpression shiftExpression() {
    return Expressions.fieldAccess(invoke("a"), id("b"));
  }

  @Test
  public void fieldAccessUsingPrimary() {
    test(Expressions.fieldAccess(invoke("a"), "field"), "a().field");
    test(Expressions.fieldAccess(invoke("a"), id("field")), "a().field");
  }

  @Test
  public void fieldAccessUsingSuper() {
    test(Expressions.fieldAccess(_super(), "field"), "super.field");
    test(Expressions.fieldAccess(_super(), id("field")), "super.field");
  }

  @Test
  public void fieldAccessUsingQualifiedSuper() {
    test(Expressions.fieldAccess(t(FieldAccessTest.class)._super(), "field"),
        "br.com.objectos.code.java.expression.FieldAccessTest.super.field");
    test(Expressions.fieldAccess(t(FieldAccessTest.class)._super(), id("field")),
        "br.com.objectos.code.java.expression.FieldAccessTest.super.field");
  }

}
