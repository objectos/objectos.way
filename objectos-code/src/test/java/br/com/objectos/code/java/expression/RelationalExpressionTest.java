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
import static br.com.objectos.code.java.type.NamedTypes.t;

import br.com.objectos.code.util.AbstractCodeJavaTest;
import java.util.List;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

public class RelationalExpressionTest extends AbstractCodeJavaTest
    implements
    RelationalTest.Adapter {

  @Factory
  public Object[] testFactory() {
    return new Object[] {
        RelationalTest.with(this)
    };
  }

  @Override
  public final RelationalExpression relationalExpression() {
    return Expressions.lt(id("x"), id("y"));
  }

  @Test
  public void fromExpressions() {
    test(Expressions.lt(id("x"), id("y")), "x < y");
    test(Expressions.gt(id("x"), id("y")), "x > y");
    test(Expressions.le(id("x"), id("y")), "x <= y");
    test(Expressions.ge(id("x"), id("y")), "x >= y");
    test(Expressions.instanceOf(id("coll"), t(List.class)),
        "coll instanceof java.util.List");
  }

  @Test
  public void ternary() {
    test(id("coll").instanceOf(t(List.class)).ternary(id("list"), id("notList")),
        "coll instanceof java.util.List ? list : notList");
  }

}
