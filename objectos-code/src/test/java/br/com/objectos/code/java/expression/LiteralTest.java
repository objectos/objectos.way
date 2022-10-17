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

import static br.com.objectos.code.java.expression.Expressions._false;
import static br.com.objectos.code.java.expression.Expressions._true;
import static br.com.objectos.code.java.expression.Expressions.invoke;
import static br.com.objectos.code.java.expression.Expressions.l;
import static br.com.objectos.code.java.statement.ReturnStatement._return;
import static org.testng.Assert.assertEquals;

import br.com.objectos.code.util.AbstractCodeJavaTest;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

public class LiteralTest extends AbstractCodeJavaTest
    implements
    AdditiveTest.Adapter,
    CalleeTest.Adapter,
    ConditionalTest.Adapter,
    EqualityTest.Adapter,
    MethodReferenceReferenceExpressionTest.Adapter,
    MultiplicativeTest.Adapter,
    RelationalTest.Adapter,
    ShiftTest.Adapter {

  @Override
  public final AdditiveExpression additiveExpression() {
    return l(789L);
  }

  @Test
  public void booleanLiterals() {
    test(_return(_true()), "return true");
    test(_return(_false()), "return false");
  }

  @Override
  public final Callee callee() {
    return l("abc");
  }

  @Override
  public final ConditionalAndExpression conditionalAndExpression() {
    return l('b');
  }

  @Override
  public final ConditionalOrExpression conditionalOrExpression() {
    return conditionalAndExpression();
  }

  @Override
  public final EqualityExpression equalityExpression() {
    return l(String.class);
  }

  @Test
  public void escapeJava() {
    String res = LiteralImpl.escapeJava("\b \t \n \f \r \" \' \\ \u0000");

    assertEquals(res, "\\b \\t \\n \\f \\r \\\" \\' \\\\ \\u0000");
  }

  @Test
  public void literal_boolean() {
    test(lit(l(false)),
        "lit(false)");
  }

  @Test
  public void literal_char() {
    test(lit(l('a')),
        "lit('a')");
  }

  @Test
  public void literal_double() {
    test(lit(l(123.456)),
        "lit(123.456)");
  }

  @Test
  public void literal_float() {
    test(lit(l(123.456F)),
        "lit(123.456F)");
  }

  @Test
  public void literal_int() {
    test(lit(l(123)),
        "lit(123)");
  }

  @Test
  public void literal_long() {
    test(lit(l(123L)),
        "lit(123L)");
  }

  @Test
  public void literal_string() {
    test(lit(l("abc")),
        "lit(\"abc\")");
  }

  @Test
  public void literal_stringWithEscape() {
    test(lit(l(".\t.\"")),
        "lit(\".\\t.\\\"\")");
  }

  @Override
  public final MethodReferenceReferenceExpression methodReferenceReferenceExpression() {
    return l("abc");
  }

  @Override
  public final MultiplicativeExpression multiplicativeExpression() {
    return l(123.45);
  }

  @Override
  public final RelationalExpression relationalExpression() {
    return l(123);
  }

  @Override
  public final ShiftExpression shiftExpression() {
    return l(1);
  }

  @Test
  public void ternary() {
    test(l(true).ternary(invoke("reallyTrue"), invoke("uhOh")),
        "true ? reallyTrue() : uhOh()");
  }

  @Factory
  public Object[] testFactory() {
    return new Object[] {
        AdditiveTest.with(this),
        CalleeTest.with(this),
        ConditionalTest.with(this),
        EqualityTest.with(this),
        MethodReferenceReferenceExpressionTest.with(this),
        MultiplicativeTest.with(this),
        RelationalTest.with(this),
        ShiftTest.with(this)
    };
  }

  private Expression lit(Literal l) {
    return invoke("lit", l);
  }

}
