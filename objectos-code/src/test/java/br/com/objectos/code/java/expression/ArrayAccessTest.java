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

import static br.com.objectos.code.java.expression.Expressions.aget;
import static br.com.objectos.code.java.expression.Expressions.arrayAccess;
import static br.com.objectos.code.java.expression.Expressions.id;
import static br.com.objectos.code.java.expression.Expressions.invoke;
import static br.com.objectos.code.java.expression.Expressions.l;

import br.com.objectos.code.util.AbstractCodeJavaTest;
import java.util.Arrays;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

public class ArrayAccessTest extends AbstractCodeJavaTest
    implements
    AdditiveTest.Adapter,
    ArrayReferenceExpressionTest.Adapter,
    CalleeTest.Adapter,
    ConditionalTest.Adapter,
    FieldAccessReferenceExpressionTest.Adapter,
    LeftHandSideTest.Adapter,
    MethodReferenceReferenceExpressionTest.Adapter,
    MultiplicativeTest.Adapter,
    PostfixExpressionTest.Adapter,
    RelationalTest.Adapter,
    ShiftTest.Adapter {

  @Factory
  public Object[] testFactory() {
    return new Object[] {
        AdditiveTest.with(this),
        ArrayReferenceExpressionTest.with(this),
        CalleeTest.with(this),
        ConditionalTest.with(this),
        FieldAccessReferenceExpressionTest.with(this),
        LeftHandSideTest.with(this),
        MethodReferenceReferenceExpressionTest.with(this),
        MultiplicativeTest.with(this),
        PostfixExpressionTest.with(this),
        RelationalTest.with(this),
        ShiftTest.with(this)
    };
  }

  @Override
  public final AdditiveExpression additiveExpression() {
    return multiplicativeExpression();
  }

  @Override
  public final ArrayReferenceExpression arrayReferenceExpression() {
    return id("x").aget(invoke("y"));
  }

  @Override
  public final Callee callee() {
    return id("objArray").aget(l(1));
  }

  @Override
  public final ConditionalAndExpression conditionalAndExpression() {
    return id("condition").aget(l(0));
  }

  @Override
  public final ConditionalOrExpression conditionalOrExpression() {
    return conditionalAndExpression();
  }

  @Override
  public final FieldAccessReferenceExpression fieldAccessReferenceExpression() {
    return id("fieldAccess").aget(l(123));
  }

  @Override
  public final LeftHandSide leftHandSide() {
    return id("lhs").aget(invoke("index"));
  }

  @Override
  public final MethodReferenceReferenceExpression methodReferenceReferenceExpression() {
    return id("ref").aget(l(1));
  }

  @Override
  public final MultiplicativeExpression multiplicativeExpression() {
    return id("mul").aget(l(45));
  }

  @Override
  public final PostfixExpression postfixExpression() {
    return id("post").aget(l(9));
  }

  @Override
  public final RelationalExpression relationalExpression() {
    return id("lhs").aget(l(1));
  }

  @Override
  public final ShiftExpression shiftExpression() {
    return id("shift").aget(l(0));
  }

  @Test
  public void agetTest() {
    test(aget(id("a"), l(0)), "a[0]");
    test(aget(id("a"), l(0), invoke("y")), "a[0][y()]");
    test(aget(id("a"), id("x"), id("y"), id("z")), "a[x][y][z]");
    test(aget(id("a"), l(1), l(2), l(3), l(4)), "a[1][2][3][4]");
    test(aget(id("a"), Arrays.asList(
        l(1), l(2), l(3), l(4), l(5)
    )), "a[1][2][3][4][5]");
  }

  @Test
  public void arrayAccessTest() {
    test(arrayAccess(id("a"), l(0)), "a[0]");
    test(arrayAccess(id("a"), l(0), invoke("y")), "a[0][y()]");
    test(arrayAccess(id("a"), id("x"), id("y"), id("z")), "a[x][y][z]");
    test(arrayAccess(id("a"), l(1), l(2), l(3), l(4)), "a[1][2][3][4]");
    test(arrayAccess(id("a"), Arrays.asList(
        l(1), l(2), l(3), l(4), l(5)
    )), "a[1][2][3][4][5]");
  }

}
