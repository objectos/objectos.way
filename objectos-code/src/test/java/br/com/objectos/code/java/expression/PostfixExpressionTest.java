/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.java.expression;

import br.com.objectos.code.util.AbstractCodeJavaTest;
import org.testng.annotations.Test;

public class PostfixExpressionTest extends AbstractCodeJavaTest {

  public interface Adapter {

    PostfixExpression postfixExpression();

  }

  private final Adapter adapter;

  private PostfixExpressionTest(Adapter adapter) {
    this.adapter = adapter;
  }

  public static PostfixExpressionTest with(Adapter adapter) {
    return new PostfixExpressionTest(adapter);
  }
  
  @Test
  public void postDecTest() {
    PostfixExpression expression = postfixExpression();
    test(expression.postDec(), expression.toString() + "--");
  }
  
  @Test
  public void postIncTest() {
    PostfixExpression expression = postfixExpression();
    test(expression.postInc(), expression.toString() + "++");
  }
  
  private PostfixExpression postfixExpression() {
    return adapter.postfixExpression();
  }

}
