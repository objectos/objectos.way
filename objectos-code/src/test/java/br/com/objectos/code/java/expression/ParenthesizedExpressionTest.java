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

import static br.com.objectos.code.java.expression.Expressions.cast;
import static br.com.objectos.code.java.expression.Expressions.id;
import static br.com.objectos.code.java.expression.Expressions.parens;
import static br.com.objectos.code.java.type.NamedTypes._int;
import static br.com.objectos.code.java.type.NamedTypes.t;

import br.com.objectos.code.util.AbstractCodeJavaTest;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

public class ParenthesizedExpressionTest extends AbstractCodeJavaTest
    implements
    AdditiveTest.Adapter,
    CalleeTest.Adapter,
    ConditionalTest.Adapter,
    EqualityTest.Adapter,
    MethodReferenceReferenceExpressionTest.Adapter,
    MultiplicativeTest.Adapter,
    PostfixExpressionTest.Adapter,
    RelationalTest.Adapter {

  @Override
  public final AdditiveExpression additiveExpression() {
    return parens(cast(_int(), id("add")));
  }

  @Override
  public final Callee callee() {
    return parens(Expressions.cast(t(String.class), id("foo")));
  }

  @Override
  public final ConditionalAndExpression conditionalAndExpression() {
    return parens(id("cond"));
  }

  @Override
  public final ConditionalOrExpression conditionalOrExpression() {
    return conditionalAndExpression();
  }

  @Override
  public final EqualityExpression equalityExpression() {
    return parens(id("equality"));
  }

  @Override
  public final MethodReferenceReferenceExpression methodReferenceReferenceExpression() {
    return parens(id("ref"));
  }

  @Override
  public final MultiplicativeExpression multiplicativeExpression() {
    return parens(cast(_int(), id("mul")));
  }

  @Test
  public void parensTest() {
    test(parens(id("obj").instanceOf(t(CharSequence.class))),
        "(obj instanceof java.lang.CharSequence)");
  }

  @Override
  public final PostfixExpression postfixExpression() {
    return parens(cast(_int(), id("postfix")));
  }

  @Override
  public final RelationalExpression relationalExpression() {
    return parens(id("rel"));
  }

  @Factory
  public Object[] testFactory() {
    return new Object[] {
        AdditiveTest.with(this),
        CalleeTest.with(this),
        ConditionalTest.with(this),
        EqualityTest.with(this),
        MethodReferenceReferenceExpressionTest.with(this),
        MultiplicativeTest.with(this),
        PostfixExpressionTest.with(this),
        RelationalTest.with(this)
    };
  }

}
