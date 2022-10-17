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
package br.com.objectos.code.java.element;

import static br.com.objectos.code.java.expression.Expressions.l;

import br.com.objectos.code.java.expression.MethodReferenceReferenceExpression;
import br.com.objectos.code.java.expression.MethodReferenceReferenceExpressionTest;
import br.com.objectos.code.util.AbstractCodeJavaTest;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

public class SuperKeywordTest extends AbstractCodeJavaTest
    implements
    MethodReferenceReferenceExpressionTest.Adapter {

  @Factory
  public Object[] testFactory() {
    return new Object[] {
        MethodReferenceReferenceExpressionTest.with(this)
    };
  }

  @Override
  public final MethodReferenceReferenceExpression methodReferenceReferenceExpression() {
    return Keywords._super();
  }

  @Test
  public void keywordOnly() {
    test(Keywords._super(), "super");
  }

  @Test
  public void methodInvocation() {
    test(Keywords._super().invoke("foo", l("bar")), "super.foo(\"bar\")");
  }

}
