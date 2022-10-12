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

public class AdditiveTest extends AbstractCodeJavaTest {

  public interface Adapter {

    AdditiveExpression additiveExpression();

  }

  private final Adapter adapter;

  private AdditiveTest(Adapter adapter) {
    this.adapter = adapter;
  }

  public static AdditiveTest with(Adapter adapter) {
    return new AdditiveTest(adapter);
  }

  @Test
  public void addTest() {
    test(additiveExpression().add(id("y")), additiveExpression(" + y"));
  }

  @Test
  public void subtractTest() {
    test(additiveExpression().subtract(id("y")), additiveExpression(" - y"));
  }

  private AdditiveExpression additiveExpression() {
    return adapter.additiveExpression();
  }

  private String additiveExpression(String trailer) {
    return adapter.additiveExpression() + trailer;
  }

}
