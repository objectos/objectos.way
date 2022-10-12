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

public class FieldAccessReferenceExpressionTest extends AbstractCodeJavaTest {

  public interface Adapter {

    FieldAccessReferenceExpression fieldAccessReferenceExpression();

  }

  private final Adapter adapter;

  private FieldAccessReferenceExpressionTest(Adapter adapter) {
    this.adapter = adapter;
  }

  public static FieldAccessReferenceExpressionTest with(Adapter adapter) {
    return new FieldAccessReferenceExpressionTest(adapter);
  }

  @Test
  public void idTest() {
    test(fieldAccessReferenceExpression().id("field"),
        fieldAccessReferenceExpression(".field")
    );
    test(fieldAccessReferenceExpression().id(id("another")),
        fieldAccessReferenceExpression(".another")
    );
  }

  private FieldAccessReferenceExpression fieldAccessReferenceExpression() {
    return adapter.fieldAccessReferenceExpression();
  }

  private String fieldAccessReferenceExpression(String trailer) {
    return fieldAccessReferenceExpression() + trailer;
  }

}
