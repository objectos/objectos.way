/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.java.expression;

import static br.com.objectos.code.java.expression.Expressions.assign;
import static br.com.objectos.code.java.expression.Expressions.id;

import br.com.objectos.code.java.element.Keywords;
import br.com.objectos.code.util.AbstractCodeJavaTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class AssignmentTest extends AbstractCodeJavaTest {

  @Test
  public void assignTest() {
    test(assign(Keywords._this().id("name"), id("name")), "this.name = name");
  }

  @DataProvider
  public Object[][] assignmentOperatorProvider() {
    return new Object[][] {
        {AssignmentOperator.MULTIPLICATION, "x *= y"},
        {AssignmentOperator.DIVISION, "x /= y"},
        {AssignmentOperator.REMAINDER, "x %= y"},
        {AssignmentOperator.ADDITION, "x += y"},
        {AssignmentOperator.SUBTRACTION, "x -= y"},
        {AssignmentOperator.LEFT_SHIFT, "x <<= y"},
        {AssignmentOperator.RIGHT_SHIFT, "x >>= y"},
        {AssignmentOperator.UNSIGNED_RIGHT_SHIFT, "x >>>= y"},
        {AssignmentOperator.BITWISE_AND, "x &= y"},
        {AssignmentOperator.BITWISE_XOR, "x ^= y"},
        {AssignmentOperator.BITWISE_OR, "x |= y"}
    };
  }

  @Test(dataProvider = "assignmentOperatorProvider")
  public void assignTest(AssignmentOperator operator, String expected) {
    test(assign(operator, id("x"), id("y")), expected);
  }

}
