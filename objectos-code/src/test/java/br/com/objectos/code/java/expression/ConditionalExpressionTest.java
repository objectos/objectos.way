/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.java.expression;

import static br.com.objectos.code.java.expression.Expressions.invoke;
import static br.com.objectos.code.java.expression.Expressions.lambda;
import static br.com.objectos.code.java.expression.Expressions.ternary;

import br.com.objectos.code.util.AbstractCodeJavaTest;
import org.testng.annotations.Test;

public class ConditionalExpressionTest extends AbstractCodeJavaTest {

  @Test
  public void fromExpressions() {
    test(ternary(invoke("cond"), invoke("a"), invoke("b")),
        "cond() ? a() : b()");
    test(ternary(invoke("cond"), lambda(invoke("a")), lambda(invoke("b"))),
        "cond() ? () -> a() : () -> b()");
  }

  @Test
  public void fromConditionalOrExpression() {
    test(invoke("cond").ternary(invoke("a"), invoke("b")),
        "cond() ? a() : b()");
  }

}
