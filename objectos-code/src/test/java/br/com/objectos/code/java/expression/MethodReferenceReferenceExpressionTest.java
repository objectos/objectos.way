/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.java.expression;

import static br.com.objectos.code.java.expression.Expressions.hint;
import static br.com.objectos.code.java.type.NamedTypes.t;

import br.com.objectos.code.util.AbstractCodeJavaTest;
import org.testng.annotations.Test;

public class MethodReferenceReferenceExpressionTest extends AbstractCodeJavaTest {

  public interface Adapter {

    MethodReferenceReferenceExpression methodReferenceReferenceExpression();
    
  }

  private final Adapter adapter;

  private MethodReferenceReferenceExpressionTest(Adapter adapter) {
    this.adapter = adapter;
  }

  public static MethodReferenceReferenceExpressionTest with(Adapter adapter) {
    return new MethodReferenceReferenceExpressionTest(adapter);
  }
  
  @Test
  public void refTest() {
    MethodReferenceReferenceExpression exp = methodReferenceReferenceExpression();
    test(exp.ref("method"), exp + "::method");
    test(exp.ref(hint(t(String.class)), "method"), exp + "::<java.lang.String> method");
  }
  
  private MethodReferenceReferenceExpression methodReferenceReferenceExpression() {
    return adapter.methodReferenceReferenceExpression();
  }

}
