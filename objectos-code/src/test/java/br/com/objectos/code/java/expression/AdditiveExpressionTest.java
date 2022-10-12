/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.java.expression;

import static br.com.objectos.code.java.expression.Expressions.add;
import static br.com.objectos.code.java.expression.Expressions.id;
import static br.com.objectos.code.java.expression.Expressions.multiply;
import static br.com.objectos.code.java.expression.Expressions.subtract;

import br.com.objectos.code.util.AbstractCodeJavaTest;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

public class AdditiveExpressionTest extends AbstractCodeJavaTest
    implements
    AdditiveTest.Adapter {

  @Factory
  public Object[] testFactory() {
    return new Object[] {
        AdditiveTest.with(this)
    };
  }

  @Override
  public final AdditiveExpression additiveExpression() {
    return id("a").add(id("b"));
  }

  @Test
  public void addTest() {
    test(add(id("a"), id("b")), "a + b");
    test(add(add(id("a"), id("b")), multiply(id("c"), id("d"))), "a + b + c * d");
  }

  @Test
  public void subtractTest() {
    test(subtract(id("a"), id("b")), "a - b");
    test(subtract(subtract(id("a"), id("b")), multiply(id("c"), id("d"))), "a - b - c * d");
  }

}
