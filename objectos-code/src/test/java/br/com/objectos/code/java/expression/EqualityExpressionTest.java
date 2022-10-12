/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.java.expression;

import static br.com.objectos.code.java.element.Keywords._this;
import static br.com.objectos.code.java.expression.Expressions.id;

import br.com.objectos.code.util.AbstractCodeJavaTest;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

public class EqualityExpressionTest extends AbstractCodeJavaTest
    implements
    ConditionalTest.Adapter {

  @Factory
  public Object[] testFactory() {
    return new Object[] {
        ConditionalTest.with(this)
    };
  }

  @Override
  public final ConditionalAndExpression conditionalAndExpression() {
    return Expressions.eq(id("a"), id("b"));
  }

  @Override
  public final ConditionalOrExpression conditionalOrExpression() {
    return conditionalAndExpression();
  }

  @Test
  public void equaliltyExpression() {
    test(Expressions.eq(id("obj"), _this()), "obj == this");
    test(Expressions.ne(id("obj"), _this()), "obj != this");
  }

}