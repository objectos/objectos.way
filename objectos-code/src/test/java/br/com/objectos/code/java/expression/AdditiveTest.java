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

public class AdditiveTest extends AbstractCodeJavaTest {

  public interface Adapter {

    AdditiveExpression additiveExpression();

  }

  private final Adapter adapter;

  private AdditiveTest(Adapter adapter) {
    this.adapter = adapter;
  }

  public static AdditiveTest with(Adapter adapter) {
    return new AdditiveTest(adapter);
  }

  @Test
  public void addTest() {
    test(additiveExpression().add(id("y")), additiveExpression(" + y"));
  }

  @Test
  public void subtractTest() {
    test(additiveExpression().subtract(id("y")), additiveExpression(" - y"));
  }

  private AdditiveExpression additiveExpression() {
    return adapter.additiveExpression();
  }

  private String additiveExpression(String trailer) {
    return adapter.additiveExpression() + trailer;
  }

}
