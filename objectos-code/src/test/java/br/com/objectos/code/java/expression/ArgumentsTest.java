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
import static br.com.objectos.code.java.element.NewLine.nl;
import static br.com.objectos.code.java.expression.Arguments.args;
import static br.com.objectos.code.java.expression.Expressions.l;

import br.com.objectos.code.util.AbstractCodeJavaTest;
import org.testng.annotations.Test;

public class ArgumentsTest extends AbstractCodeJavaTest {

  @Test(description = "this keyword/reference should be allowed")
  public void _thisTest() {
    test(args(_this()), "(this)");
  }

  @Test(description = ""
      + "arguments only should be rendered in a single line.")
  public void argumentsOnly() {
    test(args(), "()");
    test(args(l(1)), "(1)");
    test(args(l(1), l(2)), "(1, 2)");
    test(args(l(1), l(2), l(3)), "(1, 2, 3)");
    test(args(l(1), l(2), l(3), l(4)), "(1, 2, 3, 4)");
    test(args(l(1), l(2), l(3), l(4), l(5)), "(1, 2, 3, 4, 5)");
    test(args(l(1), l(2), l(3), l(4), l(5), l(6)), "(1, 2, 3, 4, 5, 6)");
    test(args(l(1), l(2), l(3), l(4), l(5), l(6), l(7)), "(1, 2, 3, 4, 5, 6, 7)");
    test(args(l(1), l(2), l(3), l(4), l(5), l(6), l(7), l(8)), "(1, 2, 3, 4, 5, 6, 7, 8)");
    test(args(l(1), l(2), l(3), l(4), l(5), l(6), l(7), l(8), l(9)), "(1, 2, 3, 4, 5, 6, 7, 8, 9)");
    test(
        args(l(1), l(2), l(3), l(4), l(5), l(6), l(7), l(8), l(9), l(10)),
        "(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)"
    );
  }

  @Test(description = ""
      + "Arguments shorthand should honor nl(). It should be double indented.")
  public void newLine() {
    test(
        args(nl()),
        "(",
        ")"
    );
    test(
        args(l(1), nl()),
        "(1",
        ")"
    );
    test(
        args(l(1), nl(), l(2)),
        "(1,",
        "    2)"
    );
    test(
        args(l(1), nl(), l(2), nl()),
        "(1,",
        "    2",
        ")"
    );
    test(
        args(nl(), l(1), nl(), l(2), nl()),
        "(",
        "    1,",
        "    2",
        ")"
    );
  }

}
