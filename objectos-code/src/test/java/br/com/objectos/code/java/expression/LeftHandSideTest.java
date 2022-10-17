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
