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

import static br.com.objectos.code.java.expression.Expressions.hint;
import static br.com.objectos.code.java.type.NamedTypes.t;

import br.com.objectos.code.util.AbstractCodeJavaTest;
import org.testng.annotations.Test;

public class MethodReferenceReferenceExpressionTest extends AbstractCodeJavaTest {

  public interface Adapter {

    MethodReferenceReferenceExpression methodReferenceReferenceExpression();
    
  }

  private final Adapter adapter;

  private MethodReferenceReferenceExpressionTest(Adapter adapter) {
    this.adapter = adapter;
  }

  public static MethodReferenceReferenceExpressionTest with(Adapter adapter) {
    return new MethodReferenceReferenceExpressionTest(adapter);
  }
  
  @Test
  public void refTest() {
    MethodReferenceReferenceExpression exp = methodReferenceReferenceExpression();
    test(exp.ref("method"), exp + "::method");
    test(exp.ref(hint(t(String.class)), "method"), exp + "::<java.lang.String> method");
  }
  
  private MethodReferenceReferenceExpression methodReferenceReferenceExpression() {
    return adapter.methodReferenceReferenceExpression();
  }

}
