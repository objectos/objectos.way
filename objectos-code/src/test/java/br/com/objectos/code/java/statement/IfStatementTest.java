/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.java.statement;

import static br.com.objectos.code.java.element.Keywords._else;
import static br.com.objectos.code.java.element.Keywords._this;
import static br.com.objectos.code.java.element.NewLine.nl;
import static br.com.objectos.code.java.expression.Expressions._null;
import static br.com.objectos.code.java.expression.Expressions.assign;
import static br.com.objectos.code.java.expression.Expressions.id;
import static br.com.objectos.code.java.expression.Expressions.invoke;
import static br.com.objectos.code.java.expression.Expressions.l;
import static br.com.objectos.code.java.statement.IfStatement._if;
import static br.com.objectos.code.java.statement.ReturnStatement._return;
import static br.com.objectos.code.java.statement.Statements._var;
import static br.com.objectos.code.java.type.NamedTypes._int;

import br.com.objectos.code.util.AbstractCodeJavaTest;
import org.testng.annotations.Test;

public class IfStatementTest extends AbstractCodeJavaTest {

  @Test
  public void _if_then_block() {
    test(
        _if(id("obj").eq(_this()),
            _return(l(false))
        ),
        "if (obj == this) {",
        "  return false;",
        "}"
    );
    test(
        _if(id("k").eq(_null()),
            assign(id("k"), invoke("newKeyword"))
        ),
        "if (k == null) {",
        "  k = newKeyword();",
        "}"
    );
    test(
        _if(invoke("condition"),
            _var(_int(), id("factor"), l(10)),
            nl(),
            id("temp").receive(id("temp").multiply(id("factor")))
        ),
        "if (condition()) {",
        "  int factor = 10;",
        "",
        "  temp = temp * factor;",
        "}"
    );
  }

  @Test
  public void _if_then_block_else_block() {
    test(
        _if(invoke("condition"),
            invoke("onTrue"),

            _else(),
            invoke("onFalse")
        ),
        "if (condition()) {",
        "  onTrue();",
        "} else {",
        "  onFalse();",
        "}"
    );
  }

}