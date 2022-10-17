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

import br.com.objectos.code.util.AbstractCodeJavaTest;
import org.testng.annotations.Test;

public class PostfixExpressionTest extends AbstractCodeJavaTest {

  public interface Adapter {

    PostfixExpression postfixExpression();

  }

  private final Adapter adapter;

  private PostfixExpressionTest(Adapter adapter) {
    this.adapter = adapter;
  }

  public static PostfixExpressionTest with(Adapter adapter) {
    return new PostfixExpressionTest(adapter);
  }
  
  @Test
  public void postDecTest() {
    PostfixExpression expression = postfixExpression();
    test(expression.postDec(), expression.toString() + "--");
  }
  
  @Test
  public void postIncTest() {
    PostfixExpression expression = postfixExpression();
    test(expression.postInc(), expression.toString() + "++");
  }
  
  private PostfixExpression postfixExpression() {
    return adapter.postfixExpression();
  }

}
