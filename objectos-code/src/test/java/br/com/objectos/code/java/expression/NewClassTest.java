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

import static br.com.objectos.code.java.expression.Expressions.hint;
import static br.com.objectos.code.java.expression.Expressions.l;
import static br.com.objectos.code.java.expression.NewClass._new;
import static br.com.objectos.code.java.type.NamedTypes.t;
import static br.com.objectos.code.java.type.NamedTypes.tvar;

import br.com.objectos.code.java.type.NamedClass;
import br.com.objectos.code.java.type.NamedTypeVariable;
import br.com.objectos.code.util.AbstractCodeJavaTest;
import java.util.Arrays;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

public class NewClassTest extends AbstractCodeJavaTest
    implements
    MethodReferenceReferenceExpressionTest.Adapter {

  @Factory
  public Object[] testFactory() {
    return new Object[] {
        MethodReferenceReferenceExpressionTest.with(this)
    };
  }

  @Override
  public final MethodReferenceReferenceExpression methodReferenceReferenceExpression() {
    return _new(t(getClass()));
  }

  @Test
  public void newTest() {
    NamedClass subject;
    subject = TESTING_CODE.nestedClass("Subject");

    test(
        _new(subject),
        "new testing.code.Subject()"
    );
    test(
        _new(subject, l(1)),
        "new testing.code.Subject(1)"
    );
    test(
        _new(subject, l(1), l(2)),
        "new testing.code.Subject(1, 2)"
    );
    test(
        _new(subject, l(1), l(2), l(3)),
        "new testing.code.Subject(1, 2, 3)"
    );
    test(
        _new(subject, l(1), l(2), l(3), l(4)),
        "new testing.code.Subject(1, 2, 3, 4)"
    );
    test(
        _new(
            subject,
            Arrays.asList(l(1), l(2), l(3), l(4), l(5), l(6), l(7), l(8), l(9))
        ),
        "new testing.code.Subject(1, 2, 3, 4, 5, 6, 7, 8, 9)"
    );
    test(
        _new(
            subject,
            Arguments.args(l(1), l(2), l(3), l(4), l(5), l(6), l(7), l(8), l(9))
        ),
        "new testing.code.Subject(1, 2, 3, 4, 5, 6, 7, 8, 9)"
    );
    test(
        _new(
            subject,
            Arguments.builder()
                .addNewLine()
                .addArgument(l(1))
                .addNewLine()
                .build()
        ),
        "new testing.code.Subject(",
        "    1",
        ")"
    );
  }

  @Test
  public void newTestWithDiamondOperator() {
    NamedClass diamond;
    diamond = TESTING_CODE.nestedClass("Diamond");

    test(
        _new(diamond, hint()),
        "new testing.code.Diamond<>()"
    );
    test(
        _new(diamond, hint(), l(1)),
        "new testing.code.Diamond<>(1)"
    );
    test(
        _new(diamond, hint(), l(1), l(2)),
        "new testing.code.Diamond<>(1, 2)"
    );
    test(
        _new(diamond, hint(), l(1), l(2), l(3)),
        "new testing.code.Diamond<>(1, 2, 3)"
    );
    test(
        _new(diamond, hint(), l(1), l(2), l(3), l(4)),
        "new testing.code.Diamond<>(1, 2, 3, 4)"
    );
    test(
        _new(
            diamond,
            hint(),
            Arrays.asList(l(1), l(2), l(3), l(4), l(5), l(6), l(7), l(8), l(9))
        ),
        "new testing.code.Diamond<>(1, 2, 3, 4, 5, 6, 7, 8, 9)"
    );
    test(
        _new(
            diamond,
            hint(),
            Arguments.args(l(1), l(2), l(3), l(4), l(5), l(6), l(7), l(8), l(9))
        ),
        "new testing.code.Diamond<>(1, 2, 3, 4, 5, 6, 7, 8, 9)"
    );
  }

  @Test
  public void newTestWithTypeArguments() {
    NamedClass generic;
    generic = TESTING_CODE.nestedClass("Generic");

    NamedTypeVariable e1 = tvar("E1");

    NamedTypeVariable e2 = tvar("E2");

    test(
        _new(generic, hint(e1, e2)),
        "new testing.code.Generic<E1, E2>()"
    );
    test(
        _new(generic, hint(e1, e2), l(1)),
        "new testing.code.Generic<E1, E2>(1)"
    );
    test(
        _new(generic, hint(e1, e2), l(1), l(2)),
        "new testing.code.Generic<E1, E2>(1, 2)"
    );
    test(
        _new(generic, hint(e1, e2), l(1), l(2), l(3)),
        "new testing.code.Generic<E1, E2>(1, 2, 3)"
    );
    test(
        _new(generic, hint(e1, e2), l(1), l(2), l(3), l(4)),
        "new testing.code.Generic<E1, E2>(1, 2, 3, 4)"
    );
    test(
        _new(
            generic,
            hint(e1, e2),
            Arrays.asList(l(1), l(2), l(3), l(4), l(5), l(6), l(7), l(8), l(9))
        ),
        "new testing.code.Generic<E1, E2>(1, 2, 3, 4, 5, 6, 7, 8, 9)"
    );
    test(
        _new(
            generic,
            hint(e1, e2),
            Arguments.args(l(1), l(2), l(3), l(4), l(5), l(6), l(7), l(8), l(9))
        ),
        "new testing.code.Generic<E1, E2>(1, 2, 3, 4, 5, 6, 7, 8, 9)"
    );
  }

}
