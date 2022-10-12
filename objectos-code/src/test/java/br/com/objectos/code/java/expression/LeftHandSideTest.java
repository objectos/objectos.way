/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.java.expression;

import static br.com.objectos.code.java.expression.Expressions.invoke;

import br.com.objectos.code.util.AbstractCodeJavaTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LeftHandSideTest extends AbstractCodeJavaTest {

  public interface Adapter {

    LeftHandSide leftHandSide();

  }

  private final Adapter adapter;

  private LeftHandSideTest(Adapter adapter) {
    this.adapter = adapter;
  }

  public static LeftHandSideTest with(Adapter adapter) {
    return new LeftHandSideTest(adapter);
  }

  @Test
  public void receive() {
    test(leftHandSide().receive(invoke("y")),
        leftHandSide(
            " = y()"
        )
    );
  }

  @DataProvider
  public Object[][] assignmentOperatorProvider() {
    return new Object[][] {
        {AssignmentOperator.MULTIPLICATION, " *= y()"},
        {AssignmentOperator.DIVISION, " /= y()"},
        {AssignmentOperator.REMAINDER, " %= y()"},
        {AssignmentOperator.ADDITION, " += y()"},
        {AssignmentOperator.SUBTRACTION, " -= y()"},
        {AssignmentOperator.LEFT_SHIFT, " <<= y()"},
        {AssignmentOperator.RIGHT_SHIFT, " >>= y()"},
        {AssignmentOperator.UNSIGNED_RIGHT_SHIFT, " >>>= y()"},
        {AssignmentOperator.BITWISE_AND, " &= y()"},
        {AssignmentOperator.BITWISE_XOR, " ^= y()"},
        {AssignmentOperator.BITWISE_OR, " |= y()"}
    };
  }

  @Test(dataProvider = "assignmentOperatorProvider")
  public void receiveTest(AssignmentOperator operator, String expected) {
    test(leftHandSide().receive(operator, invoke("y")),
        leftHandSide(
            expected
        )
    );
  }
  
  private LeftHandSide leftHandSide() {
    return adapter.leftHandSide();
  }

  private String leftHandSide(String trailer) {
    return leftHandSide() + trailer;
  }

}
