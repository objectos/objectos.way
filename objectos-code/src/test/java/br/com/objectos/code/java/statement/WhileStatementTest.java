/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.java.statement;

import static br.com.objectos.code.java.expression.Expressions.invoke;
import static br.com.objectos.code.java.statement.Block.block;
import static br.com.objectos.code.java.statement.WhileStatement._while;

import br.com.objectos.code.util.AbstractCodeJavaTest;
import org.testng.annotations.Test;

public class WhileStatementTest extends AbstractCodeJavaTest {

  @Test
  public void whileStatement() {
    test(
        _while(invoke("condition"),
            block(
                invoke("doSomething")
            )
        ),
        "while (condition()) {",
        "  doSomething();",
        "}"
    );
    test(
        _while(invoke("condition"),
            invoke("doSomething")
        ),
        "while (condition()) doSomething();"
    );
  }

}
