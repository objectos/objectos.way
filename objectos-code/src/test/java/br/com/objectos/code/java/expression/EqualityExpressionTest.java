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

import static br.com.objectos.code.java.element.Keywords._this;
import static br.com.objectos.code.java.expression.Expressions.id;

import br.com.objectos.code.util.AbstractCodeJavaTest;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

public class EqualityExpressionTest extends AbstractCodeJavaTest
    implements
    ConditionalTest.Adapter {

  @Factory
  public Object[] testFactory() {
    return new Object[] {
        ConditionalTest.with(this)
    };
  }

  @Override
  public final ConditionalAndExpression conditionalAndExpression() {
    return Expressions.eq(id("a"), id("b"));
  }

  @Override
  public final ConditionalOrExpression conditionalOrExpression() {
    return conditionalAndExpression();
  }

  @Test
  public void equaliltyExpression() {
    test(Expressions.eq(id("obj"), _this()), "obj == this");
    test(Expressions.ne(id("obj"), _this()), "obj != this");
  }

}