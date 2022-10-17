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
import java.util.ArrayList;
import org.testng.annotations.Test;

public class RelationalTest extends AbstractCodeJavaTest {

  public interface Adapter {

    RelationalExpression relationalExpression();

  }

  private final Adapter adapter;

  private RelationalTest(Adapter adapter) {
    this.adapter = adapter;
  }

  public static RelationalTest with(Adapter adapter) {
    return new RelationalTest(adapter);
  }

  @Test
  public void numericalComparisons() {
    test(relationalExpression().lt(id("y")), relationalExpression(" < y"));
    test(relationalExpression().gt(id("y")), relationalExpression(" > y"));
    test(relationalExpression().le(id("y")), relationalExpression(" <= y"));
    test(relationalExpression().ge(id("y")), relationalExpression(" >= y"));
  }
  
  @Test
  public void instanceOf() {
    test(relationalExpression().instanceOf(t(ArrayList.class)),
        relationalExpression(
            " instanceof java.util.ArrayList"
        ));
  }
  
  private RelationalExpression relationalExpression() {
    return adapter.relationalExpression();
  }
  
  private String relationalExpression(String trailer) {
    return adapter.relationalExpression() + trailer;
  }

}
