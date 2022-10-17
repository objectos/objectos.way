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

import br.com.objectos.code.util.AbstractCodeJavaTest;
import org.testng.annotations.Test;

public class EqualityTest extends AbstractCodeJavaTest {

  public interface Adapter {

    EqualityExpression equalityExpression();

  }

  private final Adapter adapter;

  private EqualityTest(Adapter adapter) {
    this.adapter = adapter;
  }

  public static EqualityTest with(Adapter adapter) {
    return new EqualityTest(adapter);
  }

  @Test
  public void equalTo() {
    test(equalityExpression().eq(_this()), equalityExpression(" == this"));
  }

  @Test
  public void notEqualTo() {
    test(equalityExpression().ne(_this()), equalityExpression(" != this"));
  }

  private EqualityExpression equalityExpression() {
    return adapter.equalityExpression();
  }

  private String equalityExpression(String trailer) {
    return equalityExpression() + trailer;
  }

}
