/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.java.element;

import static br.com.objectos.code.java.expression.Expressions.l;

import br.com.objectos.code.java.expression.MethodReferenceReferenceExpression;
import br.com.objectos.code.java.expression.MethodReferenceReferenceExpressionTest;
import br.com.objectos.code.util.AbstractCodeJavaTest;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

public class SuperKeywordTest extends AbstractCodeJavaTest
    implements
    MethodReferenceReferenceExpressionTest.Adapter {

  @Factory
  public Object[] testFactory() {
    return new Object[] {
        MethodReferenceReferenceExpressionTest.with(this)
    };
  }

  @Override
  public final MethodReferenceReferenceExpression methodReferenceReferenceExpression() {
    return Keywords._super();
  }

  @Test
  public void keywordOnly() {
    test(Keywords._super(), "super");
  }

  @Test
  public void methodInvocation() {
    test(Keywords._super().invoke("foo", l("bar")), "super.foo(\"bar\")");
  }

}
