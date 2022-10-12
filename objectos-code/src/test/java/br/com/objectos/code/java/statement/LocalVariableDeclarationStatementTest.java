/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.java.statement;

import static br.com.objectos.code.java.expression.Expressions.invoke;
import static br.com.objectos.code.java.statement.Statements._var;

import br.com.objectos.code.util.AbstractCodeJavaTest;
import org.testng.annotations.Test;

public class LocalVariableDeclarationStatementTest extends AbstractCodeJavaTest {

  @Test
  public void localVariableDeclarationStatement() {
    test(_var(String.class, "iAmNotInit"),
        "java.lang.String iAmNotInit");
  }

  @Test
  public void localVariableDeclarationStatementWithInit() {
    test(_var(String.class, "foo", invoke("compute")),
        "java.lang.String foo = compute()");
  }

}