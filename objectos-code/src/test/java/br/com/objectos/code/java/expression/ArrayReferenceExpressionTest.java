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
import static br.com.objectos.code.java.expression.Expressions.l;
import static java.util.Arrays.asList;

import br.com.objectos.code.util.AbstractCodeJavaTest;
import org.testng.annotations.Test;

public class ArrayReferenceExpressionTest extends AbstractCodeJavaTest {

  public interface Adapter {

    ArrayReferenceExpression arrayReferenceExpression();

  }

  private final ArrayReferenceExpressionTest.Adapter adapter;

  private ArrayReferenceExpressionTest(ArrayReferenceExpressionTest.Adapter adapter) {
    this.adapter = adapter;
  }

  public static ArrayReferenceExpressionTest with(Adapter adapter) {
    return new ArrayReferenceExpressionTest(adapter);
  }

  @Test
  public void agetTest() {
    test(arrayReferenceExpression().aget(l(0)),
        arrayReferenceExpression(
            "[0]"
        )
    );
    test(arrayReferenceExpression().aget(l(0), invoke("y")),
        arrayReferenceExpression(
            "[0][y()]"
        )
    );
    test(arrayReferenceExpression().aget(id("x"), id("y"), id("z")),
        arrayReferenceExpression(
            "[x][y][z]"
        )
    );
    test(arrayReferenceExpression().aget(l(1), l(2), l(3), l(4)),
        arrayReferenceExpression(
            "[1][2][3][4]"
        )
    );
    test(arrayReferenceExpression().aget(asList(l(1), l(2), l(3), l(4), l(5))),
        arrayReferenceExpression(
            "[1][2][3][4][5]"
        )
    );
  }

  @Test
  public void arrayAccessTest() {
    test(arrayReferenceExpression().arrayAccess(l(0)),
        arrayReferenceExpression(
            "[0]"
        )
    );
    test(arrayReferenceExpression().arrayAccess(l(0), invoke("y")),
        arrayReferenceExpression(
            "[0][y()]"
        )
    );
    test(arrayReferenceExpression().arrayAccess(id("x"), id("y"), id("z")),
        arrayReferenceExpression(
            "[x][y][z]"
        )
    );
    test(arrayReferenceExpression().arrayAccess(l(1), l(2), l(3), l(4)),
        arrayReferenceExpression(
            "[1][2][3][4]"
        )
    );
    test(arrayReferenceExpression().arrayAccess(asList(l(1), l(2), l(3), l(4), l(5))),
        arrayReferenceExpression(
            "[1][2][3][4][5]"
        )
    );
  }

  private ArrayReferenceExpression arrayReferenceExpression() {
    return adapter.arrayReferenceExpression();
  }

  private String arrayReferenceExpression(String trailer) {
    return adapter.arrayReferenceExpression() + trailer;
  }

}
