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

import static br.com.objectos.code.java.expression.Expressions.id;
import static br.com.objectos.code.java.expression.Expressions.parens;
import static br.com.objectos.code.java.type.NamedTypes.t;

import br.com.objectos.code.util.AbstractCodeJavaTest;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

public class UnaryExpressionTest extends AbstractCodeJavaTest
    implements
    ShiftTest.Adapter {

  @Factory
  public Object[] testFactory() {
    return new Object[] {
        ShiftTest.with(this)
    };
  }

  @Override
  public final ShiftExpression shiftExpression() {
    return Expressions.unaryMinus(id("j"));
  }

  @Test
  public void notTest() {
    test(Expressions.not(parens(id("obj").instanceOf(t(CharSequence.class)))),
        "!(obj instanceof java.lang.CharSequence)");
  }

  @Test
  public void unaryMinusTest() {
    test(Expressions.unaryMinus(id("i")), "-i");
  }

  @Test
  public void unaryPlusTest() {
    test(Expressions.unaryPlus(id("i")), "+i");
  }

}
