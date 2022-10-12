/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.java.expression;

import static br.com.objectos.code.java.expression.Expressions.id;
import static br.com.objectos.code.java.expression.Expressions.invoke;

import br.com.objectos.code.util.AbstractCodeJavaTest;
import org.testng.annotations.Test;

public class ConditionalTest extends AbstractCodeJavaTest {

  public interface Adapter {

    ConditionalAndExpression conditionalAndExpression();

    ConditionalOrExpression conditionalOrExpression();

  }

  private final Adapter adapter;

  private ConditionalTest(Adapter adapter) {
    this.adapter = adapter;
  }

  public static ConditionalTest with(Adapter adapter) {
    return new ConditionalTest(adapter);
  }

  @Test
  public void and() {
    test(conditionalAndExpression().and(invoke("anotherCondition")),
        conditionalAndExpression(
            " && anotherCondition()"
        )
    );
    test(conditionalAndExpression().and(id("c1")).and(id("c2")),
        conditionalAndExpression(
            " && c1 && c2"
        )
    );
  }
 
  @Test
  public void or() {
    ConditionalOrExpression e = conditionalOrExpression();
    test(e.or(invoke("orCondition")), e + " || orCondition()");
  }
  
  @Test
  public void ternary() {
    test(conditionalAndExpression().ternary(invoke("ifTrue"), invoke("ifFalse")),
        conditionalAndExpression(
            " ? ifTrue() : ifFalse()"
        )
    );
  }
  
  private ConditionalAndExpression conditionalAndExpression() {
    return adapter.conditionalAndExpression();
  }
  
  private ConditionalOrExpression conditionalOrExpression() {
    return adapter.conditionalOrExpression();
  }

  private String conditionalAndExpression(String trailer) {
    return conditionalAndExpression() + trailer;
  }

}
