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

public class PostDecrementExpressionTest extends AbstractCodeJavaTest {
  
  @Test
  public void postDecTest() {
    test(Expressions.postDec(id("i")), "i--");
  }

}
