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

import static br.com.objectos.code.java.element.NewLine.nl;
import static br.com.objectos.code.java.expression.Expressions.hint;
import static br.com.objectos.code.java.expression.Expressions.id;
import static br.com.objectos.code.java.expression.Expressions.invoke;
import static br.com.objectos.code.java.expression.Expressions.l;
import static br.com.objectos.code.java.type.NamedTypes.t;
import static br.com.objectos.code.java.type.NamedTypes.tvar;

import br.com.objectos.code.java.element.Keywords;
import br.com.objectos.code.java.type.NamedType;
import br.com.objectos.code.util.AbstractCodeJavaTest;
import java.util.Arrays;
import java.util.Collections;
import objectos.util.UnmodifiableList;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

public class MethodInvocationTest extends AbstractCodeJavaTest
    implements
    AdditiveTest.Adapter,
    ArrayReferenceExpressionTest.Adapter,
    CalleeTest.Adapter,
    ConditionalTest.Adapter,
    EqualityTest.Adapter,
    FieldAccessReferenceExpressionTest.Adapter,
    MethodReferenceReferenceExpressionTest.Adapter,
    MultiplicativeTest.Adapter,
    PostfixExpressionTest.Adapter,
    RelationalTest.Adapter,
    ShiftTest.Adapter {

  @Override
  public final AdditiveExpression additiveExpression() {
    return multiplicativeExpression();
  }

  @Override
  public final ArrayReferenceExpression arrayReferenceExpression() {
    return invoke("a", id("b"));
  }

  @Override
  public final Callee callee() {
    return invoke("foo", id("bar"));
  }

  @Override
  public final ConditionalAndExpression conditionalAndExpression() {
    return invoke("someCondition");
  }

  @Override
  public final ConditionalOrExpression conditionalOrExpression() {
    return conditionalAndExpression();
  }

  @Override
  public final EqualityExpression equalityExpression() {
    return invoke("reference");
  }

  @Override
  public final FieldAccessReferenceExpression fieldAccessReferenceExpression() {
    return invoke("fieldAccess");
  }

  @Test
  public void fromCalleeInvokeTest() {
    test(
        invoke(id("foo"), "m0"),
        "foo.m0()"
    );
    test(
        invoke(id("foo"), "m1", l(1)),
        "foo.m1(1)"
    );
    test(
        invoke(id("foo"), "m2", l(1), l(2)),
        "foo.m2(1, 2)"
    );
    test(
        invoke(id("foo"), "m3", l(1), l(2), l(3)),
        "foo.m3(1, 2, 3)"
    );
    test(
        invoke(id("foo"), "m4", l(1), l(2), l(3), l(4)),
        "foo.m4(1, 2, 3, 4)"
    );
    test(
        invoke(
            id("foo"), "m9",
            Arrays.asList(l(1), l(2), l(3), l(4), l(5), l(6), l(7), l(8), l(9))
        ),
        "foo.m9(1, 2, 3, 4, 5, 6, 7, 8, 9)"
    );
    test(
        invoke(Keywords._super(), "fromSuper"),
        "super.fromSuper()"
    );
  }

  @Test
  public void givenInvocationWithArgumentsInNewLinesThenArgsShouldBeDoubleIndented() {
    test(
        invoke("method", nl(), invoke("m1"), nl(), invoke("m2"), nl()),
        "method(",
        "    m1(),",
        "    m2()",
        ")"
    );
    test(
        invoke("a", id("b")).invoke("c", nl(), invoke("d"), nl()),
        "a(b).c(",
        "    d()",
        ")"
    );
  }

  @Test
  public void invokeTest() {
    test(
        invoke("m0"),
        "m0()"
    );
    test(
        invoke("m1", l(1)),
        "m1(1)"
    );
    test(
        invoke("m2", l(1), l(2)),
        "m2(1, 2)"
    );
    test(
        invoke("m3", l(1), l(2), l(3)),
        "m3(1, 2, 3)"
    );
    test(
        invoke("m4", l(1), l(2), l(3), l(4)),
        "m4(1, 2, 3, 4)"
    );
    test(
        invoke("m9",
            Arrays.asList(l(1), l(2), l(3), l(4), l(5), l(6), l(7), l(8), l(9))
        ),
        "m9(1, 2, 3, 4, 5, 6, 7, 8, 9)"
    );
  }

  @Test
  public void issue114() {
    test(
        invoke("method", nl(), l("indent-me"), nl()),
        "method(",
        "    \"indent-me\"",
        ")"
    );
  }

  @Override
  public final MethodReferenceReferenceExpression methodReferenceReferenceExpression() {
    return invoke("ref");
  }

  @Override
  public final MultiplicativeExpression multiplicativeExpression() {
    return invoke("multiply");
  }

  @Override
  public final PostfixExpression postfixExpression() {
    return invoke("postfix");
  }

  @Override
  public final RelationalExpression relationalExpression() {
    return invoke("rel");
  }

  @Override
  public final ShiftExpression shiftExpression() {
    return invoke("shift");
  }

  @Factory
  public Object[] testFactory() {
    return new Object[] {
        AdditiveTest.with(this),
        ArrayReferenceExpressionTest.with(this),
        CalleeTest.with(this),
        ConditionalTest.with(this),
        EqualityTest.with(this),
        FieldAccessReferenceExpressionTest.with(this),
        MethodReferenceReferenceExpressionTest.with(this),
        MultiplicativeTest.with(this),
        PostfixExpressionTest.with(this),
        RelationalTest.with(this),
        ShiftTest.with(this)
    };
  }

  @Test
  public void withTypeWitness() {
    test(
        invoke(id("foo"), hint(t(String.class)), "bar"),
        "foo.<java.lang.String> bar()"
    );
    test(
        invoke(Keywords._super(), hint(tvar("E")), "fromSuper"),
        "super.<E> fromSuper()"
    );

    Iterable<? extends NamedType> types;
    types = UnmodifiableList.of(tvar("E1"), tvar("E2"), tvar("E3"), tvar("E4"));

    test(
        invoke(t(Collections.class), hint(types), "foo"),
        "java.util.Collections.<E1, E2, E3, E4> foo()"
    );
  }

}
