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

import br.com.objectos.code.util.AbstractCodeJavaTest;
import org.testng.annotations.Test;

public class MultiplicativeTest extends AbstractCodeJavaTest {

  public interface Adapter {

    MultiplicativeExpression multiplicativeExpression();

  }

  private final Adapter adapter;

  private MultiplicativeTest(Adapter adapter) {
    this.adapter = adapter;
  }

  public static MultiplicativeTest with(Adapter adapter) {
    return new MultiplicativeTest(adapter);
  }

  @Test
  public void divideTest() {
    test(multiplicativeExpression().divide(id("y")), multiplicativeExpression(" / y"));
  }

  @Test
  public void multiplyTest() {
    test(multiplicativeExpression().multiply(id("y")), multiplicativeExpression(" * y"));
  }

  @Test
  public void remainderTest() {
    test(multiplicativeExpression().remainder(id("y")), multiplicativeExpression(" % y"));
  }

  private MultiplicativeExpression multiplicativeExpression() {
    return adapter.multiplicativeExpression();
  }

  private String multiplicativeExpression(String trailer) {
    return adapter.multiplicativeExpression() + trailer;
  }

}
