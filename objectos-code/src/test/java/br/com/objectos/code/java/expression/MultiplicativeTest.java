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
import org.testng.annotations.Test;

public class MultiplicativeTest extends AbstractCodeJavaTest {

  public interface Adapter {

    MultiplicativeExpression multiplicativeExpression();

  }

  private final Adapter adapter;

  private MultiplicativeTest(Adapter adapter) {
    this.adapter = adapter;
  }

  public static MultiplicativeTest with(Adapter adapter) {
    return new MultiplicativeTest(adapter);
  }

  @Test
  public void divideTest() {
    test(multiplicativeExpression().divide(id("y")), multiplicativeExpression(" / y"));
  }

  @Test
  public void multiplyTest() {
    test(multiplicativeExpression().multiply(id("y")), multiplicativeExpression(" * y"));
  }

  @Test
  public void remainderTest() {
    test(multiplicativeExpression().remainder(id("y")), multiplicativeExpression(" % y"));
  }

  private MultiplicativeExpression multiplicativeExpression() {
    return adapter.multiplicativeExpression();
  }

  private String multiplicativeExpression(String trailer) {
    return adapter.multiplicativeExpression() + trailer;
  }

}
