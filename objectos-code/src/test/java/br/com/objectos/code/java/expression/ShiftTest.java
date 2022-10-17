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
import static br.com.objectos.code.java.expression.Expressions.invoke;
import static br.com.objectos.code.java.expression.Expressions.l;

import br.com.objectos.code.util.AbstractCodeJavaTest;
import org.testng.annotations.Test;

public class ShiftTest extends AbstractCodeJavaTest {

  public interface Adapter {

    ShiftExpression shiftExpression();

  }

  private final Adapter adapter;

  private ShiftTest(Adapter adapter) {
    this.adapter = adapter;
  }

  public static ShiftTest with(Adapter adapter) {
    return new ShiftTest(adapter);
  }

  @Test
  public void leftShiftTest() {
    ShiftExpression e = shiftExpression();
    test(e.leftShift(l(1)), e + " << 1");
    test(e.leftShift(invoke("shift")), e + " << shift()");
  }

  @Test
  public void rightShiftTest() {
    ShiftExpression e = shiftExpression();
    test(e.rightShift(l(2)), e + " >> 2");
    test(e.rightShift(invoke("shift")), e + " >> shift()");
  }

  @Test
  public void unsignedRightShift() {
    ShiftExpression e = shiftExpression();
    test(e.unsignedRightShift(id("b")), e + " >>> b");
  }

  private ShiftExpression shiftExpression() {
    return adapter.shiftExpression();
  }

}
