/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.java.statement;

import static br.com.objectos.code.java.element.Keywords._break;
import static br.com.objectos.code.java.expression.Expressions.empty;
import static br.com.objectos.code.java.expression.Expressions.id;
import static br.com.objectos.code.java.expression.Expressions.invoke;
import static br.com.objectos.code.java.expression.Expressions.l;
import static br.com.objectos.code.java.expression.Expressions.lt;
import static br.com.objectos.code.java.expression.Expressions.postInc;
import static br.com.objectos.code.java.statement.ForStatement._for;
import static br.com.objectos.code.java.statement.Statements._var;
import static br.com.objectos.code.java.type.NamedTypes._int;
import static br.com.objectos.code.java.type.NamedTypes.t;
import static br.com.objectos.code.java.type.NamedTypes.tvar;

import br.com.objectos.code.java.expression.Identifier;
import br.com.objectos.code.java.type.NamedParameterized;
import br.com.objectos.code.java.type.NamedTypeVariable;
import br.com.objectos.code.util.AbstractCodeJavaTest;
import java.util.Iterator;
import org.testng.annotations.Test;

public class ForStatementTest extends AbstractCodeJavaTest {

  @Test
  public void basic() {
    Identifier i = id("i");
    test(
        _for(
            _var(_int(), i, l(0)),
            lt(i, l(20)),
            postInc(i),

            invoke("doSomething", i)
        ),
        "for (int i = 0; i < 20; i++) {",
        "  doSomething(i);",
        "}"
    );
    test(
        _for(
            _var(_int(), i, l(0)),
            lt(i, l(20)),
            postInc(i),

            invoke("m1", i),
            invoke("m2", i)
        ),
        "for (int i = 0; i < 20; i++) {",
        "  m1(i);",
        "  m2(i);",
        "}"
    );
  }

  @Test(
      description = ""
          + "The empty section should be rendered without whitespace"
  )
  public void basicWithEmpty() {
    NamedParameterized itE = t(t(Iterator.class), tvar("E"));

    Identifier it = id("it");
    Identifier next = id("next");
    test(
        _for(
            _var(itE, it, id("list").invoke("iterator")),
            it.invoke("hasNext"),
            empty(),

            _var(NamedTypeVariable.of("E"), next, it.invoke("next")),
            invoke("doSomething", next)
        ),
        "for (java.util.Iterator<E> it = list.iterator(); it.hasNext();) {",
        "  E next = it.next();",
        "  doSomething(next);",
        "}"
    );
    test(
        _for(
            empty(),
            empty(),
            empty(),

            _break()
        ),
        "for (;;) {",
        "  break;",
        "}"
    );
  }

  @Test(enabled = false)
  public void enhanced() {
    Identifier o = id("o");
    test(
        _for(
            Object.class, o, id("iterable"),

            invoke("doSomething", o)
        ),
        "for (java.lang.Object o : iterable) {",
        "  doSomething(o);",
        "}"
    );
    test(
        _for(
            Object.class, o, id("iterable"),

            invoke("doSomething", o)
        ),
        "for (java.lang.Object o : iterable) doSomething(o);"
    );
  }

}