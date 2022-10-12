/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.java.statement;

import static br.com.objectos.code.java.expression.Expressions.hint;
import static br.com.objectos.code.java.expression.Expressions.id;
import static br.com.objectos.code.java.expression.NewClass._new;
import static br.com.objectos.code.java.statement.ReturnStatement._return;
import static br.com.objectos.code.java.type.NamedTypes.t;

import br.com.objectos.code.java.expression.Identifier;
import br.com.objectos.code.util.AbstractCodeJavaTest;
import java.util.ArrayList;
import org.testng.annotations.Test;

public class ReturnStatementTest extends AbstractCodeJavaTest {

  @Test
  public void returnStatement() {
    test(_return("name"),
        "return name");
  }

  @Test
  public void returnStatement_expression() {
    test(_return(_new(t(ArrayList.class), hint())),
        "return new java.util.ArrayList<>()");
  }

  @Test
  public void returnStatement_expressionWithNewLines() {
    Identifier that = id("that");
    test(
        _return(
            id("first").invoke("equals", that.invoke("first")).nl()
                .and(id("second").invoke("equals", that.invoke("second"))).nl()
                .and(id("third").invoke("equals", that.invoke("third")))
        ),
        "return first.equals(that.first())",
        "    && second.equals(that.second())",
        "    && third.equals(that.third())");
  }

}
