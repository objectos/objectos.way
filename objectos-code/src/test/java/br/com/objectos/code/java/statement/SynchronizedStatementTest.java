/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.java.statement;

import static br.com.objectos.code.java.expression.Expressions.id;
import static br.com.objectos.code.java.expression.Expressions.invoke;
import static br.com.objectos.code.java.statement.Block.block;
import static br.com.objectos.code.java.statement.SynchronizedStatement._synchronized;

import br.com.objectos.code.util.AbstractCodeJavaTest;
import org.testng.annotations.Test;

public class SynchronizedStatementTest extends AbstractCodeJavaTest {

  @Test
  public void synchronizedStatement() {
    test(
        _synchronized(id("id"),
            block(
                invoke("exec")
            )
        ),
        "synchronized (id) {",
        "  exec();",
        "}"
    );
  }

}