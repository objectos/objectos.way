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
import static br.com.objectos.code.java.statement.DoStatement._do;
import static br.com.objectos.code.java.statement.DoStatement._while;

import br.com.objectos.code.util.AbstractCodeJavaTest;
import org.testng.annotations.Test;

public class DoStatementTest extends AbstractCodeJavaTest {

  @Test
  public void fromCode() {
    test(
        _do(
            block(
                invoke("a")
            ),
            _while(invoke("condition"))
        ),
        "do {",
        "  a();",
        "} while (condition())"
    );
    test(
        _do(
            invoke("a"),
            _while(invoke("condition"))
        ),
        "do a(); while (condition())"
    );
  }

}