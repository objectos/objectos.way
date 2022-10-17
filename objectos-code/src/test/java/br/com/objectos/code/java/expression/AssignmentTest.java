/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
