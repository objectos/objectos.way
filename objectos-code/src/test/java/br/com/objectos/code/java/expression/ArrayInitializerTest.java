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

import static br.com.objectos.code.java.expression.Expressions.a;
import static br.com.objectos.code.java.expression.Expressions.id;
import static br.com.objectos.code.java.expression.Expressions.invoke;
import static br.com.objectos.code.java.expression.Expressions.l;
import static br.com.objectos.code.java.expression.NewClass._new;
import static br.com.objectos.code.java.type.NamedTypes.t;

import br.com.objectos.code.java.statement.VariableInitializer;
import br.com.objectos.code.util.AbstractCodeJavaTest;
import org.testng.annotations.Test;

public class ArrayInitializerTest extends AbstractCodeJavaTest {

  @Test
  public void aTest() {
    test(a(), "{}");
    test(a(l(1)), "{1}");
    test(a(l(1), l(2)), "{1, 2}");
    test(a(l(1), l(2), l(3)), "{1, 2, 3}");
    test(a(l(1), l(2), l(3), l(4)), "{1, 2, 3, 4}");
    test(a(l(1), l(2), l(3), l(4), l(5)), "{1, 2, 3, 4, 5}");
    test(a(l(1), l(2), l(3), l(4), l(5), l(6)), "{1, 2, 3, 4, 5, 6}");
    test(a(l(1), l(2), l(3), l(4), l(5), l(6), l(7)), "{1, 2, 3, 4, 5, 6, 7}");
    test(a(l(1), l(2), l(3), l(4), l(5), l(6), l(7), l(8)), "{1, 2, 3, 4, 5, 6, 7, 8}");
    test(a(l(1), l(2), l(3), l(4), l(5), l(6), l(7), l(8), l(9)), "{1, 2, 3, 4, 5, 6, 7, 8, 9}");
    test(
        a(l(1), l(2), l(3), l(4), l(5), l(6), l(7), l(8), l(9), l(10)),
        "{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}"
    );
  }
  
  @Test
  public void aTestWithExplicitArray() {
    for (int i = 0; i < 10; i++) {
      VariableInitializer[] init = new VariableInitializer[i];
      StringBuilder expected = new StringBuilder("{");
      for (int j = 0; j < i; j++) {
        Literal l = l(j + 1);
        init[j] = l;
        if (j != 0) {
          expected.append(", ");
        }
        expected.append(l.toString());
      }
      test(a(init), expected.append('}').toString());
    }
  }

  @Test
  public void twoDimensions() {
    ArrayInitializer a0 = a(l(1), l(2), l(3));
    test(a(a0, a(l(4), l(5), l(6))), "{{1, 2, 3}, {4, 5, 6}}");
  }

  @Test
  public void moreExpressions() {
    test(a(id("a"), invoke("b")), "{a, b()}");
    test(a(_new(t(Integer.class), l(123))), "{new java.lang.Integer(123)}");
  }

}