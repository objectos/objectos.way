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

import static br.com.objectos.code.java.expression.Expressions.divide;
import static br.com.objectos.code.java.expression.Expressions.id;
import static br.com.objectos.code.java.expression.Expressions.l;
import static br.com.objectos.code.java.expression.Expressions.multiply;
import static br.com.objectos.code.java.expression.Expressions.remainder;

import br.com.objectos.code.util.AbstractCodeJavaTest;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

public class MultiplicativeExpressionTest extends AbstractCodeJavaTest
    implements
    AdditiveTest.Adapter,
    MultiplicativeTest.Adapter {

  @Factory
  public Object[] testFactory() {
    return new Object[] {
        AdditiveTest.with(this),
        MultiplicativeTest.with(this)
    };
  }

  @Override
  public final AdditiveExpression additiveExpression() {
    return multiply(id("a"), id("b"));
  }

  @Override
  public final MultiplicativeExpression multiplicativeExpression() {
    return multiply(id("a"), id("b"));
  }

  @Test
  public void divideTest() {
    test(divide(id("x"), id("y")), "x / y");
    test(divide(multiply(id("x"), id("y")), id("z")), "x * y / z");
  }

  @Test
  public void multiplyTest() {
    test(multiply(id("a"), l(10)), "a * 10");
    test(multiply(divide(id("a"), l(10)), l(0.5)), "a / 10 * 0.5");
  }

  @Test
  public void remainderTest() {
    test(remainder(id("x"), id("y")), "x % y");
    test(remainder(remainder(id("x"), id("y")), id("z")), "x % y % z");
  }

}
